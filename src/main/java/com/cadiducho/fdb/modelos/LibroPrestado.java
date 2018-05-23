package com.cadiducho.fdb.modelos;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author cadid
 */
public class LibroPrestado extends Libro {
    
    private final Usuario usuario;
    private final Date fechaPrestado;
    private final Date fechaDevuelto;
    
    public LibroPrestado(UUID id, Usuario usuario, Autor autor, Tema tema, String nombre, Date fechaPrestado, Date fechaDevuelto) {
        super(id, autor, tema, nombre);
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
    
}
