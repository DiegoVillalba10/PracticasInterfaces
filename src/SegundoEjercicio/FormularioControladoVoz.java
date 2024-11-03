import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class FormularioControladoVoz extends JFrame {
    private JTextField campoTexto;
    private JButton botonEnviar;
    private JLabel labelMensaje; // JLabel para mostrar el mensaje enviado

    public FormularioControladoVoz() {
        // Configuramos la ventana principal
        setTitle("Formulario Controlado por Voz");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Instanciamos los componentes
        campoTexto = new JTextField(15);
        botonEnviar = new JButton("Enviar");
        labelMensaje = new JLabel(""); // Inicializamos el JLabel

        // Añadimos los componentes al frame
        add(new JLabel("Escribe aquí:"));
        add(campoTexto);
        add(botonEnviar);
        add(labelMensaje); // Añadimos el JLabel a la ventana

        // Acción del botón
        botonEnviar.addActionListener(e -> enviarFormulario());

        // Iniciar reconocimiento de voz en un hilo separado
        new Thread(this::iniciarReconocimientoVoz).start();
    }

    private void enviarFormulario() {
        String texto = campoTexto.getText();
        System.out.println("Formulario enviado con el texto: " + texto);

        // Actualizar el JLabel con el mensaje enviado
        labelMensaje.setText("Texto enviado: " + texto);

        // Aquí puedes agregar la lógica para procesar el formulario
        campoTexto.setText(""); // Limpia el campo después de enviar
    }

    private void iniciarReconocimientoVoz() {
        Configuration configuration = new Configuration();
        // Configuramos el modelo acústico y diccionario
        configuration.setAcousticModelPath("voces/model_parameters/voxforge_es_sphinx.cd_ptm_4000");
        configuration.setDictionaryPath("voces/etc/es.dict");
        configuration.setLanguageModelPath("voces/etc/es-20k.lm");

        try {
            LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
            recognizer.startRecognition(true);
            System.out.println("Reconocimiento de voz iniciado. Di 'Enviar formulario' para enviar o 'escribir [texto]' para escribir.");

            while (true) {
                SpeechResult result = recognizer.getResult();
                if (result != null) {
                    String textoReconocido = result.getHypothesis();
                    System.out.println("Reconocido: " + textoReconocido);

                    // Si el usuario dice "enviar formulario"
                    if (textoReconocido.equalsIgnoreCase("enviar formulario")) {
                        SwingUtilities.invokeLater(this::enviarFormulario);
                    }
                    // Si el usuario dice "escribir [texto]"
                    else if (textoReconocido.startsWith("escribir")) {
                        String textoParaEscribir = textoReconocido.replaceFirst("escribir", "").trim();
                        SwingUtilities.invokeLater(() -> campoTexto.setText(textoParaEscribir));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error en el reconocimiento de voz: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FormularioControladoVoz form = new FormularioControladoVoz();
            form.setVisible(true);
        });
    }
}
