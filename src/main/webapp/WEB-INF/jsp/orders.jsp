<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">

    <title>Заказы</title>

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
                <li><a href="${contextPath}/admin/clients">Клиенты</a></li>
                <li><a href="${contextPath}/admin/orders" class="current">Заказы</a></li>
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
        
    <h4>Добавить заказ</h4>
    <form method="post" action="${contextPath}/admin/orders/add"> 
        <input name="address" placeholder="Полный адрес (город, улица, дом)" size="40"/><br/>
        Дата доставки заказа: <input type="date" name="date" placeholder="Дата доставки заказа" value="${today}" min="${today}" max="${today.plusDays(30)}"/><br/>
        <textarea name="desc" placeholder="Описание заказа" cols="39" rows="3" ></textarea>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><br/>
        <input type="submit" value="Добавить заказ"/> 
    </form>
    
    <h4>Заказы ${username}:</h4>
    <table class="to-delete"><tbody>
        <tr>
            <th>№</th>
            <th>Дата</th>
            <th>Адрес</th>
            <th>Статус</th>
            <th>Описание</th>
            <th>Отменить заказ</th>
        </tr>
        <c:forEach var="x" items="${orders}">
            <tr>
                <td>${x.id}</td>
                <td>${x.date}</td>
                <td>${x.place.address}</td>
                <td>${x.status}</td>
                <td>${x.description}</td>
                <td>
                    <c:choose>
                        <c:when test="${!x.status.equals(\"Отменен\")}">
                            <a href="${contextPath}/admin/orders/cancel?id=${x.id}">Отменить</a>
                        </c:when>
                        <c:otherwise>
                            -
                        </c:otherwise>
                    </c:choose>  
                </td>
            </tr>
        </c:forEach>
    <tbody></table>
        

</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>