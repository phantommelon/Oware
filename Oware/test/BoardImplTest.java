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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alistair Madden
 */
public class BoardImplTest {
    
    BoardImpl instance;
    
    public BoardImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new BoardImpl();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of makeMove method, of class BoardImpl.
     */
    @Test
    public void testMakeMove() throws Exception {
        
        System.out.println("makeMove");
        int house = 1;
        int playerNum = 1;
        
        instance.makeMove(house, playerNum);
        
        BoardImpl expResult = new BoardImpl();
        expResult.setSeeds(0, house, playerNum);
        expResult.setSeeds(5, 2, playerNum);
        expResult.setSeeds(5, 3, playerNum);
        expResult.setSeeds(5, 4, playerNum);
        expResult.setSeeds(5, 5, playerNum);
        
        assertEquals(expResult, instance);
    }

    /**
     * Test of indexToHouseConversion method, of class BoardImpl.
     */
    @Test
    public void testIndexToHouseConversion() {
        
        System.out.println("indexToHouseConversion");
        
        int index = 0;
        int expResult = 1;
        int result = instance.indexToHouseConversion(index);
        assertEquals(expResult, result);
        
        index = 1;
        expResult = 2;
        result = instance.indexToHouseConversion(index);
        assertEquals(expResult, result);
        
        index = 2;
        expResult = 3;
        result = instance.indexToHouseConversion(index);
        assertEquals(expResult, result);
        
        index = 3;
        expResult = 4;
        result = instance.indexToHouseConversion(index);
        assertEquals(expResult, result);
        
        index = 4;
        expResult = 5;
        result = instance.indexToHouseConversion(index);
        assertEquals(expResult, result);
        
        index = 5;
        expResult = 6;
        result = instance.indexToHouseConversion(index);
        assertEquals(expResult, result);
        
        index = 6;
        expResult = 1;
        result = instance.indexToHouseConversion(index);
        assertEquals(expResult, result);
        
        index = 7;
        expResult = 2;
        result = instance.indexToHouseConversion(index);
        assertEquals(expResult, result);
        
        index = 8;
        expResult = 3;
        result = instance.indexToHouseConversion(index);
        assertEquals(expResult, result);
        
        index = 9;
        expResult = 4;
        result = instance.indexToHouseConversion(index);
        assertEquals(expResult, result);
        
        index = 10;
        expResult = 5;
        result = instance.indexToHouseConversion(index);
        assertEquals(expResult, result);
        
        index = 11;
        expResult = 6;
        result = instance.indexToHouseConversion(index);
        assertEquals(expResult, result);      
    }

    /**
     * Test of indexToPlayerConversion method, of class BoardImpl.
     */
    @Test
    public void testIndexToPlayerConversion() {
        
        System.out.println("indexToPlayerConversion");
        
        int index = 0;
        int expResult = 1;
        int result = instance.indexToPlayerConversion(index);
        assertEquals(expResult, result);

        index = 1;
        expResult = 1;
        result = instance.indexToPlayerConversion(index);
        assertEquals(expResult, result);
        
        index = 2;
        expResult = 1;
        result = instance.indexToPlayerConversion(index);
        assertEquals(expResult, result);
        
        index = 3;
        expResult = 1;
        result = instance.indexToPlayerConversion(index);
        assertEquals(expResult, result);
        
        index = 4;
        expResult = 1;
        result = instance.indexToPlayerConversion(index);
        assertEquals(expResult, result);
        
        index = 5;
        expResult = 1;
        result = instance.indexToPlayerConversion(index);
        assertEquals(expResult, result);
        
        index = 6;
        expResult = 2;
        result = instance.indexToPlayerConversion(index);
        assertEquals(expResult, result);
        
        index = 7;
        expResult = 2;
        result = instance.indexToPlayerConversion(index);
        assertEquals(expResult, result);
        
        index = 8;
        expResult = 2;
        result = instance.indexToPlayerConversion(index);
        assertEquals(expResult, result);
        
        index = 9;
        expResult = 2;
        result = instance.indexToPlayerConversion(index);
        assertEquals(expResult, result);
        
        index = 10;
        expResult = 2;
        result = instance.indexToPlayerConversion(index);
        assertEquals(expResult, result);
        
        index = 11;
        expResult = 2;
        result = instance.indexToPlayerConversion(index);
        assertEquals(expResult, result);          
    }

    /**
     * Test of houseToIndexConversion method, of class BoardImpl.
     */
    @Test
    public void testHouseToIndexConversion() {
        
        System.out.println("houseToIndexConversion");
        
        int house = 1;
        int playerNum = 1;
        int expResult = 0;
        int result = instance.houseToIndexConversion(house, playerNum);
        assertEquals(expResult, result);

        house = 2;
        expResult = 1;
        result = instance.houseToIndexConversion(house, playerNum);
        
        house = 3;
        expResult = 2;
        result = instance.houseToIndexConversion(house, playerNum);
        
        house = 4;
        expResult = 3;
        result = instance.houseToIndexConversion(house, playerNum);
        
        house = 5;
        expResult = 4;
        result = instance.houseToIndexConversion(house, playerNum);
        
        house = 6;
        expResult = 5;
        result = instance.houseToIndexConversion(house, playerNum);
        
        playerNum = 2;
        house = 1;
        expResult = 6;
        result = instance.houseToIndexConversion(house, playerNum);   
        
        house = 2;
        expResult = 7;
        result = instance.houseToIndexConversion(house, playerNum);        
        
        house = 3;
        expResult = 8;
        result = instance.houseToIndexConversion(house, playerNum);
        
        house = 4;
        expResult = 9;
        result = instance.houseToIndexConversion(house, playerNum);
        
        house = 5;
        expResult = 10;
        result = instance.houseToIndexConversion(house, playerNum);
        
        house = 6;
        expResult = 11;
        result = instance.houseToIndexConversion(house, playerNum);        
    }

