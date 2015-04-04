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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An object representing the state of the game.
 * 
 * @author Alistair Madden
 * @version 0.5
 */
public class GameImpl implements Game, Serializable {

    private Player player1;
    private Player player2;
    private byte turn;
    private BoardImpl board;
    private List<Board> previousBoards;
    private int consecutiveMoves;
    private boolean isFinished;
    private QuitGameException endGame;
    
    /**
     * Constructor for the GameImpl class.
     * 
     * @param player1 a player.
     * @param player2 another player.
     */
    public GameImpl(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        
        board = new BoardImpl();
        turn = 1;
        previousBoards = new ArrayList<>();
        consecutiveMoves = 0;
        isFinished = false;
        endGame = new QuitGameException("Game over! Returning to the main " +
                "menu...");
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
        else if(turn == 2) {
            return player2;
        }
        else {
            return null; //Something went wrong
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
            throw endGame;
        }
        
        else if(consecutiveMoves == 100) {
            isFinished = true;
            throw endGame;
        }
        
        else if(board.getScore(1) > 24 || board.getScore(2) > 24 || 
                (board.getScore(1) + board.getScore(2)) == 48) {
            isFinished = true;
            throw endGame;
        }
        
        Board previousBoard = board.clone();
        
        int previousP1Score = board.getScore(1);
        int previousP2Score = board.getScore(2);

        // Throws the InvalidHouse/InvalidMove exceptions.
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
        String output = new String();
        output += getPlayerName(turn) + " to play.\n\n";
        output += board.toString();
        
        return output;
    }
    
    /**
     * Generates a string representation of the specified player's name.
     * 
     * @param playerNum The number of the <code>Player</code> in the context of
     * the current game.
     * 
     * @return a <code>String</code> object containing the player's name.
     */
    public String getPlayerName(byte playerNum) {
        
        String playerName;
        
        if(playerNum == 1) {
            if(!player1.isComputer()) {
                HumanPlayer player1H = (HumanPlayer) player1;
                playerName = player1H.getName();
            }
            else {
                ComputerPlayer player1C = (ComputerPlayer) player1;
                playerName = player1C.getName();
            }
        }
        else {
            if(!player2.isComputer()) {
                HumanPlayer player2H = (HumanPlayer) player2;
                playerName = player2H.getName();
            }
            else {
                ComputerPlayer player2C = (ComputerPlayer) player2;
                playerName = player2C.getName();
            }
        }
        
        // Default value given if no name specified on instantiation.
        if(playerName.equals("Player") || playerName.equals("Computer")) {
            playerName += " " + playerNum;
        }
        
        return playerName;
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
        for(int i = 0; i < 12; i++) {
                    
            int house = board.indexToHouseConversion(i);
            int playerNum = board.indexToPlayerConversion(i);
            int seeds = 0;

            seeds = board.getSeeds(house, playerNum) + 
                    board.getScore(playerNum);

            board.setScore(seeds, playerNum);
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
    
}
