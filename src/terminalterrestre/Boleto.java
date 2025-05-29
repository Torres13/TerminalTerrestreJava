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

import clasesAuxiliares.AsientoAux;
import clasesAuxiliares.ClienteAux;
import clasesAuxiliares.PasajeroAux;
import clasesAuxiliares.SalidaAux;
import clasesAuxiliares.TarjetaClienteAux;
import clasesAuxiliares.TransaccionAux;
import clasesAuxiliares.TransporteAux;
import terminalterrestre.Estacion.IIdentificable;

public class Boleto extends javax.swing.JFrame {
    private int numAsiento;
    long _idBoleto;
    long _idAsiento;
    long _idTransaccion;
    long _idPasajero;
    float _subtotal;
    boolean _modificando;
    List<SalidaAux> _salidas;
    List<TarjetaClienteAux> _tarjetas;
    List<TransporteAux> _transportes;
    List<ClienteAux> _clientes;
    List<TransaccionAux> _transacciones; 
    List<PasajeroAux> _pasajeros; 
    List<AsientoAux> _asientos; 
    Connection _conexion;
    DefaultTableModel _tablaBol;
    String USUARIO;
    public Boleto(String USUARIO)
    {
        initComponents();
        _salidas = new ArrayList<SalidaAux>();
        _tarjetas = new ArrayList<TarjetaClienteAux>();
        _transportes = new ArrayList<TransporteAux>();
        _clientes = new ArrayList<ClienteAux>();
        _transacciones = new ArrayList<TransaccionAux>();
        _pasajeros = new ArrayList<PasajeroAux>();
        _tablaBol = (DefaultTableModel)tablaBoleto.getModel(); 
        _asientos = new ArrayList<AsientoAux>();
        llenaTransportes();
        llenaSalidas();
        llenaClientes();
        llenaTarjetas();
        llenaTransacciones();
        llenaPasajeros();
        consultaDatos();
        _modificando = false;
        this.USUARIO = USUARIO;
        seteaTabla();
        
    }

