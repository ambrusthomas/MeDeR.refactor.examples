/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringWizardPage.javajet,v 1.1 2010/07/15 13:08:44 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.foldentry;

import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class RefactoringWizardPage extends UserInputWizardPage implements Listener {

	public RefactoringWizardPage(String name) {
		super(name);
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;

		new Label(composite, SWT.NONE).setText("This refactoring folds incoming actions into entry action in this state.");
		
		setControl(composite);
	}

	@Override
	public void handleEvent(Event event) {		
	}
	
}
