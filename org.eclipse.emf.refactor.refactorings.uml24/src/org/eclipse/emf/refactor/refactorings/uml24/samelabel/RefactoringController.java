/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.samelabel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.interfaces.IDataManagement;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.LtkEmfRefactoringProcessorAdapter;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;

public final class RefactoringController implements IController {

	private Refactoring parent;
	private RefactoringDataManagement dataManagement = new RefactoringDataManagement();
	private List<EObject> selection = new ArrayList<>();
	private InternalRefactoringProcessor refactoringProcessor = null;

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
				Transition selected = (Transition) dataManagement.getInPortByName("transition").getValue();
				Transition t = (Transition) dataManagement.getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
				
				t.setEffect(selected.getEffect());
				t.setGuard(selected.getGuard());
				
				if (!t.getTriggers().equals(selected.getTriggers())) {
					t.getTriggers().clear();
					
					EList<Trigger> triggers = new BasicEList<Trigger>(selected.getTriggers().size());
					Collections.copy(triggers, selected.getTriggers());
					t.getTriggers().addAll(selected.getTriggers());
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
			return new RefactoringStatus();
		}

		@Override
		public RefactoringStatus checkFinalConditions() {
			RefactoringStatus result = new RefactoringStatus();
			
			Transition selected = (Transition) dataManagement.getInPortByName("transition").getValue();
			Transition t = (Transition) dataManagement.getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
			
			if (t.getEffect() != null && selected.getEffect() == null || t.getEffect() == null && selected.getEffect() != null || t.getEffect() != null && selected.getEffect() != null && !t.getEffect().equals(selected.getEffect())) {
				result.addFatalError("Effects are different.");
			}
			
			if (t.getTriggers() != null && selected.getTriggers() == null || t.getTriggers() == null && selected.getTriggers() != null || t.getEffect() != null && selected.getEffect() != null && !t.getTriggers().equals(selected.getTriggers())) {
				result.addFatalError("Triggers are different.");
			}
			
			if (t.getGuard() != null && selected.getGuard() == null || t.getGuard() == null && selected.getGuard() != null || t.getEffect() != null && selected.getEffect() != null && !t.getGuard().equals(selected.getGuard())) {
				result.addFatalError("Guards are different.");
			}
			
			return result;
		}

	}

}