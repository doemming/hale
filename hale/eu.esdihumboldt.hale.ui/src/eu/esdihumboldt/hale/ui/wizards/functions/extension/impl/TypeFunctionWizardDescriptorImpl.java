/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                 01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2011.
 */

package eu.esdihumboldt.hale.ui.wizards.functions.extension.impl;

import org.eclipse.core.runtime.IConfigurationElement;

import de.fhg.igd.osgi.util.OsgiUtils;

import eu.esdihumboldt.hale.align.extension.function.TypeFunction;
import eu.esdihumboldt.hale.align.extension.function.TypeFunctionExtension;
import eu.esdihumboldt.hale.ui.wizards.functions.extension.FunctionWizardDescriptor;
import eu.esdihumboldt.hale.ui.wizards.functions.extension.TypeFunctionWizardDescriptor;

/**
 * Type function wizard descriptor
 * @author Simon Templer
 */
public class TypeFunctionWizardDescriptorImpl extends
		AbstractFunctionWizardDescriptor<TypeFunction> implements
		TypeFunctionWizardDescriptor {

	/**
	 * @see AbstractFunctionWizardDescriptor#AbstractFunctionWizardDescriptor(IConfigurationElement)
	 */
	public TypeFunctionWizardDescriptorImpl(IConfigurationElement conf) {
		super(conf);
	}

	/**
	 * @see FunctionWizardDescriptor#getFunction()
	 */
	@Override
	public TypeFunction getFunction() {
		TypeFunctionExtension tfe = OsgiUtils.getService(TypeFunctionExtension.class);
		return tfe.get(getFunctionId());
	}

}
