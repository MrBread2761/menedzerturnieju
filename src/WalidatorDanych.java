public class WalidatorDanych {
    public static boolean sprawdzNick(String nick) {
        return nick != null && nick.matches("^[a-zA-Z0-9]{3,15}$");
    }
}