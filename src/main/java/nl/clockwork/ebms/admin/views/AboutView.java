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
package nl.clockwork.ebms.admin.views;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.html.Span;
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
		val accordion = new Accordion();
		accordion.setSizeFull();
		accordion.add(createVersionsCard());
		accordion.add(createPropertiesCard());
		accordion.add(createLicenseInfo());
		add(accordion);
	}

	private AccordionPanel createVersionsCard()
	{
		val properties = new VerticalLayout();
		properties.add(new Span(Utils.readVersion("/META-INF/maven/nl.clockwork.ebms.admin/ebms-admin/pom.properties")));
		properties.add(new Span(Utils.readVersion("/META-INF/maven/nl.clockwork.ebms/ebms-core/pom.properties")));
		return new AccordionPanel(getTranslation("versions"),properties);
	}

	private AccordionPanel createPropertiesCard()
	{
		return new AccordionPanel(
			getTranslation("properties"),
			new Pre("prop1=val1\nprop2=val2\nprop3=val3")
		);
	}

	private AccordionPanel createLicenseInfo()
	{
		return new AccordionPanel(
			getTranslation("license"),
			new Pre("Copyright 2021 Ordina\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\n  http://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.\n")
		);
	}

}
