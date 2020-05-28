package flipcoin.results;

import com.google.inject.persist.Transactional;
import util.jpa.GenericJpaDao;

import javax.persistence.Persistence;
import java.util.List;

/**
 * DAO class for the {@link GameResult3} entity.
 */
public class GameResultDao extends GenericJpaDao<GameResult3> {

    private static GameResultDao instance; private GameResultDao() { super(GameResult3.class); }

    /**
     * Creates an entity manager named {@code flip-coin}.
     * @return instance of {@code GameResultDao}
     */
    public static GameResultDao getInstance() {
        if (instance == null) { instance = new GameResultDao();
        instance.setEntityManager(Persistence
                .createEntityManagerFactory("flip-coin")
                .createEntityManager()); }
        return instance; }



}
