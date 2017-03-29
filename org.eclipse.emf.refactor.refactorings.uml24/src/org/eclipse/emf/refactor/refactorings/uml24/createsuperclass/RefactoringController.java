/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.createsuperclass;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.extension.EmfNotifierController;
import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.interfaces.IDataManagement;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.LtkEmfRefactoringProcessorAdapter;
import org.eclipse.emf.refactor.refactorings.uml24.UmlUtils;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.VisibilityKind;


public final class RefactoringController extends EmfNotifierController implements IController{

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
	
	private List<IGraphicalEditPart> editParts;
	@Override
	public void setEditParts(List<IGraphicalEditPart> editParts) {
		this.editParts = editParts;
	}
	
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
	@Override
	protected void refactoringBody() {
				org.eclipse.uml2.uml.Class selectedEObject = 
					(org.eclipse.uml2.uml.Class) dataManagement.
							getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
				String className =
					(String) dataManagement.getInPortByName("className").getValue();
				// execute
				Package p = selectedEObject.getPackage();
				Generalization gen = null;
				if (p.getPackagedElement(className) == null) {
					// the owning package does not own a class with the inserted name
					// create new class named 'className'
					Class newSuperclass = UMLFactory.eINSTANCE.createClass();
					newSuperclass.setName(className);
					p.getPackagedElements().add(newSuperclass);
					// create generalization from context class to new class
					gen = selectedEObject.createGeneralization(newSuperclass);

				} else { // the owning package owns a class with the inserted name
					Class existingClass = (Class) p.getPackagedElement(className);
					// create generalization from context class to existing class
					gen = selectedEObject.createGeneralization(existingClass);
				}
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
				org.eclipse.uml2.uml.Class selectedEObject = 
					(org.eclipse.uml2.uml.Class) dataManagement.
							getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
				// test: the selected class must be owned by a package
				String msg = "This refactoring can only be applied" +
								" on classes which are owned by a package!";
				if (selectedEObject.getPackage() == null) result.addFatalError(msg);
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
				org.eclipse.uml2.uml.Class selectedEObject = 
					(org.eclipse.uml2.uml.Class) dataManagement.
							getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
				String className =
					(String) dataManagement.getInPortByName("className").getValue();
				Package p = selectedEObject.getPackage();
				if ((p.getPackagedElement(className) != null)) {
					// test: the owning package must not own an element with the 
					// inserted name that is not a class
					String msg = "The owning package already owns an (non-class)" +
													" element named '" + className + "'!";
					if (!(p.getPackagedElement(className) instanceof Class)) {
						result.addFatalError(msg);
					} else { // the owning package owns a class with the inserted name
						Class existingClass = (Class) p.getPackagedElement(className);
						// test: the class must not be abstract
						msg = "The class named '" + className + "' is abstract!";
						if (existingClass.isAbstract()) result.addFatalError(msg);
						// test: the class must be public
						msg = "The class named '" + className + "' is not public!";
						if (! existingClass.getVisibility().equals(VisibilityKind.PUBLIC_LITERAL)) 
													result.addFatalError(msg);
						// test: the class must not own any attributes
						msg = "The class named '" + className + "' owns at least one attribute!";
						if (UmlUtils.hasAttributes(existingClass)) result.addFatalError(msg);
						// test: the class must not own any operations
						msg = "The class named '" + className + "' owns at least one operation!";
						if (UmlUtils.hasOperations(existingClass)) result.addFatalError(msg);
						// test: the class must not have any superclasses
						msg = "The class named '" + className + "' has at least one superclass!";
						if (UmlUtils.hasSuperclasses(existingClass)) result.addFatalError(msg);
						// test: the class must not have any inner classes
						msg = "The class named '" + className + "' has at least one inner class!";
						if (UmlUtils.hasInnerClasses(existingClass)) result.addFatalError(msg);
						// test: the class must not have any outgoing associations
						msg = "The class named '" + className + "' has at least one outgoing association!";
						if (UmlUtils.hasOutgoingAssociations(existingClass)) result.addFatalError(msg);
						// test: the class must not have any incoming associations
						msg = "The class named '" + className + "' has at least one incoming association!";
						if (UmlUtils.hasIncomingAssociations(existingClass)) result.addFatalError(msg);
						// test: the class must not implement any interfaces
						msg = "The class named '" + className + "' implements at least one interface!";
						if (UmlUtils.implementsInterfaces(existingClass)) result.addFatalError(msg);
						// test: the class must not use any interfaces
						msg = "The class named '" + className + "' uses at least one interface!";
						if (UmlUtils.usesInterfaces(existingClass)) result.addFatalError(msg);
						// test: the class must not be used as attribute type
						msg = "The class named '" + className + "' is used as attribute type!";
						if (UmlUtils.isUsedAsAttributeType(existingClass)) result.addFatalError(msg);
						// test: the class must not be used as operation/parameter type
						msg = "The class named '" + className + "' is used as operation/parameter type!";
						if (UmlUtils.isUsedAsParameterType(existingClass)) result.addFatalError(msg);
					}						
				}
				return result;
		}
		
	}

}