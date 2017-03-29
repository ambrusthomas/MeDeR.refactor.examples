package org.eclipse.emf.refactor.metrics.uml24.umlst;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Vertex;

public class St_NIET implements IMetricCalculator {
	
	private List<EObject> context; 
	private List<Region> regions;
	private Region container;
	
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
		container = ((State) context.get(0)).getContainer();
		regions = ((State) context.get(0)).getRegions();
	}	
		
	@Override
	public double calculate() {
		State st = (State) context.get(0);
		return st.getIncomings().stream().filter(t -> isExternal(t.getSource()) && t.getSource().getContainer() != container).count()
				+ st.getOutgoings().stream().filter(t -> isExternal(t.getTarget()) && t.getTarget().getContainer() != container).count();
	}
	
	private boolean isExternal(Vertex s) {
		if (regions.size() == 0) return true;
		
		Region currentContainer = s.getContainer();
		while (currentContainer != null) {
			if (!regions.contains(currentContainer)) {
				currentContainer = currentContainer.getState().getContainer();
			}
			else return false;
		}
		return true;
	}
}