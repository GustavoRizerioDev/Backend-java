package com.backend.banco.tabelas;

public class Energia {

    Integer idEnergia;
    Double Kwh;
    Double Gasto;
    Integer Mes;
    Integer fk_local;

    public Energia(){

    }

    public Energia(Integer idEnergia, Double kwh, Double gasto, Integer mes, Integer fk_local) {
        this.idEnergia = idEnergia;
        Kwh = kwh;
        Gasto = gasto;
        Mes = mes;
        this.fk_local = fk_local;
    }

    public Integer getIdEnergia() {
        return idEnergia;
    }

    public void setIdEnergia(Integer idEnergia) {
        this.idEnergia = idEnergia;
    }

    public Double getKwh() {
        return Kwh;
    }

    public void setKwh(Double kwh) {
        Kwh = kwh;
    }

    public Double getGasto() {
        return Gasto;
    }

    public void setGasto(Double gasto) {
        Gasto = gasto;
    }

    public Integer getMes() {
        return Mes;
    }

    public void setMes(Integer mes) {
        Mes = mes;
    }

    public Integer getFk_local() {
        return fk_local;
    }

    public void setFk_local(Integer fk_local) {
        this.fk_local = fk_local;
    }

    @Override
    public String toString() {
        return "Energia{" +
                "idEnergia=" + idEnergia +
                ", Kwh=" + Kwh +
                ", Gasto=" + Gasto +
                ", Mes=" + Mes +
                ", fk_local=" + fk_local +
                '}';
    }
}
