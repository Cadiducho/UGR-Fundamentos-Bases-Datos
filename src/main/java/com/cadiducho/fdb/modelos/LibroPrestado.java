package com.cadiducho.fdb.modelos;

import com.cadiducho.fdb.Fundamentos;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Clase que representa un LibroPrestado
 */
public class LibroPrestado extends Libro {
    
    /**
     * El {@link Usuario} al que fue prestado el libro
     */
    private final Usuario usuario;
    
    /**
     * La fecha y hora en la que el libro fue prestado
     */
    private final Date fechaPrestado;
    
    /**
     * La fecha y hora en la que el libro fue devuelto
     */
    private Date fechaDevuelto;
    
    /**
     * Crear un libro prestado sin devolver
     * @param id la id del libro
     * @param usuario el usuario al que es prestado el libro
     * @param autor el autor del libro
     * @param tema la temática del libro
     * @param titulo el título de libro
     * @param fechaPrestado la fecha en la que fue prestado el libro
     */
    public LibroPrestado(UUID id, Usuario usuario, Autor autor, Tema tema, String titulo, Date fechaPrestado) {
        this(id, usuario, autor, tema, titulo, new Date(), null);
    }
    
    /**
     * Crear un libro prestado ya devuelto
     * @param id la id del libro
     * @param usuario el usuario al que es prestado el libro
     * @param autor el autor del libro
     * @param tema la temática del libro
     * @param titulo el título de libro
     * @param fechaPrestado la fecha en la que fue prestado el libro
     * @param fechaDevuelto la fecha en la que el libro fue devuelto
     */
    public LibroPrestado(UUID id, Usuario usuario, Autor autor, Tema tema, String titulo, Date fechaPrestado, Date fechaDevuelto) {
        super(id, autor, tema, titulo);
        this.usuario = usuario;
        this.fechaPrestado = fechaPrestado;
        this.fechaDevuelto = fechaDevuelto;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public Date getFechaPrestado() {
        return fechaPrestado;
    }

    public Date getFechaDevuelto() {
        return fechaDevuelto;
    }

    @Override
    public String toString() {
        return "LibroPrestado{" + "libro=" + super.toString() + ", usuario=" + usuario + ", fechaPrestado=" + fechaPrestado + ", fechaDevuelto=" + fechaDevuelto + '}';
    }
    
    /**
     * Insertar en la base de datos los parámetros asociados al préstamo del libro
     */
    public void procesarPrestamo() {
        try {
            PreparedStatement ps = Fundamentos.get().mysql.openConnection().prepareStatement("INSERT INTO `LibrosPrestados` (`dni`, `libro`, `fechaPrestado`, `fechaDevuelto`) VALUES (?, ?, ?, ?);");
            ps.setString(1, usuario.getDni());
            ps.setString(2, super.getId().toString());
            ps.setTimestamp(3, new Timestamp(fechaPrestado.getTime()));
            ps.setTimestamp(4, (fechaDevuelto != null ? new Timestamp(fechaDevuelto.getTime()) : null));
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Actualizar los datos del libro prestado, marcándolo como devuelto
     */
    public void devolver() {
        fechaDevuelto = new Date();
        try {
            PreparedStatement ps = Fundamentos.get().mysql.openConnection().prepareStatement("UPDATE `LibrosPrestados` SET `fechaDevuelto`=now() WHERE `dni`=? AND `libro`=?;");
            ps.setString(1, usuario.getDni());
            ps.setString(2, super.getId().toString());
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
