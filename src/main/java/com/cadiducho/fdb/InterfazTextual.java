package com.cadiducho.fdb;

import com.cadiducho.fdb.modelos.Autor;
import com.cadiducho.fdb.modelos.Libro;
import com.cadiducho.fdb.modelos.LibroPrestado;
import com.cadiducho.fdb.modelos.Tema;
import com.cadiducho.fdb.modelos.Usuario;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Clase para leer facilmente de la consola
 */
public class InterfazTextual {
    
    private final Scanner in;
    private final Fundamentos fundamentos;
    
    public InterfazTextual(Fundamentos fundamentos, Scanner in) {
        this.fundamentos = fundamentos;
        this.in = in;
    }
    
    public Tema elegirTema() {
        Map<Integer, String> menuEP = new TreeMap<>();
        int numeroOpcion = 0;
        for (Tema t : fundamentos.temas) {
            menuEP.put(numeroOpcion, t.getNombre());
            numeroOpcion++;
        }
        int salida = seleccionMenu(menuEP); // Método para controlar la elección correcta en el menú 
        return fundamentos.temas.get(salida);
    }
    
    public Autor elegirAutor() {
        Map<Integer, String> menuEP = new TreeMap<>();
        int numeroOpcion = 0;
        for (Autor t : fundamentos.autores) {
            menuEP.put(numeroOpcion, t.getNombre());
            numeroOpcion++;
        }
        int salida = seleccionMenu(menuEP);
        return fundamentos.autores.get(salida);
    }
    
    public Usuario elegirUsuario() {
        Map<Integer, String> menuEP = new TreeMap<>();
        int numeroOpcion = 0;
        for (Usuario t : fundamentos.usuarios) {
            menuEP.put(numeroOpcion, t.getNombre());
            numeroOpcion++;
        }
        int salida = seleccionMenu(menuEP);
        return fundamentos.usuarios.get(salida);
    }
    
    public Libro elegirLibroNoPrestado() {
        Map<Integer, String> menuEP = new TreeMap<>();
        int numeroOpcion = 0;
        for (Libro l : fundamentos.libros) {
            if (!(l instanceof LibroPrestado)) {
                menuEP.put(numeroOpcion, l.getTitulo() + ", por " + l.getAutor().getNombre());
            }
            numeroOpcion++;
        }
        int salida = seleccionMenu(menuEP, numeroOpcion);
        return fundamentos.libros.get(salida);
    }
    
    public Libro elegirLibroPrestado() {
        Map<Integer, String> menuEP = new TreeMap<>();
        int numeroOpcion = 0;
        for (Libro l : fundamentos.libros) {
            if (l instanceof LibroPrestado) {
                LibroPrestado lp = (LibroPrestado) l;
                if (lp.getFechaDevuelto() == null) {
                    menuEP.put(numeroOpcion, lp.getTitulo() + ", por " + lp.getAutor().getNombre() + ", prestado a " + lp.getUsuario().getNombre());
                }
            }
            numeroOpcion++;
        }
        int salida = seleccionMenu(menuEP, numeroOpcion);
        return fundamentos.libros.get(salida);
    }
    
    private int seleccionMenu(Map<Integer, String> menu, int max) {
        boolean valido;
        int numero;
        String lectura;
        do { // Hasta que se hace una selección válida
            for (Map.Entry<Integer, String> fila : menu.entrySet()) {
                numero = fila.getKey();
                String texto = fila.getValue();
                mostrar(numero + " : " + texto);  // número de opción y texto
            }
            mostrar("\nElige una opción: ");
            lectura = in.nextLine();  //lectura de teclado
            valido = comprobarOpcion(lectura, 0, max); //método para comprobar la elección correcta
        } while (!valido);
        return Integer.parseInt(lectura);
    }
    
    private int seleccionMenu(Map<Integer, String> menu) {
        return seleccionMenu(menu, menu.size() - 1);
    }
    
    private boolean comprobarOpcion(String lectura, int min, int max) {
        //método para comprobar que se introduce un entero correcto, usado por seleccion_menu
        boolean valido = true;
        int opcion;
        try {
            opcion = Integer.parseInt(lectura);
            if (opcion < min || opcion > max) { // No es un entero entre los válidos
                mostrar("El numero debe estar entre min y max");
                valido = false;
            }

        } catch (NumberFormatException e) { // No se ha introducido un entero
            mostrar("Debes introducir un número");
            valido = false;
        }
        if (!valido) {
            mostrar("\n Seleccion erronea. Intentalo de nuevo.");
        }
        return valido;
    }
    
    private void mostrar(Object texto) {
        System.out.println(texto);
    }
}
