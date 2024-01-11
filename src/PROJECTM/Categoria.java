package PROJECTM;

/**
 * Clase que representa una categoría.
 * Utilizada para almacenar información sobre las categorías de los mods.
 */
public class Categoria {

    private String nombre;

    /**
     * Constructor vacío requerido para JAXB (Java Architecture for XML Binding).
     * Este constructor es utilizado por JAXB para inicializar objetos de esta clase al leer XML.
     */
    public Categoria() {
    }

    /**
     * Constructor con parámetros para crear una instancia de la clase {@code PROJECTM.Categoria}.
     *
     * @param nombre El nombre de la categoría.
     */
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el nombre de la categoría.
     *
     * @return El nombre de la categoría.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la categoría.
     *
     * @param nombre El nuevo nombre de la categoría.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve una representación en cadena de la instancia actual de la clase {@code PROJECTM.Categoria}.
     *
     * @return Una cadena que representa la categoría y su nombre.
     */
    @Override
    public String toString() {
        return "PROJECTM.Categoria{" +
                "nombre='" + nombre + '\'' +
                '}';
    }
}