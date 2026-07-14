package com.rrhh.interfaces;

import com.rrhh.modelo.DiaFeriado;

import java.time.LocalDate;
import java.util.List;

public interface IProveedorFeriados {

    List<DiaFeriado> obtenerFeriados(int anio, String paisIso);

    boolean esFeriado(LocalDate fecha, String paisIso);
}
