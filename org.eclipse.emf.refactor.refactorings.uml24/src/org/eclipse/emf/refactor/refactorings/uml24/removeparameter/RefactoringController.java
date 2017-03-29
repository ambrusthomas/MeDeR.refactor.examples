/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.removeparameter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.interfaces.IDataManagement;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.LtkEmfRefactoringProcessorAdapter;
import org.eclipse.emf.refactor.refactorings.uml24.UmlUtils;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;


public final class RefactoringController implements IController{

	/**
	 * Refactoring supported by the controller.
	 * @generated
	 */
	private Refactoring parent;
	
	/**
	 * DataManagement object of the model refactoring.
	 * @generated
	 */
	private RefactoringDataManagement dataManagement = 
									new RefactoringDataManagement();
	
	/**
	 * Invocation context of the model refactoring.
	 * @generated
	 */	
	private List<EObject> selection = new ArrayList<EObject>();
	
	/**
	 * Ltk RefactoringProcessor of the model refactoring.
	 * @generated
	 */
	private InternalRefactoringProcessor refactoringProcessor = null;
	
	/**
	 * Gets the Refactoring supported by the controller.
	 * @return Refactoring supported by the controller.
	 * @see org.eclipse.emf.refactor.common.core.IController#getParent()
	 * @generated
	 */
	@Override
	public Refactoring getParent() {
		return this.parent;
	}
	
	/**
	 * Sets the Refactoring supported by the controller.
	 * @param emfRefactoring Refactoring supported by the controller.
	 * @see org.eclipse.emf.refactor.common.core.IController#
	 * setParent(org.eclipse.emf.refactor.common.core.Refactoring)
	 * @generated
	 */
	@Override
	public void setParent(Refactoring emfRefactoring) {
		this.parent = emfRefactoring;
	}
	
	/**
	 * Returns the DataManagement object of the model refactoring.
	 * @return DataManagement object of the model refactoring.
	 * @see org.eclipse.emf.refactor.common.core.IController#
	 * getDataManagementObject()
	 * @generated
	 */
	@Override
	public IDataManagement getDataManagementObject() {
		return this.dataManagement;
	}

	/**
	 * Returns the ltk RefactoringProcessor of the model refactoring.
	 * @return Ltk RefactoringProcessor of the model refactoring.
	 * @see org.eclipse.emf.refactor.common.core.IController#
	 * getLtkRefactoringProcessor()
	 * @generated
	 */
	@Override
	public RefactoringProcessor getLtkRefactoringProcessor() {
		return this.refactoringProcessor;
	}
	
	/**
	 * Sets the selected EObject (invocation context of the model refactoring).
	 * @param selection Invocation context of the model refactoring.
	 * @see org.eclipse.emf.refactor.common.core.IController#
	 * setSelection(java.util.List)
	 * @generated
	 */
	@Override
	public void setSelection(List<EObject> selection) {
		this.selection = selection;
		this.refactoringProcessor = 
				new InternalRefactoringProcessor(this.selection);
	}	
	
	/**
	 * Returns a Runnable object that executes the model refactoring.
	 * @return Runnable object that executes the model refactoring.
	 * @generated
	 */
	private Runnable applyRefactoring() {
		return new Runnable() {				
			/**
			 * @see java.lang.Runnable#run()
			 * @generated
			 */
			@Override
			public void run() {
				org.eclipse.uml2.uml.Parameter selectedEObject = 
					(org.eclipse.uml2.uml.Parameter) dataManagement.
							getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
				// execute: remove selected parameter from operation
				Operation owningOperation = selectedEObject.getOperation();
				owningOperation.getOwnedParameters().remove(selectedEObject);
			}
		};
	}

