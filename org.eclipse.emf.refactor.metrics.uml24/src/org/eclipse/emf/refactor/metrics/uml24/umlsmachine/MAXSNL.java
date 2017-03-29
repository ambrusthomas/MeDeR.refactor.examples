package org.eclipse.emf.refactor.metrics.uml24.umlsmachine;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;

public class MAXSNL implements IMetricCalculator {

	private List<EObject> context;
	private String metricID = "org.eclipse.emf.refactor.metrics.uml24.state.snl";
	private double nesting = 0;

	@Override
	public void setContext(List<EObject> context) {
		this.context = context;	
	}
	
	private void setNesting(Double nesting) {
		this.nesting = nesting;
	}

	@Override
	public double calculate() {
		Metric metric = Metric.getMetricInstanceFromId(metricID);
		IMetricCalculator calc = metric.getCalculateClass();
			
		StateMachine sm = (StateMachine) context.get(0);
		
		sm.allOwnedElements().stream().filter(e -> e instanceof State).map(e -> {
			State s = (State) e;
			
			calc.setContext(Arrays.asList(s));
			return calc.calculate();
		}).max(new Comparator<Double>() {

			@Override
			public int compare(Double o1, Double o2) {
				if (o1 < o2) return -1;
				else if (o2 < o1) return 1;
				return 0;
			}
			
		}).ifPresent(m -> {
			setNesting(m);
		});
		
		return nesting;
	}

}
