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

package utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * A utility class to take an object and provide a deep copy of it.
 * 
 * 
 * 
 * @author Alistair Madden
 * @version 0.1
 */
public class DeepCopy {

    public static Object copy(Object original) {
        Object copy = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            // This is why the object needs to be serialisable
            ObjectOutputStream out = new ObjectOutputStream(byteArray);
            
            out.writeObject(original);
            // Buffered - make sure all is written out
            out.flush();
            // Close ObjectOutputStream
            out.close();

            // Make an input stream from the byte array and read a copy of the
            // object back into the copy field.
            ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(byteArray.toByteArray()));
            copy = in.readObject();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return copy;
    }
}
