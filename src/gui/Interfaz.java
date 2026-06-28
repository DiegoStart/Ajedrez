import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;

public class Interfaz extends JFrame {
    private JPanel panel;
    private JPanel tablero;

    public Interfaz() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Ajedrez");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        iniciarComponentes();
    }

    public void iniciarComponentes() {
        agregarPanel();
        agregarCasillas();
    }

    public void agregarPanel() {
        // Panel principal
        panel = new JPanel();
        panel.setBackground(Color.ORANGE);
        panel.setLayout(null);
        this.add(panel);

        // Panel del tablero
        tablero = new JPanel();
        tablero.setBackground(Color.RED);
        tablero.setLayout(null);
        tablero.setBounds(460, 109, 616, 576);
        panel.add(tablero);
    }

    public void agregarCasillas() {
        int ancho = 77;
        int alto = 72;
        Color blanco = Color.white;
        Color negro = Color.black;

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                JLabel casilla = new JLabel();
                casilla.setBounds(columna * ancho, fila * alto, ancho, alto);

                if ((fila + columna) % 2 == 0) {
                    casilla.setBackground(blanco);
                } else {
                    casilla.setBackground(negro);
                }
                casilla.setOpaque(true);
                tablero.add(casilla);
            }
        }
    }

    public void agregarEvento() {

    }
}