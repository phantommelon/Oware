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

import java.util.ArrayList;
import java.util.List;

/**
 * An object representing the state of the game.
 * 
 * @author Alistair Madden
 * @version 0.5
 */
public class GameImpl implements Game {
    
    private final Board board;
    private final Player player1;
    private final Player player2;
    private String player1Name;
    private String player2Name;
    private byte turn;
    private int consecutiveMoves;
    private boolean isFinished;
    private final List<Board> previousBoards;
    
    /**
     * Constructor for the GameImpl class.
     * 
     * @param player1 a player.
     * @param player2 another player.
     */
    public GameImpl(Player player1, Player player2) {
        
        if(player1 == null || player2 == null) {
            throw new IllegalArgumentException("Invalid input - neither " +
                    "player used to create a game should be null.");
        }
        
        this.player1 = player1;
        this.player2 = player2;
        
        player1Name = "Player 1";
        player2Name = "Player 2";
        board = new BoardImpl();
        turn = 1;
        previousBoards = new ArrayList<>();
        consecutiveMoves = 0;
        isFinished = false;
    }
    
    /**
     * Copy constructor.
     * 
     * This is to allow an instance of GameImpl to be created with exactly the
     * same parameters.
     * 
     * @param currentBoard Board of the game.
     * @param player1 Player1 in the game.
     * @param player2 Player2 in the game.
     * @param player1Name Player1's name.
     * @param player2Name Player2's name.
     * @param turn Byte indicating whose turn it is.
     * @param consecutiveMoves Number of moves without a capture.
     * @param isFinished Boolean to signal if the game is finished.
     * @param previousBoards List of previous boards.
     */
    public GameImpl(Board currentBoard, Player player1, Player player2, 
            String player1Name, String player2Name, byte turn, 
            int consecutiveMoves, boolean isFinished, List<Board> previousBoards
            ) {
        
        this.board = currentBoard;
        this.consecutiveMoves = consecutiveMoves;
        this.isFinished = isFinished;
        this.player1 = player1;
        this.player1Name = player1Name;
        this.player2 = player2;
        this.player2Name = player2Name;
        this.previousBoards = previousBoards;
        this.turn = turn;
    }
    
    /**
     * A method to fetch the player whose turn it currently is.
     * 
     * @return a player representing the current player.
     */
    @Override
    public Player getCurrentPlayer() {
        
        if(turn == 1) {
            return player1;
        }
        // Otherwise turn == 2.
        else {
            return player2;
        }
    }

    /**
     * A method to get the number of the player whose turn it currently is.
     * 
     * @return an <code>int</code> representing the number of the current
     * player in the context of the game.
     */
    @Override
    public int getCurrentPlayerNum() {
        return turn;
    }

    /**
     * A method to get the board used in the game.
     * 
     * @return the <code>Board</code> object the game is taking place on.
     */
    @Override
    public Board getCurrentBoard() {
        return board;
    }

    @Override
    public int getResult() {
        if(isFinished) {
            if(board.getScore(1) > board.getScore(2)) {
                return 1;
            }
            else if(board.getScore(1) < board.getScore(2)) {
                return 2;
            }
            else {
                return 0; // Only remaining option is that the result is a draw.
            }
            
        }
        else {
            return -1; // The game is still in play.
        }
    }

