package pa.com.alura.literalura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pa.com.alura.literalura.model.Autor;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombreAndApellido(String nombre, String apellido);

    List<Autor> findAllByOrderByApellidoAscNombreAsc();

    @Query("SELECT a FROM Autor a WHERE :año BETWEEN YEAR(a.fechaNacimiento) AND COALESCE(YEAR(a.fechaMuerte), 3000)")
    List<Autor> findAutoresVivosPorAño(@Param("año") int año);

    @Query("SELECT a FROM Autor a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :fragmento, '%')) OR LOWER(a.apellido) LIKE LOWER(CONCAT('%', :fragmento, '%'))")
    List<Autor> buscarPorFragmentoNombreOApellido(@Param("fragmento") String fragmento);

    boolean existsByNombreAndApellido(String nombre, String apellido);
}
