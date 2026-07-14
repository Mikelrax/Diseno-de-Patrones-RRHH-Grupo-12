package com.rrhh.decorator;

import java.math.BigDecimal;

public class DeduccionTardanzas extends SalarioDecorator {

    private static final BigDecimal COSTO_POR_MINUTO = BigDecimal.valueOf(0.5);

    public DeduccionTardanzas(ICalculoSalario envuelto, int minutosTardanza) {
        super(envuelto, "Descuento por tardanzas (" + minutosTardanza + " min)",
                BigDecimal.valueOf(minutosTardanza).multiply(COSTO_POR_MINUTO));
    }

    @Override
    public BigDecimal calcular() {
        return envuelto.calcular().subtract(monto);
    }
}
