package DAOs;

import Recursos.Vehiculo;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DAOVehiculosSQL implements IDAOVehiculo {

    private List<Vehiculo> vehiculosDB = new ArrayList<>();
    private static DAOVehiculosSQL dao = null;


    public DAOVehiculosSQL() throws SQLException {
        obtenerVehiculos();
    }

    private void obtenerVehiculos() throws SQLException {
        Statement statement = getStatementVehiculo();

        String selectSql = "SELECT DISTINCT Marca, Modelo, Matricula, DNIPropietario FROM PrimeraDB.dbo.Vehiculos; ";
        ResultSet resultSet = statement.executeQuery(selectSql);

        while (resultSet.next()) {
            vehiculosDB.add(new Vehiculo(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
        }
    }

    private Statement getStatementVehiculo() throws SQLException {
        return ConexionSQL.getInstance().conectar().createStatement();
    }

    @Override
    public int insertarVehiculo(Vehiculo vehiculo) {
        Iterator<Vehiculo> it = vehiculosDB.iterator();
        boolean repetido = false;
        try {
            while (it.hasNext()) {
                if (it.next().getMatricula().equalsIgnoreCase(vehiculo.getMatricula())) {
                    repetido = true;
                    break;
                }
            }
            if (!repetido) {
                addVehicleBDD(vehiculo);
                return 1;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No puedes insertar un vehiculo con una matrícula ya asignado", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }

    private int addVehicleBDD(Vehiculo vehiculo) throws SQLException {
        String consulta = "INSERT INTO PrimeraDB.dbo.Vehiculos (Marca, Modelo, Matricula, DNIPropietario) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = ConexionSQL.getInstance().conectar().prepareStatement(consulta);

        preparedStatement.setString(1, vehiculo.getMarca());
        preparedStatement.setString(2, vehiculo.getModelo());
        preparedStatement.setString(3, vehiculo.getMatricula());
        preparedStatement.setString(4, vehiculo.getDniCliente());

        vehiculosDB.clear();
        obtenerVehiculos();
        vehiculosDB.add(vehiculo);
        return preparedStatement.executeUpdate(); // Retorna el número de filas afectadas

    }

    @Override
    public int eliminarVehiculo(Vehiculo vehiculo) {
        Iterator<Vehiculo> it = vehiculosDB.iterator();
        while (it.hasNext()) {
            if (it.next().getMatricula().equalsIgnoreCase(vehiculo.getMatricula())) {
                it.remove();
                deleteVehicleBDD(vehiculo);
                return 1;
            }
        }
        return -1;
    }

    private int deleteVehicleBDD(Vehiculo vehiculo) {
        String scriptSQL = "DELETE FROM PrimeraDB.dbo.Vehiculos WHERE Matricula = ?";
        try {
            PreparedStatement preparedStatement = ConexionSQL.getInstance().conectar().prepareStatement(scriptSQL);
            preparedStatement.setString(1, vehiculo.getMatricula());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int actualizarVehiculo(Vehiculo vehiculo, int posTabla) throws Exception {
        if (posTabla == -1) {
            throw new Exception();
        } else {
            vehiculosDB.get(posTabla).setMarca(vehiculo.getMarca());
            vehiculosDB.get(posTabla).setModelo(vehiculo.getModelo());
            updateVehicle(vehiculo);
            return 1;
        }
    }

    private int updateVehicle(Vehiculo vehiculo) {
        String scriptSQL = "UPDATE PrimeraDB.dbo.Vehiculos SET Marca = ?, Modelo = ? WHERE Matricula = ?";
        try {
            PreparedStatement preparedStatement = ConexionSQL.getInstance().conectar().prepareStatement(scriptSQL);
            preparedStatement.setString(1, vehiculo.getMarca());
            preparedStatement.setString(2, vehiculo.getModelo());
            preparedStatement.setString(3, vehiculo.getMatricula());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int eliminarVehiculo(String matricula) {
        if (vehiculosDB.stream().filter(v -> v.getMatricula().equals(matricula)).findFirst().orElse(null) != null) {
            vehiculosDB.remove(vehiculosDB.stream().filter(v -> v.getMatricula().equals(matricula)).findFirst().orElse(null));
            return 1;
        }
        return -1;
    }

    @Override
    public int eliminarVehiculos(List<Vehiculo> lstVehiculos) {
        if(!lstVehiculos.isEmpty()) {
            for (Vehiculo v : lstVehiculos) {
                eliminarVehiculo(v);
            }
            deleteVehiclesBDD(lstVehiculos);
            return 1;
        }
        else
            return -1;
    }

    private int deleteVehiclesBDD(List<Vehiculo> lstVehiculos) {
        int modificado = -1;
        String scriptSQL = "DELETE FROM PrimeraDB.dbo.Vehiculos WHERE Matricula = ?";
        for (Vehiculo v : lstVehiculos) {
            try {
                PreparedStatement preparedStatement = ConexionSQL.getInstance().conectar().prepareStatement(scriptSQL);
                preparedStatement.setString(1, v.getMatricula());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return modificado;
    }

    @Override
    public Vehiculo getVehiculo(String matricula) {
        return vehiculosDB.stream().filter(v -> v.getMatricula().equalsIgnoreCase(matricula)).findFirst().orElse(null);
    }

    @Override
    public List<Vehiculo> getVehiculos() {
        return vehiculosDB;
    }


    public static DAOVehiculosSQL getInstance() throws SQLException {
        if (dao == null) dao = new DAOVehiculosSQL();
        return dao;
    }
}
