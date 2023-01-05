/**
 * Provides insturctions for a single playing card with three attributes:
 * an integer, a suit, and whether it is face up. 
 * 
 * @author      Vienna Parnell 
 * @version     12.2.2020
 */
public class Card
{
    int rank; 
    String suit; 
    boolean isFaceUp; 
    /**
     * Constructor for objects of class Card. 
     * @param r     card's numerical rank
     * @param s     card's suit
     * @param f     true if card is face up; otherwise,
     *              false
     */
    public Card(int r, String s, boolean f)
    {
        rank = r; 
        suit = s; 
        isFaceUp = f; 
    }
    
    /**
     * Retrieves card's rank, which ranges from 1 to 13,
     * where 1 = Ace and 13 = King. 
     * @return      card's numerical rank
     */
    public int getRank()
    {
        return rank; 
    }
    /**
     * Retrieves card's rank, where "c" = clubs, "d" = diamonds,
     * "h" = hearts, and "s" = spades. 
     * @return      card's suit
     */
    public String getSuit() 
    {
        return suit; 
    }
    /**
     * Determines if card is red by checking if it's suit is
     * a diamond or a heart. 
     * @return      true if the card is red; otherwise,
     *              false
     */
    public boolean isRed()
    {
        if (suit.equals("d") || suit.equals("h"))
            return true;
        return false; 
    }
    /**
     * Retrieves if card is face up,
     * @return      true if card is face up; otherwise,
     *              false
     */
    public boolean isFaceUp()
    {
        return isFaceUp; 
    }
    /**
     * Turns the card so that is facing upward. 
     * @postcondition       card is facing upward
     */
    public void turnUp()
    {
        isFaceUp = true; 
    }
    /**
     * Turns the card so that it is facing downward. 
     * @postcondition       card is facing downward
     */
    public void turnDown()
    {
        isFaceUp = false; 
    }
    /**
     * Generates filename of card according to characteristics
     * of rank, suit, and face up or down. 
     * @return      filename of card
     */
    public String getFileName()
    {
        if (!isFaceUp())
        {
            return "cards/back.gif"; 
        }
        else
        {
            if (rank >= 2 && rank <= 9)
                return "cards/" + rank + suit + ".gif";
            else if (rank == 1)
                return "cards/" + "a" + suit + ".gif"; 
            else if (rank == 11)
                return "cards/" + "j" + suit + ".gif"; 
            else if (rank == 12)
                return "cards/" + "q" + suit + ".gif"; 
            else if (rank == 13)
                return "cards/" + "k" + suit + ".gif"; 
            else 
                return "cards/" + "t" + suit + ".gif"; 
        }
    }
}
