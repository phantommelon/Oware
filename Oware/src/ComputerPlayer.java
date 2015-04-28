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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Deterministic Computer Player
 * 
 * @author Alistair Madden
 * @version 0.1
 */
class ComputerPlayer implements Player {

    InputStream in;
    PrintStream out;
    
    /**
     * Default constructor for ComputerPlayer Class.
     */
    public ComputerPlayer() {
        
    }

    @Override
    public int getMove(Board board, int playerNum) throws QuitGameException {
        
        // Very simple, choose first valid house!
        for(int i = 0; i < 6; i++) {
            
            try {
                board.makeMove(i + 1, playerNum);
            }
            catch (InvalidHouseException ex) {
                continue;
            } 
            catch (InvalidMoveException ex) {
                continue;
            }

            return i + 1;
        }
        
        // Should never reach here because the game will have ended if no seeds
        // remain in house.
        return 0;
    }

    @Override
    public boolean isComputer() {
        return true;
    }

    @Override
    public void setIn(InputStream in) {
        this.in = in;
    }

    @Override
    public void setOut(PrintStream out) {
        this.out = out;
    }
    
}
