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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A test for Scanners.
 * 
 * @author Alistair Madden
 * @version 0.1
 */
public class ScannerTest {

    private BufferedReader br;
    private InputStream in;
    
    public ScannerTest() {
        this.in = System.in;
        br = new BufferedReader(new InputStreamReader(in));
    }
    
    public ScannerTest(InputStream in) {
        this.in = in;
        br = new BufferedReader(new InputStreamReader(in));
    }
    
    public void run() {
        
        String data = null;
        try {
            data = br.readLine();
        } 
        catch (IOException ex) {
            Logger.getLogger(ScannerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(data);
    }
    
    public void run2() {
        try {
            System.out.println(in.read());
        }
        catch(IOException ex) {
            
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        
        System.setIn(new FileInputStream(
                new File("test.txt")));
        
        ScannerTest test = new ScannerTest();

        try {
            while(test.br.ready()) {
                test.run();
            }
        } catch (IOException ex) {
            Logger.getLogger(ScannerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            System.out.println(test.in.available());
        } catch (IOException ex) {
            Logger.getLogger(ScannerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
}
