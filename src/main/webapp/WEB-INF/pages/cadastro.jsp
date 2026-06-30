<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<jsp:include page="includes/header.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/cadastro.css">

<div class="secao-header">
    <h2>Criar conta de Farmacêutico</h2>
</div>

<c:if test="${not empty erro}">
    <div class="msg-erro">${erro}</div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/cadastro"
      class="form-cadastro" accept-charset="UTF-8">

    <div class="form-grid">

        <div class="form-group">
            <label for="usuario">Usuário *</label>
            <input type="text" id="usuario" name="usuario" required maxlength="50"
                   autocomplete="username" value="${farmaceutico.usuario}">
        </div>

        <div class="form-group">
            <label for="nome">Nome *</label>
            <input type="text" id="nome" name="nome" required maxlength="100"
                   value="${farmaceutico.nome}">
        </div>

        <div class="form-group">
            <label for="senha">Senha *</label>
            <input type="password" id="senha" name="senha" required autocomplete="new-password">
        </div>

        <div class="form-group">
            <label for="senha2">Confirmar senha *</label>
            <input type="password" id="senha2" name="senha2" required autocomplete="new-password">
        </div>

        <div class="form-group">
            <label for="cpf">CPF *</label>
            <input type="text" id="cpf" name="cpf" required maxlength="14"
                   placeholder="000.000.000-00" value="${farmaceutico.cpf}">
        </div>

        <div class="form-group">
            <label for="crf_numero">Número do CRF *</label>
            <input type="text" id="crf_numero" name="crf_numero" required maxlength="20"
                   value="${farmaceutico.crfNumero}">
        </div>

        <div class="form-group">
            <label for="crf_uf">UF do CRF *</label>
            <select id="crf_uf" name="crf_uf" required>
                <option value="">Selecione...</option>
                <c:forEach var="uf" items="${ufs}">
                    <option value="${uf}" ${farmaceutico.crfUf == uf ? 'selected' : ''}>${uf}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="email">E-mail</label>
            <input type="email" id="email" name="email" maxlength="100"
                   value="${farmaceutico.email}">
        </div>

        <div class="form-group">
            <label for="telefone">Telefone</label>
            <input type="text" id="telefone" name="telefone" maxlength="20"
                   value="${farmaceutico.telefone}">
        </div>

        <div class="form-group">
            <label for="salario">Salário (R$)</label>
            <input type="number" id="salario" name="salario" step="0.01" min="0"
                   value="${farmaceutico.salario}">
        </div>

        <div class="form-group">
            <label for="data_admissao">Data de Admissão</label>
            <input type="date" id="data_admissao" name="data_admissao"
                   value="${farmaceutico.dataAdmissao}">
        </div>

    </div>

    <div class="form-acoes">
        <button type="submit" class="btn btn-primario">Cadastrar</button>
        <a href="${pageContext.request.contextPath}/" class="btn btn-secundario">Voltar</a>
    </div>
</form>

<jsp:include page="includes/footer.jsp"/>
