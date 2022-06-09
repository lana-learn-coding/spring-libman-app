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
        <div class="main-navbar">
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
                            <h6>General </h6>
                        </div>
                    </li>
                    <li class="dropdown"><a class="nav-link menu-title" href="../theme/index.html"
                                            target="_blank"><i
                            data-feather="home"></i><span>Dashboard</span></a></li>
                    <li class="dropdown"><a class="nav-link menu-title" href="javascript:void(0)"><i
                            data-feather="anchor"></i><span>Starter kit</span></a>
                        <ul class="nav-submenu menu-content">
                            <li><a class="submenu-title" href="javascript:void(0)">color version<span
                                    class="sub-arrow"><i class="fa fa-chevron-right"></i></span></a>
                                <ul class="nav-sub-childmenu submenu-content">
                                    <li><a href="index.tag">Layout Light</a></li>
                                    <li><a href="layout-dark.html">Layout Dark</a></li>
                                </ul>
                            </li>
                            <li><a class="submenu-title" href="javascript:void(0)">Page layout<span
                                    class="sub-arrow"><i class="fa fa-chevron-right"></i></span></a>
                                <ul class="nav-sub-childmenu submenu-content">
                                    <li><a href="boxed.html">Boxed</a></li>
                                    <li><a href="layout-rtl.html">RTL </a></li>
                                </ul>
                            </li>
                            <li><a class="submenu-title" href="javascript:void(0)">Footers<span
                                    class="sub-arrow"><i class="fa fa-chevron-right"></i></span></a>
                                <ul class="nav-sub-childmenu submenu-content">
                                    <li><a href="footer-light.html">Footer Light</a></li>
                                    <li><a href="footer-dark.html">Footer Dark</a></li>
                                    <li><a href="footer-fixed.html">Footer Fixed</a></li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                    <li class="dropdown"><a class="nav-link menu-title"
                                            href="http://support.pixelstrap.com/help-center" target="_blank"><i
                            data-feather="headphones"></i><span>Raise Support</span></a></li>
                    <li class="dropdown"><a class="nav-link menu-title"
                                            href="https://docs.pixelstrap.com/viho/document/" target="_blank"><i
                            data-feather="file-text"></i><span>Documentation                                    </span></a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>

