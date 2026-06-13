public class Mecz {
    private Uczestnik u1;
    private Uczestnik u2;
    private boolean rozegrany;

    // Konstruktor deklarujący, że może rzucić wyjątek
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

    public void wpiszWynik(int p1, int p2) {
        if (!rozegrany) {
            u1.dodajPunkty(p1);
            u2.dodajPunkty(p2);
            rozegrany = true;
        }
    }
}