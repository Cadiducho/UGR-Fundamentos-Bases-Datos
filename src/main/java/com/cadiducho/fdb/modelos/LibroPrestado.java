package com.cadiducho.fdb.modelos;

import com.cadiducho.fdb.Fundamentos;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author cadid
 */
public class LibroPrestado extends Libro {
    
    private final Usuario usuario;
    private final Date fechaPrestado;
    private Date fechaDevuelto;
    
    public LibroPrestado(UUID id, Usuario usuario, Autor autor, Tema tema, String titulo, Date fechaPrestado) {
        this(id, usuario, autor, tema, titulo, new Date(), null);
    }
    
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
