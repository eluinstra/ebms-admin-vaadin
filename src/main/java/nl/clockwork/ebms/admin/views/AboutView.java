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
package nl.clockwork.ebms.admin.views;

import com.github.appreciated.card.Card;
import com.github.appreciated.card.content.Item;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.val;
import nl.clockwork.ebms.admin.Utils;

@Route(value = "about", layout = MainLayout.class)
@PageTitle("About")
public class AboutView extends VerticalLayout
{
	public AboutView()
	{
		add(new H1(getTranslation("about")));
		add(createVersionsCard());
		add(createPropertiesCard());
	}

	private Card createVersionsCard()
	{
		val result = new Card(
			// new TitleLabel("Versions"),
			new Item("ebms-admin",Utils.readVersion("/META-INF/maven/nl.clockwork.ebms.admin/ebms-admin/pom.properties")),
			new Item("ebms-core",Utils.readVersion("/META-INF/maven/nl.clockwork.ebms/ebms-core/pom.properties"))
		);
		result.setWidthFull();
		return result;
	}

	private Component createPropertiesCard()
	{
		val properties = new Item("Properties","prop1=val1\nprop2=val2\nprop3=val3");
		properties.getElement().getStyle().set("white-space","pre-wrap");
		val result = new Card(
			// new TitleLabel("Properties"),
			properties
		);
		result.setWidthFull();
		return result;
	}

}
