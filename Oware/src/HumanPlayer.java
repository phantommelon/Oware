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

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * An object to give a user playable player to the game.
 * 
 * @author Alistair Madden
 * @version 0.3
 */
public class HumanPlayer implements Player {

    private InputStream input;
    private PrintStream output;
    private Scanner scanner;
    
    public HumanPlayer() {
    }
    
    @Override
    public int getMove(Board b, int playerNum) throws QuitGameException {
        
        scanner = new Scanner(input);
        
        int move = 0;
        
        output.print("Please enter a house number to make a move or QUIT to " +
                "return to the main menu: ");

        while(move == 0) {
            try {
                move = Integer.parseInt(scanner.nextLine());
                return move;
            }
            catch(NumberFormatException nfe) {
                
                // Get the user's input from the error message.
                String inputString = nfe.getMessage().split("\"", 2)[1].
                        replace("\"", "");
                
                if(inputString.equals("QUIT")) {
                    throw new QuitGameException("Quitting to the main menu...");
                }
                
                else if(inputString.equals("quit")) {
                    throw new IllegalArgumentException("Invalid input - A " +
                            "command should be fully capitalised, for " +
                            "example QUIT.");
                }
                
                else if(inputString.toLowerCase().contains(
                        "quit")) {
                    
                    throw new IllegalArgumentException("Invalid input - To " +
                            "quit to the main menu, please enter the QUIT " +
                            "command by itself with no additional commands " +
                            "either before or after the QUIT command.");
                }
                
                // User looks as if they wanted to use a main menu command of
                // some form.
                else if(inputString.toLowerCase().contains("new") ||
                        inputString.toLowerCase().contains("load") || 
                        inputString.toLowerCase().contains("save") ||
                        inputString.toLowerCase().contains("exit")) {
                    
                    throw new IllegalStateException("Invalid input - The " +
                            "input contains a main menu command. Please " +
                            "enter QUIT to return to the main menu before " +
                            "using this command. Please note that all " +
                            "commands are fully capitalised.");
                }
                
                // The user has entered nothing of value.
                else {
                    throw new IllegalArgumentException("Invalid input - " +
                            "Please enter a value from 1 to 6 (inclusive) or " +
                            "QUIT to return to the main menu.");
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
    public void setIn(InputStream input) {
        this.input = input;
    }

    @Override
    public void setOut(PrintStream out) {
        this.output = out;
    }
    
    public void setReader(Scanner reader) {
        this.scanner = reader;
    }
    
}
