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
import java.util.Arrays;

/**
 * An object representing the state of the Oware board.
 * 
 * The board stores the seeds in each of the houses and in the two score houses.
 * 
 * @author Alistair Madden
 * @version 0.4
 */
public class BoardImpl implements Board, Serializable {
    
    private final int[] houses;
    private int p1Score;
    private int p2Score;
    
    /**
     * Default constructor for BoardImpl class.
     */
    public BoardImpl() {
        houses = new int[12];
        
        //Set up 4 counters in each house
        Arrays.fill(houses, 4);
        
        p1Score = 0;
        p2Score = 0;
    }
    
    /**
     * Constructor to enable copying of the board.
     * 
     * @param houses an array of <code>int</code> representing the seeds in each
     * house.
     * @param p1Score player 1's score.
     * @param p2Score player 2's score.
     */
    public BoardImpl(int[] houses, int p1Score, int p2Score) {
        this.houses = houses;
        this.p1Score = p1Score;
        this.p2Score = p2Score;
    }
    
    @Override
    public void makeMove(int house, int playerNum) throws InvalidHouseException,
            InvalidMoveException {
        
        // Check house number and player number are valid.
        checkForInvalidHouseException(house, playerNum);
        
        // Make an array holding the opponent's houses.
        int[] opponentHouses = new int[6];
        System.arraycopy(houses, (3 - playerNum)*6 - 6, opponentHouses, 0, 6);
        
        // Check conditions for legal move.
        
        
        
        // Check selected house has non-zero number of seeds
        if(getSeeds(house, playerNum) == 0) {
            throw new InvalidMoveException("Invalid input - The selected " +
                    "house contains no seeds.");
        }
        
        // Check for starvation. If the opponent's houses are all empty, they
        // may be at the risk of starvation.
        if(Arrays.equals(opponentHouses, new int[6])) {
            
            // If a move has been made that will not put seeds on the opponent's
            // side of the board.
            if(!(getSeeds(house, playerNum) > 6 - house)) {
                
                // Check other houses.
                for(int i = 1; i < 7; i++) {
                    
                    // If selecting a different house would put seeds on the
                    // opponent's side of the board.
                    if(getSeeds(i, playerNum) > 6 - i) {
                        
                        throw new InvalidMoveException("Invalid input - It " +
                                "is forbidden to starve your opponent.");
                    }
                }
            }
        }
               
        // Move seems legit. Proceed.
            int seeds = getSeeds(house, playerNum);
            setSeeds(0, house, playerNum);

            int index = houseToIndexConversion(house, playerNum);

            // Only loop for the number of seeds.
            for(int i = 1; i < seeds + 1; i++) {

                // Look at the next house.
                index++;

                // Do not sow in starting house.
                if(!(i%12 == 0)) {
                    
                    sowSeed(indexToHouseConversion(index),
                            indexToPlayerConversion(index));
                }
                // Give an extra seed because it's technically not been sown.
                else {
                    seeds++;
                }
            }
            
            // After loop, index will be pointing at last house.
            seeds = getSeeds(indexToHouseConversion(index),
                            indexToPlayerConversion(index));
            
            // Reap seeds. 
            while((seeds == 2 || seeds == 3) && (indexToPlayerConversion(index)
                    == 3 - playerNum)) {

                addScore(seeds, playerNum);

                setSeeds(0, indexToHouseConversion(index), 
                        indexToPlayerConversion(index));
                
                index--;

                seeds = getSeeds(indexToHouseConversion(index),
                        indexToPlayerConversion(index));
            }
    }
    
    /**
     * Method to convert the index of the houses array to the player's house
     * number.
     * 
     * @param index position as understood by houses array.
     * @return an <code>int</code> representing the house of a player.
     */
    public int indexToHouseConversion(int index) {
        return (index % 6) + 1;
    }
    
    /**
     * Method to convert the index of the houses array to the player's number.
     * 
     * @param index position as understood by houses array.
     * @return an <code>int</code> representing the player's number.
     */
    public int indexToPlayerConversion(int index) {
        return ((index / 6) % 2) + 1;
    }
    
