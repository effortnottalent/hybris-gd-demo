package com.myoptique.hybris.productcockpit.components.browserarea;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.ClassUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

import com.myoptique.hybris.service.CloneProductService;


/**
 *
 */
public class CloneProductAction extends AbstractListViewAction
{
	private final String imageURI = "/productcockpit/images/icon_func_clone_product.png";
	private final String tooltip = "browserarea.message.clone.tooltip";

	@Resource
	private CloneProductService cloneProductService;

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
				final List<TypedObject> selectedObjects = paramContext.getBrowserModel().getSelectedItems();
				if (selectedObjects.size() > 0
						&& ClassUtils.isAssignable(selectedObjects.get(0).getObject().getClass(), ProductModel.class))
				{
					final StringBuilder newProductNames = new StringBuilder();
					for (final TypedObject selectedProduct : selectedObjects)
					{
						final ProductModel clone = cloneProductService.cloneProduct((ProductModel) selectedProduct.getObject());
						newProductNames.append("\n" + clone.getCode());
					}
					Messagebox.show(selectedObjects.size() + " cloned:\n" + newProductNames);
				}
				else
				{
					Messagebox
							.show("Cannot clone objects selected, as either no objects were selected, or were not of the Product type.");
				}
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
