package dam.projecte.pj6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class Puntuacions extends SQLiteOpenHelper {
    private static final String TIPUS_ENTER=" INTEGER";
    private static final String TIPUS_TEXT = " TEXT";
    private static final String SEPARADOR_COMA = ",";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Puntuacions.db";

    private static final String SQL_CREACIO_TAULA =
            "CREATE TABLE " + PuntuacionsContract.TaulaPuntuacio.NOM_TAULA + " " +
                    "(" + PuntuacionsContract.TaulaPuntuacio._ID +
                    " INTEGER PRIMARY KEY," +PuntuacionsContract.TaulaPuntuacio.COLUMNA_TEMPS + TIPUS_ENTER + SEPARADOR_COMA +
                    PuntuacionsContract.TaulaPuntuacio.COLUMNA_DATA + TIPUS_TEXT + " )";

    private static final String SQL_ESBORRAT_TAULA =
            "DROP TABLE IF EXISTS " +
                    PuntuacionsContract.TaulaPuntuacio.NOM_TAULA;

    public Puntuacions(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREACIO_TAULA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_ESBORRAT_TAULA);
        onCreate(db);
    }
}
