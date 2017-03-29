/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.groupstates;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Refactoring;
import org.eclipse.emf.refactor.refactoring.extension.EmfNotifierController;
import org.eclipse.emf.refactor.refactoring.interfaces.IController;
import org.eclipse.emf.refactor.refactoring.interfaces.IDataManagement;
import org.eclipse.emf.refactor.refactoring.runtime.ltk.LtkEmfRefactoringProcessorAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.UMLFactory;

public final class RefactoringController extends EmfNotifierController implements IController {

	private Refactoring parent;
	private RefactoringDataManagement dataManagement = new RefactoringDataManagement();
	private List<EObject> selection = new ArrayList<>();
	private InternalRefactoringProcessor refactoringProcessor = null;
	private Region container;
	private String newStateName;

	List<IGraphicalEditPart> editParts;

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

//	private Runnable applyRefactoring() {
//		return new Runnable() {
//			@Override
//			public void run() {
//				
//			}
//
////			private void putToDiagram(State newState, Region newRegion) {
////				DiagramRefreshService d = Guice.createInjector().getInstance(DiagramRefreshService.class);
////
////				d.addElements(Arrays.asList(newState));
////				d.addElements(Arrays.asList(newRegion));
////
////				d.addElements(selection, editParts.stream().map(e -> e.getFigure().getBounds().getLocation()).collect(Collectors.toList()));
////				List<EObject> elements = newRegion.allOwnedElements().stream().map(e -> (EObject) e).collect(Collectors.toList());
////				elements.removeAll(selection);
////				d.addElements(elements, null);
////
////				d.deleteElements(selection);
////
////				d.addElements(selection.stream().flatMap(
////					s -> {
////						List<Transition> transList = new ArrayList<>();
////						transList.addAll(((State) s).getIncomings());
////						transList.addAll(((State) s).getOutgoings());
////						return transList.stream();
////					}).distinct().collect(Collectors.toList()));
////			}
//		};
//	}

	public final class InternalRefactoringProcessor extends LtkEmfRefactoringProcessorAdapter {

		private InternalRefactoringProcessor(List<EObject> selection) {
			super(getParent(), selection, applyRefactoring());
		}

		@Override
		public RefactoringStatus checkInitialConditions() {
			RefactoringStatus result = new RefactoringStatus();

			if (selection.isEmpty()) {
				result.addFatalError("There is no selection.");
			}

			selection.stream().filter(s -> !(s instanceof State)).findAny().ifPresent(
				s -> {
					result.addFatalError("There is an element which is not state.");
				});

			State anyState = (State) dataManagement.getInPortByName(dataManagement.SELECTEDEOBJECT).getValue();
			container = anyState.getContainer();

			if (!selection.stream().allMatch(s -> ((State) s).getContainer().equals(container))) {
				result.addFatalError("Selected states doesn't have the same container.");
			}

			return result;
		}

		@Override
		public RefactoringStatus checkFinalConditions() {
			RefactoringStatus result = new RefactoringStatus();

			newStateName = (String) dataManagement.getInPortByName("newState").getValue();
			container.getOwnedElements().stream().filter(e -> (e instanceof State && ((State) e).getName().equals(newStateName))).findAny().ifPresent(
				e -> {
					result.addFatalError("State named \""
							+ newStateName
							+ "\" already exists in container.");
				});

			if (selection.stream().anyMatch(s -> !((State) s).getContainer().equals(container))) {
				result.addFatalError("Refactoring failed.");
			}

			return result;
		}

	}

	@Override
	public void setEditParts(List<IGraphicalEditPart> editParts) {
		this.editParts = editParts;
	}

	@Override
	protected void refactoringBody() {
		State newState = UMLFactory.eINSTANCE.createState();

		newState.setName(newStateName);
		newState.setContainer(container);

		Region newRegion = newState.createRegion(newStateName + "Region");
		selection.stream().forEach(s ->
				{
					((State) s).setContainer(newRegion);
					((State) s).getOutgoings().forEach(tr -> tr.setContainer(newRegion));
				});
		
		
		
//		putToDiagram(newState, newRegion);
	}

}