package com.aerolineavs.tarifas;

/**
 * Servicio de dominio para obtener la tarifa recomendada para un cliente.
 */
public interface IPricingService {

    /**
     * Calcula la tarifa aplicable según los datos del cliente.
     *
     * @param cliente datos del cliente
     * @return resultado de la evaluación
     */
    ResultadoTarifa calcular(ClientePotencial cliente);
}