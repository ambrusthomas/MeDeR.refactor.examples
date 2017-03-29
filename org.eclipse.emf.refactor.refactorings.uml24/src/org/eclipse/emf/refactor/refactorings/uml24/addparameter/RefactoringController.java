/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.addparameter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
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
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;

public final class RefactoringController implements IController {

	/**
	 * Refactoring supported by the controller.
	 * 
	 * @generated
	 */
	private Refactoring parent;

	/**
	 * DataManagement object of the model refactoring.
	 * 
	 * @generated
	 */
	private RefactoringDataManagement dataManagement = new RefactoringDataManagement();

	/**
	 * Invocation context of the model refactoring.
	 * 
	 * @generated
	 */
	private List<EObject> selection = new ArrayList<EObject>();

	/**
	 * Ltk RefactoringProcessor of the model refactoring.
	 * 
	 * @generated
	 */
	private InternalRefactoringProcessor refactoringProcessor = null;

	/**
	 * Gets the Refactoring supported by the controller.
	 * 
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
	 * 
	 * @param Refactoring
	 *            Refactoring supported by the controller.
	 * @see org.eclipse.emf.refactor.common.core.IController#
	 *      setParent(org.eclipse.emf.refactor.common.core.Refactoring)
	 * @generated
	 */
	@Override
	public void setParent(Refactoring Refactoring) {
		this.parent = Refactoring;
	}

	/**
	 * Returns the DataManagement object of the model refactoring.
	 * 
	 * @return DataManagement object of the model refactoring.
	 * @see org.eclipse.emf.refactor.common.core.IController#
	 *      getDataManagementObject()
	 * @generated
	 */
	@Override
	public IDataManagement getDataManagementObject() {
		return this.dataManagement;
	}

	/**
	 * Returns the ltk RefactoringProcessor of the model refactoring.
	 * 
	 * @return Ltk RefactoringProcessor of the model refactoring.
	 * @see org.eclipse.emf.refactor.common.core.IController#
	 *      getLtkRefactoringProcessor()
	 * @generated
	 */
	@Override
	public RefactoringProcessor getLtkRefactoringProcessor() {
		return this.refactoringProcessor;
	}

	/**
	 * Sets the selected EObject (invocation context of the model refactoring).
	 * 
	 * @param selection
	 *            Invocation context of the model refactoring.
	 * @see org.eclipse.emf.refactor.common.core.IController#
	 *      setSelection(java.util.List)
	 * @generated
	 */
	@Override
	public void setSelection(List<EObject> selection) {
		this.selection = selection;
		this.refactoringProcessor = new InternalRefactoringProcessor(this.selection);
	}

	/**
	 * Returns a Runnable object that executes the model refactoring.
	 * 
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
				org.eclipse.uml2.uml.Operation selectedEObject = (org.eclipse.uml2.uml.Operation) dataManagement
						.getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
				String paramName = (String) dataManagement.getInPortByName("paramName").getValue();
				String paramType = (String) dataManagement.getInPortByName("paramType").getValue();
				// execute: insert a new parameter with the specified name and
				// type
				// to the selected operation
				Parameter param = UMLFactory.eINSTANCE.createParameter();
				param.setName(paramName);
				EList<Type> types = UmlUtils.getTypes(selectedEObject.getModel());
				EList<NamedElement> typesWithName = UmlUtils.getNamedElementsFromList(types, paramType);
				param.setType((Type) typesWithName.get(0));
				selectedEObject.getOwnedParameters().add(param);

				// Runnable asyncExec = new Runnable() {
				// public void run() {
				// RefactorCommandContainer container = new
				// RefactorCommandContainer("Fold outgoing transitions",
				// editParts);
				// DiagramRefreshService d =
				// Guice.createInjector().getInstance(DiagramRefreshService.class);
				//
				// container.addElement(Arrays.asList(gen));
				// d.executeCommandContainer(container);
				// }
				// };
				// Display.getDefault().asyncExec(asyncExec);
			}
		};
	}

	/**
	 * Internal class for providing an instance of a LTK RefactoringProcessor
	 * used for EMF model refactorings.
	 * 
	 * @generated
	 */
	public final class InternalRefactoringProcessor extends LtkEmfRefactoringProcessorAdapter {

		/**
		 * Constructor using the invocation context of the model refactoring.
		 * 
		 * @param selection
		 *            Invocation context of the model refactoring.
		 * @generated
		 */
		private InternalRefactoringProcessor(List<EObject> selection) {
			super(getParent(), selection, applyRefactoring());
		}

		/**
		 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor#
		 *      checkInitialConditions(org.eclipse.core.runtime.IProgressMonitor)
		 * @generated
		 */
		@Override
		public RefactoringStatus checkInitialConditions() {
			RefactoringStatus result = new RefactoringStatus();
			org.eclipse.uml2.uml.Operation selectedEObject = (org.eclipse.uml2.uml.Operation) dataManagement
					.getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
			// test: the selected operation must be owned by a class
			String msg = "This refactoring can only be applied" + " on operations which are owned by a class!";
			if (selectedEObject.getClass_() == null)
				result.addFatalError(msg);
			return result;
		}

