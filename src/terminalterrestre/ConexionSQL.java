/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package terminalterrestre;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author TorresJ2
 */
public class ConexionSQL {
    
    private static final String URL = "jdbc:postgresql://localhost:5432/TerminalTerrestre";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
            
    public static Connection getConnection()
    {
        Connection connection = null;
        try
        {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL,USER,PASSWORD);
        }
        catch(Exception e)
        {     
            JOptionPane.showMessageDialog(null, "Error al crear conexión: " + e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);    
        }
//        catch(Exception e)
//        {     
//            JOptionPane.showMessageDialog(null, "Error al crear conexion","Error", JOptionPane.ERROR_MESSAGE);    
//        }
        return connection;
    }
}
