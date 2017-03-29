/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefactoringWizardPage.javajet,v 1.1 2010/07/15 13:08:44 tarendt Exp $
 */
package org.eclipse.emf.refactor.refactorings.uml24.samelabel;

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
import org.eclipse.uml2.uml.Transition;

import com.google.inject.Guice;

public class RefactoringWizardPage extends UserInputWizardPage implements
		Listener {

	private final RefactoringController controller;
	Combo combo;
	private List<Transition> transitions;
	private EnableContentProposalService service = Guice.createInjector().getInstance(EnableContentProposalService.class);

	public RefactoringWizardPage(String name, RefactoringController controller) {
		super(name);
		this.controller = controller;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayout(gl);

		new Label(composite, SWT.NONE).setText("Select transition: ");
		combo = new Combo(composite, SWT.NONE);

		transitions = getTransitions();
		List<String> items = transitions.stream()
				.map(t -> t.getQualifiedName()).collect(Collectors.toList());

		combo.setItems(items.toArray(new String[items.size()]));
		combo.setData("transitions", transitions);
		combo.setEnabled(true);
		combo.setLayoutData(gd);
		combo.addListener(SWT.Modify, this);

		service.enableContentProposal(combo);

		setControl(composite);
	}

	private List<Transition> getTransitions() {
		Transition t = (Transition) this.controller
				.getDataManagementObject()
				.getInPortByName(
						new RefactoringDataManagement().SELECTEDEOBJECT)
				.getValue();
		List<Transition> items = t.getModel().allOwnedElements().stream()
				.filter(e -> (e instanceof Transition))
				.map(e -> (Transition) e).collect(Collectors.toList());

		return items;
	}

	@Override
	public void handleEvent(Event event) {
		String text = combo.getText();

		transitions.stream().filter(t -> text.equals(t.getQualifiedName()))
				.findAny().ifPresent(t -> {
					setValue(t);
				});
	}

	@SuppressWarnings("unchecked")
	private void setValue(Transition t) {
		((RefactoringDataManagement) this.controller.getDataManagementObject())
				.getInPortByName("transition").setValue(t);
	}

}
