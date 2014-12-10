/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.myoptique.hybris.service.impl;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;

import com.myoptique.hybris.service.CloneProductService;


/**
 *
 */
public class DefaultCloneProductService implements CloneProductService
{

	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	private ArticleApprovalStatus defaultArticleApprovalStatus = ArticleApprovalStatus.CHECK;
	private boolean useDefaultArticleApprovalStatus = true;
	private String nameSuffix = " - Duplicate";
	private static final String FLEXIBLE_SEARCH_QUERY_PRODUCT = "SELECT {" + ProductModel.PK + "} FROM {" + ProductModel._TYPECODE
			+ "} WHERE {" + ProductModel.CODE + "} = ";

	/**
	 * @param nameSuffix
	 *           the nameSuffix to set
	 */
	public void setNameSuffix(final String nameSuffix)
	{
		this.nameSuffix = nameSuffix;
	}

	/**
	 * @param defaultArticleApprovalStatus
	 *           the defaultArticleApprovalStatus to set
	 */
	public void setDefaultArticleApprovalStatus(final ArticleApprovalStatus defaultArticleApprovalStatus)
	{
		this.defaultArticleApprovalStatus = defaultArticleApprovalStatus;
	}

	/**
	 * @param useDefaultArticleApprovalStatus
	 *           the useDefaultArticleApprovalStatus to set
	 */
	public void setUseDefaultArticleApprovalStatus(final boolean useDefaultArticleApprovalStatus)
	{
		this.useDefaultArticleApprovalStatus = useDefaultArticleApprovalStatus;
	}


	/**
	 * Adds a number onto the end of the existing name (or increments existing number if present)
	 */
	@Override
	public String generateNameForClone(final String productName)
	{

		int suffixNumber = 0;
		int suffixCharsFromEnd = 0;

		// get number at end

		for (int pos = 0; pos < productName.length(); pos++)
		{
			final String possibleNumberString = productName.substring(productName.length() - (pos + 1));
			if (NumberUtils.isNumber(possibleNumberString))
			{
				suffixNumber = NumberUtils.toInt(possibleNumberString);
			}
			else
			{
				suffixCharsFromEnd = pos;
				break;
			}
		}

		// increment number until product isn't found

		String newName = "";
		do
		{
			suffixNumber += (suffixNumber == 0) ? 1 : Integer.signum(suffixNumber);
			int numDigits = (suffixNumber % 10 == 0) ? suffixCharsFromEnd + 1 : suffixCharsFromEnd;
			if (numDigits == 0)
			{
				numDigits = 1;
			}
			newName = productName.substring(0, productName.length() - suffixCharsFromEnd)
					+ String.format("%0" + numDigits + "d", suffixNumber);
		}
		while (flexibleSearchService.search(FLEXIBLE_SEARCH_QUERY_PRODUCT + "'" + newName + "'").getCount() != 0);
		return newName;

	}

	@Override
	public ProductModel cloneProduct(final ProductModel productToClone)
	{

		// get clone of product
		// problems with duplicating price rows/PPG, need to fix

		boolean replacePriceRows = false;
		final ProductPriceGroup priceClass = productToClone.getEurope1PriceFactory_PPG();
		final Collection<PriceRowModel> priceRows = productToClone.getEurope1Prices();

		if (priceClass != null)
		{
			productToClone.setEurope1PriceFactory_PPG(null);
			replacePriceRows = true;
		}
		productToClone.setEurope1PriceFactory_PPG(null);
		final ProductModel newProduct = modelService.clone(productToClone);
		if (useDefaultArticleApprovalStatus)
		{
			newProduct.setApprovalStatus(defaultArticleApprovalStatus);
		}

		// add new name

		newProduct.setCode(generateNameForClone(productToClone.getCode()));
		newProduct.setName(productToClone.getName() + nameSuffix);

		if (priceClass != null)
		{
			newProduct.setEurope1Prices(priceRows);
			newProduct.setEurope1PriceFactory_PPG(priceClass);
		}

		// save and return

		modelService.save(newProduct);
		return newProduct;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}