    @Override
    public boolean positionRepeated() {
        for(Board oldBoard : previousBoards) {
            if(board.equals(oldBoard)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void nextMove() throws InvalidHouseException, InvalidMoveException, 
            QuitGameException {
        
        // End game conditions.
        if(noPossibleMove() || positionRepeated()) {
            captureAllSeeds();
            isFinished = true;
            return;
        }
        
        else if(consecutiveMoves == 100) {
            isFinished = true;
            return;
        }
        
        else if(board.getScore(1) > 24 || board.getScore(2) > 24 || 
                (board.getScore(1) + board.getScore(2)) == 48) {
            isFinished = true;
            return;
        }
        
        Board previousBoard = board.clone();
        
        int previousP1Score = board.getScore(1);
        int previousP2Score = board.getScore(2);

        // Board.makeMove throws the InvalidHouse/InvalidMove exceptions. 
        // Player.getMove throws the QuitGameException.
        board.makeMove(getCurrentPlayer().getMove(board.clone(), turn), 
                getCurrentPlayerNum());
        
        // Add the previous board to the list of previous boards.
        previousBoards.add(previousBoard);
        
        if(previousP1Score == board.getScore(1) && 
                previousP2Score == board.getScore(2)) {
            consecutiveMoves ++;
        }
        else {
            consecutiveMoves = 0;
        }
        
        turn = (byte) (3 - turn);
        
    }

    @Override
    public String toString() {
        
        String newLine = System.getProperty("line.separator");
        
        // All fields are needed for reconstruction of the game.
        String output = new String();
        output += board.toString() + newLine;
        output += player1.getClass().getName() + newLine;
        output += player2.getClass().getName() + newLine;
        output += player1Name + newLine;
        output += player2Name + newLine;
        output += turn + newLine;
        output += consecutiveMoves + newLine;
        output += isFinished + newLine;
        
        // ; separates boards.
        for(Board previousBoard : previousBoards) {
            output += previousBoard.toString() + ";";
        }
        
        return output;
    }

    /**
     * Captures all seeds in houses and adds to the house owner's score.
     * 
     * @throws InvalidHouseException if {@link 
     * BoardImpl#indexToHouseConversion(int) indexToHouseConversion} or {@link
     * BoardImpl#houseToIndexConversion(int, int) indexToHouseConversion} have
     * failed.
     */
    private void captureAllSeeds() throws InvalidHouseException {
        
        int seeds;
        
        for(int i = 1; i < 3; i++) {
            
            for(int j = 1; j < 7; j++) {
                
                seeds = board.getSeeds(j, i) + board.getScore(i);
                board.setScore(seeds, i);
            }
        }
    }

    /**
     * Checks if a possible move can be made by the current player.
     * 
     * @return <code>true</code> if no move can be made (houses are all empty)
     * <code>false</code> otherwise.
     * 
     * @throws InvalidHouseException If the parameters to {@link 
     * Board#getSeeds(int, int) getSeeds(int house, int playerNum)} are
     * incorrect.
     */
    private boolean noPossibleMove() throws InvalidHouseException {
        for(int i = 1; i < 7; i++) {
            if(board.getSeeds(i, turn) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a string representation of the specified player's name.
     * 
     * @param playerNum The number of the <code>Player</code> in the context of
     * the current game.
     * 
     * @return a <code>String</code> object containing the player's name.
     */
    public String getPlayerName(int playerNum) {
        
        checkValidPlayer(playerNum);
        
        if(playerNum == 1) {
            return player1Name;
        }
        // Only other option is playerNum == 2.
        else {
            return player2Name;
        }
        
    }

    /**
     * Sets the specified player's name to the value provided.
     * 
     * @param playerNum the number of the <code>Player</code> in the context of
     * the current game.
     * @param playerName a String to assign to the player.
     */
    public void setPlayerName(int playerNum, String playerName) {
        
        checkValidPlayer(playerNum);
        
        if(playerNum == 1) {
            this.player1Name = playerName;
        }
        // Only other option is playerNum == 2.
        else {
            this.player2Name = playerName;
        }

    }
    
    /**
     * Utility method to check if a valid player number has been entered.
     * 
     * @param playerNum the number of the player to be checked.
     */
    private void checkValidPlayer(int playerNum) {
        
        if(!(playerNum == 1 || playerNum ==2)) {
            throw new IllegalArgumentException("Invalid input - player " +
                    "number can only be 1 or 2.\n");
        }
    }
    
}
