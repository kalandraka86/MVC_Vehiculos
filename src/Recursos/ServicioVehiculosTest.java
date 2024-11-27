package Recursos;

import DAOs.IDAOVehiculo;
import DAOs.MockDAOVehiculos;

public class ServicioVehiculosTest extends ServicioVehiculos {

    @Override
    public IDAOVehiculo getDAO() {
        return MockDAOVehiculos.getInstance();
    }
}
