package br.com.catalogo.model;

/**
 * Entidade Filme, mapeada para a tabela {@code filme}.
 * Herda os atributos comuns de {@link ItemCatalogo}.
 */
public class Filme extends ItemCatalogo {

    private String diretor;
    private Genero genero;
    private int duracaoMin;
    private String sinopse;

    public String getDiretor() { return diretor; }
    public void setDiretor(String diretor) { this.diretor = diretor; }

    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }

    public int getDuracaoMin() { return duracaoMin; }
    public void setDuracaoMin(int duracaoMin) { this.duracaoMin = duracaoMin; }

    public String getSinopse() { return sinopse; }
    public void setSinopse(String sinopse) { this.sinopse = sinopse; }

    @Override
    public String getResumo() {
        return getTitulo() + " (" + getAno() + ") - " + diretor + ", " + duracaoMin + " min";
    }
}
