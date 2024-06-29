package pa.com.alura.literalura;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pa.com.alura.literalura.model.Autor;
import pa.com.alura.literalura.model.Libro;
import pa.com.alura.literalura.repository.AutorRepository;
import pa.com.alura.literalura.repository.LibroRepository;
import pa.com.alura.literalura.service.CatalogoService;
import pa.com.alura.literalura.dto.AutorDTO;
import pa.com.alura.literalura.dto.LibroDTO;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class LiterAluraApplicationTests {

    @Autowired
    private CatalogoService catalogoService;

    @MockBean
    private LibroRepository libroRepository;

    @MockBean
    private AutorRepository autorRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void listarAutoresConFechas_DeberiaRetornarAutoresConFechasCorrectas() {
        Autor autor1 = new Autor();
        autor1.setNombre("Franz");
        autor1.setApellido("Kafka");
        autor1.setFechaNacimiento(LocalDate.of(1883, 7, 3));
        autor1.setFechaMuerte(LocalDate.of(1924, 6, 3));

        Autor autor2 = new Autor();
        autor2.setNombre("Gabriel");
        autor2.setApellido("García Márquez");
        autor2.setFechaNacimiento(LocalDate.of(1927, 3, 6));
        autor2.setFechaMuerte(LocalDate.of(2014, 4, 17));

        when(autorRepository.findAll()).thenReturn(Arrays.asList(autor1, autor2));

        List<AutorDTO> resultado = catalogoService.listarAutoresConFechas();

        assertEquals(2, resultado.size());

        AutorDTO autorDTO1 = resultado.get(0);
        assertEquals("Franz", autorDTO1.getNombre());
        assertEquals("Kafka", autorDTO1.getApellido());
        assertEquals(LocalDate.of(1883, 7, 3), autorDTO1.getFechaNacimiento());
        assertEquals(LocalDate.of(1924, 6, 3), autorDTO1.getFechaMuerte());

        AutorDTO autorDTO2 = resultado.get(1);
        assertEquals("Gabriel", autorDTO2.getNombre());
        assertEquals("García Márquez", autorDTO2.getApellido());
        assertEquals(LocalDate.of(1927, 3, 6), autorDTO2.getFechaNacimiento());
        assertEquals(LocalDate.of(2014, 4, 17), autorDTO2.getFechaMuerte());
    }

    @Test
    void listarAutoresConFechas_DeberiaManejarFechasNulas() {
        Autor autor = new Autor();
        autor.setNombre("John");
        autor.setApellido("Doe");

        when(autorRepository.findAll()).thenReturn(Arrays.asList(autor));

        List<AutorDTO> resultado = catalogoService.listarAutoresConFechas();

        assertEquals(1, resultado.size());

        AutorDTO autorDTO = resultado.get(0);
        assertEquals("John", autorDTO.getNombre());
        assertEquals("Doe", autorDTO.getApellido());
        assertNull(autorDTO.getFechaNacimiento());
        assertNull(autorDTO.getFechaMuerte());
    }

    @Test
    void buscarOAgregarAutor_DeberiaAgregarNuevoAutor() {
        String nombreCompleto = "García Márquez, Gabriel";
        LocalDate fechaNacimiento = LocalDate.of(1927, 3, 6);
        LocalDate fechaMuerte = LocalDate.of(2014, 4, 17);

        when(autorRepository.findByNombreAndApellido(anyString(), anyString())).thenReturn(Optional.empty());
        when(autorRepository.save(any(Autor.class))).thenAnswer(invocation -> {
            Autor autorGuardado = invocation.getArgument(0);
            autorGuardado.setId(1L);
            return autorGuardado;
        });

        Autor resultado = catalogoService.buscarOAgregarAutor(nombreCompleto, fechaNacimiento, fechaMuerte);

        assertNotNull(resultado);
        assertEquals("Gabriel", resultado.getNombre());
        assertEquals("García Márquez", resultado.getApellido());
        verify(autorRepository).save(any(Autor.class));
    }

    @Test
    void buscarLibroPorTitulo_DeberiaRetornarLibro() {
        String titulo = "Don Quijote";
        Libro libroEsperado = new Libro();
        libroEsperado.setTitulo(titulo);
        libroEsperado.setIdioma("ES");
        libroEsperado.setDescargas(1000);
        when(libroRepository.findByTitulo(titulo)).thenReturn(Optional.of(libroEsperado));

        Libro resultado = catalogoService.buscarLibroPorTitulo(titulo);

        assertNotNull(resultado);
        assertEquals(titulo, resultado.getTitulo());
    }

    @Test
    void listarLibrosRegistrados_DeberiaRetornarTodosLosLibros() {
        List<Libro> librosEsperados = Arrays.asList(
            crearLibro("Libro 1", "ES", 100),
            crearLibro("Libro 2", "EN", 200)
        );
        when(libroRepository.findAll()).thenReturn(librosEsperados);

        List<LibroDTO> resultado = catalogoService.listarLibros();

        assertEquals(librosEsperados.size(), resultado.size());
        assertEquals(librosEsperados.get(0).getTitulo(), resultado.get(0).getTitulo());
        assertEquals(librosEsperados.get(1).getTitulo(), resultado.get(1).getTitulo());
    }

    @Test
    void listarAutoresRegistrados_DeberiaRetornarAutoresUnicos() {
        List<Autor> autoresEsperados = Arrays.asList(
            crearAutor("Miguel", "Cervantes", LocalDate.of(1547, 9, 29), LocalDate.of(1616, 4, 22)),
            crearAutor("William", "Shakespeare", LocalDate.of(1564, 4, 26), LocalDate.of(1616, 4, 23))
        );
        when(autorRepository.findAll()).thenReturn(autoresEsperados);

        List<AutorDTO> resultado = catalogoService.listarAutores();

        assertEquals(autoresEsperados.size(), resultado.size());
        assertEquals(autoresEsperados.get(0).getNombre(), resultado.get(0).getNombre());
        assertEquals(autoresEsperados.get(1).getNombre(), resultado.get(1).getNombre());
    }

    @Test
    void listarLibrosPorIdioma_DeberiaRetornarLibrosCorrectosParaIdioma() {
        String idioma = "ES";
        List<Libro> librosEsperados = Arrays.asList(
            crearLibro("Don Quijote", "ES", 1000),
            crearLibro("Cien años de soledad", "ES", 500)
        );
        when(libroRepository.findByIdiomaWithAutores(idioma)).thenReturn(librosEsperados);

        List<LibroDTO> resultado = catalogoService.listarLibrosPorIdioma(idioma);

        assertEquals(librosEsperados.size(), resultado.size());
        assertEquals(librosEsperados.get(0).getTitulo(), resultado.get(0).getTitulo());
        assertEquals(librosEsperados.get(1).getTitulo(), resultado.get(1).getTitulo());
        assertTrue(resultado.stream().allMatch(libro -> libro.getIdioma().equals(idioma)));
    }

    @Test
    void listarAutoresVivosEnAnio_DeberiaRetornarAutoresCorrectos() {
        int anio = 1600;
        List<Autor> autoresEsperados = Arrays.asList(
            crearAutor("Miguel", "Cervantes", LocalDate.of(1547, 9, 29), LocalDate.of(1616, 4, 22)),
            crearAutor("William", "Shakespeare", LocalDate.of(1564, 4, 26), LocalDate.of(1616, 4, 23))
        );
        when(autorRepository.findAutoresVivosPorAño(anio)).thenReturn(autoresEsperados);

        // Convertir la lista esperada a AutorDTO
        List<AutorDTO> autoresEsperadosDTO = autoresEsperados.stream()
            .map(AutorDTO::new)
            .collect(Collectors.toList());

        List<AutorDTO> resultado = catalogoService.listarAutoresVivosEnAno(anio);

        assertEquals(autoresEsperadosDTO.size(), resultado.size());
        assertEquals(autoresEsperadosDTO.get(0).getNombre(), resultado.get(0).getNombre());
        assertEquals(autoresEsperadosDTO.get(1).getNombre(), resultado.get(1).getNombre());
    }

    private Libro crearLibro(String titulo, String idioma, int descargas) {
        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setIdioma(idioma);
        libro.setDescargas(descargas);
        return libro;
    }

    private Autor crearAutor(String nombre, String apellido, LocalDate fechaNacimiento, LocalDate fechaMuerte) {
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setApellido(apellido);
        autor.setFechaNacimiento(fechaNacimiento);
        autor.setFechaMuerte(fechaMuerte);
        return autor;
    }
}
