import java.io.*;
import java.util.*;

public class MenedzerTurnieju {
    private static MenedzerTurnieju instancja = null;
    private List<Uczestnik> listaUczestnikow;
    private final HashSet<String> zajeteNazwy;
    private final HashMap<String, Uczestnik> mapaUczestnikow;

    // Nowe zmienne do zapamiętywania podziału na grupy podczas zapisu
    private List<Uczestnik> grupaA;
    private List<Uczestnik> grupaB;

    private MenedzerTurnieju() {
        listaUczestnikow = new ArrayList<>();
        zajeteNazwy = new HashSet<>();
        mapaUczestnikow = new HashMap<>();
        grupaA = null;
        grupaB = null;
    }

    public static MenedzerTurnieju pobierzInstancje() {
        if (instancja == null) {
            instancja = new MenedzerTurnieju();
        }
        return instancja;
    }

    public void ustawGrupy(List<Uczestnik> a, List<Uczestnik> b) {
        this.grupaA = a;
        this.grupaB = b;
    }

    public List<Uczestnik> pobierzGrupeA() { return grupaA; }
    public List<Uczestnik> pobierzGrupeB() { return grupaB; }

    public boolean dodajUczestnika(Uczestnik u) {
        if (zajeteNazwy.contains(u.pobierzNazwe())) {
            return false;
        }
        listaUczestnikow.add(u);
        zajeteNazwy.add(u.pobierzNazwe());
        mapaUczestnikow.put(u.pobierzNazwe(), u);
        return true;
    }

    public List<Uczestnik> pobierzWszystkich() {
        return listaUczestnikow;
    }

    public boolean usunDruzynne(String nazwa) {
        if (mapaUczestnikow.containsKey(nazwa)) {
            Uczestnik u = mapaUczestnikow.remove(nazwa);
            listaUczestnikow.remove(u);
            zajeteNazwy.remove(nazwa);
            return true;
        }
        return false;
    }

    public PriorityQueue<Uczestnik> generujTabele(List<Uczestnik> grupa) {
        PriorityQueue<Uczestnik> kolejka = new PriorityQueue<>(new KomparatorUczestnikow());
        kolejka.addAll(grupa);
        return kolejka;
    }

    public void logujMeczDoPliku(String wynik) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("historia_meczow.txt", true));
            bw.write(wynik);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            System.err.println("Błąd zapisu do pliku logów: " + e.getMessage());
        }
    }

    public int importujDruzynyZTXT(String sciezka) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(sciezka));
        String linia;
        int dodano = 0;
        while ((linia = br.readLine()) != null) {
            String[] czesci = linia.split(";");
            if (czesci.length == 6 && WalidatorDanych.sprawdzNazweDruzyny(czesci[0].trim())) {
                Druzyna d = new Druzyna(czesci[0].trim());
                GraczSolo[] zawodnicy = new GraczSolo[5];
                boolean poprawni = true;

                for (int i = 1; i <= 5; i++) {
                    String[] daneGracza = czesci[i].split(",");
                    if (daneGracza.length == 2 && WalidatorDanych.sprawdzNazwisko(daneGracza[1].trim())) {
                        zawodnicy[i-1] = new GraczSolo(daneGracza[0].trim(), daneGracza[1].trim());
                    } else {
                        poprawni = false; break;
                    }
                }

                if (poprawni) {
                    d.dodajZawodnika(zawodnicy);
                    if (dodajUczestnika(d)) {
                        dodano++;
                    }
                }
            }
        }
        br.close();
        return dodano;
    }

    public void eksportujRaportTxt(String sciezka) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(sciezka));
        writer.write("--- RAPORT KOŃCOWY TURNIEJU (DRUŻYNY) ---");
        writer.newLine();
        PriorityQueue<Uczestnik> tabela = generujTabele(listaUczestnikow);
        int poz = 1;
        while (!tabela.isEmpty()) {
            writer.write(poz + ". " + tabela.poll().pobierzSzczegoly());
            writer.newLine();
            poz++;
        }
        writer.close();
    }

    public void zapiszStan(String sciezka) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(sciezka));
        out.writeObject(listaUczestnikow);
        out.writeObject(grupaA);
        out.writeObject(grupaB);
        out.close();
    }

    @SuppressWarnings("unchecked")
    public void wczytajStan(String sciezka) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(sciezka));
        listaUczestnikow = (List<Uczestnik>) in.readObject();
        try {
            grupaA = (List<Uczestnik>) in.readObject();
            grupaB = (List<Uczestnik>) in.readObject();
        } catch(Exception e) {
            grupaA = null;
            grupaB = null;
        }
        in.close();

        zajeteNazwy.clear();
        mapaUczestnikow.clear();
        for (Uczestnik u : listaUczestnikow) {
            zajeteNazwy.add(u.pobierzNazwe());
            mapaUczestnikow.put(u.pobierzNazwe(), u);
        }
    }
}