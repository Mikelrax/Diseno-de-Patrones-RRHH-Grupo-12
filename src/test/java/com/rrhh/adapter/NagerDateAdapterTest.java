package com.rrhh.adapter;

import com.rrhh.dao.FeriadoCacheDAO;
import com.rrhh.modelo.DiaFeriado;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NagerDateAdapterTest {

    @Test
    void obtieneFeriadosDePeruYLosDejaEnCache() {
        NagerDateAdapter adapter = new NagerDateAdapter();
        int anio = LocalDate.now().getYear();

        List<DiaFeriado> feriados = adapter.obtenerFeriados(anio, "PE");

        assertFalse(feriados.isEmpty(), "Nager.Date debería devolver feriados para Perú");
        assertTrue(new FeriadoCacheDAO().existePara(anio, "PE"), "El adapter debe dejar los feriados en caché");
    }
}
