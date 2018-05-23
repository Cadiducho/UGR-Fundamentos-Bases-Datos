package com.cadiducho.fdb.modelos;

import com.cadiducho.fdb.Fundamentos;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public String getNombre() {
        return nombre;
    }
    
    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Autor{" + "id=" + id + ", nombre=" + nombre + '}';
    }

    public void register() {
        try {
            PreparedStatement ps = Fundamentos.get().mysql.openConnection().prepareStatement("INSERT INTO `Autores` (`id`, `nombre`) VALUES (?, ?);");
            ps.setString(1, id.toString());
            ps.setString(2, nombre);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }   
}
