import java.util.ArrayList;
import java.util.List;

public class Druzyna extends Uczestnik {
    private List<GraczSolo> sklad;

    public Druzyna(String nazwa) {
        super(nazwa);
        this.sklad = new ArrayList<>();
    }

    public void dodajZawodnika(GraczSolo... nowi) {
        for(GraczSolo g : nowi) {
            this.sklad.add(g);
        }
    }

    @Override
    public String pobierzSzczegoly() {
        return "Drużyna: " + pobierzNazwe() + " | Punkty: " + pobierzPunkty();
    }
}