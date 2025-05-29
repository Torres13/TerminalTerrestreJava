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

import clasesAuxiliares.ItinerarioAux;
import clasesAuxiliares.TerminalAux;
import terminalterrestre.Estacion.IIdentificable;

public class Itinerario extends javax.swing.JFrame {

    long _idItinerario;
    long _idSalida;
    long _idLlegada;
    String _dias;
    String _horaSalida;
    String _horaLlegada;
    Integer _kms;
    Connection _conexion;
    DefaultTableModel _tablaIti;
    List<TerminalAux> _terminales;
    List<ItinerarioAux> _itinierarios;
    String USUARIO;
    
    public Itinerario(String USUARIO) {

        initComponents();
        _tablaIti = (DefaultTableModel) tablaItinerario.getModel();
        _terminales = new ArrayList<TerminalAux>();
        _itinierarios = new ArrayList<ItinerarioAux>(); 
        llenaListas();
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

    private void llenaListas() 
    {
        llenaTerminales();
        llenaEstaciones();
        cbDestino.removeAllItems(); cbDestino.addItem("");        
        cbOrigen.removeAllItems(); cbOrigen.addItem("");        
        try
        {
            for (TerminalAux terminal : _terminales) 
            {
                terminal.setTerminalConcatenada(
                    terminal.getIdTerminal() + "-" + terminal.getNomTerminal() + " (" + terminal.getNomEstacion() + ") "
                );
                cbDestino.addItem(terminal.getTerminalConcatenada());  
                cbOrigen.addItem(terminal.getTerminalConcatenada());  
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al concatenar","Error", JOptionPane.ERROR_MESSAGE);            
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

    private void llenaEstaciones()
    {
       List<TerminalAux> _estaciones = new  ArrayList<TerminalAux>();; 
        String sentenciaSQL = "SELECT * FROM Destinos.Estacion";
        
        try
        {
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);
            ResultSet result = statement.executeQuery();
            
            while(result.next())
            {
                var nuevaEstacion = new TerminalAux();                
                nuevaEstacion.setIdEstacion(result.getInt("IdEstacion"));
                nuevaEstacion.setNomEstacion(result.getString("NomEstacion"));
             
                _estaciones.add(nuevaEstacion); 
            }
            for (TerminalAux terminal : _terminales) 
            {
                for (TerminalAux estacion : _estaciones)
                {
                    if (terminal.getIdEstacion() == estacion.getIdEstacion())
                    {
                        terminal.setNomEstacion(estacion.getNomEstacion());
                        break;
                    }
                    
                }                
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar las Estaciones","Error", JOptionPane.ERROR_MESSAGE);            
        }  
    }
    
    private String recuperaDias() 
    {
        StringBuilder dias = new StringBuilder();

        if (ChBLunes.isSelected()) 
            dias.append("L, ");
        if (ChBMartes.isSelected()) 
            dias.append("Ma, ");
        if (ChBMiercoles.isSelected()) 
            dias.append("Mi, ");
        if (ChBJueves.isSelected()) 
            dias.append("J, ");
        if (ChBViernes.isSelected()) 
            dias.append("V, ");
        if (ChBSabado.isSelected()) 
            dias.append("S, ");
        if (ChBDomingo.isSelected()) 
            dias.append("D ");

        return dias.toString().trim();
    }
    
    private void consultaDatos() 
    {
        _tablaIti.setRowCount(0);
        _itinierarios.clear();
        
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



                _tablaIti.addRow(
                        new Object[]
                        {
                            result.getInt("IdItinerario"),
                           result.getInt("IdSalida"),
                           result.getInt("IdLlegada"),
                           result.getString("Dias"),
                           result.getString("HoraSalida"),
                           result.getString("HoraLlegada"),
                           result.getInt("KMs")
                        });
                
                _itinierarios.add(nuevoIti); 
            }
            rellenaConcatenados();
        }
        catch(Exception ex)
        {
           JOptionPane.showMessageDialog(null, "Error al recuperar datos","Error", JOptionPane.ERROR_MESSAGE);            
        } 
        
    }

    private void insertaDato()
    {
        String sentenciaSQL = "INSERT INTO Destinos.Itinerario(IdSalida, IdLlegada, Dias, HoraSalida, HoraLlegada, KMs) VALUES (?, ?, ?, ?, ?, ?)";
        
        try
        {         
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL); 

            var nuevoIti = new ItinerarioAux();                
                nuevoIti.setIdTerSal(_idSalida);
                nuevoIti.setIdTerLleg(_idLlegada);
                nuevoIti.setDia(_dias);
                nuevoIti.setHoraSalida(_horaSalida);
                nuevoIti.setHoraLlegada(_horaLlegada);
                nuevoIti.setKilometros(_kms);

            verificaRepetidos(nuevoIti);
            
            
            statement.setLong(1, _idSalida);
            statement.setLong(2,_idLlegada);
            statement.setString(3,_dias);
            statement.setString(4,_horaSalida);
            statement.setString(5, _horaLlegada);
            statement.setInt(6,_kms);

            statement.executeUpdate();  

           _itinierarios.add(nuevoIti); 

            consultaDatos();
            
            txbHoraSalida.setText("");
            txbHoraLlegada.setText("");
            txbKiloemtros.setText("");
            cbDestino.setSelectedIndex(0);
            cbOrigen.setSelectedIndex(0);
            limpiaDias();          
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al insertar","Error", JOptionPane.ERROR_MESSAGE); 
            txbHoraSalida.setText("");
            txbHoraLlegada.setText("");
            txbKiloemtros.setText("");
            cbDestino.setSelectedIndex(0);
            cbOrigen.setSelectedIndex(0);
            limpiaDias();
                             
        }
    }   

    private void verificaRepetidos(ItinerarioAux nuevoIti) throws Exception
    {
        for (ItinerarioAux itinerario : _itinierarios) 
        {
            if (itinerario.getDia().equals(nuevoIti.getDia())) 
            {
                if (itinerario.getIdTerSal() == nuevoIti.getIdTerSal()) 
                {
                    if (itinerario.getIdTerLleg() == nuevoIti.getIdTerLleg()) 
                    {
                        if (itinerario.getHoraSalida().equals(nuevoIti.getHoraSalida())) 
                        {
                            if (itinerario.getHoraLlegada().equals(nuevoIti.getHoraLlegada()))
                            {
                                JOptionPane.showMessageDialog(null, "Elemento Repetido","Error", JOptionPane.ERROR_MESSAGE);
                                Exception exception = new IllegalArgumentException();           
                                throw exception;

                            }
                        }
                    }                
                }
            }
            if (nuevoIti.getIdTerSal() == nuevoIti.getIdTerLleg()) 
            {
                JOptionPane.showMessageDialog(null, "Origen y Destino Igual ","Error", JOptionPane.ERROR_MESSAGE);
                Exception exception = new IllegalArgumentException();           
                throw exception;
                
            }
        }
    }

    private void rellenaConcatenados() 
    {
        for (int i = 0; i < _tablaIti.getRowCount(); i++) 
        {
            for (TerminalAux terminal : _terminales) 
            {
                if (terminal.getIdTerminal() == (int)_tablaIti.getValueAt(i, 1)) 
                {
                    _tablaIti.setValueAt(terminal.getTerminalConcatenada(), i, 1);
                    break;                
                }
            }
        }  
        for (int i = 0; i < _tablaIti.getRowCount(); i++) 
        {
            for (TerminalAux terminal : _terminales) 
            {
                if (terminal.getIdTerminal() == (int)_tablaIti.getValueAt(i, 2)) 
                {
                    _tablaIti.setValueAt(terminal.getTerminalConcatenada(), i, 2);
                    break;                
                }
            }
        } 
    }

    private void limpiaDias() 
    {
       ChBLunes.setSelected(false);
       ChBMartes.setSelected(false);
       ChBMiercoles.setSelected(false);
       ChBJueves.setSelected(false);
       ChBViernes.setSelected(false);
       ChBSabado.setSelected(false);
       ChBDomingo.setSelected(false);
    }

    private void rellenaDias()
    {
        if (_dias.contains("L"))            
            ChBLunes.setSelected(true);
        if (_dias.contains("Ma"))
            ChBMartes.setSelected(true);
        if (_dias.contains("Mi"))
            ChBMiercoles.setSelected(true);
        if (_dias.contains("J"))
            ChBJueves.setSelected(true);
        if (_dias.contains("V"))
            ChBViernes.setSelected(true);
        if (_dias.contains("S"))
            ChBSabado.setSelected(true);
        if (_dias.contains("D"))
            ChBDomingo.setSelected(true);
    }

    private void eliminaDato() 
    {
        String sentenciaSQL = "DELETE FROM  Destinos.Itinerario WHERE IdItinerario = ?";
        
        try
        {            
            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);            
                
            statement.setLong(1,_idItinerario);
            statement.executeUpdate();
            
            consultaDatos();
           
            txbHoraSalida.setText("");
            txbHoraLlegada.setText("");
            txbKiloemtros.setText("");
            cbDestino.setSelectedIndex(0);
            cbOrigen.setSelectedIndex(0);
            limpiaDias();
           
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al eliminar ","Error", JOptionPane.ERROR_MESSAGE); 
            txbHoraSalida.setText("");
            txbHoraLlegada.setText("");
            txbKiloemtros.setText("");
            cbDestino.setSelectedIndex(0);
            cbOrigen.setSelectedIndex(0);
            limpiaDias();
        }
    }

