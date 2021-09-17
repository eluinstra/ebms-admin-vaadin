package nl.clockwork.ebms.admin.components;

import com.vaadin.flow.component.Component;

public interface WithElement
{
	default void setColSpan(Component component, int cols)
	{
		component.getElement().setAttribute("colspan",String.valueOf(cols));
	}
}
