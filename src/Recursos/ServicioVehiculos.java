package Recursos;

import DAOs.DAOVehiculosSQL;
import DAOs.IDAOVehiculo;

import java.sql.SQLException;
import java.util.List;

public class ServicioVehiculos {

    private IDAOVehiculo dao = null;

    public ServicioVehiculos(){
        this.dao = getDAO();
    }

    public ServicioVehiculos(IDAOVehiculo mockDAOVehiculo) {
        this.dao = mockDAOVehiculo;
    }

    public IDAOVehiculo getDAO(){
        try {
            return DAOVehiculosSQL.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // La cosa es que tenga que llamar a la bdd simulada
    public float getPrecioIVA(String matricula){
        return dao.getVehiculo(matricula).getPrecio()*1.21f;
    }

    public float getMaxPrecio(){
        List<Vehiculo> vehiculos = dao.getVehiculos();
        float precioMax = 0;
        for (Vehiculo vehiculo : vehiculos) {
            if(vehiculo.getPrecio() > precioMax)
                precioMax = vehiculo.getPrecio();
        }
        return precioMax;
    }

    public double setDescuentoPrecio(String matricula,double descuento){
        descuento = descuento/100;
        double precioFinal = dao.getVehiculo(matricula).getPrecio()-(dao.getVehiculo(matricula).getPrecio()*descuento);
        return precioFinal;
    }
}
