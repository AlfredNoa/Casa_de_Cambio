package pe.edu.utp.casacambio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del Sistema de Casa de Cambio.
 * Proyecto Final - Diseno de Patrones - UTP
 * Docente: Bustamante Romero, Jose
 */
@SpringBootApplication
public class CasacambioApplication {

    /**
     * Punto de entrada de la aplicacion Spring Boot.
     *
     * @param args argumentos de linea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(CasacambioApplication.class, args);
    }
}
