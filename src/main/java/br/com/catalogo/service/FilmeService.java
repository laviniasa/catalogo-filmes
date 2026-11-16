package br.com.catalogo.service;

import br.com.catalogo.dao.FilmeDAO;
import br.com.catalogo.exception.ItemNaoEncontradoException;
import br.com.catalogo.exception.ValidacaoException;
import br.com.catalogo.model.Filme;

import java.util.List;
import java.util.Map;

/**
 * Camada de regras de negocio. Fica entre o controlador (Servlet) e o DAO,
 * aplicando regras que dependem do banco (ex.: nao permitir o mesmo filme
 * duas vezes) e traduzindo "nao encontrado" em excecao propria.
 */
public class FilmeService {

    private final FilmeDAO dao;

    /** Recebe o DAO por injecao (facilita testes com um DAO falso). */
    public FilmeService(FilmeDAO dao) {
        this.dao = dao;
    }

    public List<Filme> listar(String filtro) {
        return dao.listar(filtro);
    }

    public Filme buscar(int id) throws ItemNaoEncontradoException {
        return dao.buscarPorId(id).orElseThrow(() -> new ItemNaoEncontradoException(id));
    }

    /**
     * Inclui (id nulo) ou atualiza (id preenchido) um filme ja validado.
     *
     * @throws ValidacaoException         se ja existir filme com mesmo titulo e ano
     * @throws ItemNaoEncontradoException se o filme a atualizar nao existir mais
     */
    public Filme salvar(Filme f) throws ValidacaoException, ItemNaoEncontradoException {
        if (dao.existeTituloAno(f.getTitulo(), f.getAno(), f.getId())) {
            throw new ValidacaoException(Map.of("titulo",
                    "Ja existe um filme com este titulo e ano no catalogo."));
        }
        if (f.getId() == null) {
            return dao.inserir(f);
        }
        if (!dao.atualizar(f)) {
            throw new ItemNaoEncontradoException(f.getId());
        }
        return f;
    }

    public void excluir(int id) throws ItemNaoEncontradoException {
        if (!dao.excluir(id)) {
            throw new ItemNaoEncontradoException(id);
        }
    }
}
