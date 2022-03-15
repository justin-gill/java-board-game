public class Role {
    private String name;
    private int rank;
    private boolean available;
    private String line;
    public int xCoord;
    public int yCoord;

    /* Constructors */

    // Default
    public Role() {
    }

    // Complete constructor
    public Role(String name, int rank, boolean available, String line, int xCoord, int yCoord) {
        this.name = name;
        this.rank = rank;
        this.available = available;
        this.line = line;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    /* Getters and Setters */

    /**
     * Returns the Role's name
     * 
     * @param none
     * @return the Role's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the Role's name
     * 
     * @param name
     * @return void
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the Role's rank
     * 
     * @param none
     * @return the Role's rank
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * Sets the Role's rank
     * 
     * @param rank
     * @return void
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Returns the Role's availability
     * 
     * @param none
     * @return the Role's availability
     */
    public boolean getAvailable() {
        return this.available;
    }

    /**
     * Sets the Role's availability
     * 
     * @param available
     * @return void
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Returns the Role's Line
     * 
     * @param none
     * @return the Role's line
     */
    public String getLine() {
        return this.line;
    }

}
