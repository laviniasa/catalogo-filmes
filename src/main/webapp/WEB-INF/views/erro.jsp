<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Ops! Algo deu errado</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<main class="container">
    <h1>Ops! Algo deu errado</h1>
    <%-- Mostra apenas a mensagem amigavel; stack trace fica somente no log --%>
    <div class="alerta erro">
        <c:out value="${empty mensagemErro ? 'Ocorreu um erro inesperado. Tente novamente.' : mensagemErro}"/>
    </div>
    <a class="botao" href="${pageContext.request.contextPath}/filmes">Voltar ao catalogo</a>
</main>
</body>
</html>
