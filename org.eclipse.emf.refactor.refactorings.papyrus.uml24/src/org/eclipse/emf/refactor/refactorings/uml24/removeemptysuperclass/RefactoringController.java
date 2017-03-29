/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.removeemptysuperclass;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.interfaces.IDataManagement;
import org.eclipse.emf.refactor.refactoring.papyrus.managers.PapyrusManager;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.LtkEmfRefactoringProcessorAdapter;
import org.eclipse.emf.refactor.refactorings.uml24.UmlUtils;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Package;

import com.google.inject.Guice;


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
	private List<IGraphicalEditPart> editParts;
	@Override
	public void setEditParts(List<IGraphicalEditPart> editParts) {
		this.editParts = editParts;
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
				org.eclipse.uml2.uml.Class selectedEObject = 
					(org.eclipse.uml2.uml.Class) dataManagement.
							getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
				View view = getView(selectedEObject);
				// execute: delete generalizations to selected class
				List<Class> allClasses = UmlUtils.getAllClasses(selectedEObject.getModel());
				for (Class cl : allClasses) {
					if (cl.getSuperClasses().contains(selectedEObject)){
						Generalization gen = cl.getGeneralization(selectedEObject);
						// remove edge from diagram
//						if (view != null) {
//							removeConnector(gen);
//						}
						// remove generalization
//						cl.getGeneralizations().remove(gen);
					}
				}
				// execute: delete selected class from owning package
				Package p = selectedEObject.getPackage();
				// remove shape
				if (view != null) {
					System.out.println("--> View: " + view);
					View owningView = (View) view.eContainer();
//					owningView.removeChild(view);
				}
				// remove class
//				p.getPackagedElements().remove(selectedEObject);
				
				
//				Runnable asyncExec = new Runnable() {
//					public void run() {
//						RefactorCommandContainer container = new RefactorCommandContainer("Fold outgoing transitions", editParts);
//						DiagramRefreshService d = Guice.createInjector().getInstance(DiagramRefreshService.class);
//						org.eclipse.uml2.uml.Class selectedEObject = 
//								(org.eclipse.uml2.uml.Class) dataManagement.
//										getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
//						List<Class> allClasses = UmlUtils.getAllClasses(selectedEObject.getModel());
//						for (Class cl : allClasses) {
//							if (cl.getSuperClasses().contains(selectedEObject)){
//								Generalization gen = cl.getGeneralization(selectedEObject);
//								container.removeElement(gen);
//							}
//						}
//						container.removeElement(selectedEObject);
//						d.executeCommandContainer(container);
//					}
//				};
//				Display.getDefault().asyncExec(asyncExec);
			}
			
			private void removeConnector(Generalization gen) {
				Diagram diagram = null;
				if (selection.size() > 1) {
					diagram = (Diagram) selection.get(1);
				} else {
					diagram = PapyrusManager.getInstance().getDiagram();
				}
				Connector connector = null;
				if (diagram != null) {
					TreeIterator<EObject> iter = diagram.eAllContents();
					while (iter.hasNext()) {
						EObject eo = iter.next();
						if (eo instanceof Connector) {
							Connector con = (Connector) eo;
							if (con.getElement() == gen) {
								connector = con;
								break;
							}
						}
					}
					System.out.println(">> Connector: " + connector);
					if (connector != null) {
						connector.unsetElement();
						connector.setSource(null);
						connector.setTarget(null);
						diagram.removeEdge(connector);
					}
				}
			}

			private View getView(org.eclipse.uml2.uml.Class clazz) {
				Diagram diagram = null;
				if (selection.size() > 1) {
					diagram = (Diagram) selection.get(1);
				} else {
					diagram = PapyrusManager.getInstance().getDiagram();
				}
				if (diagram != null) {
					TreeIterator<EObject> iter = diagram.eAllContents();
					while (iter.hasNext()) {
						EObject eo = iter.next();
						if (eo instanceof View) {
							View view = (View) eo;
							if (view.getElement() == clazz) {
								return view;
							}
						}
					}
				}
				return null;
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
			Class selectedEObject = 
				(Class) dataManagement.getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
			// test: the selected class must be owned by a package
			String msg = "This refactoring can only be applied" +
							" on classes which are owned by a package!";
			if (selectedEObject.getPackage() == null) result.addFatalError(msg);
			// test: the selected class must have at least one subclass
			msg = "Class '" + selectedEObject.getName() + "' does not have any subclasses!";
			if (! UmlUtils.hasSubclasses(selectedEObject)) result.addFatalError(msg);
			// test: the selected class  must not own any attributes
			msg = "Class '" + selectedEObject.getName() + "' owns at least one attribute!";
			if (UmlUtils.hasAttributes(selectedEObject)) result.addFatalError(msg);
			// test: the selected class  must not own any operations
			msg = "Class '" + selectedEObject.getName() + "' owns at least one operation!";
			if (UmlUtils.hasOperations(selectedEObject)) result.addFatalError(msg);
			// test: the selected class must not have any superclasses
			msg = "Class '" + selectedEObject.getName() + "' has at least one superclass!";
			if (UmlUtils.hasSuperclasses(selectedEObject)) result.addFatalError(msg);
			// test: the class must not have any inner classes
			msg = "Class '" + selectedEObject.getName() + "' has at least one inner class!";
			if (UmlUtils.hasInnerClasses(selectedEObject)) result.addFatalError(msg);
			// test: the class must not have any outgoing associations
			msg = "Class '" + selectedEObject.getName() + "' has at least one outgoing association!";
			if (UmlUtils.hasOutgoingAssociations(selectedEObject)) result.addFatalError(msg);
			// test: the class must not have any incoming associations
			msg = "Class '" + selectedEObject.getName() + "' has at least one incoming association!";
			if (UmlUtils.hasIncomingAssociations(selectedEObject)) result.addFatalError(msg);
			// test: the class must not implement any interfaces
			msg = "Class '" + selectedEObject.getName() + "' implements at least one interface!";
			if (UmlUtils.implementsInterfaces(selectedEObject)) result.addFatalError(msg);
			// test: the class must not use any interfaces
			msg = "Class '" + selectedEObject.getName() + "' uses at least one interface!";
			if (UmlUtils.usesInterfaces(selectedEObject)) result.addFatalError(msg);
			// test: the class must not be used as attribute type
			msg = "Class '" + selectedEObject.getName() +  "' is used as attribute type!";
			if (UmlUtils.isUsedAsAttributeType(selectedEObject)) result.addFatalError(msg);
			// test: the class must not be used as operation/parameter type
			msg = "Class '" + selectedEObject.getName() + "' is used as operation/parameter type!";
			if (UmlUtils.isUsedAsParameterType(selectedEObject)) result.addFatalError(msg);
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
				// no final checks
				return result;
		}
		
	}

}