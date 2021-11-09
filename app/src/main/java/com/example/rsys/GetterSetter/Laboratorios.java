package com.example.rsys.GetterSetter;

public class Laboratorios {
    private int ID;
    private String Aula;


    public Laboratorios(){

    }

    public Laboratorios(int ID, String Aula){
        this.setID(ID);
        this.setAula(Aula);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAula() {
        return Aula;
    }

    public void setAula(String aula) {
        Aula = aula;
    }

    @Override
    public String toString(){
        return getAula();
    }
}
