package com.rrhh.util;

import com.rrhh.dto.ReporteEmpleadoDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ExportadorJSONTest {

    @Test
    void exportaUnaListaDeReportesAUnArchivoJson(@TempDir Path tempDir) throws Exception {
        List<ReporteEmpleadoDTO> reportes = List.of(
                new ReporteEmpleadoDTO("Ana Torres", "12345678", "ana@rrhh.local", "Tecnología",
                        "Desarrolladora", BigDecimal.valueOf(3500), "ACTIVO")
        );

        Path destino = tempDir.resolve("empleados.json");
        new ExportadorJSON().exportar(reportes, destino);

        String contenido = Files.readString(destino);
        assertTrue(contenido.contains("Ana Torres"));
        assertTrue(contenido.contains("12345678"));
    }
}
