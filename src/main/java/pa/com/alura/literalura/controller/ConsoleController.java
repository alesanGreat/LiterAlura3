package pa.com.alura.literalura.controller;

import pa.com.alura.literalura.dto.LibroDTO;
import pa.com.alura.literalura.dto.AutorDTO;
import pa.com.alura.literalura.service.CatalogoService;
import pa.com.alura.literalura.util.ApiClient;

import java.util.List;

public class ConsoleController {

    private CatalogoService catalogoService;
    private ApiClient apiClient;

    public ConsoleController(CatalogoService catalogoService, ApiClient apiClient) {
        this.catalogoService = catalogoService;
        this.apiClient = apiClient;
    }

    public List<LibroDTO> listarLibros() {
        return catalogoService.listarLibros();
    }

    public LibroDTO buscarLibroPorTitulo(String titulo) {
        return apiClient.buscarLibroEnGutendex(titulo);
    }

    public void agregarLibro(LibroDTO libro) throws Exception {
        catalogoService.agregarLibro(libro);
    }

    public List<AutorDTO> listarAutores() {
        return catalogoService.listarAutores();
    }

    public List<AutorDTO> listarAutoresVivosEnAno(int ano) {
        return catalogoService.listarAutoresVivosEnAno(ano);
    }

    public List<LibroDTO> listarLibrosPorIdioma(String idioma) {
        return catalogoService.listarLibrosPorIdioma(idioma);
    }

    public List<AutorDTO> listarAutoresConFechas() {
        return catalogoService.listarAutoresConFechas();
    }
}