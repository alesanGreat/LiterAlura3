package pa.com.alura.literalura.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DbCleanupService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void limpiarDB() {
        List<?> resultList = entityManager.createNativeQuery(
                "SELECT table_name FROM information_schema.tables WHERE table_schema='public'")
                .getResultList();

        List<String> tableNames = resultList.stream()
                .map(result -> (String) result)
                .collect(Collectors.toList());

        entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();

        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName + " CASCADE").executeUpdate();
        }

        entityManager.createNativeQuery("SET CONSTRAINTS ALL IMMEDIATE").executeUpdate();
    }
}