    /**
     * Test of getSeeds method, of class BoardImpl.
     */
    @Test
    public void testGetSeeds() throws Exception {
        
        System.out.println("getSeeds");
        
        instance.setSeeds(4, 1, 1);
        instance.setSeeds(6, 2, 1);
        instance.setSeeds(3, 3, 1);
        instance.setSeeds(9, 4, 1);
        instance.setSeeds(4, 5, 1);
        instance.setSeeds(3, 6, 1);
        instance.setSeeds(1, 1, 2);
        instance.setSeeds(0, 2, 2);
        instance.setSeeds(8, 3, 2);
        instance.setSeeds(2, 4, 2);
        instance.setSeeds(7, 5, 2);
        instance.setSeeds(56, 6, 2); // Lol.
        
        int house = 1;
        int playerNum = 1;
        int expResult = 4;
        int result = instance.getSeeds(house, playerNum);
        assertEquals(expResult, result);

        house = 2;
        expResult = 6;
        result = instance.getSeeds(house, playerNum);
        
        house = 3;
        expResult = 3;
        result = instance.getSeeds(house, playerNum);
        
        house = 4;
        expResult = 9;
        result = instance.getSeeds(house, playerNum);
        
        house = 5;
        expResult = 4;
        result = instance.getSeeds(house, playerNum);
        
        house = 6;
        expResult = 3;
        result = instance.getSeeds(house, playerNum);
        
        playerNum = 2;
        house = 1;
        expResult = 1;
        result = instance.getSeeds(house, playerNum);
        
        house = 2;
        expResult = 0;
        result = instance.getSeeds(house, playerNum);
        
        house = 3;
        expResult = 8;
        result = instance.getSeeds(house, playerNum);
        
        house = 4;
        expResult = 2;
        result = instance.getSeeds(house, playerNum);
        
        house = 5;
        expResult = 7;
        result = instance.getSeeds(house, playerNum);
        
        house = 6;
        expResult = 56;
        result = instance.getSeeds(house, playerNum);
    }

    /**
     * Test of sowSeed method, of class BoardImpl.
     */
    @Test
    public void testSowSeed() throws Exception {
        
        System.out.println("sowSeed");
        int house = 1;
        int playerNum = 1;
        instance.sowSeed(house, playerNum);
        
        int expResult = 5;
        int result = instance.getSeeds(1, 1);
        assertEquals(expResult, result);
    }

    /**
     * Test of setSeeds method, of class BoardImpl.
     */
    @Test
    public void testSetSeeds() throws Exception {
        
        System.out.println("setSeeds");
        
        int seeds = 0;
        int house = 1;
        int playerNum = 1;
        instance.setSeeds(seeds, house, playerNum);
        
        int expResult = 0;
        int result = instance.getSeeds(1, 1);

        assertEquals(expResult, result);
    }

    /**
     * Test of getScore method, of class BoardImpl.
     */
    @Test
    public void testGetScore() {
        
        System.out.println("getScore");
        
        int playerNum = 1;
        int expResult = 666;
        instance.addScore(666, playerNum);
        int result = instance.getScore(playerNum);
        assertEquals(expResult, result);
    }

    /**
     * Test of addScore method, of class BoardImpl.
     */
    @Test
    public void testAddScore() {
        
        System.out.println("addScore");
        
        int playerNum = 1;
        int expResult = 666;
        instance.addScore(666, playerNum);
        int result = instance.getScore(playerNum);
        assertEquals(expResult, result);
    }

    /**
     * Test of setScore method, of class BoardImpl.
     */
    @Test
    public void testSetScore() {
        
        System.out.println("setScore");
        
        int playerNum = 1;
        int expResult = 5;
        instance.addScore(666, playerNum);
        instance.setScore(5, playerNum);
        int result = instance.getScore(playerNum);
        assertEquals(expResult, result);
    }

    /**
     * Test of clone method, of class BoardImpl.
     */
    @Test
    public void testClone() {
        
        System.out.println("clone");
        
        Board result = instance.clone();
        assertEquals(instance, result);
        
        assertNotSame(instance, result);
    }

    /**
     * Test of equals method, of class BoardImpl.
     */
    @Test
    public void testEquals() throws InvalidHouseException {
        
        System.out.println("equals");
        
        Object obj = null;
        BoardImpl instance2 = new BoardImpl();
        
        boolean expResult = false;
        
        boolean result = instance.equals(obj);
        
        assertEquals(expResult, result);

        assertEquals(instance, instance2);
        
        instance2.setScore(50, 1);
        result = instance.equals(instance2);
        assertEquals(true, result);
        
        instance2.setScore(0, 1);
        assertEquals(instance, instance2);
        
        instance2.setSeeds(5, 1, 1);
        result = instance.equals(instance2);
        assertEquals(expResult, result);
        
    }
    
}
