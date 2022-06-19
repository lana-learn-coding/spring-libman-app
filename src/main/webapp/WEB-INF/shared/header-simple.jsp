<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="page-main-header header-simple">
    <div class="main-header-right row m-0">
        <div class="main-header-left">
            <div class="logo-wrapper">
                <a href="${pageContext.request.contextPath}/" class="fw-bold h4">
                    <i class="icofont icofont-library"></i> Libman
                </a>
            </div>
            <div class="dark-logo-wrapper">
                <a href="${pageContext.request.contextPath}/" class="fw-bold h4">
                    <i class="icofont icofont-library"></i> Libman
                </a>
            </div>
        </div>
        <div class="nav-right col pull-right right-menu p-0">
            <ul class="nav-menus">
                <li class="onhover-dropdown">
                    <div class="mode"><i class="fa fa-moon-o"></i></div>
                </li>
                <li class="onhover-dropdown p-0">
                    <sec:authorize access="!isAuthenticated()">
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/login">
                            Login
                        </a>
                    </sec:authorize>
                    <sec:authorize access="isAuthenticated()">
                        <button class="btn btn-primary-light"
                                type="button"
                                data-bs-toggle="modal"
                                data-bs-target="#logout-modal">
                            <i data-feather="log-out"></i> Log out
                        </button>
                    </sec:authorize>
                </li>
            </ul>
        </div>
        <div class="d-lg-none mobile-toggle pull-right w-auto"><i data-feather="more-horizontal"></i></div>
    </div>
</div>
