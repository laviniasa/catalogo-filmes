package br.com.catalogo.dao;

import br.com.catalogo.exception.PersistenciaException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Fabrica de conexoes JDBC. Le URL, usuario e senha do arquivo
 * {@code db.properties} (fora do codigo) para nao deixar credenciais
 * "chumbadas" nas classes.
 */
public final class ConnectionFactory {

    private static final Properties CONFIG = new Properties();

    static {
        try (InputStream in = ConnectionFactory.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("Arquivo db.properties nao encontrado no classpath.");
            }
            CONFIG.load(in);
            Class.forName(CONFIG.getProperty("db.driver", "com.mysql.cj.jdbc.Driver"));
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConnectionFactory() { }

    /**
     * Abre uma nova conexao. Deve ser fechada por quem chamou
     * (preferencialmente com try-with-resources).
     *
     * @return conexao aberta
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    CONFIG.getProperty("db.url"),
                    CONFIG.getProperty("db.user"),
                    CONFIG.getProperty("db.password"));
        } catch (SQLException e) {
            throw new PersistenciaException("Nao foi possivel conectar ao banco de dados.", e);
        }
    }
}
