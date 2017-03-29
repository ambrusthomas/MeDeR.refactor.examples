/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.foldexit;

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
import org.eclipse.uml2.uml.Transition;

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
				
				state.setExit(exitBehavior);
				for (Transition t : state.getOutgoings()) {
					t.setEffect(null);
				}
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

			if (state.getExit() != null) {
				result.addFatalError("Exit action exists.");
			}

			if (state.getOutgoings() == null) {
				result.addFatalError("There are no outgoings");
			}
			
			state.getOutgoings().stream().findAny().ifPresent(i -> {
				exitBehavior = i.getEffect();
				if (exitBehavior == null) {
					if (!state.getOutgoings().stream().allMatch(t -> t.getEffect() == null)) {
						result.addFatalError("Outgoings' effect is not the same effect.");
					}
				} else {
					if (!state.getOutgoings().stream().allMatch(t -> exitBehavior.equals(t.getEffect()))) {
						result.addFatalError("Outgoings' effect is not the same effect.");
					}
				}
			});
			
			if (!state.getOutgoings().stream().filter(o -> o.getTarget() instanceof State).allMatch(o -> o.getTarget().getContainer().equals(state.getContainer()))) {
				result.addFatalError("The are outgoings, which source's container is different from the state's container.");
			}
			
			return result;
		}

		@Override
		public RefactoringStatus checkFinalConditions() {
			RefactoringStatus result = new RefactoringStatus();
//			State state = (State) selection.get(0);
//			
//			if (state.getExit() == null) {
//				result.addFatalError("Exit action must exist.");
//			}
//			
//			if (!state.getOutgoings().isEmpty()) {
//				result.addFatalError("Outgoings still exist.");
//			}
//			
//			if (!state.getExit().equals(exitBehavior)) {
//				result.addFatalError("Exit's behavior is not correct.");
//			}

			return result;
		}

	}

}