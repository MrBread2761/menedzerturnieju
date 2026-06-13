import java.util.Comparator;

// Klasa implementująca interfejs Comparator do sterowania sortowaniem w PriorityQueue
public class KomparatorUczestnikow implements Comparator<Uczestnik> {
    @Override
    public int compare(Uczestnik a, Uczestnik b) {
        if (a.pobierzPunkty() < b.pobierzPunkty()) return 1;
        if (a.pobierzPunkty() > b.pobierzPunkty()) return -1;
        return 0; // Remis
    }
}