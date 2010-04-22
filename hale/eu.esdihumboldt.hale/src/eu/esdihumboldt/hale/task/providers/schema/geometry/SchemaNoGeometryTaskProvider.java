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

package eu.esdihumboldt.hale.task.providers.schema.geometry;

import java.util.Collection;

import eu.esdihumboldt.hale.models.SchemaService.SchemaType;
import eu.esdihumboldt.hale.schemaprovider.model.AttributeDefinition;
import eu.esdihumboldt.hale.schemaprovider.model.SchemaElement;
import eu.esdihumboldt.hale.task.Task;
import eu.esdihumboldt.hale.task.providers.schema.AbstractSchemaTaskProvider;

/**
 * 
 *
 * @author Simon Templer
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 * @version $Id$ 
 */
public class SchemaNoGeometryTaskProvider extends AbstractSchemaTaskProvider {
	
	private final NoGeometryTaskFactory taskFactory;

	/**
	 * Create a geometry descriptor task provider
	 * 
	 * @param schemaType the schema type 
	 */
	public SchemaNoGeometryTaskProvider(SchemaType schemaType) {
		super((schemaType == SchemaType.SOURCE)?("source."):("target."), schemaType);
		
		taskFactory = new NoGeometryTaskFactory();
		
		addFactory(taskFactory);
	}

	/**
	 * @see AbstractSchemaTaskProvider#generateAttributeTasks(AttributeDefinition, Collection)
	 */
	@Override
	protected void generateAttributeTasks(AttributeDefinition attribute,
			Collection<Task> taskList) {
		// no tasks based on attributes
	}

	/**
	 * @see AbstractSchemaTaskProvider#generateElementTasks(SchemaElement, Collection)
	 */
	@Override
	protected void generateElementTasks(SchemaElement element,
			Collection<Task> taskList) {
		Task task = taskFactory.createTask(serviceProvider, element);
		if (task != null) {
			taskList.add(task);
		}
	}

}
