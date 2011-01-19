/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringWizardPage.java,v 1.1 2011/01/19 12:09:31 tarendt Exp $
 */
 package org.eclipse.emf.refactor.refactorings.ecore.henshin.renameedatatypeusinghenshin;

import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * Class for setting model refactoring specific parameters
 * by the user.
 * @generated
 */
public class RefactoringWizardPage extends 
				UserInputWizardPage implements Listener {
				
	/**
	 * Controller of the EMF model refactoring.
	 * @generated
	 */			
	private final RefactoringController controller;
	
	/**
	 * Label for each parameter.
	 * @generated
	 */
	private Label newNameLabel;
	
	/**
	 * TextField for each parameter.
	 * @generated
	 */
	private Text newNameWidget;

	/**
	 * Default constructor using a name and the controller of the 
	 * EMF model refactoring.
	 * @param name Name of the WizardPage.
	 * @param controller Controller of the EMF model refactoring.
	 * @generated
	 */
	public RefactoringWizardPage
		(String name, RefactoringController controller) {
		super(name);
		this.controller = controller;
	}

	/**
	 * @see org.eclipse.swt.widgets.Listener#
	 * handleEvent(org.eclipse.swt.widgets.Event)
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handleEvent(Event event) {		
		getWizard().getContainer().updateButtons();
				
		if (newNameWidget != null) {
			String newName = newNameWidget.getText();
			if (!newName.isEmpty()){
				((RefactoringDataManagement) 
						this.controller.getDataManagementObject()).
						getInPortByName("newName").
						setValue(newName);
			} else {
				((RefactoringDataManagement) 
						this.controller.getDataManagementObject()).
						getInPortByName("newName").
						setValue("unspecified");
			}
		}

	}
	
	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#
	 * createControl(org.eclipse.swt.widgets.Composite)
	 * @generated
	 */
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayout(gl);
			
		
		newNameLabel = new Label(composite, SWT.NONE);
		newNameLabel.setText("New name of the EDataType: ");
		newNameLabel.setEnabled(true);
		
		newNameWidget = new Text(composite, SWT.BORDER);
		newNameWidget.setToolTipText
				("value of variable 'newName'");
		newNameWidget.setEnabled(true);
		newNameWidget.setLayoutData(gd);
		newNameWidget.addListener(SWT.Modify, this);
		
		
		setControl(composite);
	}
	
}
	