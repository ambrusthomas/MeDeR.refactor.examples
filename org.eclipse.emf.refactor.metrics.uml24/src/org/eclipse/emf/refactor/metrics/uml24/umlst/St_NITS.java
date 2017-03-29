package org.eclipse.emf.refactor.metrics.uml24.umlst;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;

public final class St_NITS implements IMetricCalculator {

	private List<EObject> context;

	@Override
	public void setContext(List<EObject> context) {
		this.context = context;
	}

	@Override
	public double calculate() {
		State st = (State) context.get(0);
		if (!st.isComposite())
			return 0.0;
		List<EObject> innerStates = st.allOwnedElements()
				.stream()
				.filter(e -> (e instanceof State))
				.collect(Collectors.toList());
		int metricResult = innerStates
			.stream()
			.mapToInt(
					state -> (int) ((State) state)
						.getIncomings()
						.stream()
						.filter(tr -> innerStates.contains(tr.getSource()))
						.count())
			.sum();
		return metricResult;
//		Region region = (Region) st.getOwnedElements().get(0);
//		
//		List<Transition> transitions = st.getModel().allOwnedElements().stream().filter(e -> (e instanceof Transition)).map(e -> (Transition) e).collect(Collectors.toList());
//		return transitions.stream().filter(e -> e.getSource().getContainer().equals(region) && e.getTarget().getContainer().equals(region)).count();
	}
}