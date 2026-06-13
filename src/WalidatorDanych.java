public class WalidatorDanych {
    // Statyczna metoda z wykorzystaniem wyrażeń regularnych (RegEx)
    public static boolean sprawdzNick(String nick) {
        // Zezwala tylko na litery i cyfry, od 3 do 15 znaków
        return nick != null && nick.matches("^[a-zA-Z0-9]{3,15}$");
    }
}