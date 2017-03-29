/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.createsubclass;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.extension.EmfNotifierController;
import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.interfaces.IDataManagement;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.LtkEmfRefactoringProcessorAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;

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
	private Generalization gen;
	
//	private Runnable applyRefactoring() {
//		return new Runnable() {				
//			/**
//			 * @see java.lang.Runnable#run()
//			 * @generated
//			 */
//			@Override
//			public void run() {
				
				
				
//				selectedEObject.eContainer().eAdapters().add(new AdapterImpl() {
//					private boolean react = false;
//					
//					public void notifyChanged(Notification notification) {
						
//						Display.getDefault().asyncExec(new Runnable() {
//							public void run() {
//								if (!react) {
//									if (notification.)
//								} else {
//									System.out.println(notification);
////									System.out.println(notification.getNewValue());
//									System.out.println(notification.getNewValue());
//									System.out.println(notification.getOldValue());
//									if (gen != null) {
//										DropObjectsRequest dor = new DropObjectsRequest();
//										dor.setObjects(Arrays.asList(gen));
//										dor.setLocation(new Point(0,0));
//										Command c = editParts.get(0).getParent().getCommand(dor);
//										if (c == null) {
//											System.out.println("Ajjaj");
//										} else {
//											c.execute();
//										}
//									}
//								}
								
//							}
//						});
//					}
//				});
//				Comment tmpComm = UMLFactory.eINSTANCE.createComment();
//				tmpComm.setBody("EMF_Refactor");;
//				selectedEObject.getPackage().eContents().add(tmpComm);
//				selectedEObject.getPackage().addKeyword("EMF_Refactor");
//				selectedEObject.getPackage().set
//				Comment tmpComm = UMLFactory.eINSTANCE.createComment();
//				tmpComm.setBody("EMF Refactor");
//				Package pekidzs = selectedEObject.getPackage();
//				pekidzs.getOwnedComments().add(tmpComm);
//								pekidzs.getOwnedComments().remove(tmpComm);
//			}
//		};
//	}
	
	
	private void refreshPapyrusElements(Generalization gen) {
		
	
	}
//	private void refreshPapyrusElements(Generalization gen) {
//		if (editParts != null) {
////			Runnable asyncExec = new Runnable() {
////				public void run() {
//					RefactorCommandContainer container = new RefactorCommandContainer("Create subclass", editParts);
//					DiagramRefreshService d = Guice.createInjector().getInstance(DiagramRefreshService.class);
//					
//					container.addElementStable(Arrays.asList(gen));
//					d.executeCommandContainer(container);
////				}
////			};
////			Display.getDefault().asyncExec(asyncExec);
//		}
//	}

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
				//startTimeRecording();	
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
				// test: the owning package must not own an element with the inserted name
				String msg = "The owning package already owns an element named '" + className + "'!";
				Package p = selectedEObject.getPackage();
				if (p.getPackagedElement(className) != null) result.addFatalError(msg);
				return result;
		}
		
	}

	@Override
	protected void refactoringBody() {
		org.eclipse.uml2.uml.Class selectedEObject = 
				(org.eclipse.uml2.uml.Class) dataManagement.
						getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
		String className =
				(String) dataManagement.getInPortByName("className").getValue();
			// execute: create new class named 'className'
		
		
		Class subclass = UMLFactory.eINSTANCE.createClass();
		subclass.setName(className);
		
		selectedEObject.getPackage().getPackagedElements().add(subclass);
//		subclass.setPackage(selectedEObject.getPackage());
		
		gen = subclass.createGeneralization(selectedEObject);

		
			// execute: create generalization from new class to context class
		
	}


}