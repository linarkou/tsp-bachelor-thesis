<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">

    <title>Пункты производства</title><title>Пункты производства</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
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
                <li><a href="${contextPath}/admin/stocks" class="current">Пункты производства</a></li>
                <li><a href="${contextPath}/admin/drivers">Водители</a></li>
                <!--<li><a href="${contextPath}/admin/clients">Клиенты</a></li>-->
                <li><a href="${contextPath}/admin/orders">Заказы</a></li>
                <li><a href="${contextPath}/admin/createroute">Создать маршрут</a></li>
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
    
        
    <br>
    <script type="text/javascript">
        function add(){
            $.ajax({
                    url: '${contextPath}/admin/stocks/add',
                    type: 'POST',
                    dataType: 'json',
                    headers: {'${_csrf.headerName}' : '${_csrf.token}'},
                    data: {
                        address : $("#address").val()
                    }, 
                    success: function(data){
                        alert(data);
                        $('#stockTable tr:last').after('<tr id="row'+data.id+'"><td>'+data.id+'</td><td>'+data.place.address+'</td><td><a href="#" onclick="remove('+data.id+')">Удалить</a></td></tr>');
                    }
		});

        }
        function remove(id){
            $.ajax({
                    url: '${contextPath}/admin/stocks/'+id,
                    type: 'DELETE',
                    headers: {'${_csrf.headerName}' : '${_csrf.token}'},
                    success: function(data){
                        alert(data);
                        $('#row'+id).remove();
                    }
		});

        }
    </script>
    <form method="post"> 
        <input type="text" id="address" name="address" placeholder="Адрес" size="100" />
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="button" value="Добавить склад" onclick="add()"/> 
        <link href="https://cdn.jsdelivr.net/npm/suggestions-jquery@17.5.0/dist/css/suggestions.min.css" type="text/css" rel="stylesheet" />
        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
        <!--[if lt IE 10]>
        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-ajaxtransport-xdomainrequest/1.0.1/jquery.xdomainrequest.min.js"></script>
        <![endif]-->
        <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/suggestions-jquery@17.5.0/dist/js/jquery.suggestions.min.js"></script>
        <script type="text/javascript">
            $("#address").suggestions({
                token: "49cb3f7920d5667d27b2f00d7721b122f1dd3cf7",
                type: "ADDRESS",
                count: 5,
                /* Вызывается, когда пользователь выбирает одну из подсказок */
                onSelect: function(suggestion) {
                    console.log(suggestion);
                }
            });
        </script>
    </form>

    <h4>Пункты производства</h4>
    <table id="stockTable"  class="to-delete"><tbody>
        <tr>
            <th>№</th>
            <th>Адрес </th>
            <th>Удалить</th>
        </tr>
        <c:forEach var="x" items="${stocks}">
            <tr id="row${x.id}">
                <td>${x.id}</td>
                <td>${x.place.address}</td>
                <td><a href="#" onclick="remove(${x.id})">Удалить</a></td>
            </tr>
        </c:forEach>
    </tbody></table>

</div>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>