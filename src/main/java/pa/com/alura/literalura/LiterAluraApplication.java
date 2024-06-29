package pa.com.alura.literalura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import pa.com.alura.literalura.repository.LibroRepository;
import pa.com.alura.literalura.repository.AutorRepository;
import pa.com.alura.literalura.service.CatalogoService;
import pa.com.alura.literalura.service.DbCleanupService;
import pa.com.alura.literalura.util.ConsoleMenuManager;
import pa.com.alura.literalura.util.ApiClient;
import pa.com.alura.literalura.controller.ConsoleController;

@SpringBootApplication
public class LiterAluraApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LiterAluraApplication.class, args);
        
        DbCleanupService dbCleanupService = context.getBean(DbCleanupService.class);
        dbCleanupService.limpiarDB();

        ConsoleMenuManager consoleMenuManager = context.getBean(ConsoleMenuManager.class);
        consoleMenuManager.mostrarMenu();
    }

    @Bean
    public CatalogoService catalogoService(LibroRepository libroRepository, AutorRepository autorRepository) {
        return new CatalogoService(libroRepository, autorRepository);
    }

    @Bean
    public ApiClient apiClient(CatalogoService catalogoService) {
        return new ApiClient(catalogoService);
    }

    @Bean
    public ConsoleController consoleController(CatalogoService catalogoService, ApiClient apiClient) {
        return new ConsoleController(catalogoService, apiClient);
    }

    @Bean
    public ConsoleMenuManager consoleMenuManager(ConsoleController consoleController) {
        return new ConsoleMenuManager(consoleController);
    }
}
