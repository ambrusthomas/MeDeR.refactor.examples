/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.unfoldoutgoing;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.extension.EmfNotifierController;
import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.interfaces.IDataManagement;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.LtkEmfRefactoringProcessorAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.Vertex;

public final class RefactoringController extends EmfNotifierController implements IController {

	private Refactoring parent;
	private RefactoringDataManagement dataManagement = new RefactoringDataManagement();
	private List<EObject> selection = new ArrayList<>();
	private InternalRefactoringProcessor refactoringProcessor = null;
	
	private Transition transition;
	private State compositeState;
	private Vertex target;
	private Behavior effect;
	private EList<Trigger> triggers;
	
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
	@Override
	public void setEditParts(List<IGraphicalEditPart> editParts) {
		this.editParts = editParts;
	}
	
	private List<Transition> newTransitions;

//	private Runnable applyRefactoring() {
//		return new Runnable() {
//			@Override
//			public void run() {
//				
//
////				refreshPapyrusDiagram();
//			}
//		};
//	}
//	
	public final class InternalRefactoringProcessor extends LtkEmfRefactoringProcessorAdapter {

		private InternalRefactoringProcessor(List<EObject> selection) {
			super(getParent(), selection, applyRefactoring());
		}
		

		@Override
		public RefactoringStatus checkInitialConditions() {
			RefactoringStatus refactoringStatus = new RefactoringStatus();
			
			transition = (Transition) selection.get(0);
			compositeState = (State) transition.getSource();
			
			if (!compositeState.isComposite()) {
				refactoringStatus.addFatalError("Source is not composite.");
			}
			
			target = transition.getTarget();
			effect = transition.getEffect();
			triggers = transition.getTriggers();
			
			return refactoringStatus;
		}


		@Override
		public RefactoringStatus checkFinalConditions() {
			RefactoringStatus refactoringStatus = new RefactoringStatus();
			
//			if (!subStates(compositeState).stream().allMatch(s -> s.getOutgoings().stream().filter(t -> isSameToSelection(t)).count() == 1)) {
//				refactoringStatus.addFatalError("Refactoring had a problem.");
//			}
//			
//			if (transition.getSource() != null || transition.getTarget() != null ||
//					transition.getTriggers() != null || transition.getEffect() != null) {
//				refactoringStatus.addFatalError("Old transition exists.");
//			}
			
			return refactoringStatus;
		}

		private boolean isSameToSelection(Transition t) {
			return t.getTarget().equals(target) && t.getEffect().equals(effect) &&
					t.getTriggers().equals(triggers);
		}

	}
	
	private List<State> subStates(State compositeState) {
		List<State> subStates = new ArrayList<>();
		
		compositeState.getOwnedElements().stream().filter(e -> e instanceof Region).forEach(e -> {
			e.getOwnedElements().stream().filter(ee -> ee instanceof State).forEach(ee -> {
				subStates.add((State) ee);
			});
		});
		
		return subStates;
	}
	@Override
	protected void refactoringBody() {
		newTransitions = new ArrayList<>();
		subStates(compositeState).forEach(s -> {
			//TODO s.getContainer().createTransition(name)
			Transition t = UMLFactory.eINSTANCE.createTransition();
			t.setSource(s);
			t.setTarget(target);
			t.setEffect(effect);
			t.getTriggers().addAll(triggers);
//			
//			s.getOutgoings().add(t);
			
			t.setContainer(s.getContainer());
			
			newTransitions.add(t);
		});
		transition.setSource(null);
		transition.setTarget(null);
		EcoreUtil.remove(transition);
	}
}