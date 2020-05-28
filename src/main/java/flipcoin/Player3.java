package flipcoin;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Player3 {

    @Id
    @Column(nullable = false)
    private String playerName;

    @Column(nullable = false)
    private int wins;

}
