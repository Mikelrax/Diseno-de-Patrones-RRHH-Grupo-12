package com.rrhh.observer;

public interface ISujetoObservable {

    void agregarObservador(IObservador observador);

    void removerObservador(IObservador observador);

    void notificarObservadores();
}
