import java.io.*;
import java.util.*;

public class MenedzerTurnieju {
    private static MenedzerTurnieju instancja = null;
    private List<Uczestnik> listaUczestnikow;
    private HashSet<String> zajeteNazwy;
    private HashMap<String, Uczestnik> mapaUczestnikow;

    private MenedzerTurnieju() {
        listaUczestnikow = new ArrayList<>();
        zajeteNazwy = new HashSet<>();
        mapaUczestnikow = new HashMap<>();
    }

    public static MenedzerTurnieju pobierzInstancje() {
        if (instancja == null) {
            instancja = new MenedzerTurnieju();
        }
        return instancja;
    }

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

    public boolean usunGracza(String nazwa) {
        if (mapaUczestnikow.containsKey(nazwa)) {
            Uczestnik u = mapaUczestnikow.remove(nazwa);
            listaUczestnikow.remove(u);
            zajeteNazwy.remove(nazwa);
            return true;
        }
        return false;
    }

    public PriorityQueue<Uczestnik> generujTabele() {
        PriorityQueue<Uczestnik> kolejka = new PriorityQueue<>(new KomparatorUczestnikow());
        for (Uczestnik u : listaUczestnikow) {
            kolejka.add(u);
        }
        return kolejka;
    }

    public void eksportujRaportTxt(String sciezka) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(sciezka));
        writer.write("--- RAPORT KOŃCOWY TURNIEJU ---");
        writer.newLine();
        PriorityQueue<Uczestnik> tabela = generujTabele();
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
        out.close();
    }

    @SuppressWarnings("unchecked")
    public void wczytajStan(String sciezka) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(sciezka));
        listaUczestnikow = (List<Uczestnik>) in.readObject();
        in.close();
        zajeteNazwy.clear();
        mapaUczestnikow.clear();
        for (Uczestnik u : listaUczestnikow) {
            zajeteNazwy.add(u.pobierzNazwe());
            mapaUczestnikow.put(u.pobierzNazwe(), u);
        }
    }
}