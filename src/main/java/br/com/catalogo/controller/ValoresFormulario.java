package br.com.catalogo.controller;

import br.com.catalogo.model.Filme;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Valores exibidos no formulario, sempre como texto. Permite reexibir
 * exatamente o que o usuario digitou quando a validacao falha (mesmo que
 * seja um valor invalido, como "abc" no campo ano).
 */
public class ValoresFormulario {

    private String id = "", titulo = "", diretor = "", ano = "", genero = "",
            duracao = "", nota = "", sinopse = "";

    public ValoresFormulario() { }

    /** Copia os parametros enviados no POST. */
    public ValoresFormulario(HttpServletRequest req) {
        id = v(req.getParameter("id"));
        titulo = v(req.getParameter("titulo"));
        diretor = v(req.getParameter("diretor"));
        ano = v(req.getParameter("ano"));
        genero = v(req.getParameter("genero"));
        duracao = v(req.getParameter("duracao"));
        nota = v(req.getParameter("nota"));
        sinopse = v(req.getParameter("sinopse"));
    }

    /** Converte um filme existente (edicao) ou vazio (inclusao). */
    public static ValoresFormulario de(Filme f) {
        ValoresFormulario vf = new ValoresFormulario();
        if (f.getId() != null) {
            vf.id = String.valueOf(f.getId());
            vf.titulo = v(f.getTitulo());
            vf.diretor = v(f.getDiretor());
            vf.ano = String.valueOf(f.getAno());
            vf.genero = f.getGenero().name();
            vf.duracao = String.valueOf(f.getDuracaoMin());
            vf.nota = f.getNota() == null ? "" : String.valueOf(f.getNota()).replace('.', ',');
            vf.sinopse = v(f.getSinopse());
        }
        return vf;
    }

    private static String v(String s) { return s == null ? "" : s; }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDiretor() { return diretor; }
    public String getAno() { return ano; }
    public String getGenero() { return genero; }
    public String getDuracao() { return duracao; }
    public String getNota() { return nota; }
    public String getSinopse() { return sinopse; }
}
