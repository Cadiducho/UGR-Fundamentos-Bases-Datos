package com.cadiducho.fdb.modelos;

import java.util.UUID;

/**
 * Clase de Autor
 */
public class Autor {
    
    private final UUID id;
    private final String nombre;
    
    /**
     * Crear un autor a partir de su nombre
     * @param nombre nombre del autor
     */
    public Autor(String nombre) {
        this(UUID.randomUUID(), nombre);
    }
    
    /**
     * Cargar autor con su id y nombre
     * @param id su ID
     * @param nombre su Nombre
     */
    public Autor(UUID id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Autor{" + "id=" + id + ", nombre=" + nombre + '}';
    }
    
    
}
