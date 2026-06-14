import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                OknoGlowne okno = new OknoGlowne();
                okno.setVisible(true);
            }
        });
    }
}