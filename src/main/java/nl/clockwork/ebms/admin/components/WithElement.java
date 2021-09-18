package nl.clockwork.ebms.admin.components;

import com.vaadin.flow.component.Component;

public interface WithElement
{
	default void setColSpan(Component component, int cols)
	{
		if (cols > 1)
			component.getElement().setAttribute("colspan",String.valueOf(cols));
	}
}
