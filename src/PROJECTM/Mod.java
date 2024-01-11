package PROJECTM;

import java.util.List;

/**
 * Clase que representa un mod.
 * Utilizada para almacenar información sobre mods, incluyendo detalles y categorías.
 */

public class Mod {

    private String projectName;
    private String author;
    private String projectDescription;
    private List<String> details;
    private List<Categoria> categories;

    /**
     * Constructor con parámetros para crear una instancia de la clase {@code PROJECTM.Mod}.
     *
     * @param projectName      El nombre del proyecto/mod.
     * @param author           El autor del proyecto/mod.
     * @param projectDescription La descripción del proyecto/mod.
     * @param details           Los detalles del proyecto/mod.
     */
    public Mod(String projectName, String author, String projectDescription, List<String> details) {
        this.projectName = projectName;
        this.author = author;
        this.projectDescription = projectDescription;
        this.details = details;
    }

    /**
     * Obtiene el nombre del proyecto/mod.
     *
     * @return El nombre del proyecto/mod.
     */

    public String getProjectName() {
        return projectName;
    }

    /**
     * Establece el nombre del proyecto/mod.
     *
     * @param projectName El nuevo nombre del proyecto/mod.
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Obtiene el autor del proyecto/mod.
     *
     * @return El autor del proyecto/mod.
     */

    public String getAuthor() {
        return author;
    }

    /**
     * Establece el autor del proyecto/mod.
     *
     * @param author El nuevo autor del proyecto/mod.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Obtiene la descripción del proyecto/mod.
     *
     * @return La descripción del proyecto/mod.
     */

    public String getProjectDescription() {
        return projectDescription;
    }

    /**
     * Establece la descripción del proyecto/mod.
     *
     * @param projectDescription La nueva descripción del proyecto/mod.
     */
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    /**
     * Obtiene los detalles del proyecto/mod.
     *
     * @return Los detalles del proyecto/mod.
     */

    public List<String> getDetails() {
        return details;
    }

    /**
     * Establece los detalles del proyecto/mod.
     *
     * @param details Los nuevos detalles del proyecto/mod.
     */
    public void setDetails(List<String> details) {
        this.details = details;
    }

    /**
     * Obtiene las categorías asociadas al proyecto/mod.
     *
     * @return Las categorías asociadas al proyecto/mod.
     */

    public List<Categoria> getCategories() {
        return categories;
    }

    /**
     * Establece las categorías asociadas al proyecto/mod.
     *
     * @param categories Las nuevas categorías asociadas al proyecto/mod.
     */
    public void setCategories(List<Categoria> categories) {
        this.categories = categories;
    }

    /**
     * Devuelve una representación en cadena de la instancia actual de la clase {@code PROJECTM.Mod}.
     *
     * @return Una cadena que representa el proyecto/mod y sus detalles, categorías, etc.
     */
    @Override
    public String toString() {
        return "PROJECTM.Mod{" +
                "projectName='" + projectName + '\'' +
                ", author='" + author + '\'' +
                ", projectDescription='" + projectDescription + '\'' +
                ", details=" + details +
                ", categories=" + categories +
                '}';
    }
}