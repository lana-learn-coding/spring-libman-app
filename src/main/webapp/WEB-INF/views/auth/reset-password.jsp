<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<layout:base>
    <jsp:attribute name="title">Reset Password</jsp:attribute>
    <jsp:attribute name="body">
        <section>
            <div class="container-fluid p-0">
                <div class="row">
                    <div class="col-12">
                        <div class="login-card">
                            <form:form class="theme-form login-form needs-validation" method="post"
                                       up-submit="true" modelAttribute="entity">
                                <h4 class="mb-3">Reset Your Password</h4>
                                <sec:csrfInput/>
                                <div class="form-group">
                                    <label>New Password</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="icon-lock"></i></span>
                                        <form:input type="password"
                                                    cssClass="form-control"
                                                    cssErrorClass="form-control is-invalid"
                                                    path="password" required="true"
                                                    placeholder="*********"/>
                                        <form:errors cssClass="invalid-feedback" path="password"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label>Retype Password</label>
                                    <div class="input-group"><span class="input-group-text"><i
                                            class="icon-lock"></i></span>
                                        <form:input path="retypePassword"
                                                    cssErrorClass="form-control is-invalid"
                                                    cssClass="form-control" type="password"
                                                    required="true"
                                                    placeholder="*********"/>
                                        <form:errors cssClass="invalid-feedback" path="retypePassword"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <button class="btn btn-primary btn-block w-100" type="submit">Done</button>
                                </div>
                                <p><a class="ms-2" href="${pageContext.request.contextPath}/">Go Back Home</a></p>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </jsp:attribute>
</layout:base>
