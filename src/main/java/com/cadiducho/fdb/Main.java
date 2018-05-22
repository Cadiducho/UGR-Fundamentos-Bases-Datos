package com.cadiducho.fdb;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
            Fundamentos fundamentos = new Fundamentos();
            fundamentos.run();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
}