	/**
	 * Internal class for providing an instance of a LTK RefactoringProcessor 
	 * used for EMF model refactorings.	 
	 * @generated
	 */
	public final class InternalRefactoringProcessor extends 
									LtkEmfRefactoringProcessorAdapter {

		/**
		 * Constructor using the invocation context of the model refactoring.
		 * @param selection Invocation context of the model refactoring.
		 * @generated
		 */
		private InternalRefactoringProcessor(List<EObject> selection){
				super(getParent(), selection, applyRefactoring());				
		}
			
		/**
		 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor#
	 	 * checkInitialConditions(org.eclipse.core.runtime.IProgressMonitor)
		 * @generated
		 */	
		@Override
		public RefactoringStatus checkInitialConditions(){
				RefactoringStatus result = new RefactoringStatus();
				org.eclipse.uml2.uml.Parameter selectedEObject = 
					(org.eclipse.uml2.uml.Parameter) dataManagement.
							getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
				// test: the selected parameter must be an input parameter
				String msg = "This refactoring can only be applied on input parameters!";
				if ((selectedEObject.getDirection().getValue() == ParameterDirectionKind.RETURN)
						|| (selectedEObject.getDirection().getValue() == ParameterDirectionKind.OUT)) {
					result.addFatalError(msg);		
					return result;
				}										
				// test: the selected operation must be owned by a class
				msg = "This refactoring can only be applied on parameters" +
								" whose operation is owned by a class!";
				if (selectedEObject.getOperation().getClass_() == null) {
					result.addFatalError(msg);
					return result;
				}
				// test: the owning class must not own an operation with the name
				// of the contextual operation and a similar parameter list after
				// removing the selected parameter
				msg = "The owning class already owns an operation named '" +
						selectedEObject.getOperation().getName() + "' having the same signature " +
						"(type and parameter list) after removing the selected parameter!";
				Class cl = selectedEObject.getOperation().getClass_();
				if (classOwnsOperation(cl, selectedEObject)) result.addFatalError(msg);
				// test: the owning class must not own an operation with the name
				// of the contextual operation and a similar parameter list after
				// removing the selected parameter
				msg = "The owning class already inherits an operation named '" +
						selectedEObject.getOperation().getName() + "' having the same signature " +
						"(type and parameter list) after removing the selected parameter!";
				if (classInheritsOperation(cl, selectedEObject)) result.addFatalError(msg);
				return result;
		}
		
		private boolean classInheritsOperation(Class cl, Parameter param) {
			Operation owningOperation = param.getOperation();
			// remove new parameter and mark position
			int pos = owningOperation.getOwnedParameters().indexOf(param);
			owningOperation.getOwnedParameters().remove(param);
			// test
			for (NamedElement namedElement : cl.getInheritedMembers()) {
				if (namedElement instanceof Operation) {
					Operation inheritedOperation = (Operation) namedElement;
					if (inheritedOperation.getName().equals(owningOperation.getName()) 
							&& UmlUtils.haveSameTypes(inheritedOperation, owningOperation)
							 && UmlUtils.haveSameProperties(inheritedOperation.getOwnedParameters(), owningOperation.getOwnedParameters())) {
						// re-add parameter and return
						owningOperation.getOwnedParameters().add(pos, param);
						return true;
					}
				}
			}
			// re-add parameter and return
			owningOperation.getOwnedParameters().add(pos, param);
			return false;
		}

		private boolean classOwnsOperation(Class cl, Parameter param) {
			Operation owningOperation = param.getOperation();
			// remove new parameter and mark position
			int pos = owningOperation.getOwnedParameters().indexOf(param);
			owningOperation.getOwnedParameters().remove(param);
			// test
			for (Operation op : cl.getOwnedOperations()) {			 
				 if (op != owningOperation) {
					 if (op.getName().equals(owningOperation.getName()) 
							 && UmlUtils.haveSameTypes(op, owningOperation)
							 && UmlUtils.haveSameProperties(op.getOwnedParameters(), owningOperation.getOwnedParameters())) {
						// re-add parameter and return
						owningOperation.getOwnedParameters().add(pos, param);
						return true;
					 }
				 }
			}
			// re-add parameter and return
			owningOperation.getOwnedParameters().add(pos, param);
			return false;
		}

		/**
		 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor#
	     * checkFinalConditions(org.eclipse.core.runtime.IProgressMonitor, 
	     * org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext)
		 * @generated
		 */	
		@Override
		public RefactoringStatus checkFinalConditions(){
				RefactoringStatus result = new RefactoringStatus();
				// no final checks
				return result;
		}
		
	}

}