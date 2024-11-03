package CuartoEjercicio;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class GeneradorMarcador {
    public static void main(String[] args) {
        // Dimensiones del marcador
        int size = 300; // Puedes ajustar este tamaño según tus necesidades

        // Crear una imagen en blanco
        BufferedImage marker = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = marker.createGraphics();
        
        // Rellenar el fondo de blanco
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size, size);
        
        // Dibujar un cuadrado negro en el centro
        g.setColor(Color.BLACK);
        int squareSize = size / 2; // Tamaño del cuadrado
        int x = (size - squareSize) / 2; // Posición X para centrar
        int y = (size - squareSize) / 2; // Posición Y para centrar
        g.fillRect(x, y, squareSize, squareSize);
        
        // Liberar recursos
        g.dispose();

        // Mostrar el marcador
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JLabel(new ImageIcon(marker)));
        frame.pack();
        frame.setVisible(true);
    }
}
