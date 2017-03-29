/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringWizard.javajet,v 1.1 2010/07/15 13:08:44 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.samelabel;

import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.ui.AbstractRefactoringWizard;

public class RefactoringWizard extends AbstractRefactoringWizard {

	public RefactoringWizard(IController controller) {
		super(controller);
	}

	@Override
	protected void addUserInputPages() {
		addPage(new RefactoringWizardPage(controller.getParent().getName(), (RefactoringController) controller));
	}

}
