<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${empty valores.id ? 'Novo filme' : 'Editar filme'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<main class="container">
    <h1>${empty valores.id ? 'Novo filme' : 'Editar filme'}</h1>

    <c:if test="${not empty erros}">
        <div class="alerta erro">Corrija os campos destacados abaixo.</div>
    </c:if>

    <%-- novalidate: desliga a validacao do navegador para demonstrar a validacao no servidor.
         Remova o atributo em producao para ter as duas camadas. --%>
    <form method="post" action="${pageContext.request.contextPath}/filmes" class="formulario" novalidate>
        <input type="hidden" name="acao" value="salvar">
        <input type="hidden" name="id" value="<c:out value='${valores.id}'/>">

        <label class="${not empty erros.titulo ? 'invalido' : ''}">Titulo *
            <input type="text" name="titulo" maxlength="150" required value="<c:out value='${valores.titulo}'/>">
            <span class="msg"><c:out value="${erros.titulo}"/></span>
        </label>

        <label class="${not empty erros.diretor ? 'invalido' : ''}">Diretor *
            <input type="text" name="diretor" maxlength="100" required value="<c:out value='${valores.diretor}'/>">
            <span class="msg"><c:out value="${erros.diretor}"/></span>
        </label>

        <div class="linha">
            <label class="${not empty erros.ano ? 'invalido' : ''}">Ano *
                <input type="text" inputmode="numeric" name="ano" required value="<c:out value='${valores.ano}'/>">
                <span class="msg"><c:out value="${erros.ano}"/></span>
            </label>
            <label class="${not empty erros.duracao ? 'invalido' : ''}">Duracao (min) *
                <input type="text" inputmode="numeric" name="duracao" required value="<c:out value='${valores.duracao}'/>">
                <span class="msg"><c:out value="${erros.duracao}"/></span>
            </label>
            <label class="${not empty erros.nota ? 'invalido' : ''}">Nota (0-10)
                <input type="text" inputmode="decimal" name="nota" value="<c:out value='${valores.nota}'/>">
                <span class="msg"><c:out value="${erros.nota}"/></span>
            </label>
        </div>

        <label class="${not empty erros.genero ? 'invalido' : ''}">Genero *
            <select name="genero" required>
                <option value="">Selecione...</option>
                <c:forEach var="g" items="${generos}">
                    <option value="${g}" ${g.name() == valores.genero ? 'selected' : ''}>${g.descricao}</option>
                </c:forEach>
            </select>
            <span class="msg"><c:out value="${erros.genero}"/></span>
        </label>

        <label class="${not empty erros.sinopse ? 'invalido' : ''}">Sinopse
            <textarea name="sinopse" rows="4" maxlength="1000"><c:out value="${valores.sinopse}"/></textarea>
            <span class="msg"><c:out value="${erros.sinopse}"/></span>
        </label>

        <div class="barra">
            <a href="${pageContext.request.contextPath}/filmes">Cancelar</a>
            <button type="submit" class="botao">Salvar</button>
        </div>
    </form>
</main>
</body>
</html>
