package com.rrhh.decorator;

import java.math.BigDecimal;

public class BonoProductividad extends SalarioDecorator {

    public BonoProductividad(ICalculoSalario envuelto, BigDecimal monto) {
        super(envuelto, "Bono de productividad", monto);
    }

    @Override
    public BigDecimal calcular() {
        return envuelto.calcular().add(monto);
    }
}
