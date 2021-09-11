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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

import lombok.val;

@PWA(name = "ebms-admin-vaadin", shortName = "ebms-admin-vaadin", enableInstallPrompt = false, iconPath = "icons/icon.png")
@Theme(value = Material.class)
@CssImport("./styles/custom-styles.css")
@PageTitle("Main")
public class MainLayout extends AppLayout
{
	public MainLayout()
	{
		addToNavbar(createImage());
		addToNavbar(createMenuBar());
	}

	private Image createImage()
	{
		val image = new Image("images/ebms_admin.gif",getTranslation("home"));
		image.addClickListener(e -> UI.getCurrent().navigate(HomeView.class));
		return image;
	}

	private MenuBar createMenuBar()
	{
		val menuBar = new MenuBar();
		menuBar.setOpenOnHover(true);
		menuBar.addItem(createRouterLink(getTranslation("home"),HomeView.class));
		createCPAServiceMenu(menuBar.addItem(getTranslation("cpaService")));
		createMessageServiceMenu(menuBar.addItem(getTranslation("messageService")));
		createAdvancedMenu(menuBar.addItem(new Button(getTranslation("advanced"))));
		menuBar.addItem(createRouterLink(getTranslation("about"),AboutView.class));
		return menuBar;
	}

	private RouterLink createRouterLink(String text, Class<? extends Component> component)
	{
		val result = new RouterLink(text,component);
		result.getElement().getStyle().set("text-decoration","none");
		return result;
	}

	private void createCPAServiceMenu(MenuItem cpaService)
	{
		val subMenu = cpaService.getSubMenu();
		subMenu.addItem(getTranslation("cpas"));
		subMenu.add(new Hr());
		subMenu.addItem(getTranslation("urlMappings"));
		subMenu.add(new Hr());
		subMenu.addItem(getTranslation("certificateMappings"));
	}

	private void createMessageServiceMenu(MenuItem messageService)
	{
		val subMenu = messageService.getSubMenu();
		subMenu.addItem(getTranslation("ping"));
		subMenu.add(new Hr());
		subMenu.addItem(getTranslation("unprocessedMessages"));
		subMenu.add(new Hr());
		subMenu.addItem(getTranslation("messageEvents"));
		subMenu.add(new Hr());
		subMenu.addItem(getTranslation("messageSend"));
		subMenu.addItem(getTranslation("messageResend"));
		subMenu.add(new Hr());
		subMenu.addItem(getTranslation("messageStatus"));
	}

	private void createAdvancedMenu(MenuItem advanced)
	{
		val subMenu = advanced.getSubMenu();
		subMenu.addItem(getTranslation("traffic"));
		subMenu.addItem(getTranslation("trafficChart"));
		subMenu.add(new Hr());
		subMenu.addItem(getTranslation("cpas"));
		subMenu.addItem(getTranslation("messages"));
	}
}
