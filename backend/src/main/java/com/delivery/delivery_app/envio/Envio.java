package com.delivery.delivery_app.envio;

/**
 * Representa un envío genérico.
 * <p>
 * Define los datos mínimos (peso y volumen) y obliga a las clases hijas a implementar
 * la lógica de cálculo del costo.
 */
public abstract class Envio {

    private double peso;
    private double volumen;

    protected Envio(double peso, double volumen) {
        setPeso(peso);
        setVolumen(volumen);
    }

    /**
     * Calcula el costo del envío según el tipo concreto.
     */
    public abstract double calcularCosto();

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        validarNoNegativo(peso, "peso");
        this.peso = peso;
    }

    public double getVolumen() {
        return volumen;
    }

    public void setVolumen(double volumen) {
        validarNoNegativo(volumen, "volumen");
        this.volumen = volumen;
    }

    /**
     * Reutiliza la fórmula común: max(peso, volumen) * tarifa.
     */
    protected double calcularPorTarifa(double tarifa) {
        validarNoNegativo(tarifa, "tarifa");
        return Math.max(peso, volumen) * tarifa;
    }

    private void validarNoNegativo(double valor, String nombreCampo) {
        if (valor < 0) {
            throw new IllegalArgumentException("El campo '" + nombreCampo + "' no puede ser negativo.");
        }
    }
}
