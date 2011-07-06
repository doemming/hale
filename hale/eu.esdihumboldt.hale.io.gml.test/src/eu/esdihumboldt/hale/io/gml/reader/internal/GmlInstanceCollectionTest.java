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

package eu.esdihumboldt.hale.io.gml.reader.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.junit.Test;

import eu.esdihumboldt.hale.core.io.IOProviderConfigurationException;
import eu.esdihumboldt.hale.core.io.report.IOReport;
import eu.esdihumboldt.hale.core.io.supplier.DefaultInputSupplier;
import eu.esdihumboldt.hale.instance.model.Group;
import eu.esdihumboldt.hale.instance.model.Instance;
import eu.esdihumboldt.hale.instance.model.ResourceIterator;
import eu.esdihumboldt.hale.io.xsd.constraint.XmlElements;
import eu.esdihumboldt.hale.io.xsd.model.XmlElement;
import eu.esdihumboldt.hale.io.xsd.reader.XmlSchemaReader;
import eu.esdihumboldt.hale.schema.io.SchemaReader;
import eu.esdihumboldt.hale.schema.model.Schema;
import eu.esdihumboldt.hale.schema.model.TypeDefinition;


/**
 * Tests for {@link GmlInstanceCollection}
 *
 * @author Simon Templer
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 */
@SuppressWarnings("restriction")
public class GmlInstanceCollectionTest {
	
