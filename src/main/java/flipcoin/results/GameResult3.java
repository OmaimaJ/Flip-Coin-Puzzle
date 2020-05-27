package flipcoin.results;

import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.ZonedDateTime;


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

    @Column(nullable = false)
    private String player;

    private boolean solved;
}
