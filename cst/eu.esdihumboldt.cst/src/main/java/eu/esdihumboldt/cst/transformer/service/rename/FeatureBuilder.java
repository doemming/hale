/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                 01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2010.
 */

package eu.esdihumboldt.cst.transformer.service.rename;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import org.geotools.feature.AttributeImpl;
import org.geotools.feature.FeatureImpl;
import org.geotools.feature.GeometryAttributeImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.identity.Identifier;

import eu.esdihumboldt.goml.omwg.Property;

/**
 * TODO Add Type comment
 * 
 * @author Thorsten Reitz
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 * @version $Id$ 
 * @since 2.0.0.M2
 */
public class FeatureBuilder {
	
	/**
	 * @param ft
	 * @param source
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Feature buildFeature(FeatureType ft, Feature source) {
		SimpleFeatureType targetType = (SimpleFeatureType) ft;
		Feature target = null;
		
		Collection properties = new HashSet<Property>();
		for (AttributeDescriptor ad : targetType.getAttributeDescriptors()) {
			Identifier id = new FeatureIdImpl(ad.getLocalName());
			// create normal AttributeImpls
			if (ad instanceof GeometryDescriptorImpl) {
				properties.add(new GeometryAttributeImpl(
						null, (GeometryDescriptor)ad, id));
			}
			else if (ad instanceof AttributeDescriptorImpl) {
				if (ad.getType().getBinding().equals(Collection.class)) {
					if (ad.getType() instanceof FeatureType) {
						properties.add(
								new AttributeImpl(Collections.singleton(
										buildFeature((FeatureType) ad.getType(), null)), 
										ad, id));
					}
				}
				else {
					properties.add(new AttributeImpl(null, ad, id));
				}
			}
		}
		if (source == null) {
			target = new FeatureImpl(properties, targetType, 
					new FeatureIdImpl(UUID.randomUUID().toString()));
		}
		else {
			target = new FeatureImpl(properties, targetType, source.getIdentifier());
		}

		return target;
	}

}
