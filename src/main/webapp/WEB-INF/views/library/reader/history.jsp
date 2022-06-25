<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="p" tagdir="/WEB-INF/tags/page/tag" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--@elvariable id="highlight" type="java.lang.String"--%>
<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.core.book.BookBorrow>"--%>
<%--@elvariable id="entity" type="io.lana.libman.core.reader.Reader"--%>


<layout:modal>
    <jsp:attribute name="title">Reader Borrow History</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>Borrow History</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/dashboard"
                                   up-follow up-instant>Home</a>
                            </li>
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/readers"
                                   up-follow up-instant>Readers</a>
                            </li>
                            <li class="breadcrumb-item active">History</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-12 modal-p-0">
                    <div class="card modal-m-0">
                        <div class="card-header pb-0">
                            <h5>Reader Borrow History</h5>
                            <span>Borrow history of reader: ${entity.account.email}</span>
                        </div>
                        <div class="card-body" id="table">
                            <div class="row justify-content-between mb-3">
                                <div class="col-xs-12 col-md-3 col-lg-2 my-1">
                                     <component:sorting
                                             target="#table, [comp=sorting]"
                                             up="up-scroll='#table' up-transition='cross-fade'"
                                             labels="Title;Borrow Date;Due Date;Updated At;Updated By;Id"
                                             values="book.info.title,borrowDate,desc;dueDate,desc;updatedAt,desc;updatedBy,id"/>
                                </div>
                                <form class="col-xs-12 col-md-6 my-1"
                                      up-target="#table, nav .pagination" method="get">
                                    <helper:inherit-param excludes="query"/>
                                    <div class="input-group">
                                        <span class="input-group-text bg-primary"><i class="icon-search"></i></span>
                                        <input type="text"
                                               class="form-control form-control-lg"
                                               name="query"
                                               placeholder="Search for item"
                                               aria-label="query"
                                               value="${param.query}"
                                               up-autosubmit
                                               up-delay="400">
                                    </div>
                                </form>
                            </div>
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">Id</th>
                                        <th scope="col">Title</th>
                                        <th scope="col">Borrow</th>
                                        <th scope="col">Updated At</th>
                                        <th scope="col">Action</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:set var="content" value="${empty data ? [] : data.content}"/>
                                    <c:forEach var="item" items="${data.content}" varStatus="loop">
                                        <c:set var="isHighlight"
                                               value="${item.id == highlight}"/>
                                        <tr>
                                            <th scope="row" <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <component:index pageMeta="${data}" i="${loop.index}"/>
                                            </th>
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <div style="max-width: 120px">
                                                        ${ item.id }
                                                </div>
                                            </td>
                                            <td  <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <c:if test="${not empty item.book}">
                                                    <a href="${pageContext.request.contextPath}/library/books/infos/${item.book.info.id}/detail"
                                                       up-follow>${item.title}
                                                    </a>
                                                </c:if>
                                                <c:if test="${empty item.book}">
                                                    <div>${item.title} (Deleted)</div>
                                                </c:if>
                                                <div>
                                                    <c:if test="${empty item.returnDate}">
                                                        <span class="badge badge-warning">Borrowing</span>
                                                    </c:if>
                                                    <c:if test="${not empty item.returnDate}">
                                                        <span class="badge badge-success">Returned</span>
                                                    </c:if>
                                                </div>
                                            </td>
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <div>+ <span class="ms-1">
                                                        <helper:format-date date="${item.borrowDate}"/></span>
                                                </div>
                                                <div>+ <span class="ms-1">
                                                        <helper:format-date date="${item.dueDate}"/></span>
                                                </div>
                                                <c:if test="${not empty item.returnDate}">
                                                     <div><span class="txt-primary">
                                                        <helper:format-date date="${item.returnDate}"/></span>
                                                     </div>
                                                </c:if>
                                            </td>
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <div><helper:format-instant
                                                        date="${item.updatedAt}"/></div>
                                                <div>
                                                    by ${ not empty item.updatedBy ? item.updatedBy : item.createdBy }</div>
                                            </td>
                                            <td></td>
                                        </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                                <c:if test="${empty content}"><component:empty/></c:if>
                            </div>
                            <nav class="d-flex justify-content-start mt-3">
                                <component:pagination pageMeta="${data}" target="#table, [comp=pagination]"
                                                      up="up-scroll='layer' up-transition='cross-fade'"/>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:modal>
