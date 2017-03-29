package org.eclipse.emf.refactor.metrics.uml24.umlsmachine;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;

public final class UUE implements IMetricCalculator {

	private List<EObject> context;

	@Override
	public void setContext(List<EObject> context) {
		this.context = context;
	}

	@Override
	public double calculate() {
		StateMachine sm = (StateMachine) context.get(0);

		List<Object> events = sm.allOwnedElements().stream()
				.filter(e -> e instanceof Transition)
				.flatMap(e -> ((Transition) e).getTriggers().stream())
				.map(e -> ((Trigger) e).getEvent())
				.collect(Collectors.toList());
		return sm.getModel().allOwnedElements().stream()
				.filter(e -> e instanceof Event && !events.contains(e)).count();
	}
}