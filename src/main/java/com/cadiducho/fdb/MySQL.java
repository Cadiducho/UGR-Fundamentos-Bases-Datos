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
import java.text.SimpleDateFormat;
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
        "  `id` char(36) NOT NULL,\n" +
        "  `nombre` varchar(50) NOT NULL,\n" +
        "  PRIMARY KEY (`id`)\n" +
        ")";
        String createTemas = "CREATE TABLE IF NOT EXISTS `Temas` (\n" +
        "  `id` char(36) NOT NULL,\n" +
        "  `nombre` varchar(36) NOT NULL,\n" +
        "  PRIMARY KEY (`id`),\n" +
        "  UNIQUE KEY `nombre` (`nombre`)\n" +
        ")";
        String createUsuarios = "CREATE TABLE IF NOT EXISTS `Usuarios` (\n" +
        "  `dni` char(9) NOT NULL,\n" +
        "  `nombre` varchar(50) NOT NULL,\n" +
        "  PRIMARY KEY (`dni`)\n" +
        ")";
        String createLibros = "CREATE TABLE IF NOT EXISTS `Libros` (\n" +
        "  `id` char(36) NOT NULL,\n" +
        "  `Titulo` varchar(50) NOT NULL,\n" +
        "  `Tema` char(36) NOT NULL,\n" +
        "  `Autor` char(36) NOT NULL,\n" +
        "  PRIMARY KEY (`id`),\n" +
        "  UNIQUE KEY `Titulo_Autor` (`Titulo`,`Autor`),\n" +
        "  KEY `FK_Libros_Autores` (`Autor`),\n" +
        "  KEY `FK_Libros_Temas` (`Tema`),\n" +
        "  CONSTRAINT `FK_Libros_Autores` FOREIGN KEY (`Autor`) REFERENCES `Autores` (`id`),\n" +
        "  CONSTRAINT `FK_Libros_Temas` FOREIGN KEY (`Tema`) REFERENCES `Temas` (`id`)\n" +
        ");";
        String createLibrosPrestados = "CREATE TABLE IF NOT EXISTS `LibrosPrestados` (\n" +
        "  `dni` char(9) NOT NULL,\n" +
        "  `libro` char(36) NOT NULL,\n" +
        "  `fechaPrestado` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
        "  `fechaDevuelto` timestamp NULL DEFAULT NULL,\n" +
        "  PRIMARY KEY (`dni`,`libro`,`fechaPrestado`),\n" +
        "  KEY `FK_LibrosPrestados_Libros` (`libro`),\n" +
        "  CONSTRAINT `FK_LibrosPrestados_Libros` FOREIGN KEY (`libro`) REFERENCES `Libros` (`id`),\n" +
        "  CONSTRAINT `FK_LibrosPrestados_Usuarios` FOREIGN KEY (`dni`) REFERENCES `Usuarios` (`dni`)\n" +
        ")";
        
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
    
    public void insertarDatosPrueba() {
        try {
            PreparedStatement stmInsertar = openConnection().prepareStatement(""
                    + "INSERT INTO `Temas` (`id`, `nombre`) VALUES "
                    + "	('5a8256b5-5952-471e-9464-4c78b1c09acc', 'Fantasía');");
            stmInsertar.addBatch();
            stmInsertar.addBatch("INSERT INTO `Usuarios` (`dni`, `nombre`) VALUES "
                    + "	('32426456G', 'Ana'),"
                    + "	('88da8433P', 'Dani');");
            stmInsertar.addBatch("INSERT INTO `Autores` (`id`, `nombre`) VALUES"
                    + "	('4b8abfb4-ed25-4dbd-8fd5-8bb04511a814', 'Laura Gallego'),"
                    + "	('60de96dd-ac7b-47f3-a99c-3e4481c33e22', 'J. K Rowling');");
            stmInsertar.addBatch("INSERT INTO `Libros` (`id`, `Titulo`, `Tema`, `Autor`) VALUES"
                    + "	('0e4f7729-b28c-41fd-8001-4189c18cc132', 'Harry Potter y la piedra filosofal', '5a8256b5-5952-471e-9464-4c78b1c09acc', '60de96dd-ac7b-47f3-a99c-3e4481c33e22'),"
                    + "	('c6492bbf-8ae6-4453-8e6c-6d0ef4edab5b', 'Harry Potter y el prisionero de Azkaban', '5a8256b5-5952-471e-9464-4c78b1c09acc', '60de96dd-ac7b-47f3-a99c-3e4481c33e22'),"
                    + "	('dc797a0d-b225-484e-b669-7a467e38efad', 'Memorias de Idhún', '5a8256b5-5952-471e-9464-4c78b1c09acc', '4b8abfb4-ed25-4dbd-8fd5-8bb04511a814'),"
                    + "	('eedd0b1d-4e4a-45dc-bc47-49a2a8481673', 'Harry Potter y la cámara secreta', '5a8256b5-5952-471e-9464-4c78b1c09acc', '60de96dd-ac7b-47f3-a99c-3e4481c33e22');");
            stmInsertar.addBatch("INSERT INTO `LibrosPrestados` (`dni`, `libro`, `fechaPrestado`, `fechaDevuelto`) VALUES"
                    + "	('32426456G', 'dc797a0d-b225-484e-b669-7a467e38efad', '2018-05-22 18:46:09', NULL),"
                    + "	('88da8433-', 'c6492bbf-8ae6-4453-8e6c-6d0ef4edab5b', '2018-05-22 17:06:33', NULL),"
                    + "	('88da8433-', 'eedd0b1d-4e4a-45dc-bc47-49a2a8481673', '2018-05-22 16:27:18', '2018-05-22 17:06:36');");
            stmInsertar.executeBatch();
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
            String prestadosQuery = "SELECT lp.dni, lp.fechaPrestado, lp.fechaDevuelto, l.id, l.Titulo, l.Tema, l.Autor "
                    + "FROM LibrosPrestados lp, Libros l WHERE lp.libro=l.id;";
            PreparedStatement stmLibrosPrestados = openConnection().prepareStatement(prestadosQuery);
            ResultSet rsLibrosPrestados = stmLibrosPrestados.executeQuery();
            while (rsLibrosPrestados.next()) {
                UUID id = UUID.fromString(rsLibrosPrestados.getString("id"));
                String titulo = rsLibrosPrestados.getString("titulo");
                Date fechaPrestado = rsLibrosPrestados.getDate("fechaPrestado");
                Date fechaDevuelto = rsLibrosPrestados.getDate("fechaDevuelto");
                String dniUsuario = rsLibrosPrestados.getString("dni");
                UUID idTema = UUID.fromString(rsLibrosPrestados.getString("tema"));
                UUID idAutor = UUID.fromString(rsLibrosPrestados.getString("autor"));
                LibroPrestado libroPrestado = new LibroPrestado(id, fundamentos.usuarioFromDni(dniUsuario), fundamentos.autorFromId(idAutor), fundamentos.temaFromId(idTema), titulo, fechaPrestado, fechaDevuelto);
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
    
    public void mostrarHistorial() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    
            PreparedStatement stmMostrarNoDevueltos = openConnection().prepareStatement("SELECT lp.fechaPrestado, l.Titulo, (SELECT t.nombre FROM Temas t WHERE t.id = l.Tema) Tema, (SELECT aut.nombre FROM Autores aut WHERE aut.id = l.Autor) Autor, (SELECT u.nombre FROM Usuarios u WHERE lp.dni=u.dni) Usuario FROM LibrosPrestados lp, Libros l WHERE lp.libro=l.id AND lp.fechaDevuelto IS NULL;");
            ResultSet rsNoDevueltos = stmMostrarNoDevueltos.executeQuery();
            System.out.println("=== Libros NO devueltos ===");
            while (rsNoDevueltos.next()) {
                String titulo = rsNoDevueltos.getString("titulo");
                String autor = rsNoDevueltos.getString("autor");
                String tema = rsNoDevueltos.getString("tema");
                String usuario = rsNoDevueltos.getString("usuario");
                Date fecha = new Date(rsNoDevueltos.getTimestamp("fechaPrestado").getTime());
                System.out.println("Titulo: " + titulo+ "(" + tema + "), por " + autor + ". Prestado a " + usuario + " el " + sdf.format(fecha));
            }
            System.out.println("******");
            PreparedStatement stmMostrarDevueltos = openConnection().prepareStatement("SELECT lp.fechaPrestado, lp.fechaDevuelto, l.Titulo, (SELECT t.nombre FROM Temas t WHERE t.id = l.Tema) Tema, (SELECT aut.nombre FROM Autores aut WHERE aut.id = l.Autor) Autor, (SELECT u.nombre FROM Usuarios u WHERE lp.dni=u.dni) Usuario FROM LibrosPrestados lp, Libros l WHERE lp.libro=l.id AND lp.fechaDevuelto IS NOT NULL;");
            ResultSet rsDevueltos = stmMostrarDevueltos.executeQuery();
            System.out.println("=== Libros SÍ devueltos ===");
            while (rsDevueltos.next()) {
                String titulo = rsDevueltos.getString("titulo");
                String autor = rsDevueltos.getString("autor");
                String tema = rsDevueltos.getString("tema");
                String usuario = rsDevueltos.getString("usuario");
                Date fecha = new Date(rsDevueltos.getTimestamp("fechaPrestado").getTime());
                Date fechaDevuelto = new Date(rsDevueltos.getTimestamp("fechaDevuelto").getTime());
                System.out.println("Titulo: " + titulo+ "(" + tema + "), por " + autor + ". Prestado a " + usuario + " el " + sdf.format(fecha) + " y devuelto el " + sdf.format(fechaDevuelto));
            }
            System.out.println("******");
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}