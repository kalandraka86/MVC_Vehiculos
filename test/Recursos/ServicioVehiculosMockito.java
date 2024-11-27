package Recursos;

import DAOs.IDAOVehiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicioVehiculosMockito {

    private IDAOVehiculo mockDAOVehiculo;
    private ServicioVehiculos servicio;


    @BeforeEach
    void setUp() {
        // Creo un mock es como crear la clase mockdao sin poner codigo a los metodos
        mockDAOVehiculo = mock(IDAOVehiculo.class);

        // Meto datos a la bdd fake
        ArrayList<Vehiculo> vehiculos = new ArrayList<>();
        vehiculos.add(new Vehiculo("Toyota", "Corolla", "ABC1234", "12345678X", 20000.0f));
        vehiculos.add(new Vehiculo("Honda", "Civic", "XYZ9876", "23456789Y", 15000.0f));

        // Cuando se haga este metodo me devuelva los vehiculos
        when(mockDAOVehiculo.getVehiculos()).thenReturn(vehiculos);
        servicio = new ServicioVehiculos(mockDAOVehiculo);
    }

    @Test
    void getPrecioMax() {

        float resultadoReal = servicio.getMaxPrecio();
        assertEquals(20000.0f, resultadoReal);

        verify(mockDAOVehiculo).getVehiculos();
    }

}