		/**
		 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor#
		 *      checkFinalConditions(org.eclipse.core.runtime.IProgressMonitor,
		 *      org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext)
		 * @generated
		 */
		@Override
		public RefactoringStatus checkFinalConditions() {
			RefactoringStatus result = new RefactoringStatus();
			org.eclipse.uml2.uml.Operation selectedEObject = (org.eclipse.uml2.uml.Operation) dataManagement
					.getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
			String paramName = (String) dataManagement.getInPortByName("paramName").getValue();
			String paramType = (String) dataManagement.getInPortByName("paramType").getValue();
			// test: the selected operation must not own a parameter with the
			// specified name
			String msg = "The selected operation already owns a parameter " + "named '" + paramName + "'!";
			EList<Parameter> inputParameters = UmlUtils.getInputParameterList(selectedEObject.getOwnedParameters());
			for (Parameter p : inputParameters) {
				if (p.getName().equals(paramName))
					result.addFatalError(msg);
			}
			// test: the specified type must exist
			EList<Type> types = UmlUtils.getTypes(selectedEObject.getModel());
			EList<NamedElement> typesWithName = UmlUtils.getNamedElementsFromList(types, paramType);
			msg = "The specified type does not exist in the model!";
			if (typesWithName.isEmpty()) {
				result.addFatalError(msg);
			} else {
				// test: the specified type exists multiply
				msg = "The specified type exists multiply in the model!";
				if (typesWithName.size() > 1) {
					result.addFatalError(msg);
				} else {
					// test: the owning class must not own an operation with the
					// name
					// of the contextual operation and a similar parameter list
					// after
					// inserting a parameter with the given name and type
					msg = "The owning class already owns an operation named '" + selectedEObject.getName()
							+ "' having the same signature "
							+ "(type and parameter list) after inserting a parameter named '" + paramName
							+ "' with type '" + paramType + "'!";
					Class cl = selectedEObject.getClass_();
					if (classOwnsOperation(cl, selectedEObject, paramName, paramType))
						result.addFatalError(msg);
					// test: the owning class must not own an operation with the
					// name
					// of the contextual operation and a similar parameter list
					// after
					// inserting a parameter with the given name and type
					msg = "The owning class already inherits an operation named '" + selectedEObject.getName()
							+ "' having the same signature "
							+ "(type and parameter list) after inserting a parameter named '" + paramName
							+ "' with type '" + paramType + "'!";
					if (classInheritsOperation(cl, selectedEObject, paramName, paramType))
						result.addFatalError(msg);
				}
			}
			return result;
		}

		private boolean classInheritsOperation(Class cl, Operation operation, String paramName, String paramType) {
			// insert new parameter
			Parameter param = UMLFactory.eINSTANCE.createParameter();
			param.setName(paramName);
			EList<Type> types = UmlUtils.getTypes(operation.getModel());
			EList<NamedElement> typesWithName = UmlUtils.getNamedElementsFromList(types, paramType);
			param.setType((Type) typesWithName.get(0));
			// test
			for (NamedElement namedElement : cl.getInheritedMembers()) {
				if (namedElement instanceof Operation) {
					Operation inheritedOperation = (Operation) namedElement;
					EList<Parameter> ownedParameters = operation.getOwnedParameters();
					ownedParameters.add(param);
					if (inheritedOperation.getName().equals(operation.getName())
							&& UmlUtils.haveSameTypes(inheritedOperation, operation)
							&& UmlUtils.haveSameProperties(inheritedOperation.getOwnedParameters(), ownedParameters)) {
						// remove temporary parameter and return
						return true;
					}
				}
			}
			// remove temporary parameter
			return false;
		}

		private boolean classOwnsOperation(Class cl, Operation operation, String paramName, String paramType) {
			// insert new parameter
			Parameter param = UMLFactory.eINSTANCE.createParameter();
			param.setName(paramName);
			EList<Type> types = UmlUtils.getTypes(operation.getModel());
			EList<NamedElement> typesWithName = UmlUtils.getNamedElementsFromList(types, paramType);
			param.setType((Type) typesWithName.get(0));

			for (Operation op : cl.getOwnedOperations()) {
				if (op != operation) {
					List<Parameter> ownedParameters = new ArrayList<>();
					ownedParameters.addAll(operation.getOwnedParameters());
					ownedParameters.add(param);
					if (op.getName().equals(operation.getName()) && UmlUtils.haveSameTypes(op, operation)
							&& UmlUtils.haveSameProperties(op.getOwnedParameters(), ownedParameters)) {
						return true;
					}
				}
			}
			return false;
		}

	}

}