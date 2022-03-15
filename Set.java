public class Set extends Room {
    public int xCoord;
    public int yCoord;
    public int shotOne = 1;
    public int shotTwo = 1;
    public int shotThree = 1;
    public int shotCounter1x = 0;
    public int shotCounter1y = 0;
    public int shotCounter2x = 0;
    public int shotCounter2y = 0;
    public int shotCounter3x = 0;
    public int shotCounter3y = 0;
    public int[] shotCounterArray;
    private int shotCounter;
    private int initShotCounter;
    private Role[] roles;
    private boolean completed = false;
    private Scene scene;
    public int hasBeenFlipped = 0;

    /* Constructors */

    // Default
    public Set() {
    }

    // Complete constructor
    public Set(int shotCounter, Role[] roles, boolean completed, String[] neighbors, String name, int xCoord,
            int yCoord,
            int shotCounter1x, int shotCounter1y, int shotCounter2x, int shotCounter2y, int shotCounter3x,
            int shotCounter3y) {
        this.shotCounter1x = shotCounter1x;
        this.shotCounter1y = shotCounter1y;
        this.shotCounter2x = shotCounter2x;
        this.shotCounter2y = shotCounter2y;
        this.shotCounter3x = shotCounter3x;
        this.shotCounter3y = shotCounter3y;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.shotCounter = shotCounter;
        this.initShotCounter = shotCounter;
        this.roles = roles;
        this.completed = completed;
        setNeighbors(neighbors);
        setName(name);
    }

    /* Getters and Setters */

    /**
     * Returns the Room's coord
     * 
     * @param none
     * @return the Room's coord
     */
    public int[] getCoordinates() {
        int[] xy = new int[2];
        xy[0] = xCoord;
        xy[1] = yCoord;
        return xy;
    }

    /**
     * Returns the Room's original shotCounter
     * 
     * @param none
     * @return the Room's original shotCounter
     */
    public int getInitShotCounter() {
        return this.initShotCounter;
    }

    /**
     * Returns the Room's current shotCounter
     * 
     * @param none
     * @return the Room's current shotCounter
     */
    public int getShotCounter() {
        return this.shotCounter;
    }

    /**
     * Sets the Room's shotCounter
     * 
     * @param shotCounter
     * @return void
     */
    public void setShotCounter(int shotCounter) {
        this.shotCounter = shotCounter;
    }

    /**
     * Returns the Room's roles
     * 
     * @param none
     * @return the Room's roles
     */
    public Role[] getRoles() {
        return this.roles;
    }

    /**
     * Sets the Room's roles
     * 
     * @param roles
     * @return void
     */
    public void setRoles(Role[] roles) {
        this.roles = roles;
    }

    /**
     * Returns the Room's completed boolean
     * 
     * @param none
     * @return the Room's completed boolean
     */
    public boolean getCompleted() {
        return this.completed;
    }

    /**
     * Sets the Room's completed boolean
     * 
     * @param completed
     * @return void
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
        ;
    }

    /**
     * Returns the Room's scene
     * 
     * @param none
     * @return the Room's scene
     */
    public Scene getScene() {
        return this.scene;
    }

    /**
     * Sets the Room's scene
     * 
     * @param scene
     * @return void
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /* Other Public Methods */

    public int removeShotCounter() {
        // remove the shot counter
        this.shotCounter--;
        // when the last shot counter is removed, the set is completed
        if (this.shotCounter == 0) {
            this.completed = true;
        }
        return this.shotCounter;
    }
}
