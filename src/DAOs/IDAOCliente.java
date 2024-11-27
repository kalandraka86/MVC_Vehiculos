package DAOs;

import java.sql.SQLException;
import java.util.List;

import Recursos.Cliente;

public interface IDAOCliente {

    public int insertarCliente(Cliente cliente) throws SQLException;

    public int eliminarCliente(Cliente cliente) throws SQLException;

    public int actualizarCliente(Cliente cliente, int posTabla) throws Exception;

    public int eliminarCliente(String dni);

    public int eliminarCliente(List<Cliente> lstClientes) throws SQLException;

    public Cliente getCliente(String dni);

    public List<Cliente> getClientes();


}
