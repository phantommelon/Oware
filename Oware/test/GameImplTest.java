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
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
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
    
    Player humanPlayer1;
    Player humanPlayer2;
    Player computerPlayer1;
    Player computerPlayer2;
    GameImpl hVersusH;
    GameImpl hVersusC;
    GameImpl CVersusH;
    GameImpl CVersusC;
    
    public GameImplTest() {
        // Construction is done in setUp called before each test case.   
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        humanPlayer1 = new HumanPlayer();
        humanPlayer2 = new HumanPlayer();
        computerPlayer1 = new ComputerPlayer();
        computerPlayer2 = new ComputerPlayer();
        hVersusH = new GameImpl(humanPlayer1, humanPlayer2);
        hVersusC = new GameImpl(humanPlayer1, computerPlayer1);
        CVersusH = new GameImpl(computerPlayer1, humanPlayer1);
        CVersusC = new GameImpl(computerPlayer1, computerPlayer2);
        
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Method to test a move.
     * 
     * @param inputFileName name of the file containing a move for testing
     * @param outputFileName name of the file to save any output to
     * @param currentPlayer Player object to make the move
     */
    private void testMove(String inputFileName, String outputFileName,
            Player currentPlayer) throws InvalidHouseException, QuitGameException, InvalidMoveException {
        
        File input = new File("test/" + inputFileName);
        
        try {
            currentPlayer.setIn(new FileInputStream(input));
            currentPlayer.setOut(new PrintStream(new FileOutputStream("test/" +
                    outputFileName)));
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(GameImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        hVersusH.nextMove();
    }

    /**
     * Test of getCurrentPlayer method, of class GameImpl.
     */
    @Test
    public void testGetCurrentPlayer() {
        
        System.out.println("getCurrentPlayer");
        
        Player expResult = humanPlayer1;
        Player result = hVersusH.getCurrentPlayer();
        assertSame(expResult, result);
        
        try {
            testMove("getCurrentPlayerMove", "getCurrentPlayerOutput",
                    humanPlayer1);
        } catch (InvalidHouseException | QuitGameException | 
                InvalidMoveException ex) {
            Logger.getLogger(GameImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        expResult = humanPlayer2;
        result = hVersusH.getCurrentPlayer();
        assertSame(expResult, result);
    }

    /**
     * Test of getCurrentPlayerNum method, of class GameImpl.
     */
    @Test
    public void testGetCurrentPlayerNum() {
        
        System.out.println("getCurrentPlayerNum");
        
        int expResult = 1;
        int result = hVersusH.getCurrentPlayerNum();
        assertEquals(expResult, result);
        
        try {
            testMove("getCurrentPlayerNumMove", "getCurrentPlayerNumOutput",
                    hVersusH.getCurrentPlayer());
        } catch (InvalidHouseException | QuitGameException | 
                InvalidMoveException ex) {
            Logger.getLogger(GameImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        expResult = 2;
        result = hVersusH.getCurrentPlayerNum();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCurrentBoard method, of class GameImpl.
     */
    @Test
    public void testGetCurrentBoard() {
        
        System.out.println("getCurrentBoard");
        
        Board expResult = new BoardImpl();
        Board result = hVersusH.getCurrentBoard();
        assertEquals(expResult, result);

        try {
            testMove("getCurrentBoardMove", "getCurrentBoardOutput", hVersusH.
                    getCurrentPlayer());
        } 
        catch (InvalidHouseException | QuitGameException | 
                InvalidMoveException ex) {
            Logger.getLogger(GameImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
        
        result = hVersusH.getCurrentBoard();
        assertEquals(expResult, result);
    }

    /**
     * Test of getResult method, of class GameImpl.
     */
    @Test
    public void testGetResult() {
        
        System.out.println("getResult");
        
        int expResult = -1;
        int result = hVersusH.getResult();
        assertEquals(expResult, result);

        GameImpl endGame0 = new GameImpl(new BoardImpl(new int[12], 15, 15), 
                humanPlayer1, humanPlayer2, null, null, (byte) 1, 0, true,
                null);
        
        GameImpl endGame1 = new GameImpl(new BoardImpl(new int[12], 30, 0), 
                humanPlayer1, humanPlayer2, null, null, (byte) 1, 0, true,
                null);
        
        GameImpl endGame2 = new GameImpl(new BoardImpl(new int[12], 0, 30), 
                humanPlayer1, humanPlayer2, null, null, (byte) 1, 0, true,
                null);
        
        expResult = 0;
        result = endGame0.getResult();
        assertEquals(expResult, result);
        
        expResult = 1;
        result = endGame1.getResult();
        assertEquals(expResult, result);
        
        expResult = 2;
        result = endGame2.getResult();
        assertEquals(expResult, result);
    }

    /**
     * Test of positionRepeated method, of class GameImpl.
     */
    @Test
    public void testPositionRepeated() {
        
        System.out.println("positionRepeated");
        
        boolean expResult = false;
        boolean result = hVersusH.positionRepeated();
        assertEquals(expResult, result);

        try {
            testMove("positionRepeatedMove", "positionRepeatedOutput", hVersusH.
                    getCurrentPlayer());
        } 
        catch (InvalidHouseException | QuitGameException | 
                InvalidMoveException ex) {
            Logger.getLogger(GameImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        result = hVersusH.positionRepeated();
        assertEquals(expResult, result);
        
        try {
            hVersusH.getCurrentBoard().setSeeds(4, 1, 1);
            hVersusH.getCurrentBoard().setSeeds(4, 2, 1);
            hVersusH.getCurrentBoard().setSeeds(4, 3, 1);
            hVersusH.getCurrentBoard().setSeeds(4, 4, 1);
            hVersusH.getCurrentBoard().setSeeds(4, 5, 1);
        } 
        catch (InvalidHouseException ex) {
            Logger.getLogger(GameImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        expResult = true;
        result = hVersusH.positionRepeated();
        assertEquals(expResult, result);
    }

    /**
     * Test of nextMove method, of class GameImpl.
     */
    @Test
    public void testNextMove() throws Exception {
        
        System.out.println("nextMove");
        
        int[] expResultArray = new int[12];
        Arrays.fill(expResultArray, 4);
        expResultArray[0] = 0;
        expResultArray[1] = 5;
        expResultArray[2] = 5;
        expResultArray[3] = 5;
        expResultArray[4] = 5;
        
        Board expResult = new BoardImpl(expResultArray, 0, 0);
        
        CVersusC.nextMove();
        
        Board result = CVersusC.getCurrentBoard();
        
        assertEquals(expResult, result);
        
    }

    /**
     * Test of toString method, of class GameImpl.
     */
    @Test
    public void testToString() {
        
        System.out.println("toString");
        
        String newLine = System.getProperty("line.separator");
        
        String expResult = new String();
        expResult += "4 4 4 4 4 4 4 4 4 4 4 4 : 0 0" + newLine;
        expResult += "ComputerPlayer" + newLine;
        expResult += "ComputerPlayer" + newLine;
        expResult += "Player 1" + newLine;
        expResult += "Player 2" + newLine;
        expResult += "1" + newLine;
        expResult += "0" + newLine;
        expResult += "false" + newLine;
        String result = CVersusC.toString();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of getPlayerName method, of class GameImpl.
     */
    @Test
    public void testGetPlayerName() {
        System.out.println("getPlayerName");
        byte turn = 1;
        String expResult = "Player 1";
        String result = CVersusC.getPlayerName(turn);
        assertEquals(expResult, result);
        
        expResult = "Player 2";
        result = CVersusC.getPlayerName(2);
        assertEquals(expResult, result);
        
    }
    
}
