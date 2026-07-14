package com.rrhh.modelo;

import com.rrhh.observer.IObservador;
import com.rrhh.observer.ISujetoObservable;
import com.rrhh.state.EstadoPendiente;
import com.rrhh.state.IEstadoSolicitud;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Solicitud implements ISujetoObservable {

    protected Integer id;
    protected Empleado empleado;
    protected LocalDate fechaInicio;
    protected LocalDate fechaFin;
    protected int diasHabilesCalculados;
    protected IEstadoSolicitud estado;
    protected Empleado aprobador;
    protected LocalDateTime fechaSolicitud;
    protected LocalDateTime fechaResolucion;
    protected String comentario;

    private final List<IObservador> observadores = new ArrayList<>();

    protected Solicitud(Integer id, Empleado empleado, LocalDate fechaInicio, LocalDate fechaFin,
                         int diasHabilesCalculados) {
        this.id = id;
        this.empleado = empleado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.diasHabilesCalculados = diasHabilesCalculados;
        this.estado = new EstadoPendiente();
        this.fechaSolicitud = LocalDateTime.now();
    }

    public abstract String getTipoSolicitud();

    public void aprobar(Empleado aprobador) {
        this.aprobador = aprobador;
        estado.aprobar(this);
    }

    public void rechazar(Empleado aprobador, String comentario) {
        this.aprobador = aprobador;
        this.comentario = comentario;
        estado.rechazar(this);
    }

    public void cancelar() {
        estado.cancelar(this);
    }

    @Override
    public void agregarObservador(IObservador observador) {
        observadores.add(observador);
    }

    @Override
    public void removerObservador(IObservador observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarObservadores() {
        for (IObservador observador : observadores) {
            observador.actualizar(this);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getDiasHabilesCalculados() {
        return diasHabilesCalculados;
    }

    public void setDiasHabilesCalculados(int diasHabilesCalculados) {
        this.diasHabilesCalculados = diasHabilesCalculados;
    }

    public IEstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(IEstadoSolicitud estado) {
        this.estado = estado;
    }

    public String getNombreEstado() {
        return estado.getNombreEstado();
    }

    public Empleado getAprobador() {
        return aprobador;
    }

    public void setAprobador(Empleado aprobador) {
        this.aprobador = aprobador;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
