package com.cadiducho.fdb.modelos;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author cadid
 */
public class LibroPrestado extends Libro {
    
    private final Usuario usuario;
    private final boolean devuelto;
    private final Date fechaPrestado;
    private final Date fechaDevuelto;
    
    public LibroPrestado(UUID id, Usuario usuario, Autor autor, Tema tema, String nombre, boolean devuelto, Date fechaPrestado, Date fechaDevuelto) {
        super(id, autor, tema, nombre);
        this.usuario = usuario;
        this.devuelto = devuelto;
        this.fechaPrestado = fechaPrestado;
        this.fechaDevuelto = fechaDevuelto;
    }

    @Override
    public String toString() {
        return "LibroPrestado{" + "libro=" + super.toString() + ", usuario=" + usuario + ", devuelto=" + devuelto + ", fechaPrestado=" + fechaPrestado + ", fechaDevuelto=" + fechaDevuelto + '}';
    }
    
}
