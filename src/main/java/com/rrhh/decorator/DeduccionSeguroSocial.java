package com.rrhh.decorator;

import java.math.BigDecimal;

public class DeduccionSeguroSocial extends SalarioDecorator {

    private static final BigDecimal TASA_ESSALUD = BigDecimal.valueOf(0.09);

    public DeduccionSeguroSocial(ICalculoSalario envuelto) {
        super(envuelto, "Seguro social (EsSalud 9%)", envuelto.calcular().multiply(TASA_ESSALUD));
    }

    @Override
    public BigDecimal calcular() {
        return envuelto.calcular().subtract(monto);
    }
}
