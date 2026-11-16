package br.com.catalogo.exception;

/**
 * Lancada quando se tenta consultar, editar ou excluir um filme cujo id
 * nao existe no banco.
 */
public class ItemNaoEncontradoException extends Exception {

    public ItemNaoEncontradoException(int id) {
        super("Filme com id " + id + " nao encontrado.");
    }
}