	/**
	 * Test loading a simple XML file with one instance
	 * 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testLoadShiporder() throws Exception {
		GmlInstanceCollection instances = loadInstances(
				getClass().getResource("/data/shiporder/shiporder.xsd").toURI(),
				getClass().getResource("/data/shiporder/shiporder.xml").toURI(),
				false);
		
		String ns = "http://www.example.com";
		
		ResourceIterator<Instance> it = instances.iterator();
		assertTrue(it.hasNext());
		
		Instance instance = it.next();
		assertNotNull(instance);
		
		Object[] orderid = instance.getProperty(new QName("orderid")); // attribute form not qualified
		assertNotNull(orderid);
		assertEquals(1, orderid.length);
		assertEquals("889923", orderid[0]);
		
		Object[] orderperson = instance.getProperty(new QName(ns, "orderperson"));
		assertNotNull(orderperson);
		assertEquals(1, orderperson.length);
		assertEquals("John Smith", orderperson[0]);
		
		Object[] shipto = instance.getProperty(new QName(ns, "shipto"));
		assertNotNull(shipto);
		assertEquals(1, shipto.length);
		assertTrue(shipto[0] instanceof Instance);
		Instance shipto1 = (Instance) shipto[0];
		
		Object[] shiptoName = shipto1.getProperty(new QName(ns, "name"));
		assertNotNull(shiptoName);
		assertEquals(1, shiptoName.length);
		assertEquals("Ola Nordmann", shiptoName[0]);
		
		Object[] shiptoAddress = shipto1.getProperty(new QName(ns, "address"));
		assertNotNull(shiptoAddress);
		assertEquals(1, shiptoAddress.length);
		assertEquals("Langgt 23", shiptoAddress[0]);
		
		Object[] shiptoCity = shipto1.getProperty(new QName(ns, "city"));
		assertNotNull(shiptoCity);
		assertEquals(1, shiptoCity.length);
		assertEquals("4000 Stavanger", shiptoCity[0]);
		
		Object[] shiptoCountry = shipto1.getProperty(new QName(ns, "country"));
		assertNotNull(shiptoCountry);
		assertEquals(1, shiptoCountry.length);
		assertEquals("Norway", shiptoCountry[0]);
		
		// items
		Object[] items = instance.getProperty(new QName(ns, "item"));
		assertNotNull(items);
		assertEquals(2, items.length);
		
		// item 1
		Object item1 = items[0];
		assertTrue(item1 instanceof Instance);
		
		Object[] title1 = ((Instance) item1).getProperty(new QName(ns, "title"));
		assertNotNull(title1);
		assertEquals(1, title1.length);
		assertEquals("Empire Burlesque", title1[0]);
		
		// item 2
		Object item2 = items[1];
		assertTrue(item2 instanceof Instance);
		
		Object[] title2 = ((Instance) item2).getProperty(new QName(ns, "title"));
		assertNotNull(title2);
		assertEquals(1, title2.length);
		assertEquals("Hide your heart", title2[0]);
		
		// only one object
		assertFalse(it.hasNext());
		
		it.dispose();
	}
	
	/**
	 * Test loading a simple XML file with one instance including a choice.
	 * 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testLoadChoice() throws Exception {
		GmlInstanceCollection instances = loadInstances(
				getClass().getResource("/data/group/choice.xsd").toURI(),
				getClass().getResource("/data/group/choice.xml").toURI(),
				false);
		
		ResourceIterator<Instance> it = instances.iterator();
		assertTrue(it.hasNext());
		
		Instance instance = it.next();
		assertNotNull(instance);
		
		// choice
		Object[] choice_1 = instance.getProperty(new QName("/ItemsType", "choice_1"));
		assertNotNull(choice_1);
		assertEquals(5, choice_1.length);
		
		String[] expectedProperties = new String[]{"shirt", "hat", "shirt", "umbrella", "hat"};
		for (int i = 0; i < choice_1.length; i++) {
			assertTrue(choice_1[i] instanceof Group);
			Group choice = (Group) choice_1[i];
			String expectedProperty = expectedProperties[i];
			
			int num = 0;
			for (QName name : choice.getPropertyNames()) {
				assertEquals(0, num++); // expecting only one property
				assertEquals(new QName(expectedProperty), name);
			}
		}
		
		// only one object
		assertFalse(it.hasNext());
				
		it.dispose();
	}
	
	/**
	 * Test loading a (relatively) simple GML file with one instance.
	 * Includes groups and a geometry.
	 * 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testLoadWVA() throws Exception {
		GmlInstanceCollection instances = loadInstances(
				getClass().getResource("/data/sample_wva/wfs_va.xsd").toURI(),
				getClass().getResource("/data/sample_wva/wfs_va_sample.gml").toURI(),
				true);
		
		String ns = "http://www.esdi-humboldt.org/waterVA";
		String gmlNs = "http://www.opengis.net/gml";
		
		ResourceIterator<Instance> it = instances.iterator();
		assertTrue(it.hasNext());
		
		Instance instance = it.next();
		assertNotNull(instance);
		
		// check type and element
		
		TypeDefinition type = instance.getDefinition();
		assertEquals(new QName(ns, "Watercourses_VA_Type"), type.getName());
		XmlElements elements = type.getConstraint(XmlElements.class);
		Collection<? extends XmlElement> elementCollection = elements.getElements();
		assertEquals(1, elementCollection.size());
		XmlElement element = elementCollection.iterator().next();
		assertEquals(new QName(ns, "Watercourses_VA"), element.getName());
		
		// check instance
		
		// check a simple property first (FGW_ID)
		Object[] fgwID = instance.getProperty(new QName(ns, "FGW_ID"));
		assertNotNull(fgwID);
		assertEquals(1, fgwID.length);
		assertEquals("81011403", fgwID[0]);
		
		// the_geom
		Object[] the_geom = instance.getProperty(new QName(ns, "the_geom"));
		assertNotNull(the_geom);
		assertEquals(1, the_geom.length);
		assertTrue(the_geom[0] instanceof Instance);
		
		// MultiLineString
		Object[] multiLineString = ((Instance) the_geom[0]).getProperty(new QName(gmlNs, "MultiLineString"));
		assertNotNull(multiLineString);
		assertEquals(1, multiLineString.length);
		assertTrue(multiLineString[0] instanceof Instance);
		
		//TODO the MultiLineString should have a GeometryProperty value with a MultiLineString as geometry and a CRS definition
		// ...getValue()
		
		// srsName
		Object[] srsName = ((Instance) multiLineString[0]).getProperty(new QName("srsName"));
		assertNotNull(srsName);
		assertEquals(1, srsName.length);
		assertEquals("EPSG:31251", srsName[0]);
		
		// lineStringMember
		Object[] lineStringMember = ((Instance) multiLineString[0]).getProperty(new QName(gmlNs, "lineStringMember"));
		assertNotNull(lineStringMember);
		assertEquals(1, lineStringMember.length);
		assertTrue(lineStringMember[0] instanceof Instance);
		
		// LineString
		Object[] lineString = ((Instance) lineStringMember[0]).getProperty(new QName(gmlNs, "LineString"));
		assertNotNull(lineString);
		assertEquals(1, lineString.length);
		assertTrue(lineString[0] instanceof Instance);
		
		//TODO the LineString should have a GeometryProperty value with a LineString as geometry and a CRS definition
		// ...getValue()
		
		// choice
		Object[] choice_1 = ((Instance) lineString[0]).getProperty(new QName(gmlNs + "/LineStringType", "choice_1"));
		assertNotNull(choice_1);
		assertEquals(1, choice_1.length);
		assertTrue(choice_1[0] instanceof Group);
		
		// coordinates
		Object[] coordinates = ((Group) choice_1[0]).getProperty(new QName(gmlNs, "coordinates"));
		assertNotNull(coordinates);
		assertEquals(1, coordinates.length);
		
		//TODO check value of coordinates - should be a list/collection of something
		
		// only one instance should be present
		assertFalse(it.hasNext());
		
		it.dispose();
	}

	private GmlInstanceCollection loadInstances(URI schemaLocation, URI xmlLocation, 
			boolean restrictToFeatures) throws IOException, IOProviderConfigurationException {
		SchemaReader reader = new XmlSchemaReader();
		reader.setSharedTypes(null);
		reader.setSource(new DefaultInputSupplier(schemaLocation));
		IOReport schemaReport = reader.execute(null);
		assertTrue(schemaReport.isSuccess());
		Schema sourceSchema = reader.getSchema();
		
		return new GmlInstanceCollection(
				new DefaultInputSupplier(xmlLocation), 
				sourceSchema, restrictToFeatures);
	}

}
