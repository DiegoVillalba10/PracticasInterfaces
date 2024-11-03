package TercerEjercicio;

import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.MatOfRect;

import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;

public class InterfazDeteccionGestos extends JFrame {
    private JButton botonAccion;
    private JLabel etiquetaVideo;
    private VideoCapture videoCapture;
    private boolean gestoDetectado = false;

    private int contadorManosDetectadas = 0;
    private int contadorManosNoDetectadas = 0;
    private static final int CUANTOS_DEBE_DETECTAR = 5; // Número de detecciones requeridas

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Cargamos la biblioteca nativa de OpenCV 4.0
    }

    public InterfazDeteccionGestos() throws HeadlessException {
        setTitle("Detección de Gestos usando OpenCV");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Creamos el botón en la interfaz.
        botonAccion = new JButton("Botón de Acción");
        botonAccion.setEnabled(false);
        add(botonAccion, BorderLayout.SOUTH);

        // Creamos la etiqueta para mostrar el video.
        etiquetaVideo = new JLabel();
        add(etiquetaVideo, BorderLayout.CENTER);

        // Iniciamos la captura de video con OpenCV.
        videoCapture = new VideoCapture(0); // El '0' indica la cámara por defecto.
        if (!videoCapture.isOpened()) {
            System.out.println("No se pudo abrir la cámara correctamente.");
            System.exit(1);
        }

        // Iniciamos el procesamiento de video en un hilo separado para que haya ejecución en paralelo.
    }
    private void procesarVideo() {
        Mat frame = new Mat();
        CascadeClassifier detectorMano = new CascadeClassifier("C:\\Users\\diego\\IdeaProjects\\PracticasInterfaces\\src\\TercerEjercicio\\clasificadores\\hand_cascade.xml");

        if (detectorMano.empty()) {
            System.out.println("Error: El clasificador no se cargó correctamente.");
            return; // Termina si no se carga
        }


        while (videoCapture.read(frame)) {
            // Convertimos el frame en escala de grises.
            Mat frameGris = new Mat();
            Imgproc.cvtColor(frame, frameGris, Imgproc.COLOR_BGR2GRAY);

            // Detectamos los gestos (manos en este caso).
            MatOfRect manosDetectadas = new MatOfRect();
            detectorMano.detectMultiScale(frameGris, manosDetectadas, 1.1, 10, 0, new Size(30, 30), new Size());

            if (!manosDetectadas.empty()) {
                contadorManosDetectadas++;
                contadorManosNoDetectadas = 0; // Reinicia el contador de no detectadas
            } else {
                contadorManosNoDetectadas++;
                contadorManosDetectadas = 0; // Reinicia el contador de detectadas
            }

            // Habilitar el botón si hemos detectado la mano varias veces
            if (contadorManosDetectadas >= CUANTOS_DEBE_DETECTAR) {
                if (!gestoDetectado) { // Solo habilitar si no estaba habilitado
                    gestoDetectado = true;
                    habilitarBoton();
                    System.out.println("¡Mano detectada!"); // Mensaje de depuración
                }
            }
            // Deshabilitar el botón si no hemos detectado la mano varias veces
            else if (contadorManosNoDetectadas >= CUANTOS_DEBE_DETECTAR) {
                if (gestoDetectado) { // Solo deshabilitar si estaba habilitado
                    gestoDetectado = false;
                    deshabilitarBoton();
                    System.out.println("No se detectó la mano."); // Mensaje de depuración
                }
            }

            // Convertimos el Mat a BufferedImage para mostrarlo en la etiqueta.
            ImageIcon icono = new ImageIcon(HighGui.toBufferedImage(frame));
            etiquetaVideo.setIcon(icono);
            etiquetaVideo.repaint();
        }
    }
    private void habilitarBoton() {
        SwingUtilities.invokeLater(() -> {botonAccion.setEnabled(true);});
    }
    private void deshabilitarBoton() {
        SwingUtilities.invokeLater(() -> {botonAccion.setEnabled(false);});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfazDeteccionGestos interfaz = new InterfazDeteccionGestos();
            interfaz.setVisible(true);
            // Iniciamos el proceso del video en nuevo hilo.
            new Thread(interfaz::procesarVideo).start();
        });
    }
}
