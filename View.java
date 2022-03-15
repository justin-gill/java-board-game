import java.awt.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.event.*;
import javax.swing.JOptionPane;

public class View extends JFrame {

   // JLabels
   JLabel boardlabel;
   JLabel playerlabel;
   JLabel cardLabels[] = new JLabel[10];
   JLabel shotLabels[][] = new JLabel[10][3];
   JLabel activePlayerLabel = new JLabel();
   JLabel activeRankLabel = new JLabel();
   JLabel activeCreditsLabel = new JLabel();
   JLabel activeMoneyLabel = new JLabel();
   JLabel activeRehearsalPointsLabel = new JLabel();
   JLabel dayNumber = new JLabel();;

   // JButtons
   JButton bUpgradeRank;
   JButton bTakeRole;
   JButton bMove;
   JButton bAct;
   JButton bRehearse;
   JButton bEndTurn;

   // JLayered Pane
   JLayeredPane bPane;

   // JOptionPane
   JOptionPane invalidChoice;
   JOptionPane moveChoices;
   JOptionPane roleChoicesInvalid;
   JOptionPane roleChoices;

   ImageIcon icon = new ImageIcon("resources/board.jpg");

   // Constructor

   public View() {

      // Set the title of the JFrame
      super("Deadwood");
      // Set the exit option for the JFrame
      setDefaultCloseOperation(EXIT_ON_CLOSE);

      // Create the JLayeredPane to hold the display, cards, dice and buttons
      bPane = getLayeredPane();

      // Create the deadwood board
      boardlabel = new JLabel();
      boardlabel.setIcon(icon);
      boardlabel.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());

      // Add the board to the lowest layer
      bPane.add(boardlabel, JLayeredPane.DEFAULT_LAYER);

