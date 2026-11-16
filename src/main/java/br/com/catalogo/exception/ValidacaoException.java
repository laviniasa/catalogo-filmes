package br.com.catalogo.exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Lancada quando os dados informados pelo usuario violam alguma regra.
 * Guarda TODOS os erros encontrados (campo -> mensagem), permitindo que a
 * tela mostre cada mensagem ao lado do campo correspondente.
 * <p>
 * E uma excecao verificada (checked): quem chama e obrigado a trata-la.
 */
public class ValidacaoException extends Exception {

    private final Map<String, String> erros;

    public ValidacaoException(Map<String, String> erros) {
        super("Dados invalidos: " + erros.values());
        this.erros = Collections.unmodifiableMap(new LinkedHashMap<>(erros));
    }

    public Map<String, String> getErros() {
        return erros;
    }
}
