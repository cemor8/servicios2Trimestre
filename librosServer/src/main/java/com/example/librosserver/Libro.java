package com.example.librosserver;

import java.io.Serial;
import java.io.Serializable;

public class Libro implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String titulo;
    private String autor;
    private String imagen;
    private String descripcion;


    public Libro(String titulo, String autor, String imagen, String descripcion) {
        this.titulo = titulo;
        this.autor = autor;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {        this.descripcion = descripcion;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
