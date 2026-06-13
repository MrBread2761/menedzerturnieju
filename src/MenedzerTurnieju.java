import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

// Wzorzec Singleton zarządzający logiką i danymi
public class MenedzerTurnieju {
    private static MenedzerTurnieju instancja = null;
    private List<Uczestnik> listaUczestnikow;

    private MenedzerTurnieju() {
        listaUczestnikow = new ArrayList<>();
    }

    public static MenedzerTurnieju pobierzInstancje() {
        if (instancja == null) {
            instancja = new MenedzerTurnieju();
        }
        return instancja;
    }

    public void dodajUczestnika(Uczestnik u) {
        listaUczestnikow.add(u);
    }

    public List<Uczestnik> pobierzWszystkich() {
        return listaUczestnikow;
    }

    // Bezpieczne usuwanie obiektu z kolekcji za pomocą Iteratora
    public boolean usunGracza(String nazwa) {
        Iterator<Uczestnik> it = listaUczestnikow.iterator();
        while (it.hasNext()) {
            Uczestnik u = it.next();
            if (u.pobierzNazwe().equals(nazwa)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    // Generowanie posortowanej struktury za pomocą PriorityQueue i naszego Komparatora
    public PriorityQueue<Uczestnik> generujTabele() {
        PriorityQueue<Uczestnik> kolejka = new PriorityQueue<>(new KomparatorUczestnikow());
        for (Uczestnik u : listaUczestnikow) {
            kolejka.add(u);
        }
        return kolejka;
    }

    // Serializacja obiektów
    public void zapiszStan(String sciezka) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(sciezka));
        out.writeObject(listaUczestnikow);
        out.close();
    }

    // Deserializacja obiektów
    @SuppressWarnings("unchecked")
    public void wczytajStan(String sciezka) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(sciezka));
        listaUczestnikow = (List<Uczestnik>) in.readObject();
        in.close();
    }
}