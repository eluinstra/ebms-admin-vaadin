package nl.clockwork.ebms.admin.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteParameters;

public class RouterLink extends com.vaadin.flow.router.RouterLink
{
	public RouterLink (String text, Class<? extends Component> component)
	{
		super(text,component);
		getElement().getStyle().set("text-decoration","none");
	}

	public RouterLink (String text, Class<? extends Component> component, RouteParameters parameters)
	{
		super(text,component,parameters);
		getElement().getStyle().set("text-decoration","none");
	}
}
