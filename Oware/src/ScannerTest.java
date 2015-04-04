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
import java.io.InputStream;
import java.util.Scanner;

/**
 * {Insert Description of Class ScannerTest Here}
 * 
 * @author Alistair Madden
 * @version 0.1
 */
public class ScannerTest {

    private Scanner in;
    
    public ScannerTest() {
        in = new Scanner(System.in);
    }
    
    public ScannerTest(InputStream in) {
        this.in = new Scanner(in);
    }
    
    public void run() {
        
        String data = in.nextLine();
        
        System.out.println(data);
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        
        ScannerTest test = new ScannerTest(new FileInputStream(
                new File("test.txt")));
        
        for(int i = 0; i < 15; i++) {
            test.run();
        }
    }
}
