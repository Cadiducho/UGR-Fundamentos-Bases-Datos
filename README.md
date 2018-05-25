Gestor de Préstamo de Libros
============

## Conceptos iniciales
Para ejecutar la conexión entre Java y MySQL, se utilizará el Driver JDBC y las librerías proporcionadas en `java.sql`.

La conexión al driver se realizará mediente la utilidad `DriverManager.getConnection(url, user, password)`. Esta url debe contener todos los datos de dónde se encuentra la base de datos, cómo conectarte a ella y una serie de parámetros opcionales
```
connection = DriverManager.getConnection("jdbc:mysql://"
                + hostname + ":" + port + "/" + database +
                "?serverTimezone=Europe/Madrid", user, password);
```
Una clase es la encargada de realizar las conexiones a la base de datos, y mantiene esta almacenada en una variable del tipo `Connection`.
A partir del objeto `Connection`, se pueden crear objetos `Statement`, que gestionarán la consulta o query, enviándola a la base de datos y procesando el resultado de dicha consulta. En este ejercicio se ha utilizado el objeto `PreparedStatement`, un tipo de `Statement` más eficiente para múltiples consultas y que permite la insercción de parámetros en las consultas de manera segura, sin posibles inyecciones.
```
PreparedStatement ps = connection.prepareStatement("INSERT INTO `Usuarios` (`dni`, `nombre`) VALUES (?, ?);");
```

Las interrogaciones son sustituibles de una manera sencilla y segura:

```
ps.setString(1, dni);
ps.setString(2, nombre);
```

Por último, se ejecuta la update

```
ps.executeUpdate();
```
Este método devuelve un `int` que almacena el número de filas de la tabla que han sido modificadas. `executeUpdate()` es el método utilizado para consultas del tipo `INSERT`, `DELETE`, `DROP` o `UPDATE`, que producen alteraciones en los datos guardados.

Para consultas de datos que no alteran los datos, se utilizará el método `executeQuery()`:
```
PreparedStatement ps = connection.prepareStatement("SELECT `nombre` FROM Usuarios;");
ResultSet rs = ps.executeQuery();
```
En este caso, el tipo retornado es un objeto `ResultSet`, que almacena la tabla con los resultados de la columna. Dicho objeto mantiene un cursor apuntando a la primera fila de la tabla, y este cursor se desplaza por el resto de filas (si es que estas existen). El `ResultSet` nos permite navegar fácilmente por los datos obtenidos en la consulta mediante un método `next()`, que devuelve verdadero si hay una fila más de datos y avanza el cursor, y una sucesión de métodos 'get' para transformar los datos de la tabla al tipo deseado de Java, tales como `getString(nombreColumna)`, `getInt(...)` y demás
```
System.out.println("Lista de nombres: ");
while (rs.next()) {
  String nombre = rs.getString("nombre");
  System.out.println("--> " + nombre);
}
```

#### Excepciones a tener en cuenta
Todas las operaciones relaccionadas a crear `Statement`, `PreparedStatement`, `ResultSet` y demás objetos del paquete `java.sql` pueden lanzar excepciones del tipo `SQLException`. En esta excepción se controlarán errores tales como que la conexión a la base de datos se haya cortado, que la consulta ejecutada tenga errores sintácticos o que la operación no esté permitida, como intentar borrar datos de los que dependen llaves foráneas o que el usuario no tenga permisos.

Además, la obtención del driver mediante `Class.forName()` puede lanzar `ClassNotFoundException`.

