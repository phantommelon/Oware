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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Class to manage the game.
 * 
 * The GameManagerImpl allows users to start, load, save and play games.
 * 
 * @author Alistair Madden
 * @version 0.3
 */
public class GameManagerImpl implements GameManager {
    
    private Game game;
    private String mainMenu;
    private InputStream in;
    private PrintStream out;
    private Scanner scanner;
    
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
        
        File path = new File(fname);
        
        if(path.exists()) {
           
            Scanner loadScanner;
            
            try {
                loadScanner = new Scanner(path);
            } 
            catch (FileNotFoundException ex) {
                throw new FileFailedException("File " + path + " not found. " +
                        "Please try again.\n");
            }
            
            List<String> lines = new ArrayList<>();
            
            while(loadScanner.hasNextLine()) {
                lines.add(loadScanner.nextLine());
            }
            
            // Check for files of 8 lines long.
            if(!(lines.size() == 8)) {
                throw new FileFailedException("File is malformatted. Please " +
                        "check the right file was selected.\n");
            }
            
            // Fields that need to be determined to recreate game.
            Board currentBoard = processBoardString(lines.get(0));
            String player1Type = lines.get(1);
            String player2Type = lines.get(2);           
            Player player1;
            Player player2;
            Byte turn = Byte.parseByte(lines.get(5));
            int consecutiveMoves = Integer.parseInt(lines.get(6));
            boolean gameOver = Boolean.getBoolean(lines.get(7));
            List<Board> prevousBoards = new ArrayList<>();
            
            // Strings needing further analysis.
            String player1Name = lines.get(3);
            String player2Name = lines.get(4);
            String[] previousBoardStrings = lines.get(8).split(";");
            
            for(String previousBoard : previousBoardStrings) {
                prevousBoards.add(processBoardString(previousBoard));
            }

            if(player1Type.equals("HumanPlayer")) {
                player1 = new HumanPlayer();
            }
            else {
                player1 = new ComputerPlayer();
            }
            
            if(player2Type.equals("HumanPlayer")) {
                player2 = new HumanPlayer();
            }
            else {
                player2 = new ComputerPlayer();
            }
            
            player1.setIn(in);
            player1.setOut(out);
            player2.setIn(in);
            player2.setOut(out);
            
            this.game = new GameImpl(currentBoard, player1, player2, 
                    player1Name, player2Name, turn, consecutiveMoves, gameOver, 
                    prevousBoards);
        }
        else {
            throw new FileFailedException("The file specified does not exist.");
        }
    }

    @Override
    public void saveGame(String fname) {
        
        if(game == null) {
            out.println("Invalid input - no game is currently underway.\n");
        }
        else if(game.getResult() != -1) {
            out.println("Invalid input - this game is finished.\n");
        }
        else {
            
            File path = new File(fname);
            
            if(path.exists()) {
                
                out.println("File " + fname + " already exists, do you want " +
                        "to overwrite? (Y/N)");
                
                String saveInput;
                
                // Check for user input.
                do {
                    saveInput = scanner.nextLine();
                    
                    if(saveInput.equalsIgnoreCase("N")) {
                        return;
                    }
                    else if(saveInput.equalsIgnoreCase("Y")) {
                        break;
                    }
                }
                while(!(saveInput.equalsIgnoreCase("Y") || 
                        saveInput.equalsIgnoreCase("N")));
            }
            
            out.println("Saving...");
            
            String stringOut = game.toString();
            
            try {
                FileWriter fw = new FileWriter(path);
                fw.write(stringOut);
                fw.flush();
                fw.close();
            } 
            catch (IOException ex) {
                out.println("An I/O error has occured. Please try again.\n");
            }
            
            out.println("Save successful! Returning to the main menu...\n");   
        }
    }

    @Override
    public int playGame() throws QuitGameException {
        
        while(!(game.getResult() == 0 || game.getResult() == 1 ||
                game.getResult() == 2)) {
            
            String gameRep = game.toString();
            
            // Splits each line received.
            String[] reports = gameRep.split(System.getProperty(
                    "line.separator"));
            
            // Board state split house seeds from player score.
            String[] boardReport = reports[0].split(":");
            
            // Contains the seeds remaining in the houses.
            String[] houseReport = boardReport[0].split(" ");
            
            // Contains the score of players (player 1 then player 2).
            String[] scoreReport = boardReport[1].trim().split(" ");
            
            // Contains the player's names.
            String[] nameReport = Arrays.copyOfRange(reports, 3, 5);
            
            // List current scores.
            for(int i = 0; i < 2; i++) {
                out.println(nameReport[i] + ": " + scoreReport[i]);
            }
            
            // List player to play.
            out.println("\n" + nameReport[Integer.parseInt(reports[5]) - 1] +
                    " to play.\n");
            
            // List houses and their seed values.
            for(int i = 0; i < 12; i++) {
                out.println("House " + (i+1) + " contains " + houseReport[i] +
                        " seeds.");
            }
            
            out.println();
            
            try {
                game.nextMove();
            } 
            catch(InvalidHouseException | InvalidMoveException |
                    IllegalArgumentException | IllegalStateException ex) {
                out.println(ex.getMessage());
            }
        }
        
        return game.getResult();
    }

    @Override
    public Game manage(InputStream in, PrintStream out) {
        
        this.in = in;
        this.out = out;
        
        scanner = new Scanner(this.in);
        
        int result = -1;
        
        // Game will be returned when a result has been reached.
        while(result == -1) {

            // Main menu is accessed on initialisation without a file path
            // or by the QUIT command while a game is running. In either
            // case, the menu of options needs to be displayed.
            out.println(mainMenu);
            out.print("Please enter a command: ");

            String command;
            
            command = scanner.nextLine();
                
            // Split string from input around a space character.
            List<String> commands = Arrays.asList(command.split(" "));
            
            // Reusing command.
            command = commands.get(0);
            
            if(command.equals("NEW")) {
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
                    out.println(qge.getMessage() + "\n");
                }

                displayResults(result);
                
                result = -1;
            }
            else if(command.equals("LOAD")) {
                
                if(checkValidIOCommand(commands)) {
                    try {
                        loadGame(commands.get(1));
                    } 
                    catch (FileFailedException ex) {
                        out.println(ex.getMessage() + "\n");
                    }
                }
                else {
                    out.println("Invalid input - a LOAD command should be " +
                            "followed by the file name of the saved game to " +
                            "load. The file name should be separated from " +
                            "the LOAD command by a space.\n");
                }
                
                try {
                    result = playGame();
                }
                catch(QuitGameException qge) {
                    out.println(qge.getMessage() + "\n");
                }

                displayResults(result);
                
                result = -1;
            }
            else if(command.equals("SAVE")) {
                if(checkValidIOCommand(commands)) {
                    saveGame(commands.get(1));
                }
                else {
                    out.println("Invalid input - a SAVE command should be  " +
                            "followed by a file name to save the current " + 
                            "game under. The file name should be separated " +
                            "from the SAVE command by a space.\n");
                }
            }
            else if(command.equals("EXIT")) {
                return this.game;
            }
            else {
                out.println("Invalid input - valid commands are listed " +
                        "in the main menu.\n");
            }
        }
        
        // Don't think the program will ever get here as result is reset to -1
        // after printing results.
        return this.game;
        
    }
    
    @Override
    public int compare(Player o1, Player o2) {
        
        game = new GameImpl(o1, o2);
        int result1;
        
        try {
            result1 = playGame();
        } 
        catch (QuitGameException ex) {
            // Check current player does not change after ex is thrown.
            result1 = 3 - game.getCurrentPlayerNum();
        }
        
        game = new GameImpl(o2, o1);
        int result2;
        
        try {
            result2 = playGame();
        } 
        catch (QuitGameException ex) {
            // Check current player does not change after ex is thrown.
            result2 = 3 - game.getCurrentPlayerNum();
        }
        
        // Conditions for equality, o1 better, o2 better respectively.
        if((result1 == 0 && result2 == 0) || (result1 == 1 && result2 == 2) ||
                (result1 == 2 && result1 == 1)) {
            return 0;
        }
        else if((result1 == 1 && result2 == 0) || (result1 == 0 && result2 == 1)
                || (result1 == 1 && result2 == 1)) {
            return 1;
        }
        else {
            return -1;
        }
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
        
        // Does this really belong here?
        IllegalArgumentException ex = new IllegalArgumentException("Invalid " +
                "input - A NEW command must be followed by two further " + 
                "commands, 'Human' or 'Computer' to specify the type of " + 
                "player. Optionally a colon may be added after the player " + 
                "type to give a name to the player.\n");
        
        String[] player1;
        String[] player2;
        Player[] players;
        
        if(commands.size() == 3) {
            player1 = commands.get(1).split(":", 2);
            player2 = commands.get(2).split(":", 2);
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
            
            players[0].setIn(in);
            players[1].setIn(in);
            players[0].setOut(out);
            players[1].setOut(out);
            
            GameImpl newGame = new GameImpl(players[0], players[1]);

            // Assigning player names.
            if(!(player1.length == 1)) {
                newGame.setPlayerName(1, player1[1]);
            }

            if(!(player2.length == 1)) {
                newGame.setPlayerName(2, player2[1]);
            }
            
            return newGame;
        }
        else {
            throw ex;
        }
    }
    
    private boolean checkValidIOCommand(List<String> commands) {
        return commands.size() == 2;
    }
    
    /**
     * Processes the String given by Board's toString returning a copy of the
     * board with the same properties.
     * 
     * @param currentBoardString string to be turned into a board.
     * @return Board object representing a string.
     */
    private Board processBoardString(String currentBoardString) {
        
        String boardString = currentBoardString.split(":")[0].trim();
        String scoreString = currentBoardString.split(":")[1].trim();
        
        String[] housesStrings = boardString.split(" ");
        
        int player1Score = Integer.parseInt(scoreString.split(" ")[0]);
        int player2Score = Integer.parseInt(scoreString.split(" ")[1]);
        int[] houseInts = new int[12];
        
        for(int i = 0; i < 12; i++) {
            houseInts[i] = Integer.parseInt(housesStrings[i]);
        }
        
        return new BoardImpl(houseInts, player1Score, player2Score);
    }
    
    private void displayResults(int result) {

        if(result != 0) {
            out.println("Player " + result + " is the winner!\n");
        }
        else {
            out.println("The game was a draw!\n");
        }
    }
    
    public static void main(String[] args) {
        
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
            gameManager.manage(System.in, System.out);
        }
    }
    
}
