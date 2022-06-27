<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%--@elvariable id="title" type="java.lang.String"--%>
<%--@elvariable id="id" type="java.lang.String"--%>
<%--@elvariable id="entity" type="io.lana.libman.core.book.BookBorrow"--%>
<layout:modal>
    <jsp:attribute name="title">Delete Borrow Ticket</jsp:attribute>
    <jsp:attribute name="body">
        <div class="modal d-block">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-body d-flex flex-column align-items-center pt-4 pb-3">
                        <i data-feather="alert-circle" class="txt-primary"
                           style="width: 100px; height: 100px; stroke-width: 1"></i>
                        <c:if test="${not entity.returned}">
                            <h3 class="f-w-600 mt-3">Return Borrow</h3>
                            <span>#${entity.id}</span>
                            <span class="small mt-2">Reader: ${entity.reader.account.username}</span>
                            <span class="small">Book: ${entity.title}</span>
                            <c:if test="${entity.id != entity.ticketId}">
                                <span class="mt-2 small txt-primary">Note: All others book in the same ticket will stay the same</span>
                            </c:if>
                        </c:if>
                        <c:if test="${entity.returned}">
                            <h3 class="f-w-600 mt-3">Already returned</h3>
                        </c:if>
                    </div>
                    <div class="modal-footer">
                        <a href="${pageContext.request.contextPath}/library/borrows"
                           class="btn btn-light" up-dismiss up-follow up-back>Back</a>

                        <c:if test="${not entity.returned}">
                            <form action="${pageContext.request.contextPath}/library/borrows/${id}/return"
                                  method="post" up-layer="root" up-scroll="false">
                                <sec:csrfInput/>
                                <button type="submit" class="btn btn-primary">Return</button>
                            </form>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:modal>
