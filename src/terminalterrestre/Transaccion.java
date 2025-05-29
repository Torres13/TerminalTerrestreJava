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
import clasesAuxiliares.ItinerarioAux;
import clasesAuxiliares.SalidaAux;
import clasesAuxiliares.TarjetaClienteAux;
import clasesAuxiliares.TerminalAux;
import clasesAuxiliares.TransporteAux;
import terminalterrestre.Estacion.IIdentificable;

public class Transaccion extends javax.swing.JFrame {

    long _idTransaccion;
    long _idSalida;
    long _idTarjeta;
    Connection _conexion;
    DefaultTableModel _tablaTransac;
    List<SalidaAux> _salidas;
    List<TarjetaClienteAux> _tarjetas;
    List<TransporteAux> _transportes;
    List<ItinerarioAux> _itinerarios;
    List<ClienteAux> _clientes;
    List<TerminalAux> _terminales;
    String USUARIO;

    public Transaccion(String USUARIO) 
    {
        initComponents();
        _tablaTransac = (DefaultTableModel)tablaTransaccion.getModel();
        _salidas = new ArrayList<SalidaAux>();
        _tarjetas = new ArrayList<TarjetaClienteAux>();
        _transportes = new ArrayList<TransporteAux>();
        _itinerarios = new ArrayList<ItinerarioAux>();
        _terminales = new ArrayList<TerminalAux>();
        _clientes = new ArrayList<ClienteAux>();
        llenaTransportes();
        llenaTerminales();
        llenaItinerarios();
        llenaItinerariosConcatenados();
        llenaSalidas();
        llenaClientes();
        llenaTarjetas();
        consultaDatos();
        this.USUARIO = USUARIO;
        seteaTabla();
    }
    
    private void seteaTabla()
    {
        if (USUARIO.equals("Vendedor")) {
            btnEliminar.setEnabled(false);
        }
    }

    private void llenaTransportes() 
    {
        _transportes.clear();
       
        
        String sentenciaSQL = "SELECT * FROM Transporte";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                var nuevoTrans = new TransporteAux();
                nuevoTrans.setIdTransporte(result.getInt("IdTransporte"));
                nuevoTrans.setTipo(result.getString("TipoTransporte"));
                nuevoTrans.setMatricula(result.getString("Matricula"));
                nuevoTrans.setModelo(result.getString("Modelo"));

                _transportes.add(nuevoTrans); 
            }
            for (TransporteAux trans : _transportes) 
            {
                var transCon = trans.getTipo() + " [" + trans.getMatricula() + ", " + trans.getModelo()+ "]";
                trans.setTransporteConcatenado(transCon.toString());
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar las Ciudades","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }

