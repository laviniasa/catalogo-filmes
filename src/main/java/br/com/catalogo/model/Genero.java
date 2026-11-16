package br.com.catalogo.model;

/**
 * Generos aceitos pelo catalogo. Usar um enum evita que valores livres
 * (e invalidos) cheguem ao banco de dados.
 */
public enum Genero {
    ACAO("Acao"),
    ANIMACAO("Animacao"),
    COMEDIA("Comedia"),
    DOCUMENTARIO("Documentario"),
    DRAMA("Drama"),
    FICCAO("Ficcao cientifica"),
    ROMANCE("Romance"),
    SUSPENSE("Suspense"),
    TERROR("Terror");

    private final String descricao;

    Genero(String descricao) { this.descricao = descricao; }

    public String getDescricao() { return descricao; }
}