    /**
     * Method to convert the house and player's number into an index understood
     * by the houses array.
     * 
     * @param house the house number for this player
     * @param playerNum the player's number (1 or 2)
     * @return an <code>int</code> representing the index of the specified house in the
     * houses array.
     */
    public int houseToIndexConversion(int house, int playerNum) {
        
        checkValidHouse(house);
        checkValidPlayer(playerNum);
        
        return house + (playerNum - 1)*6 - 1;
    }

    @Override
    public int getSeeds(int house, int playerNum) throws InvalidHouseException {
        
        checkForInvalidHouseException(house, playerNum);
        
        return houses[houseToIndexConversion(house, playerNum)];
    }

    @Override
    public void sowSeed(int house, int playerNum) throws InvalidHouseException {
        
        checkForInvalidHouseException(house, playerNum);
        
        houses[houseToIndexConversion(house, playerNum)]++;
    }

    @Override
    public void setSeeds(int seeds, int house, int playerNum) throws 
            InvalidHouseException {
        
        checkForInvalidHouseException(house, playerNum);
        checkValidSeeds(seeds);
        
        houses[houseToIndexConversion(house, playerNum)] = seeds;
    }

    @Override
    public int getScore(int playerNum) {
        
        checkValidPlayer(playerNum);
        
        if(playerNum == 1) {
            return p1Score;
        }
        else if(playerNum == 2) {
            return p2Score;
        }
        else {
            throw new IllegalArgumentException("playerNum should be 1 or 2");
        }
    }

    @Override
    public void addScore(int seeds, int playerNum) {
        
        checkValidSeeds(seeds);
        checkValidPlayer(playerNum);
        
        if(playerNum == 1) {
            p1Score += seeds;
        }
        if(playerNum == 2) {
            p2Score += seeds;
        }
    }

    @Override
    public void setScore(int seeds, int playerNum) {
        
        checkValidSeeds(seeds);
        checkValidPlayer(playerNum);
        
        if(playerNum == 1) {
            p1Score = seeds;
        }
        else if(playerNum == 2) {
            p2Score = seeds;
        }
    }

    @Override
    public Board clone() {
        return new BoardImpl(Arrays.copyOf(houses, houses.length), p1Score, 
                p2Score);
    }
    
    @Override
    public String toString() {
        String description = "";
        
        // SeedNumber(House i) : PlayerScore
        for(int i = 0; i < 12; i++) {
            description += houses[i] + " ";
        }
        
        description += ": " + p1Score + " " + p2Score;
        return description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Arrays.hashCode(this.houses);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final BoardImpl other = (BoardImpl) obj;
        
        return Arrays.equals(this.houses, other.houses);
    }
    
    /**
     * Utility method to check if a given house and player number will fly.
     * 
     * @param house the house to be checked
     * @param playerNum the playerNumber to be checked
     * 
     * @throws InvalidHouseException If either house or player numbers are
     * incorrect.
     */
    private void checkForInvalidHouseException(int house, int playerNum) throws 
            InvalidHouseException {
        
        if(!(playerNum == 1 || playerNum == 2)) {
            throw new InvalidHouseException("Invalid input - Player number " +
                    "out of range.\n");
        }
        if(!(house < 7 && house > 0)) {
            throw new InvalidHouseException("Invalid input - House number " +
                    "out of range. Please enter a value from 1 to 6 " +
                    "(inclusive).\n");
        }
    }
    
    /**
     * Utility method to check if a valid house number has been entered.
     * 
     * @param house the number of the house to be checked.
     */
    private void checkValidHouse(int house) {
        
        if(!(house < 7 && house >0)) {
            throw new IllegalArgumentException("Invalid input - House number " +
                    "out of range. Please enter a value from 1 to 6 " +
                    "(inclusive).\n");
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

    /**
     * Utility method to check if a valid seed number has been entered.
     * 
     * @param seeds the number of seeds to be checked.
     */
    private void checkValidSeeds(int seeds) {
        
        if(seeds < 0) {
            throw new IllegalArgumentException("Invalid input - seeds cannot " +
                    "take a negative value.\n");
        }
        
        if(seeds > 48) {
            throw new IllegalArgumentException("Invalid input - seeds cannot " +
                    "take a value greater than the maximum number of seeds " +
                    "in play.\n");
        }
        
    }

}
