import java.io.Serializable;

// Klasa abstrakcyjna implementująca interfejsy
public abstract class Uczestnik implements Serializable, Zarzadzalny {
    private String nazwa;
    private int punkty;

    public Uczestnik(String nazwa) {
        this.nazwa = nazwa;
        this.punkty = 0;
    }

    public abstract String pobierzSzczegoly();

    public String pobierzNazwe() { return nazwa; }
    public int pobierzPunkty() { return punkty; }

    public void dodajPunkty(int ilosc) {
        this.punkty += ilosc;
    }

    @Override
    public void resetujPunkty() {
        this.punkty = 0;
    }

    @Override
    public String pobierzStatus() {
        return "Aktywny";
    }
}