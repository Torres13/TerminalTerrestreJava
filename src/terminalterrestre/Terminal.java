/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package terminalterrestre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import clasesAuxiliares.CiudadAux;
import clasesAuxiliares.EstacionAux;
import clasesAuxiliares.TerminalAux;
import terminalterrestre.Estacion.IIdentificable;

public class Terminal extends javax.swing.JFrame {

    Connection _conexion;
    long _idEstacion;
    long _idCiudad;
    long _idTerminal;
    String _nomTerminal;

    List<CiudadAux> _ciudades;  
    List<EstacionAux> _estaciones; 
    List<TerminalAux> _terminales;
    DefaultTableModel tablaTer;
    long _index;
    
    public Terminal() {
        _ciudades = new ArrayList<CiudadAux>();
        _estaciones = new ArrayList<EstacionAux>();
        _terminales = new ArrayList<TerminalAux>();

        initComponents();  
        tablaTer = (DefaultTableModel) tablaTerminal.getModel();
        llenaCiudades();
        llenaEstaciones();
        consultaDatos();
    }

    private void llenaCiudades() 
    {
        _ciudades.clear();
        
        String sentenciaSQL = "SELECT * FROM Destinos.Ciudad";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                var nuevaCiudad = new CiudadAux();
                nuevaCiudad.setIdCiudad(result.getInt("IdCiudad"));
                nuevaCiudad.setNomCiudad(result.getString("NonCiudad"));
                _ciudades.add(nuevaCiudad); 
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar las Ciudades","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }

