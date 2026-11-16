package br.com.catalogo.dao;

import br.com.catalogo.model.Filme;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de acesso a dados de {@link Filme} (operacoes CRUD).
 * Programar para a interface permite trocar a implementacao (JDBC, memoria
 * para testes etc.) sem alterar o restante do sistema.
 */
public interface FilmeDAO {

    Filme inserir(Filme filme);

    List<Filme> listar(String filtroTitulo);

    Optional<Filme> buscarPorId(int id);

    boolean atualizar(Filme filme);

    boolean excluir(int id);

    boolean existeTituloAno(String titulo, int ano, Integer ignorarId);
}
