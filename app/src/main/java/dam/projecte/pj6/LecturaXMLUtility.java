package dam.projecte.pj6;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LecturaXMLUtility {
    private static final String ns = null;

    // CREACIÓN CLASE Juego
    public static class Juego {
        final String nombre;
        final String id;
        final List<String> pistas;
        final List<String> imagenes;

        public Juego(String nombre,String id,ArrayList<String> pistas,ArrayList<String> imagenes){
            this.nombre = nombre;
            this.id = id;
            this.pistas = pistas;
            this.imagenes = imagenes;
        }

        @Override
        public String toString() {
            return "Juego{" +
                    "nombre='" + nombre + '\'' +
                    ", id='" + id + '\'' +
                    ", pistas='" + pistas + '\'' +
                    ", imagenes=" + imagenes +
                    '}';
        }

        public List<String> getImagenes() {
            return imagenes;
        }

        public List<String> getPistas() {
            return pistas;
        }

        public String getNombre() {
            return nombre;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Juego juego = (Juego) o;
            return Objects.equals(nombre, juego.nombre) && Objects.equals(id, juego.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nombre, id);
        }
    }



    /**
     * Mètode que llegeix l'XML i retorna una lista amb els juegos continguts
     *
     * @param in
     * @return llista juegos
     * @throws XmlPullParserException
     * @throws IOException
     */
    public List<Juego> analitzarXML(InputStream in) throws XmlPullParserException, IOException {
        try {

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return llegirJuegos(parser);
        } finally {
            in.close();
        }
    }

    private List<Juego> llegirJuegos(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Juego> juegos = new ArrayList<Juego>();

        parser.require(XmlPullParser.START_TAG, ns, "juegos");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // cerquem l'element juego
            if (name.equals("juego")) {
                juegos.add(llegirJuego(parser));
            } else {
                saltarEtiqueta(parser);
            }
        }
        return juegos;
    }

    private List<String> llegirPistas(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<String> pistas = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "pistas");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // cerquem la pista
            if (name.equals("pista")) {
                pistas.add(llegirPista(parser));
            } else {
                saltarEtiqueta(parser);
            }
        }
        return pistas;
    }

    private List<String> llegirImagenes(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<String> imagenes = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "imagenes");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // cerquem la pista
            if (name.equals("imagen")) {
                imagenes.add(llegirImagen(parser));
            } else {
                saltarEtiqueta(parser);
            }
        }
        return imagenes;
    }

    private Juego llegirJuego(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "juego");
        String nom = null;
        List<String> pista = new ArrayList<>();
        List<String> imagen = new ArrayList<>();
        String id = parser.getAttributeValue(null, "id");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("nombre")) {
                nom = llegirNom(parser);
            }
            else if (name.equals("pistas")){
                pista = llegirPistas(parser);
            }
            else if (name.equals("imagenes")){
                imagen = llegirImagenes(parser);
            }
            else {
                saltarEtiqueta(parser);
            }
        }
        return new Juego(nom, id, (ArrayList<String>) pista, (ArrayList<String>) imagen);
    }

    /**
     * Mètode que processa l'element nombre
     *
     * @param parser
     * @return cadena amb el nom
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String llegirNom(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "nombre");
        String nom = llegirText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "nombre");
        return nom;
    }

    private String llegirPista(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pista");
        String pista = llegirText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pista");
        return pista;
    }

    private String llegirImagen(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "imagen");
        String imagen = llegirText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "imagen");
        return imagen;
    }

    private String llegirText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String text = "";
        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.getText();
            parser.nextTag();
        }
        return text;
    }

    /**
     * Mètode que permet ignorar les etiquetes que no han de ser processades
     *
     * @param parser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void saltarEtiqueta(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int profunditat = 1;
        while (profunditat != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    profunditat--;
                    break;
                case XmlPullParser.START_TAG:
                    profunditat++;
                    break;
            }
        }
    }
}
