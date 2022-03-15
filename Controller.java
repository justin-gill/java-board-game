import java.awt.event.*;
import javax.swing.JLayeredPane;

/**
 * The Controller of the MVC model, along with Moderator
 */
public class Controller extends JLayeredPane {
    View view = new View();
    int currentPlayer = 0;
    Moderator mod = new Moderator();
    boolean isDayOver;
    boolean isGameOver;
    int numberOfDays;

    public void playGame() {
        view.setVisible(true);
        // driver for the program
        int numberOfPlayers;
        numberOfPlayers = view.chooseNumOfPlayers(view);
        if ((numberOfPlayers == 2 || numberOfPlayers == 3)) {
            numberOfDays = 3;
        } else if (numberOfPlayers > 3) {
            numberOfDays = 4;
        }
        Player players[] = new Player[numberOfPlayers];
        // assign each player to a die, the name is now the file color
        // b = 1
        // c = 2
        // g = 3
        // o = 4
        // p = 5
        // r = 6
        // v = 7
        // w = 8
        for (int i = 0; i < numberOfPlayers; i++) {
            switch (i) {
                case 0:
                    players[i] = new Player("blue");
                    break;
                case 1:
                    players[i] = new Player("cyan");
                    break;
                case 2:
                    players[i] = new Player("green");
                    break;
                case 3:
                    players[i] = new Player("orange");
                    break;
                case 4:
                    players[i] = new Player("purple");
                    break;
                case 5:
                    players[i] = new Player("red");
                    break;
                case 6:
                    players[i] = new Player("violet");
                    break;
                case 7:
                    players[i] = new Player("white");
                    break;
            }
        }
        if (numberOfPlayers == 5) {
            for (int i = 0; i < numberOfPlayers; i++) {
                players[i].setCredit(2);
            }
        } else if (numberOfPlayers == 6) {
            for (int i = 0; i < numberOfPlayers; i++) {
                players[i].setCredit(4);
            }
        } else if (numberOfPlayers == 8 || numberOfPlayers == 7) {
            for (int i = 0; i < numberOfPlayers; i++) {
                players[i].setRank(2);
            }
        }
        // sets all the players
        mod.setPlayers(players);

        // sets the board and cards by reading XML files
        mod.setBoard();
        // updates the board
        view.updateEntireBoard(view, mod.getPlayers(), mod.getBoard().getStructure(), mod);
        boolean isGameOver = false;
        boolean isDayOver = false;
        currentPlayer = mod.chooseRandomPlayer();
        // display the active player in the view
        view.displayActivePlayer(mod.getPlayers()[currentPlayer]);

        // display all the buttons
        view.setButtons();
        view.addTakeRoleListener(new boardMouseListener());
        view.addMoveListener(new boardMouseListener());
        view.addUpgradeListener(new boardMouseListener());
        view.addActListener(new boardMouseListener());
        view.addEndTurnListener(new boardMouseListener());
        view.addRehearseListener(new boardMouseListener());
    }

    /**
     * This listens for events set up in the view and calls the appropriate methods
     */
    class boardMouseListener implements MouseListener {

        // Code for the different button clicks
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == view.bAct) {
                act();
            } else if (e.getSource() == view.bRehearse) {
                rehearse();
            } else if (e.getSource() == view.bMove) {
                move();
            } else if (e.getSource() == view.bEndTurn) {
                endTurn();
            } else if (e.getSource() == view.bTakeRole) {
                takeRole();
            } else if (e.getSource() == view.bUpgradeRank) {
                upgradeRank();
            }
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    /**
     * This function is called when the player presses the move button
     */
    public void move() {
        // if the player is not on a role or has already moved
        if (mod.getPlayers()[currentPlayer].getCurrentRole().equals("")
                && !mod.getPlayers()[currentPlayer].alreadyMoved) {
            String userChoice = view.chooseRoom(mod.moveRoomChoices(currentPlayer));
            if (userChoice == null) {
                return;
            }
            mod.movePlayer(currentPlayer, false, userChoice);
            mod.setRoomToFlipped(userChoice);
            view.updateEntireBoard(view, mod.getPlayers(), mod.getBoard().getStructure(), mod);
            mod.getPlayers()[currentPlayer].alreadyMoved = true;
        } else {
            view.displayInvalidChoice("You cannot move... nice try!");
        }
    }

