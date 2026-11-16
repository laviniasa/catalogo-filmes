package br.com.catalogo.validation;

import br.com.catalogo.exception.ValidacaoException;
import br.com.catalogo.model.Filme;
import br.com.catalogo.model.Genero;

import java.time.Year;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Converte e valida os dados vindos do formulario (sempre Strings) para um
 * objeto {@link Filme}.
 * <p>
 * Principios aplicados:
 * <ul>
 *   <li>Validacao no servidor: a validacao HTML5 do navegador pode ser
 *       burlada, entao nunca e a unica barreira;</li>
 *   <li>Acumula todos os erros antes de lancar a excecao, para o usuario
 *       corrigir tudo de uma vez;</li>
 *   <li>Conversoes numericas protegidas (NumberFormatException tratada).</li>
 * </ul>
 */
public final class FilmeValidator {

    public static final int ANO_MINIMO = 1888; // primeiro filme da historia
    public static final int TITULO_MAX = 150;
    public static final int DIRETOR_MAX = 100;
    public static final int SINOPSE_MAX = 1000;

    private FilmeValidator() { }

    /**
     * Valida os parametros do formulario e monta o filme.
     *
     * @param id       id (vazio para inclusao)
     * @param titulo   titulo do filme
     * @param diretor  nome do diretor
     * @param ano      ano de lancamento
     * @param genero   nome de uma constante de {@link Genero}
     * @param duracao  duracao em minutos
     * @param nota     nota de 0 a 10 (opcional)
     * @param sinopse  sinopse (opcional)
     * @return filme preenchido e valido
     * @throws ValidacaoException se houver um ou mais campos invalidos
     */
    public static Filme validar(String id, String titulo, String diretor, String ano,
                                String genero, String duracao, String nota, String sinopse)
            throws ValidacaoException {

        Map<String, String> erros = new LinkedHashMap<>();
        Filme f = new Filme();

        // id (opcional; presente apenas na edicao)
        if (!vazio(id)) {
            try {
                f.setId(Integer.parseInt(id.trim()));
            } catch (NumberFormatException e) {
                erros.put("id", "Identificador invalido.");
            }
        }

        // titulo
        String t = limpar(titulo);
        if (t.isEmpty()) {
            erros.put("titulo", "Informe o titulo.");
        } else if (t.length() > TITULO_MAX) {
            erros.put("titulo", "O titulo deve ter no maximo " + TITULO_MAX + " caracteres.");
        }
        f.setTitulo(t);

        // diretor
        String d = limpar(diretor);
        if (d.isEmpty()) {
            erros.put("diretor", "Informe o diretor.");
        } else if (d.length() > DIRETOR_MAX) {
            erros.put("diretor", "O nome do diretor deve ter no maximo " + DIRETOR_MAX + " caracteres.");
        } else if (!d.matches("[\\p{L} .'-]+")) {
            erros.put("diretor", "O nome do diretor deve conter apenas letras.");
        }
        f.setDiretor(d);

        // ano
        int anoAtual = Year.now().getValue();
        try {
            int a = Integer.parseInt(limpar(ano));
            if (a < ANO_MINIMO || a > anoAtual + 5) {
                erros.put("ano", "O ano deve estar entre " + ANO_MINIMO + " e " + (anoAtual + 5) + ".");
            }
            f.setAno(a);
        } catch (NumberFormatException e) {
            erros.put("ano", "O ano deve ser um numero inteiro.");
        }

        // genero
        try {
            f.setGenero(Genero.valueOf(limpar(genero).toUpperCase()));
        } catch (IllegalArgumentException e) {
            erros.put("genero", "Selecione um genero valido.");
        }

        // duracao
        try {
            int min = Integer.parseInt(limpar(duracao));
            if (min < 1 || min > 999) {
                erros.put("duracao", "A duracao deve estar entre 1 e 999 minutos.");
            }
            f.setDuracaoMin(min);
        } catch (NumberFormatException e) {
            erros.put("duracao", "A duracao deve ser um numero inteiro de minutos.");
        }

        // nota (opcional; aceita virgula ou ponto)
        if (!vazio(nota)) {
            try {
                double n = Double.parseDouble(limpar(nota).replace(',', '.'));
                if (n < 0 || n > 10) {
                    erros.put("nota", "A nota deve estar entre 0 e 10.");
                }
                f.setNota(Math.round(n * 10) / 10.0);
            } catch (NumberFormatException e) {
                erros.put("nota", "A nota deve ser numerica (ex.: 8,5).");
            }
        }

        // sinopse (opcional)
        String s = limpar(sinopse);
        if (s.length() > SINOPSE_MAX) {
            erros.put("sinopse", "A sinopse deve ter no maximo " + SINOPSE_MAX + " caracteres.");
        }
        f.setSinopse(s.isEmpty() ? null : s);

        if (!erros.isEmpty()) {
            throw new ValidacaoException(erros);
        }
        return f;
    }

    private static boolean vazio(String v) {
        return v == null || v.isBlank();
    }

    private static String limpar(String v) {
        return v == null ? "" : v.trim();
    }
}
