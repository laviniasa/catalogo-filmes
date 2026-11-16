package br.com.catalogo.model;

/**
 * Classe base abstrata para qualquer item do catalogo (filme, serie, livro).
 * <p>
 * Concentra os atributos comuns e garante encapsulamento: os campos sao
 * privados e acessados apenas por getters/setters.
 */
public abstract class ItemCatalogo {

    private Integer id;
    private String titulo;
    private int ano;
    private Double nota;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }

    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }

    /**
     * Descricao curta exibida em listagens. Cada subclasse define a sua
     * (polimorfismo).
     *
     * @return texto resumido do item
     */
    public abstract String getResumo();
}
