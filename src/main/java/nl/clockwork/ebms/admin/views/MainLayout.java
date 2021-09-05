package nl.clockwork.ebms.admin.views;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import nl.clockwork.ebms.admin.views.about.AboutView;
import nl.clockwork.ebms.admin.views.home.HomeView;

/**
 * The main view is a top-level placeholder for other views.
 */
@PWA(name = "ebms-admin-vaadin", shortName = "ebms-admin-vaadin", enableInstallPrompt = false)
@Theme(themeFolder = "ebms-admin-vaadin")
@PageTitle("Main")
public class MainLayout extends AppLayout
{
	public static class MenuItemInfo
	{

		private String text;
		private String iconClass;
		private Class<? extends Component> view;

		public MenuItemInfo(String text, String iconClass, Class<? extends Component> view)
		{
			this.text = text;
			this.iconClass = iconClass;
			this.view = view;
		}

		public String getText()
		{
			return text;
		}

		public String getIconClass()
		{
			return iconClass;
		}

		public Class<? extends Component> getView()
		{
			return view;
		}

	}

	public MainLayout()
	{
		addToNavbar(createHeaderContent());
	}

	private Component createHeaderContent()
	{
		Header header = new Header();
		header.addClassNames("bg-base","border-b","border-contrast-10","box-border","flex","flex-col","w-full");

		Div layout = new Div();
		layout.addClassNames("flex","h-xl","items-center","px-l");

		H1 appName = new H1("ebms-admin-vaadin");
		appName.addClassNames("my-0","me-auto","text-l");
		layout.add(appName);

		Nav nav = new Nav();
		nav.addClassNames("flex","gap-s","overflow-auto","px-m");

		for (RouterLink link : createLinks())
		{
			nav.add(link);
		}

		header.add(layout,nav);
		return header;
	}

	private List<RouterLink> createLinks()
	{
		MenuItemInfo[] menuItems = new MenuItemInfo[]{ //
				new MenuItemInfo("Home","la la-file",HomeView.class), //

				new MenuItemInfo("About","la la-file",AboutView.class), //

		};
		List<RouterLink> links = new ArrayList<>();
		for (MenuItemInfo menuItemInfo : menuItems)
		{
			links.add(createLink(menuItemInfo));

		}
		return links;
	}

	private static RouterLink createLink(MenuItemInfo menuItemInfo)
	{
		RouterLink link = new RouterLink();
		link.addClassNames("flex","h-m","items-center","px-s","relative","text-secondary");
		link.setRoute(menuItemInfo.getView());

		Span icon = new Span();
		icon.addClassNames("me-s","text-l");
		if (!menuItemInfo.getIconClass().isEmpty())
		{
			icon.addClassNames(menuItemInfo.getIconClass());
		}

		Span text = new Span(menuItemInfo.getText());
		text.addClassNames("font-medium","text-s","whitespace-nowrap");

		link.add(icon,text);
		return link;
	}

}
