package org.eclipse.emf.refactor.metrics.uml24.umlsmachine;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;

public final class NE implements IMetricCalculator {

	private List<EObject> context;

	@Override
	public void setContext(List<EObject> context) {
		this.context = context;
	}

	@Override
	public double calculate() {
		StateMachine sm = (StateMachine) context.get(0);
		int metricResult = sm.allOwnedElements()
			.stream()
			.filter(e -> (e instanceof Transition))
			.mapToInt(tr ->
			 (int)	((Transition) tr)
						.getTriggers()
						.stream()
						.filter(trigger -> trigger.getEvent() != null)
						.count()
					)
			.sum();
		return metricResult;
//		return sm.allOwnedElements().stream().filter(e -> (e instanceof Event || e instanceof Signal)).count();
	}
}