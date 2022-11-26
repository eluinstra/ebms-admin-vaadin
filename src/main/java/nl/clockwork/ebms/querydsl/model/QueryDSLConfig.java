/*
 * Copyright 2021 Ordina
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.clockwork.ebms.querydsl.model;

import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.querydsl.sql.HSQLDBTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.spring.SpringConnectionProvider;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import com.zaxxer.hikari.HikariDataSource;

import bitronix.tm.resource.jdbc.PoolingDataSource;
import lombok.AccessLevel;
import lombok.val;
import lombok.experimental.FieldDefaults;
import nl.clockwork.ebms.querydsl.CachedOutputStreamType;
import nl.clockwork.ebms.querydsl.CollaborationProtocolAgreementType;
import nl.clockwork.ebms.querydsl.DeliveryTaskStatusType;
import nl.clockwork.ebms.querydsl.DocumentType;
import nl.clockwork.ebms.querydsl.EbMSMessageEventTypeType;
import nl.clockwork.ebms.querydsl.EbMSMessageStatusType;
import nl.clockwork.ebms.querydsl.X509CertificateType;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QueryDSLConfig
{
	@Bean
	public SQLQueryFactory queryFactory(DataSource dataSource, com.querydsl.sql.Configuration configuration)
	{
		val provider = new SpringConnectionProvider(dataSource);
		return new SQLQueryFactory(configuration,provider);
	}

	@Bean
	public com.querydsl.sql.Configuration querydslConfiguration(SQLTemplates templates)
	{
		// val templates = sqlTemplates(dataSource);
		val result = new com.querydsl.sql.Configuration(templates);
		result.setExceptionTranslator(new SpringExceptionTranslator());
		result.register("cpa","cpa",new CollaborationProtocolAgreementType(Types.CLOB));
		result.register("certificate_mapping","source",new X509CertificateType(Types.BLOB));
		result.register("certificate_mapping","destination",new X509CertificateType(Types.BLOB));
		result.register("delivery_log","status",new DeliveryTaskStatusType(Types.SMALLINT));
		result.register("ebms_message_event","event_type",new EbMSMessageEventTypeType(Types.SMALLINT));
		result.register("ebms_message","content",new DocumentType(Types.CLOB));
		result.register("ebms_message","status",new EbMSMessageStatusType(Types.SMALLINT));
		result.register("ebms_attachment","content",new CachedOutputStreamType(Types.BLOB));
		return result;
	}

	@Bean
	public SQLTemplates sqlTemplates(DataSource dataSource)
	{
		return createSQLTemplates(dataSource);
	}

	private SQLTemplates createSQLTemplates(DataSource dataSource)
	{
		// val driverClassName = AbstractDAOFactory.getDriverClassName(dataSource) == null ? "db2" : AbstractDAOFactory.getDriverClassName(dataSource);
		// return Match(driverClassName).of(
		// 		Case($(contains("db2")),o -> DB2Templates.builder().build()),
		// 		Case($(contains("h2")),o -> H2Templates.builder().build()),
		// 		Case($(contains("hsqldb")),o -> HSQLDBTemplates.builder().build()),
		// 		Case($(contains("mariadb")),o -> MariaDBTemplates.builder().build()),
		// 		Case($(contains("oracle")),o -> OracleTemplates.builder().build()),
		// 		Case($(contains("postgresql")),o -> PostgreSQLTemplates.builder().build()),
		// 		Case($(contains("sqlserver")),o -> SQLServer2012Templates.builder().build()),
		// 		Case($(),o -> {
		// 			throw new RuntimeException("Driver class name " + driverClassName + " not recognized!");
		// 		}));
		return HSQLDBTemplates.builder().build();
	}

	public static String getDriverClassName(DataSource dataSource)
	{
		return dataSource instanceof HikariDataSource 
			? ((HikariDataSource)dataSource).getDriverClassName()
			: dataSource  instanceof PoolingDataSource 
				? ((PoolingDataSource)dataSource).getClassName()
				: ((AtomikosDataSourceBean)dataSource).getXaDataSourceClassName();
	}

}
