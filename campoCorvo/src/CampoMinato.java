//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//CampoMinato implementa ActionListener in modo da avere un controllo su tutti gli eventi
public class CampoMinato implements ActionListener {
    public int righe;
    public int colonne;
    public int nBombe;
    private Casella[][] caselle;
    static JTextField jT;
    static JTextField countBand;
    public static int bandierine;
    public static boolean end = false;

    public JFrame frame;

    ImageIcon iconaChiara, iconaScura, iconaHover, iconaClick, iconaClickChiara, iconaBomba;

    public CampoMinato(int nColonne, int nRighe, int nBombe){
        colonne = nColonne;
        righe = nRighe;
        this.nBombe= nBombe;
        bandierine = nBombe;
        caselle  = new Casella[righe][colonne];
    }

    public void runCampoMinato() {

        int larg = 700;
        int alt = 700;
        JFrame f = new JFrame("Campo Minato");
        f.setBounds(100, 100, larg, alt);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setIconImage(new ImageIcon(Main.class.getResource("/immagini/Icona_campoMinato.png")).getImage());
        f.setContentPane(new Sfondo("medio.png"));
        f.setLayout(null);
        this.frame = f;

        JPanel p = new JPanel();
        p.setBounds(70, 0, larg - 150, alt - 150);
        p.setLayout(new GridLayout(righe, colonne));

        int larghezzaCasella = (larg - 150) / colonne;
        int altezzaCasella = (alt - 150) / righe;

        iconaChiara = scalaIcona("campoC.jpeg", larghezzaCasella, altezzaCasella);
        iconaScura = scalaIcona("campo.png", larghezzaCasella, altezzaCasella);
        iconaHover = scalaIcona("campoC+.jpeg", larghezzaCasella, altezzaCasella);
        iconaClick = scalaIcona("cliccato.png", larghezzaCasella, altezzaCasella);
        iconaClickChiara = scalaIcona("cliccatoC.PNG", larghezzaCasella, altezzaCasella);
        iconaBomba = scalaIcona("corvo.jpeg", larghezzaCasella, altezzaCasella);

        for (int k = 0; k < righe; k++) {
            for (int j = 0; j < colonne; j++) {
                boolean casellaChiara = (k + j) % 2 == 0;
                ImageIcon iconaCasellaNormale = casellaChiara ? iconaChiara : iconaScura;
                Casella casella = new Casella(false, 0, iconaCasellaNormale, iconaHover, scalaIcona("Bandiera.jpeg", larghezzaCasella, altezzaCasella));
                casella.setChiara(casellaChiara);
                casella.addActionListener(this);
                p.add(casella);
                caselle[k][j] = casella;
            }
        }

        inserisciBombe();
        leggiVicine();

        f.add(p);

        jT = new JTextField();
        jT.setBounds(100, alt - 130, 200, 40);
        jT.setEditable(false);
        f.add(jT);

        countBand = new JTextField("Bandierine disponibili: " + bandierine);
        countBand.setBounds(350, alt - 130, 200, 40);
        countBand.setForeground(Color.RED);
        countBand.setEditable(false);
        countBand.setFont(new Font("Arial", Font.BOLD, 15));
        f.add(countBand);

        f.setVisible(true);
    }

    private ImageIcon scalaIcona(String fileName, int larghezza, int altezza) {
        //imposta la grandezza giusta alle immagini
        ImageIcon icona = new ImageIcon(getClass().getResource("/immagini/" + fileName));
        Image img = icona.getImage().getScaledInstance(larghezza, altezza, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public static void dimBand() {
        if (bandierine > 0) {
            bandierine--;
            countBand.setText("Bandierine disponibili: " + bandierine);
        }
    }
    public static void aumentaBand() {
        if (bandierine < 40) {
            bandierine++;
            countBand.setText("Bandierine disponibili: " + bandierine);
        }
    }

    //inserisci le bome in posizioni casuali
    public void inserisciBombe() {
        int riga, colonna;
        for (int i = 0; i < nBombe; i++) {
            do {
                riga = (int) (Math.random() * righe);
                colonna = (int) (Math.random() * colonne);
            } while (caselle[riga][colonna].isBomba());
            caselle[riga][colonna].setBomba(true);
        }
    }


    //Scopre le posizioni senza bombe vicine nel casoin cui viene premuta una casella senza nessun numero
    private void scopriZonaVuota(int riga, int colonna) {
        //se fuori dai limiti esce
        if (riga < 0 || riga >= righe || colonna < 0 || colonna >= colonne) return;

        Casella c = caselle[riga][colonna];
        //se già cliccata o bandierina esce
        if (c.isCliccata() || c.isBandierina()) return;

        c.setCliccata(true);
        c.removeActionListener(this);
        //controlla se ha bombe vicine 8 caselle adiacenti e scopre quelle vuote su cui si richiama ricosivamente
        if (c.getVicino() == 0) {
            c.setIcon(c.isChiara() ? iconaClickChiara : iconaClick);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        scopriZonaVuota(riga + i, colonna + j);
                    }
                }
            }
        }
        //se numero mostra il numero specifico
        else {
            String path = "cliccato" + c.getVicino() + ".PNG";
            c.setIcon(scalaIcona(path, (1200 - 150) / colonne, (1200 - 150) / righe));
        }

