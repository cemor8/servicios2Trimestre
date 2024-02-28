package com.example.servercorreoelectronico;

import java.io.Serializable;

public class Correo implements Serializable {
    private String remitente;
    private String destinatario;
    private String mensaje;
    private String asunto;

    public Correo(String remitente, String destinatario, String mensaje, String asunto) {
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

    public String getRemitente() {
        return remitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }
}
