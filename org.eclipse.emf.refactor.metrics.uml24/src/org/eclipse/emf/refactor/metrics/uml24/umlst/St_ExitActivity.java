package org.eclipse.emf.refactor.metrics.uml24.umlst;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.State;

public class St_ExitActivity implements IMetricCalculator {
	
	private List<EObject> context; 
	
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {
		State st = (State) context.get(0);
		Behavior exitBehavior = st.getExit();
		return (exitBehavior == null || !(exitBehavior instanceof Activity) ? 0.0 : 1.0);
	}
}
