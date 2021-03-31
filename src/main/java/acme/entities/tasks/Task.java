package acme.entities.tasks;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Task extends DomainEntity{

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;
	
	// Attributes -------------------------------------------------------------
	
	@NotBlank  
	@Length(min=0, max=80)
	protected String         title;
	
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Future
	protected Date           periodInitial;
	
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Future
	protected Date           periodFinal;
	
	@NotBlank  
	@Length(min=0, max=500)
	protected String         description;
	
	@URL
	protected String        link;
	
	@NotNull
	protected Boolean       isPublic;
	
	
	// Derived attributes -----------------------------------------------------
	
	public Long workloadInHours() {
		Long duracion;
		duracion=0L;
		
		final long diferenceInMiliseconds = Math.abs(this.periodFinal.getTime() - this.periodInitial.getTime());
		duracion = TimeUnit.HOURS.convert(diferenceInMiliseconds, TimeUnit.MILLISECONDS);
		return duracion;
	}
	public Long workloadInMinutes() {
		Long duracion;
		duracion=0L;
		
		final long diferenceInMiliseconds = Math.abs(this.periodFinal.getTime() - this.periodInitial.getTime());
		duracion = TimeUnit.MINUTES.convert(diferenceInMiliseconds, TimeUnit.MILLISECONDS);
		return duracion;
	}
	
	// Relationships ----------------------------------------------------------

	
}
