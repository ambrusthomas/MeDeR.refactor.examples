/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.splittransitionintoinit;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.interfaces.IDataManagement;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.LtkEmfRefactoringProcessorAdapter;
//import org.eclipse.emf.refactor.service.utils.ElementsManagerUtils;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UMLFactory;

import com.google.inject.Guice;

public final class RefactoringController implements IController {

	private Refactoring parent;
	private RefactoringDataManagement dataManagement = new RefactoringDataManagement();
	private List<EObject> selection = new ArrayList<>();
	private InternalRefactoringProcessor refactoringProcessor = null;
	
	private State composite = null;
	private State target = null;
	private State source = null;
	private Transition transition = null;
	
	

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
				//TODO create pseudostate
				Pseudostate initial = UMLFactory.eINSTANCE.createPseudostate();
				initial.setContainer(composite.getRegions().get(0));
				initial.setName("Init");
				initial.setKind(PseudostateKind.INITIAL_LITERAL);

				//TODO create new transitions
				Transition newTransition1 = ((Region) composite.getOwner()).createTransition(null);
				newTransition1.setSource(source);
				newTransition1.setTarget(composite);
				newTransition1.setEffect(transition.getEffect());
				newTransition1.setGuard(transition.getGuard());
				newTransition1.getTriggers().addAll(transition.getTriggers());
				
				Transition newTransition2 = composite.getRegions().get(0).createTransition(null);
				newTransition2.setTarget(target);
				newTransition2.setSource(initial);
				newTransition2.setEffect(null);
				newTransition2.setGuard(null);
				

//				transition.getTarget().getIncomings().remove(transition);
//				transition.getSource().getOutgoings().remove(transition);
				EcoreUtil.remove(transition);
				
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
			transition = (Transition) selection.get(0);
			source = (State) transition.getSource();
			target = (State) transition.getTarget();
			composite = (State) target.getOwner().getOwner();
			
			//TODO the composite do not have any initial states
			if (composite.getOwnedElements().stream().filter(
					e -> e instanceof Pseudostate
					&& ((Pseudostate) e).getKind().equals(PseudostateKind.INITIAL_LITERAL)).count() > 0) {
				result.addFatalError("The composite must not have an initial pseudostate.");
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