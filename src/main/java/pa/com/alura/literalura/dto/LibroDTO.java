package pa.com.alura.literalura.dto;

import pa.com.alura.literalura.model.Autor;
import pa.com.alura.literalura.model.Libro;

import java.util.stream.Collectors;

public class LibroDTO {
    private String titulo;
    private String autor;
    private String idioma;
    private int descargas;
    private Integer anioNacimientoAutor;
    private Integer anioMuerteAutor;

    public LibroDTO(Libro libro) {
        this.titulo = libro.getTitulo();
        this.idioma = libro.getIdioma();
        this.descargas = libro.getDescargas();
        
        if (!libro.getAutores().isEmpty()) {
            this.autor = libro.getAutores().stream()
                    .map(a -> a.getApellido() + ", " + a.getNombre())
                    .collect(Collectors.joining(", "));
            Autor primerAutor = libro.getAutores().iterator().next();
            this.anioNacimientoAutor = primerAutor.getFechaNacimiento() != null ? primerAutor.getFechaNacimiento().getYear() : null;
            this.anioMuerteAutor = primerAutor.getFechaMuerte() != null ? primerAutor.getFechaMuerte().getYear() : null;
        } else {
            this.autor = "";
            this.anioNacimientoAutor = null;
            this.anioMuerteAutor = null;
        }
    }
    

    public LibroDTO(String titulo, String autor, String idioma, int descargas) {
        this.titulo = titulo;
        this.autor = autor;
        this.idioma = idioma;
        this.descargas = descargas;
    }

    public LibroDTO(String titulo, String autor, String idioma, int descargas, Integer anioNacimientoAutor, Integer anioMuerteAutor) {
        this.titulo = titulo;
        this.autor = autor;
        this.idioma = idioma;
        this.descargas = descargas;
        this.anioNacimientoAutor = anioNacimientoAutor;
        this.anioMuerteAutor = anioMuerteAutor;
    }

    // Getters y setters

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public int getDescargas() {
        return descargas;
    }

    public void setDescargas(int descargas) {
        this.descargas = descargas;
    }

    public Integer getAnioNacimientoAutor() {
        return anioNacimientoAutor;
    }

    public void setAnioNacimientoAutor(Integer anioNacimientoAutor) {
        this.anioNacimientoAutor = anioNacimientoAutor;
    }

    public Integer getAnioMuerteAutor() {
        return anioMuerteAutor;
    }

    public void setAnioMuerteAutor(Integer anioMuerteAutor) {
        this.anioMuerteAutor = anioMuerteAutor;
    }

    @Override
    public String toString() {
        return "LibroDTO{" +
                "titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", idioma='" + idioma + '\'' +
                ", descargas=" + descargas +
                ", anioNacimientoAutor=" + anioNacimientoAutor +
                ", anioMuerteAutor=" + anioMuerteAutor +
                '}';
    }
}