/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringGuiHandler.javajet,v 1.1 2010/07/15 13:08:44 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.insertgeneralizable;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.interfaces.IGuiHandler;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.uml2.uml.Class;

public class RefactoringGuiHandler implements IGuiHandler {

	private Refactoring parent;

	@Override
	public Refactoring getParent() {
		return parent;
	}

	@Override
	public void setParent(Refactoring emfRefactoring) {
		this.parent = emfRefactoring;
	}

	@Override
	public org.eclipse.ltk.ui.refactoring.RefactoringWizard show() {
		return new RefactoringWizard((RefactoringController) this.parent.getController());
	}

	@Override
	public boolean showInMenu(List<EObject> selection) {
		if (selection == null || selection.isEmpty()) {
			return false;
		}
		
		return selection.stream().allMatch(this::isClassOrDiagram);
	}
	
	private boolean isClassOrDiagram(EObject obj) {
		return obj instanceof Class || obj instanceof Diagram;
	}
}
