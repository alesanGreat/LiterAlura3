package pa.com.alura.literalura.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pa.com.alura.literalura.controller.ConsoleController;

import java.util.Scanner;

public class ConsoleMenuManager {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleMenuManager.class);

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";

    //private ConsoleController controller;
    private ConsoleLibroManager libroManager;
    private ConsoleAutorManager autorManager;

    public ConsoleMenuManager(ConsoleController controller) {
        //this.controller = controller;
        this.libroManager = new ConsoleLibroManager(controller);
        this.autorManager = new ConsoleAutorManager(controller);
    }

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("\nMenú Principal:");
            System.out.println("1. Listar libros registrados");
            System.out.println("2. Buscar libro por título");
            System.out.println("3. Agregar nuevo libro");
            System.out.println("4. Listar autores registrados");
            System.out.println("5. Listar autores vivos en un año específico");
            System.out.println("6. Listar libros por idioma");
            System.out.println("7. Listar todos los autores (con fecha de nacimiento y fallecimiento)");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir nueva línea

            logger.info("Opción seleccionada: {}", opcion);

            switch (opcion) {
                case 1:
                    libroManager.listarLibros();
                    break;
                case 2:
                    libroManager.buscarLibroPorTitulo(scanner);
                    break;
                case 3:
                    libroManager.agregarNuevoLibro(scanner);
                    break;
                case 4:
                    autorManager.listarAutores();
                    break;
                case 5:
                    autorManager.listarAutoresVivosEnAno(scanner);
                    break;
                case 6:
                    libroManager.listarLibrosPorIdioma(scanner);
                    break;
                case 7:
                    autorManager.listarAutoresConFechas();
                    break;
                case 8:
                    continuar = false;
                    logger.info("Saliendo de la aplicación");
                    System.out.println("Gracias por usar LiterAlura. ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción no válida, intente nuevamente.");
                    logger.warn("Opción no válida seleccionada: {}", opcion);
            }
        }

        scanner.close();
    }

    public static void imprimirRespuesta(String respuesta) {
        System.out.printf("\n\n");
        System.out.println(ANSI_GREEN + respuesta + ANSI_RESET);
        System.out.printf("\n\n");
    }
}