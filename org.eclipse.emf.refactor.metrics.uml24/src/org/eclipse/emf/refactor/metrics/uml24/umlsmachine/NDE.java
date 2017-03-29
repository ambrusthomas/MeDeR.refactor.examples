package org.eclipse.emf.refactor.metrics.uml24.umlsmachine;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.StateMachine;

public final class NDE implements IMetricCalculator {

	private List<EObject> context;

	@Override
	public void setContext(List<EObject> context) {
		this.context = context;
	}

	@Override
	public double calculate() {
		StateMachine sm = (StateMachine) context.get(0);

		return sm.allOwnedElements().stream().filter(e -> (e instanceof Event || e instanceof Signal)).count();
	}
}