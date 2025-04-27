//Cette classe permet d'instancier puis d'initialiser une connection a notre database MySQL
package com.quiz.utils;

import java.sql.Connection;//Pour obtenir la connection a la DB
import java.sql.DriverManager;//Pouvoir envoyer des SQL queries a la DB
import java.sql.SQLException;//Gerer les SQLExceptions

public class DatabaseUtil {
    private static final String URL="jdbc:mysql://localhost:3306/quiz_game";
    private static final String USER="root";
    private static final String PASSWORD="";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
}
