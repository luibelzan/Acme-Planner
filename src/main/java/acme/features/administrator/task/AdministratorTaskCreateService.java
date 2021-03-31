/*
 * AnonymousShoutCreateService.java
 *
 * Copyright (C) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.administrator.task;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tasks.Task;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Administrator;
import acme.framework.services.AbstractCreateService;

@Service
public class AdministratorTaskCreateService implements AbstractCreateService<Administrator, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorTaskRepository repository;

	// AbstractCreateService<Administrator, Shout> interface --------------

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

		request.unbind(entity, model, "title", "periodInitial", "periodFinal", "description", "isPublic");
	}

	@Override
	public Task instantiate(final Request<Task> request) {
		assert request != null;

		Task result;
		Date momentInitial;
		Date momentFinal;

		momentInitial = new Date(System.currentTimeMillis() + 20000);
		momentFinal = new Date(System.currentTimeMillis() + 30000);

		result = new Task();
		result.setTitle("Titulito");
		result.setDescription("Lorem ipsum!");
		result.setPeriodInitial(momentInitial);
		result.setPeriodFinal(momentFinal);
		result.setIsPublic(true);

		return result;
	}

	@Override
	public void validate(final Request<Task> request, final Task entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

	}

	@Override
	public void create(final Request<Task> request, final Task entity) {
		assert request != null;
		assert entity != null;

		Date momentInitial;
		Date momentFinal;

		momentInitial = new Date(System.currentTimeMillis() + 20000);
		momentFinal = new Date(System.currentTimeMillis() + 30000);
		entity.setPeriodInitial(momentInitial);
		entity.setPeriodFinal(momentFinal);
		this.repository.save(entity);
	}

}
