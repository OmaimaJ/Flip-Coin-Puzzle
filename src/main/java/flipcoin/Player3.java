package flipcoin;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Class representing a specific player.
 */
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Player3 {

    /**
     * Stores the name of the player.
     */
    @Id
    @Column(nullable = false)
    private String playerName;

    /**
     * Stores the number of wins of the player.
     */
    @Column(nullable = false)
    private int wins;

}