    private void seteaTabla()
    {
        if (USUARIO.equals("Vendedor")) {
            btnEliminar.setEnabled(false);
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

    private void llenaPasajeros() 
    {
        _pasajeros.clear();
        cbPasajero.removeAllItems();
        cbPasajero.addItem("");
        cbPasajero.setSelectedIndex(0);
        
        String sentenciaSQL = "SELECT * FROM Pasajero";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                var nuevoPasajero = new PasajeroAux();                
                nuevoPasajero.setIdPasajero(result.getInt("IdPasajero"));
                nuevoPasajero.setNombrePasajero(result.getString("NombrePasajero"));
                nuevoPasajero.setFechaNacimiento(result.getDate("FechaNacimiento"));

                _pasajeros.add(nuevoPasajero); 
            }

            for (PasajeroAux pasajero : _pasajeros) 
            {
                var anio = pasajero.getFechaNacimiento().toString().split("-");
                pasajero.setPasajeroConcatenado(
                    pasajero.getId() + pasajero.getNombrePasajero() + " (" + anio[0] + ") "
                );
                cbPasajero.addItem(pasajero.getPasajeroConcatenado());
            }
        }
        catch(Exception ex)
        {
           JOptionPane.showMessageDialog(null, "Error al recuperar datos","Error", JOptionPane.ERROR_MESSAGE);            
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
        }
        catch(Exception ex)
        {
           JOptionPane.showMessageDialog(null, "Error al recuperar Salidas","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }

    private void llenaAsientos() 
    {
        _asientos.clear();
        
        String sentenciaSQL = "SELECT * FROM Ventas.Asiento";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                var nuevoAsiento = new AsientoAux();                
                nuevoAsiento.setIdAsiento(result.getInt("IdAsiento"));
                nuevoAsiento.setIdSalida(result.getInt("IdSalida"));
                nuevoAsiento.setDisponibilidad(result.getInt("Disponibilidad"));
                nuevoAsiento.setNumAsiento(result.getInt("NumAsiento"));                
                
                _asientos.add(nuevoAsiento); 
            }
        }
        catch(Exception ex)
        {
           JOptionPane.showMessageDialog(null, "Error al recuperar Salidas","Error", JOptionPane.ERROR_MESSAGE);            
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
    
    private String regresaTipoTransporte()
    {
        for (var transa : _transacciones)
        {
            if (transa.getIdTransaccion() == buscaId((String)cbTransaccion.getSelectedItem(), _transacciones)) 
            {
                for (var salida : _salidas)
                {
                    if (salida.getIdSalida() == transa.getIdSalida())
                    {
                        for (var transporte : _transportes)
                        {
                            if (transporte.getIdTransporte() == salida.getIdTransporte())
                            {
                                return transporte.getTipo();
                            }
                        }
                    }
                }
            }
        }

        return "";
    }

    private long regresaIdSalida()
    {
        for (var transa : _transacciones)
        {
            if (transa.getIdTransaccion() == buscaId((String)cbTransaccion.getSelectedItem(), _transacciones)) 
            {
                for (var salida : _salidas)
                {
                    if (salida.getIdSalida() == transa.getIdSalida())
                    {
                        return salida.getIdSalida();
                    }
                }
            }
        }

        return -1;
    }

    public void setNumAsiento(int numAsiento) 
    {
        this.numAsiento = numAsiento;
    }
    
    public void asientoCerrado() 
    {
       labelAsiento.setText(numAsiento+"");
       btnAsiento.setText("Cambiar Asiento");
    }

    private void consultaDatos() 
    {
        
        _tablaBol.setRowCount(0);
        
        String sentenciaSQL = "SELECT * FROM Ventas.Boleto";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {              
                _tablaBol.addRow(
                        new Object[]
                        {
                            result.getLong("IdBoleto"),
                            result.getLong("IdAsiento"),                           
                            result.getLong("IdTransaccion"),
                            result.getLong("IdPasajero"),
                            result.getFloat("SubTotal")
                        });
                
            }
            rellenaConcatenados();
        }
        catch(Exception ex)
        {
           JOptionPane.showMessageDialog(null, "Error al recuperar Boletos","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }

    private void rellenaConcatenados() 
    {
        llenaAsientos();
        try
        {
            for (int i = 0; i < _tablaBol.getRowCount(); i++) 
            {
                for (var asiento : _asientos)
                {
                    if (asiento.getIdAsiento() == (long)_tablaBol.getValueAt(i, 1))
                    {
                        asiento.setAsientoConcatenado(
                            asiento.getIdAsiento() + " - " + "A" + asiento.getNumAsiento()
                            ); 
                        _tablaBol.setValueAt(asiento.getAsientoConcatenado(), i, 1);
                        break;
                    }
                }

                for (var transaccion : _transacciones)
                {
                    if (transaccion.getIdTransaccion() == (long)_tablaBol.getValueAt(i, 2))
                    {
                        _tablaBol.setValueAt(transaccion.getTransConcatenada(), i, 2);
                        break;
                    }
                }                

                for (var pasajero : _pasajeros)
                {
                    if (pasajero.getIdPasajero() ==(long)_tablaBol.getValueAt(i, 3))
                    {
                        _tablaBol.setValueAt(pasajero.getPasajeroConcatenado(), i, 3);
                        break;
                    }
                }
            }
            
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(null, "Error al llenar concatenados ","Error", JOptionPane.ERROR_MESSAGE); 
            
        }

        
        
    }

    private void insertaDato()
    {
        String sentenciaSQL = "INSERT INTO Ventas.Boleto (IdAsiento, IdTransaccion, IdPasajero, SubTotal) VALUES (?, ?, ?, ?)";
        _idTransaccion = buscaId((String)cbTransaccion.getSelectedItem(), _transacciones);
        _idPasajero = buscaId((String)cbPasajero.getSelectedItem(), _pasajeros);
        try
        {            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL); 
            
            statement.setLong(1, _idAsiento);
            statement.setLong(2,_idTransaccion);
            statement.setLong(3,_idPasajero);
            statement.setLong(4,0);


            statement.executeUpdate();         
           
            consultaDatos();
            
            cbPasajero.setSelectedIndex(0);
            cbTransaccion.setSelectedIndex(0);
            labelAsiento.setText("Ninguno");
          
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al insertar Boleto","Error", JOptionPane.ERROR_MESSAGE); 
            cbPasajero.setSelectedIndex(0);
            cbTransaccion.setSelectedIndex(0);
            labelAsiento.setText("Ninguno");        
        }
    }

    private void insertaAsiento()
    {
        String sentenciaSQL = "INSERT INTO Ventas.Asiento (IdSalida, Disponibilidad, NumAsiento) VALUES (?, ?, ?)";
        
        try
        {            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL); 
            
            statement.setLong(1, regresaIdSalida());
            statement.setLong(2,1);
            statement.setLong(3,numAsiento);


            statement.executeUpdate();         
            
            llenaAsientos();
            for(var asiento : _asientos)
            {
                if (asiento.getIdSalida() == regresaIdSalida() && numAsiento == asiento.getNumAsiento())
                {
                    _idAsiento = asiento.getIdAsiento();

                }
            }
          
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al insertar Asiento","Error", JOptionPane.ERROR_MESSAGE); 
                   
        }
    }
    
    private void eliminaDato() 
    {
        String sentenciaSQL = "DELETE FROM Ventas.Boleto WHERE IdBoleto = ?";
        
        try
        {            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);            
                
            statement.setLong(1,_idBoleto);
            statement.executeUpdate();
            
            consultaDatos();
           
            cbPasajero.setSelectedIndex(0);
            cbTransaccion.setSelectedIndex(0);
            labelAsiento.setText("Ninguno"); 
           
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al eliminar Boleto","Error", JOptionPane.ERROR_MESSAGE); 
            cbPasajero.setSelectedIndex(0);
            cbTransaccion.setSelectedIndex(0);
            labelAsiento.setText("Ninguno"); 
        }
    }

    private void modificaDato()
    {
         String sentenciaSQL = "UPDATE Ventas.Boleto SET IdAsiento = ?, IdTransaccion = ?,IdPasajero = ?  WHERE IdBoleto = ?";
        _idTransaccion = buscaId((String)cbTransaccion.getSelectedItem(), _transacciones);
        _idPasajero = buscaId((String)cbPasajero.getSelectedItem(), _pasajeros);

        try
        {          

            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);           
            
            statement.setLong(1, _idAsiento);
            statement.setLong(2,_idTransaccion);
            statement.setLong(3,_idPasajero);
            statement.setLong(4,_idBoleto);

           statement.executeUpdate();
           
           consultaDatos();
           
            cbPasajero.setSelectedIndex(0);
            cbTransaccion.setSelectedIndex(0);
            labelAsiento.setText("Ninguno");  
            
            
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al modificar Boleto","Error", JOptionPane.ERROR_MESSAGE); 
            cbPasajero.setSelectedIndex(0);
            cbTransaccion.setSelectedIndex(0);
            labelAsiento.setText("Ninguno");      
        }
    }

    private void eliminaAsiento() 
    {
        String sentenciaSQL = "DELETE FROM Ventas.Asiento WHERE IdAsiento = ?";
        
        try
        {            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);            
                
            statement.setLong(1,_idAsiento);
            statement.executeUpdate();
           
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al eliminar Boleto","Error", JOptionPane.ERROR_MESSAGE); 
        }
    }

