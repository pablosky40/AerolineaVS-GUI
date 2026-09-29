package com.aerolineavs.tarifas;

/**
 * Implementación de las reglas de negocio para recomendar tarifas.
 */
public final class PricingServiceImpl implements IPricingService {

    private static final String SUPOSICIONES = "Suposiciones: curso universitario = 9 desplazamientos/año; "
            + "la frecuencia anual informada representa los viajes relevantes de cada regla; "
            + "si no se cumple ninguna condición exacta, no se oferta tarifa.";

    @Override
    public ResultadoTarifa calcular(ClientePotencial cliente) {
        if (cliente.edad() < 18
                && cliente.tipoViajero() == TipoViajero.MENOR
                && cliente.vuelosAnuales() >= 6) {
            return resultado(Tarifa.PAJARILLO);
        }

        if (cliente.edad() >= 18 && cliente.edad() <= 25) {
            if (cliente.tipoViajero() == TipoViajero.ESTUDIANTE_UNIVERSITARIO_DESPLAZADO
                    && cliente.clasePreferida() == ClaseVuelo.TURISTA
                    && cliente.vuelosAnuales() >= 9) {
                return resultado(Tarifa.GORRION);
            }

            if (cliente.tipoViajero() == TipoViajero.TRABAJADOR_JOVEN
                    && cliente.clasePreferida() == ClaseVuelo.TURISTA
                    && cliente.vuelosAnuales() >= 3) {
                Tarifa tarifa = cliente.viveConPadres()
                        ? Tarifa.VIAJA_AHORA_QUE_PUEDES
                        : Tarifa.ATREVIENDOSE_A_SALTAR_DEL_NIDO;
                return resultado(tarifa);
            }
        }

        if (cliente.edad() > 25) {
            if (cliente.ingresosAnuales() > 20000
                    && cliente.ingresosAnuales() < 35000
                    && cliente.vuelosAnuales() >= 6
                    && cliente.clasePreferida() == ClaseVuelo.TURISTA
                    && cliente.destinoPreferido() == RegionDestino.EUROPA) {
                Tarifa tarifa = cliente.viajaConNinos()
                        ? Tarifa.CONOCE_EUROPA_CON_TUS_PEQUES
                        : Tarifa.CONOCE_EUROPA;
                return resultado(tarifa);
            }

            if (cliente.ingresosAnuales() > 35000
                    && cliente.vuelosAnuales() >= 6
                    && cliente.clasePreferida() == ClaseVuelo.BUSINESS
                    && (cliente.destinoPreferido() == RegionDestino.ASIA
                    || cliente.destinoPreferido() == RegionDestino.AMERICA)) {
                Tarifa tarifa = cliente.viajaConNinos()
                        ? Tarifa.CONOCE_EL_MUNDO_CON_TUS_PEQUES
                        : Tarifa.CONOCE_EL_MUNDO;
                return resultado(tarifa);
            }
        }

        return resultado(Tarifa.SIN_TARIFA);
    }

    private ResultadoTarifa resultado(Tarifa tarifa) {
        return new ResultadoTarifa(tarifa, SUPOSICIONES);
    }
}