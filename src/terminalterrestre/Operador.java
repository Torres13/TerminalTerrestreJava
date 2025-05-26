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

import clasesAuxiliares.OperadorAux;

public class Operador extends javax.swing.JFrame {

    long _IdOperador;
    String _nomOperador;
    String _RFC;
    Float _salario;
    Connection _conexion;
    List<OperadorAux>_operadores;
    DefaultTableModel tablaOp;

    public Operador() {
        _operadores = new ArrayList<OperadorAux>();
        initComponents();
        tablaOp = (DefaultTableModel) tablaOperador.getModel();
        consultaDatos();
    }

    private void consultaDatos() 
    {
        tablaOp.setRowCount(0);
        _operadores.clear();
        
        String sentenciaSQL = "SELECT * FROM Operador";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                var nuevoOperador = new OperadorAux();                
                nuevoOperador.setIdOperador(result.getInt("IdOperador"));
                nuevoOperador.setNombre(result.getString("NomOpe"));
                nuevoOperador.setRFC(result.getString("RFC"));

                tablaOp.addRow(
                        new Object[]
                        {
                            result.getInt("IdOperador"),
                            result.getString("NomOpe"),                           
                            result.getString("RFC"),
                            result.getFloat("Salario")
                        });
                
                _operadores.add(nuevoOperador); 
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar los Operadores","Error", JOptionPane.ERROR_MESSAGE);            
        } 
        
    }

    private void insertaDato()
    {
        String sentenciaSQL = "INSERT INTO Operador (NomOpe, RFC, Salario ) VALUES (?, ?, ?)";
        
        try
        {
           var nuevoOperador = new OperadorAux();                
                nuevoOperador.setNombre(_nomOperador);
                nuevoOperador.setRFC(_RFC);

            revisaRepetidos(nuevoOperador);
            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL); 
            
            statement.setString(1, _nomOperador);
            statement.setString(2,_RFC);
            statement.setFloat(3,_salario);

            statement.executeUpdate();         
           
            consultaDatos();
            
            txbNombre.setText("");
            txbRFC.setText("");
            txbSalario.setText("");
          
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al insertar","Error", JOptionPane.ERROR_MESSAGE); 
            txbNombre.setText("");
            txbRFC.setText("");
            txbSalario.setText("");
   
        }
    }

    private void revisaRepetidos(OperadorAux nuevoOperador) throws Exception
    {
        for (OperadorAux operador : _operadores) 
        {
            if (operador.getNombre().equals(nuevoOperador.getNombre())) 
            {
                if (operador.getRFC().equals(nuevoOperador.getRFC()))
                {
                    JOptionPane.showMessageDialog(null, "Elemento Repetido","Error", JOptionPane.ERROR_MESSAGE);
                    Exception exception = new IllegalArgumentException();           
                    throw exception;                 
                }         
            }
        }

    }

    private void eliminaDato()
    {
        String sentenciaSQL = "DELETE FROM Operador WHERE IdOperador = ?";
        
        try
        {            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);            
                
            statement.setLong(1,_IdOperador);
            statement.executeUpdate();
            
            consultaDatos();
           
            txbNombre.setText("");
            txbRFC.setText("");
            txbSalario.setText("");
           
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al eliminar ","Error", JOptionPane.ERROR_MESSAGE); 
            txbNombre.setText("");
            txbRFC.setText("");
            txbSalario.setText("");
        }
    }

    private void modificaDato()
    {
        String sentenciaSQL = "UPDATE Operador SET NomOpe = ?, RFC = ?, Salario = ? WHERE IdOperador = ?";
        
        try
        {
           var nuevoOperador = new OperadorAux();                
                nuevoOperador.setNombre(_nomOperador);
                nuevoOperador.setRFC(_RFC);

            revisaRepetidos(nuevoOperador);
            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);            
            
           statement.setString(1,_nomOperador);
           statement.setString(2,_RFC);
           statement.setFloat(3,_salario);
           statement.setFloat(4,_IdOperador);        

           statement.executeUpdate();
           
           consultaDatos();
           
            txbNombre.setText("");
            txbRFC.setText("");
            txbSalario.setText("");
           
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al modificar ","Error", JOptionPane.ERROR_MESSAGE); 
            txbNombre.setText("");
            txbRFC.setText("");
            txbSalario.setText("");;
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
        txbRFC = new javax.swing.JTextField();
        txbNombre = new javax.swing.JTextField();
        txbSalario = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaOperador = new javax.swing.JTable();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Operador");

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
        jLabel2.setText("Nombre del Operador");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("RFC");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Salario");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        tablaOperador.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "IdOperador", "Nombre Operador", "RFC", "Salario"
            }
        ));
        tablaOperador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaOperadorMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaOperador);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 10, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txbRFC, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txbNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(txbSalario, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(169, 169, 169)
                        .addComponent(btnAgregar2)
                        .addGap(18, 18, 18)
                        .addComponent(btnModificar)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txbNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txbRFC, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txbSalario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar)
                    .addComponent(btnModificar)
                    .addComponent(btnAgregar2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt)
    {        
        _nomOperador = txbNombre.getText().trim();
        _RFC = txbRFC.getText().trim();
        _salario = Float.parseFloat(txbSalario.getText().trim());
        if (_nomOperador.isEmpty() || _RFC.isEmpty()) 
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
        _nomOperador = txbNombre.getText().trim();
        _RFC = txbRFC.getText().trim();
        _salario = Float.parseFloat(txbSalario.getText().trim());
        if (_nomOperador.isEmpty() || _RFC.isEmpty()) 
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
        _nomOperador = txbNombre.getText().trim();
        _RFC = txbRFC.getText().trim();
        _salario = Float.parseFloat(txbSalario.getText().trim());
        if (_nomOperador.isEmpty() || _RFC.isEmpty()) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
             modificaDato();
        } 
        
    }

    private void tablaOperadorMouseClicked(java.awt.event.MouseEvent evt) 
    {
        int row =-1 ;
        row = tablaOperador.rowAtPoint(evt.getPoint());

        _IdOperador = (int)tablaOperador.getValueAt(row, 0);
        _nomOperador = (String)tablaOperador.getValueAt(row, 1);
        _RFC = (String)tablaOperador.getValueAt(row, 2);
        _salario = (Float)tablaOperador.getValueAt(row, 3);

        txbNombre.setText(_nomOperador);
        txbRFC.setText(_RFC);
        txbSalario.setText(_salario.toString());
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
            java.util.logging.Logger.getLogger(Operador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Operador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Operador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Operador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Operador().setVisible(true);
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
    private javax.swing.JTable tablaOperador;
    private javax.swing.JTextField txbNombre;
    private javax.swing.JTextField txbRFC;
    private javax.swing.JTextField txbSalario;
    // End of variables declaration//GEN-END:variables
}
