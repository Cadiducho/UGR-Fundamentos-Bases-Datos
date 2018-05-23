package com.cadiducho.fdb.modelos;

import com.cadiducho.fdb.Fundamentos;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public void register() {
        try {
            PreparedStatement ps = Fundamentos.get().mysql.openConnection().prepareStatement("INSERT INTO `Usuarios` (`dni`, `nombre`) VALUES (?, ?);");
            ps.setString(1, dni);
            ps.setString(2, nombre);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
