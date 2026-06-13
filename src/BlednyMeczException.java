// Własna klasa wyjątku dziedzicząca po bazowej klasie Exception
public class BlednyMeczException extends Exception {
    private String powod;

    public BlednyMeczException(String powod) {
        this.powod = powod;
    }

    @Override
    public String toString() {
        return "Błąd Turnieju: " + powod;
    }
}