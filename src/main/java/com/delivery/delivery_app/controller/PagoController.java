package com.delivery.delivery_app.controller;

import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.delivery_app.dto.PagoRequest;
import com.delivery.delivery_app.model.Pago;
import com.delivery.delivery_app.service.PagoService;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private static final Logger log = Logger.getLogger(PagoController.class.getName());

    @Autowired
    private PagoService pagoService;

    @PostMapping
    public ResponseEntity<Pago> procesarPago(@Valid @RequestBody PagoRequest request) {
        log.info("POST /api/pagos - Procesando pago para pedido: " + request.getPedidoId());
        Pago transaccion = pagoService.crearTransaccionPago(request);
        Pago pagoProcesado = pagoService.procesarTransaccionPago(transaccion.getId());
        return new ResponseEntity<>(pagoProcesado, HttpStatus.CREATED);
    }

    @GetMapping("/{pagoId}")
    public ResponseEntity<Pago> obtenerPago(@PathVariable String pagoId) {
        log.info("GET /api/pagos/" + pagoId + " - Obteniendo información del pago");
        Pago pago = pagoService.obtenerPago(pagoId);
        return new ResponseEntity<>(pago, HttpStatus.OK);
    }

    @PostMapping("/{pagoId}/reembolsar")
    public ResponseEntity<Pago> reembolsarPago(@PathVariable String pagoId) {
        log.info("POST /api/pagos/" + pagoId + "/reembolsar - Procesando reembolso");
        Pago pago = pagoService.reembolsarPago(pagoId);
        return new ResponseEntity<>(pago, HttpStatus.OK);
    }
}
