/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package terminalterrestre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Transporte extends javax.swing.JFrame {

    long _idTransporte;
    String _matricula;
    Integer _lugares;
    Integer _gasolina;
    String _modelo;
    String _marca;
    String _tipo;
    Connection _conexion;
    DefaultTableModel _tablaTrans;
    public Transporte()
    {
        initComponents();
        _tablaTrans = (DefaultTableModel)tablaTransporte.getModel();
        consultaDatos();    
    }
     private void consultaDatos() 
    {
        _tablaTrans.setRowCount(0);
        
        String sentenciaSQL = "SELECT * FROM Transporte";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {               
                _tablaTrans.addRow(
                        new Object[]
                        {
                            result.getLong("IdTransporte"),
                            result.getString("Matricula"),
                            result.getInt("CapacidadLugares"),
                            result.getInt("CapacidadGasolina"),
                            result.getString("Modelo"),
                            result.getString("Marca"),
                            result.getString("TipoTransporte"),
                        });  
            }
        }
        catch(Exception ex)
        {
           JOptionPane.showMessageDialog(null, "Error al recuperar datos","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }

    private void insertaDato()
    {
        String sentenciaSQL = "INSERT INTO Transporte (Matricula, CapacidadLugares, CapacidadGasolina, Modelo, Marca, TipoTransporte) VALUES (?, ?, ?, ?, ?, ?)";
        
        try
        {          
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL); 
            
            statement.setString(1, _matricula);
            statement.setInt(2,_lugares);
            statement.setInt(3,_gasolina);
            statement.setString(4,_modelo);
            statement.setString(5,_marca);
            statement.setString(6,_tipo);


            statement.executeUpdate();         
           
            consultaDatos();
            
            txbMatricula.setText("");
            txbModelo.setText("");
            txbMarca.setText("");
            txbGasolina.setText("");
            limpiaChecks();

          
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al insertar","Error", JOptionPane.ERROR_MESSAGE); 
            txbMatricula.setText("");
            txbModelo.setText("");
            txbMarca.setText("");
            txbGasolina.setText("");
            limpiaChecks();       
   
        }
    }

    private void eliminaDato() 
    {
        String sentenciaSQL = "DELETE FROM  Transporte WHERE IdTransporte = ?";
        
        try
        {            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);            
                
            statement.setLong(1,_idTransporte);
            statement.executeUpdate();
            
            consultaDatos();
           
            txbMatricula.setText("");
            txbModelo.setText("");
            txbMarca.setText("");
            txbGasolina.setText("");
            limpiaChecks();
           
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al eliminar ","Error", JOptionPane.ERROR_MESSAGE); 
            txbMatricula.setText("");
            txbModelo.setText("");
            txbMarca.setText("");
            txbGasolina.setText("");
            limpiaChecks();
        }
    }

    private void modificaDato()
    {
         String sentenciaSQL = "UPDATE Transporte SET Matricula = ?, CapacidadLugares = ?, CapacidadGasolina = ?, Modelo = ?, Marca = ?, TipoTransporte = ? WHERE IdTransporte = ?";
        
        try
        {          
            

            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);           
            
           statement.setString(1, _matricula);
            statement.setInt(2,_lugares);
            statement.setInt(3,_gasolina);
            statement.setString(4,_modelo);
            statement.setString(5,_marca);
            statement.setString(6,_tipo);      
           statement.setLong(7,_idTransporte);        

           statement.executeUpdate();
           
           consultaDatos();
           
            txbMatricula.setText("");
            txbModelo.setText("");
            txbMarca.setText("");
            txbGasolina.setText("");
            limpiaChecks();
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al modificar ","Error", JOptionPane.ERROR_MESSAGE); 
            txbMatricula.setText("");
            txbModelo.setText("");
            txbMarca.setText("");
            txbGasolina.setText("");
            limpiaChecks();
        }

    }

    private void limpiaChecks()
    {
        chb10.setSelected(false);
        chb6.setSelected(false);
        chbAutobus.setSelected(false);
        chbVan.setSelected(false);      
    }

    private void llenaChecks()
    {
        if (_lugares == 6) 
            chb6.setSelected(true);
        else
            chb10.setSelected(true);  
        if (_tipo.equals("Autobús")) 
            chbAutobus.setSelected(true);
        else
            chbVan.setSelected(true);
    }

    private int regresaLugares()
    {
        if (chb10.isSelected()) 
            return 10;  

        return 6;      
    }

    private String regresaTipo()
    {
        if (chbAutobus.isSelected()) 
            return "Autobús";  
                  
        return "Van";      
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        txbMatricula = new javax.swing.JTextField();
        txbModelo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        chb10 = new javax.swing.JCheckBox();
        chb6 = new javax.swing.JCheckBox();
        txbMarca = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txbGasolina = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        chbVan = new javax.swing.JCheckBox();
        chbAutobus = new javax.swing.JCheckBox();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnAgregar2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTransporte = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Transporte");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Matrícula");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Modelo");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Capacidad ");

        chb10.setText("10 Asientos");
        chb10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chb10MouseClicked(evt);
            }
        });

        chb6.setText("6 Asientos");
        chb6.setToolTipText("");
        chb6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chb6MouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Marca");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Capacidad Gasolina");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Tipo");

        chbVan.setText("Van");
        chbVan.setToolTipText("");
        chbVan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chbVanMouseClicked(evt);
            }
        });

        chbAutobus.setText("Autobús");
        chbAutobus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chbAutobusMouseClicked(evt);
            }
        });

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

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

        tablaTransporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id del Transporte", "Matrícula", "Capacidad en Lugares", "Capacidad en Gasolina", "Modelo", "Marca", "Tipo de Transporte"
            }
        ));
        tablaTransporte.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaTransporteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaTransporte);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(89, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txbMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txbModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chb6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chb10, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAgregar2)
                                .addGap(18, 18, 18)
                                .addComponent(btnModificar)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminar))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txbMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txbGasolina, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(chbVan, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chbAutobus, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(106, 106, 106))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chb10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chb6))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txbMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(txbModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txbMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(txbGasolina, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chbAutobus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chbVan))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel7)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar)
                    .addComponent(btnModificar)
                    .addComponent(btnAgregar2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chb10MouseClicked(java.awt.event.MouseEvent evt) {
        chb6.setSelected(false);
    }    

    private void chb6MouseClicked(java.awt.event.MouseEvent evt) {
        chb10.setSelected(false);
    }

    private void chbAutobusMouseClicked(java.awt.event.MouseEvent evt) {
        chbVan.setSelected(false);
    }

    private void chbVanMouseClicked(java.awt.event.MouseEvent evt) {
        chbAutobus.setSelected(false);
    }

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) 
    {
        _matricula = txbMatricula.getText().trim();
        _lugares = regresaLugares();
        _gasolina = Integer.parseInt(txbGasolina.getText().trim());
        _marca = txbMarca.getText().trim();
        _modelo = txbModelo.getText().trim();        
        _tipo = regresaTipo();
        if (_matricula.isEmpty() || _marca.isEmpty() || _modelo.isEmpty() ) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
             modificaDato();
        }

    }
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt)  
    {
        if (_matricula.isEmpty() || _marca.isEmpty() || _modelo.isEmpty() ) 
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
        _matricula = txbMatricula.getText().trim();
        _lugares = regresaLugares();
        _gasolina = Integer.parseInt(txbGasolina.getText().trim());
        _marca = txbMarca.getText().trim();
        _modelo = txbModelo.getText().trim();        
        _tipo = regresaTipo();
        if (_matricula.isEmpty() || _marca.isEmpty() || _modelo.isEmpty() ) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
             insertaDato();
        }
    }

    private void tablaTransporteMouseClicked(java.awt.event.MouseEvent evt)  
    {
        int row =-1 ;
        row = tablaTransporte.rowAtPoint(evt.getPoint());

        _idTransporte = (long)tablaTransporte.getValueAt(row, 0);
        _matricula = (String)tablaTransporte.getValueAt(row, 1);
        _lugares = (Integer)tablaTransporte.getValueAt(row, 2);
        _gasolina = (Integer)tablaTransporte.getValueAt(row, 3);
        _modelo = (String)tablaTransporte.getValueAt(row, 4);
        _marca = (String)tablaTransporte.getValueAt(row, 5);
        _tipo = (String)tablaTransporte.getValueAt(row, 6);

        txbMatricula.setText(_matricula);
        txbGasolina.setText(_gasolina.toString());
        txbModelo.setText(_modelo);
        txbMarca.setText(_marca);
        limpiaChecks();
        llenaChecks();
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
            java.util.logging.Logger.getLogger(Transporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Transporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Transporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Transporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Transporte().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar2;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JCheckBox chb10;
    private javax.swing.JCheckBox chb6;
    private javax.swing.JCheckBox chbAutobus;
    private javax.swing.JCheckBox chbVan;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaTransporte;
    private javax.swing.JTextField txbGasolina;
    private javax.swing.JTextField txbMarca;
    private javax.swing.JTextField txbMatricula;
    private javax.swing.JTextField txbModelo;
    // End of variables declaration//GEN-END:variables
}
