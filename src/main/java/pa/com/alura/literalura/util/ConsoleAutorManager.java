package pa.com.alura.literalura.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pa.com.alura.literalura.dto.AutorDTO;
import pa.com.alura.literalura.controller.ConsoleController;

import java.util.List;
import java.util.Scanner;

public class ConsoleAutorManager {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleAutorManager.class);

    private ConsoleController controller;

    public ConsoleAutorManager(ConsoleController controller) {
        this.controller = controller;
    }

    public void listarAutores() {
        logger.info("Iniciando listado de autores");
        List<AutorDTO> autores = controller.listarAutores();
        if (autores.isEmpty()) {
            ConsoleMenuManager.imprimirRespuesta("No hay autores registrados.");
            logger.info("No se encontraron autores registrados");
        } else {
            ConsoleMenuManager.imprimirRespuesta("\nAutores registrados:");
            for (AutorDTO autor : autores) {
                ConsoleMenuManager.imprimirRespuesta(autor.toString());
            }
            logger.info("Se listaron {} autores", autores.size());
        }
    }

    public void listarAutoresVivosEnAno(Scanner scanner) {
        System.out.print("Ingrese el año: ");
        int ano = scanner.nextInt();
        scanner.nextLine(); // Consumir nueva línea
        logger.info("Buscando autores vivos en el año: {}", ano);
        List<AutorDTO> autores = controller.listarAutoresVivosEnAno(ano);
        if (autores.isEmpty()) {
            ConsoleMenuManager.imprimirRespuesta("No se encontraron autores vivos en el año " + ano);
            logger.info("No se encontraron autores vivos en el año {}", ano);
        } else {
            ConsoleMenuManager.imprimirRespuesta("\nAutores vivos en el año " + ano + ":");
            for (AutorDTO autor : autores) {
                ConsoleMenuManager.imprimirRespuesta(autor.toString());
            }
            logger.info("Se encontraron {} autores vivos en el año {}", autores.size(), ano);
        }
    }

    public void listarAutoresConFechas() {
        logger.info("Iniciando listado de autores con fechas");
        List<AutorDTO> autores = controller.listarAutoresConFechas();
        if (autores.isEmpty()) {
            ConsoleMenuManager.imprimirRespuesta("No hay autores registrados.");
            logger.info("No se encontraron autores registrados");
        } else {
            ConsoleMenuManager.imprimirRespuesta("\nAutores registrados (con fechas):");
            for (AutorDTO autor : autores) {
                String fechaFallecimiento = autor.getFechaMuerte() != null ? autor.getFechaMuerte().toString() : "--";
                System.out.printf("%s %s (Nacimiento: %s, Fallecimiento: %s)%n",
                        autor.getNombre(),
                        autor.getApellido(),
                        autor.getFechaNacimiento(),
                        fechaFallecimiento);
            }
            logger.info("Se listaron {} autores con fechas", autores.size());
        }
    }
}