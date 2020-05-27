package flipcoin.state;

import lombok.Data;

@Data
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
    }

    public boolean isGameFinished()
    {
        for(int i = 0; i<coinBoard.length; i++)
            if(coinBoard[i]!='T')
                return false;
        return true;
    }

}

