/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringGuiHandler.javajet,v 1.1 2010/07/15 13:08:44 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.mergeassociations;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.interfaces.IGuiHandler;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.uml2.uml.Association;
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
		if (selection == null) {
			return false;
		}
		
		List<EObject> eselection = selection.stream()
				.filter(this::notDiagram)
				.collect(Collectors.toList());
		
		return eselection.size() >= 2
				&& eselection.stream()
					.filter(RefactoringGuiHandler::isAssociation)
					.count() == eselection.size()-1
				&& eselection.stream()
					.filter(RefactoringGuiHandler::isClass)
					.count() == 1;
	}
	
	private boolean notDiagram(EObject eObject) {
		return !(eObject instanceof Diagram);
	}

	public static boolean isAssociation(EObject eObject) {
		return eObject != null && eObject instanceof Association;
	}
	
	public static boolean isClass(EObject eObject) {
		return eObject != null && eObject instanceof Class;
	}
}
