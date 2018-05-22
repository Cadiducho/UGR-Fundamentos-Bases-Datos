package com.cadiducho.fdb.modelos;

import java.util.UUID;

/**
 *
 * @author cadid
 */
public class Libro {

    private final UUID id;
    private final Autor autor;
    private final Tema tema;
    private final String nombre;

    public Libro(UUID id, Autor autor, Tema tema, String nombre) {
        this.id = id;
        this.autor = autor;
        this.tema = tema;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Libro{" + "id=" + id + ", autor=" + autor + ", tema=" + tema + ", nombre=" + nombre + '}';
    }


}
