/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.moveoperation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.interfaces.IDataManagement;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.LtkEmfRefactoringProcessorAdapter;
import org.eclipse.emf.refactor.refactorings.uml24.UmlUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.VisibilityKind;


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
				org.eclipse.uml2.uml.Operation selectedEObject = 
					(org.eclipse.uml2.uml.Operation) dataManagement.
							getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
				String className =
					(String) dataManagement.getInPortByName("className").getValue();
				// execute: move selected operation to specified class
				List<Class> associatedClasses = 
						UmlUtils.getOne2OneAssociatedClasses(selectedEObject.getClass_());
				Class newClass = UmlUtils.getAssociatedClass(associatedClasses, className);
				Class oldClass = selectedEObject.getClass_();
				oldClass.getOwnedOperations().remove(selectedEObject);
				newClass.getOwnedOperations().add(selectedEObject);
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
				org.eclipse.uml2.uml.Operation selectedEObject = 
					(org.eclipse.uml2.uml.Operation) dataManagement.
							getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
				// test: the selected operation must be owned by a class
				String msg = "This refactoring can only be applied" +
								" on operations which are owned by a class!";
				if (selectedEObject.getClass_() == null) {
					result.addFatalError(msg);
				} else {
					// test: the selected operation must be public
					msg = "This refactoring can only be applied on public operations!";
					if (! selectedEObject.getVisibility().equals(VisibilityKind.PUBLIC_LITERAL)) 
															result.addFatalError(msg);
					// test: the owning class must be related to at least one other class
					// using a bidirectional association with multiplicity 1:1
					msg = "The owning class is not related to at least one other class " +
							"using a bidirectional association with multiplicity 1:1 " +
							"and public association ends!";
					List<Class> associatedClasses = 
							UmlUtils.getOne2OneAssociatedClasses(selectedEObject.getClass_());
					if (associatedClasses.isEmpty()) result.addFatalError(msg);
				}
				//if (result.hasEntries()) MessageDialog.openInformation(null, "", result.getEntries().toString());
				return result;
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
				org.eclipse.uml2.uml.Operation selectedEObject = 
					(org.eclipse.uml2.uml.Operation) dataManagement.
							getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
				String className =
					(String) dataManagement.getInPortByName("className").getValue();
				// test: the owning class must be related to a class with the specified 
				// name using a bidirectional association with multiplicity 1:1
				String msg = "The owning class is not related to a class named '" +
								className + "' using a bidirectional association with " +
								"multiplicity 1:1 and public association ends!";
				List<Class> associatedClasses = 
						UmlUtils.getOne2OneAssociatedClasses(selectedEObject.getClass_());
				if (! UmlUtils.isAssociatedClass(associatedClasses, className)) {
					result.addFatalError(msg);
				} else {
					// test: the associated class must not own an operation with the 
					// same name as the selected operation and a similar parameter list
					msg = "The associated class already owns an operation named '" + 
							selectedEObject.getName() + "' having the same signature " +
							"(type and parameter list)!";
					Class cl = UmlUtils.getAssociatedClass(associatedClasses, className);
					if (classOwnsOperation(cl, selectedEObject)) result.addFatalError(msg);
					// test: the associated class must already inherit an operation with the 
					// same name as the selected operation and a similar parameter list
					msg = "The associated class already inherits an an operation named '" + 
							selectedEObject.getName() + "' having the same signature " +
							"(type and parameter list)!";
					if (classInheritsOperation(cl, selectedEObject)) result.addFatalError(msg);
				}
				if (result.hasEntries()) MessageDialog.openInformation(null, "", result.getEntries().toString());
				return result;
		}

		private boolean classInheritsOperation(Class cl, Operation operation) {
			for (NamedElement namedElement : cl.getInheritedMembers()) {
				if (namedElement instanceof Operation) {
					Operation inheritedOperation = (Operation) namedElement;
					if (inheritedOperation.getName().equals(operation.getName())
							&& UmlUtils.haveSameTypes(inheritedOperation, operation)
							 && UmlUtils.haveSameProperties(inheritedOperation.getOwnedParameters(), operation.getOwnedParameters())) {
						 return true;
					}
				}
			}
			return false;
		}

		private boolean classOwnsOperation(Class cl, Operation operation) {
			for (Operation op : cl.getOwnedOperations()) {	
				if (op.getName().equals(operation.getName()) 
						&& UmlUtils.haveSameTypes(op, operation)
						&& UmlUtils.haveSameProperties(op.getOwnedParameters(), operation.getOwnedParameters())) {
					return true;
				}
			}
			return false;
		}
		
	}

}