    private void modificaDato()
    {
         String sentenciaSQL = "UPDATE Destinos.Itinerario SET IdSalida = ?, IdLlegada = ?, Dias = ?, HoraSalida = ?, HoraLlegada = ?, KMs = ? WHERE IdItinerario = ?";
        
        try
        {          
             var nuevoIti = new ItinerarioAux();                
                nuevoIti.setIdTerSal(_idSalida);
                nuevoIti.setIdTerLleg(_idLlegada);
                nuevoIti.setDia(_dias);
                nuevoIti.setHoraSalida(_horaSalida);
                nuevoIti.setHoraLlegada(_horaLlegada);
                nuevoIti.setKilometros(_kms);

            verificaRepetidos(nuevoIti);

            _conexion = ConexionSQL.getConnection(); 
            PreparedStatement statement = _conexion.prepareStatement(sentenciaSQL);           
            
           statement.setLong(1,_idSalida);
           statement.setLong(2,_idLlegada);
           statement.setString(3,_dias);
           statement.setString(4,_horaSalida);        
           statement.setString(5,_horaLlegada);   
           statement.setInt(6,_kms);       
           statement.setLong(7,_idItinerario);        

           statement.executeUpdate();
           
           consultaDatos();
           
           txbHoraSalida.setText("");
            txbHoraLlegada.setText("");
            txbKiloemtros.setText("");
            cbDestino.setSelectedIndex(0);
            cbOrigen.setSelectedIndex(0);
            limpiaDias();; 
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error al modificar ","Error", JOptionPane.ERROR_MESSAGE); 
            txbHoraSalida.setText("");
            txbHoraLlegada.setText("");
            txbKiloemtros.setText("");
            cbDestino.setSelectedIndex(0);
            cbOrigen.setSelectedIndex(0);
            limpiaDias();
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txbKiloemtros = new javax.swing.JTextField();
        txbHoraSalida = new javax.swing.JTextField();
        txbHoraLlegada = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbDestino = new javax.swing.JComboBox<>();
        cbOrigen = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        ChBLunes = new javax.swing.JCheckBox();
        ChBMiercoles = new javax.swing.JCheckBox();
        ChBMartes = new javax.swing.JCheckBox();
        ChBViernes = new javax.swing.JCheckBox();
        ChBSabado = new javax.swing.JCheckBox();
        ChBDomingo = new javax.swing.JCheckBox();
        ChBJueves = new javax.swing.JCheckBox();
        btnEliminar = new javax.swing.JButton();
        btnAgregar2 = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaItinerario = new javax.swing.JTable();

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
        setTitle("Itinerario");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Kilómetros");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Hora de Salida");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Hora de Llegada");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Origen");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Días");

