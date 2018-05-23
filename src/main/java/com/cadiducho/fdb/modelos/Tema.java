package com.cadiducho.fdb.modelos;

import com.cadiducho.fdb.Fundamentos;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public void register() {
        try {
            PreparedStatement ps = Fundamentos.get().mysql.openConnection().prepareStatement("INSERT INTO `Temas` (`id`, `nombre`) VALUES (?, ?);");
            ps.setString(1, id.toString());
            ps.setString(2, nombre);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
