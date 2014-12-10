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
package com.myoptique.hybris.service;

import de.hybris.platform.core.model.product.ProductModel;


/**
 *
 */
public interface CloneProductService
{

	public ProductModel cloneProduct(ProductModel productToClone);

	public String generateNameForClone(String productName);

}
