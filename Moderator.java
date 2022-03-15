import java.util.Random;

public class Moderator {
    private int day;
    private Player[] players;
    private Board board;

    /* Getters and Setters */

    /**
     * Returns the board
     * 
     * @param none
     * @return the board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Returns the day number
     * 
     * @param none
     * @return the day number
     */
    public int getDay() {
        return this.day;
    }

    /**
     * Sets the day number
     * 
     * @param
     * @return void
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Returns the array of players
     * 
     * @param none
     * @return players array
     */
    public Player[] getPlayers() {
        return this.players;
    }

    /**
     * Sets the players array
     * 
     * @param players
     * @return void
     */
    public void setPlayers(Player[] players) {
        this.players = players;
    }

    /* Other Public Methods */
    public void setBoard() {
        ParseXML parser = new ParseXML();
        // create board class from XML
        try {
            this.board = parser.readBoardAndCards(parser.getDocFromFile("resources/board.xml"),
                    parser.getDocFromFile("resources/cards.xml"));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        // put all player's in the trailer
        for (int i = 0; i < players.length; i++) {
            players[i].setCurrentRoom("trailer");
        }
        // put one card for each scene on the board
        this.board.placeCards();

    }

    // resets the board after a day is over
    public void resetBoard() {
        for (int i = 0; i < board.getStructure().length; i++) {
            board.getStructure()[i].hasBeenFlipped = 0;
        }
        for (int i = 0; i < players.length; i++) {
            players[i].setCurrentRoom("trailer");
            players[i].setCurrentRole("");
            players[i].setRehearsalPoints(0);
        }
        // put one card for each scene on the board
        this.board.placeCards();
        for (int i = 0; i < board.getStructure().length; i++) {
            // sets all the completed sets to not completed
            board.getStructure()[i].setCompleted(false);
            // resets all the shot counters to their init values
            board.getStructure()[i].setShotCounter(board.getStructure()[i].getInitShotCounter());
            for (int j = 0; j < board.getStructure()[i].getRoles().length; j++) {
                // set all roles on the board to true, no need to do it for cards because there
                // are new cards
                board.getStructure()[i].getRoles()[j].setAvailable(true);
            }
        }
    }

    // checks if the board is done at the end of round
    public boolean checkBoard() {
        int count = 0;
        // gets the number of completed scenes
        for (int i = 0; i < board.getStructure().length; i++) {
            if (board.getStructure()[i].getCompleted()) {
                count++;
            }
        }
        // compares the completed number to all scenes minus 1
        if (count == board.getStructure().length - 1) {
            resetBoard();
            return true;
        }
        // if there are < 9 scenes, return false
        return false;
    }

    // calculate all the player's scores at the end of the game
    private void calcScore() {
        int playerScore;
        for (int i = 0; i < players.length; i++) {
            // calc score for player
            playerScore = players[i].getMoney() + 5 * (players[i].getRank()) + players[i].getCredit();
            // set player's score
            players[i].setScore(playerScore);
        }
    }

    // This is when the user acts, the number are per the rules
    public boolean rewardPlayer(Player playerToReward, boolean succeed) {
        if (!succeed && playerToReward.getOnCard()) {
            return false;
        } else if (!succeed && !(playerToReward.getOnCard())) {
            playerToReward.setMoney(playerToReward.getMoney() + 1);
            return false;
        } else if (succeed && playerToReward.getOnCard()) {
            playerToReward.setCredit(playerToReward.getCredit() + 2);
            getSetFromString(playerToReward.getCurrentRoom()).removeShotCounter();
        } else if (succeed && !(playerToReward.getOnCard())) {
            playerToReward.setCredit(playerToReward.getCredit() + 1);
            playerToReward.setMoney(playerToReward.getMoney() + 1);
            getSetFromString(playerToReward.getCurrentRoom()).removeShotCounter();
        }
        if (getSetFromString(playerToReward.getCurrentRoom()).getCompleted()) {
            playerToReward.setOnCard(false);
            return wrapsUpScene(playerToReward.getCurrentRoom());
        }
        return false;
    }

    // wraps up a scene when the last shot token is removed
    public boolean wrapsUpScene(String scene) {
        Random rand = new Random();
        boolean playerOnCard = false;
        int indexOfScene = 0;
        for (int i = 0; i < board.getStructure().length; i++) {
            // iterate until you get the correct scene on the board
            if (board.getStructure()[i].getName().equals(scene)) {
                indexOfScene = i;
                // iterate through all the roles associated with the scene
                for (int j = 0; j < board.getStructure()[i].getScene().getRoles().length; j++) {
                    // check all the players for the role
                    for (int x = 0; x < players.length; x++) {
                        if (players[x].getCurrentRole()
                                .equals(board.getStructure()[i].getScene().getRoles()[j].getName())) {
                            playerOnCard = true;
                        }
                    }
                }
            }
        }
        // set all roles on card to unavailable
        for (int i = 0; i < board.getStructure()[indexOfScene].getScene().getRoles().length; i++) {
            board.getStructure()[indexOfScene].getScene().getRoles()[i].setAvailable(false);
        }
        // set all roles off card to unavailable
        for (int i = 0; i < board.getStructure()[indexOfScene].getRoles().length; i++) {
            board.getStructure()[indexOfScene].getRoles()[i].setAvailable(false);
        }
        if (!playerOnCard) {
            // reset all rehearsal points for all players on the set
            for (int i = 0; i < players.length; i++) {
                for (int j = 0; j < board.getStructure()[indexOfScene].getRoles().length; j++) {
                    // if the player is on the role in the scene
                    if (players[i].getCurrentRole()
                            .equals(board.getStructure()[indexOfScene].getRoles()[j].getName())) {
                        players[i].setRehearsalPoints(0);
                        players[i].setCurrentRole("");
                    }
                }
            }
        } else {
            int numOfDie = board.getStructure()[indexOfScene].getScene().getBudget();
            int dieArray[] = new int[numOfDie];
            int numOfRoles = board.getStructure()[indexOfScene].getScene().getRoles().length;
            // roll die
            for (int i = 0; i < dieArray.length; i++) {
                // rolls die for each number of die and puts in die array
                dieArray[i] = rand.nextInt(6) + 1;
            }
            // we iterate backwards because the ranks on the card are sorted in reverse
            // order of budget
            for (int i = numOfRoles - 1; i >= 0; i--) {
                // iterate through all the players
                for (int j = 0; j < players.length; j++) {
                    // if the player is on the scene
                    if (players[j].getCurrentRole().equals(board.getStructure()[indexOfScene].getScene().getRoles()[i].getName())) {
                        players[j].setRehearsalPoints(0);
                        players[i].setCurrentRole("");
                        board.getStructure()[indexOfScene].getScene().getRoles()[j].setAvailable(false);
                        // reward the player for the die that they should get
                        for (int x = 0; x < (dieArray.length); x++) {
                            if (x % numOfRoles == i) {
                                players[j].setMoney(players[j].getMoney() + dieArray[x]);
                            }
                        }
                    }
                }
            }
            // reward all players off the card
            for (int i = 0; i < players.length; i++) {
                for (int j = 0; j < board.getStructure()[indexOfScene].getRoles().length; j++) {
                    board.getStructure()[indexOfScene].getRoles()[j].setAvailable(false);
                    // if the player is on the role in the scene
                    if (players[i].getCurrentRole()
                            .equals(board.getStructure()[indexOfScene].getRoles()[j].getName())) {
                        players[i].setRehearsalPoints(0);
                        players[i].setCurrentRole("");
                        players[i].setMoney(
                                players[i].getMoney() + board.getStructure()[indexOfScene].getRoles()[j].getRank());
                    }
                }
            }
        }
        return checkBoard();
    }

    // chooses a random player's index
    public int chooseRandomPlayer() {
        Random generator = new Random();
        int randomIndex = generator.nextInt(this.players.length);
        // set to first player to compile
        return randomIndex;
    }

    // asks the player where to move and moves them if it can
    public String[] moveRoomChoices(int currentPlayer) {
        String currentRoom = players[currentPlayer].getCurrentRoom();
        // if the player is in the trailer
        if (currentRoom.equals("trailer")) {
            return board.getTrailer().getNeighbors();
        }
        // if the player is in the office
        else if (currentRoom.equals("office")) {
            return board.getCastingOffice().getNeighbors();
        }
        // if the player is on a set
        else {
            // display neighbors
            for (int i = 0; i < board.getStructure().length; i++) {
                if (currentRoom.equals(board.getStructure()[i].getName())) {
                    return board.getStructure()[i].getNeighbors();
                }
            }
        }
        return board.getTrailer().getNeighbors();
    }

    // asks the player where to move and moves them if it can
    public boolean movePlayer(int playerIndex, boolean hasMoved, String userInput) {
        if (hasMoved) {
            return false;
        }
        String currentRoom = players[playerIndex].getCurrentRoom();
        // if the player is in the trailer
        if (currentRoom.equals("trailer")) {
            for (int i = 0; i < board.getTrailer().getNeighbors().length; i++) {
                if (userInput.equals(board.getTrailer().getNeighbors()[i])) {
                    players[playerIndex].setCurrentRoom(userInput);
                    return true;
                }
            }
        }
        // if the player is in the office
        else if (currentRoom.equals("office")) {
            // move player
            for (int i = 0; i < board.getCastingOffice().getNeighbors().length; i++) {
                if (userInput.equals(board.getCastingOffice().getNeighbors()[i])) {
                    players[playerIndex].setCurrentRoom(userInput);
                    return true;
                }
            }

        }
        // if the player is on a set
        else {
            // move player
            for (int i = 0; i < board.getStructure().length; i++) {
                if (currentRoom.equals(board.getStructure()[i].getName())) {
                    for (int j = 0; j < board.getStructure()[i].getNeighbors().length; j++) {
                        if (userInput.equals(board.getStructure()[i].getNeighbors()[j])) {
                            players[playerIndex].setCurrentRoom(userInput);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // gets set from a String by checking each set in structure, returns set index
    public Set getSetFromString(String room) {
        int roomIndex = 0;
        for (int i = 0; i < board.getStructure().length; i++) {
            if (board.getStructure()[i].getName().equals(room)) {
                roomIndex = i;
                break;
            }
        }
        return board.getStructure()[roomIndex];
    }

    // gets a role given a String role and String room, returns role object
    public Role getRoleFromStrings(String role, String room) {
        int roomIndex = 0;
        for (int i = 0; i < board.getStructure().length; i++) {
            if (board.getStructure()[i].getName().equals(room)) {
                roomIndex = i;
                break;
            }
        }
        int i = 0;
        int roleIndex = 0;
        // look through the set's roles
        for (i = 0; i < board.getStructure()[roomIndex].getRoles().length; i++) {
            if (board.getStructure()[roomIndex].getRoles()[i].getName().equals(role)) {
                return board.getStructure()[roomIndex].getRoles()[i];
            }
        }
        // look through the roles on the card
        for (i = 0; i < board.getStructure()[roomIndex].getScene().getRoles().length; i++) {
            if (board.getStructure()[roomIndex].getScene().getRoles()[i].getName().equals(role)) {
                roleIndex = i;
            }
        }
        // return the role
        return board.getStructure()[roomIndex].getScene().getRoles()[roleIndex];
    }

    // finishes the game and finds the player with the highest score
    public String finishGame() {
        String winnerName = "";
        int maxScore = 0;
        calcScore();
        for (int i = 0; i < players.length; i++) {
            if (players[i].getScore() > maxScore) {
                maxScore = players[i].getScore();
                winnerName = players[i].getName();
            }
        }
        // first player in the list wins if there is a tie, this is my version of a
        // tiebreaker.. entirely intentional
        return winnerName;
    }

    // Allows the player to pick and select a role
    public boolean manageRoles(int currentPlayer, String userInput) {
        Role roomRolls[] = getSetFromString(getPlayers()[currentPlayer].getCurrentRoom()).getRoles();
        Role sceneRolls[] = getSetFromString(getPlayers()[currentPlayer].getCurrentRoom()).getScene().getRoles();
        return getPlayers()[currentPlayer].takeRole(sceneRolls, roomRolls, userInput);
    }

    public void setRoomToFlipped(String room) {
        for (int i = 0; i < board.getStructure().length; i++) {
            if (board.getStructure()[i].getName().equals(room)) {
                board.getStructure()[i].hasBeenFlipped = 1;
            }
        }
    }
}