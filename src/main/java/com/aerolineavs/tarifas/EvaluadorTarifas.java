package com.aerolineavs.tarifas;

/**
 * Fachada de compatibilidad para el API estático anterior.
 *
 * @deprecated usar {@link IPricingService} con {@link PricingServiceImpl}
 */
@Deprecated
public final class EvaluadorTarifas {

    private static final IPricingService SERVICIO = new PricingServiceImpl();

    private EvaluadorTarifas() {
    }

    /**
     * Evalúa una única tarifa aplicable en función de los datos del cliente.
     *
     * @param cliente datos del cliente
     * @return tarifa resultante junto con suposiciones
     */
    public static ResultadoTarifa evaluar(ClientePotencial cliente) {
        return SERVICIO.calcular(cliente);
    }
}
