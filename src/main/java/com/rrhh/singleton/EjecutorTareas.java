package com.rrhh.singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class EjecutorTareas {

    private static volatile EjecutorTareas instancia;

    private final ExecutorService executor;

    private EjecutorTareas() {
        this.executor = Executors.newCachedThreadPool(runnable -> {
            Thread hilo = new Thread(runnable, "rrhh-tarea-bd");
            hilo.setDaemon(true);
            return hilo;
        });
    }

    public static EjecutorTareas obtenerInstancia() {
        if (instancia == null) {
            synchronized (EjecutorTareas.class) {
                if (instancia == null) {
                    instancia = new EjecutorTareas();
                }
            }
        }
        return instancia;
    }

    public void ejecutar(Runnable tarea) {
        executor.execute(tarea);
    }
}
