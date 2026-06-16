import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class OknoGlowne extends JFrame {
    private MenedzerTurnieju menedzer;
    private JTextArea obszarLogow;
    private ArchiwumTurniejow<Druzyna> archiwum;

    private List<Uczestnik> grupaA;
    private List<Uczestnik> grupaB;

    public OknoGlowne() {
        menedzer = MenedzerTurnieju.pobierzInstancje();
        archiwum = new ArchiwumTurniejow<>();

        setTitle("Menedżer Turnieju Drużynowego - Zaliczenie");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu menuPlik = new JMenu("Plik");
        JMenuItem itemZapisz = new JMenuItem("Zapisz stan (.dat)");
        JMenuItem itemWczytaj = new JMenuItem("Wczytaj stan (.dat)");

        menuPlik.add(itemZapisz);
        menuPlik.add(itemWczytaj);
        menuBar.add(menuPlik);
        setJMenuBar(menuBar);

        obszarLogow = new JTextArea();
        obszarLogow.setEditable(false);
        obszarLogow.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(obszarLogow), BorderLayout.CENTER);

        JPanel panelPrzykow = new JPanel();
        panelPrzykow.setLayout(new GridLayout(3, 4, 5, 5));

        JButton btnDodaj = new JButton("1. Dodaj Drużynę");
        JButton btnImport = new JButton("2. Import Drużyn (TXT)");
        JButton btnFazaGrupowa = new JButton("3. Losuj i Graj (Grupy)");
        JButton btnTabela = new JButton("4. Pokaż Tabele Grup");
        JButton btnDrabinka = new JButton("5. Finały (Drabinka)");
        JButton btnEksport = new JButton("Eksportuj Raport TXT");
        JButton btnUsun = new JButton("Usuń Drużynę");
        JButton btnCzysc = new JButton("Wyczyść Ekran");

        panelPrzykow.add(btnDodaj);
        panelPrzykow.add(btnImport);
        panelPrzykow.add(btnFazaGrupowa);
        panelPrzykow.add(btnTabela);
        panelPrzykow.add(btnDrabinka);
        panelPrzykow.add(btnEksport);
        panelPrzykow.add(btnUsun);
        panelPrzykow.add(btnCzysc);

        add(panelPrzykow, BorderLayout.SOUTH);

        btnCzysc.addActionListener(e -> obszarLogow.setText(""));

        itemZapisz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    menedzer.zapiszStan("turniej_zapis.dat");
                    log("Zapisano stan turnieju do pliku turniej_zapis.dat");
                } catch (Exception ex) {
                    log("Błąd zapisu: " + ex.getMessage());
                }
            }
        });

        itemWczytaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    menedzer.wczytajStan("turniej_zapis.dat");
                    // Pobieramy grupy przywrócone z pliku
                    grupaA = menedzer.pobierzGrupeA();
                    grupaB = menedzer.pobierzGrupeB();
                    log("Wczytano stan turnieju z pliku turniej_zapis.dat");
                } catch (Exception ex) {
                    log("Błąd odczytu: " + ex.getMessage());
                }
            }
        });

        btnDodaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nazwa = JOptionPane.showInputDialog("Podaj nazwę Drużyny:");
                if (nazwa == null || nazwa.trim().isEmpty()) return;

                if (!WalidatorDanych.sprawdzNazweDruzyny(nazwa)) {
                    JOptionPane.showMessageDialog(null, "Błędna nazwa drużyny! (3-30 znaków)");
                    return;
                }

                String nazwiska = JOptionPane.showInputDialog("Podaj dokładnie 5 nazwisk zawodników po przecinku:");
                if (nazwiska == null) return;

                String[] listaNazwisk = nazwiska.split(",");
                if (listaNazwisk.length != 5) {
                    JOptionPane.showMessageDialog(null, "Błąd! Musisz podać dokładnie 5 nazwisk!");
                    return;
                }

                Druzyna nowaDruzyna = new Druzyna(nazwa);
                GraczSolo[] zawodnicy = new GraczSolo[5];

                for (int i = 0; i < 5; i++) {
                    String nazwiskoZawodnika = listaNazwisk[i].trim();
                    if (!WalidatorDanych.sprawdzNazwisko(nazwiskoZawodnika)) {
                        JOptionPane.showMessageDialog(null, "Błąd walidacji! Złe nazwisko: " + nazwiskoZawodnika);
                        return;
                    }
                    zawodnicy[i] = new GraczSolo("Gracz", nazwiskoZawodnika);
                }

                nowaDruzyna.dodajZawodnika(zawodnicy);

                if (menedzer.dodajUczestnika(nowaDruzyna)) {
                    archiwum.dodajDoHistorii(nowaDruzyna);
                    log("Dodano Drużynę: " + nowaDruzyna.pobierzNazwe());
                } else {
                    JOptionPane.showMessageDialog(null, "Drużyna o takiej nazwie już istnieje!");
                }
            }
        });

        btnImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sciezka = JOptionPane.showInputDialog("Podaj nazwę pliku:", "druzyny.txt");
                if (sciezka != null) {
                    try {
                        int dodano = menedzer.importujDruzynyZTXT(sciezka);
                        log("Zaimportowano " + dodano + " drużyn z pliku " + sciezka);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Błąd odczytu pliku: " + sciezka);
                    }
                }
            }
        });

        btnTabela.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (grupaA == null || grupaB == null) {
                    log("Grupy nie zostały jeszcze wylosowane!");
                    return;
                }
                wypiszTabele("GRUPA A", grupaA);
                wypiszTabele("GRUPA B", grupaB);
            }
        });

        btnUsun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nazwa = JOptionPane.showInputDialog("Którą drużynę usunąć (nazwa):");
                if (nazwa != null && !nazwa.trim().isEmpty()) {
                    if (menedzer.usunDruzynne(nazwa)) {
                        log("Pomyślnie usunięto drużynę: " + nazwa);
                    } else {
                        log("Nie znaleziono drużyny o nazwie: " + nazwa);
                    }
                }
            }
        });

        btnFazaGrupowa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Uczestnik> wszyscy = new ArrayList<>(menedzer.pobierzWszystkich());
                if (wszyscy.size() < 4) {
                    JOptionPane.showMessageDialog(null, "Potrzeba minimum 4 drużyn do podziału na grupy!");
                    return;
                }

                for(Uczestnik u : wszyscy) u.resetujPunkty();

                Collections.shuffle(wszyscy);
                int srodek = wszyscy.size() / 2;

                // Użycie ArrayList dla bezpiecznej serializacji w przyszłości
                grupaA = new ArrayList<>(wszyscy.subList(0, srodek));
                grupaB = new ArrayList<>(wszyscy.subList(srodek, wszyscy.size()));
                menedzer.ustawGrupy(grupaA, grupaB);

                log("\n=== WYLOSOWANO GRUPY ===");
                log("GRUPA A: " + grupaA.size() + " drużyn");
                log("GRUPA B: " + grupaB.size() + " drużyn");
                log("=== ROZPOCZĘCIE MECZÓW W GRUPACH ===");

                try {
                    rozegrajMeczeWGrupie("GRUPA A", grupaA);
                    rozegrajMeczeWGrupie("GRUPA B", grupaB);
                    log("=== KONIEC FAZY GRUPOWEJ. SPRAWDŹ TABELE! ===");
                } catch (BlednyMeczException ex) {
                    log("Błąd krytyczny turnieju: " + ex.getMessage());
                }
            }
        });

        btnDrabinka.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (grupaA == null || grupaB == null) {
                    JOptionPane.showMessageDialog(null, "Najpierw rozegraj fazę grupową!");
                    return;
                }

                PriorityQueue<Uczestnik> tabA = menedzer.generujTabele(grupaA);
                PriorityQueue<Uczestnik> tabB = menedzer.generujTabele(grupaB);

                if (tabA.size() < 2 || tabB.size() < 2) {
                    JOptionPane.showMessageDialog(null, "Za mało drużyn na półfinały!");
                    return;
                }

                log("\n=========== FAZA PUCHAROWA ===========");
                Uczestnik a1 = tabA.poll();
                Uczestnik a2 = tabA.poll();
                Uczestnik b1 = tabB.poll();
                Uczestnik b2 = tabB.poll();

                try {
                    log(">> Półfinał 1: [1. Grupa A] " + a1.pobierzNazwe() + " vs [2. Grupa B] " + b2.pobierzNazwe());
                    Uczestnik finalista1 = grajDrabinke(a1, b2);

                    log(">> Półfinał 2: [1. Grupa B] " + b1.pobierzNazwe() + " vs [2. Grupa A] " + a2.pobierzNazwe());
                    Uczestnik finalista2 = grajDrabinke(b1, a2);

                    log("\n>> WIELKI FINAŁ: " + finalista1.pobierzNazwe() + " vs " + finalista2.pobierzNazwe());
                    Uczestnik mistrz = grajDrabinke(finalista1, finalista2);

                    log("★★★ ZWYCIĘZCA TURNIEJU: " + mistrz.pobierzNazwe().toUpperCase() + " ★★★\n");

                } catch (BlednyMeczException ex) {
                    log("Błąd generowania drabinki: " + ex.getMessage());
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

    private void wypiszTabele(String nazwaGrupy, List<Uczestnik> grupa) {
        PriorityQueue<Uczestnik> tabela = menedzer.generujTabele(grupa);
        log("\n--- TABELA " + nazwaGrupy + " ---");
        int poz = 1;
        while (!tabela.isEmpty()) {
            log(poz + ". " + tabela.poll().pobierzSzczegoly());
            poz++;
        }
    }

    private void rozegrajMeczeWGrupie(String nazwaGrupy, List<Uczestnik> grupa) throws BlednyMeczException {
        for (int i = 0; i < grupa.size(); i++) {
            for (int j = i + 1; j < grupa.size(); j++) {
                Mecz m = new Mecz(grupa.get(i), grupa.get(j));
                String n1 = m.pobierzUczestnika1().pobierzNazwe();
                String n2 = m.pobierzUczestnika2().pobierzNazwe();

                String[] opcje = {n1 + " Wygrywa", "REMIS", n2 + " Wygrywa"};
                int wybor = JOptionPane.showOptionDialog(null,
                        "Kto wygrał mecz w grupie?\n" + n1 + " vs " + n2,
                        nazwaGrupy + ": Wpisz wynik",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcje, opcje[0]);

                String wynikTekst;
                if (wybor == 0) wynikTekst = m.wpiszWynikRecznie(1);
                else if (wybor == 2) wynikTekst = m.wpiszWynikRecznie(2);
                else wynikTekst = m.wpiszWynikRecznie(0);

                log("[" + nazwaGrupy + "] " + wynikTekst);
                menedzer.logujMeczDoPliku("[" + nazwaGrupy + "] " + wynikTekst);
            }
        }
    }

    private Uczestnik grajDrabinke(Uczestnik u1, Uczestnik u2) throws BlednyMeczException {
        Mecz m = new Mecz(u1, u2);
        String[] opcje = {u1.pobierzNazwe() + " Wygrywa", u2.pobierzNazwe() + " Wygrywa"};
        int wybor = JOptionPane.showOptionDialog(null,
                "Faza Pucharowa (brak remisów)!\n" + u1.pobierzNazwe() + " vs " + u2.pobierzNazwe(),
                "Drabinka: Wpisz wynik",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opcje, opcje[0]);

        String wynikTekst;
        Uczestnik wygrany;
        if (wybor == 0) {
            wynikTekst = m.wpiszWynikRecznie(1);
            wygrany = u1;
        } else {
            wynikTekst = m.wpiszWynikRecznie(2);
            wygrany = u2;
        }

        log("[DRABINKA] " + wynikTekst);
        menedzer.logujMeczDoPliku("[DRABINKA] " + wynikTekst);
        return wygrany;
    }

    private void log(String wiadomosc) {
        obszarLogow.append(wiadomosc + "\n");
        obszarLogow.setCaretPosition(obszarLogow.getDocument().getLength());
    }
}