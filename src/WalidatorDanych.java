public class WalidatorDanych {
    public static boolean sprawdzNazwisko(String nazwisko) {
        return nazwisko != null && nazwisko.matches("^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]{3,20}$");
    }

    public static boolean sprawdzNazweDruzyny(String nazwa) {
        return nazwa != null && nazwa.matches("^[a-zA-Z0-9 ąćęłńóśźżĄĆĘŁŃÓŚŹŻ]{3,30}$");
    }
}