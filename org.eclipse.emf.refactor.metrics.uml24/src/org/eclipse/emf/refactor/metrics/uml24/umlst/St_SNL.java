package org.eclipse.emf.refactor.metrics.uml24.umlst;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;

public class St_SNL implements IMetricCalculator {
	
	private List<EObject> context; 
	
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {
		State st = (State) context.get(0);
		return nestingLevel(st)-1;
	}
	
	private int nestingLevel(State st) {
		if (st == null) return 0;
		
		int max = 0;
		for (Region r : st.getRegions()) {
			for (Element e : r.getOwnedElements()) {
				if (e instanceof State) {
					int curr = nestingLevel((State) e);
					if (curr > max) {
						max = curr;
					}
				}
			}
			
		}
		return max+1;
	}
}
