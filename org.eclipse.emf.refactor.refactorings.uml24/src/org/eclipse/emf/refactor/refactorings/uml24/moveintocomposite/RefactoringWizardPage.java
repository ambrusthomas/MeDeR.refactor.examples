/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringWizardPage.javajet,v 1.1 2010/07/15 13:08:44 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.moveintocomposite;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.refactor.service.proposal.EnableContentProposalService;
import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.uml2.uml.State;

import com.google.inject.Guice;

public class RefactoringWizardPage extends UserInputWizardPage implements Listener {
	Combo combo;
	private List<State> compositeStates;
	private EnableContentProposalService service = Guice.createInjector().getInstance(EnableContentProposalService.class);
	private final RefactoringController controller;
	
	public RefactoringWizardPage(String name, RefactoringController controller) {
		super(name);
		this.controller = controller;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		composite.setLayout(gl);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		
		new Label(composite, SWT.NONE).setText("Select composite state: ");
		combo = new Combo(composite, SWT.NONE);

		compositeStates = getCompositeStates();
		List<String> items = compositeStates.stream()
				.map(t -> t.getQualifiedName()).collect(Collectors.toList());

		combo.setItems(items.toArray(new String[items.size()]));
		combo.setEnabled(true);
		combo.setLayoutData(gd);
		combo.addListener(SWT.Modify, this);

		service.enableContentProposal(combo);
		
		setControl(composite);
	}
	
	private List<State> getCompositeStates() {
		State selectedState = (State) this.controller.getDataManagementObject()
				.getInPortByName(new RefactoringDataManagement().SELECTEDEOBJECT).getValue();
		
		return selectedState.getModel().allOwnedElements().stream()
			.filter(e -> (e instanceof State) && ((State) e).isComposite())
			.map(e -> (State) e).collect(Collectors.toList());
	}

	@Override
	public void handleEvent(Event event) {	
		String text = combo.getText();

		compositeStates.stream().filter(s -> text.equals(s.getQualifiedName()))
			.findAny().ifPresent(s -> {
				setValue(s);
			});
	}
	
	@SuppressWarnings("unchecked")
	private void setValue(State s) {
		((RefactoringDataManagement) this.controller.getDataManagementObject())
				.getInPortByName("compositeState").setValue(s);
	}
	
}
