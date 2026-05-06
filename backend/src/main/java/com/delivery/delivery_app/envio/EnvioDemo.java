package com.delivery.delivery_app.envio;

import java.util.Arrays;
import java.util.List;

/**
 * Ejemplo de uso: demuestra herencia + polimorfismo al calcular el costo
 * de diferentes tipos de envío a través de una referencia Envio.
 */
public class EnvioDemo {

    public static void main(String[] args) {
        Envio estandar = new EnvioEstandar(3.2, 2.0);
        Envio expres = new EnvioExpres(1.0, 4.5);
        Envio internacional = new EnvioInternacional(10.0, 7.0);
        Envio porDron = new EnvioPorDron(0.8, 0.6);

        List<Envio> envios = Arrays.asList(estandar, expres, internacional, porDron);

        for (Envio envio : envios) {
            double costo = envio.calcularCosto();
            System.out.println(envio.getClass().getSimpleName() + " -> costo: " + costo);
        }
    }
}
