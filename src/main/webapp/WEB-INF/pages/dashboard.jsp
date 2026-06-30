<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<jsp:include page="includes/header.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">

<div class="dashboard-boas-vindas">
    <h2>Bem-vindo(a), <c:out value="${sessionScope.farmaceutico.nome}"/>!</h2>
    <p>Você está no sistema FarmaSys. Use o menu ou os atalhos abaixo para navegar.</p>
</div>

<div class="dashboard-atalhos">
    <a href="${pageContext.request.contextPath}/medicamento" class="atalho">
        <span class="atalho-icone">
            <img src="${pageContext.request.contextPath}/img/pill.png" alt="Medicamento">
        </span>
        <span class="atalho-label">Medicamentos</span>
    </a>
    <a href="${pageContext.request.contextPath}/cliente" class="atalho">
        <span class="atalho-icone">
             <img src="${pageContext.request.contextPath}/img/users.png" alt="Clientes">
        </span>
        <span class="atalho-label">Clientes</span>
    </a>
    <a href="${pageContext.request.contextPath}/farmaceutico" class="atalho">
        <span class="atalho-icone">
             <img src="${pageContext.request.contextPath}/img/person.png" alt="Farmacêutico">
        </span>
        <span class="atalho-label">Farmacêuticos</span>
    </a>
</div>

<div class="secao-header" style="margin-top: 2.5rem;">
    <h2>Histórico de Vendas</h2>
</div>

<c:choose>
    <c:when test="${empty vendasRealizadas}">
        <p class="sem-registros">Nenhuma venda registrada.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
                <tr>
                    <th>#</th>
                    <th>Data/Hora</th>
                    <th>Farmacêutico</th>
                    <th>Cliente</th>
                    <th>Forma de Pagamento</th>
                    <th>Itens</th>
                    <th>Total</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="v" items="${vendasRealizadas}">
                    <tr>
                        <td>${v.id}</td>
                        <td>${fn:replace(fn:substring(v.dataHora.toString(), 0, 16), 'T', ' ')}</td>
                        <td><c:out value="${v.farmaceuticoNome}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty v.clienteNome}"><c:out value="${v.clienteNome}"/></c:when>
                                <c:otherwise>—</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${v.formaPagamento == 'DINHEIRO'}">Dinheiro</c:when>
                                <c:when test="${v.formaPagamento == 'CARTAO'}">Cartão</c:when>
                                <c:when test="${v.formaPagamento == 'PIX'}">PIX</c:when>
                                <c:otherwise><c:out value="${v.formaPagamento}"/></c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:forEach var="item" items="${v.itens}">
                                <div><c:out value="${item.medicamentoNome}"/> &times;${item.quantidade}
                                    (<fmt:formatNumber value="${item.valorUnitario}" type="currency" currencySymbol="R$ "/>)
                                </div>
                            </c:forEach>
                        </td>
                        <td>
                            <fmt:formatNumber value="${v.valorTotal}" type="currency" currencySymbol="R$ "/>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<jsp:include page="includes/footer.jsp"/>
