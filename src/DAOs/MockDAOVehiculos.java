package DAOs;

import Recursos.Vehiculo;
import java.util.ArrayList;
import java.util.List;

public class MockDAOVehiculos implements IDAOVehiculo {
    private List<Vehiculo> vehiculosDB = new ArrayList<>();
    private static MockDAOVehiculos dao = null;


    @Override
    public int insertarVehiculo(Vehiculo vehiculo) {
        vehiculosDB.add(vehiculo);
        return 1; // Éxito
    }

    @Override
    public int eliminarVehiculo(Vehiculo vehiculo) {
        return vehiculosDB.remove(vehiculo) ? 1 : -1;
    }

    @Override
    public int actualizarVehiculo(Vehiculo vehiculo, int posTabla) throws Exception {
        if (posTabla < 0 || posTabla >= vehiculosDB.size()) {
            throw new Exception("Posición no válida");
        }
        vehiculosDB.set(posTabla, vehiculo);
        return 1;
    }

    @Override
    public int eliminarVehiculo(String matricula) {
        return vehiculosDB.removeIf(v -> v.getMatricula().equalsIgnoreCase(matricula)) ? 1 : -1;
    }

    @Override
    public int eliminarVehiculos(List<Vehiculo> lstVehiculos) {
        if (lstVehiculos.isEmpty()) {
            return -1; // Error: lista vacía
        }
        vehiculosDB.removeAll(lstVehiculos);
        return 1; // Éxito
    }

    @Override
    public Vehiculo getVehiculo(String matricula) {
        return vehiculosDB.stream()
                .filter(v -> v.getMatricula().equalsIgnoreCase(matricula))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Vehiculo> getVehiculos() {
        return vehiculosDB;
    }



    public static MockDAOVehiculos getInstance(){
        if (dao == null) dao = new MockDAOVehiculos();
        return dao;
    }
}
