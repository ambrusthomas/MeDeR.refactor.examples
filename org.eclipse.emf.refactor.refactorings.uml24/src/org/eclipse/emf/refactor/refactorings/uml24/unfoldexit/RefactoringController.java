/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.unfoldexit;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.interfaces.IDataManagement;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.LtkEmfRefactoringProcessorAdapter;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.State;

public final class RefactoringController implements IController {

	private Refactoring parent;
	private RefactoringDataManagement dataManagement = new RefactoringDataManagement();
	private List<EObject> selection = new ArrayList<>();
	private InternalRefactoringProcessor refactoringProcessor = null;
	
	private Behavior exitBehavior;

	@Override
	public Refactoring getParent() {
		return this.parent;
	}

	@Override
	public void setParent(Refactoring emfRefactoring) {
		this.parent = emfRefactoring;
	}

	@Override
	public IDataManagement getDataManagementObject() {
		return this.dataManagement;
	}

	@Override
	public RefactoringProcessor getLtkRefactoringProcessor() {
		return this.refactoringProcessor;
	}

	@Override
	public void setSelection(List<EObject> selection) {
		this.selection = selection;
		this.refactoringProcessor = new InternalRefactoringProcessor(this.selection);
	}

	private Runnable applyRefactoring() {
		return new Runnable() {
			@Override
			public void run() {
				State state = (State) selection.get(0);
				
				state.getOutgoings().forEach(o -> o.setEffect(exitBehavior));
				state.setExit(null);
			}
		};
	}

	public final class InternalRefactoringProcessor extends
			LtkEmfRefactoringProcessorAdapter {

		private InternalRefactoringProcessor(List<EObject> selection) {
			super(getParent(), selection, applyRefactoring());
		}

		@Override
		public RefactoringStatus checkInitialConditions() {
			RefactoringStatus result = new RefactoringStatus();
			State state = (State) selection.get(0);

			if (state.getExit() == null) {
				result.addFatalError("Exit action must exists.");
			}
			exitBehavior = state.getExit();
			
			if (!state.getOutgoings().stream().allMatch(o -> o.getEffect() == null)) {
				result.addFatalError("All outgoing's effect must be null.");
			}
			
			if (!state.getOutgoings().stream().filter(o -> o.getTarget() instanceof State).allMatch(o -> o.getTarget().getContainer().equals(state.getContainer()))) {
				result.addFatalError("The are outgoings, which target's container is different from the state's container.");
			}
			
			return result;
		}

		@Override
		public RefactoringStatus checkFinalConditions() {
			RefactoringStatus result = new RefactoringStatus();
//			State state = (State) selection.get(0);
//			
//			if (state.getExit() != null) {
//				result.addFatalError("Exit action still exists.");
//			}
//			
//			if (!state.getOutgoings().stream().allMatch(o -> exitBehavior.equals(o.getEffect()))) {
//				result.addFatalError("All outgoing's effect should be the same as the exit effect.");
//			}

			return result;
		}

	}

}