/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.moveoutofcomposite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.interfaces.IDataManagement;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.LtkEmfRefactoringProcessorAdapter;
//import org.eclipse.emf.refactor.service.utils.ElementsManagerUtils;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;

import com.google.inject.Guice;

public final class RefactoringController implements IController {

	private Refactoring parent;
	private RefactoringDataManagement dataManagement = new RefactoringDataManagement();
	private List<EObject> selection = new ArrayList<>();
	private InternalRefactoringProcessor refactoringProcessor = null;
	
	private State composite = null;
	private State state = null;
	private List<Transition> innerIncomings = null;
	private List<Transition> innerOutgoings = null;
	private List<Transition> outerIncomings = null;
	private List<Transition> outerOutgoings = null;
	
	

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
				System.out.println("apply");
				//TODO set the container to the outer region
				state.setContainer(composite.getContainer());
				state.getOutgoings().forEach(t -> t.setContainer(composite.getContainer()));
				//TODO entries and exits
				//TODO if any transition's source is the initial state,
				// it's a simple incoming transition to the state
				// instead of the composite -> remove the transition to
				// the composite, create to the sub, if the initial
				// does not have other transitions, delete the initial
				List<Transition> simpleIncomings = innerIncomings.stream()
					.filter(trans -> trans.getSource() instanceof Pseudostate
							&& ((Pseudostate)trans.getSource()).getKind().equals(PseudostateKind.INITIAL_LITERAL))
							.collect(Collectors.toList());
				
				
				//TODO remove old transition
//				DiagramRefreshService d = Guice.createInjector().getInstance(DiagramRefreshService.class);
//				ElementsManagerUtils e = Guice.createInjector().getInstance(ElementsManagerUtils.class);
				
//				e.removeElement(state);
//				d.putToDiagram(Arrays.asList(state));
//				innerIncomings.forEach(t -> d.putToDiagram(Arrays.asList(t)));
//				outerIncomings.forEach(t -> d.putToDiagram(Arrays.asList(t)));
//				innerOutgoings.forEach(t -> d.putToDiagram(Arrays.asList(t)));
//				outerOutgoings.forEach(t -> d.putToDiagram(Arrays.asList(t)));
				
				
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
			
			state = (State) selection.get(0);
			composite = (State) state.getOwner().getOwner();
			innerIncomings = new ArrayList<>();
			innerOutgoings = new ArrayList<>();
			outerIncomings = new ArrayList<>();
			outerOutgoings = new ArrayList<>();
			
			//TODO entries and exits
			state.getIncomings().stream()
			.forEach(trans -> {
				if (state.getContainer().allOwnedElements().contains(trans.getSource())) {
					innerIncomings.add(trans);
				} else {
					outerIncomings.add(trans);
				}
			});
			state.getOutgoings().stream()
			.forEach(trans -> {
				if (state.getContainer().allOwnedElements().contains(trans.getTarget())) {
					innerOutgoings.add(trans);
				} else {
					outerOutgoings.add(trans);
				}
				
			});
			
			if (composite.getDoActivity() != null
					&& state.getDoActivity() != null) {
				result.addFatalError("Composite superstate and its substate must not have do activity at the same time.");
			}
			
			
			return result;
		}


		@Override
		public RefactoringStatus checkFinalConditions() {
			RefactoringStatus result = new RefactoringStatus();
			return result;
		}

	}

}