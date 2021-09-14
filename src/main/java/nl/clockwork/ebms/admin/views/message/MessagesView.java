/**
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

import static nl.clockwork.ebms.admin.views.BeanProvider.getEbMSAdminDAO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.StreamResource;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.cxf.io.CachedOutputStream;

import lombok.val;
import nl.clockwork.ebms.admin.components.DownloadButton;
import nl.clockwork.ebms.admin.components.RouterLink;
import nl.clockwork.ebms.admin.dao.EbMSDAO;
import nl.clockwork.ebms.admin.model.EbMSMessage;
import nl.clockwork.ebms.admin.views.MainLayout;

@Route(value = "message", layout = MainLayout.class)
@PageTitle("Messages")
public class MessagesView extends VerticalLayout
{
	public MessagesView()
	{
		add(new H1(getTranslation("messages")));
		val ebMSDAO = getEbMSAdminDAO();
		add(createMessageGrid(ebMSDAO));
		add(new DownloadButton(getTranslation("cmd.download"),new StreamResource("messages.csv",() -> this.createCsv(ebMSDAO))));
	}

	private Component createMessageGrid(EbMSDAO ebMSDAO)
	{
		val result = new Grid<EbMSMessage>(EbMSMessage.class,false);
		result.setDataProvider(createMessageDataProvider(ebMSDAO));
		result.setSelectionMode(SelectionMode.NONE);
		result.addColumn(new ComponentRenderer<>(message -> createRouterLink(message,MessageView.class))).setHeader(getTranslation("lbl.messageId"));
		result.addColumn("messageNr").setHeader(getTranslation("lbl.messageNr"));
		result.addColumn("conversationId").setHeader(getTranslation("lbl.conversationId"));
		result.addColumn("refToMessageId").setHeader(getTranslation("lbl.refToMessageId"));
		result.addColumn("timestamp").setHeader(getTranslation("lbl.timestamp"));
		result.addColumn("cpaId").setHeader(getTranslation("lbl.cpaId"));
		result.addColumn("fromPartyId").setHeader(getTranslation("lbl.fromPartyId"));
		result.addColumn("fromRole").setHeader(getTranslation("lbl.fromRole"));
		result.addColumn("toPartyId").setHeader(getTranslation("lbl.toPartyId"));
		result.addColumn("toRole").setHeader(getTranslation("lbl.toRole"));
		result.addColumn("service").setHeader(getTranslation("lbl.service"));
		result.addColumn("action").setHeader(getTranslation("lbl.action"));
		result.addColumn("status").setHeader(getTranslation("lbl.status"));
		result.addColumn("statusTime").setHeader(getTranslation("lbl.statusTime"));
		return result;
	}

	private DataProvider<EbMSMessage,?> createMessageDataProvider(EbMSDAO ebMSDAO)
	{
		return DataProvider.fromCallbacks(query -> ebMSDAO.selectMessages(null,query.getOffset(),query.getLimit()).stream(),
				query -> ((Long)ebMSDAO.countMessages(null)).intValue());
	}

	private RouterLink createRouterLink(EbMSMessage message, Class<? extends Component> component)
	{
		return new RouterLink(message.getMessageId(),component,new RouteParameters("messageId",message.getMessageId()));
	}

	private InputStream createCsv(EbMSDAO ebMSAdminDAO)
	{
		try (val output = new CachedOutputStream(); val printer = new CSVPrinter(new OutputStreamWriter(output),CSVFormat.DEFAULT))
		{
			ebMSAdminDAO.printMessagesToCSV(printer,null);
			return output.getInputStream();
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

}
