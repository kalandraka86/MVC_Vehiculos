package Vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ListSelectionModel;


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import Controller.ControllerCRUD;
import DAOs.DAOClienteSQL;
import DAOs.DAOVehiculosSQL;
import Recursos.Cliente;
import Recursos.Vehiculo;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.JComboBox;

public class PanelCRUD extends JPanel {

    private ControllerCRUD controller;

    public PanelCRUD() throws SQLException {
        initComponents();
        setLayout(null);
        ControllerCRUD.cargarTabla(tablaVehiculos);
        add(jScrollPane1);
        add(jLabelMatricula);
        add(jLabel1);
        add(jLabel2);
        add(txtMatricula);
        add(txtMarca);
        add(txtModelo);
        add(btLeer);
        add(btRegistrar);
        add(btEliminar);
        add(btActualizar);
        
        lblDNI = new JLabel("DNI_Propietario");
        lblDNI.setBounds(100, 139, 99, 16);
        add(lblDNI);
        
        comboBox = new JComboBox();
        comboBox.setBounds(209, 135, 147, 27);
        fillComboBox(comboBox);
        add(comboBox);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new JLabel("Marca");
        jLabel1.setBounds(99, 22, 37, 16);
        jScrollPane1 = new JScrollPane();
        jScrollPane1.setBounds(100, 169, 375, 181);
        tablaVehiculos = new JTable();
        tablaVehiculos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tablaVehiculos.addMouseListener(new TablaVehiculosMouseListener());
        txtMarca = new JTextField();
        txtMarca.setBounds(163, 17, 199, 26);
        txtModelo = new JTextField();
        txtModelo.setBounds(163, 52, 199, 26);
        jLabel2 = new JLabel("Modelo");
        jLabel2.setBounds(99, 57, 46, 16);
        jLabelMatricula = new JLabel("Matricula");
        jLabelMatricula.setBounds(99, 104, 58, 16);
        txtMatricula = new JTextField();
        txtMatricula.setBounds(163, 99, 199, 26);
        btRegistrar = new JButton("Registrar");
        btRegistrar.setBounds(368, 17, 107, 29);
        btLeer = new JButton("Leer");
        btLeer.setBounds(368, 52, 107, 29);
        btEliminar = new JButton("Eliminar");
        btEliminar.setBounds(368, 99, 107, 29);
        btActualizar = new JButton("Actualizar");
        btActualizar.setBounds(368, 134, 107, 29);

        // Configurar tabla
        tablaVehiculos
                .setModel(new javax.swing.table.DefaultTableModel(
                        new Object[][]{{null, null, null, null}, {null, null, null, null},
                                {null, null, null, null}, {null, null, null, null}},
                        new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));

        jScrollPane1.setViewportView(tablaVehiculos);

        // Asignar acciones a los botones
        btRegistrar.addActionListener(new btnActionListener());
        btLeer.addActionListener(new btnActionListener());
        btEliminar.addActionListener(new btnActionListener());
        btActualizar.addActionListener(new btnActionListener());
    }

