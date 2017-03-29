/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringWizardPage.javajet,v 1.1 2010/07/15 13:08:44 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.mergeassociations;

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
	
	private RefactoringController controller;
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
		System.out.println("wizard");
		new Label(composite, SWT.NONE).setText("This refactoring merge associations into a superclass.");
		
		List<Property> possibleAssociationEnds =
				controller.getPossibleAssociationsEnds();
		if (possibleAssociationEnds != null) {
			endCombo = new Combo(composite, SWT.BORDER);
			for (Property prop : possibleAssociationEnds) {
				endCombo.add(prop.getName() + prop.isNavigable());
			}
			endCombo.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println(endCombo.getSelectionIndex());
					controller.setCommonNumberInRelated(endCombo.getSelectionIndex());
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {}
				
			});

			endCombo.setLayoutData(gd);
		}
		
		setControl(composite);
	}

	@Override
	public void handleEvent(Event event) {		
	}
	
}
