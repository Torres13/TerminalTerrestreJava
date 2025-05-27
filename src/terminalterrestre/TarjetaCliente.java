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

import clasesAuxiliares.ClienteAux;
import clasesAuxiliares.TarjetaClienteAux;
import terminalterrestre.Estacion.IIdentificable;

public class TarjetaCliente extends javax.swing.JFrame {

    long _idTarjeta;
    long _idCliente;
    String _CCV;
    String _mes;
    String _anio;
    String _fechaVenci;
    String _banco;
    String _tipo;
    long _numTarjeta;
    Connection _conexion;
    List<ClienteAux> _clientes;
    List<TarjetaClienteAux> _tarjetas;
    DefaultTableModel _tablaTar;
    
    public TarjetaCliente() 
    {
        initComponents();
        _tablaTar = (DefaultTableModel) tablaTarjetas.getModel();
        _clientes = new ArrayList<ClienteAux>();
        _tarjetas = new ArrayList<TarjetaClienteAux>(); 
        llenaClientes();
        llenaBancosYTipos();
        consultaDatos();    
    }

     public void llenaBancosYTipos()
    {
        cbBanco.removeAllItems();
        cbBanco.addItem(""); 
        cbBanco.addItem("Santander"); cbBanco.addItem("Banamex"); cbBanco.addItem("BBVA"); cbBanco.addItem("HSBC"); cbBanco.addItem("BanBajio");
        cbBanco.addItem("Scotiabank"); cbBanco.addItem("Banorte"); cbBanco.addItem("Azteca"); cbBanco.addItem("Inbursa"); cbBanco.addItem("Afirme");

        cbTipo.removeAllItems();
        cbTipo.addItem(""); 
        cbTipo.addItem("Crédito"); cbTipo.addItem("Débito");
    }

    private void llenaClientes() 
    {
         _clientes.clear();
         cbCliente.removeAllItems();
        cbCliente.addItem("");    

        
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
                _clientes.add(nuevoCliente); 
            }
            for (ClienteAux cliente : _clientes) 
            {
                var clienteConcatenado = cliente.getNomCliente() + " {" + cliente.getIdCliente() + "}";
                cliente.setClienteConcatenado(clienteConcatenado);
                cbCliente.addItem(clienteConcatenado);    
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar las Ciudades","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }

    private void consultaDatos() 
    {
        _tablaTar.setRowCount(0);
        _tarjetas.clear();
        
        String sentenciaSQL = "SELECT * FROM InfoCliente.TarjetaCliente";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {            
                _tablaTar.addRow(
                        new Object[]
                        {
                            result.getInt("IdTarjeta"),
                            result.getInt("IdCliente"),                           
                            result.getString("Banco"),
                            result.getString("Tipo"),
                            result.getLong("NumTarjeta"),
                            result.getString("FechaVenci"),
                            result.getString("ConSeg")                          
                        });
                
            }
            rellenaConcatenados();
        }
        catch(Exception ex)
        {
           JOptionPane.showMessageDialog(null, "Error al recuperar datos","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }

    private void rellenaConcatenados() 
    {
        for (int i = 0; i < _tablaTar.getRowCount(); i++) 
        {
            for (ClienteAux cliente : _clientes) 
            {
                if (cliente.getIdCliente() == (int)_tablaTar.getValueAt(i, 1)) 
                {
                    _tablaTar.setValueAt(cliente.getClienteConcatenado(), i, 1);
                    break;                
                }
            }
        }   
    }

    private void insertaDato()
    {
        String sentenciaSQL = "INSERT INTO InfoCliente.TarjetaCliente(IdCliente, Banco, Tipo, NumTarjeta, FechaVenci, ConSeg) VALUES (?, ?, ?, ?, ?, ?)";
        
        try
        {
           
            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL); 
            
            statement.setLong(1, _idCliente);
            statement.setString(2,_banco);
            statement.setString(3,_tipo);
            statement.setLong(4,_numTarjeta);
            statement.setString(5, _fechaVenci);
            statement.setString(6,_CCV);

            statement.executeUpdate();         
           
            consultaDatos();
            
            txbNumero.setText("");
            txbAnio.setText("");
            txbMes.setText("");
            txbCCV.setText("");
            cbCliente.setSelectedIndex(0);
            cbTipo.setSelectedIndex(0);
            cbBanco.setSelectedIndex(0);          
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al insertar","Error", JOptionPane.ERROR_MESSAGE); 
            txbNumero.setText("");
            txbAnio.setText("");
            txbMes.setText("");
            txbCCV.setText("");
            cbCliente.setSelectedIndex(0);
            cbTipo.setSelectedIndex(0);
            cbBanco.setSelectedIndex(0); 
                 
        }
    }

    private void eliminaDato() 
        {
            String sentenciaSQL = "DELETE FROM InfoCliente.TarjetaCliente WHERE IdTarjeta = ?";
            
            try
            {            
                _conexion = ConexionSQL.getConnection(); 
                PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);            
                    
                statement.setLong(1,_idTarjeta);
                statement.executeUpdate();
                
                consultaDatos();
            
                txbNumero.setText("");
                txbAnio.setText("");
                txbMes.setText("");
                txbCCV.setText("");
                cbCliente.setSelectedIndex(0);
                cbTipo.setSelectedIndex(0);
                cbBanco.setSelectedIndex(0); 
            
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(null, "Error al eliminar ","Error", JOptionPane.ERROR_MESSAGE); 
                txbNumero.setText("");
                txbAnio.setText("");
                txbMes.setText("");
                txbCCV.setText("");
                cbCliente.setSelectedIndex(0);
                cbTipo.setSelectedIndex(0);
                cbBanco.setSelectedIndex(0); 
            }
        }
   