    private void modificaAsiento()
    {
         String sentenciaSQL = "UPDATE Ventas.Asiento SET NumAsiento = ?, IdSalida = ?  WHERE IdAsiento = ?";
        
        try
        {          

            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);           
            
            statement.setInt(1,Integer.parseInt(labelAsiento.getText()));
            statement.setLong(2,regresaIdSalida());
            statement.setLong(3, _idAsiento);

           statement.executeUpdate();          
                 
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al modificar Boleto","Error", JOptionPane.ERROR_MESSAGE);      
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

        cbPasajero = new javax.swing.JComboBox<>();
        labelAsiento = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnAgregar2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnModificar = new javax.swing.JButton();
        cbTransaccion = new javax.swing.JComboBox<>();
        btnAsiento = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaBoleto = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Boleto");

        cbPasajero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        labelAsiento.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        labelAsiento.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelAsiento.setText("(Ninguno)");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Asiento:");

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
        jLabel3.setText("Transacción");

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        cbTransaccion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnAsiento.setText("Selecciona Asiento");
        btnAsiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsientoActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Pasajero");

        tablaBoleto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "id del Boleto", "Asiento", "Transacción", "Pasajero", "Subtotal"
            }
        ));
        tablaBoleto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaBoletoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaBoleto);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbTransaccion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbPasajero, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnAgregar2)
                                .addGap(18, 18, 18)
                                .addComponent(btnModificar)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminar)
                                .addGap(134, 134, 134))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnAsiento, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelAsiento)
                                .addGap(120, 120, 120))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbPasajero, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTransaccion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAsiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5)
                    .addComponent(labelAsiento))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar)
                    .addComponent(btnModificar)
                    .addComponent(btnAgregar2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) 
    {
        eliminaDato();
        eliminaAsiento();
        consultaDatos();
        _modificando = false;
        btnAsiento.setText("Seleccionar Asiento"); 
    }

    private void btnAgregar2ActionPerformed(java.awt.event.ActionEvent evt)  
    {
        insertaAsiento();
        insertaDato();
        consultaDatos();
        btnAsiento.setText("Seleccionar Asiento"); 
    }

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) 
    {
        modificaAsiento();
        modificaDato();
        consultaDatos();
        _modificando = false;
        btnAsiento.setText("Seleccionar Asiento");
    }

    private void btnAsientoActionPerformed(java.awt.event.ActionEvent evt)
    {
        if (regresaTipoTransporte().equals("Autobús"))
        {
            if (_modificando)
            {
                
                new Asiento10(this,regresaIdSalida(),numAsiento).setVisible(true);
            }
            else
            {
                new Asiento10(this,regresaIdSalida(),0).setVisible(true);
            }
        } 
        if (regresaTipoTransporte().equals("Van"))
        {
            if (_modificando)
            {
                
                new Asiento6(this,regresaIdSalida(),numAsiento).setVisible(true);
            }
            else
            {
                new Asiento6(this,regresaIdSalida(),0).setVisible(true);
            }
        }        
    }

    private void tablaBoletoMouseClicked(java.awt.event.MouseEvent evt) 
    {
        _modificando = true;
        int row =-1 ;
        row = tablaBoleto.rowAtPoint(evt.getPoint());

        _idBoleto = (long)tablaBoleto.getValueAt(row, 0);
        _idAsiento = buscaId((String)tablaBoleto.getValueAt(row, 1), _asientos);
        _idTransaccion = buscaId((String)tablaBoleto.getValueAt(row, 2), _transacciones);
        _idPasajero = buscaId((String)tablaBoleto.getValueAt(row, 3), _pasajeros);
        
        cbTransaccion.setSelectedItem(tablaBoleto.getValueAt(row, 2));
        cbPasajero.setSelectedItem(tablaBoleto.getValueAt(row, 3));

        for(var asiento : _asientos)            
        if (asiento.getIdAsiento() == _idAsiento)
        {
            numAsiento = asiento.getNumAsiento();
            labelAsiento.setText(numAsiento+"");
        }
        btnAsiento.setText("Cambiar Asiento");
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
            java.util.logging.Logger.getLogger(Boleto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Boleto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Boleto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Boleto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Boleto("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar2;
    private javax.swing.JButton btnAsiento;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cbPasajero;
    private javax.swing.JComboBox<String> cbTransaccion;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelAsiento;
    private javax.swing.JTable tablaBoleto;
    // End of variables declaration//GEN-END:variables
}