## Descripción del ejercicio
El ejercicio desarrollado es un simple gestor de préstamo de libros. Este permite registrar usuarios, temas, autores y libros que luego pueden ser prestados y mantienen un historial en la base de datos.
Se han implementado las siguientes clases para manejar los datos:
```
class Usuario {
  String dni;
  String nombre;
}
```
```
class Tema {
  UUID id;
  String nombre;
}
```
```
class Autor {
  UUID id;
  String nombre;
}
```
```
class Libro {
  UUID id;
  Autor autor;
  Tema tema;
  String titulo;
}
```
```
class LibroPrestado extends Libro {
  Usuario usuario;
  Date fechaPrestado;
  Date fechaDevuelto;
}
```
![](https://i.imgur.com/a7HajvV.png)
Las ID de `Tema`, `Autor` y `Libro` se implementan mediante `UUID`, asegurándonos de que se crean strings de 36 caracteres únicas.

El programa implementa las siguientes acciones:
1. Crear tablas en la DB
2. Insertar datos de prueba
3. Cargar datos desde la DB
4. Mostrar historial de libros
5. Insertar nuevo Usuario
6. Insertar nuevo Tema
7. Insertar nuevo Autor
8. Insertar nuevo Libro
9. Prestar Libro
10. Devolver Libro
11. Desconectarte de la DB

##### Crear tablas
Ejecuta la consulta necesaria para eliminar las tablas existentes y volverlas a crear de nuevo.

![](https://i.imgur.com/WXTaDyU.png)
##### Insertar datos
Ejecuta una consulta insertando una serie de libros, autores y temas de prueba.

##### Cargar datos
Los datos se obtienen de la DB y se montan en los objetos adecuados en Java, enlazándo estos y almacenándolos en unos `ArrayList`.
> SELECT * FROM Usuarios;

> SELECT * FROM Temas;

> SELECT * FROM Autores;

> SELECT lp.dni, lp.fechaPrestado, lp.fechaDevuelto, l.id, l.Titulo, l.Tema, l.Autor FROM LibrosPrestados lp, Libros l WHERE lp.libro=l.id;

> SELECT l.id, l.Titulo, l.Tema, l.Autor FROM Libros l LEFT JOIN LibrosPrestados lp ON lp.libro = l.id WHERE lp.libro IS NULL;

##### Mostrar el Historial de Libros
Solicita a la base de datos dos listas, una de libros prestados y ya devueltos y otra de libros prestados pero aún no devueltos.
> SELECT lp.fechaPrestado, l.Titulo, (SELECT t.nombre FROM Temas t WHERE t.id = l.Tema) Tema, (SELECT aut.nombre FROM Autores aut WHERE aut.id = l.Autor) Autor, (SELECT u.nombre FROM Usuarios u WHERE lp.dni=u.dni) Usuario FROM LibrosPrestados lp, Libros l WHERE lp.libro=l.id AND lp.fechaDevuelto IS NULL;

> SELECT lp.fechaPrestado, lp.fechaDevuelto, l.Titulo, (SELECT t.nombre FROM Temas t WHERE t.id = l.Tema) Tema, (SELECT aut.nombre FROM Autores aut WHERE aut.id = l.Autor) Autor, (SELECT u.nombre FROM Usuarios u WHERE lp.dni=u.dni) Usuario FROM LibrosPrestados lp, Libros l WHERE lp.libro=l.id AND lp.fechaDevuelto IS NOT NULL;

##### Funciones de insercción
Crea el objeto en Java, lo añade al correspondiente `ArrayList` e inserta esos datos en la DB.
De ser necesario enlaces entre objetos, como en `Libro`, el sistema solicitará que se escoja dicho segundo objeto.
> INSERT INTO 'Autores' ('id', 'nombre') VALUES (?, ?);

> INSERT INTO 'Temas' ('id', 'nombre') VALUES (?, ?);

> INSERT INTO 'Usuarios' ('dni', 'nombre') VALUES (?, ?);

> INSERT INTO 'Libros' ('id', 'nombre', 'Tema', 'Autor') VALUES (?, ?);

##### Prestar libro
Elimina el objeto `Libro` de su lista y crea un objeto `LibroPrestado` que es añadido acto seguido con sus parámetros correctos. En la base de datos, se registra el préstamo del libro.
> INSERT INTO 'LibrosPrestados' ('dni', 'Libro', 'fechaPrestado', 'fechaDevuelto') VALUES (?, ?, ?, ?);

##### Devolver libro
Se establece una fecha de devolución al objeto `LibroPrestado`, marcándolo así como libro devuelto. En la base de datos, se actualizan sus datos añadiendo dicha fecha.
> UPDATE 'LibrosPrestados' SET 'fechaDevuelto'=now() WHERE 'dni'=? AND 'libro'=?;

## Datos de implementación
El ejercicio ha sido desarrollado en Java 8, con el Driver 6.0.6 mediante la clase `com.mysql.cj.jdbc.Driver` y usado Maven como gestor de dependencias.
La base de datos utilizada fue MariaDB 10.1.32 de forma remota.
