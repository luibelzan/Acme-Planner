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

package acme.features.administrator.dashboard;

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
public class AdministratorDashboardShowService implements AbstractShowService<Administrator, Dashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorDashboardRepository repository;

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
			"averageNumberOfJobsPerEmployer", "averageNumberOfApplicationsPerWorker", // 
			"avegageNumberOfApplicationsPerEmployer", "ratioOfPendingApplications", //
			"ratioOfRejectedApplications", "ratioOfAcceptedApplications",
			"numberPublicTask", "numberPrivateTask", "numberFinalTask", "numberNoFinalTask","averageWorkloadsTasks");
	}

	@Override
	public Dashboard findOne(final Request<Dashboard> request) {
		assert request != null;

		Dashboard result;
		Double averageNumberOfApplicationsPerEmployer;
		Double averageNumberOfApplicationsPerWorker;
		Double averageNumberOfJobsPerEmployer;
		Double ratioOfPendingApplications;
		Double ratioOfAcceptedApplications;
		Double ratioOfRejectedApplications;
		
		
		
		final Double numberPublicTask;
		final Double numberPrivateTask;
		final Date now = new Date();
		final List<Task> terminadas = new ArrayList<>();
		final Collection<Task> tasks = this.repository.findTasks();
		Double averageWorkloadsTasks;
		Double deviationWorkloadsTasks;
		Double minimumWorkloadsTasks;
		Double maximumWorkloadsTasks;
		

		averageNumberOfApplicationsPerEmployer = this.repository.averageNumberOfApplicationsPerEmployer();
		averageNumberOfApplicationsPerWorker = this.repository.averageNumberOfApplicationsPerWorker();
		averageNumberOfJobsPerEmployer = this.repository.averageNumberOfJobsPerEmployer();
		ratioOfPendingApplications = this.repository.ratioOfPendingApplications();
		ratioOfAcceptedApplications = this.repository.ratioOfAcceptedApplications();
		ratioOfRejectedApplications = this.repository.ratioOfRejectedApplications();
		
		numberPublicTask = this.repository.numberPublicTask();
		numberPrivateTask = this.repository.numberPrivateTask();
		averageWorkloadsTasks=0.0;
		deviationWorkloadsTasks= 0.0;
		maximumWorkloadsTasks=0.0;
		
		for (final Task t: tasks) {
			averageWorkloadsTasks= averageWorkloadsTasks+ t.workloadInHours();
		}
		
		
		for (final Task t: tasks) {
			//Comprueba si la tarea esta terminada
			if (now.getTime()>=t.getPeriodFinal().getTime()) {
				terminadas.add(t);
			}
		}
		for (final Task t: tasks) {
			//Esto es para calcular el maximo en los Workloads tasks
			if (t.workloadInHours()>maximumWorkloadsTasks) {
				maximumWorkloadsTasks=1.0*t.workloadInHours();
			}
		}
		//Ponemos que el minimo en el inicio sea el maximo posible, y de ahi vamos decreciendo
		minimumWorkloadsTasks=maximumWorkloadsTasks;
		for (final Task t: tasks) {
			//Para calcular el minimo de las workloads tasks
			if (t.workloadInHours()<minimumWorkloadsTasks) {
				minimumWorkloadsTasks=1.0*t.workloadInHours();
			}
		}
		final List<Double> workloadsLists= new ArrayList<>(); //Creamos una lista de workloads
		for (final Task t: tasks) {
			final Double workload= 1.0*t.workloadInHours();
			workloadsLists.add(workload);  //Metemos los workloads en la lista
		}
		deviationWorkloadsTasks= AdministratorDashboardShowService.calculateStandardDeviation(workloadsLists); //Usamos una funcion creada para calcular la desviacion tipica de  los workloads
		
		
		final Double noTerminadas = (double) (tasks.size() - terminadas.size());
		final Double term = (double) terminadas.size();
		
		result = new Dashboard();
		result.setAvegageNumberOfApplicationsPerEmployer(averageNumberOfApplicationsPerEmployer);
		result.setAverageNumberOfApplicationsPerWorker(averageNumberOfApplicationsPerWorker);
		result.setAverageNumberOfJobsPerEmployer(averageNumberOfJobsPerEmployer);
		result.setRatioOfPendingApplications(ratioOfPendingApplications);
		result.setRatioOfAcceptedApplications(ratioOfAcceptedApplications);
		result.setRatioOfRejectedApplications(ratioOfRejectedApplications);
		result.setNumberPublicTask(numberPublicTask);
		result.setNumberPrivateTask(numberPrivateTask);
		result.setNumberFinalTask(term);
		result.setNumberNoFinalTask(noTerminadas);
		result.setAverageWorkloadsTasks(averageWorkloadsTasks);
		result.setDeviationWorkloadsTasks(deviationWorkloadsTasks);
		result.setMinimumWorkloadsTasks(minimumWorkloadsTasks);
		result.setMaximumWorkloadsTasks(maximumWorkloadsTasks);

		return result;
	}
	
	
	private static double calculateStandardDeviation(final List<Double> lista) {
		//Sumatorio de los valores de la lista
		double sum=0.0;
		for (int i =0; i< lista.size(); i++) {
			sum=sum+lista.get(i);
		}
		//Obtiene la media
		final double media = sum / lista.size();
		
		//Calcula la desviacion estandar
		double standardDeviation = 0.0;
		for (int i = 0; i < lista.size(); i++) {
			standardDeviation += Math.pow(lista.get(i) - media, 2);

		}
		return Math.sqrt(standardDeviation/lista.size());
	}

}
