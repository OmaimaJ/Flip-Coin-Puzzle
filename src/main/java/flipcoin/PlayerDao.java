package flipcoin;

import com.google.inject.persist.Transactional;
import flipcoin.results.GameResult3;
import util.jpa.GenericJpaDao;

import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

/**
 * DAO class for the {@link Player3} entity.
 */
public class PlayerDao extends GenericJpaDao<Player3> {
    private static PlayerDao instance;

    private PlayerDao(){
        super(Player3.class);
    }
    /**
     * Creates an entity manager named {@code flip-coin}.
     * @return instance of {@code PlayerDao}
     */
    public static PlayerDao getInstance(){
        if (instance == null) { instance = new PlayerDao();
            instance.setEntityManager(Persistence
                    .createEntityManagerFactory("flip-coin")
                    .createEntityManager()); }
        return instance;
    }

    /**
     *
     * @param name Stores the player name.
     * @param win Stores the number of wins of the player.
     */
    public void update(String name, int win) {
        entityManager.getTransaction().begin();
        Query updateQuery = entityManager.createQuery("UPDATE Player3 set wins =:n where playerName = :name")
                .setParameter("n", win)
                .setParameter("name", name);
        updateQuery.executeUpdate();
        entityManager.getTransaction().commit();
    }
    /**
     * Returns the list of {@code n} best results with respect to.
     *
     * @param n the maximum number of results to be returned
     * @return the list of {@code n} best results with respect to
     */
    @Transactional
    public List<Player3> findBest(int n) {
        List<Player3> p= entityManager.createQuery("SELECT r FROM Player3 r WHERE r.wins>0 ORDER BY r.wins DESC ", Player3.class)
                .setMaxResults(n)
                .getResultList();

        return p;
    }
}
