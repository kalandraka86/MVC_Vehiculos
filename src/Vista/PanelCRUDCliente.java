package Vista;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import Controller.ControllerCRUD;
import Controller.ControllerCRUDCliente;
import Recursos.Cliente;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;

public class PanelCRUDCliente extends JPanel {
    private JLabel lblDNI;
    private JTextField textFieldDNI;
    private JTextField textFieldNombre;
    private JLabel lblNombre;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JTable tablaVehiculos;
    private JTable tablaClientes;
    private JButton btnRegistrar;
    private JButton btnBorrar;
    private JButton btnActualizar;
    private PanelCRUDCliente thisPanel = this;

    /**
     * Launch the application.
     */

    public PanelCRUDCliente() throws InterruptedException, SQLException {
        setLayout(null);

        lblDNI = new JLabel("DNI");
        lblDNI.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        lblDNI.setBounds(76, 57, 58, 16);
        add(lblDNI);

        textFieldDNI = new JTextField();
        textFieldDNI.setBounds(189, 53, 200, 26);
        add(textFieldDNI);
        textFieldDNI.setColumns(10);

        textFieldNombre = new JTextField();
        textFieldNombre.setColumns(10);
        textFieldNombre.setBounds(189, 113, 200, 26);
        add(textFieldNombre);

        lblNombre = new JLabel("Nombre");
        lblNombre.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        lblNombre.setBounds(76, 117, 117, 16);
        add(lblNombre);


        jScrollPane1 = new JScrollPane();
        jScrollPane1.setBounds(22, 173, 265, 170);
        add(jScrollPane1);

        tablaClientes = new JTable();
        tablaClientes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tablaClientes.addMouseListener(new TablaClientesMouseListener());
        jScrollPane1.setViewportView(tablaClientes);

        ControllerCRUDCliente.cargarTabla(tablaClientes);

        jScrollPane2 = new JScrollPane();
        jScrollPane2.setBounds(325, 173, 265, 170);
        add(jScrollPane2);

        tablaVehiculos = new JTable();
        tablaVehiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tablaVehiculos);

        ControllerCRUD.cargarTabla(tablaVehiculos);
        tablaVehiculos.setEnabled(false);


        btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(new BtnActionListener());
        btnRegistrar.setBounds(428, 39, 117, 29);
        add(btnRegistrar);

        btnBorrar = new JButton("Borrar");
        btnBorrar.addActionListener(new BtnActionListener());
        btnBorrar.setBounds(428, 82, 117, 29);
        add(btnBorrar);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new BtnActionListener());
        btnActualizar.setBounds(428, 123, 117, 29);
        add(btnActualizar);
    }

    private class TablaClientesMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (tablaClientes.getSelectedRows().length > 1) {
                for (int i = 0; i < tablaClientes.getSelectedRows().length; i++) {
                    System.out.println(tablaClientes.getSelectedRows()[i]);
                }
            }
            if (tablaClientes.getSelectedRow() == -1) {
                btnRegistrar.setEnabled(true);
            } else {
                if (e.isControlDown() || e.isMetaDown()) {
                    textFieldDNI.setEnabled(true);
                    btnRegistrar.setEnabled(true);
                    getTextFieldDNI().setText("");
                    getTextFieldNombre().setText("");
                } else {
                    btnRegistrar.setEnabled(false);
                    getTextFieldDNI().setEnabled(false);
                    getTextFieldDNI().setText((String) tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 0));
                    getTextFieldNombre().setText((String) tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 1));
                    try {
                        ControllerCRUDCliente.cargarTablaConcreta(new Cliente(tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 0).toString(), tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 1).toString()), tablaVehiculos);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }


    private class BtnActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnRegistrar) {
                try {
                    ControllerCRUDCliente.insertarCliente(new Cliente(getTextFieldDNI().getText(), getTextFieldNombre().getText()), thisPanel, tablaClientes);
                    getTextFieldDNI().setText("");
                    getTextFieldNombre().setText("");

                    btnRegistrar.setEnabled(true);
                } catch (SQLException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (e.getSource() == btnBorrar) {
                int filaSelec = tablaClientes.getSelectedRow();
                String[] partesCliente = new String[2];

                if (tablaClientes.getSelectedRow() == -1) {
                    mensajeError("Selecciona un cliente para eliminarlo de la tabla");
                } else if (tablaClientes.getSelectedRows().length > 1) {
                    int cantSeleccionados = tablaClientes.getSelectedRows().length;
                    ArrayList<Cliente> clientesBorrados = new ArrayList<>();

                    for (int i = 0; i < cantSeleccionados; i++) {
                       String dni = tablaClientes.getValueAt(tablaClientes.getSelectedRows()[i],0).toString();
                       String nombre =tablaClientes.getValueAt(tablaClientes.getSelectedRows()[i],1).toString();
                        clientesBorrados.add(new Cliente(dni,nombre));
                    }
                    textFieldDNI.setText("");
                    textFieldNombre.setText("");
                    try {
                        ControllerCRUDCliente.eliminarClientes(clientesBorrados, tablaClientes);
                    } catch (InterruptedException | SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    btnRegistrar.setEnabled(true);
                } else {
                    textFieldDNI.setText("");
                    textFieldNombre.setText("");
                    for (int i = 0; i < tablaClientes.getColumnCount(); i++) {
                        partesCliente[i] = (String) tablaClientes.getValueAt(filaSelec, i);
                    }
                    Cliente c = new Cliente(partesCliente[0], partesCliente[1]);
                    try {
                        ControllerCRUDCliente.eliminarCliente(c, tablaClientes);
                    } catch (InterruptedException | SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    btnRegistrar.setEnabled(true);
                }
            }
            if (e.getSource() == btnActualizar) {
                try {
                    ControllerCRUDCliente.actualizarCliente(thisPanel, tablaClientes);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

            }
        }
    }

    private void mensajeError(String s) {
        JOptionPane.showMessageDialog(null, s, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public JTextField getTextFieldDNI() {
        return textFieldDNI;
    }

    public JTextField getTextFieldNombre() {
        return textFieldNombre;
    }
}
