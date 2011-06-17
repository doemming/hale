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

package eu.esdihumboldt.hale.ui.service.project;

import de.fhg.igd.osgi.util.configuration.IConfigurationService;
import eu.esdihumboldt.hale.core.io.IOProvider;
import eu.esdihumboldt.hale.core.io.IOProviderFactory;
import eu.esdihumboldt.hale.ui.io.advisor.IOAdvisorFactory;

/**
 * The {@link ProjectService} manages information on a HALE project,
 * such as the loaded schemas, instances etc.
 * 
 * @author Thorsten Reitz
 * @author Simon Templer
 */
public interface ProjectService {
	
	/**
	 * Adds a project service listener 
	 * @param listener the listener to add
	 */
	public void addListener(ProjectServiceListener listener);
	
	/**
	 * Removes a project service listener 
	 * @param listener the listener to remove
	 */
	public void removeListener(ProjectServiceListener listener);

	/**
	 * Remember I/O operations after the execution of the corresponding I/O 
	 * provider for storing it in the project.
	 * 
	 * @param advisorFactory the advisor factory
	 * @param providerType the I/O provider type
	 * @param providerId the I/O provider identifier
	 * @param provider the I/O provider instance used for the I/O operation
	 */
	public void rememberIO(IOAdvisorFactory advisorFactory, 
			Class<? extends IOProviderFactory<?>> providerType, 
			String providerId, IOProvider provider);
	
	/**
	 * Get a project scoped configuration service
	 * @return the configuration service
	 */
	public IConfigurationService getConfigurationService();
	
	/**
	 * Get the current's project name
	 * 
	 * @return the project name, may be <code>null</code>
	 */
	public String getProjectName();
	
//	/**
//	 * Get if the project content is changed
//	 * 
//	 * @return if the project content is changed
//	 */
//	public boolean isChanged();
	
	/**
	 * Clean the project, reset all services.
	 */
	public void clean();
	
//	/**
//	 * Load a project from a given file.
//	 * 
//	 * @param file the project file
//	 * @param monitor the progress monitor
//	 */
//	public void load(File file, IProgressMonitor monitor);
	
	/**
	 * Open a project.
	 */
	public void open();
	
//	/**
//	 * Save the project. Calls {@link #saveAs()} if no file name is set yet.
//	 */
//	public void save(IProgressMonitor monitor);
	
	/**
	 * Save the project to the given file
	 */
	public void saveAs();
	
}
