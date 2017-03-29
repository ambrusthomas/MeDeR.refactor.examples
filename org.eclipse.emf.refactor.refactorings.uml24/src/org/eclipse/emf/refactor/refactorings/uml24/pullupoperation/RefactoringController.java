/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.pullupoperation;

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
	 * @param Refactoring Refactoring supported by the controller.
	 * @see org.eclipse.emf.refactor.common.core.IController#
	 * setParent(org.eclipse.emf.refactor.common.core.Refactoring)
	 * @generated
	 */
	@Override
	public void setParent(Refactoring Refactoring) {
		this.parent = Refactoring;
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
				// execute: move selected attribute to specified superclass
				Class oldClass = selectedEObject.getClass_();
				Class newClass = oldClass.getSuperClass(className);				
				oldClass.getOwnedOperations().remove(selectedEObject);
				newClass.getOwnedOperations().add(selectedEObject);
				// execute: remove equivalent operations from subclasses
				ArrayList<Class> classes = UmlUtils.getAllSubClasses(newClass);
				classes.remove(oldClass);
				for (Class cls : classes) {
					Operation operationToRemove = null;
					for (Operation op : cls.getOwnedOperations()) {
						if (UmlUtils.haveSameNames(op, selectedEObject)
								&& UmlUtils.haveSameType(op, selectedEObject)
								&& UmlUtils.haveSameProperties(op.getOwnedParameters(), selectedEObject.getOwnedParameters())) {
							operationToRemove = op;
							break;
						}
					}
					// remove equivalent operation from subclass
					Class owningSubClass = operationToRemove.getClass_();
					owningSubClass.getOwnedOperations().remove(operationToRemove);
				}
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
					// test: the owning class must have at least one superclass
					msg = "This refactoring can not be applied because the owning class " +
							"of the selected operation does not have any superclasses!";
					if (selectedEObject.getClass_().getSuperClasses().isEmpty()) 
															result.addFatalError(msg);
				}
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
				// test: the owning class must have a superclass with the specified name
				String msg = "The owning class does not have a superclass named '" +
																		className + "'!";
				Class superClass = selectedEObject.getClass_().getSuperClass(className);
				if (superClass == null) {
					result.addFatalError(msg);
				} else {
					// test: each subclass of the specified superclass must have an
					// operation equal to the selected operation
					if (! UmlUtils.subClassesHaveOperation(superClass, selectedEObject)) {
						ArrayList<String> msgs = 
								UmlUtils.getReasonsWhySubClassesDoNotHaveOperation(superClass, selectedEObject);
						for (String str : msgs) {
							result.addFatalError(str);
						}
					}
					// test: the superclass must not own an operation with the 
					// same name as the selected operation and a similar parameter list
					msg = "Class '" + className + "' already owns an operation named '" + 
							selectedEObject.getName() + "' having the same signature " +
							"(type and parameter list)!";
					if (classOwnsOperation(superClass, selectedEObject)) result.addFatalError(msg);
					// test: the superclass must not inherit an operation with the 
					// same name as the selected operation and a similar parameter list
					msg = "Class '" + className + "' already inherits an operation named '" + 
							selectedEObject.getName() + "' having the same signature " +
							"(type and parameter list)!";
					if (classInheritsOperation(superClass, selectedEObject)) result.addFatalError(msg);
				}
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