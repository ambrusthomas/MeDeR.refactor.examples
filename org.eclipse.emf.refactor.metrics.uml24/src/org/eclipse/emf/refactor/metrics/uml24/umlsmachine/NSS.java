package org.eclipse.emf.refactor.metrics.uml24.umlsmachine;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;

public final class NSS implements IMetricCalculator {

	private List<EObject> context;

	@Override
	public void setContext(List<EObject> context) {
		this.context = context;
	}

	@Override
	public double calculate() {
		StateMachine sm = (StateMachine) context.get(0);

		return sm.allOwnedElements().stream().filter(e -> (e instanceof State && !((State) e).isComposite())).count();
	}
}