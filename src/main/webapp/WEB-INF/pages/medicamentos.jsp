<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<jsp:include page="includes/header.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/medicamentos.css">

<div class="secao-header">
    <h2>${empty medicamento ? 'Cadastrar Medicamento' : 'Editar Medicamento'}</h2>
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

<form method="post" action="${pageContext.request.contextPath}/medicamento"
      class="form-medicamento" accept-charset="UTF-8">
    <input type="hidden" name="id" value="${medicamento.id}">

    <div class="form-grid">

        <div class="form-group">
            <label for="nome">Nome *</label>
            <input type="text" id="nome" name="nome" required maxlength="100"
                   value="${medicamento.nome}">
        </div>

        <div class="form-group">
            <label for="principio_ativo">Princípio Ativo *</label>
            <input type="text" id="principio_ativo" name="principio_ativo" required maxlength="100"
                   value="${medicamento.principioAtivo}">
        </div>

        <div class="form-group">
            <label for="dosagem">Dosagem</label>
            <input type="text" id="dosagem" name="dosagem" maxlength="50"
                   placeholder="ex.: 500mg" value="${medicamento.dosagem}">
        </div>

        <div class="form-group">
            <label for="laboratorio">Laboratório</label>
            <input type="text" id="laboratorio" name="laboratorio" maxlength="100"
                   value="${medicamento.laboratorio}">
        </div>

        <div class="form-group">
            <label for="lote">Lote *</label>
            <input type="text" id="lote" name="lote" required maxlength="50"
                   value="${medicamento.lote}">
        </div>

        <div class="form-group">
            <label for="validade">Validade *</label>
            <input type="date" id="validade" name="validade" required
                   value="${medicamento.validade}">
        </div>

        <div class="form-group">
            <label for="classificacao">Classificação *</label>
            <select id="classificacao" name="classificacao" required>
                <option value="">Selecione...</option>
                <c:forEach var="cl" items="${classificacoes}">
                    <option value="${cl}" ${medicamento.classificacao == cl ? 'selected' : ''}>${cl}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="preco_venda">Preço de Venda (R$) *</label>
            <input type="number" id="preco_venda" name="preco_venda" step="0.01" min="0" required
                   value="${medicamento.precoVenda}">
        </div>

        <div class="form-group">
            <label for="estoque_atual">Estoque Atual</label>
            <input type="number" id="estoque_atual" name="estoque_atual" min="0"
                   value="${medicamento.estoqueAtual}">
        </div>

        <div class="form-group">
            <label for="tipo">Tipo</label>
            <select id="tipo" name="tipo">
                <option value="">Selecione...</option>
                <c:forEach var="t" items="${tipos}">
                    <option value="${t}" ${medicamento.tipo == t ? 'selected' : ''}>${t}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="grupo_farmacologico">Grupo Farmacológico</label>
            <input type="text" id="grupo_farmacologico" name="grupo_farmacologico" maxlength="50"
                   value="${medicamento.grupoFarmacologico}">
        </div>

    </div>

    <div class="form-acoes">
        <button type="submit" class="btn btn-primario">
            ${empty medicamento ? 'Cadastrar' : 'Salvar alterações'}
        </button>
        <c:if test="${not empty medicamento}">
            <a href="${pageContext.request.contextPath}/medicamento" class="btn btn-secundario">Cancelar</a>
        </c:if>
    </div>
</form>

<hr class="separador">

<div class="secao-header">
    <h2>Medicamentos Cadastrados</h2>
</div>

<c:choose>
    <c:when test="${empty lista}">
        <p class="sem-registros">Nenhum medicamento cadastrado.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>Princípio Ativo</th>
                    <th>Dosagem</th>
                    <th>Lote</th>
                    <th>Validade</th>
                    <th>Classif.</th>
                    <th>Tipo</th>
                    <th>Grupo Farmacológico</th>
                    <th>Preço</th>
                    <th>Estoque</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="m" items="${lista}">
                    <tr>
                        <td>${m.nome}</td>
                        <td>${m.principioAtivo}</td>
                        <td>${m.dosagem}</td>
                        <td>${m.lote}</td>
                        <td>
                            <c:if test="${not empty m.validade}">
                                <fmt:parseDate value="${m.validade}" pattern="yyyy-MM-dd" var="dataFmt"/>
                                <fmt:formatDate value="${dataFmt}" pattern="dd/MM/yyyy"/>
                            </c:if>
                        </td>
                        <td>${m.classificacao}</td>
                        <td>${m.tipo}</td>
                        <td>${m.grupoFarmacologico}</td>
                        <td style="white-space: nowrap">
                            <fmt:formatNumber value="${m.precoVenda}" type="currency"
                                              currencySymbol="R$ "/>
                        </td>
                        <td>${m.estoqueAtual}</td>
                        <td class="acoes">
                            <a href="${pageContext.request.contextPath}/medicamento?acao=editar&id=${m.id}"
                               class="btn btn-primario btn-sm">Editar</a>
                            <a href="${pageContext.request.contextPath}/medicamento?acao=excluir&id=${m.id}"
                               class="btn btn-perigo btn-sm"
                               onclick="return confirm('Confirma a exclusão de \'${m.nome}\'?')">Excluir</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<jsp:include page="includes/footer.jsp"/>
