package com.cadiducho.fdb.modelos;

/**
 * Clase de Usuario
 */
public class Usuario {
    
    private final String dni;
    private final String nombre;
    
    /**
     * Cargar usuario con su dni y nombre
     * @param dni su ID
     * @param nombre su Nombre
     */
    public Usuario(String dni, String nombre) {
        this.dni = dni;
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Usuario{" + "dni=" + dni + ", nombre=" + nombre + '}';
    }
    
}
