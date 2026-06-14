public class GraczSolo extends Uczestnik {
    private String pseudonim;
    private transient boolean obecnyNaTurnieju;

    public GraczSolo(String nazwa, String pseudonim) {
        super(nazwa);
        this.pseudonim = pseudonim;
        this.obecnyNaTurnieju = true;
    }

    public String pobierzPseudonim() {
        return this.pseudonim;
    }

    @Override
    public String pobierzSzczegoly() {
        return "Gracz: " + pobierzNazwe() + " (" + pseudonim + ") | Punkty: " + pobierzPunkty();
    }
}