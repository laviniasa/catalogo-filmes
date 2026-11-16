package br.com.catalogo.exception;

/**
 * Encapsula falhas de acesso a dados (SQLException, falha de conexao etc.).
 * <p>
 * Evita que detalhes tecnicos do JDBC "vazem" para as camadas superiores e,
 * principalmente, para a tela do usuario. A causa original e preservada
 * para fins de log.
 */
public class PersistenciaException extends RuntimeException {

    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
