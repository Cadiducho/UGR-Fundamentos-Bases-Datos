package com.cadiducho.fdb.modelos;

import com.cadiducho.fdb.Fundamentos;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 *
 * @author cadid
 */
public class Libro {
    
    private final UUID id;
    private final Autor autor;
    private final Tema tema;
    private final String titulo;

    public Libro(Autor autor, Tema tema, String nombre) {
        this(UUID.randomUUID(), autor, tema, nombre);
    }
    
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

    @Override
    public String toString() {
        return "Libro{" + "id=" + id + ", autor=" + autor + ", tema=" + tema + ", titulo=" + titulo + '}';
    }

    public void register() {
        try {
            PreparedStatement ps = Fundamentos.get().mysql.openConnection().prepareStatement("NSERT INTO `Libros` (`id`, `Titulo`, `Tema`, `Autor`) VALUES (?, ?, ?, ?);");
            ps.setString(1, id.toString());
            ps.setString(2, titulo);
            ps.setString(3, tema.getId().toString());
            ps.setString(4, autor.getId().toString());
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }    
}
