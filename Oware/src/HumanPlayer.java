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

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An object to give a user playable player to the game.
 * 
 * @author Alistair Madden
 * @version 0.3
 */
public class HumanPlayer implements Player, Serializable {

    private InputStream input;
    private PrintStream output;
    private Scanner in;
    
    public HumanPlayer() {
        input = System.in;
        output = System.out;
        in = new Scanner(input);
    }
    
    @Override
    public int getMove(Board b, int playerNum) throws QuitGameException {
        
        int move = 0;
        
        output.print("Please enter a house number to make a move or QUIT to " +
                "return to the main menu: ");
        
        String data = in.nextLine();
        
        while(move == 0) {
            try {
                move = Integer.parseInt(data);
                return move;
            }
            catch(NumberFormatException nfe) {
                List<String> inputStrings = new ArrayList(Arrays.asList(
                        nfe.getMessage().split("\"")));
                
                inputStrings.remove(0);
                
                // Check is empty to catch "s being entered.
                if(!inputStrings.isEmpty() && 
                        inputStrings.get(0).equals("QUIT")) {
                    throw new QuitGameException("Quitting to the main menu...");
                }
                
                // See if the user was trying to issue a main menu command.
                else if(!inputStrings.isEmpty() &&
                        (inputStrings.get(0).equals("NEW") ||
                        inputStrings.get(0).equals("LOAD") ||
                        inputStrings.get(0).equals("SAVE") ||
                        inputStrings.get(0).equals("EXIT"))) {
                    
                    throw new IllegalStateException("Invalid input - " +
                            inputStrings.get(0) + " is a main menu command. " +
                            "Please enter QUIT to return to the main menu " +
                            "before using this command.");
                }
                
                // The user has entered nothing of value.
                else {
                    throw new IllegalArgumentException("Invalid input - " +
                            "Please enter a value from 1 to 6 (inclusive).");
                }
            }
        }

        return move;
    }

    @Override
    public boolean isComputer() {
        return false; //Human != computer
    }

    @Override
    public void setIn(InputStream in) {
        this.input = in;
        this.in = new Scanner(in);
    }

    @Override
    public void setOut(PrintStream out) {
        this.output = out;
    }
    
}
