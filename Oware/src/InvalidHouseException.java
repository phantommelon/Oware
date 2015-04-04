
import java.io.Serializable;

/**
 * When the house specification is not in the correct range: house 1..6, playerNum 1..2
 *
 * @author Steven Bradley, Alistair Madden
 * @version 1.1
 */
public class InvalidHouseException extends Exception implements Serializable
{
    public InvalidHouseException(String message)
    {
        super(message);
    }
}
