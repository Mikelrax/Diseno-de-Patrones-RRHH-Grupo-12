package com.rrhh.singleton;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;

public final class GestorConexionBD {

    private static volatile GestorConexionBD instancia;

    private final String url;
    private final String usuario;
    private final String password;
    private final Deque<Connection> pool = new ArrayDeque<>();
    private final int tamanoPool;

    private GestorConexionBD() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        this.url = dotenv.get("DB_URL");
        this.usuario = dotenv.get("DB_USER");
        this.password = dotenv.get("DB_PASSWORD");
        this.tamanoPool = Integer.parseInt(dotenv.get("DB_POOL_SIZE", "5"));
    }

    public static GestorConexionBD obtenerInstancia() {
        if (instancia == null) {
            synchronized (GestorConexionBD.class) {
                if (instancia == null) {
                    instancia = new GestorConexionBD();
                }
            }
        }
        return instancia;
    }

    public synchronized Connection obtenerConexion() throws SQLException {
        Connection conexion = pool.poll();
        while (conexion != null && conexion.isClosed()) {
            conexion = pool.poll();
        }
        if (conexion == null) {
            conexion = DriverManager.getConnection(url, usuario, password);
        }
        return conexion;
    }

    public synchronized void liberarConexion(Connection conexion) {
        if (conexion == null) {
            return;
        }
        if (pool.size() < tamanoPool) {
            pool.offer(conexion);
        } else {
            cerrarSilenciosamente(conexion);
        }
    }

    private void cerrarSilenciosamente(Connection conexion) {
        try {
            conexion.close();
        } catch (SQLException ignored) {
        }
    }
}
