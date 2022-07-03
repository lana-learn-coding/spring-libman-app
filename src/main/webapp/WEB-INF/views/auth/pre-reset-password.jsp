<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<layout:base>
    <jsp:attribute name="title">Login</jsp:attribute>
    <jsp:attribute name="body">
        <section>
            <div class="container-fluid p-0">
                <div class="row">
                    <div class="col-12">
                        <div class="login-card">
                            <form class="theme-form login-form needs-validation" method="post" up-submit>
                                <sec:csrfInput/>
                                <h4>Forgot password</h4>
                                <h6>We will send you email to reset password</h6>
                                <div class="form-group">
                                    <label for="email">Enter your email</label>
                                    <div class="input-group has-validation">
                                        <span class="input-group-text"><i class="icon-email"></i></span>
                                        <input class="form-control"
                                               type="text" placeholder="Account@gmail.com"
                                               name="email" id="email">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <button class="btn btn-primary" type="submit">Send</button>
                                </div>
                                <p><a class="ms-2" href="${pageContext.request.contextPath}/">Go Back Home</a></p>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </jsp:attribute>
</layout:base>
