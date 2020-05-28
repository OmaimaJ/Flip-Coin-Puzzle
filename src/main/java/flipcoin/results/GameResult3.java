package flipcoin.results;

import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.ZonedDateTime;
/**
 * Class representing the result of a game played by a specific player.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
public class GameResult3 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the player.
     */
    @Column(nullable = false)
    private String player1;

    /**
     * The name of the player.
     */
    @Column(nullable = false)
    private String player2;

    /**
     * The name of the winner.
     */
    @Column(nullable = false)
    private String winner;

    /**
     * The number of steps made by the player.
     */
    @Column(nullable = false)
    private int movesByPlayer1;

    /**
     * The number of steps made by the player.
     */
    @Column(nullable = false)
    private int movesByPlayer2;

    /**
     * Indicates whether the player has solved the puzzle.
     */
    private boolean solved;


    /**
     * The timestamp when the result was saved.
     */
    @Column(nullable = false)
    private ZonedDateTime created;

    @PrePersist
    protected void onPersist() {
        created = ZonedDateTime.now();
    }

}
