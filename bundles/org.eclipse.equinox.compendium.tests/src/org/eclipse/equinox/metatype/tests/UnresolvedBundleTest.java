/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.equinox.metatype.tests;

import java.io.IOException;
import java.io.InputStream;
import org.osgi.framework.Bundle;
import org.osgi.service.metatype.*;

/*
 * Ensure the metadata XML information from an unresolved bundle is provided.
 * 
 * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=363374.
 */
public class UnresolvedBundleTest extends AbstractTest {
	private Bundle bundle;

	public void testUnresolvedBundle() throws Exception {
		assertBundleUnresolved();
		MetaTypeInformation mti = metatype.getMetaTypeInformation(bundle);
		assertNotNull("Metatype information was null", mti); //$NON-NLS-1$
		ObjectClassDefinition ocd = mti.getObjectClassDefinition("org.eclipse.equinox.metatype.tests.tb8", null); //$NON-NLS-1$
		assertNotNull("Object class definition was null", ocd); //$NON-NLS-1$
		assertEquals("Wrong object class definition ID", "1", ocd.getID()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Wrong object class definition name", "OCD1 Name", ocd.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		AttributeDefinition[] ads = ocd.getAttributeDefinitions(ObjectClassDefinition.ALL);
		assertEquals("Wrong number of attribute definitions", 1, ads.length); //$NON-NLS-1$
		AttributeDefinition ad = ads[0];
		assertNotNull("Attribute definition was null", ad); //$NON-NLS-1$
		assertEquals("Wrong attribute definition ID", "1", ad.getID()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Wrong attribute definition name", "AD1 Name", ad.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Wrong attribute definition type", AttributeDefinition.STRING, ad.getType()); //$NON-NLS-1$
		InputStream icon = ocd.getIcon(10000);
		assertNotNull("Icon was null", icon); //$NON-NLS-1$
		try {
			icon.close();
		} catch (IOException e) {
			// noop
		}
		String[] locales = mti.getLocales();
		assertNotNull("Locales was null", locales); //$NON-NLS-1$
		assertEquals("Wrong number of locales", 1, locales.length); //$NON-NLS-1$
		assertEquals("Wrong locale", "en", locales[0]); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected void setUp() throws Exception {
		super.setUp();
		bundle = bundleInstaller.installBundle("tb8"); //$NON-NLS-1$
		assertBundleUnresolved();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private void assertBundleUnresolved() {
		assertEquals("Bundle should not be resolved", Bundle.INSTALLED, bundle.getState()); //$NON-NLS-1$
	}
}
