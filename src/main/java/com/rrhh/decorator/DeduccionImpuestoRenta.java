package com.rrhh.decorator;

import java.math.BigDecimal;

public class DeduccionImpuestoRenta extends SalarioDecorator {

    private static final BigDecimal TASA_QUINTA_CATEGORIA = BigDecimal.valueOf(0.08);

    public DeduccionImpuestoRenta(ICalculoSalario envuelto) {
        super(envuelto, "Impuesto a la renta (5ta categoría)", envuelto.calcular().multiply(TASA_QUINTA_CATEGORIA));
    }

    @Override
    public BigDecimal calcular() {
        return envuelto.calcular().subtract(monto);
    }
}
