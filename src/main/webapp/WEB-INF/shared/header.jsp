<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="page-main-header">
    <div class="main-header-right row m-0">
        <div class="main-header-left">
            <div class="logo-wrapper"><a href="index.tag"><img class="img-fluid" src="/static/images/logo/logo.png"
                                                               alt=""></a></div>
            <div class="dark-logo-wrapper"><a href="index.tag"><img class="img-fluid"
                                                                    src="/static/images/logo/dark-logo.png" alt=""></a>
            </div>
            <div class="toggle-sidebar"><i class="status_toggle middle" data-feather="align-center"
                                           id="sidebar-toggle"></i></div>
        </div>
        <div class="left-menu-header col">
            <ul>
                <li>
                    <form class="form-inline search-form">
                        <div class="search-bg"><i class="fa fa-search"></i>
                            <input class="form-control-plaintext" placeholder="Search here.....">
                        </div>
                    </form>
                    <span class="d-sm-none mobile-search search-bg"><i class="fa fa-search"></i></span>
                </li>
            </ul>
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
<sec:authorize access="isAuthenticated()">
    <div class="modal fade" tabindex="-1" id="logout-modal" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <form class="modal-content" action="${pageContext.request.contextPath}/logout"
                  method="post">
                <sec:csrfInput/>
                <div class="modal-body d-flex flex-column align-items-center pt-4 pb-3">
                    <i data-feather="info" class="txt-info" style="width: 100px; height: 100px; stroke-width: 1"></i>
                    <h3 class="f-w-600 mt-3">Log Out</h3>
                    <span>You are about to logout</span>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">back</button>
                    <button type="submit" class="btn btn-primary">Log out</button>
                </div>
            </form>
        </div>
    </div>
</sec:authorize>
