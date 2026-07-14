package com.rrhh.modelo;

import com.rrhh.modelo.enums.TipoMovimientoNomina;

import java.math.BigDecimal;

public class DetalleNomina {

    private Integer id;
    private Integer idNomina;
    private TipoMovimientoNomina tipo;
    private String concepto;
    private BigDecimal monto;

    public DetalleNomina(Integer id, Integer idNomina, TipoMovimientoNomina tipo, String concepto, BigDecimal monto) {
        this.id = id;
        this.idNomina = idNomina;
        this.tipo = tipo;
        this.concepto = concepto;
        this.monto = monto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdNomina() {
        return idNomina;
    }

    public void setIdNomina(Integer idNomina) {
        this.idNomina = idNomina;
    }

    public TipoMovimientoNomina getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimientoNomina tipo) {
        this.tipo = tipo;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}
