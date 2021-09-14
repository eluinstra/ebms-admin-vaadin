package nl.clockwork.ebms.admin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.StreamResource;

public class DownloadButton extends Anchor
{
	public DownloadButton(String text, StreamResource resource)
	{
		super(resource,"");
		getElement().setAttribute("download",true);
		add(new Button(text));
	}
}
