/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringWizardPage.javajet,v 1.1 2010/07/15 13:08:44 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.insertgeneralizable;

import java.util.List;

import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.uml2.uml.Property;

public class RefactoringWizardPage extends UserInputWizardPage implements Listener {

	private final RefactoringController controller;
	private List<Property> possibleAssociationEnds;
	private Combo endCombo;
	
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
		
		System.out.println("wizard");
		new Label(composite, SWT.NONE).setText("This refactoring merge associations into a superclass.");
		
		possibleAssociationEnds = controller.getPossibleAssociationEnds();
		
		if (possibleAssociationEnds != null) {
			endCombo = new Combo(composite, SWT.BORDER);
			for (Property prop : possibleAssociationEnds) {
				endCombo.add(prop.getName() + prop.isNavigable());
			}
			
		}
		
		setControl(composite);
	}

	@Override
	public void handleEvent(Event event) {		
	}
	
}
