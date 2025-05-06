import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame menù = new JFrame("Menù");
        menù.setBounds(100, 100, 800, 300);
        menù.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menù.setIconImage(new ImageIcon(Main.class.getResource("/immagini/Icona_campoMinato.png")).getImage());
        menù.setContentPane(new Sfondo("medio.png"));
        JLabel label = new JLabel("Seleziona la difficoltà");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        menù.add(label);
        JPanel panel = new JPanel(new FlowLayout());
        JButton facile = new JButton("Facile (80 caselle 10 bombe)");
        facile.addActionListener(e -> {
            CampoMinato campoMinato = new CampoMinato(10,8,10);
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run() {
                    //Fa partire campo minato con le difficoltà selezionate
                    campoMinato.runCampoMinato();
                }
            });
            menù.setVisible(false);
        });
        JButton media = new JButton("Media (252 caselle 40 bombe)");
        media.addActionListener(e -> {
            CampoMinato campoMinato = new CampoMinato(18,14,40);
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run() {
                    campoMinato.runCampoMinato();
                }
            });
            menù.setVisible(false);
        });
        JButton difficile = new JButton("Difficile (480 caselle 99 bombe)");
        difficile.addActionListener(e -> {
            CampoMinato campoMinato = new CampoMinato(24,20,99);
            //L'invokeLater è più utili quando creiamo una finestra con tanti bottoni
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run() {
                    campoMinato.runCampoMinato();
                }
            });
            menù.setVisible(false);
        });
        panel.add(facile);
        panel.add(media);
        panel.add(difficile);
        menù.add(panel);
        menù.setVisible(true);
    }
}
