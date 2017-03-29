package org.eclipse.emf.refactor.metrics.uml24.umlst;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.uml2.uml.State;

public class St_NDEIT implements IMetricCalculator {
	
	private List<EObject> context; 
	
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {
		State st = (State) context.get(0);
		return st.getIncomings().stream()
			.flatMap(t -> {
				return t.getTriggers().stream()
						.filter(trigger -> {
							return trigger.getEvent() != null;
						})
						.map(trigger -> trigger.getEvent());
			})
			.distinct()
			.count();
	}
}
