/*
 * Copyright (C) 2015 Alistair Madden <phantommelon@gmail.com>
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Write a description of class ControlClass here.
 * 
 * @author Alistair Madden <phantommelon@gmail.com> 
 * @version (a version number or a date)
 */

public class ControlClass {
    
    private InputStream in;
    private PrintStream out;
    private Scanner scanner;
    private ChildClass child;
    
    public void manage(InputStream in, PrintStream out) {
        this.in = in;
        this.out = out;
        
        child = createNewChild();
        
        scanner = new Scanner(in);
        
//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        
//        try {
//            out.println(br.readLine());
//        } 
//        catch (IOException ex) {
//            Logger.getLogger(ControlClass.class.getName()).log(Level.SEVERE, null, ex);
//        }

        child.printStream();
        
        out.println(scanner.nextLine());
        
        child.printStream();
    }
    
    private ChildClass createNewChild() {
        ChildClass controller = new ChildClass();
        controller.setIn(in);
        controller.setOut(out);
        return controller;
    }
    
    public static void main(String[] args) {
        
        ControlClass control = new ControlClass();
        
        try {
            System.setIn(new FileInputStream("test.txt"));
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(ControlClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            control.manage(new FileInputStream("test.txt"), System.out);
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(ControlClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
