<%--
- form.jsp
-
- Copyright (C) 2012-2021 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form readonly="${readonly}">
	<acme:form-textbox code="manager.task.form.label.title" path="title"/>
	<acme:form-textbox code="manager.task.form.label.description" path="description"/>
	<acme:form-moment code="manager.task.form.label.initial" path="periodInitial"/>
	<acme:form-moment code="manager.task.form.label.end" path="periodInitial"/>
	<acme:form-textbox code="manager.task.form.label.link" path="link"/>
	
	<jstl:if test="${!readonly}">
		<acme:form-submit code="manager.task.form.button.create" action="/manager/task/create"/>
	</jstl:if>	

	<acme:form-return code="manager.task.form.button.return"/>
</acme:form>