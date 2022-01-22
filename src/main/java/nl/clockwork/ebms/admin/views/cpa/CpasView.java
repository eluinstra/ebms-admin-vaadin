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
package nl.clockwork.ebms.admin.views.cpa;

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

import lombok.val;
import nl.clockwork.ebms.admin.components.RouterLink;
import nl.clockwork.ebms.admin.model.CPA;
import nl.clockwork.ebms.admin.views.MainLayout;
import nl.clockwork.ebms.admin.views.WithBean;

@Route(value = "cpa", layout = MainLayout.class)
@PageTitle("CPAs")
public class CpasView extends VerticalLayout implements WithBean
{
	public CpasView()
	{
		add(new H1(getTranslation("cpas")));
		add(createCpaGrid());
	}

	private Component createCpaGrid()
	{
		val result = new Grid<CPA>(CPA.class,false);
		result.setDataProvider(createCpaDataProvider());
		result.setSelectionMode(SelectionMode.NONE);
		// result.addItemClickListener(cpa -> UI.getCurrent().navigate(CpaView.class, new RouteParameters("cpaId",cpa.getItem().getCpaId())));
		// result.addColumn("cpaId").setHeader(getTranslation("lbl.cpaId"));
		result.addColumn(new ComponentRenderer<>(cpa -> createRouterLink(cpa,CpaView.class))).setHeader(getTranslation("lbl.cpaId"));
		return result;
	}

	private RouterLink createRouterLink(CPA cpa, Class<? extends Component> component)
	{
		return new RouterLink(cpa.getCpaId(),component, new RouteParameters("cpaId",cpa.getCpaId()));
	}

	private DataProvider<CPA,?> createCpaDataProvider()
	{
		return DataProvider.fromCallbacks(
			query -> getEbMSAdminDAO().selectCPAs(query.getOffset(),query.getLimit()).stream(),
			query -> ((Long)getEbMSAdminDAO().countCPAs()).intValue());
	}
}
