package pe.edu.utp.casacambio.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.casacambio.modelo.cliente.Moneda;
import pe.edu.utp.casacambio.modelo.cliente.ParMoneda;
import pe.edu.utp.casacambio.repository.cliente.MonedaRepository;
import pe.edu.utp.casacambio.repository.cliente.ParMonedaRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParMonedaResolverTest {

    @Mock
    private MonedaRepository monedaRepository;
    @Mock
    private ParMonedaRepository parMonedaRepository;
    @InjectMocks
    private ParMonedaResolver resolver;

    @Test
    void resolver_siParExiste_debeRetornarloSinCrearNada() {
        ParMoneda existente = par(moneda("PEN"), moneda("USD"));
        when(parMonedaRepository.buscarPorCodigos("PEN", "USD")).thenReturn(Optional.of(existente));

        ParMoneda resultado = resolver.resolver("PEN", "USD");

        assertSame(existente, resultado);
        verifyNoInteractions(monedaRepository);
        verify(parMonedaRepository, never()).guardar(any());
    }

    @Test
    void resolver_siMonedasExistenYParNo_debeCrearPar() {
        Moneda pen = moneda("PEN");
        Moneda usd = moneda("USD");
        when(parMonedaRepository.buscarPorCodigos("PEN", "USD")).thenReturn(Optional.empty());
        when(monedaRepository.buscarPorCodigo("PEN")).thenReturn(Optional.of(pen));
        when(monedaRepository.buscarPorCodigo("USD")).thenReturn(Optional.of(usd));
        when(parMonedaRepository.guardar(any(ParMoneda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ParMoneda resultado = resolver.resolver("PEN", "USD");

        assertSame(pen, resultado.getMonedaOrigen());
        assertSame(usd, resultado.getMonedaDestino());
        assertTrue(resultado.isActivo());
        verify(monedaRepository, never()).guardar(any());
    }

    @Test
    void resolver_siMonedasNoExisten_debeCrearlasConDatosConocidos() {
        when(parMonedaRepository.buscarPorCodigos("PEN", "EUR")).thenReturn(Optional.empty());
        when(monedaRepository.buscarPorCodigo(anyString())).thenReturn(Optional.empty());
        when(monedaRepository.guardar(any(Moneda.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(parMonedaRepository.guardar(any(ParMoneda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ParMoneda resultado = resolver.resolver("PEN", "EUR");

        assertEquals("Sol peruano", resultado.getMonedaOrigen().getNombre());
        assertEquals("S/", resultado.getMonedaOrigen().getSimbolo());
        assertEquals("Euro", resultado.getMonedaDestino().getNombre());
        assertEquals("€", resultado.getMonedaDestino().getSimbolo());
        verify(monedaRepository, times(2)).guardar(any(Moneda.class));
    }

    @Test
    void resolver_monedaDesconocida_debeUsarCodigoComoNombreYSimbolo() {
        when(parMonedaRepository.buscarPorCodigos("GBP", "USD")).thenReturn(Optional.empty());
        when(monedaRepository.buscarPorCodigo(anyString())).thenReturn(Optional.empty());
        when(monedaRepository.guardar(any(Moneda.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(parMonedaRepository.guardar(any(ParMoneda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ParMoneda resultado = resolver.resolver("GBP", "USD");

        assertEquals("GBP", resultado.getMonedaOrigen().getNombre());
        assertEquals("GBP", resultado.getMonedaOrigen().getSimbolo());
    }

    private Moneda moneda(String codigo) {
        Moneda moneda = new Moneda();
        moneda.setCodigo(codigo);
        return moneda;
    }

    private ParMoneda par(Moneda origen, Moneda destino) {
        ParMoneda par = new ParMoneda();
        par.setMonedaOrigen(origen);
        par.setMonedaDestino(destino);
        par.setActivo(true);
        return par;
    }
}
