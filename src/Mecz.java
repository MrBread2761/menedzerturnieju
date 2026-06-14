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

    public String symulujMecz() {
        if (rozegrany) return "Ten mecz już się odbył.";

        int wynik1 = (int)(Math.random() * 5);
        int wynik2 = (int)(Math.random() * 5);

        rozegrany = true;

        if (wynik1 > wynik2) {
            u1.dodajPunkty(3);
            return "WYGRANA: " + u1.pobierzNazwe() + " pokonuje " + u2.pobierzNazwe() + " (" + wynik1 + ":" + wynik2 + ") [+3 pkt]";
        } else if (wynik1 < wynik2) {
            u2.dodajPunkty(3);
            return "WYGRANA: " + u2.pobierzNazwe() + " pokonuje " + u1.pobierzNazwe() + " (" + wynik2 + ":" + wynik1 + ") [+3 pkt]";
        } else {
            u1.dodajPunkty(1);
            u2.dodajPunkty(1);
            return "REMIS: " + u1.pobierzNazwe() + " remisuje z " + u2.pobierzNazwe() + " (" + wynik1 + ":" + wynik2 + ") [+1 pkt dla obu]";
        }
    }
}