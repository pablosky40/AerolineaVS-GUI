package com.aerolineavs.tarifas;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicación de escritorio Swing.
 */
public final class GuiApp {

    private GuiApp() {
    }

    /**
     * Inicia la GUI en el Event Dispatch Thread.
     *
     * @param args argumentos no usados
     */
    public static void main(String[] args) {
        IPricingService pricingService = new PricingServiceImpl();
        SwingUtilities.invokeLater(() -> new AerolineaGui(pricingService).setVisible(true));
    }
}