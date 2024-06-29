package pa.com.alura.literalura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pa.com.alura.literalura.model.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTitulo(String titulo);
    
    @Query("SELECT DISTINCT l FROM Libro l LEFT JOIN FETCH l.autores WHERE l.idioma = :idioma")
    List<Libro> findByIdiomaWithAutores(@Param("idioma") String idioma);
    
    boolean existsByTitulo(String titulo);
    
    List<Libro> findByTituloContainingIgnoreCase(String fragmentoTitulo);
    
    @Query("SELECT l FROM Libro l WHERE l.descargas > :minDescargas")
    List<Libro> findLibrosByDescargasMayorQue(@Param("minDescargas") int minDescargas);
    
    @Query("SELECT l FROM Libro l JOIN l.autores a WHERE a.nombre = :nombreAutor OR a.apellido = :apellidoAutor")
    List<Libro> findLibrosByAutor(@Param("nombreAutor") String nombreAutor, @Param("apellidoAutor") String apellidoAutor);
}