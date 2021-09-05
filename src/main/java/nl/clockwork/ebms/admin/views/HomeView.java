package nl.clockwork.ebms.admin.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "home", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PageTitle("Home")
public class HomeView extends Div
{
	public HomeView()
	{
		add(new Text("Welcome to the EbMS Admin Console"));
	}
}