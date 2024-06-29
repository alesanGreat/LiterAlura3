package pa.com.alura.literalura.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pa.com.alura.literalura.model.Autor;
import pa.com.alura.literalura.model.Libro;
import pa.com.alura.literalura.repository.LibroRepository;
import pa.com.alura.literalura.repository.AutorRepository;
import pa.com.alura.literalura.dto.LibroDTO;
import pa.com.alura.literalura.dto.AutorDTO;
import pa.com.alura.literalura.exception.LibroNoEncontradoException;
import pa.com.alura.literalura.exception.LibroDuplicadoException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CatalogoService {
    private static final Logger logger = LoggerFactory.getLogger(CatalogoService.class);

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public CatalogoService(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    @Transactional(readOnly = true)
    public List<LibroDTO> listarLibros() {
        logger.info("Listando todos los libros");
        return libroRepository.findAll().stream()
                .map(LibroDTO::new)
                .collect(Collectors.toList());
    }

    public Libro buscarLibroPorTitulo(String titulo) throws LibroNoEncontradoException {
        logger.info("Buscando libro por título: {}", titulo);
        return libroRepository.findByTitulo(titulo)
                .orElseThrow(() -> {
                    logger.warn("Libro no encontrado: {}", titulo);
                    return new LibroNoEncontradoException("Libro no encontrado: " + titulo);
                });
    }

    @Transactional
    public void agregarLibro(Libro libro) throws LibroDuplicadoException {
        if (libroRepository.existsByTitulo(libro.getTitulo())) {
            logger.warn("Intento de agregar libro duplicado: {}", libro.getTitulo());
            throw new LibroDuplicadoException("El libro ya está registrado: " + libro.getTitulo());
        }
        libroRepository.save(libro);
        logger.info("Libro agregado: {}", libro.getTitulo());
    }

    @Transactional
    public void agregarLibro(LibroDTO libroDTO) throws LibroDuplicadoException {
        if (libroRepository.existsByTitulo(libroDTO.getTitulo())) {
            logger.warn("Intento de agregar libro duplicado: {}", libroDTO.getTitulo());
            throw new LibroDuplicadoException("El libro ya está registrado: " + libroDTO.getTitulo());
        }
        Libro libro = new Libro();
        libro.setTitulo(libroDTO.getTitulo());
        libro.setIdioma(libroDTO.getIdioma());
        libro.setDescargas(libroDTO.getDescargas());
        // Convertir Integer a LocalDate
        LocalDate fechaNacimiento = libroDTO.getAnioNacimientoAutor() != null ? LocalDate.of(libroDTO.getAnioNacimientoAutor(), 1, 1) : null;
        LocalDate fechaMuerte = libroDTO.getAnioMuerteAutor() != null ? LocalDate.of(libroDTO.getAnioMuerteAutor(), 1, 1) : null;
        Autor autor = buscarOAgregarAutor(libroDTO.getAutor(), fechaNacimiento, fechaMuerte);
        libro.getAutores().add(autor);
        libroRepository.save(libro);
        logger.info("Libro agregado: {}", libro.getTitulo());
    }

    @Transactional(readOnly = true)
    public List<AutorDTO> listarAutores() {
        logger.info("Listando todos los autores");
        return autorRepository.findAll().stream()
                .map(AutorDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AutorDTO> listarAutoresVivosEnAno(int ano) {
        logger.info("Listando autores vivos en el año: {}", ano);
        return autorRepository.findAutoresVivosPorAño(ano).stream()
                .map(AutorDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LibroDTO> listarLibrosPorIdioma(String idioma) {
        logger.info("Listando libros por idioma: {}", idioma);
        return libroRepository.findByIdiomaWithAutores(idioma).stream()
                .map(LibroDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Autor buscarOAgregarAutor(String nombreCompleto, LocalDate fechaNacimiento, LocalDate fechaMuerte) {
        logger.info("Buscando o agregando autor: {}", nombreCompleto);
        String[] partes = nombreCompleto.split(", ");
        String apellido = partes.length > 1 ? partes[0].trim() : "";
        String nombre = partes.length > 1 ? partes[1].trim() : nombreCompleto.trim();

        Optional<Autor> autorOptional = autorRepository.findByNombreAndApellido(nombre, apellido);
        if (autorOptional.isPresent()) {
            logger.info("Autor encontrado: {}", nombreCompleto);
            return autorOptional.get();
        } else {
            Autor autor = new Autor();
            autor.setNombre(nombre);
            autor.setApellido(apellido);
            autor.setFechaNacimiento(fechaNacimiento);
            autor.setFechaMuerte(fechaMuerte);
            autorRepository.save(autor);
            logger.info("Nuevo autor agregado: {}", nombreCompleto);
            return autor;
        }
    }

    @Transactional
    public Autor buscarOAgregarAutor(Autor autor) {
        logger.info("Buscando o agregando autor: {} {}", autor.getNombre(), autor.getApellido());
        Optional<Autor> autorOptional = autorRepository.findByNombreAndApellido(autor.getNombre(), autor.getApellido());
        if (autorOptional.isPresent()) {
            logger.info("Autor encontrado: {} {}", autor.getNombre(), autor.getApellido());
            return autorOptional.get();
        } else {
            Autor autorGuardado = autorRepository.save(autor);
            logger.info("Nuevo autor agregado: {} {}", autor.getNombre(), autor.getApellido());
            return autorGuardado;
        }
    }

    @Transactional(readOnly = true)
    public List<AutorDTO> listarAutoresConFechas() {
        logger.info("Listando todos los autores con fechas");
        return autorRepository.findAll().stream()
                .map(AutorDTO::new)
                .collect(Collectors.toList());
    }
}
