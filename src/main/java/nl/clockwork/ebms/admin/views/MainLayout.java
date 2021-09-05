package nl.clockwork.ebms.admin.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import lombok.val;

@PWA(name = "ebms-admin-vaadin", shortName = "ebms-admin-vaadin", enableInstallPrompt = false)
@Theme(themeFolder = "ebms-admin-vaadin")
@PageTitle("Main")
public class MainLayout extends AppLayout
{
	public MainLayout()
	{
		val menuBar = new MenuBar();
		menuBar.addItem(new RouterLink("Home",HomeView.class));
		createCPAServiceMenu(menuBar.addItem("cpaService"));
		createMessageServiceMenu(menuBar.addItem("messageService"));
		createAdvancedMenu(menuBar.addItem("advanced"));
		menuBar.addItem(new RouterLink("About",AboutView.class));
		addToNavbar(menuBar);
	}

	private void createCPAServiceMenu(MenuItem cpaService)
	{
		val subMenu = cpaService.getSubMenu();
		subMenu.addItem("cpas");
		subMenu.addItem(new Hr());
		subMenu.addItem("urlMappings");
		subMenu.addItem(new Hr());
		subMenu.addItem("certificateMappings");
	}

	private void createMessageServiceMenu(MenuItem messageService)
	{
		val subMenu = messageService.getSubMenu();
		subMenu.addItem("ping");
		subMenu.addItem(new Hr());
		subMenu.addItem("unprocessedMessages");
		subMenu.addItem(new Hr());
		subMenu.addItem("messageEvents");
		subMenu.addItem(new Hr());
		subMenu.addItem("messageSend");
		subMenu.addItem("messageResend");
		subMenu.addItem(new Hr());
		subMenu.addItem("messageStatus");
	}

	private void createAdvancedMenu(MenuItem advanced)
	{
		val subMenu = advanced.getSubMenu();
		subMenu.addItem("traffic");
		subMenu.addItem("trafficChart");
		subMenu.addItem(new Hr());
		subMenu.addItem("cpas");
		subMenu.addItem("messages");
	}
}
