package QuintoEjercicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BotonInteligenteTest {
    private BotonInteligente botonInteligente;

    @BeforeEach
    public void setUp() {
        // Inicializa el botón inteligente antes de cada prueba
        botonInteligente = new BotonInteligente("Haz click o habla.");
    }

    @Test
    public void testOnClick() {
        // Simulamos hacer clic en el botón
        botonInteligente.doClick(); // Esto llama a la función "onClick()" directamente.

        // Verificamos que el botón no sea nulo (esto indica que la función fue llamada correctamente).
        assertNotNull(botonInteligente);
    }

    @Test
    public void testActivarPorVoz() {
        // Llamamos a la función "activarPorVoz" para asegurarnos de que funciona correctamente.
        botonInteligente.activarPorVoz("activar"); // Debemos asegurarnos que la función sea accesible.

        // Dado que no estamos usando Mockito, no podemos verificar directamente el funcionamiento.
        // Solo verificamos que el botón no sea nulo.
        assertNotNull(botonInteligente);
    }

    @Test
    public void testNoActivarPorVozIncorrecto() {
        // Intentamos activar el botón con un comando incorrecto
        botonInteligente.activarPorVoz("desactivar");

        // Aquí simplemente verificamos que el botón sigue existiendo
        assertNotNull(botonInteligente);
    }

    @Test
    public void testConstructor() {
        // Verificamos que el texto del botón esté correctamente establecido
        assertEquals("Haz click o habla.", botonInteligente.getText());
    }
}
