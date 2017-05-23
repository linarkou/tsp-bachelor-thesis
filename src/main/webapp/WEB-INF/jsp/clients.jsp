<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">

    <title>Клиенты</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
</head>
<body>

<div class="container">
    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h4>Добро пожаловать, ${pageContext.request.userPrincipal.name}!</h4>
        <nav class="menu-main">
        <ul>
            <li><a href="${contextPath}/welcome">Добро пожаловать</a></li>
            <c:if test="${pageContext.request.userPrincipal.authorities.toString().contains(\"ROLE_ADMIN\")}">
                <li><a href="${contextPath}/admin/stocks">Склады</a></li>
                <li><a href="${contextPath}/admin/drivers">Водители</a></li>
                <li><a href="${contextPath}/admin/clients" class="current">Клиенты</a></li>
                <li><a href="${contextPath}/admin/orders">Заказы</a></li>
            </c:if>
            <c:if test="${pageContext.request.userPrincipal.authorities.toString().contains(\"ROLE_DRIVER\")}">
                <li><a href="${contextPath}/driver/currentRoute">Текущий маршрут</a></li>
                <li><a href="${contextPath}/driver/routes">Законченные маршруты</a></li>
            </c:if>
            <c:if test="${pageContext.request.userPrincipal.authorities.toString().contains(\"ROLE_CLIENT\")}">
                <li><a href="${contextPath}/client/orders">Мои заказы</a></li>
                <li><a href="${contextPath}/client/addOrder">Добавить заказ</a></li>
            </c:if>  
            <li><a onclick="document.forms['logoutForm'].submit()">Выйти</a></li>
        </ul>
        </nav>
    </c:if>
        
    <h4>Зарегистрированные клиенты: </h4>
    <table class="to-delete"><tbody>
        <tr>
            <th>№</th>
            <th>ФИО</th>
            <th>Адрес</th>
            <th>Количество заказов</th>
            <th>Удалить</th>
        </tr>
        <c:forEach var="x" items="${clients}">
            <tr>
                <td>${x.id}</td>
                <td>${x.fullName}</td>
                <td>${x.place.address}</td>
                <td>${x.orders.size()}</td>
                <td><a href="${contextPath}/admin/clients/remove?id=${x.id}">Удалить</a></td>
            </tr>
        </c:forEach>
    <tbody></table>
        

</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>