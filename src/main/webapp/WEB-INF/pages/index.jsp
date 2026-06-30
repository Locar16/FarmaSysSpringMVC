<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FarmaSys — Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
    <div class="login-card">
        <div class="logo">FarmaSys</div>
        <div class="subtitulo">Sistema de Gestão de Farmácia</div>

        <c:choose>
            <c:when test="${param.msg == 'cadastrado'}">
                <div class="msg-sucesso">Conta criada com sucesso! Faça login para continuar.</div>
            </c:when>
            <c:when test="${not empty param.msg}">
                <div class="msg-erro">${param.msg}</div>
            </c:when>
        </c:choose>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="form-group">
                <label for="usuario">Usuário</label>
                <input type="text" id="usuario" name="usuario" required maxlength="50" autocomplete="username">
            </div>
            <div class="form-group">
                <label for="senha">Senha</label>
                <input type="password" id="senha" name="senha" required>
            </div>
            <button type="submit" class="btn btn-primario">Entrar</button>
        </form>

        <p class="cadastro-link">
            Novo por aqui?
            <a href="${pageContext.request.contextPath}/cadastro">Cadastre-se aqui</a>
        </p>
    </div>
</body>
</html>