    /**
     * This is called when the user clicks take role
     * 
     * @return whether the role was taken
     */
    public boolean takeRole() {
        // if the player cannot take a role
        if (mod.getPlayers()[currentPlayer].getCurrentRoom().equals("office") ||
                mod.getPlayers()[currentPlayer].getCurrentRoom().equals("trailer") ||
                !mod.getPlayers()[currentPlayer].getCurrentRole().equals("")) {
            view.displayInvalidChoice("You cannot take a role... nice try!");
            return false;
        }
        // the user can take a role
        else {
            Role roomRoles[] = mod.getSetFromString(mod.getPlayers()[currentPlayer].getCurrentRoom()).getRoles();
            Role sceneRoles[] = mod.getSetFromString(mod.getPlayers()[currentPlayer].getCurrentRoom()).getScene()
                    .getRoles();
            String[] onCardRoles = new String[sceneRoles.length];
            String[] offCardRoles = new String[roomRoles.length];
            for (int i = 0; i < roomRoles.length; i++) {
                offCardRoles[i] = roomRoles[i].getName();
            }
            for (int i = 0; i < sceneRoles.length; i++) {
                onCardRoles[i] = sceneRoles[i].getName();
            }
            // combines all the roles that are avaliable
            String[] combined = new String[roomRoles.length + sceneRoles.length];
            System.arraycopy(offCardRoles, 0, combined, 0, offCardRoles.length);
            System.arraycopy(onCardRoles, 0, combined, offCardRoles.length, onCardRoles.length);
            // let the user choose the role
            String userChoice = view.chooseRole(combined, true);
            if (userChoice == null) {
                return false;
            }
            if (mod.manageRoles(currentPlayer, userChoice)) {
                Role temp = mod.getRoleFromStrings(mod.getPlayers()[currentPlayer].getCurrentRole(),
                        mod.getPlayers()[currentPlayer].getCurrentRoom());
                view.setPlayerOnRole(mod.getPlayers()[currentPlayer], temp.xCoord, temp.yCoord,
                        mod.getSetFromString(mod.getPlayers()[currentPlayer].getCurrentRoom()));
            } else {
                view.displayInvalidChoice("That role is unavaliable to you... nice try!");
                return false;
            }
            return true;
        }
    }

    /**
     * This is called when the clicks on end turn, it ends the users turn and moves
     * to next player
     */
    public void endTurn() {
        currentPlayer = (currentPlayer + 1) % mod.getPlayers().length;
        view.updateEntireBoard(view, mod.getPlayers(), mod.getBoard().getStructure(), mod);
        view.displayActivePlayer(mod.getPlayers()[currentPlayer]);
        // reset that the player has already moved
        mod.getPlayers()[currentPlayer].alreadyMoved = false;
    }

    /**
     * This is called when the user selects act
     * 
     * @return
     */
    public boolean act() {
        // if the user is not on a role or has already moved, they cannnot act
        if (mod.getPlayers()[currentPlayer].getCurrentRole().equals("")
                || mod.getPlayers()[currentPlayer].alreadyMoved) {
            view.displayInvalidChoice("You cannot act... nice try!");
            return false;
        } else {
            int budget = mod.getSetFromString(mod.getPlayers()[currentPlayer].getCurrentRoom()).getScene().getBudget();
            int roll = mod.getPlayers()[currentPlayer].act(budget);
            if (roll >= budget) {
                // the player rolled high enough
                isDayOver = mod.rewardPlayer(mod.getPlayers()[currentPlayer], true);
                view.displayReward(true, roll);
                view.displayActivePlayer(mod.getPlayers()[currentPlayer]);
            } else {
                view.displayReward(false, roll);
            }
            // check if the day is over
            if (isDayOver) {
                isDayOver = false;
                mod.setDay(mod.getDay() + 1);
                currentPlayer = (currentPlayer + 1) % mod.getPlayers().length;
                view.displayActivePlayer(mod.getPlayers()[currentPlayer]);
                // if the day is equal to the number of days, the game is over
                if (mod.getDay() == numberOfDays) {
                    view.finishGameView(mod.finishGame());
                }
            } else {
                mod.getPlayers()[currentPlayer].alreadyMoved = true;
            }
            view.updateEntireBoard(view, mod.getPlayers(), mod.getBoard().getStructure(), mod);
            return true;
        }
    }

    /**
     * Called when the player clicks rehearse
     */
    public void rehearse() {
        // if the player cannot rehearse
        if (mod.getPlayers()[currentPlayer].alreadyMoved
                || mod.getPlayers()[currentPlayer].getCurrentRole().equals("")) {
            view.displayInvalidChoice("You cannot rehearse... nice try!");
            return;
        }
        // upgrade the rehearse tokens if it can
        int budget = mod.getSetFromString(mod.getPlayers()[currentPlayer].getCurrentRoom()).getScene().getBudget();
        boolean val = mod.getPlayers()[currentPlayer].rehearse(budget);
        if (val) {
            view.displayActivePlayer(mod.getPlayers()[currentPlayer]);
            mod.getPlayers()[currentPlayer].alreadyMoved = true;
            view.displayInvalidChoice("You have been given a rehearsal point!");
        } else {
            // if the player could not rehearse
            view.displayInvalidChoice("You already have the max rehearsal points for the role!");
            return;
        }
    }

    /**
     * Called when the player clicks upgrade rank
     */
    public void upgradeRank() {
        // if the player is not in the correct room
        if (!mod.getPlayers()[currentPlayer].getCurrentRoom().equals("office")) {
            view.displayInvalidChoice("You can only upgrade in the office... nice try!");
            return;
        } else {
            // upgrade the players rank
            String userChoice = view.upgradeRank();
            if (userChoice == null) {
                return;
            }
            if (!mod.getPlayers()[currentPlayer].upgradeRank(userChoice)) {
                // if the rank could not be upgraded with the seleciton
                view.displayInvalidChoice("You do not have the funds... nice try!");
            }
            view.updateEntireBoard(view, mod.getPlayers(), mod.getBoard().getStructure(), mod);
            view.displayActivePlayer(mod.getPlayers()[currentPlayer]);
        }
    }
}
