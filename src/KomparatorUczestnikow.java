import java.util.Comparator;

public class KomparatorUczestnikow implements Comparator<Uczestnik> {
    @Override
    public int compare(Uczestnik a, Uczestnik b) {
        if (a.pobierzPunkty() < b.pobierzPunkty()) return 1;
        if (a.pobierzPunkty() > b.pobierzPunkty()) return -1;
        return 0;
    }
}