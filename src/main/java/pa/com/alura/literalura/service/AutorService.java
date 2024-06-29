package pa.com.alura.literalura.service;

import org.springframework.stereotype.Service;
import pa.com.alura.literalura.model.Autor;
import pa.com.alura.literalura.repository.AutorRepository;

//import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorService {

    private AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public List<Autor> listarAutores() {
        return autorRepository.findAll();
    }

    public List<Autor> listarAutoresVivosEnAno(int ano) {
        return listarAutores().stream()
                .filter(autor -> autor.getFechaMuerte() == null || autor.getFechaMuerte().getYear() >= ano)
                .collect(Collectors.toList());
    }

    public boolean existeAutorPorNombreYApellido(String nombre, String apellido) {
        return autorRepository.findByNombreAndApellido(nombre, apellido) != null;
    }
}