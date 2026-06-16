public class Mecz {
    private Uczestnik u1;
    private Uczestnik u2;
    private boolean rozegrany;

    public Mecz(Uczestnik u1, Uczestnik u2) throws BlednyMeczException {
        if (u1 == null || u2 == null) {
            throw new BlednyMeczException("Uczestnicy nie mogą być puści!");
        }
        if (u1.pobierzNazwe().equals(u2.pobierzNazwe())) {
            throw new BlednyMeczException("Uczestnik nie może grać sam ze sobą!");
        }
        this.u1 = u1;
        this.u2 = u2;
        this.rozegrany = false;
    }

    public Uczestnik pobierzUczestnika1() { return u1; }
    public Uczestnik pobierzUczestnika2() { return u2; }

    public String wpiszWynikRecznie(int opcja) {
        if (rozegrany) return "Ten mecz już się odbył.";
        rozegrany = true;

        if (opcja == 1) {
            u1.dodajPunkty(3);
            return "WYGRANA: " + u1.pobierzNazwe() + " pokonuje " + u2.pobierzNazwe() + " [3 pkt]";
        } else if (opcja == 2) {
            u2.dodajPunkty(3);
            return "WYGRANA: " + u2.pobierzNazwe() + " pokonuje " + u1.pobierzNazwe() + " [3 pkt]";
        } else {
            u1.dodajPunkty(1);
            u2.dodajPunkty(1);
            return "REMIS: " + u1.pobierzNazwe() + " remisuje z " + u2.pobierzNazwe() + " [po 1 pkt]";
        }
    }
}