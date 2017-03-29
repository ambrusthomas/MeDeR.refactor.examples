/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.insertgeneralizable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.interfaces.IDataManagement;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.LtkEmfRefactoringProcessorAdapter;
import org.eclipse.emf.refactor.refactorings.uml24.UmlUtils;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import com.google.inject.Guice;
//import org.eclipse.emf.refactor.service.utils.ElementsManagerUtils;

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
	private List<Class> superClasses;
	
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
				System.out.println("apply");
				
//				DiagramRefreshService d = Guice.createInjector().getInstance(DiagramRefreshService.class);
				
				//TODO create a new class
				//TODO put it on diagram
				Class newSuperclass = UMLFactory.eINSTANCE.createClass();
				newSuperclass.setName("új class");
				subClasses.get(0).getPackage().getPackagedElements().add(newSuperclass);
				
//				d.putToDiagram(Arrays.asList(newSuperclass));
				
				//TODO create new generalizations for subclasses
				//TODO put them on diagram
				
				

				
				for (Class sub : subClasses) {
						Generalization gen = sub.createGeneralization(newSuperclass);
//						d.putToDiagram(Arrays.asList(gen));
					
				}
				
				//TODO create new generalizations for the new class
				//TODO put them on diagram
				for (Class sup : superClasses) {
						Generalization gen = newSuperclass.createGeneralization(sup);
//						d.putToDiagram(Arrays.asList(gen));
				}
				
				
				//TODO remove the subclasses' generalizations from diagram
				//TODO remove the subclasses' generalizations
//				List<Generalization> gens = generalizable.getGeneralizations();
//				gens.forEach(gen -> {
//					if (selection.size() == 2) {
//						ElementsController.removeConnector((Diagram) selection.get(1), gen);
//					}
//					generalizable.getGeneralizations().remove(gen);
//				});
				
				for (Class sub : subClasses) {
					for (Class sup : superClasses) {
						Generalization gen = sub.getGeneralization(sup);
						if (selection.size() == 2) {
//							d.deleteOldElementsFromDiagram(Arrays.asList(gen));
						}
						
						sub.getGeneralizations().remove(gen);
					}
					
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
			//TODO subclasses and superclasses separated?
			//TODO the superclasses must be superclass of all subclasses ->
			//TODO iterate over all selected classes and intersect the getSuperClasses()
			//TODO if it's empty, then error
			//TODO else do what has to be done O.o
			//TODO that's not enough O.o what if someone wants more superclasses O.o
			//TODO ok, storno, u have to select the gens that must be broken :D
			
			//TODO get the class of the first gen
			//TODO get the first class' gens, what do we have in our selection
			//TODO with that we can get what superclasses are chosen
			//TODO for all gens we have to have one of the superclasses and one of the subclasses
			//TODO ummmm but all kind of gens must be defined once O.o
			
			//TODO select subclasses and we can choose the superclasses to generalize
			//TODO ooor select superclasses and generalize those subclasses that have them as super in common
			superClasses = selection.stream().filter(e -> e instanceof Class).map(e -> (Class) e).collect(Collectors.toList());
			
			subClasses = new ArrayList<>();
			List<Class> allClasses = UmlUtils.getAllClasses(superClasses.get(0).getModel());
			for (Class cl : allClasses) {
				if (cl.getSuperClasses().containsAll(superClasses)){
					subClasses.add(cl);
				}
			}
			
			
			
			
			
			//Association assoc = (Association) selection.get(0);
			//TODO the selected generalizable has at least one superclass and at least one subclass
			//generalizable = (Class) selection.get(0);
			
			
			
			/*if (generalizable.getSuperClasses().isEmpty()) {
				result.addFatalError("The selected class must have at least one superclass.");
				return result;
			}*/
			
			//TODO has that class any subclasses -> gather them
			
			
			if (subClasses.isEmpty()) {
				result.addFatalError("The selected classes must have at least one common subclass.");
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