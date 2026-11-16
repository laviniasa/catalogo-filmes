package br.com.catalogo.service;

import br.com.catalogo.dao.FilmeDAO;
import br.com.catalogo.exception.ItemNaoEncontradoException;
import br.com.catalogo.exception.ValidacaoException;
import br.com.catalogo.model.Filme;
import br.com.catalogo.model.Genero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa as regras de negocio com um DAO em memoria (sem banco de dados),
 * mostrando o beneficio de depender da interface FilmeDAO.
 */
class FilmeServiceTest {

    /** DAO falso que guarda os filmes num Map. */
    static class FilmeDAOMemoria implements FilmeDAO {
        private final Map<Integer, Filme> dados = new HashMap<>();
        private int seq = 0;

        public Filme inserir(Filme f) { f.setId(++seq); dados.put(f.getId(), f); return f; }
        public List<Filme> listar(String q) {
            return dados.values().stream()
                    .filter(f -> q == null || f.getTitulo().toLowerCase().contains(q.toLowerCase()))
                    .collect(Collectors.toList());
        }
        public Optional<Filme> buscarPorId(int id) { return Optional.ofNullable(dados.get(id)); }
        public boolean atualizar(Filme f) { return dados.replace(f.getId(), f) != null; }
        public boolean excluir(int id) { return dados.remove(id) != null; }
        public boolean existeTituloAno(String t, int ano, Integer ignorar) {
            return dados.values().stream().anyMatch(f -> f.getTitulo().equalsIgnoreCase(t)
                    && f.getAno() == ano && !Objects.equals(f.getId(), ignorar));
        }
    }

    private FilmeService service;

    @BeforeEach
    void setUp() {
        service = new FilmeService(new FilmeDAOMemoria());
    }

    private Filme filme(String titulo, int ano) {
        Filme f = new Filme();
        f.setTitulo(titulo);
        f.setDiretor("Diretor");
        f.setAno(ano);
        f.setGenero(Genero.DRAMA);
        f.setDuracaoMin(100);
        return f;
    }

    @Test
    void cadastraEListaFilme() throws Exception {
        Filme salvo = service.salvar(filme("Central do Brasil", 1998));
        assertNotNull(salvo.getId());
        assertEquals(1, service.listar(null).size());
    }

    @Test
    void impedeFilmeDuplicado() throws Exception {
        service.salvar(filme("Central do Brasil", 1998));
        ValidacaoException e = assertThrows(ValidacaoException.class,
                () -> service.salvar(filme("CENTRAL DO BRASIL", 1998)));
        assertTrue(e.getErros().containsKey("titulo"));
    }

    @Test
    void permiteEditarSemAcusarDuplicidadeDeSiMesmo() throws Exception {
        Filme f = service.salvar(filme("Central do Brasil", 1998));
        f.setDuracaoMin(113);
        assertDoesNotThrow(() -> service.salvar(f));
        assertEquals(113, service.buscar(f.getId()).getDuracaoMin());
    }

    @Test
    void buscarInexistenteLancaExcecao() {
        assertThrows(ItemNaoEncontradoException.class, () -> service.buscar(99));
    }

    @Test
    void excluirInexistenteLancaExcecao() {
        assertThrows(ItemNaoEncontradoException.class, () -> service.excluir(99));
    }
}