        if (vittoria()) {
            vinto();
            end = true;
        }
    }

    //verifica se si ha vinto
    public boolean vittoria() {
        for (int i = 0; i < righe; i++) {
            for (int j = 0; j < colonne; j++) {
                if (!caselle[i][j].isBomba() && !caselle[i][j].isCliccata()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void vinto() {
        ImageIcon icon = scalaIcona("vittoria.png", 600, 300);
        JLabel labelImmagine = new JLabel(icon);
        labelImmagine.setBounds((frame.getWidth() - 600) / 2, (frame.getHeight() - 300) / 2, 600, 300);

        JLabel labelTesto = new JLabel("Hai vinto!!!", SwingConstants.CENTER);
        labelTesto.setBounds((frame.getWidth() - 600) / 2, (frame.getHeight() - 300) / 2 + 310, 600, 50);
        labelTesto.setFont(new Font("Jokerman", Font.BOLD, 36));
        labelTesto.setForeground(Color.black);

        frame.getLayeredPane().add(labelImmagine, JLayeredPane.POPUP_LAYER);
        frame.getLayeredPane().add(labelTesto, JLayeredPane.POPUP_LAYER);
        labelImmagine.setVisible(true);
        labelTesto.setVisible(true);

        new Timer(3000, e -> System.exit(0)).start();
    }

    public void setBombe() {
        for (int i = 0; i < righe; i++) {
            for (int j = 0; j < colonne; j++) {
                if (caselle[i][j].isBomba() && !caselle[i][j].isBandierina()) {
                    caselle[i][j].setIcon(iconaBomba);
                    caselle[i][j].setCliccata(true);
                }
                caselle[i][j].removeActionListener(this);
            }
        }

        ImageIcon icon = scalaIcona("gg.png", 600, 300);
        JLabel labelImmagine = new JLabel(icon);
        labelImmagine.setBounds((frame.getWidth() - 600) / 2, (frame.getHeight() - 300) / 2, 600, 300);

        JLabel labelTesto = new JLabel("Hai perso!!!", SwingConstants.CENTER);
        labelTesto.setBounds((frame.getWidth() - 600) / 2, (frame.getHeight() - 300) / 2 + 310, 600, 50);
        labelTesto.setFont(new Font("Jokerman", Font.BOLD, 36));
        labelTesto.setForeground(Color.black);

        frame.getLayeredPane().add(labelImmagine, JLayeredPane.POPUP_LAYER);
        frame.getLayeredPane().add(labelTesto, JLayeredPane.POPUP_LAYER);

        new Timer(3000, e -> System.exit(0)).start();
    }

    //Conta quante bombe ci sono vicine
    public void leggiVicine() {
        for (int i = 0; i < righe; i++) {
            for (int j = 0; j < colonne; j++) {
                caselle[i][j].setVicino(contaBombeVicini(i, j));
            }
        }
    }

    private int contaBombeVicini(int riga, int colonna) {
        int contatore = 0;
        for (int i = riga - 1; i <= riga + 1; i++) {
            for (int j = colonna - 1; j <= colonna + 1; j++) {
                if (i >= 0 && i < righe && j >= 0 && j < colonne && !(i == riga && j == colonna)) {
                    if (caselle[i][j].isBomba()) {
                        contatore++;
                    }
                }
            }
        }
        return contatore;
    }

    //legge tutti gli eventi
    @Override
    public void actionPerformed(ActionEvent e) {
        Object sorgente = e.getSource();
        for (int k = 0; k < righe; k++) {
            for (int j = 0; j < colonne; j++) {
                Casella c = caselle[k][j];
                if (sorgente.equals(c) && !c.isCliccata() && !c.isBandierina()) {
                    jT.setText("Riga: " + k + "    Colonna: " + j);
                    if (c.isBomba()) {
                        c.setIcon(iconaBomba);
                        end = true;
                        setBombe();
                    } else {
                        scopriZonaVuota(k, j);
                        if (vittoria()) {
                            vinto();
                            end = true;
                        }
                    }
                    return;
                }
            }
        }
    }
}