    private void llenaEstaciones()
    {
        _estaciones.clear();    
        String sentenciaSQL = "SELECT * FROM Destinos.Estacion";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                var nuevaEstacion = new EstacionAux();                
                nuevaEstacion.setIdEstacion(result.getInt("IdEstacion"));
                nuevaEstacion.setIdCiudad(result.getInt("IdCiudad"));
                nuevaEstacion.setNomEstacion(result.getString("NomEstacion"));
             
                _estaciones.add(nuevaEstacion); 
            }
            concatenaEstaciones();

        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar las Estaciones","Error", JOptionPane.ERROR_MESSAGE);            
        }  
    }
    
    private void concatenaEstaciones()
    {
        cbEstacion.removeAllItems();
        cbEstacion.addItem("");
        for (EstacionAux estacion : _estaciones) 
        {
            for (CiudadAux ciudad : _ciudades) 
            {
                if (estacion.getIdCiudad() == ciudad.getIdCiudad())
                {
                    estacion.setNomCiudad(ciudad.getNomCiudad());
                    estacion.setNomConcatenado(estacion.getNomEstacion()+ " - " + ciudad.getNomCiudad());
                    cbEstacion.addItem(estacion.getNomConcatenado());
                    break;
                }             
            }            
        }
    }
   
    private void consultaDatos()
    {
        tablaTer.setRowCount(0);
        _terminales.clear();
        
        String sentenciaSQL = "SELECT * FROM Destinos.Terminal";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                var nuevaTerminal = new TerminalAux();                
                nuevaTerminal.setIdTerminal(result.getInt("IdTerminal"));
                nuevaTerminal.setNomTerminal(result.getString("NomTerminal"));
                nuevaTerminal.setIdEstacion(result.getInt("IdEstacion"));

                tablaTer.addRow(
                        new Object[]
                        {
                            result.getInt("IdTerminal"),
                            result.getString("NomTerminal"),                           
                            result.getInt("IdEstacion"),
                        });
                
                _terminales.add(nuevaTerminal); 
            }
            rellenaConcatenados();
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar las Estaciones","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }

    private void rellenaConcatenados() 
    {
        for (int i = 0; i < tablaTer.getRowCount(); i++) 
        {
            for (EstacionAux estacion : _estaciones) 
            {
                if (estacion.getIdEstacion()== (int)tablaTer.getValueAt(i, 2)) 
                {
                    tablaTer.setValueAt(estacion.getNomConcatenado(), i, 2);
                    break;                
                }
            }
        }   
    }

    private void insertaDato()
    {
        String sentenciaSQL = "INSERT INTO Destinos.Terminal (IdEstacion, NomTerminal ) VALUES (?, ?)";
        
        try
        {
            var nuevaTerminal = new TerminalAux();                
                nuevaTerminal.setIdEstacion(_idEstacion);
                nuevaTerminal.setNomTerminal(_nomTerminal);

            revisaRepetidos(nuevaTerminal);
            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL); 
            
            statement.setLong(1, _idEstacion);
            statement.setString(2,_nomTerminal);
            statement.executeUpdate();         
           
            consultaDatos();
            
            txbTerminal.setText("");
            cbEstacion.setSelectedIndex(0);
          
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al insertar departamento","Error", JOptionPane.ERROR_MESSAGE); 
            txbTerminal.setText("");
            cbEstacion.setSelectedIndex(0);
   
        }
    }

    private void eliminaDato()
    {
        String sentenciaSQL = "DELETE FROM Destinos.Terminal WHERE IdTerminal = ?";
        
        try
        {            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);            
                
            statement.setLong(1,_idTerminal);
            statement.executeUpdate();
            
            consultaDatos();
           
            txbTerminal.setText("");
            cbEstacion.setSelectedIndex(0);
           
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al eliminar Terminal","Error", JOptionPane.ERROR_MESSAGE); 
            txbTerminal.setText("");
            cbEstacion.setSelectedIndex(0);
        }
    }

    private void modificaDato()
    {
        String sentenciaSQL = "UPDATE Destinos.Terminal SET IdEstacion = ?, NomTerminal = ? WHERE IdTerminal = ?";
        
        try
        {
            var nuevaTerminal = new TerminalAux();                
                nuevaTerminal.setIdEstacion(_idEstacion);
                nuevaTerminal.setNomTerminal(_nomTerminal);

            revisaRepetidos(nuevaTerminal);
            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);            
            
           statement.setLong(1,_idEstacion);
           statement.setString(2,_nomTerminal);
           statement.setLong(3,_idTerminal);


           statement.executeUpdate();
           
           consultaDatos();
           
            txbTerminal.setText("");
            cbEstacion.setSelectedIndex(0); 
           
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al modificar Terminal","Error", JOptionPane.ERROR_MESSAGE); 
            txbTerminal.setText("");
            cbEstacion.setSelectedIndex(0);
        }
    }

    private void revisaRepetidos(TerminalAux nuevaTerminal) throws Exception
    {
      for (TerminalAux terminal : _terminales) 
        {
            if (terminal.getIdEstacion() == nuevaTerminal.getIdEstacion()) 
            {
                if (terminal.getNomTerminal().equals(nuevaTerminal.getNomTerminal())) 
                {
                    JOptionPane.showMessageDialog(null, "Elemento Repetido","Error", JOptionPane.ERROR_MESSAGE);
                    Exception exception = new IllegalArgumentException();           
                    throw exception;           
                }
                
            }
                       
        }
    }

    public static <T extends IIdentificable> long buscaId(String concatenacion, List<T> lista) 
    {
        for (T elemento : lista) 
        {
            if (elemento.getConcatenado().equals(concatenacion)) 
            {
                return elemento.getId();
            }
        }
        return -1;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txbTerminal = new javax.swing.JTextField();
        cbEstacion = new javax.swing.JComboBox<>();
        btnEliminar = new javax.swing.JButton();
        btnAgregar2 = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTerminal = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Terminal");

        cbEstacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnAgregar2.setText("Agregar");
        btnAgregar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregar2ActionPerformed(evt);
            }
        });

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Nombre de la Terminal");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Estación a la que pertenece");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        tablaTerminal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id de la Terminal", "Nombre de la Terminal", "Estación"
            }
        ));
        tablaTerminal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaTerminalMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaTerminal);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(txbTerminal, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                                .addGap(12, 12, 12)
                                .addComponent(cbEstacion, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(178, 178, 178)
                                .addComponent(btnAgregar2)
                                .addGap(18, 18, 18)
                                .addComponent(btnModificar)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminar))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txbTerminal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cbEstacion, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar)
                    .addComponent(btnModificar)
                    .addComponent(btnAgregar2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) 
    {
        _nomTerminal = txbTerminal.getText().trim();
        if (_nomTerminal.isEmpty()) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
             eliminaDato();
        } 
    }

    private void btnAgregar2ActionPerformed(java.awt.event.ActionEvent evt) 
    {
        _idEstacion = buscaId((String)cbEstacion.getSelectedItem(), _estaciones);
        _nomTerminal = txbTerminal.getText().trim();
        if (_nomTerminal.isEmpty()) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
             insertaDato();
        } 
    }

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) 
    {
         _idEstacion = buscaId((String)cbEstacion.getSelectedItem(), _estaciones);
        _nomTerminal = txbTerminal.getText().trim();
        if (_nomTerminal.isEmpty()) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
             modificaDato();
        } 

    }

    

    private void tablaTerminalMouseClicked(java.awt.event.MouseEvent evt) 
    {
         int row =-1 ;
        row = tablaTerminal.rowAtPoint(evt.getPoint());

        _idTerminal = (int)tablaTerminal.getValueAt(row, 0);
        _idEstacion = buscaId((String)tablaTerminal.getValueAt(row, 2), _estaciones) ;

        _nomTerminal = (String)tablaTerminal.getValueAt(row, 1);

        txbTerminal.setText(_nomTerminal);

        
        for (EstacionAux estacion : _estaciones)         
            if (estacion.getIdEstacion() == _idEstacion) 
                cbEstacion.setSelectedItem(estacion.getNomConcatenado());  

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Terminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Terminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Terminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Terminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Terminal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar2;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cbEstacion;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaTerminal;
    private javax.swing.JTextField txbTerminal;
    // End of variables declaration//GEN-END:variables
}
