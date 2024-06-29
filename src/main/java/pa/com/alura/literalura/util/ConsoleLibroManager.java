package pa.com.alura.literalura.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pa.com.alura.literalura.dto.LibroDTO;
import pa.com.alura.literalura.controller.ConsoleController;

import java.util.List;
import java.util.Scanner;

public class ConsoleLibroManager {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleLibroManager.class);

    private ConsoleController controller;

    public ConsoleLibroManager(ConsoleController controller) {
        this.controller = controller;
    }

    public void listarLibros() {
        logger.info("Iniciando listado de libros");
        List<LibroDTO> libros = controller.listarLibros();
        if (libros.isEmpty()) {
            ConsoleMenuManager.imprimirRespuesta("No hay libros registrados.");
            logger.info("No se encontraron libros registrados");
        } else {
            ConsoleMenuManager.imprimirRespuesta("\nLibros registrados:");
            for (LibroDTO libro : libros) {
                ConsoleMenuManager.imprimirRespuesta(libro.toString());
            }
            logger.info("Se listaron {} libros", libros.size());
        }
    }

    public void buscarLibroPorTitulo(Scanner scanner) {
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine();
        logger.info("Buscando libro por título: {}", titulo);
        try {
            LibroDTO libro = controller.buscarLibroPorTitulo(titulo);
            if (libro != null) {
                ConsoleMenuManager.imprimirRespuesta("Libro encontrado:");
                ConsoleMenuManager.imprimirRespuesta(libro.toString());
                logger.info("Libro encontrado: {}", libro.getTitulo());
                System.out.print("¿Desea agregar este libro a la base de datos? (S/N): ");
                String respuesta = scanner.nextLine();
                if (respuesta.equalsIgnoreCase("S")) {
                    controller.agregarLibro(libro);
                    ConsoleMenuManager.imprimirRespuesta("Libro agregado a la base de datos.");
                    logger.info("Libro agregado a la base de datos: {}", libro.getTitulo());
                }
            } else {
                ConsoleMenuManager.imprimirRespuesta("Libro no encontrado en Gutendex.");
                logger.info("Libro no encontrado en Gutendex: {}", titulo);
            }
        } catch (Exception e) {
            ConsoleMenuManager.imprimirRespuesta("Error al buscar el libro: " + e.getMessage());
            logger.error("Error al buscar el libro: {}", titulo, e);
        }
    }

    public void agregarNuevoLibro(Scanner scanner) {
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine();
        System.out.print("Ingrese el autor del libro: ");
        String autor = scanner.nextLine();
        System.out.print("Ingrese el idioma del libro: ");
        String idioma = scanner.nextLine();
        System.out.print("Ingrese el número de descargas del libro: ");
        int descargas = scanner.nextInt();
        scanner.nextLine(); // Consumir nueva línea

        LibroDTO libro = new LibroDTO(titulo, autor, idioma, descargas);
        logger.info("Intentando agregar nuevo libro: {}", titulo);

        try {
            controller.agregarLibro(libro);
            ConsoleMenuManager.imprimirRespuesta("Libro agregado exitosamente.");
            logger.info("Libro agregado exitosamente: {}", titulo);
        } catch (Exception e) {
            ConsoleMenuManager.imprimirRespuesta("Error al agregar el libro: " + e.getMessage());
            logger.error("Error al agregar el libro: {}", titulo, e);
        }
    }

    public void listarLibrosPorIdioma(Scanner scanner) {
        System.out.print("Ingrese el código de idioma (ES, EN, FR, PT): ");
        String idioma = scanner.nextLine();
        logger.info("Buscando libros en el idioma: {}", idioma);
        List<LibroDTO> libros = controller.listarLibrosPorIdioma(idioma);
        if (libros.isEmpty()) {
            ConsoleMenuManager.imprimirRespuesta("No se encontraron libros en el idioma " + idioma);
            logger.info("No se encontraron libros en el idioma {}", idioma);
        } else {
            ConsoleMenuManager.imprimirRespuesta("\nLibros en el idioma " + idioma + ":");
            for (LibroDTO libro : libros) {
                ConsoleMenuManager.imprimirRespuesta(libro.toString());
            }
            logger.info("Se encontraron {} libros en el idioma {}", libros.size(), idioma);
        }
    }
}