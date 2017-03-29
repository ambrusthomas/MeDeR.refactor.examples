package org.eclipse.emf.refactor.metrics.uml24.umlsmachine;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.uml2.uml.StateMachine;

public final class CC implements IMetricCalculator {

	private List<EObject> context;
	private String metricID1 = "org.eclipse.emf.refactor.metrics.uml24.nt";
	private String metricID2 = "org.eclipse.emf.refactor.metrics.uml24.ns";

	@Override
	public void setContext(List<EObject> context) {
		this.context = context;
	}

	@Override
	public double calculate() {
		StateMachine sm = (StateMachine) context.get(0);
		
		Metric metric1 = Metric.getMetricInstanceFromId(metricID1);
		Metric metric2 = Metric.getMetricInstanceFromId(metricID2);
		
		IMetricCalculator calc1 = metric1.getCalculateClass();
		calc1.setContext(Arrays.asList(sm));
		
		IMetricCalculator calc2 = metric2.getCalculateClass();
		calc2.setContext(Arrays.asList(sm));
		
		return calc1.calculate() - calc2.calculate() + 2;
	}
}