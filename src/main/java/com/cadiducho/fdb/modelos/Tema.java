package com.cadiducho.fdb.modelos;

import java.util.UUID;

/**
 * Clase de Tema
 */
public class Tema {
    
    private final UUID id;
    private final String nombre;
    
    /**
     * Crear un tema
     * @param nombre nombre del tema
     */
    public Tema(String nombre) {
        this(UUID.randomUUID(), nombre);
    }
    
    /**
     * Cargar tema con su id y nombre
     * @param id su ID
     * @param nombre su Nombre
     */
    public Tema(UUID id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Tema{" + "id=" + id + ", nombre=" + nombre + '}';
    }
    
    
    
}
