package CuartoEjercicio;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class DetectorRealidadAumentada {

    public boolean detectMarker(Mat frame, ArrayList<Point> markerPoints) {
        // Convertir la imagen a escala de grises
        Mat gray = new Mat();
        Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);

        // Aplicar un desenfoque para suavizar la imagen
        Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);

        // Aplicar detección de bordes
        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, 100, 200);

        // Encontrar contornos en la imagen
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        // Recorrer los contornos para encontrar un cuadrado
        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area > 1000) { // Filtrar por área mínima
                MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());
                double perimeter = Imgproc.arcLength(contour2f, true);
                MatOfPoint2f approx = new MatOfPoint2f();
                Imgproc.approxPolyDP(contour2f, approx, 0.02 * perimeter, true);

                // Verificar si el contorno tiene 4 vértices (es un cuadrado)
                if (approx.total() == 4) {
                    Rect boundingBox = Imgproc.boundingRect(approx);
                    markerPoints.clear(); // Limpiar puntos anteriores
                    markerPoints.add(new Point(boundingBox.x, boundingBox.y));
                    markerPoints.add(new Point(boundingBox.x + boundingBox.width, boundingBox.y));
                    markerPoints.add(new Point(boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height));
                    markerPoints.add(new Point(boundingBox.x, boundingBox.y + boundingBox.height));

                    // Dibuja un rectángulo alrededor del marcador detectado
                    Imgproc.rectangle(frame, boundingBox.tl(), boundingBox.br(), new Scalar(255, 0, 0), 2);

                    return true; // Marcador detectado
                }
            }
        }
        return false; // No se detectó ningún marcador
    }
}
