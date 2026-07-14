package com.rrhh.util;

import com.rrhh.singleton.EjecutorTareas;
import javafx.concurrent.Task;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public final class TareasFX {

    private TareasFX() {
    }

    public static <T> void ejecutar(Callable<T> trabajo, Consumer<T> alExito, Consumer<Throwable> alFallo) {
        Task<T> tarea = new Task<>() {
            @Override
            protected T call() throws Exception {
                return trabajo.call();
            }
        };
        tarea.setOnSucceeded(evento -> alExito.accept(tarea.getValue()));
        tarea.setOnFailed(evento -> alFallo.accept(tarea.getException()));
        EjecutorTareas.obtenerInstancia().ejecutar(tarea);
    }
}
