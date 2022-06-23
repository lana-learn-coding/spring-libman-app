<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<header class="main-nav">
    <sec:authorize access="isAuthenticated()">
        <div class="sidebar-user text-center">
            <a class="setting-primary" href="javascript:void(0)"><i data-feather="settings"></i></a>
            <sec:authentication property="principal.avatar" var="avatar"/>
            <img class="img-90 rounded-circle"
                 src="${pageContext.request.contextPath}${(empty avatar ? '/static/images/avatar/default.png' : avatar)}"
                 alt="avatar">
            <div class="badge-bottom"><span class="badge badge-primary">New</span>
            </div>
            <a href="#">
                <sec:authentication property="principal.lastName" var="lastName"/>
                <sec:authentication property="principal.firstName" var="firstName"/>
                <h6 class="mt-3 f-14 f-w-600">
                    <span>${empty firstName ? 'Anon' : firstName}</span>
                    <span>${empty lastName ? '' : lastName}</span>
                </h6>
            </a>
            <p class="mb-0 font-roboto">Human Resources Department</p>
        </div>
    </sec:authorize>
    <sec:authorize access="!isAuthenticated()">
        <div class="mt-2"></div>
    </sec:authorize>
    <nav>
        <div class="main-navbar" up-nav="include-query">
            <div id="mainnav">
                <ul class="nav-menu custom-scrollbar">
                    <li class="back-btn">
                        <div class="mobile-back text-end">
                            <span>Back</span>
                            <i class="fa fa-angle-right ps-2" aria-hidden="true"></i>
                        </div>
                    </li>
                    <li class="sidebar-main-title">
                        <div>
                            <h6>Home</h6>
                        </div>
                    </li>
                    <li>
                        <a id="back-link" class="nav-link" href="${pageContext.request.contextPath}/" up-back up-hungry>
                            <i data-feather="arrow-left"></i><span>Go Back</span>
                        </a>
                    </li>
                    <li>
                        <a class="nav-link" href="${pageContext.request.contextPath}/library/dashboard">
                            <i data-feather="bar-chart-2"></i><span>Dashboard</span>
                        </a>
                    </li>
                    <li>
                        <a class="nav-link" href="${pageContext.request.contextPath}/home">
                            <i data-feather="home"></i><span>Home</span>
                        </a>
                    </li>
                    <li class="sidebar-main-title">
                        <div>
                            <h6>Borrow</h6>
                        </div>
                    </li>
                    <li>
                        <a class="nav-link" href="${pageContext.request.contextPath}/library/readers"
                           up-alias="${pageContext.request.contextPath}/library/readers/*"
                           up-follow up-instant>
                            <i data-feather="user-check"></i><span>Reader</span>
                        </a>
                    </li>
                    <li>
                        <a class="nav-link" href="${pageContext.request.contextPath}/library/borrow/borrow">
                            <i data-feather="calendar"></i><span>Borrow</span>
                        </a>
                    </li>
                    <li class="sidebar-main-title">
                        <div>
                            <h6>Book</h6>
                        </div>
                    </li>
                    <li>
                        <a class="nav-link" href="${pageContext.request.contextPath}/library/books/infos"
                           up-alias="${pageContext.request.contextPath}/library/books/infos/*"
                           up-follow up-instant>
                            <i data-feather="book"></i><span>Book Info</span>
                        </a>
                    </li>
                    <li>
                        <a class="nav-link" href="${pageContext.request.contextPath}/library/books/books">
                            <i data-feather="book-open"></i><span>Books</span>
                        </a>
                    </li>
                    <li>
                        <a class="nav-link" href="${pageContext.request.contextPath}/library/tags/series"
                           up-alias="${pageContext.request.contextPath}/library/tags/series/*"
                           up-follow up-instant>
                            <i data-feather="layers"></i><span>Book Series</span>
                        </a>
                    </li>
                    <li>
                        <a class="nav-link" href="${pageContext.request.contextPath}/library/tags/authors"
                           up-alias="${pageContext.request.contextPath}/library/tags/authors/*"
                           up-follow up-instant>
                            <i data-feather="user"></i><span>Authors</span>
                        </a>
                    </li>
                    <li>
                        <a class="nav-link" href="${pageContext.request.contextPath}/library/tags/publishers"
                           up-alias="${pageContext.request.contextPath}/library/tags/publishers/*"
                           up-follow up-instant>
                            <i data-feather="user"></i><span>Publisher</span>
                        </a>
                    </li>
                    <li>
                        <a class="nav-link" href="${pageContext.request.contextPath}/library/tags/genres"
                           up-alias="${pageContext.request.contextPath}/library/tags/genres/*"
                           up-follow up-instant>
                            <i data-feather="tag"></i><span>Genres</span>
                        </a>
                    </li>
                    <li>
                        <a class="nav-link" href="${pageContext.request.contextPath}/library/tags/shelf"
                           up-alias="${pageContext.request.contextPath}/library/tags/shelf/*"
                           up-follow up-instant>
                            <i data-feather="package"></i><span>Shelf</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>

