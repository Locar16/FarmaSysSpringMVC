<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<jsp:include page="includes/header.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/venda.css">

<div class="secao-header">
    <h2>Nova Venda</h2>
</div>

<c:if test="${not empty param.msg}">
    <div class="msg-sucesso">${param.msg}</div>
</c:if>
<c:if test="${not empty erro}">
    <div class="msg-erro">${erro}</div>
</c:if>

<%-- ── Adicionar item ── --%>
<div class="secao-header" style="margin-top:1rem;">
    <h3 style="font-size:1.1rem;color:var(--azul-primario);">Adicionar Item</h3>
</div>

<form method="post" action="${pageContext.request.contextPath}/venda"
      class="form-venda form-adicionar" accept-charset="UTF-8">
    <input type="hidden" name="acao" value="adicionar">

    <div class="form-group">
        <label for="medicamento_busca">Medicamento *</label>
        <input type="text" id="medicamento_busca" list="lista-medicamentos"
               placeholder="Digite o nome..." autocomplete="off" required>
        <datalist id="lista-medicamentos">
            <c:forEach var="m" items="${medicamentos}">
                <option value="${m.nome}<c:if test="${not empty m.dosagem}"> – ${m.dosagem}</c:if>"> Estoque: ${m.estoqueAtual}</option>
            </c:forEach>
        </datalist>
        <input type="hidden" name="medicamento_id" id="medicamento_id_hidden">
    </div>

    <script>
        (function () {
            const medicamentos = [
                <c:forEach var="m" items="${medicamentos}" varStatus="st">
                { id: ${m.id}, label: "<c:out value="${m.nome}"/><c:if test="${not empty m.dosagem}"> – <c:out value="${m.dosagem}"/></c:if>" }<c:if test="${!st.last}">,</c:if>
                </c:forEach>
            ];
            const busca  = document.getElementById('medicamento_busca');
            const hidden = document.getElementById('medicamento_id_hidden');
            busca.addEventListener('input', function () {
                const val = this.value.trim().toLowerCase();
                const match = medicamentos.find(m => m.label.toLowerCase() === val);
                hidden.value = match ? match.id : '';
            });
        })();
    </script>

    <div class="form-group form-group-qtd">
        <label for="quantidade">Quantidade *</label>
        <input type="number" id="quantidade" name="quantidade" min="1" value="1" required>
    </div>

    <div>
        <button type="submit" class="btn btn-primario"
                onclick="if(!document.getElementById('medicamento_id_hidden').value){
                    alert('Selecione um medicamento válido da lista.');return false;}">
            Adicionar
        </button>
    </div>
</form>

<hr class="separador">

<%-- ── Carrinho ── --%>
<div class="secao-header">
    <h3 style="font-size:1.1rem;color:var(--azul-primario);">Carrinho</h3>
</div>

<c:choose>
    <c:when test="${empty carrinho}">
        <p class="sem-registros">Carrinho vazio. Adicione pelo menos um item para continuar.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
                <tr>
                    <th>Medicamento</th>
                    <th>Qtd</th>
                    <th>Preço Unit.</th>
                    <th>Subtotal</th>
                    <th>Remover</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${carrinho}" varStatus="st">
                    <tr>
                        <td>${item.medicamentoNome}</td>
                        <td>${item.quantidade}</td>
                        <td>
                            <fmt:formatNumber value="${item.valorUnitario}" type="currency"
                                              currencySymbol="R$ "/>
                        </td>
                        <td>
                            <fmt:formatNumber value="${item.subtotal}" type="currency"
                                              currencySymbol="R$ "/>
                        </td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/venda"
                                  style="margin:0;" accept-charset="UTF-8">
                                <input type="hidden" name="acao"   value="remover">
                                <input type="hidden" name="indice" value="${st.index}">
                                <button type="submit" class="btn btn-perigo btn-sm">Remover</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <tr class="total-row">
                    <td colspan="4">Total</td>
                    <td>
                        <fmt:formatNumber value="${totalCarrinho}" type="currency"
                                          currencySymbol="R$ "/>
                    </td>
                    <td></td>
                </tr>
            </tbody>
        </table>

        <hr class="separador">

        <%-- ── Finalizar venda ── --%>
        <div class="secao-header">
            <h3 style="font-size:1.1rem;color:var(--azul-primario);">Finalizar Venda</h3>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/venda"
              class="form-finalizar" accept-charset="UTF-8">
            <input type="hidden" name="acao" value="finalizar">

            <div class="form-group">
                <label for="forma_pagamento">Forma de Pagamento *</label>
                <select id="forma_pagamento" name="forma_pagamento" required>
                    <option value="">Selecione...</option>
                    <option value="DINHEIRO">Dinheiro</option>
                    <option value="CARTAO">Cartão</option>
                    <option value="PIX">PIX</option>
                </select>
            </div>

            <div class="form-group">
                <label for="cliente_busca">Cliente</label>
                <input type="text" id="cliente_busca" list="lista-clientes"
                       placeholder="Digite o nome..." autocomplete="off">
                <datalist id="lista-clientes">
                    <c:forEach var="cl" items="${clientes}">
                        <option value="${cl.nome}"> — <c:out value="${cl.cpf}"/></option>
                    </c:forEach>
                </datalist>
                <input type="hidden" name="cliente_id" id="cliente_id_hidden">
            </div>

            <script>
                (function () {
                    const clientes = [
                        <c:forEach var="cl" items="${clientes}" varStatus="st">
                        { id: ${cl.id}, nome: "<c:out value="${cl.nome}"/>" }<c:if test="${!st.last}">,</c:if>
                        </c:forEach>
                    ];
                    const busca  = document.getElementById('cliente_busca');
                    const hidden = document.getElementById('cliente_id_hidden');
                    busca.addEventListener('input', function () {
                        const val = this.value.trim().toLowerCase();
                        const match = clientes.find(c => c.nome.toLowerCase() === val);
                        hidden.value = match ? match.id : '';
                    });
                })();
            </script>

            <div>
                <button type="submit" class="btn btn-primario"
                        onclick="return confirm('Confirmar a venda de R$ ' +
                            document.querySelector('.total-row td:nth-child(2)').innerText + '?')">
                    Finalizar Venda
                </button>
            </div>
        </form>
    </c:otherwise>
</c:choose>

<jsp:include page="includes/footer.jsp"/>
