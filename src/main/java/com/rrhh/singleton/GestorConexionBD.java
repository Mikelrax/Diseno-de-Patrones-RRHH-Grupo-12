package com.rrhh.singleton;

import io.github.cdimascio.dotenv.Dotenv;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
        Connection real = pool.poll();
        while (real != null && real.isClosed()) {
            real = pool.poll();
        }
        if (real == null) {
            real = DriverManager.getConnection(url, usuario, password);
        }
        return crearProxy(real);
    }

    /**
     * Envuelve la conexión real en un proxy dinámico: el código cliente sigue usando
     * try-with-resources normalmente, pero close() devuelve la conexión al pool
     * en vez de cerrar el socket.
     */
    private Connection crearProxy(Connection real) {
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[] { Connection.class },
                new ConexionProxyHandler(real));
    }

    private synchronized void liberarConexion(Connection real) {
        if (pool.size() < tamanoPool) {
            pool.offer(real);
        } else {
            cerrarSilenciosamente(real);
        }
    }

    private void cerrarSilenciosamente(Connection conexion) {
        try {
            conexion.close();
        } catch (SQLException ignored) {
        }
    }

    private final class ConexionProxyHandler implements InvocationHandler {
        private final Connection real;
        private boolean cerrada = false;

        private ConexionProxyHandler(Connection real) {
            this.real = real;
        }

        @Override
        public Object invoke(Object proxy, Method metodo, Object[] args) throws Throwable {
            switch (metodo.getName()) {
                case "close":
                    if (!cerrada) {
                        cerrada = true;
                        liberarConexion(real);
                    }
                    return null;
                case "isClosed":
                    return cerrada || real.isClosed();
                default:
                    if (cerrada) {
                        throw new SQLException("La conexión ya fue devuelta al pool");
                    }
                    try {
                        return metodo.invoke(real, args);
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
            }
        }
    }
}
