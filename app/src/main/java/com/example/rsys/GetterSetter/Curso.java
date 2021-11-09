package com.example.rsys.GetterSetter;

public class Curso {
    private int id;
    private String nombre;
    private String seccion;

    public Curso(){

    }

    public Curso(int id, String nombre, String seccion){
        this.setId(id);
        this.setNombre(nombre);
        this.setSeccion(seccion);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    @Override
    public String toString(){
        return nombre;
    }
}
