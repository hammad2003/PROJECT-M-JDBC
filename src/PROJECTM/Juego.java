package PROJECTM;

import java.util.List;

/**
 * Clase que representa un juego.
 * Utilizada para almacenar información sobre juegos y sus respectivos mods.
 */
public class Juego {

    private String title;
    private String description;
    private List<Mod> mods;

    /**
     * Constructor vacío requerido para JAXB (Java Architecture for XML Binding).
     * Este constructor es utilizado por JAXB para inicializar objetos de esta clase al leer XML.
     */
    public Juego() {
    }

    /**
     * Constructor con parámetros para crear una instancia de la clase {@code PROJECTM.Juego}.
     *
     * @param title       El título del juego.
     * @param description La descripción del juego.
     */
    public Juego(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /**
     * Obtiene el título del juego.
     *
     * @return El título del juego.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título del juego.
     *
     * @param title El nuevo título del juego.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene la descripción del juego.
     *
     * @return La descripción del juego.
     */

    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción del juego.
     *
     * @param description La nueva descripción del juego.
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene la lista de mods asociados al juego.
     *
     * @return La lista de mods del juego.
     */

    public List<Mod> getProjectCards() {
        return mods;
    }

    /**
     * Establece la lista de mods asociados al juego.
     *
     * @param mods La nueva lista de mods del juego.
     */
    public void setProjectCards(List<Mod> mods) {
        this.mods = mods;
    }

    /**
     * Devuelve una representación en cadena de la instancia actual de la clase {@code PROJECTM.Juego}.
     *
     * @return Una cadena que representa el juego y su título, descripción y lista de mods.
     */
    @Override
    public String toString() {
        return "PROJECTM.Juego{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", mods=" + mods +
                '}';
    }
}