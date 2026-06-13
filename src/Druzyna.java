import java.util.ArrayList;
import java.util.List;

public class Druzyna extends Uczestnik {
    private List<GraczSolo> sklad; // Kompozycja i generyczna lista

    public Druzyna(String nazwa) {
        super(nazwa);
        this.sklad = new ArrayList<>();
    }

    // Metoda o zmiennej liczbie parametrów (varargs)
    public void dodajZawodnika(GraczSolo... nowi) {
        for(GraczSolo g : nowi) {
            this.sklad.add(g);
        }
    }

    @Override
    public String pobierzSzczegoly() {
        return "Drużyna: " + pobierzNazwe() + " (Skład: " + sklad.size() + " os.) | Punkty: " + pobierzPunkty();
    }
}