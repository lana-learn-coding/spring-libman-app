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
                            <form class="theme-form login-form needs-validation" method="post">
                                <sec:csrfInput/>
                                <h4>Login</h4>
                                <h6>Welcome back! Log in to your account.</h6>
                                <div class="form-group">
                                    <label for="username">Email or Username</label>
                                    <div class="input-group has-validation">
                                        <span class="input-group-text"><i class="icon-email"></i></span>
                                        <input class="form-control <c:if test="${not empty param.error}">is-invalid</c:if>"
                                               type="text" placeholder="Account@gmail.com"
                                               name="username" id="username">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="password">Password</label>
                                    <div class="input-group has-validation">
                                        <span class="input-group-text"><i class="icon-lock"></i></span>
                                        <input class="form-control <c:if test="${not empty param.error}">is-invalid</c:if>"
                                               type="password" name="password"
                                               placeholder="*********" required id="password">
                                        <c:if test="${not empty param.error}">
                                            <div class="invalid-feedback">${SPRING_SECURITY_LAST_EXCEPTION.message}</div>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="checkbox">
                                        <input id="checkbox1" type="checkbox" name="remember-me">
                                        <label for="checkbox1">Remember me</label>
                                    </div>
                                    <a class="link" href="${pageContext.request.contextPath}/reset-password" up-follow>Forgot
                                        password?</a>
                                </div>
                                <div class="form-group">
                                    <button class="btn btn-primary btn-block w-100" type="submit">Sign in</button>
                                </div>
                                <div class="login-social-title">
                                    <h5>Libman Library</h5>
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
