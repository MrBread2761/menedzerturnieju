import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.PriorityQueue;

public class OknoGlowne extends JFrame {
    private MenedzerTurnieju menedzer;
    private JTextArea obszarLogow;
    private ArchiwumTurniejow<GraczSolo> archiwum;

    public OknoGlowne() {
        menedzer = MenedzerTurnieju.pobierzInstancje();
        archiwum = new ArchiwumTurniejow<>();

        setTitle("Menedżer E-sportowy");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        obszarLogow = new JTextArea();
        obszarLogow.setEditable(false);
        obszarLogow.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(obszarLogow), BorderLayout.CENTER);

        JPanel panelPrzykow = new JPanel();
        panelPrzykow.setLayout(new GridLayout(3, 3, 5, 5));

        JButton btnDodaj = new JButton("Dodaj Gracza");
        JButton btnTabela = new JButton("Pokaż Tabelę");
        JButton btnUsun = new JButton("Usuń Gracza");
        JButton btnFazaGrupowa = new JButton("Symuluj Fazę Grupową");
        JButton btnDrabinka = new JButton("Drabinka (Finały)");
        JButton btnZapisz = new JButton("Zapisz do Pliku");
        JButton btnWczytaj = new JButton("Wczytaj z Pliku");
        JButton btnEksport = new JButton("Eksportuj Raport TXT");
        JButton btnCzysc = new JButton("Wyczyść Ekran");

        panelPrzykow.add(btnDodaj);
        panelPrzykow.add(btnFazaGrupowa);
        panelPrzykow.add(btnDrabinka);
        panelPrzykow.add(btnTabela);
        panelPrzykow.add(btnUsun);
        panelPrzykow.add(btnCzysc);
        panelPrzykow.add(btnZapisz);
        panelPrzykow.add(btnWczytaj);
        panelPrzykow.add(btnEksport);

        add(panelPrzykow, BorderLayout.SOUTH);

        btnCzysc.addActionListener(e -> obszarLogow.setText(""));

        btnDodaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String imie = JOptionPane.showInputDialog("Podaj imię:");
                if (imie == null || imie.trim().isEmpty()) return;

                String nick = JOptionPane.showInputDialog("Podaj pseudonim (tylko litery/cyfry, 3-15 znaków):");
                if (nick == null) return;

                if (WalidatorDanych.sprawdzNick(nick)) {
                    GraczSolo nowy = new GraczSolo(imie, nick);
                    if (menedzer.dodajUczestnika(nowy)) {
                        archiwum.dodajDoHistorii(nowy);
                        log("Dodano gracza: " + nowy.pobierzPseudonim());
                    } else {
                        JOptionPane.showMessageDialog(null, "Uczestnik o takim imieniu już istnieje!");
                    }
                } else {
                    // Zmieniony, w 100% czysty i profesjonalny komunikat błędu
                    JOptionPane.showMessageDialog(null, "Błąd! Pseudonim musi mieć od 3 do 15 znaków i składać się wyłącznie z liter lub cyfr.");
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

        btnFazaGrupowa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Uczestnik> wszyscy = menedzer.pobierzWszystkich();
                if (wszyscy.size() < 3) {
                    JOptionPane.showMessageDialog(null, "Potrzeba minimum 3 graczy do fazy grupowej!");
                    return;
                }

                log("\n=== ROZPOCZĘCIE FAZY GRUPOWEJ (KAŻDY Z KAŻDYM) ===");
                try {
                    for (int i = 0; i < wszyscy.size(); i++) {
                        for (int j = i + 1; j < wszyscy.size(); j++) {
                            Mecz m = new Mecz(wszyscy.get(i), wszyscy.get(j));
                            log(m.symulujMecz());
                        }
                    }
                    log("=== KONIEC FAZY GRUPOWEJ. SPRAWDŹ TABELĘ! ===");
                } catch (BlednyMeczException ex) {
                    log("Błąd krytyczny turnieju: " + ex.getMessage());
                }
            }
        });

        btnDrabinka.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PriorityQueue<Uczestnik> tabela = menedzer.generujTabele();
                if (tabela.size() < 4) {
                    JOptionPane.showMessageDialog(null, "Za mało graczy na półfinały! Wymagane minimum 4 osoby.");
                    return;
                }

                log("\n=== FAZA PUCHAROWA: PÓŁFINAŁY ===");
                Uczestnik m1 = tabela.poll();
                Uczestnik m2 = tabela.poll();
                Uczestnik m3 = tabela.poll();
                Uczestnik m4 = tabela.poll();

                try {
                    log("Półfinał 1: " + m1.pobierzNazwe() + " vs " + m4.pobierzNazwe());
                    Mecz pol1 = new Mecz(m1, m4);
                    while (pol1.symulujMecz().contains("REMIS")) {
                        m1.dodajPunkty(-1); m4.dodajPunkty(-1);
                        pol1 = new Mecz(m1, m4);
                    }
                    log(" -> Awans: " + (m1.pobierzPunkty() > m4.pobierzPunkty() ? m1.pobierzNazwe() : m4.pobierzNazwe()));

                    log("Półfinał 2: " + m2.pobierzNazwe() + " vs " + m3.pobierzNazwe());
                    Mecz pol2 = new Mecz(m2, m3);
                    while (pol2.symulujMecz().contains("REMIS")) {
                        m2.dodajPunkty(-1); m3.dodajPunkty(-1);
                        pol2 = new Mecz(m2, m3);
                    }
                    log(" -> Awans: " + (m2.pobierzPunkty() > m3.pobierzPunkty() ? m2.pobierzNazwe() : m3.pobierzNazwe()));

                    log("\n=== WIELKI FINAŁ ===");
                    Uczestnik finalista1 = m1.pobierzPunkty() > m4.pobierzPunkty() ? m1 : m4;
                    Uczestnik finalista2 = m2.pobierzPunkty() > m3.pobierzPunkty() ? m2 : m3;

                    Mecz wielkiFinal = new Mecz(finalista1, finalista2);
                    while (wielkiFinal.symulujMecz().contains("REMIS")) {
                        finalista1.dodajPunkty(-1); finalista2.dodajPunkty(-1);
                        wielkiFinal = new Mecz(finalista1, finalista2);
                    }

                    Uczestnik zwyciezca = finalista1.pobierzPunkty() > finalista2.pobierzPunkty() ? finalista1 : finalista2;
                    log("★★★ ZWYCIĘZCA TURNIEJU: " + zwyciezca.pobierzNazwe().toUpperCase() + " ★★★");

                } catch (BlednyMeczException ex) {
                    log("Błąd generowania drabinki: " + ex.getMessage());
                }
            }
        });

        btnZapisz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    menedzer.zapiszStan("turniej_zapis.dat");
                    log("Zapisano stan turnieju na dysku.");
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
                    log("Wczytano stan turnieju z dysku.");
                } catch (Exception ex) {
                    log("Błąd odczytu: " + ex.getMessage());
                }
            }
        });

        btnEksport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    menedzer.eksportujRaportTxt("raport_turnieju.txt");
                    log("Wyeksportowano raport do pliku raport_turnieju.txt");
                } catch (Exception ex) {
                    log("Błąd eksportu: " + ex.getMessage());
                }
            }
        });
    }

    private void log(String wiadomosc) {
        obszarLogow.append(wiadomosc + "\n");
    }
}