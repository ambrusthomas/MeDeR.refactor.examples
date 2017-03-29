/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.removegeneralizable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.interfaces.IDataManagement;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.LtkEmfRefactoringProcessorAdapter;
import org.eclipse.emf.refactor.refactorings.uml24.UmlUtils;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Property;

import com.google.inject.Guice;

public final class RefactoringController implements IController {

	private Refactoring parent;
	private RefactoringDataManagement dataManagement = new RefactoringDataManagement();
	private List<EObject> selection = new ArrayList<>();
	private InternalRefactoringProcessor refactoringProcessor = null;
	
	private Class generalizable = null;
	private Association assoc = null;
//	private List<Association> assocs = null;
//	private List<Boolean> sameOrder = null;
//	private Set<Class> otherClasses = null;
//	private int commonNumberInRelated;
	
	private boolean canChoose;
	private int properEnd;
	private List<Class> subClasses;
	
	public List<Property> getPossibleAssociationEnds() {
		if (canChoose)
			return assoc.getMemberEnds();
		else return null;
	}
	
	public void setProperEnd(int properEnd) {
		this.properEnd = properEnd;
	}

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
				
				//TODO attributes
				System.out.println("apply");
				//TODO create new generalizations for subclasses
				//TODO put them on diagram
				
				for (Class sub : subClasses) {
					for (Class sup : generalizable.getSuperClasses()) {
						Generalization gen = sub.createGeneralization(sup);
//						d.putToDiagram(Arrays.asList(gen));
					}
				}
				
				//TODO remove the element's generalizations from diagram
				//TODO remove the element's generalization
				List<Generalization> gens = new ArrayList<>();
				generalizable.getGeneralizations().forEach(gen -> {
					gens.add(gen);
				});;
				gens.forEach(gen -> {
					if (selection.size() == 2) {
						//TODO ElementsController.removeConnector((Diagram) selection.get(1), gen);
					}
					generalizable.getGeneralizations().remove(gen);
				});
				
				for (Class sub : subClasses) {
					Generalization gen = sub.getGeneralization(generalizable);
					if (selection.size() == 2) {
						//TODO ElementsController.removeConnector((Diagram) selection.get(1), gen);
					}
					
					sub.getGeneralizations().remove(gen);
				}
				
				//TODO remove the element from diagram
				if (selection.size() == 2) {
					View view = (View) selection.get(1);
					TreeIterator<EObject> iter = view.eAllContents();
					while (iter.hasNext()) {
						EObject eo = iter.next();
						if (eo instanceof View) {
							View view2 = (View) eo;
							if (view2.getElement() == selection.get(0)) {
								((View) view2.eContainer()).removeChild(view2);
								break;
							}
						}
					}
					//((View) view.eContainer()).removeChild(view);
				}
				//TODO remove the element
				System.out.println("de");
				generalizable.getPackage().getPackagedElements().remove(generalizable);
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
			//Association assoc = (Association) selection.get(0);
			//TODO the selected generalizable has at least one superclass and at least one subclass
			generalizable = (Class) selection.get(0);
			
			
			
			if (generalizable.getSuperClasses().isEmpty()) {
				result.addFatalError("The selected class must have at least one superclass.");
				return result;
			}
			
			//TODO has that class any subclasses -> gather them
			subClasses = new ArrayList<>();
			List<Class> allClasses = UmlUtils.getAllClasses(generalizable.getModel());
			for (Class cl : allClasses) {
				if (cl.getSuperClasses().contains(generalizable)){
					subClasses.add(cl);
				}
			}
			
			if (subClasses.isEmpty()) {
				result.addFatalError("The selected class must have at least one subclass.");
				return result;
			}
			
			
			return result;
		}

		@Override
		public RefactoringStatus checkFinalConditions() {
			RefactoringStatus result = new RefactoringStatus();
			//Association association = (Association) selection.get(0);
			System.out.println("final");
			/*if (state.getExit() != null) {
				result.addFatalError("Exit action must exists.");
			}*/
			
			
//			if (!state.getOutgoings().stream().allMatch(o -> o.getEffect().isCompatibleWith(p))) {
//				result.addFatalError("All outgoing's effect must be null.");
//			}
//			
//			if (!state.getOutgoings().stream().filter(o -> o.getTarget() instanceof State).allMatch(o -> o.getTarget().getContainer().equals(state.getContainer()))) {
//				result.addFatalError("The are outgoings, which container is different from the state's container.");
//			}			
			return result;
		}

	}

}