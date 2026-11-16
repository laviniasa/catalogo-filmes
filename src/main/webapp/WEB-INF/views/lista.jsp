<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Catalogo de Filmes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<main class="container">
    <h1>Catalogo de Filmes</h1>

    <%-- Mensagem de sucesso (flash) gravada na sessao pelo Servlet --%>
    <c:if test="${not empty sessionScope.mensagem}">
        <div class="alerta sucesso"><c:out value="${sessionScope.mensagem}"/></div>
        <c:remove var="mensagem" scope="session"/>
    </c:if>

    <div class="barra">
        <form method="get" action="${pageContext.request.contextPath}/filmes" class="busca">
            <input type="search" name="q" placeholder="Buscar por titulo" value="<c:out value='${q}'/>">
            <button type="submit">Buscar</button>
        </form>
        <a class="botao" href="${pageContext.request.contextPath}/filmes?acao=novo">+ Novo filme</a>
    </div>

    <c:choose>
        <c:when test="${empty filmes}">
            <p class="vazio">Nenhum filme encontrado.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr><th>Titulo</th><th>Ano</th><th>Diretor</th><th>Genero</th><th>Duracao</th><th>Nota</th><th>Acoes</th></tr>
                </thead>
                <tbody>
                <c:forEach var="f" items="${filmes}">
                    <tr>
                        <%-- c:out escapa HTML: evita XSS com dados vindos do banco --%>
                        <td><c:out value="${f.titulo}"/></td>
                        <td>${f.ano}</td>
                        <td><c:out value="${f.diretor}"/></td>
                        <td>${f.genero.descricao}</td>
                        <td>${f.duracaoMin} min</td>
                        <td>${empty f.nota ? '-' : f.nota}</td>
                        <td class="acoes">
                            <a href="${pageContext.request.contextPath}/filmes?acao=editar&id=${f.id}">Editar</a>
                            <form method="post" action="${pageContext.request.contextPath}/filmes"
                                  onsubmit="return confirm('Excluir este filme?');">
                                <input type="hidden" name="acao" value="excluir">
                                <input type="hidden" name="id" value="${f.id}">
                                <button type="submit" class="link perigo">Excluir</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <p class="total">${fn:length(filmes)} filme(s)</p>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
