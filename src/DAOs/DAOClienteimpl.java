package DAOs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Recursos.Cliente;

import javax.swing.*;

public class DAOClienteimpl implements IDAOCliente {

    private List<Cliente> falsaBD;
    private static DAOClienteimpl dao = null;

    private DAOClienteimpl() {
        super();
        this.falsaBD = new ArrayList<Cliente>();
        falsaBD.add(new Cliente("12345678A", "Hugo"));
        falsaBD.add(new Cliente("12345678B", "Diego"));
        falsaBD.add(new Cliente("12345678C", "Enrique"));
        falsaBD.add(new Cliente("12345678D", "Lucía"));

    }

    public static IDAOCliente getInstanceCliente() {
        if (dao == null) dao = new DAOClienteimpl();
        return dao;
    }

    @Override
    public int insertarCliente(Cliente cliente) {
        Iterator<Cliente> it = falsaBD.iterator();
        boolean repetido = false;
        try {
            while (it.hasNext()) {
                if (it.next().getDni().equalsIgnoreCase(cliente.getDni())) {
                    repetido = true;
                    break;
                }
            }
            if (!repetido) {
                falsaBD.add(cliente);
                return 1;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No puedes insertar un cliente con un DNI ya asignado", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }

    @Override
    public int eliminarCliente(Cliente cliente) {
        Iterator<Cliente> it = falsaBD.iterator();
        while (it.hasNext()) {
            if (it.next().getDni().equalsIgnoreCase(cliente.getDni())) {
                it.remove();
                return 1;
            }
        }
        return -1;
    }

    @Override
    public int actualizarCliente(Cliente cliente, int posTabla) {
        try {
            if (posTabla == -1) {
                throw new Exception();
            } else {
                falsaBD.remove(posTabla);
                falsaBD.add(posTabla, cliente);
            }
            return 1;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error interno en la actualización", "ERROR", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }

    @Override
    public int eliminarCliente(String dni) {
        return 0;
    }

//    @Override
//    public Cliente eliminarCliente(String dni) {
//        for (Cliente c : falsaBD) {
//            if (c.getDni().equalsIgnoreCase(dni))
//                return c;
//        }
//        return null;
//    }

    @Override
    public int eliminarCliente(List<Cliente> lstClientes) {
        falsaBD.removeAll(lstClientes);
        return 1;
    }

    @Override
    public Cliente getCliente(String dni) {
        return null;
    }

    @Override
    public List<Cliente> getClientes() {
        return this.falsaBD;
    }
}
