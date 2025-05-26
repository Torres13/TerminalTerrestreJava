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

public class Estacion extends javax.swing.JFrame {

    Connection _conexion;
    long _idEstacion;
    long _idCiudad;
    String _nomEstacion;
    String _direccion;

    List<CiudadAux> _ciudades;  
    List<EstacionAux> _estaciones;  
    DefaultTableModel tablaEstaci;
    long _index;
    public Estacion() {
        _ciudades = new ArrayList<CiudadAux>();
        _estaciones = new ArrayList<EstacionAux>();
        initComponents();  
        tablaEstaci = (DefaultTableModel) tablaEstacion.getModel();
        llenaCiudades();
        consultaDatos();    
    }

    private void llenaCiudades() 
    {
        _ciudades.clear();
        cbCiudad.removeAllItems();
        cbCiudad.addItem("");
        
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
                cbCiudad.addItem(nuevaCiudad.getNomCiudad());               
                _ciudades.add(nuevaCiudad); 
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar los Ciudades","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }

    private void consultaDatos()
    {
        tablaEstaci.setRowCount(0);
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
                nuevaEstacion.SetDireccion(result.getString("Direccion"));                

                tablaEstaci.addRow(
                        new Object[]
                        {
                            result.getInt("IdEstacion"),
                            result.getInt("IdCiudad"),
                            result.getString("NomEstacion"),
                            result.getString("Direccion"),
                            result.getInt("NumTerminales"),                            
                        });
                
                _estaciones.add(nuevaEstacion); 
            }
            concatena();
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar las Estaciones","Error", JOptionPane.ERROR_MESSAGE);            
        }  

    }
    
    private void eliminaDato()
     {
        String sentenciaSQL = "DELETE FROM Destinos.Estacion WHERE IdEstacion = ?";
        
        try
        {            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);            
                
            statement.setLong(1,_idEstacion);
            statement.executeUpdate();
            
            consultaDatos();
           
            txbEstacion.setText("");
            txbDireccion.setText("");  
            cbCiudad.setSelectedIndex(0);
           
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al eliminar Ciudad","Error", JOptionPane.ERROR_MESSAGE); 
            txbEstacion.setText("");
            txbDireccion.setText(""); 
            cbCiudad.setSelectedIndex(0);
 
        }

     }
    
    private void concatena() 
    {
        for (EstacionAux estacion : _estaciones) 
        {
            for (CiudadAux ciudad : _ciudades) 
            {
                if (estacion.getIdCiudad() == ciudad.getIdCiudad())
                {
                    estacion.setNomCiudad(ciudad.getNomCiudad());
                    estacion.setNomConcatenado(ciudad.getNomCiudad());
                    break;
                }             
            }            
        }
        for (int i = 0; i < tablaEstaci.getRowCount(); i++) 
        {
            for (EstacionAux estacion : _estaciones) 
            {
                if (estacion.getIdEstacion()== (int)tablaEstaci.getValueAt(i, 0)) 
                {
                    tablaEstaci.setValueAt(estacion.getNomConcatenado(), i, 1);
                    break;                
                }
            }
        }        
    }

    private void insertaDato()
    {
         String sentenciaSQL = "INSERT INTO Destinos.Estacion (IdCiudad, NomEstacion, Direccion, NumTerminales) VALUES (?, ?, ?, ?)";
        
        try
        {
            var nuevaEstacion = new EstacionAux();                
                nuevaEstacion.setIdCiudad(_idCiudad);
                nuevaEstacion.setNomEstacion(_nomEstacion);
                nuevaEstacion.SetDireccion(_direccion);

            revisaRepetidos(nuevaEstacion);
            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL); 
            
            statement.setLong(1, _idCiudad);
            statement.setString(2,_nomEstacion);
            statement.setString(3, _direccion);
            statement.setInt(4, 0);
            statement.executeUpdate();         
           
            consultaDatos();
            
            txbEstacion.setText("");
            txbDireccion.setText(""); 
            cbCiudad.setSelectedIndex(0);
          
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al insertar departamento","Error", JOptionPane.ERROR_MESSAGE); 
            txbEstacion.setText("");
            txbDireccion.setText(""); 
            cbCiudad.setSelectedIndex(0);
   
        }
    }
    
    private void modificaDato() 
    {
        String sentenciaSQL = "UPDATE Destinos.Estacion SET IdCiudad = ?, NomEstacion = ?, Direccion = ?  WHERE IdEstacion = ?";
        
        try
        {
            var nuevaEstacion = new EstacionAux();                
                nuevaEstacion.setIdEstacion(_idEstacion);
                nuevaEstacion.setIdCiudad(_idCiudad);
                nuevaEstacion.setNomEstacion(_nomEstacion);
                nuevaEstacion.SetDireccion(_direccion);

            revisaRepetidos(nuevaEstacion);
            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);            
            
           statement.setLong(1,_idCiudad);
           statement.setString(2,_nomEstacion);
           statement.setString(3,_direccion);
           statement.setLong(4,_idEstacion);


           statement.executeUpdate();
           
           consultaDatos();
           
             txbEstacion.setText("");
            txbDireccion.setText(""); 
            cbCiudad.setSelectedIndex(0);
           
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al modificar departamento","Error", JOptionPane.ERROR_MESSAGE); 
            txbEstacion.setText("");
            txbDireccion.setText(""); 
            cbCiudad.setSelectedIndex(0);
        }
    }


    private void revisaRepetidos(EstacionAux _nuevaEstacion) throws Exception 
    {
        for (EstacionAux estacion : _estaciones) 
        {
            if (estacion.getIdCiudad() == _nuevaEstacion.getIdCiudad())
            {
                if (estacion.getNomEstacion().equals(_nuevaEstacion.getNomEstacion())) 
                {
                    if (estacion.getDireccion().equals( _nuevaEstacion.getDireccion()))
                    {
                        JOptionPane.showMessageDialog(null, "Elemento Repetido","Error", JOptionPane.ERROR_MESSAGE);
                        Exception exception = new IllegalArgumentException();
                        throw exception;
                    }
                }
            }            
        }
    }

    public interface IIdentificable {
    long getId();
    String getConcatenado();
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

        btnEliminar = new javax.swing.JButton();
        btnAgregar2 = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEstacion = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txbDireccion = new javax.swing.JTextField();
        txbEstacion = new javax.swing.JTextField();
        cbCiudad = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Estación");

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

        tablaEstacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id de la Esatción", "Id de la Ciudad", "Nombre de la Estación", "Dirección", "Número de Terminales"
            }
        ));
        tablaEstacion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEstacionMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaEstacion);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Nombre de la Estación");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Dirección");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Ciudad en la que se encuentra:");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        cbCiudad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAgregar2))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txbDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnModificar)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnEliminar))
                                    .addComponent(txbEstacion, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txbEstacion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3))
                    .addComponent(txbDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar)
                    .addComponent(btnModificar)
                    .addComponent(btnAgregar2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt)
    {
        _idCiudad = buscaId((String)cbCiudad.getSelectedItem(), _ciudades);
        _nomEstacion = txbEstacion.getText().trim();
        _direccion = txbDireccion.getText().trim();        
        if (_nomEstacion.isEmpty() || _direccion.isEmpty()) 
        {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un elemento a eliminar","Error", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
             eliminaDato();
        }               
    }

    private void btnAgregar2ActionPerformed(java.awt.event.ActionEvent evt) 
    {
        _idCiudad = buscaId((String)cbCiudad.getSelectedItem(), _ciudades);
        _nomEstacion = txbEstacion.getText().trim();
        _direccion = txbDireccion.getText().trim();        
        if (_nomEstacion.isEmpty() || _direccion.isEmpty()) 
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
        _idCiudad = buscaId((String)cbCiudad.getSelectedItem(), _ciudades);
        _nomEstacion = txbEstacion.getText().trim();
        _direccion = txbDireccion.getText().trim();        
        if (_nomEstacion.isEmpty() || _direccion.isEmpty()) 
        {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un elemento a modificar","Error", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
            modificaDato();
        } 
    }

   
    private void tablaEstacionMouseClicked(java.awt.event.MouseEvent evt) 
    {
        int row =-1 ;
        row = tablaEstacion.rowAtPoint(evt.getPoint());

        _idEstacion = (int)tablaEstacion.getValueAt(row, 0);
        _idCiudad = buscaId((String)tablaEstacion.getValueAt(row, 1), _estaciones) ;

        _nomEstacion = (String)tablaEstacion.getValueAt(row, 2);
        _direccion = (String)tablaEstacion.getValueAt(row, 3);

        txbEstacion.setText(_nomEstacion);
        txbDireccion.setText(_direccion);

        cbCiudad.setSelectedItem("CDMX");
        for (EstacionAux estacion : _estaciones)         
            if (estacion.getIdEstacion() == _idEstacion) 
                cbCiudad.setSelectedItem(estacion.getNomConcatenado());          
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
            java.util.logging.Logger.getLogger(Estacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Estacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Estacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Estacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Estacion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar2;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cbCiudad;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaEstacion;
    private javax.swing.JTextField txbDireccion;
    private javax.swing.JTextField txbEstacion;
    // End of variables declaration//GEN-END:variables
}
