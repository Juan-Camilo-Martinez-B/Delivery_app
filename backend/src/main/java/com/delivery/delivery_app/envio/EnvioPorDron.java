package com.delivery.delivery_app.envio;

/**
 * Envío por dron.
 * Fórmula: costo = max(peso, volumen) * 20000
 */
public class EnvioPorDron extends Envio {

    private static final double TARIFA = 20000.0;

    public EnvioPorDron(double peso, double volumen) {
        super(peso, volumen);
    }

    @Override
    public double calcularCosto() {
        return calcularPorTarifa(TARIFA);
    }
}
