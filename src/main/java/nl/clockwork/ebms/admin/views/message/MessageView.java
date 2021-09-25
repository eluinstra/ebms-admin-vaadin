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

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.NonNull;
import lombok.val;
import nl.clockwork.ebms.admin.components.BackButton;
import nl.clockwork.ebms.admin.components.WithElement;
import nl.clockwork.ebms.admin.model.DeliveryLog;
import nl.clockwork.ebms.admin.model.DeliveryTask;
import nl.clockwork.ebms.admin.model.EbMSAttachment;
import nl.clockwork.ebms.admin.model.EbMSMessage;
import nl.clockwork.ebms.admin.views.MainLayout;
import nl.clockwork.ebms.admin.views.WithBean;

@Route(value = "message/:messageId", layout = MainLayout.class)
@PageTitle("Message")
public class MessageView extends VerticalLayout implements BeforeEnterObserver, WithBean, WithElement
{
	public MessageView()
	{
		add(new H1(getTranslation("message")));
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event)
	{
		val messageId = event.getRouteParameters().get("messageId").orElse(null);
		if (messageId == null)
			add(new Text("Message not found"));
		else
		{
			val message = getEbMSAdminDAO().findMessage(messageId);
			add(createForm(message));
			add(new BackButton(getTranslation("cmd.back")));
		}
	}

	private Component createForm(final EbMSMessage message)
	{
		val result = new FormLayout();
		result.setSizeFull();
		result.add(createField(getTranslation("lbl.messageId"),message.getMessageId()));
		result.add(createField(getTranslation("lbl.messageNr"),String.valueOf(message.getMessageNr())));
		result.add(createField(getTranslation("lbl.conversationId"),message.getConversationId()));
		result.add(createField(getTranslation("lbl.refToMessageId"),message.getRefToMessageId()));
		result.add(createField(getTranslation("lbl.timestamp"),message.getTimestamp()));
		result.add(createField(getTranslation("lbl.cpaId"),message.getCpaId()));
		result.add(createField(getTranslation("lbl.fromPartyId"),message.getFromPartyId()));
		result.add(createField(getTranslation("lbl.fromRole"),message.getFromRole()));
		result.add(createField(getTranslation("lbl.toPartyId"),message.getToPartyId()));
		result.add(createField(getTranslation("lbl.toRole"),message.getToRole()));
		result.add(createField(getTranslation("lbl.service"),message.getService()));
		result.add(createField(getTranslation("lbl.action"),message.getAction()));
		//viewMessageError
		result.add(createField(getTranslation("lbl.status"),message.getStatus().name()));
		result.add(createField(getTranslation("lbl.statusTime"),message.getStatusTime()));
		if (message.getDeliveryTask() != null)
			result.add(createDeliveryTask(message.getDeliveryTask()));
		result.add(createDeliveryLogs(message.getDeliveryLogs()));
		result.add(createAttachments(message.getAttachments()));
		return result;
	}

	private Component createField(final String label, final Instant value)
	{
		return createField(label,value == null ? null : value.toString());
	}

	private Component createField(final String label, final String value)
	{
		val result = new HorizontalLayout();
		setColSpan(result,2);//
		result.add(createLabel(label));
		result.add(new Span(value));
		return result;
	}

	private Label createLabel(final String label)
	{
		val result = new Label(label);
		result.getElement().getStyle().set("font-weight","bold");
		return result;
	}

	private Component createDeliveryTask(@NonNull DeliveryTask deliveryTask)
	{
		val result = new VerticalLayout();
		result.add(createLabel(getTranslation("lbl.deliveryTasks")));
		result.add(createDeliveryTaskTable(Arrays.asList(deliveryTask)));
		return result;
	}

	private Component createDeliveryTaskTable(@NonNull List<DeliveryTask> deliveryTasks)
	{
		val result = new Grid<DeliveryTask>(DeliveryTask.class,false);
		result.setHeightByRows(true);
		result.setItems(deliveryTasks);
		result.addColumn("timeToLive").setHeader(getTranslation("lbl.timeToLive"));
		result.addColumn("timestamp").setHeader(getTranslation("lbl.timestamp"));
		result.addColumn("retries").setHeader(getTranslation("lbl.retries"));
		return result;
	}

	private Component createDeliveryLogs(@NonNull List<DeliveryLog> deliveryLogs)
	{
		val result = new VerticalLayout();
		setColSpan(result,2);//
		result.add(createLabel(getTranslation("lbl.deliveryLog")));
		result.add(createDeliveryLogTable(deliveryLogs));
		return result;
	}

	private Component createDeliveryLogTable(@NonNull List<DeliveryLog> deliveryLogs)
	{
		val result = new Grid<DeliveryLog>(DeliveryLog.class,false);
		result.setHeightByRows(true);
		result.setItems(deliveryLogs);
		result.addColumn("timestamp").setHeader(getTranslation("lbl.timestamp"));
		result.addColumn("uri").setHeader(getTranslation("lbl.uri"));
		result.addColumn("status").setHeader(getTranslation("lbl.status"));
		//result.addColumn("errorMessage");
		return result;
	}

	private Component createAttachments(@NonNull List<EbMSAttachment> attachments)
	{
		val result = new VerticalLayout();
		setColSpan(result,2);//
		result.add(createLabel(getTranslation("lbl.attachments")));
		result.add(createAttachmentsTable(attachments));
		return result;
	}

	private Component createAttachmentsTable(@NonNull List<EbMSAttachment> attachments)
	{
		val result = new Grid<EbMSAttachment>(EbMSAttachment.class,false);
		result.setHeightByRows(true);
		result.setItems(attachments);
		result.addColumn("name").setHeader(getTranslation("lbl.name"));
		result.addColumn("contentId").setHeader(getTranslation("lbl.contentId"));
		result.addColumn("contentType").setHeader(getTranslation("lbl.contentType"));
		// result.addColumn("content");
		return result;
	}

	private TextField createTextField(final String label, final String value)
	{
		val result = new TextField();
		setColSpan(result,2);//
		result.setReadOnly(true);
		result.setLabel(label);
		result.setValue(value);
		return result;
	}

	private IntegerField createIntegerField(final String label, final Integer value)
	{
		val result = new IntegerField();
		setColSpan(result,2);//
		result.setReadOnly(true);
		result.setLabel(label);
		result.setValue(value);
		return result;
	}

}
