/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringWizard.java,v 1.1 2011/01/19 12:09:31 tarendt Exp $
 */
 package org.eclipse.emf.refactor.refactorings.ecore.henshin.renameedatatypeusinghenshin;

import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.ui.AbstractRefactoringWizard;

/**
 * Class used for providing an implementation of an LTK
 * RefactoringWizard in EMF Refactor.
 * @generated
 */
public class RefactoringWizard 
    extends AbstractRefactoringWizard {
	
	/**
	 * Default constructor implementation.
	 * @param controller Controller of the EMF model refactoring.
	 * @generated
	 */					
	public RefactoringWizard(IController controller) {
		super(controller);
	}

	/**
	 * @see org.eclipse.ltk.ui.refactoring.RefactoringWizard#
	 * addUserInputPages()
	 * @generated
	 */
	@Override
	protected void addUserInputPages() {
		addPage(new RefactoringWizardPage
			(controller.getParent().getName(), (RefactoringController)controller));
	}

}