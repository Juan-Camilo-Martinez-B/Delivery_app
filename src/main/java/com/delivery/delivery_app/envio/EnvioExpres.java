package com.delivery.delivery_app.envio;

/**
 * Envío exprés.
 * Fórmula: costo = max(peso, volumen) * 8000
 */
public class EnvioExpres extends Envio {

    private static final double TARIFA = 8000.0;

    public EnvioExpres(double peso, double volumen) {
        super(peso, volumen);
    }

    @Override
    public double calcularCosto() {
        return calcularPorTarifa(TARIFA);
    }
}
