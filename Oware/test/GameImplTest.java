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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A testing class to evaluate the rules of the game.
 * 
 * @author Alistair Madden
 */
public class GameImplTest {
    
    Player player1;
    Player player2;
    GameImpl gameImpl;
    
    public GameImplTest() {
        
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        player1 = new HumanPlayer();
        player2 = new HumanPlayer();
        gameImpl = new GameImpl(player1, player2);
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Method to make the move declared in testMove.txt.
     */
    private void testMove() {
        
        File input = new File("test/testMove.txt");
        
        try {
            player1.setIn(new FileInputStream(input));
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(GameImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            gameImpl.nextMove();
        } 
        catch (InvalidHouseException ex) {
            Logger.getLogger(GameImplTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (InvalidMoveException ex) {
            Logger.getLogger(GameImplTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (QuitGameException ex) {
            Logger.getLogger(GameImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of getCurrentPlayer method, of class GameImpl.
     */
    @Test
    public void testGetCurrentPlayer() {
        
        System.out.println("getCurrentPlayer");
        
        Player expResult = player1;
        Player result = gameImpl.getCurrentPlayer();
        assertSame(expResult, result);
        
        testMove();
        
        expResult = player2;
        result = gameImpl.getCurrentPlayer();
        assertSame(expResult, result);
    }

    /**
     * Test of getCurrentPlayerNum method, of class GameImpl.
     */
    @Test
    public void testGetCurrentPlayerNum() {
        
        System.out.println("getCurrentPlayerNum");
        
        int expResult = 1;
        int result = gameImpl.getCurrentPlayerNum();
        assertEquals(expResult, result);
        
        testMove();
        
        expResult = 2;
        result = gameImpl.getCurrentPlayerNum();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCurrentBoard method, of class GameImpl.
     */
    @Test
    public void testGetCurrentBoard() {
        
        System.out.println("getCurrentBoard");
        
        Board expResult = new BoardImpl();
        Board result = gameImpl.getCurrentBoard();
        assertEquals(expResult, result);

        testMove();
        
        try {
            expResult.setSeeds(0, 1, 1);
            expResult.setSeeds(5, 2, 1);
            expResult.setSeeds(5, 3, 1);
            expResult.setSeeds(5, 4, 1);
            expResult.setSeeds(5, 5, 1);
        } 
        catch (InvalidHouseException ex) {
            Logger.getLogger(GameImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        result = gameImpl.getCurrentBoard();
        assertEquals(expResult, result);
    }

    /**
     * Test of getResult method, of class GameImpl.
     */
    @Test
    public void testGetResult() {
        
        System.out.println("getResult");
        
        int expResult = -1;
        int result = gameImpl.getResult();
        assertEquals(expResult, result);
        // TODO return once rules have been rigidly enforced.
        fail("The test case is a prototype.");
    }

    /**
     * Test of positionRepeated method, of class GameImpl.
     */
    @Test
    public void testPositionRepeated() {
        
        System.out.println("positionRepeated");
        
        boolean expResult = false;
        boolean result = gameImpl.positionRepeated();
        assertEquals(expResult, result);

        testMove();
        
        expResult = true;
        
        try {
            gameImpl.getCurrentBoard().getSeeds(4, 1);
            gameImpl.getCurrentBoard().getSeeds(4, 2);
            gameImpl.getCurrentBoard().getSeeds(4, 3);
            gameImpl.getCurrentBoard().getSeeds(4, 4);
            gameImpl.getCurrentBoard().getSeeds(4, 5);
        } 
        catch (InvalidHouseException ex) {
            Logger.getLogger(GameImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        result = gameImpl.positionRepeated();
        assertEquals(expResult, result);
    }

    /**
     * Test of nextMove method, of class GameImpl.
     */
    @Test
    public void testNextMove() throws Exception {
        System.out.println("nextMove");
        GameImpl instance = null;
        instance.nextMove();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class GameImpl.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        GameImpl instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPlayerName method, of class GameImpl.
     */
    @Test
    public void testGetPlayerName() {
        System.out.println("getPlayerName");
        byte turn = 0;
        GameImpl instance = null;
        String expResult = "";
        String result = instance.getPlayerName(turn);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
