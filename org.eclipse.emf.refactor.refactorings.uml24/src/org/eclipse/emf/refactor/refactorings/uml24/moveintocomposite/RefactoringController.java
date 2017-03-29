/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.moveintocomposite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.interfaces.IDataManagement;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.LtkEmfRefactoringProcessorAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
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
	
	private State selectedState;
	private State compositeState;
	private List<State> subStates;

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
	
	private List<IGraphicalEditPart> editParts;
	
	public void setEditParts(List<IGraphicalEditPart> editParts) {
		this.editParts = editParts;
	}
	
	

	private Runnable applyRefactoring() {
		return new Runnable() {
			@Override
			public void run() {
				System.out.println("Gogogo");
				selectedState.setContainer(compositeState.getRegions().get(0));
				
				selectedState.getOutgoings().removeIf(t -> compositeState.getOutgoings().stream().anyMatch(o -> sameLabel(o, t) && o.getTarget() == t.getTarget()));
				selectedState.getOutgoings().forEach(t -> t.setContainer(compositeState.getRegions().get(0)));
				if (compositeState.getDoActivity() != null) {
					selectedState.setDoActivity(null);
				}
				
				if (compositeState.getEntry() != null) {
					selectedState.getOutgoings().stream().filter(t -> subStates.contains((State) t.getTarget())).forEach(t -> t.setEffect(EcoreUtil.copy(compositeState.getEntry())));
				}
				
				if (compositeState.getExit() != null) {
					selectedState.getIncomings().stream().filter(t -> subStates.contains((State) t.getSource())).forEach(t -> t.setEffect(EcoreUtil.copy(compositeState.getExit())));
				}
				
//				Runnable asyncExec = new Runnable() {
//					public void run() {
//						RefactorCommandContainer container = new RefactorCommandContainer("Move state into composite", editParts);
//						DiagramRefreshService d = Guice.createInjector().getInstance(DiagramRefreshService.class);
//						
//						
////						container.removeElement(selectedEObject);
//						container.moveElements(Arrays.asList(selectedState), compositeState.getRegions().get(0));
//						d.executeCommandContainer(container);
//					}
//				};
//				Display.getDefault().asyncExec(asyncExec);
			}
		};
	}
	
	private boolean sameLabel(Transition t1, Transition t2) {
//		if (t1.getEffect() != null && t2.getEffect() == null || t1.getEffect() == null && t2.getEffect() != null || t.getEffect() != null && selected.getEffect() != null && !t.getEffect().equals(selected.getEffect())) {
//			return false;
//		}
//		
//		if (t.getTriggers() != null && selected.getTriggers() == null || t.getTriggers() == null && selected.getTriggers() != null || t.getEffect() != null && selected.getEffect() != null && !t.getTriggers().equals(selected.getTriggers())) {
//			result.addFatalError("Triggers are different.");
//		}
//		
//		if (t.getGuard() != null && selected.getGuard() == null || t.getGuard() == null && selected.getGuard() != null || t.getEffect() != null && selected.getEffect() != null && !t.getGuard().equals(selected.getGuard())) {
//			result.addFatalError("Guards are different.");
//		}
		return true;
	}

	private boolean sameLabel(Behavior b1, Behavior b2) {
		return true;
	}
	
	private List<State> getIncomingsSources(State state) {
		List<State> sources = new ArrayList<>();
		
		state.getIncomings().forEach(i -> sources.add((State) i.getSource()));
		
		return sources;
	}
	
	private List<State> getOutgoingsTargets(State state) {
		List<State> targets = new ArrayList<>();
		
		state.getOutgoings().forEach(i -> targets.add((State) i.getTarget()));
		
		return targets;
	}

	public final class InternalRefactoringProcessor extends
			LtkEmfRefactoringProcessorAdapter {

		private InternalRefactoringProcessor(List<EObject> selection) {
			super(getParent(), selection, applyRefactoring());
		}
		

		@Override
		public RefactoringStatus checkInitialConditions() {
			RefactoringStatus result = new RefactoringStatus();
			
			
			return result;
		}

		@Override
		public RefactoringStatus checkFinalConditions() {
			RefactoringStatus result = new RefactoringStatus();
			

			selectedState = (State) selection.get(0);
			compositeState = (State) dataManagement.getInPortByName("compositeState").getValue();

			subStates = compositeState.getRegions().get(0).getOwnedElements().stream().filter(s -> s instanceof State).map(s -> (State) s).collect(Collectors.toList());
			
			if (subStates.contains(selectedState)) {
				result.addFatalError("Selected composite state already contains the state.");
			}
			
			if (!compositeState.getContainer().equals(selectedState.getContainer())) {
				result.addFatalError("Selected composite state's container is different from the state's container.");
			}
			
			if (compositeState.getRegions().size() > 1) {
				result.addFatalError("Selected composite state is concurrent.");
			}
			
			boolean matchOutgoingTransitions = compositeState.getOutgoings().stream().allMatch(each -> {
				return selectedState.getOutgoings().stream().anyMatch(t -> {
					return sameLabel(each, t) && t.getTarget() == each.getTarget();
				});
			});
			if (!matchOutgoingTransitions) {
				result.addFatalError("Outgoing transition does not match.");
			}
			
			if (compositeState.getDoActivity() != null && !sameLabel(compositeState.getDoActivity(), selectedState.getDoActivity())) {
				result.addFatalError("Problem with do activity.");
			}
			
			List<State> stateIncomingsSources = getIncomingsSources(selectedState);
			boolean entryAction = compositeState.getEntry() == null || (subStates.containsAll(stateIncomingsSources) && selectedState.getOutgoings().stream().filter(t -> subStates.contains((State) t.getTarget())).allMatch(t -> t.getEffect() == null));
			if (!entryAction) {
				result.addFatalError("Problem with entry action.");
			}
			
			List<State> stateOutgoingsTargets = getOutgoingsTargets(selectedState);
			boolean exitAction = compositeState.getExit() == null || (subStates.containsAll(stateOutgoingsTargets) && selectedState.getIncomings().stream().filter(t -> subStates.contains((State) t.getSource())).allMatch(t -> t.getEffect() == null));
			if (!exitAction) {
				result.addFatalError("Problem with entry action.");
			}
			
//			if (!subStates.contains(selectedState)) {
//				result.addFatalError("After refactoring: Selected composite state does not contain the state.");
//			}
//			
//			boolean outgoings = compositeState.getOutgoings().stream().filter(each -> {
//				return selectedState.getOutgoings().stream().anyMatch(t -> {
//					return sameLabel(t, each) && t.getTarget() == each.getTarget();
//				});
//			}).count() == 0;
//			if (!outgoings) {
//				
//			}
//			
//			if (compositeState.getDoActivity() != null && selectedState.getDoActivity() != null) {
//				
//			}
//			
//			boolean entry = compositeState.getEntry() == null || selectedState.getOutgoings().stream().filter(t -> subStates.contains((State) t.getTarget())).allMatch(t -> sameLabel(t.getEffect(), compositeState.getEntry()));
//			if (!entry) {
//				
//			}
//			
//			boolean exit = compositeState.getExit() == null || selectedState.getIncomings().stream().filter(t -> subStates.contains((State) t.getSource())).allMatch(t -> sameLabel(t.getEffect(), compositeState.getExit()));
//			if (!exit) {
//				
//			}
			
			return result;
		}

	}

}