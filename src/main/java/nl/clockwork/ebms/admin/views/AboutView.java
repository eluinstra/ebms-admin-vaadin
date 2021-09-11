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

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import nl.clockwork.ebms.admin.Utils;

@Route(value = "about", layout = MainLayout.class)
@PageTitle("About")
public class AboutView extends VerticalLayout
{
	public AboutView()
	{
		setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
		add(new Div(new Text(Utils.readVersion("/META-INF/maven/nl.clockwork.ebms.admin/ebms-admin/pom.properties"))));
		add(new Div(new Text(Utils.readVersion("/META-INF/maven/nl.clockwork.ebms/ebms-core/pom.properties"))));
	}
}
