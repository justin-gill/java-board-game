public class Trailer extends Room {
    private String description;

    /* Constructors */

    // Default
    public Trailer() {
    }

    // Complete constructor
    public Trailer(String description, String[] neighbors, String name) {
        this.description = description;
        this.neighbors = neighbors;
        this.name = name;
    }

    /**
     * Returns the Trailer's description
     * 
     * @param none
     * @return the Trailer's description
     */
    public String getDescription() {
        return this.description;
    }
}