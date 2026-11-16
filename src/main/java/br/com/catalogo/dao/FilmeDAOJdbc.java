package br.com.catalogo.dao;

import br.com.catalogo.exception.PersistenciaException;
import br.com.catalogo.model.Filme;
import br.com.catalogo.model.Genero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementacao JDBC do {@link FilmeDAO}.
 * <ul>
 *   <li>Usa somente {@link PreparedStatement} (protege contra SQL Injection);</li>
 *   <li>Usa try-with-resources (conexoes sempre fechadas, mesmo com erro);</li>
 *   <li>Converte {@link SQLException} em {@link PersistenciaException}.</li>
 * </ul>
 */
public class FilmeDAOJdbc implements FilmeDAO {

    private static final String COLUNAS =
            "id, titulo, diretor, ano, genero, duracao_min, nota, sinopse";

    @Override
    public Filme inserir(Filme f) {
        String sql = "INSERT INTO filme (titulo, diretor, ano, genero, duracao_min, nota, sinopse) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencher(ps, f);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    f.setId(rs.getInt(1));
                }
            }
            return f;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao cadastrar o filme.", e);
        }
    }

    @Override
    public List<Filme> listar(String filtroTitulo) {
        boolean filtrar = filtroTitulo != null && !filtroTitulo.isBlank();
        String sql = "SELECT " + COLUNAS + " FROM filme"
                // LOWER dos dois lados: busca sem diferenciar maiusculas em qualquer banco
                + (filtrar ? " WHERE LOWER(titulo) LIKE LOWER(?)" : "")
                + " ORDER BY titulo";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (filtrar) {
                ps.setString(1, "%" + filtroTitulo.trim() + "%");
            }
            List<Filme> lista = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar os filmes.", e);
        }
    }

    @Override
    public Optional<Filme> buscarPorId(int id) {
        String sql = "SELECT " + COLUNAS + " FROM filme WHERE id = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar o filme.", e);
        }
    }

    @Override
    public boolean atualizar(Filme f) {
        String sql = "UPDATE filme SET titulo = ?, diretor = ?, ano = ?, genero = ?, "
                + "duracao_min = ?, nota = ?, sinopse = ? WHERE id = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, f);
            ps.setInt(8, f.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar o filme.", e);
        }
    }

    @Override
    public boolean excluir(int id) {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM filme WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao excluir o filme.", e);
        }
    }

    @Override
    public boolean existeTituloAno(String titulo, int ano, Integer ignorarId) {
        String sql = "SELECT COUNT(*) FROM filme WHERE LOWER(titulo) = LOWER(?) AND ano = ?"
                + (ignorarId != null ? " AND id <> ?" : "");
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, titulo);
            ps.setInt(2, ano);
            if (ignorarId != null) {
                ps.setInt(3, ignorarId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao verificar duplicidade.", e);
        }
    }

    /** Preenche os 7 primeiros parametros comuns a INSERT e UPDATE. */
    private void preencher(PreparedStatement ps, Filme f) throws SQLException {
        ps.setString(1, f.getTitulo());
        ps.setString(2, f.getDiretor());
        ps.setInt(3, f.getAno());
        ps.setString(4, f.getGenero().name());
        ps.setInt(5, f.getDuracaoMin());
        if (f.getNota() == null) {
            ps.setNull(6, Types.DECIMAL);
        } else {
            ps.setDouble(6, f.getNota());
        }
        ps.setString(7, f.getSinopse());
    }

    /** Converte a linha atual do ResultSet em um objeto Filme. */
    private Filme mapear(ResultSet rs) throws SQLException {
        Filme f = new Filme();
        f.setId(rs.getInt("id"));
        f.setTitulo(rs.getString("titulo"));
        f.setDiretor(rs.getString("diretor"));
        f.setAno(rs.getInt("ano"));
        f.setGenero(Genero.valueOf(rs.getString("genero")));
        f.setDuracaoMin(rs.getInt("duracao_min"));
        double nota = rs.getDouble("nota");
        f.setNota(rs.wasNull() ? null : nota);
        f.setSinopse(rs.getString("sinopse"));
        return f;
    }
}
