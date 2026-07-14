package com.rrhh.decorator;

import java.math.BigDecimal;

public class BonoAntiguedad extends SalarioDecorator {

    private static final BigDecimal MONTO_POR_ANIO = BigDecimal.valueOf(50);

    public BonoAntiguedad(ICalculoSalario envuelto, int aniosAntiguedad) {
        super(envuelto, "Bono de antigüedad (" + aniosAntiguedad + " años)",
                BigDecimal.valueOf(aniosAntiguedad).multiply(MONTO_POR_ANIO));
    }

    @Override
    public BigDecimal calcular() {
        return envuelto.calcular().add(monto);
    }
}
