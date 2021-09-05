package nl.clockwork.ebms.admin.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
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
		addToNavbar(createImage());
		addToNavbar(createMenuBar());
	}

	private Image createImage()
	{
		val image = new Image("images/ebms_admin.gif",getTranslation("home"));
		image.addClickListener(e -> image.getUI().ifPresent(ui -> ui.navigate(HomeView.class)));
		return image;
	}

	private MenuBar createMenuBar()
	{
		val menuBar = new MenuBar();
		menuBar.addItem(new RouterLink(getTranslation("home"),HomeView.class));
		createCPAServiceMenu(menuBar.addItem(getTranslation("cpaService")));
		createMessageServiceMenu(menuBar.addItem(getTranslation("messageService")));
		createAdvancedMenu(menuBar.addItem(getTranslation("advanced")));
		menuBar.addItem(new RouterLink(getTranslation("about"),AboutView.class));
		return menuBar;
	}

	private void createCPAServiceMenu(MenuItem cpaService)
	{
		val subMenu = cpaService.getSubMenu();
		subMenu.addItem(getTranslation("cpas"));
		subMenu.addItem(new Hr());
		subMenu.addItem(getTranslation("urlMappings"));
		subMenu.addItem(new Hr());
		subMenu.addItem(getTranslation("certificateMappings"));
	}

	private void createMessageServiceMenu(MenuItem messageService)
	{
		val subMenu = messageService.getSubMenu();
		subMenu.addItem(getTranslation("ping"));
		subMenu.addItem(new Hr());
		subMenu.addItem(getTranslation("unprocessedMessages"));
		subMenu.addItem(new Hr());
		subMenu.addItem(getTranslation("messageEvents"));
		subMenu.addItem(new Hr());
		subMenu.addItem(getTranslation("messageSend"));
		subMenu.addItem(getTranslation("messageResend"));
		subMenu.addItem(new Hr());
		subMenu.addItem(getTranslation("messageStatus"));
	}

	private void createAdvancedMenu(MenuItem advanced)
	{
		val subMenu = advanced.getSubMenu();
		subMenu.addItem(getTranslation("traffic"));
		subMenu.addItem(getTranslation("trafficChart"));
		subMenu.addItem(new Hr());
		subMenu.addItem(getTranslation("cpas"));
		subMenu.addItem(getTranslation("messages"));
	}
}
