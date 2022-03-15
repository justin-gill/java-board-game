import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.util.Random;

public class Player {
    private String name;
    private boolean onCard;
    private int rehearsalPoints = 0;
    private int money = 0;
    private int score = 0;
    private int credit = 0;
    private int rank = 1;
    private String currentRoom = "";
    private String currentRole = "";
    public String playerFilename = "";
    public ImageIcon pIcon;
    public JLabel playerlabel;
    public boolean alreadyMoved = false;

    /* Constructors */

    public Player() {
    }

    public Player(String name) {
        this.name = name;
        this.playerFilename = "" + this.name.charAt(0) + this.rank + ".png";
        this.playerlabel = new JLabel();
        this.pIcon = new ImageIcon("resources/dice/" + this.playerFilename);
    }

    /* Getters and Setters */

    /**
     * Returns the Player's onCard status
     * 
     * @param none
     * @return the Player's onCard status
     */
    public boolean getOnCard() {
        return this.onCard;
    }

    /**
     * Sets the Player's onCard status
     * 
     * @param onCard
     * @return void
     */
    public void setOnCard(boolean onCard) {
        this.onCard = onCard;
    }

    /**
     * Returns the Player's rehearse points
     * 
     * @param none
     * @return the Player's rehearse points
     */
    public int getRehearsalPoints() {
        return this.rehearsalPoints;
    }

    /**
     * Sets the Player's rehearse points
     * 
     * @param none
     * @return the Player's rehearse points
     */
    public void setRehearsalPoints(int rehearsalPoints) {
        this.rehearsalPoints =  rehearsalPoints;
    }

    /**
     * Returns the Player's name
     * 
     * @param none
     * @return the Player's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the Player's name
     * 
     * @param name
     * @return void
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the Player's money
     * 
     * @param none
     * @return the Player's money
     */
    public int getMoney() {
        return this.money;
    }

    /**
     * Sets the Player's money
     * 
     * @param money
     * @return void
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Returns the Player's credit
     * 
     * @param none
     * @return the Player's money
     */
    public int getCredit() {
        return this.credit;
    }

    /**
     * Sets the Player's credit
     * 
     * @param money
     * @return void
     */
    public void setCredit(int credit) {
        this.credit = credit;
    }

    /**
     * Returns the Player's score
     * 
     * @param none
     * @return the Player's score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Sets the Player's score
     * 
     * @param score
     * @return void
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns the Player's rank
     * 
     * @param none
     * @return the Player's rank
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * Sets the Player's rank
     * 
     * @param rank
     * @return void
     */
    public void setRank(int rank) {
        this.rank = rank;
        this.playerFilename = "" + this.name.charAt(0) + this.rank + ".png";
        this.pIcon = new ImageIcon("resources/dice/" + this.playerFilename);
    }

    /**
     * Returns the Player's current room
     * 
     * @param none
     * @return the Player's rank
     */
    public String getCurrentRoom() {
        return this.currentRoom;
    }

    /**
     * Sets the Player's current Room
     * 
     * @param currentRoom
     * @return void
     */
    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * Returns the Player's current role
     * 
     * @param none
     * @return the Player's role
     */
    public String getCurrentRole() {
        return this.currentRole;
    }

    /**
     * Sets the Player's current role
     * 
     * @param currentRole
     * @return void
     */
    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    /** Other Public Methods */

    /**
     * This function is called by the controlelr and takes a role for a player if it
     * can
     * 
     * @param onCardRoles
     * @param offCardRoles
     * @param chosenRole
     * @return
     */
    public boolean takeRole(Role[] onCardRoles, Role[] offCardRoles, String chosenRole) {
        // checks all the on card roles
        for (int i = 0; i < onCardRoles.length; i++) {
            if (onCardRoles[i].getName().equals(chosenRole)) {
                if (!onCardRoles[i].getAvailable()) {
                    return false;
                }
                if (onCardRoles[i].getRank() > this.rank) {
                    return false;
                }
                this.currentRole = chosenRole;
                onCardRoles[i].setAvailable(false);
                this.onCard = true;
                return true;
            }
        }
        // checks all the off card roles
        for (int i = 0; i < offCardRoles.length; i++) {
            if (offCardRoles[i].getName().equals(chosenRole)) {
                if (!offCardRoles[i].getAvailable()) {
                    return false;
                }
                if (offCardRoles[i].getRank() > this.rank) {
                    return false;
                }
                this.currentRole = chosenRole;
                offCardRoles[i].setAvailable(false);
                this.onCard = false;
                return true;
            }
        }
        return false;
    }

    // Function to let the player act
    public int act(int budget) {
        Random rand = new Random();
        // roll one die
        int roll = 1 + rand.nextInt(6);
        roll += this.rehearsalPoints;
        return roll;
    }

    // Function to let player rehearse
    public boolean rehearse(int budget) {
        if (rehearsalPoints >= budget - 1) {
            return false;
        }
        rehearsalPoints += 1;
        return true;
    }

    // Function to upgrade the player's rank
    public boolean upgradeRank(String choice) {
        // player must be in the office
        if (this.rank == 1) {
            return upgradeRankHelper(4, 5, choice);
        } else if (this.rank == 2) {
            return upgradeRankHelper(10, 10, choice);
        } else if (this.rank == 3) {
            return upgradeRankHelper(18, 15, choice);
        } else if (this.rank == 4) {
            return upgradeRankHelper(28, 20, choice);
        } else if (this.rank == 5) {
            return upgradeRankHelper(40, 25, choice);
        } else if (this.rank == 6) {
            return false;
        }
        return false;
    }

    /**
     * Helper function for upgrading rank
     * 
     * @param dollars
     * @param credits
     * @param userChoice
     * @return
     */
    private boolean upgradeRankHelper(int dollars, int credits, String userChoice) {
        // Credits
        if (userChoice.equals("Credits")) {
            if (this.credit < credits) {
                return false;
            }
            this.credit -= credits;
            this.setRank(this.rank + 1);
            return true;
        }
        // dollars
        else if (userChoice.equals("Dollars")) {
            if (this.money < dollars) {
                return false;
            }
            this.money -= dollars;
            this.setRank(this.rank + 1);
            return true;
        }
        return false;
    }

}
