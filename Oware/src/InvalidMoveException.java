
import java.io.Serializable;

/**
 * When a move is against the rules, because of starvation of the opponent.
 * 
 * @author Steven Bradley, Alistair Madden
 * @version 1.1
 * 
 */
public class InvalidMoveException extends Exception implements Serializable
{
    public InvalidMoveException(String message){
        super(message);
    }
}
