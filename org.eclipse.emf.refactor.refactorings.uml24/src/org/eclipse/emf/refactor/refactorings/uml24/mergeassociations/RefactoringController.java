/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.mergeassociations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.eclipse.uml2.uml.VisibilityKind;

public final class RefactoringController implements IController {

	private Refactoring parent;
	private RefactoringDataManagement dataManagement = new RefactoringDataManagement();
	private List<EObject> selection = new ArrayList<>();
	private InternalRefactoringProcessor refactoringProcessor = null;
	
	private Class superClass = null;
	private List<Association> assocs = null;
	private List<Boolean> sameOrder = null;
	private Set<Class> otherClasses = null;
	private int commonNumberInRelated;
	
	private boolean mustChooseAssociationEnd;
	
	public List<Property> getPossibleAssociationsEnds() {
		if (mustChooseAssociationEnd) {
			return assocs.get(0).getMemberEnds();
		}
		else return null;
	}
	
	public void setCommonNumberInRelated(int commonNumberInRelated) {
		this.commonNumberInRelated = commonNumberInRelated;
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
				//TODO create new association
				Property commonEnd = assocs.get(0).getMemberEnds().get(commonNumberInRelated);
				Property superEnd = assocs.get(0).getMemberEnds().get(1-commonNumberInRelated);
				Association newassoc = superClass.createAssociation(
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
				
//				DiagramRefreshService d = Guice.createInjector().getInstance(DiagramRefreshService.class);
//				d.putToDiagram(Arrays.asList(newassoc));
				
				//TODO remove associations from diagram
				for (int i=0;i<selection.size()-1;++i) {
					if (selection.get(i) instanceof Association && selection.get(i+1) instanceof Diagram) {
//						d.deleteOldElementsFromDiagram(Arrays.asList((Element) selection.get(i)));
						++i;
					}
				}
				
				//TODO delete old associations
				for (Association assoc : assocs) {
					assoc.getMemberEnds().forEach(prop -> ((Class)prop.getType()).getOwnedAttributes().remove(prop));
				}
				
				assocs.forEach(assoc -> {
					assoc.getPackage().getPackagedElements().remove(assoc);
				}) ;
			}
		};
	}

	public final class InternalRefactoringProcessor extends
			LtkEmfRefactoringProcessorAdapter {

		private InternalRefactoringProcessor(List<EObject> selection) {
			super(getParent(), selection, applyRefactoring());
		}
		
		private boolean isSameAssociation(Property a1, Property a2) {
			return a1.getName().equals(a2.getName()) 
					&& a1.isNavigable() == a2.isNavigable()
					&& a1.getAggregation().equals(a2.getAggregation())
					&& a1.getLower() == a2.getLower()
					&& a1.getUpper() == a2.getUpper();
		}
		
		private boolean hasTheTypeAsSuperClass(Class type) {
			return type.getSuperClasses().contains(superClass);
		}
		
		private Class getNthEndClass(Association assoc, int nth) {
			return (Class) assoc.getMemberEnds().get(nth).getType();
		}

		@Override
		public RefactoringStatus checkInitialConditions() {
			//TODO if navigable, check properties of class
			RefactoringStatus result = new RefactoringStatus();
			assocs = selection.stream().filter(RefactoringGuiHandler::isAssociation).map(e -> (Association) e).collect(Collectors.toList());
			superClass = (Class) selection.stream().filter(RefactoringGuiHandler::isClass).findAny().get();
			sameOrder = new ArrayList<>();
			otherClasses = new HashSet<>();
			commonNumberInRelated = -1;
			
			
			// If only one association is selected, the user may have to choose the association end.
			if (assocs.size() == 1) {
				if (checkOneAssociation(result)) return result;
			}
			
			// There must be a class that all associations are connected to.
			else if (assocs.size() > 1) {
				checkMoreAssociations(result);
			}
			
			// the common end must be public
			checkIfPublic(result);
			
			// have we used all subclasses of the superClass?
			checkIfAllSubClassSelected(result);
			
			
			return result;
		}

		private boolean checkOneAssociation(RefactoringStatus result) {
			Association assoc = assocs.get(0);
			//TODO 3. eset mindkét vége jó lenne O.o ki kell választani -> wizardpage
			boolean fstEndContains = hasTheTypeAsSuperClass(getNthEndClass(assoc, 0)),
					sndEndContains = hasTheTypeAsSuperClass(getNthEndClass(assoc, 1));
			
			// If both ends have the selected class as super class, the user has to choose,
			// otherwise it's obvious. In the first case we do other checks after selecting
			// the proper end. (in checkFinalConditions)
			if (fstEndContains && sndEndContains) {
				mustChooseAssociationEnd = true;
			}
			else if (fstEndContains) {
				commonNumberInRelated = 1;
				otherClasses.add(getNthEndClass(assoc,0));
			} else if (sndEndContains) {
				commonNumberInRelated = 0;
				otherClasses.add(getNthEndClass(assoc, 1));
				
			} else {
				result.addFatalError("One end of the association must have the selected class as its superclass");
			}
			return mustChooseAssociationEnd;
		}

		private void checkMoreAssociations(RefactoringStatus result) {
			// All associations must have the same end: with names, aggregations, navigation and multiplicity
			for (Association a : assocs) {
				if (isSameAssociation(a.getMemberEnds().get(0),assocs.get(0).getMemberEnds().get(0))
						&& isSameAssociation(a.getMemberEnds().get(1),assocs.get(0).getMemberEnds().get(1))) {
					sameOrder.add(true);
				} else if (isSameAssociation(a.getMemberEnds().get(0),assocs.get(0).getMemberEnds().get(1))
						&& isSameAssociation(a.getMemberEnds().get(1),assocs.get(0).getMemberEnds().get(0))) {
					sameOrder.add(false);
				} else {
					result.addFatalError("All associations must have the same ends.");
				}
			}
			
			// From the first two we can decide the common class
			int fstindex = 0, sndindex = 0;
			if (!sameOrder.get(1)) sndindex = 1;

			// common is first
			if (getNthEndClass(assocs.get(0),fstindex).equals(getNthEndClass(assocs.get(1),sndindex)))
				commonNumberInRelated = 0;
			// common is second
			else if (getNthEndClass(assocs.get(0),1-fstindex).equals(getNthEndClass(assocs.get(1),1-sndindex)))
				commonNumberInRelated = 1;
			else {
				result.addFatalError("The associations do not have a common end class.");
			} 
			
			Class commonClass = (Class) getNthEndClass(assocs.get(0),commonNumberInRelated);
			
			
				
			//the others also have to relate that class
			for (int i=2;i<assocs.size();++i) {
				
				fstindex = commonNumberInRelated;
				if (sameOrder.get(i)) sndindex = fstindex;
				else sndindex = 1-fstindex;
				
				if (!commonClass.equals(getNthEndClass(assocs.get(i),sndindex))) {
					result.addFatalError("The associations do not have a common endclass.");
				}
			}
			
			//the other end joins to different classes? every class is a subclass of the superClass?
			//gather the other ends, all must be different

			for (int i=0;i<assocs.size();++i) {
				Association assoc = assocs.get(i);
				int properEnd = (sameOrder.get(i) ? 1-commonNumberInRelated : commonNumberInRelated);
				Class otherEnd = getNthEndClass(assoc, properEnd);
				if (!hasTheTypeAsSuperClass(otherEnd)) {
					result.addFatalError("The associations' different endclasses must be the subclass of the selected class.");
				}
				if (!otherClasses.contains(otherEnd)) {
					otherClasses.add(otherEnd);
				} else {
					result.addFatalError("The associations must not have the same class on both ends.");
				}
			}
		}

		private void checkIfAllSubClassSelected(RefactoringStatus result) {
			List<Class> allClasses = UmlUtils.getAllClasses(superClass.getModel());
			for (Class cl : allClasses) {
				if (cl.getSuperClasses().contains(superClass)){
					if (!otherClasses.contains(cl)) {
						result.addFatalError("Not all subclasses have an association selected for merging.");
					}
				}
			}
		}

		private void checkIfPublic(RefactoringStatus result) {
			if (!assocs.get(0).getMemberEnds().get(commonNumberInRelated).getVisibility().equals(VisibilityKind.PUBLIC_LITERAL)) {
				result.addFatalError("The common association end must be public.");
			}
		}

		@Override
		public RefactoringStatus checkFinalConditions() {
			RefactoringStatus result = new RefactoringStatus();
			
			checkIfPublic(result);
			checkIfAllSubClassSelected(result);
			
			
			return result;
			
		}

	}

}