package CuartoEjercicio;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;

public class RealidadAumentadaAPP extends JFrame {
    // Cargar la biblioteca nativa de OpenCV
    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private VideoCapture videoCapture;
    private JLabel videoLabel;
    private DetectorRealidadAumentada markerDetector;
    private ArrayList<Point> markerPoints;

    public RealidadAumentadaAPP() {
        setTitle("Aplicación de Realidad Aumentada");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        videoLabel = new JLabel();
        add(videoLabel, BorderLayout.CENTER);

        videoCapture = new VideoCapture(0);
        if (!videoCapture.isOpened()) {
            System.out.println("Error: No se pudo abrir la cámara.");
            System.exit(0);
        }

        markerDetector = new DetectorRealidadAumentada();
        markerPoints = new ArrayList<>();
        new Thread(this::captureVideo).start();
    }

    private void captureVideo() {
        Mat frame = new Mat();
        while (true) {
            if (videoCapture.read(frame)) {
                processFrame(frame);
            } else {
                System.out.println("Error: No se pudo capturar el marco.");
            }
        }
    }

    private void processFrame(Mat frame) {
        boolean markerDetected = markerDetector.detectMarker(frame, markerPoints);

        // Si se detecta el marcador, dibuja el cubo
        if (markerDetected && markerPoints.size() >= 4) {
            drawCube(frame, markerPoints);
        }

        // Convertir Mat a BufferedImage para mostrar en JLabel
        BufferedImage img = matToBufferedImage(frame);
        videoLabel.setIcon(new ImageIcon(img));
        videoLabel.repaint(); // Asegúrate de repintar el JLabel para mostrar la nueva imagen
    }

    private void drawCube(Mat frame, ArrayList<Point> markerPoints) {
        // Dibuja líneas para formar un cubo simple
        Point topLeft = markerPoints.get(0);
        Point topRight = markerPoints.get(1);
        Point bottomRight = markerPoints.get(2);
        Point bottomLeft = markerPoints.get(3);

        // Dibuja líneas del cubo
        Imgproc.line(frame, topLeft, topRight, new Scalar(255, 0, 0), 2);
        Imgproc.line(frame, topRight, bottomRight, new Scalar(255, 0, 0), 2);
        Imgproc.line(frame, bottomRight, bottomLeft, new Scalar(255, 0, 0), 2);
        Imgproc.line(frame, bottomLeft, topLeft, new Scalar(255, 0, 0), 2);

        // Dibuja la parte superior del cubo
        Imgproc.line(frame, new Point(topLeft.x, topLeft.y - 50), new Point(topRight.x, topRight.y - 50), new Scalar(255, 0, 0), 2);
        Imgproc.line(frame, new Point(bottomLeft.x, bottomLeft.y - 50), new Point(bottomRight.x, bottomRight.y - 50), new Scalar(255, 0, 0), 2);
        Imgproc.line(frame, new Point(topLeft.x, topLeft.y - 50), new Point(bottomLeft.x, bottomLeft.y), new Scalar(255, 0, 0), 2);
        Imgproc.line(frame, new Point(topRight.x, topRight.y - 50), new Point(bottomRight.x, bottomRight.y), new Scalar(255, 0, 0), 2);
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        // Definir el tipo de BufferedImage basado en el número de canales
        int type;
        if (mat.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        // Crear BufferedImage
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        // Convertir Mat a BufferedImage
        mat.get(0, 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData());

        return image;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RealidadAumentadaAPP().setVisible(true));
    }
}
