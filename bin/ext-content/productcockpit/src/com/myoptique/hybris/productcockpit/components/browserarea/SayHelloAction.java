package com.myoptique.hybris.productcockpit.components.browserarea;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;

import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;


/**
 *
 */
public class SayHelloAction extends AbstractListViewAction
{

	private final String imageURI = "/testproductcockpit/images/icon_func_get_pdf_available.png";
	private final String tooltip = "browserarea.message.hello.tooltip";

	@Override
	public String getImageURI(final Context paramContext)
	{
		return imageURI;
	}

	@Override
	public EventListener getEventListener(final Context paramContext)
	{
		return new EventListener()
		{

			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				Messagebox.show("Hello!!");

			}
		};
	}

	@Override
	public Menupopup getPopup(final Context paramContext)
	{
		return null;
	}

	@Override
	public Menupopup getContextPopup(final Context paramContext)
	{
		return null;
	}

	@Override
	public String getTooltip(final Context paramContext)
	{
		return Labels.getLabel(tooltip);
	}

	@Override
	protected void doCreateContext(final Context paramContext)
	{
	}

}
