import java.util.*;

/**
 * Solitaire provides the instructions for playing the card game of 
 * the same name. During the game, the player is able to order 
 * the cards sequentially in alternating colors in piles. The goal 
 * is to organize all of the cards in four sequential foundations. 
 * @author      Vienna Parnell
 * @version     12.7.2020
 */
public class Solitaire
{
	/**
	 * Executes solitaire game. 
	 * @param args     arguments from the command line
	 */
	public static void main(String[] args)
	{
		new Solitaire();
	}

	private Stack<Card> stock;
	private Stack<Card> waste;
	private Stack<Card>[] foundations;
	private Stack<Card>[] piles;
	private SolitaireDisplay display;

	/**
	 * Constructs Solitaire object, initilizing stock, waste,
	 * foundations, and piles, each as an empty Stack of Card objects.
	 * The stock is assigned the cards in the deck in a random order,
	 * and the piles are dealed. 
	 */
	public Solitaire()
	{
		foundations = new Stack[4];
		for (int i = 0; i < 4; i++) 
		{
		    foundations[i] = new Stack<Card>();
		}
		piles = new Stack[7];
		for (int i = 0; i < 7; i++) 
		{
		    piles[i] = new Stack<Card>(); 
		}
		stock = new Stack<Card>(); 
		waste = new Stack<Card>(); 
		display = new SolitaireDisplay(this);
		createStock(); 
		deal(); 
	}

	/**
	 * Retrieves the card on top of the stock,
	 * or null if the stock is empty. 
	 * @return             card or null
	 */
	public Card getStockCard()
	{
		if (stock.isEmpty()) return null; 
		return stock.peek(); 
	}

	/**
	 * Retrieves the card on top of the waste,
	 * or null if the waste is empty. 
	 * @return             card or null
	 */
	public Card getWasteCard()
	{
		if (waste.isEmpty()) return null; 
		return waste.peek(); 
	}
            
	/**
	 * Retrieves the card on top of the given foundation, 
	 * or null if the foundation is empty. 
	 * @param index        index of foundation
	 * @precondition       0 <= index < 4
	 * @return             card or null
	 */
	public Card getFoundationCard(int index)
	{
		if (foundations[index].isEmpty()) return null;
		return foundations[index].peek(); 
	}

	/**
	 * Retrieves the reference to the given pile.
	 * @param index        index of pile
	 * @precondition       0 <= index < 7
	 * @return             a reference to the given pile      
	 */
	public Stack<Card> getPile(int index)
	{
		return piles[index]; 
	}
	
	/**
	 * Generates random order of 52 playing cards and 
	 * fills stock. 
	 * @postcondition      random sequence of cards has been generated
	 *                     and placed in stock
	 */
	public void createStock()
	{
	    ArrayList<Card>deck = new ArrayList<Card>(); 
	    String [] suits = {"d","s","c","h"}; 
	    
	    for (int i = 0; i < 4; i++)
	    {
	        String s = suits[i]; 
	        for (int j = 1; j <= 13; j++) 
	        {
	            deck.add(new Card(j, s, false));
	        }       
	    }
	    
	    while (deck.size() > 0)
	    {
	        int rand = (int) (Math.random()*deck.size()); 
	        stock.push(deck.remove(rand));
	    }
	}
	
	/**
	 * Deals the cards from the stock to the piles, placing 
	 * them in an increasing, ascending manner. The top card 
	 * of the piles is face up, while the cards underneath are 
	 * face down. 
	 * @postcondition      initial setup of solitaire is complete
	 */
	public void deal()
	{
	    for (int i = 0; i <= 6; i++)
	    {
	        for (int j = 0; j <= i-1; j++)
	        {
	            stock.peek().turnDown(); 
	            piles[i].push(stock.pop());
	        }
	        stock.peek().turnUp(); 
	        piles[i].push(stock.pop()); 
	    }
	}
	
	/**
	 * Moves the top three cards from the stock to the waste.
	 * If there are fewer than three cards on the stock, the
	 * remaining cards are transferred to the waste. 
	 * Cards are turned to face upward as they are moved onto
	 * the waste. 
	 * @postcondition      three more cards have been dealt face up onto waste
	 */
	public void dealThreeCards() 
	{
	    int c = 1;  
	    while (!stock.isEmpty() && c <= 3)
	    {
	        stock.peek().turnUp(); 
	        waste.push(stock.pop());
	        c++; 
	    }
	}
	
