<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.core.book.BookBorrow>"--%>
<%--@elvariable id="entity" type="io.lana.libman.core.user.User"--%>

<layout:librarian>
    <jsp:attribute name="title">Borrowing Books</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>Borrowing Books</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/dashboard"
                                   up-follow up-instant>Home</a>
                            </li>
                            <li class="breadcrumb-item">Users</li>
                            <li class="breadcrumb-item active">Borrowing</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
       <div class="container-fluid">
           <div class="row" id="borrow">
               <div class="col-12">
                   <div class="card">
                       <div class="card-header pb-0">
                           <h5>Borrowing Book</h5>
                           <span>List of your borrowing books (<component:total pageMeta="${data}"/>)</span>
                           <c:if test="${entity.isReader()}">
                               <c:set value="${entity.getReader().borrowLimit - data.totalElements}" var="borrowLeft"/>
                               <div class="mt-2 f-w-500 ">
                                   <span class="me-1 txt-primary">
                                       Your borrow limit is ${entity.getReader().borrowLimit}
                                   </span>
                                   <small>(${borrowLeft > 0 ? borrowLeft : 0} left)</small>
                               </div>
                           </c:if>
                       </div>
                       <div class="card-body" id="table">
                           <div class="row justify-content-between mb-3">
                               <div class="col-xs-12 col-md-3 col-lg-2 my-1">
                                    <component:sorting
                                            target="#table"
                                            up="up-scroll='#table' up-transition='cross-fade'"
                                            labels="Title;Borrow Date;Due Date"
                                            values="book.info.title;borrowDate,desc;dueDate,asc"/>
                               </div>
                               <form class="col-xs-12 col-md-6 my-1"
                                     up-target="#table, nav .pagination" up-transition='cross-fade' method="get">
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
                                       <th scope="col">Image</th>
                                       <th scope="col">Title</th>
                                       <th scope="col">Borrow At</th>
                                       <th scope="col">Due Date</th>
                                       <th scope="col">Cost</th>
                                       <th scope="col">Cost/Day</th>
                                   </tr>
                                   </thead>
                                   <tbody>
                                   <c:forEach var="item" items="${data.content}" varStatus="loop">
                                        <tr>
                                            <th scope="row">
                                                <component:index pageMeta="${data}" i="${loop.index}"/>
                                            </th>
                                            <td>
                                                <img class="round-box mx-auto"
                                                     style="width: 60px"
                                                     src="${(empty item.book.image ? '/static/images/book-default.png' : item.book.image)}"
                                                     alt="book cover">
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/home/books/${item.book.info.id}"
                                                   up-layer="new" up-size="large" up-instant
                                                   class="d-block">${item.title}
                                                </a>
                                                <c:if test="${not empty item.seriesName}">
                                                    <small>In series: ${item.seriesName}</small>
                                                </c:if>
                                            </td>
                                            <td>
                                                <helper:format-date date="${item.borrowDate}"/>
                                                <small class="d-block">
                                                    by ${ not empty item.updatedBy ? item.updatedBy : item.createdBy }
                                                </small>
                                            </td>
                                            <td>
                                                <span <c:if test="${item.isOverDue()}">class="txt-danger"</c:if>>
                                                    <helper:format-date date="${item.dueDate}"/>
                                                </span>
                                            </td>
                                            <td>
                                                <span><fmt:formatNumber value="${item.totalCost}" type="currency"
                                                                        maxFractionDigits="2"/></span>
                                            </td>
                                            <td>
                                                <div>
                                                    <fmt:formatNumber
                                                            value="${item.borrowCost}"
                                                            type="currency"
                                                            maxFractionDigits="2"/>
                                                </div>
                                                <c:if test="${item.isOverDue()}">
                                                    <div class="txt-warning">+
                                                        <fmt:formatNumber
                                                                value="${item.totalOverDueAdditionalCost}"
                                                                type="currency"
                                                                maxFractionDigits="2"/>
                                                    </div>
                                                </c:if>
                                            </td>
                                        </tr>
                                        </c:forEach>
                                   </tbody>
                               </table>
                               <c:if test="${empty data.content}"><component:empty/></c:if>
                           </div>
                           <nav class="d-flex justify-content-between mt-3">
                                <component:pagination pageMeta="${data}"
                                                      target="#table"
                                                      up="up-scroll='#table' up-transition='cross-fade'"/>
                               <div class="d-none d-md-block pt-2">
                                   <component:total pageMeta="${data}" verbose="true"/>
                               </div>
                           </nav>
                       </div>
                   </div>
               </div>
           </div>
       </div>
    </jsp:attribute>
</layout:librarian>

