/*
 * AdministratorAnnouncementCreateService.java
 *
 * Copyright (C) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.manager.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.spamWords.SpamWord;
import acme.entities.tasks.Task;
import acme.features.anonymous.shout.AnonymousShoutRepository;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Manager;
import acme.framework.services.AbstractCreateService;

@Service
public class ManagerTaskCreateService implements AbstractCreateService<Manager, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ManagerTaskRepository repository;
	
	@Autowired
	protected AnonymousShoutRepository ar;

	// AbstractCreateService<Administrator, Announcement> interface --------------


	@Override
	public boolean authorise(final Request<Task> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final Request<Task> request, final Task entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<Task> request, final Task entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "description", "isPublic", "link", "periodFinal", "periodInitial", "title", "workloadInHours");
//		model.setAttribute("confirmation", false);
//		model.setAttribute("readonly", false);
	}

	@Override
	public Task instantiate(final Request<Task> request) {
		assert request != null;

		Task result;
	
		result = new Task();
		result.setDescription("");
		result.setLink("");
		result.setPeriodFinal(null);
		result.setPeriodInitial(null);
		result.setTitle(null);
		result.setWorkloadInHours(null);
		result.setIsPublic(false);
		final Integer id = request.getPrincipal().getActiveRoleId();
		final Manager m = this.repository.managerById(id);
		result.setManager(m);

		return result;
	}

	@Override
	public void validate(final Request<Task> request, final Task entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		final Collection<SpamWord> sp = this.ar.findManySpamWord();
		final List<SpamWord> lsp = new ArrayList<>();
		lsp.addAll(sp);

		final boolean textHasErrors = errors.hasErrors("title");
		final boolean descHasErrors = errors.hasErrors("description");

		if (!textHasErrors || !descHasErrors) {
			for (int i = 0; i < lsp.size(); i++) {
				errors.state(request, !lsp.get(i).isSpam(entity.getTitle()), "title", "manager.message.form.error.spam");
				errors.state(request, !lsp.get(i).isSpam(entity.getDescription()), "description", "manager.message.form.error.spam");
			}
		}

	}

	@Override
	public void create(final Request<Task> request, final Task entity) {
		assert request != null;
		assert entity != null;
		
		boolean confirmation;
		confirmation = request.getModel().getBoolean("isPublic");
		entity.setIsPublic(confirmation);

		this.repository.save(entity);
	}

}