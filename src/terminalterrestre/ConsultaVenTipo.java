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
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import clasesAuxiliares.ClienteAux;
import clasesAuxiliares.TarjetaClienteAux;
import clasesAuxiliares.TransaccionAux;
import terminalterrestre.Estacion.IIdentificable;


public class ConsultaVenTipo extends javax.swing.JFrame {

    List<ClienteAux> _clientes;
    List<TransaccionAux> _transacciones; 
    List<TarjetaClienteAux> _tarjetas;
    Connection _conexion;
    DefaultTableModel tablaRep;

    public ConsultaVenTipo() 
    {
        initComponents();
        
        _tarjetas = new ArrayList<TarjetaClienteAux>();
        _clientes = new ArrayList<ClienteAux>();
        _transacciones = new ArrayList<TransaccionAux>();
        tablaRep = (DefaultTableModel) tablaReporte.getModel();

        seteaLeyenda();
        rellenaTipos();
        llenaClientes();
        llenaTarjetas();
        llenaTransacciones();
    }

  
    private void rellenaTipos() 
    {
        cbTipoPas.removeAllItems();
        cbTipoPas.addItem(""); 
        cbTipoPas.addItem("Adulto"); cbTipoPas.addItem("Niño"); cbTipoPas.addItem("Estudiante"); cbTipoPas.addItem("Adulto Mayor"); cbTipoPas.addItem("Discapacitado");
    }
    
    private void seteaLeyenda()
    {
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER); // Centrado horizontalmente
        jLabel1.setVerticalAlignment(SwingConstants.CENTER);   // Centrado verticalmente
        jLabel1.setText("<html><body style='text-align:center;'>Al seleccionar una transacción y un tipo de pasajero, "
                + "se obtienen el total de boletos con ese tipo en la transacción </body></html>");

    }

    private void llenaClientes() 
    {
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
                _clientes.add(nuevoCliente); 
            }
            for (ClienteAux cliente : _clientes) 
            {
                var clienteConcatenado = cliente.getNomCliente() + " {" + cliente.getIdCliente() + "}";
                cliente.setClienteConcatenado(clienteConcatenado);  
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar los clientes","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }
    
    private void llenaTarjetas() 
    {
        _tarjetas.clear();
        
        String sentenciaSQL = "SELECT * FROM InfoCliente.TarjetaCliente";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                var nuevaTarjeta = new TarjetaClienteAux();
                nuevaTarjeta.setIdTarjeta(result.getLong("IdTarjeta"));
                nuevaTarjeta.setIdCliente(result.getLong("IdCliente"));
                nuevaTarjeta.setBanco(result.getString("Banco"));
                nuevaTarjeta.setNumTarjeta(result.getLong("NumTarjeta"));

                _tarjetas.add(nuevaTarjeta); 
            }
            for (TarjetaClienteAux tarjeta : _tarjetas)
            {
                for (ClienteAux cliente : _clientes)
                {
                    if (tarjeta.getIdCliente() == cliente.getIdCliente())
                    {
                        tarjeta.setNomCliente(cliente.getNomCliente()); 
                        break;
                    }
                }
            }
            for (TarjetaClienteAux tarjeta : _tarjetas)
            {
                var numTarjeta = tarjeta.getNumTarjeta()+"";
                var tamano = numTarjeta.length();

                tarjeta.setTarjetaConcatenada("*" +  numTarjeta.charAt(tamano-3) + numTarjeta.charAt(tamano-2) + numTarjeta.charAt(tamano-1) +" (" + tarjeta.getBanco() + ") " + "- " + tarjeta.getNomCliente());
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar las Tarjetas","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }
    
    private void llenaTransacciones() 
    {
        _transacciones.clear();
        String sentenciaSQL = "SELECT * FROM Ventas.Transaccion";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                 var nuevaTrans = new TransaccionAux();
                nuevaTrans.setIdTransaccion(result.getInt("IdTransaccion"));
                nuevaTrans.setIdTarjeta(result.getInt("IdTarjeta"));
                nuevaTrans.setFecha(result.getDate("FechaTransaccion"));
                nuevaTrans.setIdSalida(result.getInt("IdSalida"));
                _transacciones.add(nuevaTrans); 
                
            }
            ConcatenaTransaccion();
        }
        catch(Exception ex)
        {
           JOptionPane.showMessageDialog(null, "Error al recuperar datos","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }

    private void ConcatenaTransaccion() 
    {
        cbTransaccion.removeAllItems();
        cbTransaccion.addItem("");
        cbTransaccion.setSelectedIndex(0);
        for(var transaccion : _transacciones)
        {
            for(var tarjeta : _tarjetas)
            {
                if (transaccion.getIdTarjeta() == tarjeta.getIdTarjeta())
                {
                    transaccion.setTransConcatenada(
                        transaccion.getIdTransaccion() + ", " +
                        tarjeta.getTarjetaConcatenada() + ", " +
                        transaccion.getFecha().toString()
                    );                        
                }
            }
            cbTransaccion.addItem(transaccion.getTransConcatenada());
        }
    }

    private void consultaDatos()
    {
         tablaRep.setRowCount(0);
        
        String sentenciaSQL = 
        "SELECT p.TipoPasajero, COUNT(b.IdBoleto) AS TotalBoletos FROM Ventas.Boleto b INNER JOIN Pasajero p ON b.IdPasajero = p.IdPasajero WHERE b.IdTransaccion = ? AND p.TipoPasajero = ? GROUP BY p.TipoPasajero;";

                                    
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);

            statement.setLong(1, buscaId((String)cbTransaccion.getSelectedItem(), _transacciones));
            statement.setString(2,(String)cbTipoPas.getSelectedItem());

            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                
                tablaRep.addRow(
                        new Object[]
                        {                                                     
                            result.getString("tipopasajero"),
                            result.getLong("totalboletos")
                        });
                
            }
        }
        catch(Exception ex)
        {
           JOptionPane.showMessageDialog(null, "Error al recuperar datos","Error", JOptionPane.ERROR_MESSAGE);            
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cbTransaccion = new javax.swing.JComboBox<>();
        cbTipoPas = new javax.swing.JComboBox<>();
        btnGenerar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaReporte = new javax.swing.JTable();

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
        setTitle("Reporte de Tipos");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Reporte Tipos de Pasajero por Transacción");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Al seleccionar una transacción y un tipo de pasajero, se obtienen el total de pasajeros de ese tipo en la transacción ");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Tipo de Pasajero");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Transacción");

        cbTransaccion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbTipoPas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnGenerar.setText("Generar");
        btnGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarActionPerformed(evt);
            }
        });

        tablaReporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Tipo de Pasjero", "Total de Boletos"
            }
        ));
        jScrollPane2.setViewportView(tablaReporte);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(28, 28, 28)
                        .addComponent(cbTipoPas, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbTransaccion, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cbTipoPas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(btnGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbTransaccion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGenerarActionPerformed(java.awt.event.ActionEvent evt) 
    {
        consultaDatos();
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
            java.util.logging.Logger.getLogger(ConsultaVenTipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConsultaVenTipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConsultaVenTipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConsultaVenTipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConsultaVenTipo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenerar;
    private javax.swing.JComboBox<String> cbTipoPas;
    private javax.swing.JComboBox<String> cbTransaccion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tablaReporte;
    // End of variables declaration//GEN-END:variables
}
