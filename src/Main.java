import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Uruchomienie aplikacji okienkowej zgodnie z dobrymi praktykami Javy
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                OknoGlowne okno = new OknoGlowne();
                okno.setVisible(true);
            }
        });
    }
}