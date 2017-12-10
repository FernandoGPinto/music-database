package test.util;

/**
 * Created by Fernando on 12/10/2017.
 */
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory emFactory;

    static {
        emFactory = Persistence.createEntityManagerFactory("MusicApp");
    }

    public static EntityManager getEntityManager(){
        return emFactory.createEntityManager();
    }

    public static void close(){
        emFactory.close();
    }
}
