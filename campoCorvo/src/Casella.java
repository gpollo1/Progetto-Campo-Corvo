import javax.swing.*;
import java.awt.event.*;

public class Casella extends JButton {
    private boolean bomba;
    private int vicino;
    private boolean chiara;
    private boolean cliccata = false;
    private boolean isBandierina = false;

    private ImageIcon iconaNormale;
    private ImageIcon iconaHover;
    private ImageIcon bandierina;

    public Casella(boolean bomba, int vicino, ImageIcon iconaNormale, ImageIcon iconaHover, ImageIcon bandiera) {
        super(iconaNormale);
        this.bomba = bomba;
        this.vicino = vicino;
        this.iconaNormale = iconaNormale;
        this.iconaHover = iconaHover;
        this.bandierina = bandiera;
        setBorderPainted(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!cliccata && !isBandierina) {
                    setIcon(iconaHover);
                }
            }

            @Override
            public void mousePressed(MouseEvent e){
                if(SwingUtilities.isRightMouseButton(e) && !cliccata && !CampoMinato.end){
                    if(isBandierina){
                        isBandierina = false;
                        CampoMinato.aumentaBand();
                        setIcon(iconaNormale);
                    } else if(CampoMinato.bandierine > 0){
                        isBandierina = true;
                        CampoMinato.dimBand();
                        setIcon(bandierina);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!cliccata && !isBandierina) {
                    setIcon(iconaNormale);
                }
            }
        });
    }

    public boolean isBandierina() { return isBandierina; }
    public void setBandierina(boolean bandierina) { isBandierina = bandierina; }
    public boolean isBomba() { return bomba; }
    public void setBomba(boolean bomba) { this.bomba = bomba; }
    public int getVicino() { return vicino; }
    public void setVicino(int vicino) { this.vicino = vicino; }
    public boolean isChiara() { return chiara; }
    public void setChiara(boolean chiara) { this.chiara = chiara; }
    public boolean isCliccata() { return cliccata; }
    public void setCliccata(boolean cliccata) { this.cliccata = cliccata; }
}
