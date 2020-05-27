package flipcoin.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlipCoinStateTest {

    @Test
    void testIsRightMostH() {
        FlipCoinState flipCoinState = new FlipCoinState();
        flipCoinState.setCoinBoard(new char[]{'T','H','H','H','H','H','H','H','H','T'});
        assertFalse(flipCoinState.isRightMostH(9));
        assertTrue(flipCoinState.isRightMostH(6));
        assertTrue(flipCoinState.isRightMostH(3));
        assertTrue(flipCoinState.isRightMostH(1));
        assertFalse(flipCoinState.isRightMostH(0));
    }

    @Test
    void testDoFlip() {
        FlipCoinState flipCoinState = new FlipCoinState();

        flipCoinState.doFlip(1);
        assertEquals('T',flipCoinState.getCoinBoard()[1]);
        assertEquals('H',flipCoinState.getCoinBoard()[3]);

        flipCoinState.doFlip(3);
        assertEquals('T',flipCoinState.getCoinBoard()[3]);
    }

    @Test
    void testIsGameFinished() {
        FlipCoinState flipCoinState = new FlipCoinState();
        assertFalse(flipCoinState.isGameFinished());

        flipCoinState.setCoinBoard(new char[]{'T','T','T','T','T','T','T','T','T','T'});
        assertTrue(flipCoinState.isGameFinished());

        flipCoinState.doFlip(2);
        assertFalse(flipCoinState.isGameFinished());
    }
}