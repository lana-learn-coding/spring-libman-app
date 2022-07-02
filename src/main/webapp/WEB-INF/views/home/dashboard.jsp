<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="summary" type="io.lana.libman.core.home.dashboard.DashboardSummary"--%>
<%--@elvariable id="borrow7Days" type="java.util.Map"--%>
<%--@elvariable id="reader7Days" type="java.util.Map"--%>
<%--@elvariable id="income7Days" type="java.util.Map"--%>
<%--@elvariable id="overDues" type="java.util.List<io.lana.libman.core.book.BookBorrow>"--%>

<layout:librarian>
    <jsp:attribute name="title">Dashboard</jsp:attribute>
    <jsp:attribute name="body">
        <jsp:include page="/WEB-INF/presets/counter.head.jsp"/>
        <jsp:include page="/WEB-INF/presets/chart.head.jsp"/>
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
        <div class="row">
            <div class="col-xl-4">
                <div class="card o-hidden">
                    <div class="chart-widget-top">
                        <div class="row card-body">
                            <div class="col-5">
                                <h6 class="f-w-600 font-primary">BORROW</h6>
                            </div>
                            <div class="col-7 text-end">
                                <h4 class="num total-value"><span class="counter"><fmt:formatNumber
                                        value="${borrow7Days.today}"/></span></h4>
                            </div>
                        </div>
                        <div>
                            <div id="chart-borrow7Days"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xl-4">
                <div class="card o-hidden">
                    <div class="chart-widget-top">
                        <div class="row card-body">
                            <div class="col-7">
                                <h6 class="f-w-600 font-secondary">ACTIVE READER</h6>
                            </div>
                            <div class="col-5 text-end">
                                <h4 class="num total-value"><span class="counter"><fmt:formatNumber
                                        value="${reader7Days.today}"/></span></h4>
                            </div>
                        </div>
                        <div id="chart-reader7Days"></div>
                    </div>
                </div>
            </div>
            <div class="col-xl-4">
                <div class="card o-hidden">
                    <div class="chart-widget-top">
                        <div class="row card-body">
                            <div class="col-8">
                                <h6 class="f-w-600 font-primary">INCOME</h6>
                            </div>
                            <div class="col-4 text-end">
                                <h4 class="num total-value">$<span class="counter"><fmt:formatNumber
                                        value="${income7Days.today}"/></span></h4>
                            </div>
                        </div>
                        <div id="chart-income7Days">
                            <div class="flot-chart-placeholder" id="chart-widget-top-third"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xl-6 xl-100 box-col-12">
                <div class="card">
                    <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOKBORROW_READ')" var="canRead"/>
                    <div class="card-header pb-0 d-flex justify-content-between align-items-center">
                        <h5>OVERDUE BORROW</h5>
                        <c:if test="${canRead}">
                            <div class="setting-list">
                                <ul class="list-unstyled setting-option">
                                    <li>
                                        <a class="setting-primary"
                                           href="${pageContext.request.contextPath}/library/borrows?sort=borrowDate%2Cdesc"
                                           up-follow up-instant>
                                            <i class="icon-arrow-top-right"></i></a>
                                    </li>
                                </ul>
                            </div>
                        </c:if>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordernone">
                                <thead>
                                <tr>
                                    <th scope="col">#</th>
                                    <th scope="col">Details</th>
                                    <th scope="col">Reader</th>
                                    <th scope="col">Price</th>
                                </tr>
                                </thead>
                                <c:if test="${canRead}">
                                    <tbody>
                                    <c:forEach items="${overDues}" varStatus="loop" var="item">
                                        <tr>
                                            <td>${loop.index + 1}</td>
                                            <td>
                                                <a class="d-block"
                                                   href="${pageContext.request.contextPath}/library/borrows/${item.id}/detail"
                                                   up-follow>${item.book.title}
                                                </a>
                                                <c:if test="${not empty item.seriesName}">
                                                     <small class="small">
                                                         In Series: ${ item.seriesName }
                                                     </small>
                                                </c:if>
                                            </td>
                                            <td>
                                                <div>
                                                    <c:if test="${not empty item.reader.account.firstName}">
                                                        <span class="me-1">${item.reader.account.firstName}</span>
                                                    </c:if>
                                                    <c:if test="${not empty item.reader.account.lastName}">
                                                        <span>${item.reader.account.lastName}</span>
                                                    </c:if>
                                                </div>
                                                <a class="txt-primary" up-follow up-layer="parent root"
                                                   href="${pageContext.request.contextPath}/library/readers/${item.reader.id}/detail#borrow">
                                                        ${  item.reader.account.email }
                                                </a>
                                            </td>
                                            <td>
                                                <div>
                                                    <fmt:formatNumber value="${item.totalBorrowCost}" type="currency"
                                                                      maxFractionDigits="2"/>
                                                </div>
                                                <div class="txt-danger">
                                                    + <fmt:formatNumber value="${item.totalOverDueAdditionalCost}"
                                                                        type="currency"
                                                                        maxFractionDigits="2"/>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </c:if>
                            </table>
                            <c:if test="${not canRead or empty overDues}">
                                <component:empty message="Currently no overdue"/>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script>
            /*Line chart*/
            defer(() => {
                showApexChartsFromData('#chart-borrow7Days', {
                    chart: {
                        toolbar: {
                            show: false
                        },
                        height: 170,
                        type: 'area',
                    },
                }, ${borrow7Days.data});
                showApexChartsFromData('#chart-reader7Days', {
                    chart: {
                        toolbar: {
                            show: false
                        },
                        height: 170,
                        type: 'area',
                    },
                    colors: [vihoAdminConfig.secondary],
                }, ${reader7Days.data});
                showApexChartsFromData('#chart-income7Days', {
                    chart: {
                        toolbar: {
                            show: false
                        },
                        height: 170,
                        type: 'area',
                    },
                }, ${income7Days.data})
            });
        </script>
    </jsp:attribute>
</layout:librarian>
