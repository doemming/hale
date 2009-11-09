/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                  01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2010.
 */

package eu.esdihumboldt.goml.oml.ext;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.esdihumboldt.cst.align.ext.IParameter;
import eu.esdihumboldt.cst.align.ext.ITransformation;
import eu.esdihumboldt.goml.rdf.About;

/**
 * This class represents <xs:group name="transformation">.
 * 
 * @author Thorsten Reitz, Marian de Vries 
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 * @partner 08 / Delft University of Technology
 * @version $Id$ 
 */
public class Transformation 
	implements ITransformation {


	private About about;
	private URI service;
	private String label;
	private List<IParameter> parameters;

	// getters / setters .......................................................

	
	public Transformation() {
		this.parameters = new ArrayList<IParameter>();
	}
	
	/**
	 * @return the service
	 */
	public URI getService() {
		return service;
	}

	/**
	 * @param service
	 *            the service to set
	 */
	public void setService(URI service) {
		this.service = service;
	}

	public String getLabel() {
		return this.label;
	}

	public List<IParameter> getParameters() {
		return this.parameters;
	}
	
	public Map<String, IParameter> getParameterMap() {
		Map<String, IParameter> result = new HashMap<String, IParameter>();
		for (IParameter p : this.parameters) {
			result.put(p.getName(), p);
		}
		return result;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(List<IParameter> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the about
	 */
	public About getAbout() {
		return about;
	}

	/**
	 * @param about the about to set
	 */
	public void setAbout(About about) {
		this.about = about;
	}
	
	

}
