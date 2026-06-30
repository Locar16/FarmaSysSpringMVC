<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FarmaSys</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
</head>
<body>
<header class="site-header">
    <div class="header-inner">
        <span class="brand">FarmaSys</span>
        <c:if test="${not empty sessionScope.farmaceutico}">
            <nav>
                <ul class="nav-list">
                    <li><a href="${pageContext.request.contextPath}/dashboard">Início</a></li>
                    <li><a href="${pageContext.request.contextPath}/venda">Nova Venda</a></li>
                </ul>
            </nav>
            <div class="user-area">
                <span class="user-nome">${sessionScope.farmaceutico.nome}</span>
                <span class="user-separador">|</span>
                <button type="button" class="user-btn"
                        onclick="location.href='${pageContext.request.contextPath}/farmaceutico?acao=editar&id=${sessionScope.farmaceutico.id}'">Editar Perfil</button>
                <button type="button" class="user-btn user-btn-sair"
                        onclick="location.href='${pageContext.request.contextPath}/logout'">Sair</button>
            </div>
        </c:if>
    </div>
</header>
<main class="site-main">
