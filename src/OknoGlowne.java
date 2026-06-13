import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.PriorityQueue;

// Klasa dziedzicząca po JFrame tworząca warstwę widoku
public class OknoGlowne extends JFrame {
    private MenedzerTurnieju menedzer;
    private JTextArea obszarLogow;
    private ArchiwumTurniejow<GraczSolo> archiwum; // Wykorzystanie klasy generycznej

    public OknoGlowne() {
        menedzer = MenedzerTurnieju.pobierzInstancje();
        archiwum = new ArchiwumTurniejow<>();

        setTitle("Menedżer E-sportowy (Zaliczenie Java)");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel górny - logi
        obszarLogow = new JTextArea();
        obszarLogow.setEditable(false);
        obszarLogow.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(obszarLogow), BorderLayout.CENTER);

        // Panel dolny - przyciski
        JPanel panelPrzykow = new JPanel();
        panelPrzykow.setLayout(new GridLayout(2, 3, 5, 5));

        JButton btnDodaj = new JButton("Dodaj Gracza");
        JButton btnTabela = new JButton("Pokaż Tabelę");
        JButton btnUsun = new JButton("Usuń Gracza");
        JButton btnMecz = new JButton("Rozegraj Mecz (Test)");
        JButton btnZapisz = new JButton("Zapisz do Pliku");
        JButton btnWczytaj = new JButton("Wczytaj z Pliku");

        panelPrzykow.add(btnDodaj);
        panelPrzykow.add(btnTabela);
        panelPrzykow.add(btnUsun);
        panelPrzykow.add(btnMecz);
        panelPrzykow.add(btnZapisz);
        panelPrzykow.add(btnWczytaj);

        add(panelPrzykow, BorderLayout.SOUTH);

        // Event Listeners - obsługa zdarzeń
        btnDodaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String imie = JOptionPane.showInputDialog("Podaj imię:");
                if (imie == null || imie.trim().isEmpty()) return;

                String nick = JOptionPane.showInputDialog("Podaj pseudonim (tylko litery/cyfry, 3-15 znaków):");
                if (nick == null) return;

                if (WalidatorDanych.sprawdzNick(nick)) {
                    GraczSolo nowy = new GraczSolo(imie, nick);
                    menedzer.dodajUczestnika(nowy);
                    archiwum.dodajDoHistorii(nowy);
                    log("Dodano gracza: " + nowy.pobierzPseudonim());
                } else {
                    JOptionPane.showMessageDialog(null, "Błąd walidacji! Nick nie spełnia kryteriów RegEx.");
                }
            }
        });

        btnTabela.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PriorityQueue<Uczestnik> tabela = menedzer.generujTabele();
                log("\n--- TABELA WYNIKÓW ---");
                int poz = 1;
                while (!tabela.isEmpty()) {
                    log(poz + ". " + tabela.poll().pobierzSzczegoly());
                    poz++;
                }
            }
        });

        btnUsun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nazwa = JOptionPane.showInputDialog("Kogo usunąć (imię):");
                if (nazwa != null && !nazwa.trim().isEmpty()) {
                    if (menedzer.usunGracza(nazwa)) {
                        log("Pomyślnie usunięto gracza: " + nazwa);
                    } else {
                        log("Nie znaleziono gracza o imieniu: " + nazwa);
                    }
                }
            }
        });

        btnMecz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (menedzer.pobierzWszystkich().size() < 2) {
                        throw new BlednyMeczException("Za mało graczy w bazie (potrzeba min. 2)!");
                    }
                    Uczestnik u1 = menedzer.pobierzWszystkich().get(0);
                    Uczestnik u2 = menedzer.pobierzWszystkich().get(1);

                    Mecz m = new Mecz(u1, u2); // Ryzyko rzucenia wyjątku
                    m.wpiszWynik(3, 0); // Przypisanie 3 pkt pierwszemu graczowi
                    log("Rozegrano mecz! " + u1.pobierzNazwe() + " zdobywa 3 pkt przeciwko " + u2.pobierzNazwe());
                } catch (BlednyMeczException ex) {
                    JOptionPane.showMessageDialog(null, ex.toString(), "Złapano Wyjątek", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnZapisz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    menedzer.zapiszStan("turniej_zapis.dat");
                    log("Zapisano stan turnieju na dysku (Serializacja).");
                } catch (Exception ex) {
                    log("Błąd zapisu: " + ex.getMessage());
                }
            }
        });

        btnWczytaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    menedzer.wczytajStan("turniej_zapis.dat");
                    log("Wczytano stan turnieju z dysku (Deserializacja).");
                } catch (Exception ex) {
                    log("Błąd odczytu: " + ex.getMessage());
                }
            }
        });
    }

    private void log(String wiadomosc) {
        obszarLogow.append(wiadomosc + "\n");
    }
}