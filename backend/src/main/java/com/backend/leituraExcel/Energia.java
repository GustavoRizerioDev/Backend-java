package main.java.com.backend.leituraExcel;

import java.time.LocalDate;

public class Energia {

    private String local;
    private String mes;
    private Double kwh;
    private Double gasto;
    private Integer ano;

    public Energia() {
    }

    public Energia(Double gasto, Double kwh, String mes, String local, Integer ano) {
        this.gasto = gasto;
        this.kwh = kwh;
        this.mes = mes;
        this.local = local;
        this.ano = ano;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Double getKwh() {
        return kwh;
    }

    public void setKwh(Double kwh) {
        this.kwh = kwh;
    }

    public Double getGasto() {
        return gasto;
    }

    public void setGasto(Double gasto) {
        this.gasto = gasto;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    @Override
    public String toString() {
        return "Energia{" +
                "local='" + local + '\'' +
                ", mes='" + mes + '\'' +
                ", kwh=" + kwh +
                ", gasto=" + gasto +
                ", ano=" + ano +
                '}';
    }
}

