package com.cadiducho.fdb;

import com.cadiducho.fdb.modelos.Autor;
import com.cadiducho.fdb.modelos.Libro;
import com.cadiducho.fdb.modelos.Tema;
import com.cadiducho.fdb.modelos.Usuario;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class Fundamentos {
    
    private MySQL mysql = null;
    private final Scanner in;
    private Connection conn;
    
    private final ArrayList<Usuario> usuarios;
    private final ArrayList<Tema> temas;
    private final ArrayList<Autor> autores;
    private final ArrayList<Libro> libros;
    
    public Fundamentos() {
        in = new Scanner(System.in);
        usuarios = new ArrayList<>();
        temas = new ArrayList<>();
        autores = new ArrayList<>();
        libros = new ArrayList<>();
    }
    
    public void run() throws SQLException, ClassNotFoundException {
        boolean running = true;
       /*
        System.out.println("Conéctate a una base de datos");
        System.out.println("Hostame/IP: (Sugerencia: cadiducho.com");
        String hostname = in.nextLine();
        System.out.println("Puerto: (Sugerencia: 3306)");
        String puerto = in.nextLine();
        System.out.println("Database name: (Sugerencia: fdb)");
        String dbname = in.nextLine();
        System.out.println("Username: (Sugerencia: fundamentos)");
        String user = in.nextLine();
        System.out.println("Contraseña: (Sugerencia: password)");
        String pass = in.nextLine();*/
       
        String hostname = "cadiducho.com";
        String puerto = "3306";
        String dbname = "fdb";
        String user = "fundamentos";
        String pass = "password";
        
        mysql = new MySQL(this, hostname, puerto, dbname, user, pass);
        conn = mysql.openConnection();

        if (conn.isClosed()) {
            System.out.println("No se ha podido conectar a la db");
            running = false;
        }
        System.out.println("Conexión establecida");
        
        while (running) {

            
            // Menu
            System.out.println("Opciones: ");
            System.out.println("1. Crea una tabla");
            System.out.println("2. Cargar datos");
            
            System.out.println("0. Desconectate");
            switch (in.nextInt()) {
                case 0:
                    mysql.closeConnection();
                    System.out.println("Conexión terminada");
                    running = false;
                case 1:
                    mysql.createTables();
                    break;
                case 2:
                    mysql.loadData(usuarios, libros, temas, autores);
                    libros.forEach(System.out::println);
                    break;
            }
        }
        System.out.println("Ejecucion terminada");
    }
    
    public Autor autorFromId(UUID id) {
        return autores.stream().filter(aut -> aut.getId().equals(id)).findAny().orElse(null);
    }
    
    public Tema temaFromId(UUID id) {
        return temas.stream().filter(tema -> tema.getId().equals(id)).findAny().orElse(null);
    }
    
    public Usuario usuarioFromDni(String dni) {
        return usuarios.stream().filter(user -> user.getDni().equalsIgnoreCase(dni)).findAny().orElse(null);
    }
}
