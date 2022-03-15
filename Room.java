abstract class Room {
    // each room has a list of rooms attached to it
    protected String[] neighbors;
    protected String name;

    /**
     * Returns the Room's name
     * 
     * @param none
     * @return the Room's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the Rooms's name
     * 
     * @param name
     * @return void
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the Neighbor's list
     * 
     * @param neighbors
     * @return void
     */
    public void setNeighbors(String[] neighbors) {
        this.neighbors = neighbors;
    }

    /**
     * Returns the Room's neighbors
     * 
     * @param none
     * @return the Room's neighbors
     */
    public String[] getNeighbors() {
        return this.neighbors;
    }
}