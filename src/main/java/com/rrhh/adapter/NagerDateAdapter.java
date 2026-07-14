package com.rrhh.adapter;

import com.rrhh.dao.FeriadoCacheDAO;
import com.rrhh.interfaces.IProveedorFeriados;
import com.rrhh.modelo.DiaFeriado;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NagerDateAdapter implements IProveedorFeriados {

    private final NagerDateClient client = new NagerDateClient();
    private final FeriadoCacheDAO cacheDAO = new FeriadoCacheDAO();

    @Override
    public List<DiaFeriado> obtenerFeriados(int anio, String paisIso) {
        if (cacheDAO.existePara(anio, paisIso)) {
            return cacheDAO.listarPorAnioYPais(anio, paisIso);
        }

        try {
            List<PublicHolidayDTO> crudos = client.obtenerFeriadosPublicos(anio, paisIso);
            List<DiaFeriado> feriados = new ArrayList<>();
            for (PublicHolidayDTO dto : crudos) {
                feriados.add(new DiaFeriado(LocalDate.parse(dto.date), dto.localName, dto.fixed));
            }
            cacheDAO.guardarTodos(feriados, paisIso);
            return feriados;
        } catch (Exception e) {
            return cacheDAO.listarPorAnioYPais(anio, paisIso);
        }
    }

    @Override
    public boolean esFeriado(LocalDate fecha, String paisIso) {
        return obtenerFeriados(fecha.getYear(), paisIso).stream()
                .anyMatch(feriado -> feriado.getFecha().equals(fecha));
    }
}
