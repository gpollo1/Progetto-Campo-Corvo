import javax.swing.*;
import java.awt.*;

public class Sfondo extends JPanel {
    private Image backgroundImage;

    public Sfondo(String imageName) {
        backgroundImage = new ImageIcon(getClass().getResource("/Immagini/" + imageName)).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