	/**
	 * Resets stock so that the cards from waste are 
	 * transferred back to the stock. 
	 * @postcondition      cards have been turned downward onto stock
	 */
	public void resetStock()
	{
	    while (!waste.isEmpty())
	    {
	        waste.peek().turnDown(); 
	        stock.push(waste.pop()); 
	    }
	}
	
	/**
	 * If waste or pile is already selected, nothing occurs.
	 * If the stock is not empty, cards are dealt 
	 * three at a time to the waste.
	 * Otherwise, the stock is reset. 
	 * @postcondition      stock has been dealt or reset
	 */
	public void stockClicked()
	{
	    if (display.isWasteSelected() || display.isPileSelected()) 
		     return;    
	    if (!stock.isEmpty()) 
		    dealThreeCards();
		else 
		    resetStock();
		System.out.println("stock clicked");
	}

	/**
	 * Selects waste if it is not empty and neither the waste
	 * nor a pile is already selected. 
	 * Unselects waste if it is already selected. 
	 * @postcondition      selection of waste has been toggled
	 */
	public void wasteClicked()
	{
		if (!waste.isEmpty() && !display.isWasteSelected() && !display.isPileSelected()) 
		{
		    display.selectWaste(); 
		}
		else if (display.isWasteSelected())
		{
		    display.unselect(); 
		}
		System.out.println("waste clicked");
	}

	/**
	 * Called when given foundation is clicked.
	 * @param index        index of foundation that has been clicked
	 * @precondition       0 <= index < 4
	 * @postcondition      card has either been added from waste or pile,
	 *                     or card has moved from foundation to a pile
	 */
	public void foundationClicked(int index)
	{
	    if (hasWon()) 
	    {
	        clearBoard(); 
	        System.out.println("Congratulations!! You won!! (:");
	    }
	    if (display.isWasteSelected())
		{
		    if (canAddToFoundation(waste.peek(), index))
		    {
		        foundations[index].push(waste.pop());
		        display.unselect(); 
		    }
		}
		else if (display.isPileSelected())
		{
		    if (canAddToFoundation(piles[display.selectedPile()].peek(), index))
		    {
		        foundations[index].push(piles[display.selectedPile()].pop());
		        display.unselect(); 
		    }
		}
		else if (display.isFoundationSelected())
		{
		    if (display.selectedFoundation() == index+3)
		    {
		        display.unselect(); 
		    }    
		}
		else if (!display.isWasteSelected() && !display.isPileSelected())
		{
		    display.selectFoundation(index); 
		}
		System.out.println("foundation #" + index + " clicked");
	}

	/**
	 * Called when given pile is clicked.
	 * If waste selected, top card from waste 
	 * moved onto given pile. 
	 * @param index        index of pile that has been clicked
	 * @precondition       0 <= index < 7
	 * @postcondition      card has either been flipped upward, a stack
	 *                     of cards has been moved to another pile,
	 *                     or a card has been added to pile from waste
	 */
	public void pileClicked(int index)
	{
	    if (!display.isWasteSelected() && !display.isPileSelected() && 
	        !display.isFoundationSelected() && !piles[index].isEmpty())
	    {
	        if (piles[index].peek().isFaceUp())
	            display.selectPile(index); 
	        else
	            piles[index].peek().turnUp(); 
	    }
	    else if (display.isPileSelected())
	    {
	        if (display.selectedPile() == index)
	            display.unselect(); 
	        else
	        {
	            Stack<Card>s = removeFaceUpCards(display.selectedPile()); 
	            int len = s.size(); 
	            while (!s.isEmpty() && canAddToPile(s.peek(), index))
	            {
	                piles[index].push(s.pop());
	            }
	            if (!s.isEmpty())
	            {
	                for (int i = 0; i < len-s.size(); i++)
	                {
	                    s.push(piles[index].pop());
	                }
	                addToPile(s, display.selectedPile());
	            }
	            display.unselect(); 
	        }
	    }
	    else if (display.isWasteSelected())
		{
		    if (canAddToPile(waste.peek(), index)) 
		    {
		        piles[index].push(waste.pop()); 
		        display.unselect(); 
		    }
		}
		else if (display.isFoundationSelected())
		{
		    if (!foundations[display.selectedFoundation()-3].isEmpty() && 
		         canAddToPile(foundations[display.selectedFoundation()-3].peek(), index))
		    {
		        piles[index].push(foundations[display.selectedFoundation()-3].pop()); 
		        display.unselect(); 
		    }
		}
		System.out.println("pile #" + index + " clicked");
	}
	