    private void modificaDato()
    {
         String sentenciaSQL = "UPDATE InfoCliente.TarjetaCliente SET IdCliente = ?, Banco = ?, Tipo = ?, NumTarjeta = ?, FechaVenci = ?, ConSeg = ? WHERE IdTarjeta = ?";
        
        try
        {          
            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);           
            
           statement.setLong(1,_idCliente);
           statement.setString(2,_banco);
           statement.setString(3,_tipo);
           statement.setLong(4,_numTarjeta);        
           statement.setString(5,_fechaVenci);   
           statement.setString(6,_CCV);       
           statement.setLong(7,_idTarjeta);        



           statement.executeUpdate();
           
           consultaDatos();
           
            txbNumero.setText("");
            txbAnio.setText("");
            txbMes.setText("");
            txbCCV.setText("");
            cbCliente.setSelectedIndex(0);
            cbTipo.setSelectedIndex(0);
            cbBanco.setSelectedIndex(0); 
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al modificar ","Error", JOptionPane.ERROR_MESSAGE); 
            txbNumero.setText("");
            txbAnio.setText("");
            txbMes.setText("");
            txbCCV.setText("");
            cbCliente.setSelectedIndex(0);
            cbTipo.setSelectedIndex(0);
            cbBanco.setSelectedIndex(0); 
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

        jLabel4 = new javax.swing.JLabel();
        txbMes = new javax.swing.JTextField();
        btnEliminar = new javax.swing.JButton();
        btnAgregar2 = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txbNumero = new javax.swing.JTextField();
        txbCCV = new javax.swing.JTextField();
        txbAnio = new javax.swing.JTextField();
        cbCliente = new javax.swing.JComboBox<>();
        cbBanco = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        cbTipo = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTarjetas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tarjeta Cliente");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Cliente");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);

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
        jLabel2.setText("Número de Tarjeta");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Vencimiento");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("CCV");
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        cbCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbBanco.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Banco");
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        cbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Tipo Tarjeta");
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        tablaTarjetas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "id Tarjeta", "Id del Cliente", "Banco", "Tipo", "Número de Tarjeta", "Vencimiento", "Código de Seguridad"
            }
        ));
        tablaTarjetas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaTarjetasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaTarjetas);

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
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txbMes, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txbAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txbNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(38, 38, 38)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txbCCV, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(90, 90, 90)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(190, 190, 190)
                        .addComponent(btnAgregar2)
                        .addGap(18, 18, 18)
                        .addComponent(btnModificar)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txbMes, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txbAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(7, 7, 7))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txbCCV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txbNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar)
                    .addComponent(btnModificar)
                    .addComponent(btnAgregar2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablaTarjetasMouseClicked(java.awt.event.MouseEvent evt) 
    {
        int row =-1 ;
        row = tablaTarjetas.rowAtPoint(evt.getPoint());

        _idTarjeta = (int)tablaTarjetas.getValueAt(row, 0);
        _idCliente = buscaId((String)tablaTarjetas.getValueAt(row, 1), _clientes) ;

        _banco = (String)tablaTarjetas.getValueAt(row, 2);
        _tipo = (String)tablaTarjetas.getValueAt(row, 3);
        _numTarjeta = (Long)tablaTarjetas.getValueAt(row, 4);
        _fechaVenci = (String)tablaTarjetas.getValueAt(row, 5);
        var fecha = _fechaVenci.split("/");
        _mes = fecha[0]; _anio = fecha[1];
        _CCV = (String)tablaTarjetas.getValueAt(row, 6);


        cbBanco.setSelectedItem(_banco);
        cbTipo.setSelectedItem(_tipo);
        txbNumero.setText(_numTarjeta +"");
        txbAnio.setText(_anio);
        txbMes.setText(_mes);
        txbCCV.setText(_CCV+"");

        for (ClienteAux cliente : _clientes)         
            if (cliente.getIdCliente() == _idCliente) 
                cbCliente.setSelectedItem(cliente.getClienteConcatenado());


    }

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) 
    {
         _idCliente = buscaId((String)cbCliente.getSelectedItem(), _clientes);
        _banco = (String)cbBanco.getSelectedItem();
        _tipo = (String)cbTipo.getSelectedItem();
        _numTarjeta = Long.parseLong(txbNumero.getText());
        _fechaVenci = txbMes.getText() + "/"+ txbAnio.getText();
        _CCV = txbCCV.getText();
        if (_banco.isEmpty() || _tipo.isEmpty() || _fechaVenci == null) 
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
        _idCliente = buscaId((String)cbCliente.getSelectedItem(), _clientes);
        _banco = (String)cbBanco.getSelectedItem();
        _tipo = (String)cbTipo.getSelectedItem();
        _numTarjeta = Long.parseLong(txbNumero.getText());
        _fechaVenci = txbMes.getText() + "/"+ txbAnio.getText();
        _CCV = txbCCV.getText();
        if (_banco.isEmpty() || _tipo.isEmpty() || _fechaVenci == null ) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }
        if (Integer.parseInt(txbAnio.getText()) < 2025 && Integer.parseInt(txbMes.getText()) < 8) 
        {
             JOptionPane.showMessageDialog(null, "Fecha Invalida ","Error", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
             insertaDato();
        } 
    }

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) 
    {
        _idCliente = buscaId((String)cbCliente.getSelectedItem(), _clientes);
        _banco = (String)cbBanco.getSelectedItem();
        _tipo = (String)cbTipo.getSelectedItem();
        _numTarjeta = Long.parseLong(txbNumero.getText());
        _fechaVenci = txbMes.getText() + "/"+ txbAnio.getText();
        _CCV = txbCCV.getText();
        if (_banco.isEmpty() || _tipo.isEmpty() || _fechaVenci == null) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }
        if (Integer.parseInt(txbAnio.getText()) <= 2025 && Integer.parseInt(txbMes.getText()) < 8) 
        {
             JOptionPane.showMessageDialog(null, "Fecha Invalida ","Error", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
             modificaDato();
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
            java.util.logging.Logger.getLogger(TarjetaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TarjetaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TarjetaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TarjetaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TarjetaCliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar2;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cbBanco;
    private javax.swing.JComboBox<String> cbCliente;
    private javax.swing.JComboBox<String> cbTipo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaTarjetas;
    private javax.swing.JTextField txbAnio;
    private javax.swing.JTextField txbCCV;
    private javax.swing.JTextField txbMes;
    private javax.swing.JTextField txbNumero;
    // End of variables declaration//GEN-END:variables
}
