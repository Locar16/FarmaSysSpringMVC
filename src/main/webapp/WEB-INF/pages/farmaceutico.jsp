<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<jsp:include page="includes/header.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/farmaceutico.css">

<div class="secao-header">
    <h2>Farmacêuticos</h2>
</div>

<c:if test="${not empty param.msg}">
    <div class="msg-sucesso">
        <c:choose>
            <c:when test="${param.msg == 'editado'}">Dados atualizados com sucesso.</c:when>
            <c:otherwise>${param.msg}</c:otherwise>
        </c:choose>
    </div>
</c:if>

<%-- Formulário de edição — visível somente quando acao=editar e é o próprio farmacêutico --%>
<c:if test="${not empty farmaceuticoEdicao}">
    <div class="secao-header">
        <h3>Editar meu cadastro</h3>
    </div>

    <c:if test="${not empty erroSenha}">
        <div class="msg-erro">${erroSenha}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/farmaceutico"
          class="form-farmaceutico" accept-charset="UTF-8">
        <input type="hidden" name="id" value="${farmaceuticoEdicao.id}">

        <div class="form-grid">

            <div class="form-group">
                <label for="usuario">Usuário *</label>
                <input type="text" id="usuario" name="usuario" required maxlength="50"
                       autocomplete="username" value="${farmaceuticoEdicao.usuario}">
            </div>

            <div class="form-group">
                <label for="nome">Nome *</label>
                <input type="text" id="nome" name="nome" required maxlength="100"
                       value="${farmaceuticoEdicao.nome}">
            </div>

            <div class="form-group">
                <label for="cpf">CPF *</label>
                <input type="text" id="cpf" name="cpf" required maxlength="14"
                       placeholder="000.000.000-00" value="${farmaceuticoEdicao.cpf}">
            </div>

            <div class="form-group">
                <label for="crf_numero">Número do CRF *</label>
                <input type="text" id="crf_numero" name="crf_numero" required maxlength="20"
                       value="${farmaceuticoEdicao.crfNumero}">
            </div>

            <div class="form-group">
                <label for="crf_uf">UF do CRF *</label>
                <select id="crf_uf" name="crf_uf" required>
                    <option value="">Selecione...</option>
                    <c:forEach var="uf" items="${ufs}">
                        <option value="${uf}" ${farmaceuticoEdicao.crfUf == uf ? 'selected' : ''}>${uf}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="email">E-mail</label>
                <input type="email" id="email" name="email" maxlength="100"
                       value="${farmaceuticoEdicao.email}">
            </div>

            <div class="form-group">
                <label for="telefone">Telefone</label>
                <input type="text" id="telefone" name="telefone" maxlength="20"
                       value="${farmaceuticoEdicao.telefone}">
            </div>

            <div class="form-group">
                <label for="salario">Salário (R$)</label>
                <input type="number" id="salario" name="salario" step="0.01" min="0"
                       value="${farmaceuticoEdicao.salario}">
            </div>

            <div class="form-group">
                <label for="data_admissao">Data de Admissão</label>
                <input type="date" id="data_admissao" name="data_admissao"
                       value="${farmaceuticoEdicao.dataAdmissao}">
            </div>

            <div class="form-group">
                <label for="senhaAtual">Senha atual *</label>
                <input type="password" id="senhaAtual" name="senhaAtual" required
                       autocomplete="current-password">
            </div>

            <div class="form-group">
                <label for="novaSenha">Nova senha <span class="hint">(deixe em branco para manter)</span></label>
                <input type="password" id="novaSenha" name="novaSenha"
                       autocomplete="new-password">
            </div>

            <div class="form-group">
                <label for="novaSenha2">Confirmar nova senha</label>
                <input type="password" id="novaSenha2" name="novaSenha2"
                       autocomplete="new-password">
            </div>

        </div>

        <div class="form-acoes">
            <button type="submit" class="btn btn-primario">Salvar alterações</button>
            <a href="${pageContext.request.contextPath}/farmaceutico" class="btn btn-secundario">Cancelar</a>
        </div>
    </form>

    <div class="form-desativar">
        <form method="post" action="${pageContext.request.contextPath}/farmaceutico" style="margin:0">
            <input type="hidden" name="acao" value="desativarConta">
            <input type="hidden" name="id" value="${farmaceuticoEdicao.id}">
            <button type="submit" class="btn btn-perigo"
                    onclick="return confirm('Tem certeza que deseja desativar sua conta? Você será desconectado e não conseguirá mais acessar o sistema com ela.')">
                Desativar conta
            </button>
        </form>
    </div>

</c:if>

<c:if test="${empty farmaceuticoEdicao}">
<div class="secao-header">
    <h2>Lista de Farmacêuticos</h2>
</div>

<c:choose>
    <c:when test="${empty lista}">
        <p class="sem-registros">Nenhum farmacêutico cadastrado.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>Usuário</th>
                    <th>CRF</th>
                    <th>CPF</th>
                    <th>E-mail</th>
                    <th>Telefone</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="f" items="${lista}">
                    <tr>
                        <td><c:out value="${f.nome}"/></td>
                        <td><c:out value="${f.usuario}"/></td>
                        <td><c:out value="${f.crfNumero}"/>/<c:out value="${f.crfUf}"/></td>
                        <td><c:out value="${f.cpf}"/></td>
                        <td><c:out value="${f.email}"/></td>
                        <td><c:out value="${f.telefone}"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>
</c:if>

<jsp:include page="includes/footer.jsp"/>
