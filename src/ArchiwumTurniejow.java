import java.util.ArrayList;
import java.util.List;

public class ArchiwumTurniejow<T extends Uczestnik> {
    private List<T> historia;

    public ArchiwumTurniejow() {
        this.historia = new ArrayList<>();
    }

    public void dodajDoHistorii(T obiekt) {
        historia.add(obiekt);
    }

    public List<T> pobierzHistorie() {
        return historia;
    }
}