    private void llenaItinerarios() 
    {
         _itinerarios.clear();
        
        String sentenciaSQL = "SELECT * FROM Destinos.Itinerario";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                var nuevoIti = new ItinerarioAux();                
                nuevoIti.setIdItinerario(result.getInt("IdItinerario"));
                nuevoIti.setIdTerSal(result.getInt("IdSalida"));
                nuevoIti.setIdTerLleg(result.getInt("IdLlegada"));
                nuevoIti.setDia(result.getString("Dias"));
                nuevoIti.setHoraSalida(result.getString("HoraSalida"));
                nuevoIti.setHoraLlegada(result.getString("HoraLlegada"));
                nuevoIti.setKilometros(result.getInt("KMs"));

                
                _itinerarios.add(nuevoIti); 
            }
            llenaItinerariosConcatenados();
        }
        catch(Exception ex)
        {
           JOptionPane.showMessageDialog(null, "Error al recuperar datos","Error", JOptionPane.ERROR_MESSAGE);            
        } 
        
    }  

    private void llenaTerminales() 
    {
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

                _terminales.add(nuevaTerminal);
            }          
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar las Terminales","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }
   
    private void llenaItinerariosConcatenados() 
    {
        for (ItinerarioAux itinerario : _itinerarios) 
        {
            for (TerminalAux terminal : _terminales) 
            {
                if (itinerario.getIdTerLleg() == terminal.getIdTerminal()) 
                    itinerario.setNomTerLleg(terminal.getNomTerminal());

                if (itinerario.getIdTerSal() == terminal.getIdTerminal()) 
                    itinerario.setNomTerSal(terminal.getNomTerminal());
            }
        } 

        for (ItinerarioAux itinerario : _itinerarios) 
        {
            itinerario.setItinerarioConcatenado(
                itinerario.getNomTerSal() + " - " +
                itinerario.getNomTerLleg() + ", " +
                itinerario.getDia() + ", " +
                itinerario.getHoraSalida() + ", " +
                itinerario.getHoraLlegada()
                );
        } 
    }
   
    private void llenaSalidas() 
    {
        _salidas.clear();
        
        String sentenciaSQL = "SELECT * FROM Ventas.Salida";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                var nuevaSalida = new SalidaAux();                
                nuevaSalida.setIdSalida(result.getInt("IdSalida"));
                nuevaSalida.setIdTransporte(result.getInt("IdTransporte"));
                nuevaSalida.setIdOperador(result.getInt("IdOperador"));
                nuevaSalida.setIdItinerario(result.getInt("IdItinerario"));                
                nuevaSalida.setPrecioSalida(result.getFloat("PrecioSalida"));                
                
                _salidas.add(nuevaSalida); 
            }
            concatenaSalidas();
        }
        catch(Exception ex)
        {
           JOptionPane.showMessageDialog(null, "Error al recuperar Salidas","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }

    private void concatenaSalidas() 
    {
        cbSalida.removeAllItems();
        cbSalida.addItem("");
        cbSalida.setSelectedIndex(0);
        for (var salida : _salidas)
        {
            for (var itinerario : _itinerarios)
            {
                if (salida.getIdItinerario() == itinerario.getIdItinerario())
                {
                    salida.setSalidaConcatenada(itinerario.getItinerarioConcatenado());
                    break;
                }
            }

            for(var transporte : _transportes)
            {
                if (salida.getIdTransporte() == transporte.getIdTransporte())
                {
                    var concatenada = salida.getSalidaConcatenada() + " - " + transporte.getTransporteConcatenado();
                    salida.setSalidaConcatenada(concatenada) ;
                    break;
                }                    
            }
            cbSalida.addItem(salida.getSalidaConcatenada());
        }
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
            concatenaTarjetas();
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar las Tarjetas","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }
    
    private void concatenaTarjetas() 
    {
        try
        {
            cbTarjeta.removeAllItems();
            cbTarjeta.addItem("");
            cbTarjeta.setSelectedIndex(0);
            for (TarjetaClienteAux tarjeta : _tarjetas)
            {
                var numTarjeta = tarjeta.getNumTarjeta()+"";
                var tamano = numTarjeta.length();

                tarjeta.setTarjetaConcatenada("*" +  numTarjeta.charAt(tamano-3) + numTarjeta.charAt(tamano-2) + numTarjeta.charAt(tamano-1) +" (" + tarjeta.getBanco() + ") " + "- " + tarjeta.getNomCliente());
                cbTarjeta.addItem(tarjeta.getTarjetaConcatenada());   
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al concatenar las Tarjetas","Error", JOptionPane.ERROR_MESSAGE);            
        } 
        
    }

    private void consultaDatos() 
    {
        _tablaTransac.setRowCount(0);
        
        String sentenciaSQL = "SELECT * FROM Ventas.Transaccion";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                _tablaTransac.addRow(
                        new Object[]
                        {
                            result.getInt("IdTransaccion"),
                            result.getInt("IdSalida"),
                            result.getInt("IdTarjeta"),
                            result.getDate("FechaTransaccion"),                
                            result.getFloat("Total"),
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
        for (int i = 0; i < _tablaTransac.getRowCount(); i++) 
        {
            for (SalidaAux salida : _salidas) 
            {
                if (salida.getIdSalida() == (int)_tablaTransac.getValueAt(i, 1)) 
                {
                    _tablaTransac.setValueAt(salida.getSalidaConcatenada(), i, 1);
                    break;                
                }
            }
            for (TarjetaClienteAux tarjeta : _tarjetas) 
            {
                if (tarjeta.getIdTarjeta() == (int)_tablaTransac.getValueAt(i, 2)) 
                {
                    _tablaTransac.setValueAt(tarjeta.getTarjetaConcatenada(), i, 2);
                    break;                
                }
            }
            
        }
    }

    private void insertaDato()
    {
        String sentenciaSQL = "INSERT INTO Ventas.Transaccion (IdSalida, IdTarjeta, Total) VALUES (?, ?, ?)";
        
        try
        {            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL); 
            
            statement.setLong(1, _idSalida);
            statement.setLong(2,_idTarjeta);
            statement.setLong(3,0);

            statement.executeUpdate();         
           
            consultaDatos();
            
            cbSalida.setSelectedIndex(0);
            cbTarjeta.setSelectedIndex(0);
          
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al insertar","Error", JOptionPane.ERROR_MESSAGE); 
            cbSalida.setSelectedIndex(0);
            cbTarjeta.setSelectedIndex(0);        
        }
    }

    private void eliminaDato() 
    {
        String sentenciaSQL = "DELETE FROM Ventas.Transaccion WHERE IdTransaccion = ?";
        
        try
        {            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);            
                
            statement.setLong(1,_idTransaccion);
            statement.executeUpdate();
            
            consultaDatos();
           
            cbSalida.setSelectedIndex(0);
            cbTarjeta.setSelectedIndex(0); 
           
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al eliminar ","Error", JOptionPane.ERROR_MESSAGE); 
            cbSalida.setSelectedIndex(0);
            cbTarjeta.setSelectedIndex(0); 
        }
    }

    private void modificaDato()
    {
         String sentenciaSQL = "UPDATE Ventas.Transaccion SET IdSalida = ?, IdTarjeta = ? WHERE IdTransaccion = ?";
        
        try
        {          

            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);           
            
            statement.setLong(1, _idSalida);
            statement.setLong(2,_idTarjeta);
            statement.setLong(3,_idTransaccion);

           statement.executeUpdate();
           
           consultaDatos();
           
            cbSalida.setSelectedIndex(0);
            cbTarjeta.setSelectedIndex(0);          
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al modificar ","Error", JOptionPane.ERROR_MESSAGE); 
            cbSalida.setSelectedIndex(0);
            cbTarjeta.setSelectedIndex(0);       
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

        cbTarjeta = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnAgregar2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnModificar = new javax.swing.JButton();
        cbSalida = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTransaccion = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Transacción");

        cbTarjeta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Tarjeta del Cliente");

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

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Salida");

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        cbSalida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tablaTransaccion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id de la Transacción", "Salida", "Tarjeta", "Fecha de la Transacción", "Total"
            }
        ));
        tablaTransaccion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaTransaccionMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaTransaccion);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAgregar2)
                .addGap(18, 18, 18)
                .addComponent(btnModificar)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addGap(223, 223, 223))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(cbTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar)
                    .addComponent(btnModificar)
                    .addComponent(btnAgregar2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt)  
    {
        if ( _idSalida == -1 || _idTarjeta == -1 || _idTransaccion == -1) 
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
        _idSalida = buscaId((String)cbSalida.getSelectedItem(), _salidas);
        _idTarjeta = buscaId((String)cbTarjeta.getSelectedItem(), _tarjetas);

        if ( _idSalida == -1 || _idTarjeta == -1) 
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
        _idSalida = buscaId((String)cbSalida.getSelectedItem(), _salidas);
        _idTarjeta = buscaId((String)cbTarjeta.getSelectedItem(), _tarjetas);

        if ( _idSalida == -1 || _idTarjeta == -1) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }       
        else
        {
             modificaDato();
        }
        
    }

    private void tablaTransaccionMouseClicked(java.awt.event.MouseEvent evt) 
    {
        int row =-1 ;
        row = tablaTransaccion.rowAtPoint(evt.getPoint());

        _idTransaccion = (int)tablaTransaccion.getValueAt(row, 0);
        _idSalida = buscaId((String)tablaTransaccion.getValueAt(row, 1), _salidas);
        _idTarjeta = buscaId((String)tablaTransaccion.getValueAt(row, 2), _tarjetas);
        
        cbSalida.setSelectedItem(tablaTransaccion.getValueAt(row, 1));
        cbTarjeta.setSelectedItem(tablaTransaccion.getValueAt(row, 2));
        
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
            java.util.logging.Logger.getLogger(Transaccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Transaccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Transaccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Transaccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Transaccion("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar2;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cbSalida;
    private javax.swing.JComboBox<String> cbTarjeta;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaTransaccion;
    // End of variables declaration//GEN-END:variables
}