        cbDestino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbOrigen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Destino");

        ChBLunes.setText("L");
        ChBLunes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChBLunes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChBLunes.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        ChBMiercoles.setText("Mi");
        ChBMiercoles.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChBMiercoles.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChBMiercoles.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        ChBMartes.setText("Ma");
        ChBMartes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChBMartes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChBMartes.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        ChBViernes.setText("V");
        ChBViernes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChBViernes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChBViernes.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        ChBSabado.setText("S");
        ChBSabado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChBSabado.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChBSabado.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        ChBDomingo.setText("D");
        ChBDomingo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChBDomingo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChBDomingo.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        ChBJueves.setText("J");
        ChBJueves.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChBJueves.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChBJueves.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

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

        tablaItinerario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id Itinerario", "Terminal de Salida", "Terminal de Llegada", "Días", "Hora de Salida", "Hora de Llegada", "Kilómetros"
            }
        ));
        tablaItinerario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaItinerarioMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaItinerario);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txbHoraSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txbHoraLlegada, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txbKiloemtros, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 115, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(12, 12, 12))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(18, 18, 18)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbDestino, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbOrigen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel6)
                                .addGap(42, 42, 42)
                                .addComponent(ChBLunes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ChBMartes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ChBMiercoles)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ChBJueves)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ChBViernes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ChBSabado)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ChBDomingo)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAgregar2)
                        .addGap(18, 18, 18)
                        .addComponent(btnModificar)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar)
                        .addGap(196, 196, 196))
                    .addComponent(jScrollPane2)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(29, 29, 29)
                                        .addComponent(jLabel3))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(txbHoraSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4)
                                    .addComponent(txbHoraLlegada, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cbOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cbDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(txbKiloemtros, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(ChBLunes)
                            .addComponent(ChBMartes)
                            .addComponent(ChBMiercoles)
                            .addComponent(ChBJueves)
                            .addComponent(ChBViernes)
                            .addComponent(ChBSabado)
                            .addComponent(ChBDomingo))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar)
                    .addComponent(btnModificar)
                    .addComponent(btnAgregar2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) 
    {     
        if (_horaLlegada.isEmpty() || _horaSalida.isEmpty() || _dias.isEmpty()) 
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
         _idSalida = buscaId((String)cbOrigen.getSelectedItem(), _terminales);
         _idLlegada = buscaId((String)cbDestino.getSelectedItem(), _terminales);
         
        _dias = recuperaDias();
        _horaSalida = (String)txbHoraSalida.getText();
        _horaLlegada = (String)txbHoraLlegada.getText(); 
        _kms = Integer.parseInt(txbKiloemtros.getText());

        if (_horaLlegada.isEmpty() || _horaSalida.isEmpty() || _dias.isEmpty()) 
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
        _idSalida = buscaId((String)cbOrigen.getSelectedItem(), _terminales);
         _idLlegada = buscaId((String)cbDestino.getSelectedItem(), _terminales);
         
        _dias = recuperaDias();
        _horaSalida = (String)txbHoraSalida.getText();
        _horaLlegada = (String)txbHoraLlegada.getText(); 
        _kms = Integer.parseInt(txbKiloemtros.getText());
        
         if (_horaLlegada.isEmpty() || _horaSalida.isEmpty() || _dias.isEmpty()) 
        {
            JOptionPane.showMessageDialog(null, "Campos incompletos ","Error", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
            modificaDato();
        } 
        
    }

    private void tablaItinerarioMouseClicked(java.awt.event.MouseEvent evt) 
    {
        int row =-1 ;
        row = tablaItinerario.rowAtPoint(evt.getPoint());

        _idItinerario = (int)tablaItinerario.getValueAt(row, 0);
        _idSalida = buscaId((String)tablaItinerario.getValueAt(row, 1), _terminales);
        _idLlegada = buscaId((String)tablaItinerario.getValueAt(row, 2), _terminales);

        _dias = (String)tablaItinerario.getValueAt(row, 3);
        _horaSalida = (String)tablaItinerario.getValueAt(row, 4);
        _horaLlegada = (String)tablaItinerario.getValueAt(row, 5);
        _kms = (int)tablaItinerario.getValueAt(row, 6);    
       
        txbKiloemtros.setText(_kms +"");
        txbHoraSalida.setText(_horaSalida);
        txbHoraLlegada.setText(_horaLlegada);
        cbOrigen.setSelectedItem(tablaItinerario.getValueAt(row, 1));
        cbDestino.setSelectedItem(tablaItinerario.getValueAt(row, 2));  
        limpiaDias();
        rellenaDias();     

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
            java.util.logging.Logger.getLogger(Itinerario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Itinerario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Itinerario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Itinerario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Itinerario("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ChBDomingo;
    private javax.swing.JCheckBox ChBJueves;
    private javax.swing.JCheckBox ChBLunes;
    private javax.swing.JCheckBox ChBMartes;
    private javax.swing.JCheckBox ChBMiercoles;
    private javax.swing.JCheckBox ChBSabado;
    private javax.swing.JCheckBox ChBViernes;
    private javax.swing.JButton btnAgregar2;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cbDestino;
    private javax.swing.JComboBox<String> cbOrigen;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tablaItinerario;
    private javax.swing.JTextField txbHoraLlegada;
    private javax.swing.JTextField txbHoraSalida;
    private javax.swing.JTextField txbKiloemtros;
    // End of variables declaration//GEN-END:variables
}
