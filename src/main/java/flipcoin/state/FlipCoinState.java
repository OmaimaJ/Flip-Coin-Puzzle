package flipcoin.state;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class FlipCoinState {
    private char[] coinBoard = new char[]{'H','H','H','H','H','H','H','H','H','H'};

    public boolean isRightMostH(int col){
        if(coinBoard[col]=='H')
            return true;
        return false;
    }

    public void doFlip(int col)
    {
        if (coinBoard[col] == 'H')
            coinBoard[col] = 'T';
        else
            coinBoard[col] = 'H';
        log.info("Coin at (0,{}) is flipped to {}", col,coinBoard[col]);
    }

    public boolean isGameFinished()
    {
        for(int i = 0; i<coinBoard.length; i++)
            if(coinBoard[i]!='T')
                return false;
        return true;
    }

}

