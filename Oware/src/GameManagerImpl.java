/*
 * Copyright (C) 2015 Alistair Madden
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to manage the game.
 * 
 * The GameManagerImpl allows users to start, load, save and play games.
 * 
 * @author Alistair Madden
 * @version 0.3
 */
public class GameManagerImpl implements GameManager, Serializable {
    
    private Game game;
    private boolean inMainMenu;
    private String mainMenu;
    private InputStream in;
    private PrintStream out;
    
    
    public GameManagerImpl() {
        
        mainMenu = new String();
        
        mainMenu += "|| " + "Main menu" + "                               ||\n";
        mainMenu += "||-" + "-------------------------------" + "---------||\n";
        mainMenu += "|| " + "NEW - Start a new game of Oware" + "         ||\n";
        mainMenu += "|| " + "LOAD - Load a previous game of Oware" + "    ||\n";
        mainMenu += "|| " + "SAVE - Save progress of current game" + "    ||\n";
        mainMenu += "|| " + "EXIT - Close the program without saving" + " ||\n";
    }
    
    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void loadGame(String fname) throws FileFailedException {
        
        File fileInfo = new File(fname);
        
        if(fileInfo.exists()) {
            ObjectInputStream input;
            try {
                input = new ObjectInputStream(new FileInputStream(fileInfo));
            } 
            catch (IOException ex) {
                throw new FileFailedException("Please try again.");
            }
            try {
                game = (Game) input.readObject();
            } 
            catch (IOException ex) {
                throw new FileFailedException("Please try again.");
            } 
            catch (ClassNotFoundException ex) {
                throw new FileFailedException("Cannot instantiate class - no " +
                        "such class exists");
            }
        }
    }

    @Override
    public void saveGame(String fname) throws FileFailedException {
        
        File fileInfo = new File(fname);
        
        if(fileInfo.exists()) {
            throw new FileFailedException("File already exists.");
        }
        else {
            
            try {
                ObjectOutputStream output = new ObjectOutputStream(
                        new FileOutputStream(fileInfo));
                output.writeObject(game);
                output.flush();
                output.close();
            } 
            catch (IOException ex) {
                throw new FileFailedException("Something went wrong.");
            }
        }
    }

    @Override
    public int playGame() throws QuitGameException {
        
        while(!(game.getResult() == 0 || game.getResult() == 1 ||
                game.getResult() == 2)) {
            
            out.println("\n" + game.toString());
            
            try {
                game.nextMove();
            } 
            catch(InvalidHouseException | InvalidMoveException |
                    IllegalArgumentException | IllegalStateException ex) {
                out.println("\n" + ex.getMessage());
            }
        }
        
        return game.getResult();
    }

    @Override
    public Game manage(InputStream in, PrintStream out) {
        
//        try {
//            this.fIn = (FileInputStream) in;
//        }
//        // Not a FileInputStream.
//        catch(ClassCastException cce) {
        this.in = in;
//        }
        
        this.out = out;

        Scanner input;
        
//        if(fIn != null) {
//            input = new Scanner(fIn);
//        }
//        else {
            input = new Scanner(in);
//        }
        
        int result = -1;
        
        // Game will be returned when a result has been reached.
        while(result == -1) {

            // Main menu is accessed on initialisation without a file path
            // or by the QUIT command while a game is running. In either
            // case, the menu of options needs to be displayed.
            out.println(mainMenu);
            out.print("Please enter a command: ");

            String command = input.nextLine();
            
            // Split string from input around a space character.
            List<String> commands = Arrays.asList(command.split(" "));
            
            switch(commands.get(0)) {
                
                case "NEW": {
                    
                    try {
                        this.game = createNewGame(commands);
                    }
                    catch(IllegalArgumentException ex) {
                        out.println(ex.getMessage());
                        continue;
                    }

                    try {
                        result = playGame();
                    }
                    catch(QuitGameException qge) {
                        
                        out.println("\n" + qge.getMessage() + "\n");
                        
                        if(!qge.getMessage().contains("Game over")) {
                            continue;
                        }
                    }

                    if(result != 0) {
                        out.println("Player " + result + " is the winner!\n");
                    }
                    else if(result == 0) {
                        out.println("The game was a draw!\n");
                    }
                    else {
                        out.println("Something really bad has happened. Help!");
                    }
                }
                    
                
                case "LOAD": {
                    throw new UnsupportedOperationException("Not supported " +
                            "yet.");
                }
                    
                    
                case "SAVE": {
                    throw new UnsupportedOperationException("Not supported " +
                            "yet.");
                }
                
                case "EXIT": {
                    System.exit(0);
                }
                
                default: {
                    out.println("Invalid input - valid commands are listed " +
                            "in the main menu.\n");
                }
            }
        }
            
        return this.game;
    }
    
    @Override
    public int compare(Player o1, Player o2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Creates a new Game based on the commands given.
     * <p>
     * If the commands cannot be recognised, an IllegalArgumentException is 
     * thrown.
     * 
     * @param commands the list of String commands the user has given
     * @return a new Game instance, set up according to the commands given
     */
    private Game createNewGame(List<String> commands) {
        
        IllegalArgumentException ex = new IllegalArgumentException("Invalid " +
                "input - A NEW command must be proceeded by two further " + 
                "commands, 'Human' or 'Computer' to specify the type of " + 
                "player. Optionally a colon may be added after the player " + 
                "type to give a name to the player.\n");
        
        String[] player1;
        String[] player2;
        Player[] players;
        
        if(commands.size() == 3) {
            player1 = commands.get(1).split(":");
            player2 = commands.get(2).split(":");
            players = new Player[2];
            
            // Player1 creation.
            if(player1[0].equals("Human")) {
                players[0] = new HumanPlayer();
            }
            else if(player1[0].equals("Computer")) {
                players[0] = new ComputerPlayer();
            }
            else {
                throw ex;
            }
            
            // Player2 creation
            if(player2[0].equals("Human")) {
                players[1] = new HumanPlayer();
            }
            else if(player2[0].equals("Computer")) {
                players[1] = new ComputerPlayer();
            }
            else {
                throw ex;
            }
            
            // Will have definitely created a player when this code is reached.
//            if(fIn != null) {
//                try {
//                    fIn.getChannel().position(0);
//                    players[0].setIn(fIn);
//                    players[1].setIn(fIn);
//                } 
//                catch (IOException ex1) {
//                    Logger.getLogger(GameManagerImpl.class.getName()).log(Level.SEVERE, null, ex1);
//                }
//            }
//            else { 
//            }
            
            players[0].setIn(in);
            players[1].setIn(in);
            players[0].setOut(out);
            players[1].setOut(out);
            
            GameImpl newGame = new GameImpl(players[0], players[1]);
            newGame.setPlayerName(1, player1[1]);
            newGame.setPlayerName(2, player2[1]);
            return newGame;
        }
        else {
            throw ex;
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        
        GameManagerImpl gameManager = new GameManagerImpl();
        
        // Optional launch straight into game -- MODIFY
        if(args.length != 0) {
            
            String fileName = args[0];
            
            try {
                gameManager.loadGame(fileName);
            } 
            catch (FileFailedException ex) {
                System.out.println(ex.getMessage());
                System.exit(4);
            }
            
            try {
                gameManager.playGame();
            }
            
            catch (QuitGameException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        else {
            
//            gameManager.manage(System.in, System.out);
            
            gameManager.manage(new FileInputStream(new File("test.txt")),
                    System.out);
            
            
            
        }
    }
    
}
