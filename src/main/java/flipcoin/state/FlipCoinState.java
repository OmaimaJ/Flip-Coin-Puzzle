package flipcoin.state;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Class representing the state of the puzzle.
 */

@Data
@Slf4j
public class FlipCoinState {
    /**
     * The array representing the initial configuration.
     */
    private char[] coinBoard = new char[]{'H','H','H','H','H','H','H','H','H','H'};

    /**
     *
     * Returns whether the coin at the right most position is Head or Tails.
     *
     * @param col represents the index position {@code coinBoard[]}.
     * @return {@code True}, if right most position is Head,
     *         {@code False}, otherwise.
     */
    public boolean isRightMostH(int col){
        if(coinBoard[col]=='H')
            return true;
        return false;
    }

    /**
     * Flips the selected coin.
     *
     * @param col represents the index position {@code coinBoard[]}.
     */
    public void doFlip(int col)
    {
        if (coinBoard[col] == 'H')
            coinBoard[col] = 'T';
        else
            coinBoard[col] = 'H';
        log.info("Coin at (0,{}) is flipped to {}", col,coinBoard[col]);
    }

    /**
     * Checks whether the puzzle is solved.
     *
     * @return {@code true} if the puzzle is solved, {@code false} otherwise
     */
    public boolean isGameFinished()
    {
        for(int i = 0; i<coinBoard.length; i++)
            if(coinBoard[i]!='T')
                return false;
        return true;
    }

}

