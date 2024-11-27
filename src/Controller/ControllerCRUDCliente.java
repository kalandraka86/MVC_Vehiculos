package Controller;


import DAOs.DAOClienteSQL;
import DAOs.DAOVehiculosSQL;
import Recursos.Cliente;
import Recursos.Vehiculo;
import Vista.PanelCRUDCliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerCRUDCliente {

    public static void cargarTabla(JTable tablaClientes) throws InterruptedException {

        try {
            List<Cliente> lstClientes = DAOClienteSQL.getInstance().getClientes();
            DefaultTableModel modelo = new DefaultTableModel();

            modelo.addColumn("DNI");
            modelo.addColumn("Nombre");

            for (Cliente c : lstClientes) {
                modelo.addRow(new Object[]{c.getDni(), c.getNombre()});
            }
            modelo.fireTableDataChanged();
            tablaClientes.setModel(modelo);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar la tabla de clientes", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static boolean insertarCliente(Cliente cliente, PanelCRUDCliente frmCliente, JTable tablaClientes) throws SQLException, InterruptedException {
        int resultado = DAOClienteSQL.getInstance().insertarCliente(cliente);
        if (resultado == 1) {
            cargarTabla(tablaClientes);
            return true;
        }
        return false;
    }

    public static void leerCliente(PanelCRUDCliente thisPanel, JTable tablaClientes) {
        int filaSelec = tablaClientes.getSelectedRow();

        if (filaSelec == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona un cliente para obtener su información", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            String modelo = "";
            for (int i = 0; i < tablaClientes.getColumnCount(); i++) {
                modelo += tablaClientes.getValueAt(filaSelec, i).toString() + " ";
            }
            JOptionPane.showMessageDialog(null, modelo, "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static boolean actualizarCliente(PanelCRUDCliente thisPanel, JTable tablaClientes) throws Exception {
        boolean valido = false;
        int filaSelec = tablaClientes.getSelectedRow();

        if (filaSelec == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona un cliente para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            return valido;
        }

        String modelo = thisPanel.getTextFieldDNI().getText() + " " + thisPanel.getTextFieldNombre().getText();
        String[] partes = modelo.split(" ");

        Cliente cAntiguo = new Cliente(tablaClientes.getValueAt(filaSelec, 0).toString(),
                tablaClientes.getValueAt(filaSelec, 1).toString());
        Cliente cNuevo = new Cliente(partes[0], partes[1]);

        if (cAntiguo.equals(cNuevo)) {
            JOptionPane.showMessageDialog(null, "No puedes actualizar un vehiculo sin cambiarle algún dato", "Error", JOptionPane.ERROR_MESSAGE);
            return valido;
        }

        int valor = DAOClienteSQL.getInstance().actualizarCliente(cNuevo,filaSelec);
        if (valor == 1) {
            cargarTabla(tablaClientes);
            valido = true;
        }

        return valido;
    }

    public static void cargarTablaConcreta(Cliente c, JTable tablaVehiculos) throws SQLException {
        List<Vehiculo> lstVehiculos = DAOVehiculosSQL.getInstance().getVehiculos();
        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Marca");
        modelo.addColumn("Modelo");
        modelo.addColumn("Matricula");

        for (Vehiculo vehiculo : lstVehiculos) {
            if (c.getDni().equalsIgnoreCase(vehiculo.getDniCliente())) {
                modelo.addRow(new Object[]{vehiculo.getMarca(), vehiculo.getModelo(), vehiculo.getMatricula()});
            }
        }

        tablaVehiculos.setModel(modelo);
    }

    public static boolean eliminarCliente(Cliente c, JTable tablaClientes) throws InterruptedException, SQLException {
        int resultado = DAOClienteSQL.getInstance().eliminarCliente(c);
        if (resultado == 1) {
            cargarTabla(tablaClientes);
            return true;
        }
        return false;
    }

    public static boolean eliminarClientes(ArrayList<Cliente> clientes, JTable tablaClientes) throws SQLException, InterruptedException {
        int resultado = DAOClienteSQL.getInstance().eliminarCliente(clientes);
        if (resultado == 1) {
            cargarTabla(tablaClientes);
            return true;
        }
        return false;
    }
}
