package nl.clockwork.ebms.admin.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;

public class BackButton extends Button
{
	public BackButton(String label)
	{
		setText(label);
		addClickListener(e -> UI.getCurrent().getElement().executeJs("window.history.back()"));
	}
}
