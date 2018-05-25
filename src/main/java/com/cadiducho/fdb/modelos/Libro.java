package com.cadiducho.fdb.modelos;

import com.cadiducho.fdb.Fundamentos;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

/**
 * Clase que representa un Libro
 */
public class Libro {
    
    /**
     * La ID del Libro
     */
    private final UUID id;
    
    /**
     * El {@link Autor} del libro
     */
    private final Autor autor;
    
    /**
     * El {@link Tema} del libro
     */
    private final Tema tema;
    
    /**
     * El título del libro
     */
    private final String titulo;

    /**
     * Crear un Libro a partir de su autor, temática y título.
     * La ID es generada aleatoriamente
     * @param autor el autor del libro
     * @param tema la temática del libro
     * @param titulo el título del libro
     */
    public Libro(Autor autor, Tema tema, String titulo) {
        this(UUID.randomUUID(), autor, tema, titulo);
    }
    
    /**
     * Cargar un libro con todos sus parámetros
     * @param id la id del libro
     * @param autor el autor del libro
     * @param tema la temática del libro
     * @param titulo el título de libro
     */
    public Libro(UUID id, Autor autor, Tema tema, String titulo) {
        this.id = id;
        this.autor = autor;
        this.tema = tema;
        this.titulo = titulo;
    }

    public UUID getId() {
        return id;
    }

    public Autor getAutor() {
        return autor;
    }

    public Tema getTema() {
        return tema;
    }

    public String getTitulo() {
        return titulo;
    }
    
    public LibroPrestado prestar(Usuario usuario) {
        return new LibroPrestado(this.id, usuario, this.autor, this.tema, this.titulo, new Date());
    }
    
    /**
     * Insertar en la base de datos a este libro
     */
    public void register() {
        try {
            PreparedStatement ps = Fundamentos.get().mysql.openConnection().prepareStatement("INSERT INTO `Libros` (`id`, `Titulo`, `Tema`, `Autor`) VALUES (?, ?, ?, ?);");
            ps.setString(1, id.toString());
            ps.setString(2, titulo);
            ps.setString(3, tema.getId().toString());
            ps.setString(4, autor.getId().toString());
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Libro{" + "id=" + id + ", autor=" + autor + ", tema=" + tema + ", titulo=" + titulo + '}';
    }    
}
