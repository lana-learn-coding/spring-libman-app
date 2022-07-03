<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>


<%--@elvariable id="user" type="io.lana.libman.core.user.User"--%>
<%--@elvariable id="password" type="io.lana.libman.core.user.dto.ChangePasswordDto"--%>
<%--@elvariable id="profile" type="io.lana.libman.core.user.dto.ProfileUpdateDto"--%>

<layout:librarian>
    <jsp:attribute name="title">Edit Profile</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>Edit Profile</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/dashboard"
                                   up-follow up-instant>Home</a>
                            </li>
                            <li class="breadcrumb-item">Users</li>
                            <li class="breadcrumb-item active">Edit Profile</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
       <div class="container-fluid">
           <div class="edit-profile">
               <div class="row">
                   <div class="col-xl-4">
                       <div class="card">
                           <div class="card-header pb-0">
                               <h4 class="card-title mb-0">My Profile</h4>
                           </div>
                           <div class="card-body">
                               <div class="row mb-2">
                                   <div class="profile-title">
                                       <div class="media">
                                           <sec:authentication property="principal.avatar" var="avatar"/>
                                           <img class="img-70 rounded-circle"
                                                src="${pageContext.request.contextPath}${(empty avatar ? '/static/images/avatar/default.png' : avatar)}"
                                                alt="avatar">
                                           <div class="media-body">
                                               <h3 class="mb-1 f-20 txt-primary">
                                                   <sec:authentication property="principal.lastName" var="lastName"/>
                                                   <sec:authentication property="principal.firstName" var="firstName"/>
                                                   <span>${empty firstName ? 'Anon' : firstName}</span>
                                                   <span>${empty lastName ? '' : lastName}</span>
                                               </h3>
                                               <p class="f-12">
                                                   <sec:authorize access="hasAnyAuthority('ADMIN')">
                                                       ADMIN
                                                   </sec:authorize>
                                                   <sec:authorize
                                                           access="!hasAnyAuthority('ADMIN') && hasAnyAuthority('LIBRARIAN')">
                                                           LIBRARIAN
                                                   </sec:authorize>
                                                   <sec:authorize
                                                           access="!hasAnyAuthority('ADMIN') && !hasAnyAuthority('LIBRARIAN')">
                                                           READER
                                                   </sec:authorize>
                                               </p>
                                           </div>
                                       </div>
                                   </div>
                               </div>
                               <div class="mt-2 mt-sm-0">
                                   <div class="mb-1">Username
                                       <span class="ms-2 txt-primary">${user.username}</span>
                                   </div>
                                   <div class="mb-1">Email
                                       <span class="ms-2 txt-primary">${user.email}</span>
                                   </div>
                                   <c:if test="${not empty user.phone}">
                                        <div class="mb-1">Phone
                                            <span class="ms-2 txt-primary">${user.phone}</span>
                                        </div>
                                    </c:if>
                                   <div class="mb-1">Name
                                       <div class="ms-2 txt-primary d-inline">
                                            <c:if test="${not empty user.firstName}">
                                                <span>${user.firstName} </span>
                                            </c:if>
                                           <c:if test="${not empty user.firstName}">
                                                <span>${user.lastName}</span>
                                            </c:if>
                                       </div>
                                   </div>
                                   <c:if test="${not empty user.gender}">
                                        <div class="mb-1">Gender
                                            <span class="ms-2 txt-primary">${user.gender.name().toLowerCase()}</span>
                                        </div>
                                    </c:if>
                                   <c:if test="${not empty user.dateOfBirth}">
                                        <div class="mb-1">Birthdate
                                            <span class="ms-2 txt-primary">
                                                <helper:format-date date="${user.dateOfBirth}"/>
                                            </span>
                                        </div>
                                    </c:if>
                               </div>
                           </div>
                       </div>
                       <div class="card">
                           <div class="card-header pb-0">
                               <h4 class="card-title mb-0">Change Password</h4>
                           </div>
                           <div class="card-body">
                               <form:form action="/me/change-password" method="post"
                                          up-submit="true" modelAttribute="password">
                                   <sec:csrfInput/>
                                   <div class="mb-3">
                                       <label class="form-label">Current Password</label>
                                       <form:input path="oldPassword" cssClass="form-control"
                                                   cssErrorClass="form-control is-invalid"
                                                   type="password" placeholder="Old password"/>
                                       <form:errors path="oldPassword" cssClass="invalid-feedback"/>
                                   </div>
                                   <div class="mb-3">
                                       <label class="form-label">New Password</label>
                                       <form:input path="newPassword" cssClass="form-control"
                                                   cssErrorClass="form-control is-invalid"
                                                   type="password" placeholder="New password"/>
                                       <form:errors path="newPassword" cssClass="invalid-feedback"/>
                                   </div>
                                   <div class="mb-3">
                                       <label class="form-label">Retype Password</label>
                                       <form:input path="retypeNewPassword" cssClass="form-control"
                                                   cssErrorClass="form-control is-invalid"
                                                   type="password" placeholder="Retype new password"/>
                                       <form:errors path="retypeNewPassword" cssClass="invalid-feedback"/>
                                   </div>
                                   <div class="form-footer">
                                       <button class="btn btn-primary btn-block" type="submit">Change</button>
                                   </div>
                               </form:form>
                           </div>
                       </div>
                   </div>
                   <div class="col-xl-8">
                       <div class="card">
                           <div class="card-header pb-0">
                               <h4 class="card-title mb-0">Edit Profile</h4>
                               <div class="card-options"><a class="card-options-collapse" href="#"
                                                            data-bs-toggle="card-collapse"><i
                                       class="fe fe-chevron-up"></i></a><a class="card-options-remove" href="#"
                                                                           data-bs-toggle="card-remove"><i
                                       class="fe fe-x"></i></a></div>
                           </div>
                           <div class="card-body">
                               <form:form action="/me/update-profile" method="post"
                                          up-submit="true" modelAttribute="profile">
                                   <input type="hidden" name="id" value="${user.id}">
                                   <div class="row">
                                       <div class="col-sm-6">
                                           <div class="mb-3">
                                               <label class="form-label">Username</label>
                                               <input class="form-control" type="text" placeholder="Username"
                                                      value="${user.username}" readonly disabled>
                                           </div>
                                       </div>
                                       <div class="col-sm-6">
                                           <div class="mb-3">
                                               <label class="form-label">Email address</label>
                                               <input class="form-control" type="email" placeholder="Email"
                                                      value="${user.email}" readonly disabled>
                                           </div>
                                       </div>
                                       <div class="col-12 col-sm-6">
                                           <div class="mb-3">
                                               <label class="form-label" for="firstName">First Name</label>
                                               <form:input path="firstName"
                                                           cssErrorClass="form-control is-invalid"
                                                           cssClass="form-control"
                                                           type="text" placeholder="First Name"
                                               />
                                               <form:errors path="firstName" cssClass="invalid-feedback"/>
                                           </div>
                                       </div>
                                       <div class="col-12 col-sm-6">
                                           <div class="mb-3">
                                               <label class="form-label" for="lastName">Last Name</label>
                                               <form:input path="lastName"
                                                           cssErrorClass="form-control is-invalid"
                                                           cssClass="form-control"
                                                           type="text" placeholder="Last Name"
                                               />
                                               <form:errors path="lastName" cssClass="invalid-feedback"/>
                                           </div>
                                       </div>
                                       <div class="col-12 col-sm-6">
                                           <div class="mb-3">
                                               <helper:format-date date="${profile.dateOfBirth}" var="birth"/>
                                               <label class="col-form-label pt-0">Date of Birth</label>
                                               <form:input path="dateOfBirth"
                                                           cssClass="datepicker-here form-control digits"
                                                           cssErrorClass="datepicker-here form-control digits is-invalid"
                                                           placeholder="Select birthdate" type="text"
                                                           value="${birth}" data-start-date="${birth}"
                                                           readonly="true"/>
                                               <form:errors path="dateOfBirth" cssClass="invalid-feedback"/>
                                           </div>
                                       </div>
                                       <div class="col-12 col-sm-6">
                                           <div class="mb-3">
                                               <label class="col-form-label pt-0" for="firstName">Gender</label>
                                               <form:select path="gender" cssClass="form-select"
                                                            cssErrorClass="form-select is-invalid" required="required">
                                                    <form:options items="${Gender.values()}"/>
                                               </form:select>
                                               <form:errors path="gender" cssClass="invalid-feedback"/>
                                           </div>
                                       </div>
                                       <div class="col-12 col-md-10 col-lg-9">
                                           <div class="mb-3">
                                               <label class="form-label" for="phone">Phone</label>
                                               <form:input path="phone"
                                                           cssErrorClass="form-control is-invalid"
                                                           cssClass="form-control"
                                                           type="phone" placeholder="Phone"
                                               />
                                               <form:errors path="phone" cssClass="invalid-feedback"/>
                                           </div>
                                       </div>
                                       <div class="col-md-12">
                                           <div>
                                               <label class="form-label">Address</label>
                                               <form:textarea path="address"
                                                              cssClass="form-control" rows="3"
                                                              placeholder="Enter your address"/>
                                           </div>
                                       </div>
                                   </div>
                                   <div class="form-footer mt-3">
                                       <button class="btn btn-primary btn-block" type="submit">Update Profile</button>
                                   </div>
                               </form:form>
                           </div>
                       </div>
                   </div>
               </div>
           </div>
       </div>
    </jsp:attribute>
</layout:librarian>

