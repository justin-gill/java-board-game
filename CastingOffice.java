public class CastingOffice extends Room {
    private String description;

    /* Constructors */

    // Default
    public CastingOffice() {
    }

    // Complete constructor
    public CastingOffice(String description, String[] neighbors, String name) {
        this.description = description;
        this.neighbors = neighbors;
        this.name = name;
    }

    /**
     * Returns the Casting Office's description
     * 
     * @param none
     * @return the Casting Office's description
     */
    public String getDescription() {
        return this.description;
    }
}
