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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
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

import lombok.val;
import nl.clockwork.ebms.admin.components.BackButton;
import nl.clockwork.ebms.admin.model.EbMSMessage;
import nl.clockwork.ebms.admin.views.MainLayout;

@Route(value = "message/:messageId", layout = MainLayout.class)
@PageTitle("Message")
public class MessageView extends VerticalLayout implements BeforeEnterObserver
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
		return result;
	}

	private Component createField(final String label, final String value)
	{
		val result = new HorizontalLayout();
		result.addClassName("vaadin-text-field-container");
		result.getElement().setAttribute("colspan","2");
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

	private TextField createTextField(final String label, final String value)
	{
		val result = new TextField();
		result.getElement().setAttribute("colspan","2");
		result.setReadOnly(true);
		result.setLabel(label);
		result.setValue(value);
		return result;
	}

	private IntegerField createIntegerField(final String label, final Integer value)
	{
		val result = new IntegerField();
		result.getElement().setAttribute("colspan","2");
		result.setReadOnly(true);
		result.setLabel(label);
		result.setValue(value);
		return result;
	}

}
