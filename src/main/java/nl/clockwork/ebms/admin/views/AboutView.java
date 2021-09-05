package nl.clockwork.ebms.admin.views;

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.val;
import nl.clockwork.ebms.admin.Utils;

@Route(value = "about", layout = MainLayout.class)
@PageTitle("About")
public class AboutView extends Div
{
	public AboutView()
	{
		val div = new Div();
		div.add(Utils.readVersion("/META-INF/maven/nl.clockwork.ebms.admin/ebms-admin/pom.properties"));
		div.add(new HtmlComponent("br"));
		div.add(Utils.readVersion("/META-INF/maven/nl.clockwork.ebms/ebms-core/pom.properties"));
		add(div);
	}
}
