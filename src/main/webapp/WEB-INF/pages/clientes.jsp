<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<jsp:include page="includes/header.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/clientes.css">

<div class="secao-header">
    <h2>${empty cliente ? 'Cadastrar Cliente' : 'Editar Cliente'}</h2>
</div>

<c:if test="${not empty param.msg}">
    <div class="msg-sucesso">${param.msg}</div>
</c:if>
<c:if test="${not empty param.erro}">
    <div class="msg-erro">${param.erro}</div>
</c:if>
<c:if test="${not empty erro}">
    <div class="msg-erro">${erro}</div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/cliente"
      class="form-cliente" accept-charset="UTF-8">
    <input type="hidden" name="id" value="${cliente.id}">

    <div class="form-grid">

        <div class="form-group">
            <label for="nome">Nome *</label>
            <input type="text" id="nome" name="nome" required maxlength="100"
                   value="${cliente.nome}">
        </div>

        <div class="form-group">
            <label for="cpf">CPF *</label>
            <input type="text" id="cpf" name="cpf" required maxlength="14"
                   placeholder="000.000.000-00" value="${cliente.cpf}">
        </div>

        <div class="form-group">
            <label for="telefone">Telefone</label>
            <input type="text" id="telefone" name="telefone" maxlength="20"
                   placeholder="(00) 00000-0000" value="${cliente.telefone}">
        </div>

        <div class="form-group">
            <label for="email">E-mail</label>
            <input type="email" id="email" name="email" maxlength="100"
                   value="${cliente.email}">
        </div>

        <div class="form-group form-group-full">
            <label for="endereco">Endereço</label>
            <input type="text" id="endereco" name="endereco" maxlength="255"
                   value="${cliente.endereco}">
        </div>

        <div class="form-group">
            <label for="data_nascimento">Data de Nascimento</label>
            <input type="date" id="data_nascimento" name="data_nascimento"
                   value="${cliente.dataNascimento}">
        </div>

    </div>

    <div class="form-acoes">
        <button type="submit" class="btn btn-primario">
            ${empty cliente ? 'Cadastrar' : 'Salvar alterações'}
        </button>
        <c:if test="${not empty cliente}">
            <a href="${pageContext.request.contextPath}/cliente" class="btn btn-secundario">Cancelar</a>
        </c:if>
    </div>
</form>

<hr class="separador">

<div class="secao-header">
    <h2>Clientes Cadastrados</h2>
</div>

<c:choose>
    <c:when test="${empty lista}">
        <p class="sem-registros">Nenhum cliente cadastrado.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>CPF</th>
                    <th>Telefone</th>
                    <th>E-mail</th>
                    <th>Endereço</th>
                    <th>Nascimento</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="c" items="${lista}">
                    <tr>
                        <td><c:out value="${c.nome}"/></td>
                        <td><c:out value="${c.cpf}"/></td>
                        <td><c:out value="${c.telefone}"/></td>
                        <td><c:out value="${c.email}"/></td>
                        <td><c:out value="${c.endereco}"/></td>
                        <td>
                            <c:if test="${not empty c.dataNascimento}">
                                <fmt:parseDate value="${c.dataNascimento}" pattern="yyyy-MM-dd" var="dataFmt"/>
                                <fmt:formatDate value="${dataFmt}" pattern="dd/MM/yyyy"/>
                            </c:if>
                        </td>
                        <td class="acoes">
                            <a href="${pageContext.request.contextPath}/cliente?acao=editar&id=${c.id}"
                               class="btn btn-primario btn-sm">Editar</a>
                            <a href="${pageContext.request.contextPath}/cliente?acao=excluir&id=${c.id}"
                               class="btn btn-perigo btn-sm"
                               onclick="return confirm('Confirma a exclusão de \'${c.nome}\'?')">Excluir</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<jsp:include page="includes/footer.jsp"/>
