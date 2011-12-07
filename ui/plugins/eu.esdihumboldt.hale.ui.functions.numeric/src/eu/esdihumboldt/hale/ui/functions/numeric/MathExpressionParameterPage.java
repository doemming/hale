package eu.esdihumboldt.hale.ui.functions.numeric;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.iabcinc.jmep.Environment;
import com.iabcinc.jmep.Expression;
import com.iabcinc.jmep.hooks.Constant;

import eu.esdihumboldt.hale.common.align.extension.function.FunctionParameter;
import eu.esdihumboldt.hale.common.align.model.Cell;
import eu.esdihumboldt.hale.common.align.model.Entity;
import eu.esdihumboldt.hale.ui.HaleWizardPage;
import eu.esdihumboldt.hale.ui.common.definition.viewer.DefinitionLabelProvider;
import eu.esdihumboldt.hale.ui.function.generic.AbstractGenericFunctionWizard;
import eu.esdihumboldt.hale.ui.function.generic.pages.ParameterPage;

/**
 * Parameter page for mathematical expression function.
 * 
 * @author Kai Schwierczek
 */
public class MathExpressionParameterPage extends HaleWizardPage<AbstractGenericFunctionWizard<?, ?>> implements
		ParameterPage {
	private String initialExpression = "";
	private Text expressionField;
	private ListViewer varList;
	private String[] variables = new String[0];
	private Environment environment = new Environment();

	/**
	 * Default constructor.
	 */
	public MathExpressionParameterPage() {
		super("expression");

		setTitle("Function parameters");
		setDescription("Enter a mathematical expression");

		setPageComplete(false);
	}

	/**
	 * @see ParameterPage#setParameter(Set, ListMultimap)
	 */
	@Override
	public void setParameter(Set<FunctionParameter> params, ListMultimap<String, String> initialValues) {
		// this page is only for parameter expression, ignore params
		// XXX don't ignore params? more generic approach necessary? how?
		if (initialValues != null) {
			List<String> initialData = initialValues.get("expression");
			if (initialData.size() > 0)
				initialExpression = initialData.get(0);
		}
	}

	/**
	 * @see ParameterPage#getConfiguration()
	 */
	@Override
	public ListMultimap<String, String> getConfiguration() {
		ListMultimap<String, String> params = ArrayListMultimap.create();
		params.put("expression", expressionField.getText());
		return params;
	}

	/**
	 * @see HaleWizardPage#onShowPage(boolean)
	 */
	@Override
	protected void onShowPage(boolean firstShow) {
		Cell cell = getWizard().getUnfinishedCell();

		// update variables as they could have changed
		List<? extends Entity> sourceEntities = cell.getSource().get("var");
		variables = new String[sourceEntities.size()];
		Iterator<? extends Entity> iter = sourceEntities.iterator();
		DefinitionLabelProvider dlp = new DefinitionLabelProvider(true, true);
		for (int i = 0; i < variables.length; i++)
			variables[i] = dlp.getText(iter.next().getDefinition());
		varList.setInput(variables);

		// update environment for verifier
		environment = new Environment();
		// add dummy variables
		for (String var : variables) {
			environment.addVariable(var, new Constant(new Double(1)));
		}
		// update check whether current value is valid
		expressionField.setText(expressionField.getText());

		((Composite) getControl()).layout();
		getWizard().getShell().pack();
	}

	/**
	 * @see HaleWizardPage#createContent(Composite)
	 */
	@Override
	protected void createContent(Composite page) {
		page.setLayout(GridLayoutFactory.swtDefaults().create());

		// input field
		expressionField = new Text(page, SWT.SINGLE | SWT.BORDER);
		expressionField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		expressionField.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				try {
					Expression ex = new Expression(expressionField.getText(), environment);
					ex.evaluate();
					setErrorMessage(null);
					setPageComplete(true);
				} catch (Exception ex) {
					String message = ex.getLocalizedMessage();
					if (message != null && !message.isEmpty())
						setErrorMessage(ex.getLocalizedMessage());
					else
						setErrorMessage("Invalid variable");
					setPageComplete(false);
				}
			}
		});
		expressionField.setText(initialExpression);

		// variables
		Label label = new Label(page, SWT.NONE);
		label.setText("Available variables (double click to insert)");
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		varList = new ListViewer(page);
		varList.getControl().setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		varList.setContentProvider(new ArrayContentProvider());
		varList.getList().addMouseListener(new MouseAdapter() {
			/**
			 * @see MouseAdapter#mouseDoubleClick(MouseEvent)
			 */
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				int index = varList.getList().getSelectionIndex();
				if (index >= 0) {
					String var = varList.getList().getItem(index);
					expressionField.insert(var);
					expressionField.setFocus();
				}
			}
		});
	}
}
