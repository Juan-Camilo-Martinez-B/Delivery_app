package com.delivery.delivery_app.envio;

/**
 * Envío internacional.
 * Fórmula: costo = max(peso, volumen) * 15000
 */
public class EnvioInternacional extends Envio {

    private static final double TARIFA = 15000.0;

    public EnvioInternacional(double peso, double volumen) {
        super(peso, volumen);
    }

    @Override
    public double calcularCosto() {
        return calcularPorTarifa(TARIFA);
    }
}
