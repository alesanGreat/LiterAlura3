package pa.com.alura.literalura;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pa.com.alura.literalura.service.DbCleanupService;

@Component
public class DbInitializer implements ApplicationRunner {

    private final DbCleanupService dbCleanupService;

    public DbInitializer(DbCleanupService dbCleanupService) {
        this.dbCleanupService = dbCleanupService;
    }

    @Override
    public void run(ApplicationArguments args) {
        dbCleanupService.limpiarDB();
    }
}