package com.example.librosserver;

import java.io.Serial;
import java.io.Serializable;

public class Libro implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String titulo;
    private String autor;
    private String imagen;

    public Libro(String titulo, String autor, String imagenPath) {
        this.titulo = titulo;
        this.autor = autor;
        this.imagen = imagenPath;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
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
