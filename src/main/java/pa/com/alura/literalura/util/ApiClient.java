package pa.com.alura.literalura.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pa.com.alura.literalura.dto.LibroDTO;
import pa.com.alura.literalura.dto.AutorDTO;
import pa.com.alura.literalura.model.Libro;
import pa.com.alura.literalura.model.Autor;
import pa.com.alura.literalura.service.CatalogoService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ApiClient {

    private final String API_URL = "https://gutendex.com/books";
    private final CatalogoService catalogoService;

    public ApiClient(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public LibroDTO buscarLibroEnGutendex(String titulo) {
        RestTemplate restTemplate = new RestTemplate();
        String url = API_URL + "?search=" + titulo;
        ResponseEntity<GutendexResponse> response = restTemplate.getForEntity(url, GutendexResponse.class);
        
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && !response.getBody().getResults().isEmpty()) {
            GutendexBook book = response.getBody().getResults().get(0);
            Author gutendexAuthor = book.getAuthors().get(0);
            return new LibroDTO(
                book.getTitle(),
                gutendexAuthor.getName(),
                book.getLanguages().get(0),
                book.getDownload_count(),
                gutendexAuthor.getBirth_year(),
                gutendexAuthor.getDeath_year()
            );
        }
        return null;
    }

    public List<LibroDTO> listarLibros() {
        return catalogoService.listarLibros();
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

    public void agregarLibro(LibroDTO libroDTO) {
        Libro libro = new Libro();
        libro.setTitulo(libroDTO.getTitulo());
        libro.setIdioma(libroDTO.getIdioma());
        libro.setDescargas(libroDTO.getDescargas());

        Autor autor = new Autor();
        String[] nombreCompleto = libroDTO.getAutor().split(", ");
        autor.setApellido(nombreCompleto[0]);
        autor.setNombre(nombreCompleto[1]);
        autor.setFechaNacimiento(libroDTO.getAnioNacimientoAutor() != null ? LocalDate.of(libroDTO.getAnioNacimientoAutor(), 1, 1) : null);
        autor.setFechaMuerte(libroDTO.getAnioMuerteAutor() != null ? LocalDate.of(libroDTO.getAnioMuerteAutor(), 12, 31) : null);

        autor = catalogoService.buscarOAgregarAutor(autor);

        Set<Autor> autores = new HashSet<>();
        autores.add(autor);
        libro.setAutores(autores);

        catalogoService.agregarLibro(libro);
    }

    private static class GutendexResponse {
        private List<GutendexBook> results;

        public List<GutendexBook> getResults() {
            return results;
        }

        public void setResults(List<GutendexBook> results) {
            this.results = results;
        }
    }

    private static class GutendexBook {
        private String title;
        private List<Author> authors;
        private List<String> languages;
        private int download_count;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Author> getAuthors() {
            return authors;
        }

        public void setAuthors(List<Author> authors) {
            this.authors = authors;
        }

        public List<String> getLanguages() {
            return languages;
        }

        public void setLanguages(List<String> languages) {
            this.languages = languages;
        }

        public int getDownload_count() {
            return download_count;
        }

        public void setDownload_count(int download_count) {
            this.download_count = download_count;
        }
    }

    private static class Author {
        private String name;
        private Integer birth_year;
        private Integer death_year;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getBirth_year() {
            return birth_year;
        }

        public void setBirth_year(Integer birth_year) {
            this.birth_year = birth_year;
        }

        public Integer getDeath_year() {
            return death_year;
        }

        public void setDeath_year(Integer death_year) {
            this.death_year = death_year;
        }
    }
}