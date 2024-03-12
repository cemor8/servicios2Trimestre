package com.example.librosserver;

public class Controllers {
    private ControllerPanelPrincipal controllerPanelPrincipal;
    private ControllerMenu controllerMenu;

    public ControllerMenu getControllerMenu() {
        return controllerMenu;
    }

    public void setControllerMenu(ControllerMenu controllerMenu) {
        this.controllerMenu = controllerMenu;
    }

    public ControllerPanelPrincipal getControllerPanelPrincipal() {
        return controllerPanelPrincipal;
    }

    public void setControllerPanelPrincipal(ControllerPanelPrincipal controllerPanelPrincipal) {
        this.controllerPanelPrincipal = controllerPanelPrincipal;
    }
}
