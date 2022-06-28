<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--@elvariable id="title" type="java.lang.String"--%>
<%--@elvariable id="id" type="java.lang.String"--%>
<%--@elvariable id="entity" type="io.lana.libman.core.reader.Reader"--%>
<layout:modal>
    <jsp:attribute name="title">Return All Borrow</jsp:attribute>
    <jsp:attribute name="body">
        <div class="modal d-block">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-body d-flex flex-column align-items-center pt-4 pb-3">
                        <i data-feather="alert-triangle" class="txt-warning"
                           style="width: 100px; height: 100px; stroke-width: 1"></i>
                        <h3 class="f-w-600 mt-3">Return All Borrow</h3>
                        <span>${entity.account.email}</span>
                        <span class="small">Total Book: ${entity.borrowingBooksCount}</span>
                        <c:if test="${cost > 0}">
                            <h5 class="txt-danger mt-3">Total Cost
                                <fmt:formatNumber type="currency" value="${cost}"/></h5>
                        </c:if>
                    </div>
                    <div class="modal-footer">
                        <a href="${pageContext.request.contextPath}/library/borrows"
                           class="btn btn-light" up-dismiss up-follow up-back>Back</a>

                        <form action="${pageContext.request.contextPath}/library/borrows/batch/${id}/return"
                              method="post" up-layer="root" up-scroll="false">
                            <sec:csrfInput/>
                            <button type="submit" class="btn btn-danger">Return</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:modal>
