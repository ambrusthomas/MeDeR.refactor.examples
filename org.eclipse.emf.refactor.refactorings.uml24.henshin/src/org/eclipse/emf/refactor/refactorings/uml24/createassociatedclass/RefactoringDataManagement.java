/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringDataManagementHenshin.javajet,v 1.1 2010/07/15 13:09:58 tarendt Exp $
 */
 package org.eclipse.emf.refactor.refactorings.uml24.createassociatedclass;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.refactoring.core.Port;
import org.eclipse.emf.refactor.refactoring.henshin.interfaces.IHenshinDataManagement;
import org.eclipse.emf.refactor.refactoring.henshin.interfaces.IHenshinInformation;
import org.eclipse.emf.refactor.refactoring.runtime.DataManagementAdapter;

/**
 * Class for specific data concerning a model refactoring.
 * @generated
 */
public class RefactoringDataManagement extends DataManagementAdapter 
											implements IHenshinDataManagement {

	private final String SELECTEDEOBJECT = "selectedEObject";
	
	/**
	 * HenshinInformation object used for checking the initial
	 * conditions of the EMF model refactoring.
	 * @generated
	 */
	private RefactoringInformation initialConditionsInformation = 
								new RefactoringInformation(this);
								
	/**
	 * HenshinInformation object used for checking the final
	 * conditions of the EMF model refactoring.
	 * @generated
	 */							
	private RefactoringInformation finalConditionsInformation = 
								new RefactoringInformation(this);
								
	/**
	 * HenshinInformation object used for executing the EMF 
	 * model refactoring.
	 * @generated
	 */							
	private RefactoringInformation createChangeInformation = 
								new RefactoringInformation(this);

	/**
	 * Default constructor.
	 * @generated
	 */
	public RefactoringDataManagement() {
		this.addPorts();
		this.addInformation();
	}
	
	/**
	 * Adds the ports to the data management used for parameter passing.
	 * @generated
	 */
	private void addPorts(){
		this.inPorts.add
			(new Port<org.eclipse.uml2.uml.Class>
				(SELECTEDEOBJECT, org.eclipse.uml2.uml.Class.class));
		this.inPorts.add
			(new Port<String>
				("className", String.class, "unspecified"));
		this.inPorts.add
			(new Port<String>
				("associationName", String.class, "unspecified"));
		this.inPorts.add
			(new Port<String>
				("roleName1", String.class, "unspecified"));
		this.inPorts.add
			(new Port<String>
				("roleName2", String.class, "unspecified"));		
	}
	
	/**
	 * Adds the Henshin informations to the data management.
	 * @generated
	 */
	private void addInformation() {		
		this.initialConditionsInformation.
				setTransformationFileName("create_associated_class_check_1");			
		this.finalConditionsInformation.
				setTransformationFileName("create_associated_class_check_2");			
		this.createChangeInformation.
				setTransformationFileName("create_associated_class_execute");
	}
	
	/**
	 * @see org.eclipse.emf.refactor.henshin.core.IHenshinDataManagement#
	 * getCreateChangeHenshinInformation()
	 * @generated
	 */
	@Override
	public IHenshinInformation getCreateChangeHenshinInformation() {
		return this.createChangeInformation;
	}

	/**
	 * @see org.eclipse.emf.refactor.henshin.core.IHenshinDataManagement#
	 * getFinalConditionsHenshinInformation()
	 * @generated
	 */
	@Override
	public IHenshinInformation getFinalConditionsHenshinInformation() {
		return this.finalConditionsInformation;
	}

	/**
	 * @see org.eclipse.emf.refactor.henshin.core.IHenshinDataManagement#
	 * getInitialConditionsHenshinInformation()
	 * @generated
	 */
	@Override
	public IHenshinInformation getInitialConditionsHenshinInformation() {
		return this.initialConditionsInformation;
	}
	
	/**
	 * @see org.eclipse.emf.refactor.common.core.IDataManagement#
	 * preselect(java.util.List)
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void preselect(List<EObject> selection) {
		getInPortByName(SELECTEDEOBJECT).
				setValue((org.eclipse.uml2.uml.Class) selection.get(0));
	}

}
	