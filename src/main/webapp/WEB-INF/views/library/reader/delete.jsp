<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%--@elvariable id="title" type="java.lang.String"--%>
<%--@elvariable id="id" type="java.lang.String"--%>
<%--@elvariable id="entity" type="io.lana.libman.core.reader.Reader"--%>
<layout:modal>
    <jsp:attribute name="title">Delete Reader</jsp:attribute>
    <jsp:attribute name="body">
        <div class="modal d-block">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-body d-flex flex-column align-items-center pt-4 pb-3">
                        <i data-feather="alert-triangle" class="txt-warning"
                           style="width: 100px; height: 100px; stroke-width: 1"></i>
                        <h3 class="f-w-600 mt-3">${entity.account.email}</h3>
                        <c:if test="${entity.borrowingBooksCount > 0}">
                            <span>Cannot delete. Reader is borrowing books</span>
                        </c:if>
                        <c:if test="${entity.borrowingBooksCount == 0}">
                            <span>Are you sure delete this reader</span>
                        </c:if>
                    </div>
                    <div class="modal-footer">
                        <a href="${pageContext.request.contextPath}/library/readers"
                           class="btn btn-light" up-dismiss up-follow>Back</a>

                        <c:if test="${entity.borrowingBooksCount == 0}">
                             <form action="${pageContext.request.contextPath}/library/readers/${id}/delete"
                                   method="post" up-layer="parent root" up-scroll="false">
                                 <sec:csrfInput/>
                                 <button type="submit" class="btn btn-danger">Delete</button>
                             </form>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:modal>
