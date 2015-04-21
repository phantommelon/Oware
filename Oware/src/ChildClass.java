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

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Write a description of class ChildClass here.
 * 
 * @author Alistair Madden <phantommelon@gmail.com> 
 * @version (a version number or a date)
 */

public class ChildClass {

    private InputStream in;
    private PrintStream out;
    private Scanner parser;
    
    public void setIn(InputStream in) {
        this.in = in;
        this.parser = new Scanner(in);
    }
    
    public void setOut(PrintStream out) {
        this.out = out;
    }
    
    public void printStream() {
        out.println(parser.nextLine());
    }
    
    
}
