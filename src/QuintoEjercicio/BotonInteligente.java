package QuintoEjercicio;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BotonInteligente extends JButton {
    private String voiceCommand = "activa"; // Comando de voz para activar el botón

    public BotonInteligente(String text) {
        super(text);
        setBorderPainted(false); // Sin bordes
        setContentAreaFilled(false); // Sin fondo
        setFocusPainted(false); // Sin efecto de foco

        // Manejar clics del mouse
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick();
            }
        });

        // Iniciar el reconocimiento de voz
        empezarReconocimientoVoz();
    }

    // Función llamada para hacer clic al boton.
    private void onClick() {
        System.out.println("Botón activado por clic");
        // Aquí puedes añadir la lógica que quieres que se ejecute al hacer clic
    }

    // Función para poder activar el botón mediante la voz.
    void activarPorVoz(String voiceCommand) {
        if (voiceCommand.equalsIgnoreCase(this.voiceCommand)) {
            onClick();
        }
    }

    // Función para empezar el reconocimiento de voz.
    private void empezarReconocimientoVoz() {
        // Se hace en un hilo para que esté en constante ejecución en paralelo.
        new Thread(() -> {
            try {
                Configuration configuration = new Configuration();

                // Es necesario que la carpeta "voces" esté presente el proyecto para que detecte los modelos y voces.
                // Establecer la ruta del modelo de voz y el diccionario
                configuration.setAcousticModelPath("voces/model_parameters/voxforge_es_sphinx.cd_ptm_4000");
                // Configuramos el diccionario fonético en español.
                configuration.setDictionaryPath("voces/etc/es.dict");
                // Configuramos el modelo del lenguaje en español.
                configuration.setLanguageModelPath("voces/etc/es-20k.lm");

                // Declaramos y hacemos que empiece a reconocer la voz.
                LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
                recognizer.startRecognition(true);
                // Declaramos la variable para el resultado del comando de voz.
                SpeechResult result;

                // Función que indica que se ha reconocido.
                while ((result = recognizer.getResult()) != null) {
                    String recognizedText = result.getHypothesis();
                    System.out.println("Reconocido: " + recognizedText);
                    activarPorVoz(recognizedText);
                }
                // Para de reconocer la voz.
                recognizer.stopRecognition();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    public static void main(String[] args) {
        // Declaramos las propiedades, añadimos el boton y hacemos el frame visible.
        JFrame frame = new JFrame("Botón Inteligente");
        BotonInteligente botonInteligente = new BotonInteligente("Haz click o habla.");
        frame.setLayout(new FlowLayout());
        frame.add(botonInteligente);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
