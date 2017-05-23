<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Регистрация</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">


    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>

</head>

<body>

<div class="container">
    <form:form method="POST" modelAttribute="userForm" class="form-signin">
        <h2 class="form-signin-heading">Создание аккаунта</h2>
        
        <spring:bind path="fullName">
            <div class="form-group">
                <form:input type="text" path="fullName" class="form-control" placeholder="Полное имя"
                            autofocus="true"></form:input>
            </div>
        </spring:bind>
        
        <spring:bind path="username">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="username" class="form-control" placeholder="Логин"
                            autofocus="true"></form:input>
                <form:errors path="username"></form:errors>
            </div>
        </spring:bind>

        <spring:bind path="password">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="password" path="password" class="form-control" placeholder="Пароль"></form:input>
                <form:errors path="password"></form:errors>
            </div>
        </spring:bind>

        <spring:bind path="confirmPassword">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="password" path="confirmPassword" class="form-control"
                            placeholder="Подтвердите пароль"></form:input>
                <form:errors path="confirmPassword"></form:errors>
            </div>
        </spring:bind>
        
        <c:if test="${userClass == 'com.tsp.model.Driver'}">
            <spring:bind path="stock">
                <div class="form-group">
                    <form:select path="stock" items="${stocks}" itemLabel="place.address" itemValue="id"/>
                </div>   
            </spring:bind>    
        </c:if>
        
        <c:if test="${userClass == 'com.tsp.model.Client'}">
            <spring:bind path="place">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <form:input type="text" path="place" class="form-control"
                                placeholder="Адрес доставки (город, улица, дом, кв)"></form:input>
                    <form:errors path="place"></form:errors>
                </div>
            </spring:bind>    
            
            <spring:bind path="phone">
                <form:input type="number" path="phone" class="form-control"
                            placeholder="Контактный телефон"></form:input>
            </spring:bind>   
        </c:if>

        <button class="btn btn-lg btn-primary btn-block" type="submit">Создать</button>
    </form:form>

</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>