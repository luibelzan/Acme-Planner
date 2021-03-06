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
import acme.framework.services.AbstractUpdateService;

@Service
public class ManagerTaskUpdateService implements AbstractUpdateService<Manager, Task> {

	@Autowired
	private ManagerTaskRepository		repository;

	@Autowired
	protected AnonymousShoutRepository	ar;


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
	}

	@Override
	public Task findOne(final Request<Task> request) {
		assert request != null;
		Task result;
		int id;
		id = request.getModel().getInteger("id");
		result = this.repository.findOneTaskById(id);
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

		for (int i = 0; i < lsp.size(); i++) {
			errors.state(request, !lsp.get(i).isSpam(entity.getTitle()), "title", "manager.message.form.error.spam");
			errors.state(request, !lsp.get(i).isSpam(entity.getDescription()), "description", "manager.message.form.error.spam");
		}

		if (entity.getPeriodFinal() != null && entity.getPeriodInitial() != null && entity.getPeriodInitial().after(entity.getPeriodFinal())) {
			errors.state(request, false, "periodInitial", "manager.message.form.error.date");
		}

		if (entity.getPeriodFinal() != null && entity.getPeriodInitial() != null && entity.getPeriodFinal().before(entity.getPeriodInitial())) {
			errors.state(request, false, "periodFinal", "manager.message.form.error.date2");
		}

		if (entity.getWorkloadInHours() != null) {

			if (entity.getPeriodInitial() != null && entity.getPeriodFinal() != null) {

				if (entity.getWorkloadInHours() > (entity.durationPeriodInHours())) {
					errors.state(request, false, "workloadInHours", "manager.message.form.error.workload");
				}
			}

			if (entity.getWorkloadInHours() < 0) {
				errors.state(request, false, "workloadInHours", "manager.message.form.error.workload3");
			}
			
			final double number = entity.getWorkloadInHours();

			final String str = String.valueOf(number);

			final int decNumberInt = Integer.parseInt(str.substring(str.indexOf('.') + 1));
			
			if(decNumberInt<0 || decNumberInt>60 ) {
				errors.state(request, false, "workloadInHours", "manager.message.form.error.workload2");
			}
		}

	}

	@Override
	public void update(final Request<Task> request, final Task entity) {
		assert request != null;
		assert entity != null;

		boolean confirmation;
		confirmation = request.getModel().getBoolean("isPublic");
		entity.setIsPublic(confirmation);

		this.repository.save(entity);
	}
}
