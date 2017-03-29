package org.eclipse.emf.refactor.metrics.uml24.umlsmachine;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;

public final class NTA implements IMetricCalculator {

	private List<EObject> context;

	@Override
	public void setContext(List<EObject> context) {
		this.context = context;
	}

	@Override
	public double calculate() {
		StateMachine sm = (StateMachine) context.get(0);

		return sm.allOwnedElements().stream().filter(e -> (e instanceof Transition && ((Transition) e).getEffect() != null)).count();
	}
}