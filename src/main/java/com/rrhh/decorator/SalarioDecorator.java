package com.rrhh.decorator;

import java.math.BigDecimal;

public abstract class SalarioDecorator implements ICalculoSalario {

    protected final ICalculoSalario envuelto;
    protected final String concepto;
    protected final BigDecimal monto;

    protected SalarioDecorator(ICalculoSalario envuelto, String concepto, BigDecimal monto) {
        this.envuelto = envuelto;
        this.concepto = concepto;
        this.monto = monto;
    }

    public String getConcepto() {
        return concepto;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    @Override
    public String getDescripcion() {
        return envuelto.getDescripcion() + " + " + concepto;
    }
}
