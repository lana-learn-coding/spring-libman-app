<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--@elvariable id="summary" type="io.lana.libman.core.home.dashboard.DashboardSummary"--%>

<layout:librarian>
    <jsp:attribute name="title">Dashboard</jsp:attribute>
    <jsp:attribute name="body">
        <jsp:include page="/WEB-INF/presets/counter.head.jsp"/>
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-6">
                        <h3>Dashboard</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">Home</li>
                            <li class="breadcrumb-item dashboard">Dashboard</li>
                        </ol>
                    </div>
                    <div class="col-sm-6">
                        <!-- Bookmark Start-->
                        <div class="bookmark">
                            <ul>
                                <sec:authorize access="hasAnyAuthority('ADMIN', 'READER_READ')">
                                    <li><a href="${pageContext.request.contextPath}/library/readers"
                                           up-follow up-instant><i
                                            data-feather="user-check"></i></a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOKINFO_READ')">
                                    <li><a href="${pageContext.request.contextPath}/library/books/infos"
                                           up-follow up-instant><i
                                            data-feather="book"></i></a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOKBORROW_READ')">
                                    <li><a href="${pageContext.request.contextPath}/library/readers"
                                           up-follow up-instant><i
                                            data-feather="calendar"></i></a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOKBORROW_READ')">
                                    <li><a href="${pageContext.request.contextPath}/library/history"
                                           up-follow up-instant><i
                                            data-feather="clock"></i></a></li>
                                </sec:authorize>
                            </ul>
                        </div>
                        <!-- Bookmark Ends-->
                    </div>
                </div>
            </div>
        </div>
        <div class="container-fluid general-widget">
            <div class="row">
                <div class="col-sm-6 col-xl-3 col-lg-6">
                    <div class="card o-hidden border-0">
                        <div class="bg-primary b-r-4 card-body">
                            <div class="media static-top-widget">
                                <div class="align-self-center text-center"><i data-feather="calendar"></i></div>
                                <div class="media-body"><span class="m-0">Borrows</span>
                                    <h4 class="mb-0 counter">
                                        <fmt:formatNumber value="${summary.borrowsCount}"/>
                                    </h4>
                                    <i class="icon-bg" data-feather="calendar"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 col-xl-3 col-lg-6">
                    <div class="card o-hidden border-0">
                        <div class="bg-secondary b-r-4 card-body">
                            <div class="media static-top-widget">
                                <div class="align-self-center text-center"><i data-feather="clock"></i></div>
                                <div class="media-body"><span class="m-0">Over Dues</span>
                                    <h4 class="mb-0 counter">
                                        <fmt:formatNumber value="${summary.overDuesCount}"/>
                                    </h4>
                                    <i class="icon-bg" data-feather="clock"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 col-xl-3 col-lg-6">
                    <div class="card o-hidden border-0">
                        <div class="bg-primary b-r-4 card-body">
                            <div class="media static-top-widget">
                                <div class="align-self-center text-center"><i data-feather="book"></i>
                                </div>
                                <div class="media-body"><span class="m-0">Books</span>
                                    <h4 class="mb-0 counter">
                                        <fmt:formatNumber value="${summary.booksCount}"/>
                                    </h4>
                                    <i class="icon-bg" data-feather="book"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 col-xl-3 col-lg-6">
                    <div class="card o-hidden border-0">
                        <div class="bg-primary b-r-4 card-body">
                            <div class="media static-top-widget">
                                <div class="align-self-center text-center"><i data-feather="user-check"></i></div>
                                <div class="media-body"><span class="m-0">Readers</span>
                                    <h4 class="mb-0 counter">
                                        <fmt:formatNumber value="${summary.readersCount}"/>
                                    </h4>
                                    <i class="icon-bg" data-feather="user-check"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:librarian>
