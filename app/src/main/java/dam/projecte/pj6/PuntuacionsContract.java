package dam.projecte.pj6;

import android.provider.BaseColumns;

public final class PuntuacionsContract {
    public PuntuacionsContract(){}

    public static abstract class TaulaPuntuacio implements BaseColumns{
        public static final String NOM_TAULA = "puntuacions";
        public static final String COLUMNA_TEMPS = "temps";
        public static final String COLUMNA_DATA = "data";
        public static final String COLUMNA_NULL = "null";
    }
}
