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
import clasesAuxiliares.OperadorAux;
import clasesAuxiliares.SalidaAux;
import clasesAuxiliares.TerminalAux;
import clasesAuxiliares.TransporteAux;
import terminalterrestre.Estacion.IIdentificable;


public class Salida extends javax.swing.JFrame {

    long _idSalida;
    long _idTransporte;
    long _idOperador;
    long _idItinerario;
    float _precio;
    Connection _conexion;
    DefaultTableModel _tablaSal;
    List<TransporteAux> _transportes;
    List<OperadorAux> _operadores;
    List<ItinerarioAux> _itinerarios;
    List<TerminalAux> _terminales;
    List<SalidaAux> _salidas;
    String USUARIO;

    public Salida(String USUARIO) 
    {
        initComponents();
        _tablaSal = (DefaultTableModel)tablaSalida.getModel();
        _transportes = new ArrayList<TransporteAux>();
        _operadores = new ArrayList<OperadorAux>();
        _itinerarios = new ArrayList<ItinerarioAux>();
        _terminales = new ArrayList<TerminalAux>();
        _salidas = new ArrayList<SalidaAux>();

        llenaTransportes();
        llenaOperadores();
        llenaTerminales();
        llenaItinerarios();
        llenaItinerariosConcatenados();
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
        cbTransporte.removeAllItems();
        cbTransporte.addItem("");    

        
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
                cbTransporte.addItem(transCon.toString());    
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar las Ciudades","Error", JOptionPane.ERROR_MESSAGE);            
        } 
    }

    private void llenaOperadores() 
    {
        _operadores.clear();
        cbOperador.removeAllItems();
        cbOperador.addItem("");    
                
        String sentenciaSQL = "SELECT * FROM Operador";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                var nuevoOp = new OperadorAux();
                nuevoOp.setIdOperador(result.getInt("IdOperador"));
                nuevoOp.setNombre(result.getString("NomOpe"));
                nuevoOp.setRFC(result.getString("RFC"));

                _operadores.add(nuevoOp); 
            }
            for (OperadorAux op : _operadores) 
            {
                String RFC = op.getRFC();
                var opConcatenad = op.getNombre() + " (" + RFC.charAt(4) + RFC.charAt(5) + ") ";
                op.setOperadorConcatenado(opConcatenad.toString());
                cbOperador.addItem(opConcatenad.toString());    
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

        cbItinerario.removeAllItems();
        cbItinerario.addItem("");  

        for (ItinerarioAux itinerario : _itinerarios) 
        {
            itinerario.setItinerarioConcatenado(
                itinerario.getNomTerSal() + " - " +
                itinerario.getNomTerLleg() + ", " +
                itinerario.getDia() + ", " +
                itinerario.getHoraSalida() + ", " +
                itinerario.getHoraLlegada()
                );
            cbItinerario.addItem(itinerario.getItinerarioConcatenado()); 
        } 
    }
   
    private void consultaDatos() 
    {
        _tablaSal.setRowCount(0);
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



                _tablaSal.addRow(
                        new Object[]
                        {
                            result.getInt("IdSalida"),
                            result.getInt("IdTransporte"),
                            result.getInt("IdOperador"),
                            result.getInt("IdItinerario"),                
                            result.getFloat("PrecioSalida"),
                        });
                
                _salidas.add(nuevaSalida); 
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
        for (int i = 0; i < _tablaSal.getRowCount(); i++) 
        {
            for (TransporteAux transporte : _transportes) 
            {
                if (transporte.getIdTransporte() == (int)_tablaSal.getValueAt(i, 1)) 
                {
                    _tablaSal.setValueAt(transporte.getTransporteConcatenado(), i, 1);
                    break;                
                }
            }
            for (OperadorAux op : _operadores) 
            {
                if (op.getIdOperador() == (int)_tablaSal.getValueAt(i, 2)) 
                {
                    _tablaSal.setValueAt(op.getOperadorConcatenado(), i, 2);
                    break;                
                }
            }
            for (ItinerarioAux iti : _itinerarios) 
            {
                if (iti.getIdItinerario() == (int)_tablaSal.getValueAt(i, 3)) 
                {
                    _tablaSal.setValueAt(iti.getItinerarioConcatenado(), i, 3);
                    break;                
                }
            }
        }
    }

    private void insertaDato()
    {
        String sentenciaSQL = "INSERT INTO Ventas.Salida (IdTransporte, IdOperador, IdItinerario, PrecioSalida) VALUES (?, ?, ?, ?)";
        
        try
        {
           var nuevaSalida = new SalidaAux();                
                nuevaSalida.setIdTransporte(_idTransporte);
                nuevaSalida.setIdOperador(_idOperador);
                nuevaSalida.setIdItinerario(_idItinerario);                
                nuevaSalida.setPrecioSalida(_precio);;
           
            revisaRepetidos(nuevaSalida);
            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL); 
            
            statement.setLong(1, _idTransporte);
            statement.setLong(2,_idOperador);
            statement.setLong(3,_idItinerario);
            statement.setFloat(4,_precio);

            statement.executeUpdate();         
           
            consultaDatos();
            
            txbPrecio.setText("");
            cbItinerario.setSelectedIndex(0);
            cbOperador.setSelectedIndex(0);
            cbTransporte.setSelectedIndex(0); 
          
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al insertar","Error", JOptionPane.ERROR_MESSAGE); 
            txbPrecio.setText("");
            cbItinerario.setSelectedIndex(0);
            cbOperador.setSelectedIndex(0);
            cbTransporte.setSelectedIndex(0);         
   
        }
    }

    private void eliminaDato() 
    {
        String sentenciaSQL = "DELETE FROM Ventas.Salida WHERE IdSalida = ?";
        
        try
        {            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);            
                
            statement.setLong(1,_idSalida);
            statement.executeUpdate();
            
            consultaDatos();
           
            txbPrecio.setText("");
            cbItinerario.setSelectedIndex(0);
            cbOperador.setSelectedIndex(0);
            cbTransporte.setSelectedIndex(0); 
           
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al eliminar ","Error", JOptionPane.ERROR_MESSAGE); 
            txbPrecio.setText("");
            cbItinerario.setSelectedIndex(0);
            cbOperador.setSelectedIndex(0);
            cbTransporte.setSelectedIndex(0); 
        }
    }

    private void modificaDato()
    {
         String sentenciaSQL = "UPDATE Ventas.Salida SET IdTransporte = ?, IdOperador = ?, IdItinerario = ?, PrecioSalida = ? WHERE IdSalida = ?";
        
        try
        {          
             var nuevaSalida = new SalidaAux();                
                nuevaSalida.setIdTransporte(_idTransporte);
                nuevaSalida.setIdOperador(_idOperador);
                nuevaSalida.setIdItinerario(_idItinerario);                
                nuevaSalida.setPrecioSalida(_precio);;
           
            revisaRepetidos(nuevaSalida);

            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);           
            
           statement.setLong(1, _idTransporte);
            statement.setLong(2,_idOperador);
            statement.setLong(3,_idItinerario);
            statement.setFloat(4,_precio);
            statement.setLong(5,_idSalida);




           statement.executeUpdate();
           
           consultaDatos();
           
            txbPrecio.setText("");
            cbItinerario.setSelectedIndex(0);
            cbOperador.setSelectedIndex(0);
            cbTransporte.setSelectedIndex(0);         
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al modificar ","Error", JOptionPane.ERROR_MESSAGE); 
            txbPrecio.setText("");
            cbItinerario.setSelectedIndex(0);
            cbOperador.setSelectedIndex(0);
            cbTransporte.setSelectedIndex(0);       
        }

    }

    private void revisaRepetidos(SalidaAux nuevaSalida) throws Exception
    {
        for (SalidaAux salida : _salidas) 
        {
            if (salida.getPrecioSalida() == nuevaSalida.getPrecioSalida())
            {
                if (salida.getIdItinerario() == nuevaSalida.getIdItinerario() && salida.getIdOperador() == nuevaSalida.getIdOperador())
                {
                    JOptionPane.showMessageDialog(null, "Error Operador no disponible","Error", JOptionPane.ERROR_MESSAGE);
                    Exception exception = new IllegalArgumentException();           
                    throw exception;
                }
                if (salida.getIdItinerario() == nuevaSalida.getIdItinerario() && salida.getIdTransporte() == nuevaSalida.getIdTransporte())
                {
                    JOptionPane.showMessageDialog(null, "Error Transporte no disponible","Error", JOptionPane.ERROR_MESSAGE);
                    Exception exception = new IllegalArgumentException();           
                    throw exception;
                }
                if (salida.getIdItinerario() == nuevaSalida.getIdItinerario() && salida.getIdTransporte() == nuevaSalida.getIdTransporte())
                {
                    JOptionPane.showMessageDialog(null, "Error Transporte no disponible","Error", JOptionPane.ERROR_MESSAGE);
                    Exception exception = new IllegalArgumentException();           
                    throw exception;
                }
            }
        }
        for (SalidaAux salida : _salidas) 
        {
            if (salida.getIdItinerario() == nuevaSalida.getIdItinerario() && salida.getIdTransporte() == nuevaSalida.getIdTransporte())
            {
                JOptionPane.showMessageDialog(null, "Error tupla Repetida","Error", JOptionPane.ERROR_MESSAGE);
                Exception exception = new IllegalArgumentException();           
                throw exception;
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

        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbItinerario = new javax.swing.JComboBox<>();
        cbOperador = new javax.swing.JComboBox<>();
        cbTransporte = new javax.swing.JComboBox<>();
        txbPrecio = new javax.swing.JTextField();
        btnModificar = new javax.swing.JButton();
        btnAgregar2 = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaSalida = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Salida");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Transporte");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Operador");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Itinerario");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Precio de Salida");

        cbItinerario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbOperador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbTransporte.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

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

        tablaSalida.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "id de Salida", "Transporte", "Operador", "Itinerario", "Precio de Salida"
            }
        ));
        tablaSalida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaSalidaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaSalida);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbOperador, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTransporte, 0, 161, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbItinerario, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txbPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnAgregar2)
                .addGap(18, 18, 18)
                .addComponent(btnModificar)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addGap(164, 164, 164))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(cbItinerario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTransporte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(cbOperador, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txbPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar)
                    .addComponent(btnModificar)
                    .addComponent(btnAgregar2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt)
    {
        _idTransporte = buscaId((String)cbTransporte.getSelectedItem(), _transportes);
        _idOperador = buscaId((String)cbOperador.getSelectedItem(), _operadores);
        _idItinerario = buscaId((String)cbItinerario.getSelectedItem(), _itinerarios);        
        _precio = Float.parseFloat(txbPrecio.getText());

        if (_precio == 0 || _idTransporte == -1 || _idOperador == -1 || _idItinerario == -1) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }       
        else
        {
            modificaDato();
        } 
    }

    private void btnAgregar2ActionPerformed(java.awt.event.ActionEvent evt) 
    {
        _idTransporte = buscaId((String)cbTransporte.getSelectedItem(), _transportes);
        _idOperador = buscaId((String)cbOperador.getSelectedItem(), _operadores);
        _idItinerario = buscaId((String)cbItinerario.getSelectedItem(), _itinerarios);        
        _precio = Float.parseFloat(txbPrecio.getText());

        if (_precio == 0 || _idTransporte == -1 || _idOperador == -1 || _idItinerario == -1) 
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
        if (_precio == 0 || _idTransporte == -1 || _idOperador == -1 || _idItinerario == -1) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }       
        else
        {
             eliminaDato();
        }
        
    }

    private void tablaSalidaMouseClicked(java.awt.event.MouseEvent evt)
    {
        int row =-1 ;
        row = tablaSalida.rowAtPoint(evt.getPoint());

        _idSalida = (int)tablaSalida.getValueAt(row, 0);
        _idTransporte = buscaId((String)tablaSalida.getValueAt(row, 1), _transportes);
        _idOperador = buscaId((String)tablaSalida.getValueAt(row, 2), _operadores);
        _idItinerario = buscaId((String)tablaSalida.getValueAt(row, 3), _itinerarios);
        _precio = (float)tablaSalida.getValueAt(row, 4);

        cbTransporte.setSelectedItem(tablaSalida.getValueAt(row, 1));
        cbOperador.setSelectedItem(tablaSalida.getValueAt(row, 2));
        cbItinerario.setSelectedItem(tablaSalida.getValueAt(row, 3));
        txbPrecio.setText(_precio +"");
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
            java.util.logging.Logger.getLogger(Salida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Salida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Salida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Salida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Salida("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar2;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cbItinerario;
    private javax.swing.JComboBox<String> cbOperador;
    private javax.swing.JComboBox<String> cbTransporte;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaSalida;
    private javax.swing.JTextField txbPrecio;
    // End of variables declaration//GEN-END:variables
}
