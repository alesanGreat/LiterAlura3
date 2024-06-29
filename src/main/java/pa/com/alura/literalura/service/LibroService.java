package pa.com.alura.literalura.service;

import org.springframework.stereotype.Service;
import pa.com.alura.literalura.model.Libro;
import pa.com.alura.literalura.model.Autor;
import pa.com.alura.literalura.repository.LibroRepository;
import pa.com.alura.literalura.repository.AutorRepository;

import java.util.List;

@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public LibroService(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    public List<Autor> listarAutores() {
        return autorRepository.findAll();
    }

    public List<Libro> listarLibrosPorIdioma(String idioma) {
        return libroRepository.findByIdiomaWithAutores(idioma);
    }

    public List<Autor> listarAutoresVivosEnAno(int ano) {
        return autorRepository.findAutoresVivosPorAño(ano);
    }
}