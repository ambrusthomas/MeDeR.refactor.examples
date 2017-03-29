/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.splitassociations;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.interfaces.IDataManagement;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.LtkEmfRefactoringProcessorAdapter;
import org.eclipse.emf.refactor.refactorings.uml24.UmlUtils;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

import com.google.inject.Guice;

public final class RefactoringController implements IController {

	private Refactoring parent;
	private RefactoringDataManagement dataManagement = new RefactoringDataManagement();
	private List<EObject> selection = new ArrayList<>();
	private InternalRefactoringProcessor refactoringProcessor = null;
	
	private Class superClass = null;
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
		else return new ArrayList<>();
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
				//TODO create new associations for subclasses
				//TODO put them on diagram
				
				Property commonEnd = assoc.getMemberEnds().get(1-properEnd);
				Property superEnd = assoc.getMemberEnds().get(properEnd);
				
				for (Class sub : subClasses) {
					Association newassoc = sub.createAssociation(
							commonEnd.isNavigable(),
							commonEnd.getAggregation(),
							commonEnd.getName(),
							commonEnd.getLower(),
							commonEnd.getUpper(),
							commonEnd.getType(),
							superEnd.isNavigable(),
							superEnd.getAggregation(),
							superEnd.getName(),
							superEnd.getLower(),
							superEnd.getUpper());
					
				}
				
				
				
				//TODO remove the selected association from diagram
				for (int i=0;i<selection.size()-1;++i) {
					if (selection.get(i) instanceof Association && selection.get(i+1) instanceof Diagram) {
						//TODO ElementsController.removeConnector((Diagram) selection.get(i+1), selection.get(i));
						++i;
					}
				}
				//TODO remove the selected association
				assoc.getMemberEnds().forEach(prop -> ((Class)prop.getType()).getOwnedAttributes().remove(prop));
				assoc.getPackage().getPackagedElements().remove(assoc);
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
			//TODO if both ends connects to the same class, we have to choose
			//the proper one in a dialog (wizard)
			assoc = (Association) selection.stream().filter(RefactoringGuiHandler::isAssociation).findAny().get();
			superClass = (Class) selection.stream().filter(RefactoringGuiHandler::isClass).findAny().get();
			
			if (!assoc.getRelatedElements().contains(superClass)) {
				result.addFatalError("At least one end of the association must connect to the selected class.");
				return result;
			}
			
			if (assoc.getRelatedElements().size() == 1) {
				//should choose
				canChoose = true;
			}
			else {
				properEnd = assoc.getRelatedElements().lastIndexOf(superClass);
			}
			
			//TODO has that class any subclasses -> gather them
			subClasses = new ArrayList<>();
			List<Class> allClasses = UmlUtils.getAllClasses(superClass.getModel());
			for (Class cl : allClasses) {
				if (cl.getSuperClasses().contains(superClass)){
					subClasses.add(cl);
				}
			}
			
			if (subClasses.isEmpty()) {
				result.addFatalError("The selected class does not have any subclasses.");
				return result;
			}
			
			//TODO maybe attributes must be checked
			
			
			
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