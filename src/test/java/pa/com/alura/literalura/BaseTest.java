package pa.com.alura.literalura;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pa.com.alura.literalura.service.DbCleanupService;

@SpringBootTest
public class BaseTest {

    @Autowired
    private DbCleanupService dbCleanupService;

    @BeforeEach
    public void setUp() {
        dbCleanupService.limpiarDB();
    }
}