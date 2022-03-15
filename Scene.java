public class Scene {
    private String title;
    private String description;
    private int budget;
    private int number;
    private Role[] roles;
    public String sceneImg;

    /* Constructors */

    // Default
    public Scene() {
    }

    // Complete constructor
    public Scene(String title, String description, int budget, Role[] roles, int number, String sceneImg) {
        this.number = number;
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.roles = roles;
        this.sceneImg = sceneImg;
    }
    /* Getters and Setters */

    /**
     * Returns the Scene's number
     * 
     * @param none
     * @return the scene's number
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Returns the Scene's title
     * 
     * @param none
     * @return the scene's title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the Scene's description
     * 
     * @param none
     * @return the scene's description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the Scene's budget
     * 
     * @param none
     * @return the scene's budget
     */
    public int getBudget() {
        return this.budget;
    }

    /**
     * Returns the Scene's roles
     * 
     * @param none
     * @return the scene's roles
     */
    public Role[] getRoles() {
        return this.roles;
    }

    /**
     * Sets the Scene's roles
     * 
     * @param roles
     * @return void
     */
    public void setRoles(Role[] roles) {
        this.roles = roles;
    }
}
