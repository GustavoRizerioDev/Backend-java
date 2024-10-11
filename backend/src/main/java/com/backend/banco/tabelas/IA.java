package com.backend.banco.tabelas;

public class IA {

    Integer idIA;
    String Prompt;

    public IA(){

    }
    public IA(Integer idIA, String prompt) {
        this.idIA = idIA;
        this.Prompt = prompt;
    }

    public Integer getIdIA() {
        return idIA;
    }

    public void setIdIA(Integer idIA) {
        this.idIA = idIA;
    }

    public String getPrompt() {
        return Prompt;
    }

    public void setPrompt(String prompt) {
        Prompt = prompt;
    }

    @Override
    public String toString() {
        return "IA{" +
                "idIA=" + idIA +
                ", Prompt='" + Prompt + '\'' +
                '}';
    }
}
