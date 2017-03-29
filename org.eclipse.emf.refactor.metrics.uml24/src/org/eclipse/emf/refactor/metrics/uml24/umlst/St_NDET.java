package org.eclipse.emf.refactor.metrics.uml24.umlst;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Vertex;

public class St_NDET implements IMetricCalculator {

	private List<EObject> context; 
	private List<Region> regions;
	private Region container;
	private int depth;
	
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
		container = ((State) context.get(0)).getContainer();
		regions = ((State) context.get(0)).getRegions();
		calculateDepth(((Vertex) context.get(0)));
	}	
	
	private void calculateDepth(Vertex s) {
		State container = s.getContainer().getState();
		depth = 0;
		while (container != null) {
			++depth;
			container = container.getContainer().getState();
		}
	}

	@Override
	public double calculate() {
		State state = (State) context.get(0);
		return state.getIncomings().stream().filter(t -> {
			VertexData vd = getVertexData(t.getSource());
			return vd.isExternal && vd.depth > depth;
		}).count() + state.getOutgoings().stream().filter(t -> {
			VertexData vd = getVertexData(t.getTarget());
			return vd.isExternal && vd.depth > depth;
		}).count();
	}
	
	private VertexData getVertexData(Vertex s) {
		VertexData vertexData = new VertexData();
		State currentContainer = s.getContainer().getState();
		while (currentContainer != null && vertexData.isExternal) {
			if (!regions.contains(currentContainer)) {
				currentContainer = currentContainer.getContainer().getState();
				++vertexData.depth;
			}
			else {
				vertexData.isExternal = false;
			}
		}
		return vertexData;
	}
	
	private class VertexData {
		public int depth = 0;
		public boolean isExternal = true;
	}
}
