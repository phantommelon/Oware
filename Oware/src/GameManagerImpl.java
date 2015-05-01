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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    private InputStream in;
    private PrintStream out;
    private Scanner scanner;
    private String mainMenu = 
            "|| Main menu                               ||\n" +
            "||-----------------------------------------||\n" +
            "|| NEW - Start a new game of Oware         ||\n" +
            "|| LOAD - Load a previous game of Oware    ||\n" +
            "|| SAVE - Save progress of current game    ||\n" +
            "|| EXIT - Close the program without saving ||\n";
            
    public GameManagerImpl() {
        
    }
    
    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void loadGame(String fname) throws FileFailedException {
        
        File path = new File(fname);
           
        Scanner loadScanner;

        try {
            loadScanner = new Scanner(path);
        } 
        catch (FileNotFoundException ex) {
            throw new FileFailedException("File " + path + " not found. " +
                    "Please try again.");
        }

        List<String> lines = new ArrayList<>();

        while(loadScanner.hasNextLine()) {
            lines.add(loadScanner.nextLine());
        }

        FileFailedException fileFormatFailed = new FileFailedException(
                "File " + path + " is either of incorrect format, or is " +
                "corrupt. Please check the file and try again.");

        // Check for files of 8-9 lines long. (Could still be 9 lines long but
        // rubbish...)
        if(!(lines.size() == 9 || lines.size() == 8)) {
            throw fileFormatFailed;
        }

        // Check first line (current board state) can be parsed.
        Board currentBoard;

        try {
            currentBoard = processBoardString(lines.get(0));
        }
        catch (NumberFormatException nfe) {
            throw fileFormatFailed;
        }

        // Check second and third line are as expected.
        String player1Type = lines.get(1);
        String player2Type = lines.get(2);
        Player player1;
        Player player2;

        if(player1Type.equals("HumanPlayer")) {
            player1 = new HumanPlayer();
        }
        else if(player1Type.equals("ComputerPlayer")) {
            player1 = new ComputerPlayer();
        }
        else {
            throw fileFormatFailed;
        }

        if(player2Type.equals("HumanPlayer")) {
            player2 = new HumanPlayer();
        }
        else if(player2Type.equals("ComputerPlayer")) {
            player2 = new ComputerPlayer();
        }
        else {
            throw fileFormatFailed;
        }

        // As the players can be called anything at all, any String will do.
        String player1Name = lines.get(3);
        String player2Name = lines.get(4);

        // Check sixth and seventh lines can be processed.
        Byte turn;
        int consecutiveMoves;

        try {
            turn = Byte.parseByte(lines.get(5));
            consecutiveMoves = Integer.parseInt(lines.get(6));
        }
        catch (NumberFormatException ex) {
            throw fileFormatFailed;
        }

        // Check eighth line is "true" or "false";
        boolean gameOver;

        if(lines.get(7).equals("true")) {
            gameOver = true;
        }
        else if(lines.get(7).equals("false")) {
            gameOver = false;
        }
        else {
            throw fileFormatFailed;
        }
        
        List<Board> prevousBoards = new ArrayList<>();
        
        if(lines.size() == 9) {
            // Split the ninth line into boards. Ensure each is correct.
            String[] previousBoardStrings = lines.get(8).split(";");

            try {
                for(String previousBoard : previousBoardStrings) {
                    prevousBoards.add(processBoardString(previousBoard));
                }
            }
            catch(NumberFormatException nfe) {
                throw fileFormatFailed;
            }
        }
        

        

        // If input or print streams are null (because the game was instantiated
        // from the command line) then use default System.in and System.out
        if(in == null) {
            in = System.in;
        }
        
        if(out == null) {
            out = System.out;
        }
        
        player1.setIn(in);
        player2.setIn(in);
        player1.setOut(out);
        player2.setOut(out);

        this.game = new GameImpl(currentBoard, player1, player2, 
                player1Name, player2Name, turn, consecutiveMoves, gameOver, 
                prevousBoards);
        
    }

    @Override
    public void saveGame(String fname) throws FileFailedException {
        
        if(fname.equals(" ")) {
            throw new FileFailedException("Invalid input - A SAVE command is " +
                    "followed by a file name to save the file under.");
        }
        
        if(game == null) {
            throw new FileFailedException("Invalid input - No game is " + 
                    "currently underway.");
        }
        else {
            
            File path = new File(fname);
            
            if(path.exists()) {
                throw new FileFailedException("Invalid input - File " + fname +
                        " already exists.");
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
            
            // Check if the player is human, if not, they are a computer.
            if(game.getCurrentPlayer().getClass().getCanonicalName().equals(
                    "HumanPlayer")) {
                try {
                    game.nextMove();
                } 
                catch(InvalidHouseException | InvalidMoveException |
                        IllegalArgumentException | IllegalStateException ex) {
                    out.println(ex.getMessage() + "\n");
                }
            }
            else {
                
                // Use concurrency to handle computer timeout.
                ExecutorService handler = Executors.newSingleThreadExecutor();
                Future<Void> future = handler.submit(new ComputerMove(game));
                
                try {
                    future.get(1, TimeUnit.SECONDS);
                }
                // Computer player's makeMove has thrown an exception or elapsed
                // 1 second computation time - forfeit game.
                catch (ExecutionException | TimeoutException ex) {
                    return 3 - game.getCurrentPlayerNum();
                } 
                catch (InterruptedException ex) {
                    throw new IllegalArgumentException(ex.getMessage());
                }
                // Get rid of thread generated now it is not needed.
                finally {
                    handler.shutdownNow();
                }
                
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

            String scannedInput = scanner.nextLine();
                
            // Split string from input around a space character.
            String[] commands = scannedInput.split(" ", 2);
            
            String identifyingCommand = commands[0];
            
            if(identifyingCommand.equals("NEW")) {
                
                if(commands.length == 2) {
                    try {
                        this.game = createNewGame(commands[1]);
                    }
                    catch(IllegalArgumentException ex) {
                        out.println(ex.getMessage());
                        continue;
                    }
                }
                else {
                    out.println("Invalid input - A NEW command must be " +
                            "followed by two further commands, 'Human' or " +
                            "'Computer' to specify the type of player. " +
                            "Optionally a colon may be added after the " +
                            "player type to give a name to that player.\n");
                    continue;
                }
                
                try {
                    result = playGame();
                }
                catch(QuitGameException qge) {
                    out.println(qge.getMessage() + "\n");
                    continue;
                }

                displayResults(result);
                
                result = -1;
            }
            
            else if(identifyingCommand.equals("LOAD")) {
                
                if(commands.length == 2) {
                    try {
                        loadGame(commands[1]);
                    }
                    catch (FileFailedException ex) {
                        out.println(ex.getMessage() + "\n");
                        continue;
                    }
                }
                else {
                    out.println("Invalid input - a LOAD command should be " +
                            "followed by the file name of the saved game to " +
                            "load. The file name should be separated from " +
                            "the LOAD command by a space.\n");
                    continue;
                }
                
                try {
                    result = playGame();
                }
                catch(QuitGameException qge) {
                    out.println(qge.getMessage() + "\n");
                    continue;
                }

                displayResults(result);
                
                result = -1;
            }
            
            else if(identifyingCommand.equals("SAVE")) {
                
                if(commands.length == 2) {
                    try {
                        saveGame(commands[1]);
                    } 
                    catch(FileFailedException ex) {
                        out.println(ex.getMessage() + "\n");
                    }
                }
                else {
                    out.println("Invalid input - a SAVE command should be " +
                            "followed by a file name to save the file under." +
                            "The file name should be separated from the SAVE " +
                            "command by a space.\n");
                    // Unecessary to continue. 
                }
            }
            
            else if(identifyingCommand.equals("EXIT")) {
                return this.game;
            }
            
            else {
                out.println("Invalid input - Valid commands are listed " +
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
     * @param input the input the user has given following a command.
     * @return a new Game instance, set up according to the commands given
     */
    private Game createNewGame(String input) {
        
        IllegalArgumentException ex = new IllegalArgumentException("Invalid " +
                "input - A NEW command must be followed by two further " + 
                "commands, 'Human' or 'Computer' to specify the type of " + 
                "player. Optionally a colon may be added after the player " + 
                "type to give a name to the player.\n");
        
        String[] playerTypes = input.split(" ", 2);
        String[] player1;
        String[] player2;
        Player[] players;
        
        if(playerTypes.length != 2) {
            throw ex;
        }
        
        player1 = playerTypes[0].split(":", 2);
        player2 = playerTypes[1].split(":", 2);
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

        GameImpl gameImpl = (GameImpl) this.game;
        
        if(result != 0) {
            out.println(gameImpl.getPlayerName(result) + " is the winner!\n");
        }
        else {
            out.println("The game was a draw!\n");
        }
    }
    
    /**
     * An inner class implementing Callable.
     * <p>
     * Class has call method (analogous to Runnable's run) that allows game.
     * nextMove to run and throw exceptions in a new thread.
     */
    private class ComputerMove implements Callable<Void> {

        Game game;
        
        private ComputerMove(Game game) {
            this.game = game;
        }

        @Override
        public Void call() throws Exception {
            game.nextMove();
            return null;
        }
    }
    
    public static void main(String[] args) {
        
        GameManagerImpl gameManager = new GameManagerImpl();
        
        if(args.length > 0) {
            
            String fileName = "";
                    
            for(int i = 0; i < args.length; i++) {
                
                if(i == 0) {
                    fileName += args[i];
                }
                else {
                    fileName += " " + args[i];
                }
            }
            
            try {
                gameManager.loadGame(fileName);
            } 
            catch (FileFailedException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
            
            if(gameManager.getGame() != null) {
                try {
                    gameManager.playGame();
                }
            
                catch (QuitGameException ex) {
                    System.out.println(ex.getMessage() + "\n");
                }
            }

            gameManager.manage(System.in, System.out);
        }
        
        else {
            gameManager.manage(System.in, System.out);
        }
    }
    
}