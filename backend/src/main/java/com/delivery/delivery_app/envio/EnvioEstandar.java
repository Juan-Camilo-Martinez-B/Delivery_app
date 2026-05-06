package com.delivery.delivery_app.envio;

/**
 * Envío estándar.
 * Fórmula: costo = max(peso, volumen) * 5000
 */
public class EnvioEstandar extends Envio {

    private static final double TARIFA = 5000.0;

    public EnvioEstandar(double peso, double volumen) {
        super(peso, volumen);
    }

    @Override
    public double calcularCosto() {
        return calcularPorTarifa(TARIFA);
    }
}
