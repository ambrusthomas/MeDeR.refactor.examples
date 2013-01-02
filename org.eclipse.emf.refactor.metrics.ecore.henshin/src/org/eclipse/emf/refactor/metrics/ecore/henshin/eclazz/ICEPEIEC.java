package org.eclipse.emf.refactor.metrics.ecore.henshin.eclazz;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.henshin.managers.HenshinManager;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

public class ICEPEIEC implements IMetricCalculator {

	private String transformationPath = 
			getFullPath("transformations/ICEPEIEC.henshin"); 
			
	private EObject context; 
			
	@Override
	public void setContext(List<EObject> context) {
		this.context = context.get(0);
	}	
			
	@Override
	public double calculate() {
		return HenshinManager.run(transformationPath, this.context);
	}
		
	private String getFullPath(String transformationPath){
		URL url = FileLocator.find(org.eclipse.emf.refactor.metrics.ecore.henshin.Activator.getDefault().getBundle(), new Path(transformationPath), Collections.EMPTY_MAP);
		URL fileUrl = null;
		try {
			fileUrl = FileLocator.toFileURL(url);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return fileUrl.getPath();
	}	

}
