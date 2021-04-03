package acme.forms;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardTask implements  Serializable {
	// Serialisation identifier -----------------------------------------------

		protected static final long	serialVersionUID	= 1L;

		// Attributes -------------------------------------------------------------
		//Esto es para la tabla relacionado con las tareas
		Double                      numberPublicTask;
		Double                      numberPrivateTask;

		// Derived attributes -----------------------------------------------------

		// Relationships ----------------------------------------------------------

}
