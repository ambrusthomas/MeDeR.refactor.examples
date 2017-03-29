/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringWizardPage.javajet,v 1.1 2010/07/15 13:08:44 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.moveoutofcomposite;

import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class RefactoringWizardPage extends UserInputWizardPage implements Listener {
	
	
	public RefactoringWizardPage(String name, RefactoringController controller) {
		super(name);
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		composite.setLayout(gl);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		System.out.println("wizard");
		Label label = new Label(composite, SWT.NONE);
		label.setText("This refactoring moves a state out of a composite state.");
		label.setLayoutData(gd);
		
		setControl(composite);
	}

	@Override
	public void handleEvent(Event event) {		
	}
	
}
