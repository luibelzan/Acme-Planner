/*
 * AnonymousShoutListService.java
 *
 * Copyright (C) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.anonymous.shout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.shouts.Shout;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Anonymous;
import acme.framework.services.AbstractListService;

@Service
public class AnonymousShoutListService implements AbstractListService<Anonymous, Shout> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnonymousShoutRepository repository;


	// AbstractListService<Administrator, Shout> interface --------------

	@Override
	public boolean authorise(final Request<Shout> request) {
		assert request != null;

		return true;
	}

	@Override
	public void unbind(final Request<Shout> request, final Shout entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "author", "text", "moment");
	}
	
	public int compare(final Date a, final Date b) {
        return a.compareTo(b);
    }

	@Override
	public List<Shout> findMany(final Request<Shout> request) {
		assert request != null;
		final List<Shout> res = new ArrayList<>();
		final Collection<Shout> shouts = this.repository.findMany();
		final Date now = new Date();
		for(final Shout s: shouts) {
			if(this.restarMeses(s.getMoment(), 1).after(now)){
				res.add(s);
			}
		}
		//res.sort(Comparator.comparing(naturalOrder));
		Collections.sort(res, Comparator.comparing(x->x.getMoment()));
		for(final Shout s: res) {
			System.out.println(s.getMoment());
		}
		return res;
	}
	
	public Date restarMeses(final Date fecha, final int meses) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.MONTH, meses);
		return calendar.getTime();
	}

}
