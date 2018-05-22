package com.cadiducho.fdb;

import com.cadiducho.fdb.modelos.Autor;
import com.cadiducho.fdb.modelos.Libro;
import com.cadiducho.fdb.modelos.LibroPrestado;
import com.cadiducho.fdb.modelos.Tema;
import com.cadiducho.fdb.modelos.Usuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class MySQL {

    protected Connection connection;
    private final Fundamentos fundamentos;
    private final String user, database, password, port, hostname;

    public MySQL(Fundamentos fundamentos, String hostname, String port, String database, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.user = username;
        this.password = password;
        this.fundamentos = fundamentos;
    }

    public boolean checkConnection() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean closeConnection() throws SQLException {
        if (connection == null) {
            return false;
        }
        connection.close();
        return true;
    }

    public Connection openConnection() throws SQLException, ClassNotFoundException {
        if (checkConnection()) {
            return connection;
        }

        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"
                + this.hostname + ":" + this.port + "/" + this.database + "?autoReconnect=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Madrid", this.user, this.password);
        return connection;
    }
    
    public void createTables() {
        String createAutores = "CREATE TABLE IF NOT EXISTS `Autores` (\n" +
        "	`id` CHAR(36) NOT NULL,\n" +
        "	`nombre` VARCHAR(50) NOT NULL,\n" +
        "	PRIMARY KEY (`id`)\n" +
        ");";
        String createTemas = "CREATE TABLE IF NOT EXISTS `Temas` (\n" +
        "	`id` CHAR(36) NOT NULL,\n" +
        "	`nombre` VARCHAR(36) NOT NULL,\n" +
        "	PRIMARY KEY (`id`),\n" +
        "	UNIQUE INDEX `nombre` (`nombre`)\n" +
        ");";
        String createUsuarios = "CREATE TABLE IF NOT EXISTS `Usuarios` (\n" +
        "	`dni` CHAR(9) NOT NULL,\n" +
        "	`nombre` VARCHAR(50) NOT NULL,\n" +
        "	PRIMARY KEY (`dni`)\n" +
        ");";
        String createLibros = "CREATE TABLE IF NOT EXISTS `Libros` (\n" +
        "	`id` CHAR(36) NOT NULL,\n" +
        "	`Titulo` VARCHAR(50) NOT NULL,\n" +
        "	`Tema` CHAR(36) NOT NULL,\n" +
        "	`Autor` CHAR(36) NOT NULL,\n" +
        "	PRIMARY KEY (`id`),\n" +
        "	UNIQUE INDEX `Titulo_Autor` (`Titulo`, `Autor`),\n" +
        "	INDEX `FK_Libros_Autores` (`Autor`),\n" +
        "	INDEX `FK_Libros_Temas` (`Tema`),\n" +
        "	CONSTRAINT `FK_Libros_Autores` FOREIGN KEY (`Autor`) REFERENCES `Autores` (`id`),\n" +
        "	CONSTRAINT `FK_Libros_Temas` FOREIGN KEY (`Tema`) REFERENCES `Temas` (`id`)\n" +
        ");";
        String createLibrosPrestados = "CREATE TABLE `LibrosPrestados` (\n" +
        "	`dni` CHAR(9) NOT NULL,\n" +
        "	`libro` CHAR(36) NOT NULL,\n" +
        "	`devuelto` TINYINT(1) NOT NULL,\n" +
        "	`fechaPrestado` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
        "	`fechaDevuelto` TIMESTAMP NULL DEFAULT NULL,\n" +
        "	PRIMARY KEY (`dni`, `libro`, `fechaPrestado`),\n" +
        "	INDEX `FK_LibrosPrestados_Libros` (`libro`),\n" +
        "	CONSTRAINT `FK_LibrosPrestados_Libros` FOREIGN KEY (`libro`) REFERENCES `Libros` (`id`),\n" +
        "	CONSTRAINT `FK_LibrosPrestados_Usuarios` FOREIGN KEY (`dni`) REFERENCES `Usuarios` (`dni`)\n" +
        ");";
        
        try {
            PreparedStatement stmAutores = openConnection().prepareStatement(createAutores);
            stmAutores.executeUpdate();
            
            PreparedStatement stmTemas = openConnection().prepareStatement(createTemas);
            stmTemas.executeUpdate();
            
            PreparedStatement stmUsuarios = openConnection().prepareStatement(createUsuarios);
            stmUsuarios.executeUpdate();
            
            PreparedStatement stmLibros = openConnection().prepareStatement(createLibros);
            stmLibros.executeUpdate();
            
            PreparedStatement stmPrestados = openConnection().prepareStatement(createLibrosPrestados);
            stmPrestados.executeUpdate();
            
            System.out.println("Tablas creadas correctamente");
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void loadData(ArrayList<Usuario> usuarios, ArrayList<Libro> libros, ArrayList<Tema> temas, ArrayList<Autor> autores) {
        try {
            
            //Cargar Usuarios
            PreparedStatement stmUsuarios = openConnection().prepareStatement("SELECT * FROM Usuarios;");
            ResultSet rsUsuarios = stmUsuarios.executeQuery();
            while (rsUsuarios.next()) {
                String dni = rsUsuarios.getString("dni");
                String nombre = rsUsuarios.getString("nombre");
                Usuario usuario = new Usuario(dni, nombre);
                usuarios.add(usuario);
            }
            
            //Cargar Temas
            PreparedStatement stmTemas = openConnection().prepareStatement("SELECT * FROM Temas;");
            ResultSet rsTemas = stmTemas.executeQuery();
            while (rsTemas.next()) {
                UUID id = UUID.fromString(rsTemas.getString("id"));
                String nombre = rsTemas.getString("nombre");
                Tema tema = new Tema(id, nombre);
                temas.add(tema);
            }
            
            //Cargar Autores
            PreparedStatement stmAutores = openConnection().prepareStatement("SELECT * FROM Autores;");
            ResultSet rsAutores = stmAutores.executeQuery();
            while (rsAutores.next()) {
                UUID id = UUID.fromString(rsAutores.getString("id"));
                String nombre = rsAutores.getString("nombre");
                Autor autor = new Autor(id, nombre);
                autores.add(autor);
            }
            
            //Cargar libros prestados (y su historial)
            PreparedStatement stmLibrosPrestados = openConnection().prepareStatement("SELECT lp.dni, lp.devuelto, lp.fechaPrestado, lp.fechaDevuelto, l.id, l.Titulo, l.Tema, l.Autor FROM LibrosPrestados lp, Libros l WHERE lp.libro=l.id;");
            ResultSet rsLibrosPrestados = stmLibrosPrestados.executeQuery();
            while (rsLibrosPrestados.next()) {
                UUID id = UUID.fromString(rsLibrosPrestados.getString("id"));
                String titulo = rsLibrosPrestados.getString("titulo");
                boolean devuelto = rsLibrosPrestados.getBoolean("devuelto");
                Date fechaPrestado = rsLibrosPrestados.getDate("fechaPrestado");
                Date fechaDevuelto = rsLibrosPrestados.getDate("fechaDevuelto");
                String dniUsuario = rsLibrosPrestados.getString("dni");
                UUID idTema = UUID.fromString(rsLibrosPrestados.getString("tema"));
                UUID idAutor = UUID.fromString(rsLibrosPrestados.getString("autor"));
                LibroPrestado libroPrestado = new LibroPrestado(id, fundamentos.usuarioFromDni(dniUsuario), fundamentos.autorFromId(idAutor), fundamentos.temaFromId(idTema), titulo, devuelto, fechaPrestado, fechaDevuelto);
                libros.add(libroPrestado);
            }
            
            PreparedStatement stmLibros = openConnection().prepareStatement("SELECT l.id, l.Titulo, l.Tema, l.Autor FROM Libros l LEFT JOIN LibrosPrestados lp ON lp.libro = l.id WHERE lp.libro IS NULL;");
            ResultSet rsLibros = stmLibros.executeQuery();
            while (rsLibros.next()) {
                UUID id = UUID.fromString(rsLibros.getString("id"));
                String titulo = rsLibros.getString("titulo");
                UUID idTema = UUID.fromString(rsLibros.getString("tema"));
                UUID idAutor = UUID.fromString(rsLibros.getString("autor"));
                Libro libro = new Libro(id, fundamentos.autorFromId(idAutor), fundamentos.temaFromId(idTema), titulo);
                libros.add(libro);
            }
            //Cargar el resto de libros
            
            
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}