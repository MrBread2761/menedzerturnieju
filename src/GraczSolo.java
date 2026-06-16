public class GraczSolo extends Uczestnik {
    private String nazwisko;
    private transient boolean obecnyNaTurnieju;

    public GraczSolo(String nazwa, String nazwisko) {
        super(nazwa);
        this.nazwisko = nazwisko;
        this.obecnyNaTurnieju = true;
    }

    public String pobierzNazwisko() {
        return this.nazwisko;
    }

    @Override
    public String pobierzSzczegoly() {
        return "Zawodnik: " + pobierzNazwe() + " " + nazwisko + " | Punkty: " + pobierzPunkty();
    }
}