	/**
	 * Determines if card can legally be moved to the top of the 
	 * provided pile according to the rules: 
	 * If the top card is face up, then only a card of the opposite
	 * color and next lower rank may be added. 
	 * @param card         card being placed on pile
	 * @param index        index of pile
	 * @precondition       0 <= index < 7
	 * @return             true if the given card can be legally moved
	 *                          to the top of the given pile; otherwise,
	 *                     false
	 */
	public boolean canAddToPile(Card card, int index)
	{
	    if (piles[index].isEmpty()) 
	    {
	        if (card.getRank() == 13) 
	            return true; 
	        return false; 
	    }
	    if (piles[index].peek().isFaceUp())
	    {
	        if (card.getRank() != piles[index].peek().getRank()-1) return false; 
	        if (piles[index].peek().isRed() && card.isRed()) return false; 
	        if (!piles[index].peek().isRed() && !card.isRed()) return false; 
	    }
	    return true; 
	}
	
	/**
	 * Removes and stores the face up cards in the pile at the 
	 * given index. 
	 * @param index        pile that is being removed from
	 * @precondition       0 <= index < 7
	 * @postcondition      Removes all face-up cards on the top of the given pile.
	 * @return             A stack containing removed face-up cards.
	 */
	private Stack<Card> removeFaceUpCards(int index)
	{
	    Stack<Card>ret = new Stack<Card>(); 
	    Stack<Card>temp = new Stack<Card>(); 
	    while(!piles[index].isEmpty())
	    {
	        if (!piles[index].peek().isFaceUp())
	        {
	            temp.push(piles[index].pop()); 
	        }
	        else
	        {
	            ret.push(piles[index].pop()); 
	        }
	    }
	    while (!temp.isEmpty())
	    {
	        piles[index].push(temp.pop()); 
	    }
	    return ret; 
	}
	
	/**
	 * Adds removed elements from given stack to the pile at the provided
	 * index. 
	 * @param cards        stack of removed cards
	 * @param index        pile that is being added to
	 * @precondition       0 <= index < 7
	 * @postcondition      Removes elements from cards and adds them to given pile. 
	 */
	private void addToPile(Stack<Card>cards, int index)
	{
	    while (!cards.isEmpty())
	    {
	        piles[index].push(cards.pop()); 
	    }
	}
	
	/**
	 * Determines if it is legal to add card to foundation. 
	 * @param card         card being added
	 * @param index        index of foundation being added to 
	 * @precondition       0 <= index < 4
	 * @return             true if the given card can be legally moved to the top of
	 *                          the given foundation; otherwise,
	 *                     false
	 */
	private boolean canAddToFoundation(Card card, int index)
	{
	    if (foundations[index].isEmpty()) 
	    {
	        if (card.getRank() != 1) return false; 
	        return true; 
	    }
	    if (foundations[index].peek().getRank() + 1 != card.getRank()) return false; 
	    if (!foundations[index].peek().getSuit().equals(card.getSuit())) return false;
	    return true; 
	}

	/**
	 * Determines if solitaire game as been won by player by
	 * checking if the top cards of the stacks of the foundations
	 * are all kings. 
	 * @return         true if the player has won; otherwise,
	 *                 false
	 */
	private boolean hasWon()
	{
	    for (int i = 0; i < 4; i++)
	    {
	        if (foundations[i].isEmpty()) return false;
	        if (foundations[i].peek().getRank() != 13)  return false;
	    }
	    return true; 
	}
	
	/**
	 * Clears the board, removing all stacks of cards.
	 * @postcondition      all cards have been removed from the board
	 */
	private void clearBoard()
	{
	    stock.clear(); 
	    waste.clear(); 
	    for (int i = 0; i < 4; i++)
	    {
	        foundations[i].clear(); 
	    }
	    for (int i = 0; i < 7; i++)
	    {
	        piles[i].clear(); 
	    }
	}
}