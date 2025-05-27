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

import javax.sound.sampled.FloatControl;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import clasesAuxiliares.ClienteAux;
import clasesAuxiliares.OperadorAux;

public class Cliente extends javax.swing.JFrame {

    long _idCliente;
    String _nombreCliente;
    String _email;
    Long _telefono;
    String _clienteDesde;
    Integer _numTarjetas;
    Connection _conexion;
    List<ClienteAux> _clientes;
    DefaultTableModel tablaClient;

    public Cliente() {
        _clientes = new ArrayList<ClienteAux>();
        initComponents();
        tablaClient = (DefaultTableModel) tablaCliente.getModel();
        consultaDatos();
    }

    private void consultaDatos() 
    {
        tablaClient.setRowCount(0);
        _clientes.clear();
        
        String sentenciaSQL = "SELECT * FROM InfoCliente.Cliente";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                var nuevoCliente = new ClienteAux();                
                nuevoCliente.setIdCliente(result.getInt("IdCliente"));
                nuevoCliente.setNomCliente(result.getString("NomCliente"));
                nuevoCliente.setEmail(result.getString("Email"));
                nuevoCliente.setTelefono(result.getLong("Telefono"));



                tablaClient.addRow(
                        new Object[]
                        {
                            result.getInt("IdCliente"),
                            result.getString("NomCliente"),                           
                            result.getString("Email"),
                            result.getLong("Telefono"),
                            result.getDate("ClienteDesde"),
                            result.getInt("NumTarjetas")
                        });
                
                _clientes.add(nuevoCliente); 
            }
        }
        catch(Exception ex)
        {
           JOptionPane.showMessageDialog(null, "Error al recuperar datos","Error", JOptionPane.ERROR_MESSAGE);            
        } 
        
    }

    private void insertaDato()
    {
        String sentenciaSQL = "INSERT INTO InfoCliente.Cliente (NomCliente, Email, Telefono, NumTarjetas ) VALUES (?, ?, ?, ?)";
        
        try
        {
           var nuevoCliente = new ClienteAux();                
                nuevoCliente.setNomCliente(_nombreCliente);
                nuevoCliente.setEmail(_email);
                nuevoCliente.setTelefono(_telefono);
           
           // revisaRepetidos(nuevoCliente);
            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL); 
            
            statement.setString(1, _nombreCliente);
            statement.setString(2,_email);
            statement.setLong(3,_telefono);
            statement.setFloat(4,0);

            statement.executeUpdate();         
           
            consultaDatos();
            
            txbNombre.setText("");
            txbTelefono.setText("");
            txbEmail.setText("");
          
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al insertar","Error", JOptionPane.ERROR_MESSAGE); 
             txbNombre.setText("");
            txbTelefono.setText("");
            txbEmail.setText("");        
   
        }
    }

    private void eliminaDato() 
    {
        String sentenciaSQL = "DELETE FROM  InfoCliente.Cliente WHERE IdCliente = ?";
        
        try
        {            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);            
                
            statement.setLong(1,_idCliente);
            statement.executeUpdate();
            
            consultaDatos();
           
             txbNombre.setText("");
            txbTelefono.setText("");
            txbEmail.setText("");
           
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al eliminar ","Error", JOptionPane.ERROR_MESSAGE); 
             txbNombre.setText("");
            txbTelefono.setText("");
            txbEmail.setText("");
        }
    }

    private void modificaDato()
    {
        String sentenciaSQL = "UPDATE InfoCliente.Cliente SET NomCliente = ?, Email = ?, Telefono = ? WHERE IdCliente = ?";
        
        try
        {
           var nuevoCliente = new ClienteAux();                
                nuevoCliente.setNomCliente(_nombreCliente);
                nuevoCliente.setEmail(_email);
                nuevoCliente.setTelefono(_telefono);
           
            //revisaRepetidosModificados(nuevoCliente);
            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);           
            
           statement.setString(1,_nombreCliente);
           statement.setString(2,_email);
           statement.setLong(3,_telefono);
           statement.setFloat(4,_idCliente);        

           statement.executeUpdate();
           
           consultaDatos();
           
            txbNombre.setText("");
            txbTelefono.setText("");
            txbEmail.setText("");
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al modificar ","Error", JOptionPane.ERROR_MESSAGE); 
             txbNombre.setText("");
            txbTelefono.setText("");
            txbEmail.setText("");
        }
    }

    private void revisaRepetidos(ClienteAux nuevoCliente) throws Exception
    {
        for (ClienteAux cliente : _clientes) 
        {
            /*if (cliente.getNomCliente().equals(nuevoCliente.getNomCliente())) 
            {*/
                if (cliente.getEmail().equals(nuevoCliente.getEmail()) || cliente.getTelefono() == nuevoCliente.getTelefono()) 
                {
                    JOptionPane.showMessageDialog(null, "Elemento Repetido","Error", JOptionPane.ERROR_MESSAGE);
                    Exception exception = new IllegalArgumentException();           
                    throw exception; 
                }
            //}            
        }
    }

    private void revisaRepetidosModificados(ClienteAux nuevoCliente) throws Exception
    {
        for (ClienteAux cliente : _clientes) 
        {
            if (cliente.getNomCliente().equals(nuevoCliente.getNomCliente())) 
            {
                if (cliente.getEmail().equals(nuevoCliente.getEmail()) && cliente.getTelefono() == nuevoCliente.getTelefono()) 
                {
                    JOptionPane.showMessageDialog(null, "Elemento Repetido","Error", JOptionPane.ERROR_MESSAGE);
                    Exception exception = new IllegalArgumentException();           
                    throw exception; 
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
        btnEliminar = new javax.swing.JButton();
        btnAgregar2 = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txbTelefono = new javax.swing.JTextField();
        txbNombre = new javax.swing.JTextField();
        txbEmail = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaCliente = new javax.swing.JTable();

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
        setTitle("Cliente");

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
        jLabel2.setText("Nombre del Cliente");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Teléfono");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Email");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        tablaCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Id del Cliente", "Nombre del Cliente", "Email", "Teléfono", "Cliente desde", "Número de Tarjetas"
            }
        ));
        tablaCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaClienteMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaCliente);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txbNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txbTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(169, 169, 169)
                .addComponent(btnAgregar2)
                .addGap(18, 18, 18)
                .addComponent(btnModificar)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txbNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txbTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar)
                    .addComponent(btnModificar)
                    .addComponent(btnAgregar2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) 
    {
         _nombreCliente = txbNombre.getText().trim();
        _telefono = Long.parseLong(txbTelefono.getText().trim());
        _email = txbEmail.getText().trim();
        if (_nombreCliente.isEmpty() || _email.isEmpty() || _telefono == null) 
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
        _nombreCliente = txbNombre.getText().trim();
        _telefono = Long.parseLong(txbTelefono.getText().trim());
        _email = txbEmail.getText().trim();
        if (_nombreCliente.isEmpty() || _email.isEmpty() || _telefono == null) 
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
         _nombreCliente = txbNombre.getText().trim();
        _telefono = Long.parseLong(txbTelefono.getText().trim());
        _email = txbEmail.getText().trim();
        if (_nombreCliente.isEmpty() || _email.isEmpty() || _telefono == null) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
             modificaDato();
        } 

    }

    private void tablaClienteMouseClicked(java.awt.event.MouseEvent evt)
    {
        int row =-1 ;
        row = tablaCliente.rowAtPoint(evt.getPoint());

        _idCliente = (int)tablaCliente.getValueAt(row, 0);
        _nombreCliente = (String)tablaCliente.getValueAt(row, 1);
        _telefono = (Long)tablaCliente.getValueAt(row, 3);
        _email = (String)tablaCliente.getValueAt(row, 2);

        txbNombre.setText(_nombreCliente);
        txbTelefono.setText(_telefono.toString());
        txbEmail.setText(_email);

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
            java.util.logging.Logger.getLogger(Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar2;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tablaCliente;
    private javax.swing.JTextField txbEmail;
    private javax.swing.JTextField txbNombre;
    private javax.swing.JTextField txbTelefono;
    // End of variables declaration//GEN-END:variables
}
