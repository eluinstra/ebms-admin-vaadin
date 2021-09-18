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
package nl.clockwork.ebms.admin.views.cpa;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.NonNull;
import lombok.val;
import nl.clockwork.ebms.admin.components.BackButton;
import nl.clockwork.ebms.admin.components.WithElement;
import nl.clockwork.ebms.admin.model.CPA;
import nl.clockwork.ebms.admin.views.MainLayout;
import nl.clockwork.ebms.admin.views.WithBean;

@Route(value = "cpa/:cpaId", layout = MainLayout.class)
@PageTitle("CPA")
public class CpaView extends VerticalLayout implements BeforeEnterObserver, WithBean, WithElement
{
	public CpaView()
	{
		add(new H1(getTranslation("cpa")));
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event)
	{
		val cpaId = event.getRouteParameters().get("cpaId").orElse(null);
		if (cpaId == null)
			add(new Text("CPA not found"));
		else
		{
			val cpa = getEbMSAdminDAO().findCPA(cpaId);
			add(createFormLayout(cpa));
			add(new BackButton(getTranslation("cmd.back")));
		}
	}

	private Component createFormLayout(CPA cpa)
	{
		val result = new FormLayout();
		result.setSizeFull();
		result.add(createTextArea(getTranslation("lbl.cpa"),cpa.getCpa()));
		return result;
	}

	private Component createTextArea(@NonNull String label, @NonNull String value)
	{
		val result = new TextArea();
		setColSpan(result,2);//
		// result.getElement().setAttribute("rows","40");
		result.setHeight("600px");
		result.setReadOnly(true);
		result.setLabel(label);
		result.setValue(value);
		return result;
	}
}
