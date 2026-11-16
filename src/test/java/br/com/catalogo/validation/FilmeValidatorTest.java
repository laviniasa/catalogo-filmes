package br.com.catalogo.validation;

import br.com.catalogo.exception.ValidacaoException;
import br.com.catalogo.model.Filme;
import br.com.catalogo.model.Genero;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Testes unitarios da validacao de entrada (nao precisam de banco). */
class FilmeValidatorTest {

    @Test
    void aceitaFilmeValido() throws ValidacaoException {
        Filme f = FilmeValidator.validar("", " Cidade de Deus ", "Fernando Meirelles", "2002",
                "drama", "130", "9,0", "Sinopse");
        assertNull(f.getId());
        assertEquals("Cidade de Deus", f.getTitulo()); // espacos removidos
        assertEquals(Genero.DRAMA, f.getGenero());
        assertEquals(9.0, f.getNota());
    }

    @Test
    void notaESinopseSaoOpcionais() throws ValidacaoException {
        Filme f = FilmeValidator.validar(null, "Titulo", "Diretor", "2000", "ACAO", "90", "", "  ");
        assertNull(f.getNota());
        assertNull(f.getSinopse());
    }

    @Test
    void acumulaTodosOsErros() {
        ValidacaoException e = assertThrows(ValidacaoException.class, () ->
                FilmeValidator.validar("", "", "", "abc", "XYZ", "-5", "11", null));
        assertEquals(6, e.getErros().size());
        assertTrue(e.getErros().containsKey("titulo"));
        assertTrue(e.getErros().containsKey("diretor"));
        assertTrue(e.getErros().containsKey("ano"));
        assertTrue(e.getErros().containsKey("genero"));
        assertTrue(e.getErros().containsKey("duracao"));
        assertTrue(e.getErros().containsKey("nota"));
    }

    @Test
    void rejeitaAnoAnteriorAoCinema() {
        ValidacaoException e = assertThrows(ValidacaoException.class, () ->
                FilmeValidator.validar("", "T", "D", "1500", "DRAMA", "90", "", ""));
        assertTrue(e.getErros().get("ano").contains("1888"));
    }

    @Test
    void rejeitaTituloMuitoLongo() {
        String longo = "a".repeat(FilmeValidator.TITULO_MAX + 1);
        assertThrows(ValidacaoException.class, () ->
                FilmeValidator.validar("", longo, "D", "2000", "DRAMA", "90", "", ""));
    }

    @Test
    void rejeitaDiretorComCaracteresEstranhos() {
        ValidacaoException e = assertThrows(ValidacaoException.class, () ->
                FilmeValidator.validar("", "T", "<script>alert(1)</script>", "2000", "DRAMA", "90", "", ""));
        assertTrue(e.getErros().containsKey("diretor"));
    }

    @Test
    void rejeitaIdNaoNumerico() {
        ValidacaoException e = assertThrows(ValidacaoException.class, () ->
                FilmeValidator.validar("1 OR 1=1", "T", "D", "2000", "DRAMA", "90", "", ""));
        assertTrue(e.getErros().containsKey("id"));
    }
}