    // Listeners para manejar los eventos
    private class btnActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btLeer) {
                ControllerCRUD.leerVehiculo(tablaVehiculos);
            }
            if (e.getSource() == btActualizar) {
                if (tablaVehiculos.getSelectedRow() == -1) {
                    mensajeError("Tienes que seleccionar un vehiculo en la tabla para actualizarlo");
                }
                else
                if (comprobarCampos(thisPanel)) {
                    try {
                        ControllerCRUD.actualizarVehiculo(thisPanel, tablaVehiculos);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

            if (e.getSource() == btEliminar) {
                int filaSelec = tablaVehiculos.getSelectedRow();
                String[] partesVehiculo = new String[3];

                if (tablaVehiculos.getSelectedRow() == -1) {
                    mensajeError("Selecciona un coche para eliminarlo de la tabla");
                } else {

                    for (int i = 0; i < tablaVehiculos.getColumnCount(); i++) {
                        partesVehiculo[i] = (String) tablaVehiculos.getValueAt(filaSelec, i);
                    }
                    Vehiculo v = new Vehiculo(partesVehiculo[0], partesVehiculo[1], partesVehiculo[2]);
                    try {
                        ControllerCRUD.eliminarVehiculo(v, tablaVehiculos);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    getTxtMarca().setText("");
                    getTxtModelo().setText("");
                    getTxtMatricula().setText("");
                    comboBox.setSelectedIndex(0);
                    btRegistrar.setEnabled(true);
                }
            }
            if (e.getSource() == btRegistrar) {
                if (comprobarCampos(thisPanel)) {
                    try {
                        ControllerCRUD.insertarVehiculo(new Vehiculo(thisPanel.getTxtMarca().getText(), thisPanel.getTxtModelo().getText(), thisPanel.getTxtMatricula().getText(),thisPanel.comboBox.getSelectedItem().toString()),tablaVehiculos);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }

    private void mensajeError(String s) {
        JOptionPane.showMessageDialog(null, s, "Error", JOptionPane.ERROR_MESSAGE);
    }


    private boolean comprobarCampos(PanelCRUD thisPanel) {
        boolean isValido = false;
        boolean isMarca = false;
        boolean isModelo = false;
        boolean isMatricula = false;

        if (thisPanel.getTxtMarca().getText().equalsIgnoreCase("")
                && thisPanel.getTxtModelo().getText().equalsIgnoreCase("")
                && thisPanel.getTxtMatricula().getText().equalsIgnoreCase("")) {
            mensajeError("Rellena todos los campos");
        } else {
            if (thisPanel.getTxtMarca().getText().equalsIgnoreCase("")) {
                mensajeError("El campo 'marca' no puede estar vacío");
            } else {
                isMarca = true;
            }
            if (thisPanel.getTxtModelo().getText().equalsIgnoreCase("")) {
                mensajeError("El campo 'modelo' no puede estar vacío");
            } else {
                isModelo = true;
            }

            if (thisPanel.getTxtMatricula().getText().equalsIgnoreCase("")) {
                mensajeError("El campo 'matrícula' no puede estar vacío");
            } else {
                isMatricula = true;
            }

            if (isMarca && isModelo && isMatricula) {
                isValido = true;
            }
        }
        return isValido;
    }

    private void fillComboBox(JComboBox comboBox) throws SQLException {
        comboBox.addItem("");
        for (Cliente cliente : DAOClienteSQL.getInstance().getClientes()) {
           comboBox.addItem(cliente.getDni());
        }
    }

    private class TablaVehiculosMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {

            if (tablaVehiculos.getSelectedRow() == -1) {
                comboBox.removeAllItems();
                try {
                    fillComboBox(comboBox);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                btRegistrar.setEnabled(true);
                thisPanel.getTxtMatricula().setEnabled(true);
            }
            if (e.isControlDown() || e.isMetaDown()) {
                btRegistrar.setEnabled(true);
                txtMarca.setText("");
                txtModelo.setText("");
                txtMatricula.setText("");
            }
            if (tablaVehiculos.getSelectedRow() != -1) {
                btRegistrar.setEnabled(false);
                thisPanel.getTxtMatricula().setEnabled(false);
                txtMarca.setText((String) tablaVehiculos.getValueAt(tablaVehiculos.getSelectedRow(), 0));
                txtModelo.setText((String) tablaVehiculos.getValueAt(tablaVehiculos.getSelectedRow(), 1));
                txtMatricula.setText((String) tablaVehiculos.getValueAt(tablaVehiculos.getSelectedRow(), 2));
                comboBox.removeAllItems();
                try {
                    comboBox.addItem(DAOVehiculosSQL.getInstance().getVehiculo(txtMatricula.getText()).getDniCliente());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    // Variables declaration
    private JButton btLeer;
    private JButton btRegistrar;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabelMatricula;
    private JScrollPane jScrollPane1;
    private JTable tablaVehiculos;
    private JTextField txtMatricula;
    private JTextField txtMarca;
    private JTextField txtModelo;
    private JButton btEliminar;
    private JButton btActualizar;
    private PanelCRUD thisPanel = this;
    private JLabel lblDNI;
    private JComboBox comboBox;
    // End of variables declaration

    public JTextField getTxtMatricula() {
        return txtMatricula;
    }

    public JTextField getTxtMarca() {
        return txtMarca;
    }

    public JTextField getTxtModelo() {
        return txtModelo;
    }
}
