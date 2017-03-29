package org.eclipse.emf.refactor.smells.uml24.metricbased;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.emf.refactor.smells.core.MetricBasedModelSmellFinderClass;

public class DeepNesting extends MetricBasedModelSmellFinderClass {


	private String metricId = "org.eclipse.emf.refactor.metrics.uml24.state.snl";
	private Metric localMetric = Metric.getMetricInstanceFromId(metricId);
	
	@Override
	public LinkedList<LinkedList<EObject>> findSmell(EObject root) {
		double globalLimit = this.getLimit();
		LinkedList<EObject> rootList = new LinkedList<EObject>();
		rootList.add(root);
		IMetricCalculator localCalculateClass = localMetric.getCalculateClass();
		return findSmellyObjectGroups(root, globalLimit, localCalculateClass);
	}
	
	private LinkedList<LinkedList<EObject>> findSmellyObjectGroups(EObject root, double globalLimit, 
			IMetricCalculator localCalculateClass) {
		String context = localMetric.getContext();
		LinkedList<LinkedList<EObject>> smellyEObjects = new LinkedList<LinkedList<EObject>>();
		List<EObject> containedEObjects = root.eContents();
		for(EObject object : containedEObjects){
			String objectType = object.eClass().getInstanceClass().getSimpleName();
			if(objectType.equals(context)){
				LinkedList<EObject> rootList = new LinkedList<EObject>();
				rootList.add(object);
				localCalculateClass.setContext(rootList);
				double localValue = localCalculateClass.calculate();				
				if(limitReached(localValue, globalLimit)) {
					LinkedList<EObject> currentObjects = new LinkedList<EObject>();
					currentObjects.add(object);
					smellyEObjects.add((currentObjects));
				}				
			} else {
				smellyEObjects.addAll(findSmellyObjectGroups(object, globalLimit, localCalculateClass));
			}
		}
		return smellyEObjects;
	}
	
	private boolean limitReached(double localValue, double globalLimit) {
		return (localValue > globalLimit);
	}

}
