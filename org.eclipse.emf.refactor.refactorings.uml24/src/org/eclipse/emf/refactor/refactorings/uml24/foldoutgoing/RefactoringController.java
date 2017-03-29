/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringController.javajet,v 1.3 2011/01/21 13:08:06 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.foldoutgoing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;

public final class RefactoringController extends EmfNotifierController
		implements IController {

	private Refactoring parent;
	private RefactoringDataManagement dataManagement = new RefactoringDataManagement();
	private List<EObject> selection = new ArrayList<>();
	private InternalRefactoringProcessor refactoringProcessor = null;

	private State composite = null;
	private State target = null;
	private Transition sample = null;
	private int targetNumber;
	private List<State> possibleTargets;
	private List<Transition> allOutgoings;
	private List<Transition> goodOutgoings;

	private List<State> innerStates;

	private List<IGraphicalEditPart> editParts;

	public void setEditParts(List<IGraphicalEditPart> editParts) {
		this.editParts = editParts;
	}

	public List<State> getPossibleTargets() {
		System.out.println(possibleTargets);
		if (possibleTargets == null)
			possibleTargets = composite
					.getRegions()
					.stream()
					.flatMap(
							region -> {
								innerStates = region
										.getOwnedElements()
										.stream()
										.filter(element -> element instanceof State)
										.map(element -> (State) element)
										.collect(Collectors.toList());
								allOutgoings = innerStates
										.stream()
										.flatMap(
												state -> ((State) state)
														.getOutgoings()
														.stream())
										.map(obj -> (Transition) obj)
										.collect(Collectors.toList());
								return allOutgoings.stream().map(
										trans -> trans.getTarget());
							}

					).map(state -> (State) state).distinct()
					.collect(Collectors.toList());
		System.out.println(innerStates);
		for (Transition t : allOutgoings) {
			System.out.println(t.getTarget());
		}
		System.out.println(allOutgoings);
		return possibleTargets;
	}

	public void setTargetNumber(int targetNumber) {
		this.targetNumber = targetNumber;
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
		this.refactoringProcessor = new InternalRefactoringProcessor(
				this.selection);
	}

	// private Runnable applyRefactoring() {
	// return new Runnable() {
	// @Override
	// public void run() {
	// System.out.println("apply");
	// //TODO create new transition
	//
	// /*innerStates.forEach(st -> st.getOutgoings().removeIf(t -> {
	// System.out.println(t.equals(sample));
	// return t.getTarget() == null || isSameAsSample(t);
	// }));*/
	// refreshPapyrusDiagram(newTransition);
	//
	// // d.putToDiagram(Arrays.asList(newTransition));
	//
	// }
	// };
	// }

	private boolean isSameAsSample(Transition t) {
		return t.getTarget().equals(sample.getTarget())
				&& bothNullOrEquals(t.getEffect(), sample.getEffect())
				&& bothNullOrEquals(t.getGuard(), sample.getGuard())
//				&& isSameTriggerList(t.getTriggers(), sample.getTriggers())
				;
	}

	private boolean bothNullOrEquals(Object a, Object b) {
		return (a == null && b == null) || (a != null && a.equals(b));
	}

	private boolean isSameTriggerList(List<Trigger> a, List<Trigger> b) {
		if (a.size() == b.size()) {
			System.out.println("equals" + a + b);
			for (int i = 0; i < a.size(); ++i) {
				if (!bothNullOrEquals(a.get(i).getEvent(), b.get(i).getEvent()))
					return false;
			}
			return true;
		}
		return false;
	}

//	private void refreshPapyrusDiagram(Transition newTransition) {
//		Runnable asyncExec = new Runnable() {
//			public void run() {
//				RefactorCommandContainer container = new RefactorCommandContainer(
//						"Fold outgoing transitions", editParts);
//				DiagramRefreshService d = Guice.createInjector().getInstance(
//						DiagramRefreshService.class);
//				// ElementsManagerUtils e =
//				// Guice.createInjector().getInstance(ElementsManagerUtils.class);
//				for (Transition t : goodOutgoings) {
//					container.removeElement(t);
//					// try {
//					// e.removeConnector(t);
//					// } catch (Exception ex) {
//					// System.out.println(ex);
//					// }
//					// t.getTarget().getIncomings().remove(t);
//					// t.getSource().getOutgoings().remove(t);
//
//					// EcoreUtil.remove(t);
//				}
//				container.addElementStable(Arrays.asList(newTransition));
//				d.executeCommandContainer(container);
//			}
//		};
//		Display.getDefault().asyncExec(asyncExec);
//	}

	public final class InternalRefactoringProcessor extends
			LtkEmfRefactoringProcessorAdapter {

		private InternalRefactoringProcessor(List<EObject> selection) {
			super(getParent(), selection, applyRefactoring());
		}

		@Override
		public RefactoringStatus checkInitialConditions() {

			RefactoringStatus result = new RefactoringStatus();
			composite = (State) selection.get(0);
			possibleTargets = null;

			return result;
		}

		@Override
		public RefactoringStatus checkFinalConditions() {
			RefactoringStatus result = new RefactoringStatus();

			// check selected transition
			// that all substates have it (and only once)
			// set sample
			target = possibleTargets.get(targetNumber);
			System.out.println(allOutgoings);
			goodOutgoings = new ArrayList<>();
			// llOutgoings.stream()
			// .filter(trans -> { System.out.println(trans.getTarget()); return
			// trans != null && trans.getTarget() != null &&
			// trans.getTarget().equals(target);})
			// .collect(Collectors.toList());
			System.out.println(goodOutgoings);

			HashMap<Transition, Integer> outGoings = new HashMap<>();
			allOutgoings
					.stream()
					.filter(trans -> trans.getTarget() == target)
					.forEach(
							trans -> {
								sample = trans;
								Optional<Transition> possibleKey = outGoings
										.keySet().stream()
										.filter(tr -> isSameAsSample(tr))
										.findFirst();
								if (possibleKey.isPresent()) {
									outGoings.replace(
											possibleKey.get(),
											outGoings.get(possibleKey.get()) + 1);
								} else {
									System.out.println(trans);
									outGoings.put(trans, 1);
								}
							});
			System.out.println("size: " + innerStates.size());
			System.out.println(innerStates);
			for (Entry<Transition, Integer> entry : outGoings.entrySet()) {
				System.out.println(entry);
				if (entry.getValue() == innerStates.size()) {
					goodOutgoings.add(entry.getKey());
				}
			}
			System.out.println(goodOutgoings);
			if (goodOutgoings.isEmpty()) {
				result.addFatalError("Not all states have an outgoing transition into the selected state.");
				return result;
			}
			sample = goodOutgoings.get(0);
			if (!innerStates.stream().allMatch(
					st -> st.getOutgoings().stream()
							.anyMatch(tr -> isSameAsSample(tr)))) {
				result.addFatalError("222Not all states have an outgoing transition into the selected state.");
				return result;
			}

			// check if all substates have it
			// check if only one matching transition is there
			// otherwise the user must also select a transition
			if (goodOutgoings.stream().filter(trans -> !isSameAsSample(trans))
					.count() > 0) {
				result.addFatalError("It is not obvious which transition you want to fold."
				/*
				 * +
				 * " Please select one of the correct transitions and then refactor again."
				 */);
			}
			// List<State> sourceStates = goodOutgoings.stream()
			// .map(trans -> (State) trans.getSource())
			// .collect(Collectors.toList());
			// if (sourceStates.size() < innerStates.size()) {
			// result.addFatalError("Not all states have an outgoing transition into the selected state.");
			//
			// }
			// else if (sourceStates.size() > innerStates.size()) {
			// result.addFatalError("There are states that have more than one outgoings to the selected state.");
			// }
			// else {
			// innerStates.forEach(state -> {
			// if (!sourceStates.contains(state)) {
			// result.addFatalError("Not all states have an outgoing transition into the selected state.");
			// }
			// });
			// }
			return result;
		}

	}

	@Override
	protected void refactoringBody() {
		Transition newTransition = ((Region) composite.getOwner())
				.createTransition(null);
		newTransition.setSource(composite);
		newTransition.setTarget(target);
		newTransition.setEffect(sample.getEffect());
		newTransition.setGuard(sample.getGuard());
		newTransition.getTriggers().addAll(sample.getTriggers());

		// TODO remove all transitions
//		System.out.println("remove"
//				+ goodOutgoings.get(0).getOwner().getOwner().getOwner());
		ArrayList<Transition> deletable = new ArrayList<>();
		Iterator<EObject> iterator = composite.eAllContents();
		while (iterator.hasNext()) {
			EObject e = iterator.next();
			if (e instanceof State) {
				State st = (State) e;
				for (Transition t :st.getOutgoings()) {
					if (isSameAsSample(t)) {
						deletable.add(t);
					}
				}
			}
		}
		for (Transition t : deletable) {
			// bugfix for metrics
			t.getTarget().getIncomings().remove(t);
			EcoreUtil.delete(t);
		}
		System.out.println(target.getIncomings());
	}

}