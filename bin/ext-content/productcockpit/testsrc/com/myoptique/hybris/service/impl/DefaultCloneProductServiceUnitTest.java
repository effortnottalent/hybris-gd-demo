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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.myoptique.hybris.service.CloneProductService;


/**
 *
 */
@UnitTest
public class DefaultCloneProductServiceUnitTest
{
	@InjectMocks
	private final CloneProductService cloneProductService = new DefaultCloneProductService();
	@Mock(answer = Answers.RETURNS_MOCKS)
	private FlexibleSearchService flexibleSearchService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGenerateName()
	{
		Assert.assertEquals("existingName1", cloneProductService.generateNameForClone("existingName"));
		Assert.assertEquals("existingName1", cloneProductService.generateNameForClone("existingName0"));
		Assert.assertEquals("existingName2", cloneProductService.generateNameForClone("existingName1"));
		Assert.assertEquals("existingName10", cloneProductService.generateNameForClone("existingName9"));
		Assert.assertEquals("existingName37999", cloneProductService.generateNameForClone("existingName37998"));
		Assert.assertEquals("existingName00039", cloneProductService.generateNameForClone("existingName00038"));
	}

}
