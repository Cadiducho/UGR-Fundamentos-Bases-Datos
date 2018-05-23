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
    
    public MySQL mysql = null;
    private final Scanner in;
    private Connection conn;
    
    public final ArrayList<Usuario> usuarios;
    public final ArrayList<Tema> temas;
    public final ArrayList<Autor> autores;
    public final ArrayList<Libro> libros;
    
    private final InterfazTextual interfaz;
    
    private static Fundamentos instance;
    public static Fundamentos get() {
        return instance;
    }
    
    public Fundamentos() {
        in = new Scanner(System.in);
        usuarios = new ArrayList<>();
        temas = new ArrayList<>();
        autores = new ArrayList<>();
        libros = new ArrayList<>();
        interfaz = new InterfazTextual(this, in);
        instance = this;
    }
    
    public void run() throws SQLException, ClassNotFoundException {
        boolean running = true;
       
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
            showMenu();
       
            int option;
            try {
                option = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                // pasar al siguiente running y mostrar menu
                option = -1;
            }
            switch (option) {
                case 0:
                    mysql.closeConnection();
                    System.out.println("Conexión terminada");
                    running = false;
                case 1:
                    mysql.createTables();
                    break;
                case 2:
                    mysql.insertarDatosPrueba();
                    break;
                case 3:
                    mysql.loadData(usuarios, libros, temas, autores);
                    libros.forEach(System.out::println);
                    break;
                case 4:
                    mysql.mostrarHistorial();
                    break;
                case 5:
                    insertarUsuario();
                    break;
                case 6:
                    insertarTema();
                    break;
                case 7:
                    insertarAutor();
                    break;
                case 8:
                    insertarLibro();
                    break;
                case 9:
                    prestarLibro();
                    break;
                case 10:
                    devolverLibro();
                    break;
            }
        }
        System.out.println("Ejecucion terminada");
    }
    
    private void showMenu() {
        System.out.println("Opciones: ");
        System.out.println("1. Crear tablas");
        System.out.println("2. Insertar datos de prueba");
        System.out.println("3. Cargar datos");
        System.out.println("4. Mostrar historial de libros");

        System.out.println("5. Insertar nuevo Usuario");
        System.out.println("6. Insertar nuevo Tema");
        System.out.println("7. Insertar nuevo Autor");
        System.out.println("8. Insertar nuevo Libro");

        System.out.println("9. Prestar Libro");
        System.out.println("10. Devolver Libro");

        System.out.println("0. Desconectate");
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

    private void insertarUsuario() {
        System.out.println("Inserta nombre: ");
        String nombre = in.nextLine();
        System.out.println("Inserta dni: ");
        String dni = in.nextLine();
        Usuario usuario = new Usuario(dni, nombre);
        usuarios.add(usuario);
        usuario.register();
        usuarios.forEach(System.out::println);
    }

    private void insertarTema() {
        System.out.println("Inserta tema: ");
        String nombre = in.nextLine();
        Tema tema = new Tema(nombre);
        temas.add(tema);
        tema.register();
        temas.forEach(System.out::println);
    }

    private void insertarAutor() {
        System.out.println("Inserta autor: ");
        String nombre = in.nextLine();
        Autor autor = new Autor(nombre);
        autores.add(autor);
        autor.register();
        autores.forEach(System.out::println);
    }

    private void insertarLibro() {
        System.out.println("Inserta nombre: ");
        String nombre = in.nextLine();
        System.out.println("Selecciona un tema: ");
        Tema tema = interfaz.elegirTema();
        System.out.println("Selecciona un autor: ");
        Autor autor = interfaz.elegirAutor();
        Libro libro = new Libro(autor, tema, nombre);
        libros.add(libro);
        libro.register();
        libros.forEach(System.out::println);
    }

    private void prestarLibro() {
        Libro libro = interfaz.elegirLibroNoPrestado();
    }

    private void devolverLibro() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
