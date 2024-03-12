package com.example.librosserver;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;

public class Data {
    public Data() {

    }
    private KeyPair llaves;
    private Socket socket;
    private PublicKey llaveServer;
    private boolean viendo;
    private Controllers controllers = new Controllers();

    public Controllers getControllers() {
        return controllers;
    }

    public void setControllers(Controllers controllers) {
        this.controllers = controllers;
    }

    public KeyPair getLlaves() {
        return llaves;
    }

    public PublicKey getLlaveServer() {
        return llaveServer;
    }

    public void setLlaveServer(PublicKey llaveServer) {
        this.llaveServer = llaveServer;
    }

    public void setLlaves(KeyPair llaves) {
        this.llaves = llaves;
    }

    public boolean isViendo() {
        return viendo;
    }

    public void setViendo(boolean viendo) {
        this.viendo = viendo;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