      // Set the size of the GUI
      setSize(icon.getIconWidth() + 200, icon.getIconHeight() + 50);
      for (int i = 0; i < shotLabels.length; i++) {
         for (int j = 0; j < shotLabels[0].length; j++) {
            shotLabels[i][j] = new JLabel();
         }
      }
      for (int i = 0; i < cardLabels.length; i++) {
         cardLabels[i] = new JLabel();
      }
   }

   /**
    * 
    * @param board
    * @return
    */
   public int chooseNumOfPlayers(View board) {
      int accepted = 0;
      int resultInt = 0;
      do {
         String result = JOptionPane.showInputDialog(board, "How many players?");
         try {
            resultInt = Integer.parseInt(result);
         } catch (NumberFormatException e) {
            accepted = 0;
            continue;
         }
         accepted = 1;
      } while (accepted == 0 || (resultInt < 2 || resultInt > 8));
      return resultInt;
   }

   /**
    * 
    * @param board
    * @param players
    * @param sets
    * @param mod
    */
   public void updateEntireBoard(View board, Player[] players, Set sets[], Moderator mod) {
      // iterate through all the sets and put a card and shot counters
      for (int i = 0; i < sets.length; i++) {
         // if the set is done, show no card
         if (sets[i].getCompleted()) {
            ImageIcon cIcon = new ImageIcon("resources/cards/" + sets[i].getScene().sceneImg);
            cardLabels[i].setIcon(cIcon);
            cardLabels[i].setBounds(sets[i].getCoordinates()[0], sets[i].getCoordinates()[1], cIcon.getIconWidth() + 2,
                  cIcon.getIconHeight());
            cardLabels[i].setVisible(false);
            bPane.add(cardLabels[i], JLayeredPane.MODAL_LAYER);
         }
         // if the card has been flipped, show card
         else if (sets[i].hasBeenFlipped == 0) {
            ImageIcon cIcon = new ImageIcon("resources/cards/CardBack-small.jpg");
            cardLabels[i].setIcon(cIcon);
            cardLabels[i].setBounds(sets[i].getCoordinates()[0], sets[i].getCoordinates()[1], cIcon.getIconWidth() + 2,
                  cIcon.getIconHeight());
            cardLabels[i].setOpaque(true);
            cardLabels[i].setVisible(true);
            bPane.add(cardLabels[i], JLayeredPane.MODAL_LAYER);
            // show a blank card if not flipped or done
         } else {
            ImageIcon cIcon = new ImageIcon("resources/cards/" + sets[i].getScene().sceneImg);
            cardLabels[i].setIcon(cIcon);
            cardLabels[i].setBounds(sets[i].getCoordinates()[0], sets[i].getCoordinates()[1], cIcon.getIconWidth() + 2,
                  cIcon.getIconHeight());
            cardLabels[i].setOpaque(true);
            cardLabels[i].setVisible(true);
            bPane.add(cardLabels[i], JLayeredPane.MODAL_LAYER);
         }
         // place shot counters
         if (sets[i].getShotCounter() < sets[i].getInitShotCounter()) {
            for (int x = 0; x < sets[i].getInitShotCounter(); x++) {
               shotLabels[i][x].setVisible(false);
            }
         }
         for (int j = 0; j < sets[i].getShotCounter(); j++) {
            ImageIcon cIcon = new ImageIcon("resources/shot.png");
            shotLabels[i][j].setIcon(cIcon);
            if (j == 0 && sets[i].shotOne == 1) {
               shotLabels[i][j].setBounds(sets[i].shotCounter1x, sets[i].shotCounter1y, cIcon.getIconWidth(),
                     cIcon.getIconHeight());
            } else if (j == 1 && sets[i].shotTwo == 1) {
               shotLabels[i][j].setBounds(sets[i].shotCounter2x, sets[i].shotCounter2y, cIcon.getIconWidth(),
                     cIcon.getIconHeight());
            } else if (j == 2 && sets[i].shotThree == 1) {
               shotLabels[i][j].setBounds(sets[i].shotCounter3x, sets[i].shotCounter3y, cIcon.getIconWidth(),
                     cIcon.getIconHeight());
            }
            shotLabels[i][j].setOpaque(true);
            shotLabels[i][j].setVisible(true);
            bPane.add(shotLabels[i][j], JLayeredPane.MODAL_LAYER);
         }
      }
      // place players
      for (int i = 0; i < players.length; i++) {
         // check where the player is at and place them there
         if (!players[i].getCurrentRole().equals("")) {
            Role temp = mod.getRoleFromStrings(mod.getPlayers()[i].getCurrentRole(),
                  mod.getPlayers()[i].getCurrentRoom());
            setPlayerOnRole(mod.getPlayers()[i], temp.xCoord, temp.yCoord,
                  mod.getSetFromString(mod.getPlayers()[i].getCurrentRoom()));
         } else if (players[i].getCurrentRoom().equals("trailer")) {
            setPlayerOnTrailer(players[i], i);
         } else if (players[i].getCurrentRoom().equals("Main Street")) {
            players[i].getCurrentRoom();
            setPlayerOnMain(players[i], i);
         } else if (players[i].getCurrentRoom().equals("Saloon")) {
            setPlayerOnSaloon(players[i], i);
         } else if (players[i].getCurrentRoom().equals("Bank")) {
            setPlayerOnBank(players[i], i);
         } else if (players[i].getCurrentRoom().equals("Church")) {
            setPlayerOnChurch(players[i], i);
         } else if (players[i].getCurrentRoom().equals("Hotel")) {
            setPlayerOnHotel(players[i], i);
         } else if (players[i].getCurrentRoom().equals("Secret Hideout")) {
            setPlayerOnHideout(players[i], i);
         } else if (players[i].getCurrentRoom().equals("Ranch")) {
            setPlayerOnRanch(players[i], i);
         } else if (players[i].getCurrentRoom().equals("General Store")) {
            setPlayerOnStore(players[i], i);
         } else if (players[i].getCurrentRoom().equals("Train Station")) {
            setPlayerOnTrainStation(players[i], i);
         } else if (players[i].getCurrentRoom().equals("Jail")) {
            setPlayerOnJail(players[i], i);
         } else if (players[i].getCurrentRoom().equals("office")) {
            setPlayerOnOffice(players[i], i);
         }
      }
      // display number
      dayNumber.setText("Day Number: " + (mod.getDay() + 1));
      dayNumber.setBounds(icon.getIconWidth() + 20, 0, 200, 1500);
      bPane.add(dayNumber, JLayeredPane.PALETTE_LAYER);

   }

   /**
    * shows the active players information on the right hand pane
    * 
    * @param activePlayer
    */
   public void displayActivePlayer(Player activePlayer) {
      activePlayerLabel.setText("Active Player is " + activePlayer.getName());
      activePlayerLabel.setBounds(icon.getIconWidth() + 20, 0, 200, 20);
      bPane.add(activePlayerLabel, JLayeredPane.MODAL_LAYER);

      activeRankLabel.setText("Current rank is " + activePlayer.getRank());
      activeRankLabel.setBounds(icon.getIconWidth() + 20, 0, 200, 500);
      bPane.add(activeRankLabel, JLayeredPane.PALETTE_LAYER);

      activeMoneyLabel.setText("Money: " + activePlayer.getMoney());
      activeMoneyLabel.setBounds(icon.getIconWidth() + 20, 0, 200, 540);
      bPane.add(activeMoneyLabel, JLayeredPane.PALETTE_LAYER);

      activeCreditsLabel.setText("Credits: " + activePlayer.getCredit());
      activeCreditsLabel.setBounds(icon.getIconWidth() + 20, 0, 200, 580);
      bPane.add(activeCreditsLabel, JLayeredPane.PALETTE_LAYER);

      activeRehearsalPointsLabel.setText("Rehearsal Points: " + activePlayer.getRehearsalPoints());
      activeRehearsalPointsLabel.setBounds(icon.getIconWidth() + 20, 0, 200, 620);
      bPane.add(activeRehearsalPointsLabel, JLayeredPane.PALETTE_LAYER);
   }

   /**
    * Sets all the buttons on the tight hand pane
    */
   public void setButtons() {

      bMove = new JButton("Move");
      bMove.setBackground(Color.white);
      bMove.setBounds(icon.getIconWidth() + 10, 30, 150, 20);

      bTakeRole = new JButton("Take Role");
      bTakeRole.setBackground(Color.white);
      bTakeRole.setBounds(icon.getIconWidth() + 10, 60, 150, 20);

      bUpgradeRank = new JButton("Upgrade Rank");
      bUpgradeRank.setBackground(Color.white);
      bUpgradeRank.setBounds(icon.getIconWidth() + 10, 90, 150, 20);

      bAct = new JButton("Act");
      bAct.setBackground(Color.white);
      bAct.setBounds(icon.getIconWidth() + 10, 120, 150, 20);

      bRehearse = new JButton("Rehearse");
      bRehearse.setBackground(Color.white);
      bRehearse.setBounds(icon.getIconWidth() + 10, 150, 150, 20);

      bEndTurn = new JButton("End Turn");
      bEndTurn.setBackground(Color.white);
      bEndTurn.setBounds(icon.getIconWidth() + 10, 180, 150, 20);

      bPane.add(bTakeRole, JLayeredPane.PALETTE_LAYER);
      bPane.add(bUpgradeRank, JLayeredPane.PALETTE_LAYER);
      bPane.add(bMove, JLayeredPane.PALETTE_LAYER);
      bPane.add(bEndTurn, JLayeredPane.PALETTE_LAYER);
      bPane.add(bAct, JLayeredPane.PALETTE_LAYER);
      bPane.add(bRehearse, JLayeredPane.PALETTE_LAYER);
   }

   /**
    * setPlayerOnTrailer and all of the similar functions place the player on the
    * set, slightly away from each other
    * 
    * @param currentPlayer
    * @param playerNumber
    */
   public void setPlayerOnTrailer(Player currentPlayer, int playerNumber) {
      currentPlayer.playerlabel.setIcon(currentPlayer.pIcon);
      currentPlayer.playerlabel.setBounds(1000 + (playerNumber * 15), 300, currentPlayer.pIcon.getIconWidth(),
            currentPlayer.pIcon.getIconHeight());
      currentPlayer.playerlabel.setVisible(true);
      bPane.add(currentPlayer.playerlabel, JLayeredPane.DRAG_LAYER);
   }

   /**
    * sets the player on location
    * 
    * @param currentPlayer
    * @param playerNumber
    */
   public void setPlayerOnMain(Player currentPlayer, int playerNumber) {
      currentPlayer.playerlabel.setIcon(currentPlayer.pIcon);
      currentPlayer.playerlabel.setBounds(810 + (playerNumber * 5), 100, currentPlayer.pIcon.getIconWidth(),
            currentPlayer.pIcon.getIconHeight());
      currentPlayer.playerlabel.setVisible(true);
      bPane.add(currentPlayer.playerlabel, JLayeredPane.DRAG_LAYER);
   }

   /**
    * sets the player on location
    * 
    * @param currentPlayer
    * @param playerNumber
    */
   public void setPlayerOnJail(Player currentPlayer, int playerNumber) {
      currentPlayer.playerlabel.setIcon(currentPlayer.pIcon);
      currentPlayer.playerlabel.setBounds(290 + (playerNumber * 5), 170, currentPlayer.pIcon.getIconWidth(),
            currentPlayer.pIcon.getIconHeight());
      currentPlayer.playerlabel.setVisible(true);
      bPane.add(currentPlayer.playerlabel, JLayeredPane.DRAG_LAYER);
   }

   /**
    * sets the player on location
    * 
    * @param currentPlayer
    * @param playerNumber
    */
   public void setPlayerOnStore(Player currentPlayer, int playerNumber) {
      currentPlayer.playerlabel.setIcon(currentPlayer.pIcon);
      currentPlayer.playerlabel.setBounds(290 + (playerNumber * 5), 390, currentPlayer.pIcon.getIconWidth(),
            currentPlayer.pIcon.getIconHeight());
      currentPlayer.playerlabel.setVisible(true);
      bPane.add(currentPlayer.playerlabel, JLayeredPane.DRAG_LAYER);
   }

   /**
    * sets the player on location
    * 
    * @param currentPlayer
    * @param playerNumber
    */
   public void setPlayerOnRanch(Player currentPlayer, int playerNumber) {
      currentPlayer.playerlabel.setIcon(currentPlayer.pIcon);
      currentPlayer.playerlabel.setBounds(270 + (playerNumber * 5), 650, currentPlayer.pIcon.getIconWidth(),
            currentPlayer.pIcon.getIconHeight());
      currentPlayer.playerlabel.setVisible(true);
      bPane.add(currentPlayer.playerlabel, JLayeredPane.DRAG_LAYER);
   }

   /**
    * sets the player on location
    * 
    * @param currentPlayer
    * @param playerNumber
    */
   public void setPlayerOnBank(Player currentPlayer, int playerNumber) {
      currentPlayer.playerlabel.setIcon(currentPlayer.pIcon);
      currentPlayer.playerlabel.setBounds(615 + (playerNumber * 5), 595, currentPlayer.pIcon.getIconWidth(),
            currentPlayer.pIcon.getIconHeight());
      currentPlayer.playerlabel.setVisible(true);
      bPane.add(currentPlayer.playerlabel, JLayeredPane.DRAG_LAYER);
   }

   /**
    * sets the player on location
    * 
    * @param currentPlayer
    * @param playerNumber
    */
   public void setPlayerOnHideout(Player currentPlayer, int playerNumber) {
      currentPlayer.playerlabel.setIcon(currentPlayer.pIcon);
      currentPlayer.playerlabel.setBounds(250 + (playerNumber * 5), 825, currentPlayer.pIcon.getIconWidth(),
            currentPlayer.pIcon.getIconHeight());
      currentPlayer.playerlabel.setVisible(true);
      bPane.add(currentPlayer.playerlabel, JLayeredPane.DRAG_LAYER);
   }

   /**
    * sets the player on location
    * 
    * @param currentPlayer
    * @param playerNumber
    */
   public void setPlayerOnChurch(Player currentPlayer, int playerNumber) {
      currentPlayer.playerlabel.setIcon(currentPlayer.pIcon);
      currentPlayer.playerlabel.setBounds(745 + (playerNumber * 5), 680, currentPlayer.pIcon.getIconWidth(),
            currentPlayer.pIcon.getIconHeight());
      currentPlayer.playerlabel.setVisible(true);
      bPane.add(currentPlayer.playerlabel, JLayeredPane.DRAG_LAYER);
   }

   /**
    * sets the player on location
    * 
    * @param currentPlayer
    * @param playerNumber
    */
   public void setPlayerOnHotel(Player currentPlayer, int playerNumber) {
      currentPlayer.playerlabel.setIcon(currentPlayer.pIcon);
      currentPlayer.playerlabel.setBounds(1020 + (playerNumber * 5), 460, currentPlayer.pIcon.getIconWidth(),
            currentPlayer.pIcon.getIconHeight());
      currentPlayer.playerlabel.setVisible(true);
      bPane.add(currentPlayer.playerlabel, JLayeredPane.DRAG_LAYER);
   }

   /**
    * sets the player on location
    * 
    * @param currentPlayer
    * @param playerNumber
    */
   public void setPlayerOnTrainStation(Player currentPlayer, int playerNumber) {
      currentPlayer.playerlabel.setIcon(currentPlayer.pIcon);
      currentPlayer.playerlabel.setBounds(20 + (playerNumber * 5), 220, currentPlayer.pIcon.getIconWidth(),
            currentPlayer.pIcon.getIconHeight());
      currentPlayer.playerlabel.setVisible(true);
      bPane.add(currentPlayer.playerlabel, JLayeredPane.DRAG_LAYER);
   }

   /**
    * sets the player on location
    * 
    * @param currentPlayer
    * @param playerNumber
    */
   public void setPlayerOnSaloon(Player currentPlayer, int playerNumber) {
      currentPlayer.playerlabel.setIcon(currentPlayer.pIcon);
      currentPlayer.playerlabel.setBounds(610 + (playerNumber * 5), 400, currentPlayer.pIcon.getIconWidth(),
            currentPlayer.pIcon.getIconHeight());
      currentPlayer.playerlabel.setVisible(true);
      bPane.add(currentPlayer.playerlabel, JLayeredPane.DRAG_LAYER);
   }

   /**
    * sets the player on location
    * 
    * @param currentPlayer
    * @param playerNumber
    */
   public void setPlayerOnOffice(Player currentPlayer, int playerNumber) {
      currentPlayer.playerlabel.setIcon(currentPlayer.pIcon);
      currentPlayer.playerlabel.setBounds(20 + (playerNumber * 5), 470, currentPlayer.pIcon.getIconWidth(),
            currentPlayer.pIcon.getIconHeight());
      currentPlayer.playerlabel.setVisible(true);
      bPane.add(currentPlayer.playerlabel, JLayeredPane.DRAG_LAYER);
   }

   /**
    * adds the mouselintener to the button
    * 
    * @param move
    */
   public void addMoveListener(MouseListener move) {
      bMove.addMouseListener(move);
   }

   /**
    * adds the mouselintener to the button
    * 
    * @param takeRole
    */
   public void addTakeRoleListener(MouseListener takeRole) {
      bTakeRole.addMouseListener(takeRole);
   }

   /**
    * adds the mouselintener to the button
    * 
    * @param upgradeRank
    */
   public void addUpgradeListener(MouseListener upgradeRank) {
      bUpgradeRank.addMouseListener(upgradeRank);
   }

   /**
    * adds the mouselintener to the button
    * 
    * @param act
    */
   public void addActListener(MouseListener act) {
      bAct.addMouseListener(act);
   }

   /**
    * adds the mouselintener to the button
    * 
    * @param rehearse
    */
   public void addRehearseListener(MouseListener rehearse) {
      bRehearse.addMouseListener(rehearse);
   }

   /**
    * adds the mouselintener to the button
    * 
    * @param endTurn
    */
   public void addEndTurnListener(MouseListener endTurn) {
      bEndTurn.addMouseListener(endTurn);
   }

   /**
    * option popup for choosing roome
    * 
    * @param rooms
    * @return
    */
   public String chooseRoom(String[] rooms) {
      JDialog.setDefaultLookAndFeelDecorated(true);
      moveChoices = new JOptionPane(rooms);
      moveChoices.setVisible(true);
      String choice = (String) JOptionPane.showInputDialog(null, "Choose a Room", "Move to?",
            JOptionPane.QUESTION_MESSAGE, null, rooms, rooms[0]);
      return choice;
   }

   /**
    * option popup for choosing role
    * 
    * @param roles
    * @param allowed
    * @return
    */
   public String chooseRole(String[] roles, boolean allowed) {
      if (!allowed) {
         JDialog.setDefaultLookAndFeelDecorated(true);
         roleChoicesInvalid = new JOptionPane(roles);
         roleChoicesInvalid.setVisible(true);
         JOptionPane.showMessageDialog(roleChoicesInvalid, "You cannot take a role, nice try!.");
         return "";
      }
      JDialog.setDefaultLookAndFeelDecorated(true);
      roleChoices = new JOptionPane(roles);
      roleChoices.setVisible(true);
      String choice = (String) JOptionPane.showInputDialog(null, "Choose a Role", "Choose Role",
            JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);
      return choice;
   }

   /**
    * Sets the player on the role
    * 
    * @param currentPlayer
    * @param x
    * @param y
    * @param set
    */
   public void setPlayerOnRole(Player currentPlayer, int x, int y, Set set) {
      if (currentPlayer.getOnCard()) {
         x += set.xCoord;
         y += set.yCoord;
      }
      currentPlayer.playerlabel.setIcon(currentPlayer.pIcon);
      currentPlayer.playerlabel.setBounds(x, y, currentPlayer.pIcon.getIconWidth(),
            currentPlayer.pIcon.getIconHeight());
      currentPlayer.playerlabel.setVisible(true);
      bPane.add(currentPlayer.playerlabel, JLayeredPane.DRAG_LAYER);
   }

   /**
    * shows an invalid choice
    */
   public void displayInvalidChoice(String error) {
      JDialog.setDefaultLookAndFeelDecorated(true);
      roleChoicesInvalid = new JOptionPane();
      roleChoicesInvalid.setVisible(true);
      JOptionPane.showMessageDialog(roleChoicesInvalid, error);
   }

   /**
    * show what the user rolled and say whether they won or lost
    * 
    * @param win
    * @param roll
    */
   public void displayReward(boolean win, int roll) {
      JDialog.setDefaultLookAndFeelDecorated(true);
      roleChoicesInvalid = new JOptionPane();
      roleChoicesInvalid.setVisible(true);
      if (win) {
         JOptionPane.showMessageDialog(roleChoicesInvalid, "You rolled a " + String.valueOf(roll) + " and won!");
      } else {
         JOptionPane.showMessageDialog(roleChoicesInvalid, "You rolled a " + String.valueOf(roll) + " and lost :(");
      }
   }

   /**
    * option pane to upgrade the player's rank
    * 
    * @return
    */
   public String upgradeRank() {
      JDialog.setDefaultLookAndFeelDecorated(true);
      String options[] = new String[2];
      options[0] = "Dollars";
      options[1] = "Credits";
      roleChoices = new JOptionPane(options);
      roleChoices.setVisible(true);
      String choice = (String) JOptionPane.showInputDialog(null, "How would you like to Pay", "Choose Payment Method",
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
      return choice;
   }

   /**
    * shows the final winner and ends program
    * 
    * @param name
    */
   public void finishGameView(String name) {
      JDialog.setDefaultLookAndFeelDecorated(true);
      roleChoicesInvalid = new JOptionPane();
      roleChoicesInvalid.setVisible(true);
      JOptionPane.showMessageDialog(roleChoicesInvalid, "The game is over. " + name + " won!!!");
      System.exit(0);
   }

}