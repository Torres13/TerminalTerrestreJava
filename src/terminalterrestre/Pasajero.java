/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package terminalterrestre;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import clasesAuxiliares.ClienteAux;
import clasesAuxiliares.PasajeroAux;

import java.text.SimpleDateFormat;

import com.toedter.calendar.JDateChooser;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Pasajero extends javax.swing.JFrame {

    long _idPasajero;
    String _nomPasajero;
    Integer _edad;
    Date _fechaNac;
    String _tipoPasajero;
    Connection _conexion;
    DefaultTableModel _tablaPas;
    List<PasajeroAux> _pasajeros;    

    public Pasajero() 
    {
        initComponents();
        _tablaPas = (DefaultTableModel)tablaPasajero.getModel();
        _pasajeros = new ArrayList<PasajeroAux>();       
        consultaDatos();
        rellenaTipos();
        
    }

    private void rellenaTipos() 
    {
        cbTipoPas.removeAllItems();
        cbTipoPas.addItem(""); 
        cbTipoPas.addItem("Adulto"); cbTipoPas.addItem("Niño"); cbTipoPas.addItem("Estudiante"); cbTipoPas.addItem("Adulto Mayor"); cbTipoPas.addItem("Discapacitado");
    }

    private void consultaDatos() 
    {
        _tablaPas.setRowCount(0);
        _pasajeros.clear();
        
        String sentenciaSQL = "SELECT * FROM Pasajero";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                var nuevoPasajero = new PasajeroAux();                
                nuevoPasajero.setNombrePasajero(result.getString("NombrePasajero"));
                nuevoPasajero.setTipoPasajero(result.getString("TipoPasajero"));
                nuevoPasajero.setFechaNacimiento(result.getDate("FechaNacimiento"));

                _tablaPas.addRow(
                        new Object[]
                        {
                             result.getLong("IdPasajero"),
                            result.getString("NombrePasajero"),
                            result.getString("TipoPasajero"),
                            result.getDate("FechaNacimiento"),
                            result.getInt("Edad")
                        });
                
                _pasajeros.add(nuevoPasajero); 
            }
        }
        catch(Exception ex)
        {
           JOptionPane.showMessageDialog(null, "Error al recuperar datos","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }

    private void insertaDato()
    {
        String sentenciaSQL = "INSERT INTO Pasajero (NombrePasajero, TipoPasajero, FechaNacimiento ) VALUES (?, ?, ?)";
        
        try
        {
           var nuevoPasajero = new PasajeroAux();                
                nuevoPasajero.setNombrePasajero(_nomPasajero);
                nuevoPasajero.setTipoPasajero(_tipoPasajero);
                nuevoPasajero.setFechaNacimiento(_fechaNac);
           
            revisaRepetidos(nuevoPasajero);
            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL); 
            
            statement.setString(1, _nomPasajero);
            statement.setString(2,_tipoPasajero);
            statement.setDate(3,_fechaNac);

            statement.executeUpdate();         
           
            consultaDatos();

            txbNombrePas.setText("");
            cbTipoPas.setSelectedIndex(0);
            DateChooser.setDate(new java.util.Date());           
          
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al insertar","Error", JOptionPane.ERROR_MESSAGE); 
            txbNombrePas.setText("");
            cbTipoPas.setSelectedIndex(0);
            DateChooser.setDate(new java.util.Date());                   
        }
    }

    private void eliminaDato() 
    {
        String sentenciaSQL = "DELETE FROM  Pasajero WHERE IdPasajero = ?";
        
        try
        {            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);            
                
            statement.setLong(1,_idPasajero);
            statement.executeUpdate();
            
            consultaDatos();
           
            txbNombrePas.setText("");
            cbTipoPas.setSelectedIndex(0);
            DateChooser.setDate(new java.util.Date());
           
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al eliminar ","Error", JOptionPane.ERROR_MESSAGE); 
            txbNombrePas.setText("");
            cbTipoPas.setSelectedIndex(0);
            DateChooser.setDate(new java.util.Date());
        }
    }
    private void modificaDato()
    {
        String sentenciaSQL = "UPDATE Pasajero SET NombrePasajero = ?, TipoPasajero = ?, FechaNacimiento = ? WHERE IdPasajero = ?";
        
        try
        {
          var nuevoPasajero = new PasajeroAux();                
                nuevoPasajero.setNombrePasajero(_nomPasajero);
                nuevoPasajero.setTipoPasajero(_tipoPasajero);
                nuevoPasajero.setFechaNacimiento(_fechaNac);
           
            revisaRepetidos(nuevoPasajero);
            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);           
            
            statement.setString(1, _nomPasajero);
            statement.setString(2,_tipoPasajero);
            statement.setDate(3,_fechaNac);
           statement.setLong(4,_idPasajero);        

           statement.executeUpdate();
           
           consultaDatos();
           
            txbNombrePas.setText("");
            cbTipoPas.setSelectedIndex(0);
            DateChooser.setDate(new java.util.Date());
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al modificar ","Error", JOptionPane.ERROR_MESSAGE); 
             txbNombrePas.setText("");
            cbTipoPas.setSelectedIndex(0);
            DateChooser.setDate(new java.util.Date());
        }
    }
        


    private void revisaRepetidos(PasajeroAux nuevoPasajero) throws Exception
    {
        for (PasajeroAux pasajero : _pasajeros)
        {
            if (pasajero.getNombrePasajero().equals(nuevoPasajero.getNombrePasajero()))
            {
                if (pasajero.getFechaNacimiento().toString().equals(nuevoPasajero.getFechaNacimiento().toString())) 
                {
                    //if (pasajero.getTipoPasajero().equals(nuevoPasajero.getTipoPasajero()))
                   // {
                        JOptionPane.showMessageDialog(null, "Elemento Repetido","Error", JOptionPane.ERROR_MESSAGE);
                        Exception exception = new IllegalArgumentException();           
                        throw exception;
                  //  }
                }
            }
        }
    }

     

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        DateChooser = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        txbNombrePas = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbTipoPas = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaPasajero = new javax.swing.JTable();
        btnAgregar2 = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pasajero");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Nombre");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Fecha de Nacimiento");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Tipo de Pasajero");

        cbTipoPas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tablaPasajero.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id del Pasajero", "Nombre del Pasajero", "Tipo ", "Fecha de Nacimiento", "Edad"
            }
        ));
        tablaPasajero.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaPasajeroMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaPasajero);

        btnAgregar2.setText("Agregar");
        btnAgregar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregar2ActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(cbTipoPas, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txbNombrePas, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(DateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(47, 47, 47)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(btnAgregar2)
                .addGap(18, 18, 18)
                .addComponent(btnModificar)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txbNombrePas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(DateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbTipoPas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar)
                    .addComponent(btnModificar)
                    .addComponent(btnAgregar2))
                .addGap(32, 32, 32)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregar2ActionPerformed(java.awt.event.ActionEvent evt) 
    {
        _nomPasajero = txbNombrePas.getText().trim();
        java.util.Date fechaSeleccionada = DateChooser.getDate();
        _fechaNac = new java.sql.Date(fechaSeleccionada.getTime());
        
        _tipoPasajero = (String)cbTipoPas.getSelectedItem();
        if (_nomPasajero.isEmpty() || _tipoPasajero.isEmpty() ) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
             insertaDato();
        }
    }

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) 
    {
        if (_nomPasajero.isEmpty() || _tipoPasajero.isEmpty() ) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
             eliminaDato();
        }
    }

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) 
    {
        _nomPasajero = txbNombrePas.getText().trim();
        java.util.Date fechaSeleccionada = DateChooser.getDate();
        _fechaNac = new java.sql.Date(fechaSeleccionada.getTime());
        
        _tipoPasajero = (String)cbTipoPas.getSelectedItem();
        if (_nomPasajero.isEmpty() || _tipoPasajero.isEmpty() ) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
            modificaDato();
        }
        
    }

    private void tablaPasajeroMouseClicked(java.awt.event.MouseEvent evt) 
    {
        try {
            int row =-1 ;
            row = tablaPasajero.rowAtPoint(evt.getPoint());
            
            _idPasajero = (long)tablaPasajero.getValueAt(row, 0);
            _nomPasajero = (String)tablaPasajero.getValueAt(row, 1);
            _fechaNac = (Date)tablaPasajero.getValueAt(row, 3);
            _tipoPasajero = (String)tablaPasajero.getValueAt(row, 2);
            
            txbNombrePas.setText(_nomPasajero);
            cbTipoPas.setSelectedItem(_tipoPasajero);
            
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            var fecha = _fechaNac.toString();
            DateChooser.setDate(formato.parse(fecha));
        }
        catch (ParseException ex)
        {
            Logger.getLogger(Pasajero.class.getName()).log(Level.SEVERE, null, ex);
        }

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
            java.util.logging.Logger.getLogger(Pasajero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pasajero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pasajero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pasajero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pasajero().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser DateChooser;
    private javax.swing.JButton btnAgregar2;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cbTipoPas;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tablaPasajero;
    private javax.swing.JTextField txbNombrePas;
    // End of variables declaration//GEN-END:variables
}
