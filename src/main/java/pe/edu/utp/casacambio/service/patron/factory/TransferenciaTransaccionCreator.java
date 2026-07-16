package pe.edu.utp.casacambio.service.patron.factory;

import pe.edu.utp.casacambio.modelo.cliente.TipoOperacion;
import pe.edu.utp.casacambio.modelo.transaccion.EstadoTransaccion;
import pe.edu.utp.casacambio.modelo.transaccion.Transaccion;
import lombok.extern.slf4j.Slf4j;

/**
 * Creator concreto para transacciones de tipo TRANSFERENCIA.
 * Patron: Factory Method. Principio SOLID: SRP.
 *
 * TRANSFERENCIA: conversion entre dos cuentas en distintas divisas.
 * Inicia directamente en estado EN_PROCESO por requerir validacion adicional.
 */
@Slf4j
public class TransferenciaTransaccionCreator implements TransaccionCreator {

    /**
     * Crea una transaccion de TRANSFERENCIA con estado inicial EN_PROCESO.
     *
     * @return transaccion preconfigurada como TRANSFERENCIA
     */
    @Override
    public Transaccion crearTransaccion() {
        // La transferencia inicia en EN_PROCESO por requerir validacion adicional
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo(TipoOperacion.TRANSFERENCIA);
        transaccion.setEstado(EstadoTransaccion.EN_PROCESO);
        log.info("Factory: transaccion de TRANSFERENCIA creada.");
        return transaccion;
    }
}
