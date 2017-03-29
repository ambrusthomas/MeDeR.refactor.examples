/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringDataManagement.javajet,v 1.2 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.unfoldexit;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Port;
import org.eclipse.emf.refactor.refactoring.runtime.DataManagementAdapter;
import org.eclipse.uml2.uml.State;

public class RefactoringDataManagement extends DataManagementAdapter {

	protected final String SELECTEDEOBJECT = "selectedEObject";

	public RefactoringDataManagement() {
		this.addPorts();
	}

	private void addPorts() {
		this.inPorts.add(new Port<State>(SELECTEDEOBJECT, State.class));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void preselect(List<EObject> selection) {
		getInPortByName(SELECTEDEOBJECT).setValue((State) selection.get(0));
	}

}
