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
package nl.clockwork.ebms.admin.views.message;

import static com.vaadin.flow.component.grid.Grid.SelectionMode.NONE;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;

import lombok.val;
import nl.clockwork.ebms.admin.components.WithDate;
import nl.clockwork.ebms.admin.model.EbMSMessage;
import nl.clockwork.ebms.admin.views.MainLayout;
import nl.clockwork.ebms.admin.views.Utils;
import nl.clockwork.ebms.admin.views.WithBean;

@Route(value = "traffic", layout = MainLayout.class)
@PageTitle("Traffic")
@CssImport(themeFor = "vaadin-grid", value = "./styles/dynamic-message-status-color.css")
public class TrafficView extends VerticalLayout implements WithBean, WithDate
{
	public TrafficView()
	{
		setSizeFull();
		add(new H1(getTranslation("traffic")));
		val messageFilter = EbMSMessageFilter.ebMSMessageFilterBuilder()
				.messageNr(0)
				.serviceMessage(false)
				.build();
		val dataProvider = createMessageDataProvider(messageFilter);
		add(createSearchFilterDetails(getTranslation("messageFilter"),messageFilter,dataProvider));
		add(createMessageGrid(dataProvider));
	}

	private DataProvider<EbMSMessage,?> createMessageDataProvider(EbMSMessageFilter messageFilter)
	{
		return DataProvider.fromCallbacks(query -> getEbMSAdminDAO().selectMessages(messageFilter,query.getOffset(),query.getLimit()).stream(),query -> ((Long)getEbMSAdminDAO().countMessages(messageFilter)).intValue());
	}

	private Details createSearchFilterDetails(String label, EbMSMessageFilter messageFilter, DataProvider<EbMSMessage, ?> dataProvider)
	{
		val result = new Details();
		result.setSummaryText(label);
		result.setContent(new SearchFilter(messageFilter,dataProvider,() -> result.setOpened(false)));
		return result;
	}

	private Component createMessageGrid(DataProvider<EbMSMessage,?> dataProvider)
	{
		val result = new Grid<EbMSMessage>(EbMSMessage.class,false);
		result.setDataProvider(dataProvider);
		result.setSelectionMode(NONE);
		result.addColumn(new ComponentRenderer<>(message -> createRouterLink(message,MessageView.class))).setHeader(getTranslation("lbl.messageId")).setAutoWidth(true).setFrozen(true);
		result.addColumn("conversationId").setHeader(getTranslation("lbl.conversationId")).setAutoWidth(true);
		result.addColumn(new LocalDateTimeRenderer<>(m -> toLocalDateTime.apply(m.getTimestamp()),DISPLAY_DATE_TIME_FORMATTER)).setHeader(getTranslation("lbl.timestamp")).setAutoWidth(true);
		result.addColumn("cpaId").setHeader(getTranslation("lbl.cpaId")).setAutoWidth(true);
		result.addColumn("fromRole").setHeader(getTranslation("lbl.fromRole")).setAutoWidth(true);
		result.addColumn("toRole").setHeader(getTranslation("lbl.toRole")).setAutoWidth(true);
		result.addColumn("service").setHeader(getTranslation("lbl.service")).setAutoWidth(true);
		result.addColumn("action").setHeader(getTranslation("lbl.action")).setAutoWidth(true);
		result.addColumn("status").setHeader(getTranslation("lbl.status"))
				.setAutoWidth(true)
				.setClassNameGenerator(message -> Utils.getTableCellCssClass(message.getStatus()));
		result.addColumn(new LocalDateTimeRenderer<>(m -> m.getStatusTime() == null ? null : toLocalDateTime.apply(m.getStatusTime()),DISPLAY_DATE_TIME_FORMATTER))
				.setHeader(getTranslation("lbl.statusTime"))
				.setAutoWidth(true);
		result.setClassNameGenerator(message -> Utils.getTableRowCssClass(message.getStatus()));
		return result;
	}

	private RouterLink createRouterLink(EbMSMessage message, Class<? extends Component> component)
	{
		return new RouterLink(message.getMessageId(),component,new RouteParameters("messageId",message.getMessageId()));
	}

}
