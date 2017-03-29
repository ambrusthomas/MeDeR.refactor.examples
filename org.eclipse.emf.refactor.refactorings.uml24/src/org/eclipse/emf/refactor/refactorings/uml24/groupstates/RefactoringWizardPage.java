/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringWizardPage.javajet,v 1.1 2010/07/15 13:08:44 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.groupstates;

import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class RefactoringWizardPage extends UserInputWizardPage implements Listener {

	private final RefactoringController controller;
	Text newState;

	public RefactoringWizardPage(String name, RefactoringController controller) {
		super(name);
		this.controller = controller;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayout(gl);

		new Label(composite, SWT.NONE).setText("Name of the new surrounding state: ");
		newState = new Text(composite, SWT.BORDER);
		newState.setEnabled(true);
		newState.setLayoutData(gd);
		newState.addListener(SWT.Modify, this);
		
		setControl(composite);
	}

	@Override
	public void handleEvent(Event event) {		
		String newStateName = newState.getText();
		if (!newStateName.isEmpty()){
			setValue(newStateName);
		} else {
			setValue("unspecified");
		}
	}

	@SuppressWarnings("unchecked")
	private void setValue(String value) {
		((RefactoringDataManagement) this.controller.getDataManagementObject()).getInPortByName("newState").setValue(value);
	}
	
}
