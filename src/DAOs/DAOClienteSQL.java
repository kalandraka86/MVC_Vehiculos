package DAOs;

import Recursos.Cliente;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DAOClienteSQL implements IDAOCliente {

    private List<Cliente> clientesDB = new ArrayList<>();
    private static DAOClienteSQL dao = null;

    private DAOClienteSQL() throws SQLException {
        obtenerClientes();
    }

    private void obtenerClientes() throws SQLException {
        Statement statement = getStatementCliente();

        String selectSql = "SELECT DISTINCT DNI, Nombre FROM PrimeraDB.dbo.Clientes; ";
        ResultSet resultSet = statement.executeQuery(selectSql);


        while (resultSet.next()) {
            clientesDB.add(new Cliente(resultSet.getString(1), resultSet.getString(2)));
        }
    }

    private Statement getStatementCliente() throws SQLException {
        return ConexionSQL.getInstance().conectar().createStatement();
    }

    @Override
    public int insertarCliente(Cliente cliente) throws SQLException {
        Iterator<Cliente> it = clientesDB.iterator();
        boolean repetido = false;
        try {
            while (it.hasNext()) {
                if (it.next().getDni().equalsIgnoreCase(cliente.getDni())) {
                    repetido = true;
                    break;
                }
            }
            if (!repetido) {
                addClientBDD(cliente);
                return 1;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No puedes insertar un cliente con un DNI ya asignado", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }

    private int addClientBDD(Cliente cliente) throws SQLException {
        String consulta = "INSERT INTO PrimeraDB.dbo.Clientes (DNI, Nombre) VALUES (?, ?)";
        PreparedStatement preparedStatement = ConexionSQL.getInstance().conectar().prepareStatement(consulta);

        preparedStatement.setString(1, cliente.getDni());
        preparedStatement.setString(2, cliente.getNombre());

        clientesDB.clear();
        obtenerClientes();
        clientesDB.add(cliente);
        return preparedStatement.executeUpdate(); // Retorna el número de filas afectadas
    }

    @Override
    public int eliminarCliente(Cliente cliente) throws SQLException {
        Iterator<Cliente> it = clientesDB.iterator();
        while (it.hasNext()) {
            if (it.next().getDni().equalsIgnoreCase(cliente.getDni())) {
                it.remove();
                deleteClientBDD(cliente);
                return 1;
            }
        }
        return -1;
    }

    private int deleteClientBDD(Cliente cliente) throws SQLException {
        String scriptSQL = "DELETE FROM PrimeraDB.dbo.Clientes WHERE DNI = ?";
        try {
            PreparedStatement preparedStatement = ConexionSQL.getInstance().conectar().prepareStatement(scriptSQL);
            preparedStatement.setString(1, cliente.getDni());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int actualizarCliente(Cliente cliente, int posTabla) throws Exception {
        if (posTabla == -1) {
            throw new Exception();
        } else {
            clientesDB.get(posTabla).setNombre(cliente.getNombre());
            updateClient(cliente);
            return 1;
        }
    }

    private int updateClient(Cliente cliente) {
        String scriptSQL = "UPDATE PrimeraDB.dbo.Clientes SET Nombre = ? WHERE DNI = ?";
        try {
            PreparedStatement preparedStatement = ConexionSQL.getInstance().conectar().prepareStatement(scriptSQL);
            preparedStatement.setString(1, cliente.getNombre());
            preparedStatement.setString(2, cliente.getDni());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int eliminarCliente(String dni) {
        if(clientesDB.stream().filter(c -> c.getDni().equals(dni)).findFirst().orElse(null) != null) {
            clientesDB.remove(clientesDB.stream().filter(c -> c.getDni().equals(dni)).findFirst().orElse(null));
            return 1;
        }
        return -1;
    }

    @Override
    public int eliminarCliente(List<Cliente> lstClientes) throws SQLException {
        for (Cliente c : lstClientes) {
            System.out.println(c);
        }
        if(!lstClientes.isEmpty()) {
            for (Cliente c : lstClientes) {
                eliminarCliente(c);
            }
            deleteClientsBDD(lstClientes);
            return 1;
        }
        else
            return -1;
    }

    private int deleteClientsBDD(List<Cliente> lstClientes) {
        int modificado = -1;
        String scriptSQL = "DELETE FROM PrimeraDB.dbo.Clientes WHERE DNI = ?";

        for (int i = 0; i < lstClientes.size(); i++) {
            try {
                PreparedStatement preparedStatement = ConexionSQL.getInstance().conectar().prepareStatement(scriptSQL);
                preparedStatement.setString(1, lstClientes.get(i).getDni());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return modificado;
    }

    @Override
    public Cliente getCliente(String dni) {
        return clientesDB.stream().filter(c -> c.getDni().equalsIgnoreCase(dni)).findFirst().orElse(null);
        // El orElse es para que si no lo encuentra, devuelva un null
    }

    @Override
    public List<Cliente> getClientes() {
        return clientesDB;
    }

    public static DAOClienteSQL getInstance() throws SQLException {
        if (dao == null) dao = new DAOClienteSQL();
        return dao;
    }
}





