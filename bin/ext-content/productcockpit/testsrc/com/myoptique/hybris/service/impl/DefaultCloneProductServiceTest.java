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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.myoptique.hybris.service.CloneProductService;


/**
 *
 */
@IntegrationTest
public class DefaultCloneProductServiceTest extends ServicelayerTransactionalTest
{
	@Resource
	private ProductService productService;
	@Resource
	private CloneProductService cloneProductService;
	@Resource
	private PriceService priceService;
	@Resource
	private ModelService modelService;

	@Before
	public void setUp() throws Exception
	{

		productService = Registry.getApplicationContext().getBean(ProductService.class);
		cloneProductService = Registry.getApplicationContext().getBean(CloneProductService.class);

		createCoreData();
		createHardwareCatalog();

	}

	@Test
	public void testCloneProduct()
	{
		final ProductModel product = productService.getProductForCode("HW1100-0023");
		final ProductModel clone = cloneProductService.cloneProduct(product);
		Assert.assertNotSame(clone, product);
		Assert.assertEquals("HW1100-0024", clone.getCode());
		Assert.assertFalse(product.getName().contains("Duplicate"));
		Assert.assertTrue(clone.getName().contains("Duplicate"));
		Assert.assertEquals(clone.getPriceQuantity(), product.getPriceQuantity());
	}

	@Test
	@Ignore
	public void testCloneProductWithPriceGroup()
	{

		final PriceRowModel priceRow = modelService.create(PriceRowModel.class);
		priceRow.setPg(ProductPriceGroup.valueOf("testPriceGroup"));
		priceRow.setPrice(50.00);
		final UnitModel unitModel = modelService.create(UnitModel.class);
		unitModel.setCode("units");
		unitModel.setName("units");
		priceRow.setUnit(unitModel);
		modelService.saveAll();

		final ProductModel product = productService.getProductForCode("HW1100-0023");
		product.setEurope1Prices(new ArrayList<PriceRowModel>()
		{
			{
				add(priceRow);
			}
		});
		modelService.save(product);

		final ProductModel clone = cloneProductService.cloneProduct(product);
		Assert.assertNotSame(clone, product);
		Assert.assertEquals("HW1100-0024", clone.getCode());
		Assert.assertFalse(product.getName().contains("Duplicate"));
		Assert.assertTrue(clone.getName().contains("Duplicate"));
		Assert.assertEquals(clone.getPriceQuantity(), product.getPriceQuantity());
	}

	@Test
	public void testCloneProductWithFeatures()
	{
		final ProductModel product = productService.getProductForCode("HW1240-1732");
		final ProductModel clone = cloneProductService.cloneProduct(product);
		Assert.assertNotSame(clone, product);
		Assert.assertEquals("HW1240-1734", clone.getCode());
		Assert.assertFalse(product.getName().contains("Duplicate"));
		Assert.assertTrue(clone.getName().contains("Duplicate"));
		Assert.assertEquals(clone.getPriceQuantity(), product.getPriceQuantity());

		// print out features

		System.out.println("product:");
		for (final ProductFeatureModel productFeature : product.getFeatures())
		{
			System.out.println(productFeature.getQualifier() + ": " + productFeature.getValue());
		}
		System.out.println("\nclone:");
		for (final ProductFeatureModel productFeature : clone.getFeatures())
		{
			System.out.println(productFeature.getQualifier() + ": " + productFeature.getValue());
		}

		// check features

		Assert.assertEquals(clone.getFeatures().size(), product.getFeatures().size());
		for (final ProductFeatureModel cloneFeature : clone.getFeatures())
		{
			boolean guard = false;
			for (final ProductFeatureModel productFeature : product.getFeatures())
			{
				if (productFeature.getQualifier().equals(cloneFeature.getQualifier())
						&& productFeature.getValue().equals(cloneFeature.getValue()))
				{
					guard = true;
				}
			}
			Assert.assertTrue(guard);
		}
	}
}
