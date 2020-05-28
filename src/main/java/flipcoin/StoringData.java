package flipcoin;

import lombok.Getter;
import lombok.Setter;
/**
 * Class used to store in-game player names.
 */

public class StoringData {

        /**
         * The name of the player 1.
         */
        @Setter
        @Getter
        private static String p1;

        /**
         * The name of the player 2.
         */
        @Setter
        @Getter
        private static String p2;


}
