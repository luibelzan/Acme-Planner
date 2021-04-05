/*
 * AdministratorDashboardShowService.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tasks.Task;
import acme.forms.Dashboard;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Administrator;
import acme.framework.services.AbstractShowService;

@Service
public class AdministratorTaskShowService implements AbstractShowService<Administrator, Dashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorTaskRepository repository;

	// AbstractShowService<Administrator, Dashboard> interface ----------------


	@Override
	public boolean authorise(final Request<Dashboard> request) {
		assert request != null;

		return true;
	}

	@Override
	public void unbind(final Request<Dashboard> request, final Dashboard entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, //
			"numberPublicTask", "numberPrivateTask", "numberFinalTask", "numberNoFinalTask");
	}

	@Override
	public Dashboard findOne(final Request<Dashboard> request) {
		assert request != null;

		final Dashboard result;
		final Double numberPublicTask;
		final Double numberPrivateTask;
		final Date now = new Date();
		final List<Task> terminadas = new ArrayList<>();
		final Collection<Task> tasks = this.repository.findTasks();
		
		numberPublicTask = this.repository.numberPublicTask();
		numberPrivateTask = this.repository.numberPrivateTask();
		
		
		for (final Task t: tasks) {
			
			if (now.getTime()>=t.getPeriodFinal().getTime()) {
				terminadas.add(t);
			}
		}
		
		final Double noTerminadas = (double) (tasks.size() - terminadas.size());
		final Double term = (double) terminadas.size();
		
		result = new Dashboard();
		result.setNumberPublicTask(numberPublicTask);
		result.setNumberPrivateTask(numberPrivateTask);
		result.setNumberFinalTask(term);
		result.setNumberNoFinalTask(noTerminadas);
		
		
		return result;
	}

}
