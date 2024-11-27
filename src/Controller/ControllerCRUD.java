package Controller;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import DAOs.DAOVehiculosSQL;
import Recursos.Cliente;
import Recursos.Vehiculo;
import Vista.PanelCRUD;

public class ControllerCRUD {

	private static List<Vehiculo> lstVehiculos;

	public static void cargarTabla(JTable tablaVehiculos) throws SQLException { // DefaultTableModel modeloDeDatosTabla =
		// (DefaultTableModel) tablaVehiculos.getModel();
		lstVehiculos = DAOVehiculosSQL.getInstance().getVehiculos();

		DefaultTableModel modelo = new DefaultTableModel();

		modelo.addColumn("Marca");

		modelo.addColumn("Modelo");

		modelo.addColumn("Matricula");

		Object[] registroLeido = new Object[3];

		for (Vehiculo vehiculo : lstVehiculos) {
			registroLeido[0] = vehiculo.getMarca();

			registroLeido[1] = vehiculo.getModelo();

			registroLeido[2] = vehiculo.getMatricula();

			modelo.addRow(registroLeido);

		}

		tablaVehiculos.setModel(modelo);
	}

	public static boolean insertarVehiculo(Vehiculo vehiculo, JTable tablaVehiculos) throws SQLException {
		boolean insertado = false;
		if (vehiculo.getDniCliente().equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(null,"No puedes asignar un coche sin propietario","ERROR",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		int resultado = DAOVehiculosSQL.getInstance().insertarVehiculo(vehiculo);
		if (resultado == 1) {
			insertado = true;
			cargarTabla(tablaVehiculos);
		}
		System.out.println(insertado);
		return insertado;
	}

	public static boolean eliminarVehiculo(Vehiculo v, JTable tablaVehiculos) throws SQLException {
		boolean eliminado = false;

		int resultado = DAOVehiculosSQL.getInstance().eliminarVehiculo(v);
		if (resultado == 1) {
			eliminado = true;
			cargarTabla(tablaVehiculos);
		}
		System.out.println(eliminado);

		return eliminado;
	}

	public static void leerVehiculo(JTable tablaVehiculos) {
		int filaSelec = tablaVehiculos.getSelectedRow();

		String modelo = "";

		if (tablaVehiculos.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(null, "Selecciona un coche para obtener su información", "ERROR",
					JOptionPane.ERROR_MESSAGE);
		} else {

			for (int i = 0; i < tablaVehiculos.getColumnCount(); i++) {
				modelo += tablaVehiculos.getValueAt(filaSelec, i).toString() + " ";
			}
			String[] partes = modelo.split(" ");
			Vehiculo vehiculo = new Vehiculo(partes[0],partes[1],partes[2]);

			String dni = "";
			for (Vehiculo v : lstVehiculos) {
				if(v.equals(vehiculo))
					dni = v.getDniCliente();

			}

			JOptionPane.showMessageDialog(null, modelo+dni, "Información", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static boolean actualizarVehiculo(PanelCRUD thisPanel, JTable tablaVehiculos) throws Exception {
		boolean valido = false;
		int filaSelec = tablaVehiculos.getSelectedRow();
		String modelo = thisPanel.getTxtMarca().getText() + " " + thisPanel.getTxtModelo().getText() + " "
				+ thisPanel.getTxtMatricula().getText();
		String[] partes = modelo.toString().split(" ");

		Vehiculo vAntiguo = new Vehiculo(tablaVehiculos.getValueAt(filaSelec, 0).toString(),
				tablaVehiculos.getValueAt(filaSelec, 1).toString(), tablaVehiculos.getValueAt(filaSelec, 2).toString());
		Vehiculo vNuevo = new Vehiculo(partes[0], partes[1], partes[2]);
		if (vAntiguo.equals(vNuevo)) {
			JOptionPane.showMessageDialog(null, "No puedes actualizar un vehiculo sin cambiarle algún dato", "Error",
					JOptionPane.ERROR_MESSAGE);
			return valido;
		}
		int valor = DAOVehiculosSQL.getInstance().actualizarVehiculo(vNuevo, filaSelec);
		if (valor == 1) {
			cargarTabla(tablaVehiculos);
			valido = true;
		}
		return valido;
	}

}
