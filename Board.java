public class Board {
    private Scene[] cards;
    private int cardIndex = 0;
    private Set[] structure;
    private CastingOffice castingOffice;
    private Trailer trailer;
    private static Board board = null;

    /** Constructors */

    // singleton design pattern
    private Board() {
    }

    /**
     * This is the public method used to get the single instance of the board class
     * 
     * @param cardArray
     * @param structure
     * @param castingOffice
     * @param trailer
     * @return
     */
    public static Board getInstance(Scene[] cardArray, Set[] structure, CastingOffice castingOffice, Trailer trailer) {
        // this is a lazy loading version of singleton
        if (board == null)
            board = new Board(cardArray, structure, castingOffice, trailer);

        return board;
    }

    /**
     * This is the constructor for the singleton method above
     * 
     * @param cards
     * @param structure
     * @param castingOffice
     * @param trailer
     */
    private Board(Scene[] cards, Set[] structure, CastingOffice castingOffice, Trailer trailer) {
        this.cards = cards;
        this.structure = structure;
        this.castingOffice = castingOffice;
        this.trailer = trailer;
    }

    /**
     * getCards() returns cards currently on the board
     * 
     * @param none
     * @return Boards current cards
     */
    public Scene[] getCards() {
        return this.cards;
    }

    /**
     * setCards() sets the cards on the board
     * 
     * @param cards
     * @return void
     */
    public void setCards(Scene[] cards) {
        this.cards = cards;
    }

    /**
     * getTrailer() returns trailer
     * 
     * @param none
     * @return trailer
     */
    public Trailer getTrailer() {
        return this.trailer;
    }

    /**
     * getCastingOffice() returns castingOffice
     * 
     * @param none
     * @return casting office object
     */
    public CastingOffice getCastingOffice() {
        return this.castingOffice;
    }

    /**
     * getStructure() returns the room structure of Board
     * 
     * @param none
     * @return Board's structure
     */
    public Set[] getStructure() {
        return this.structure;
    }

    /**
     * Places cards for the board's sets
     * 
     * @param card
     * @param set
     * @return void
     */
    public void placeCards() {
        // keeps track of the card in the array so no cards are used twice in the same
        // game
        for (int i = 0; i < structure.length; i++) {
            structure[i].setScene(cards[cardIndex]);
            cardIndex++;
        }
    }
}
