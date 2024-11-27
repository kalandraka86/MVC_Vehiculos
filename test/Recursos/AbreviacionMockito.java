package Recursos;

import DAOs.IDAOVehiculo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbreviacionMockito {
    @Mock
    private IDAOVehiculo mockDAOVehiculo;
    @InjectMocks
    private ServicioVehiculosTest servicio;

    @BeforeEach
    void beforeEach() {
        ArrayList<Vehiculo> vehiculos = new ArrayList<>();
        vehiculos.add(new Vehiculo("Toyota", "Corolla", "ABC1234", "12345678X", 20000.0f));
        vehiculos.add(new Vehiculo("Honda", "Civic", "XYZ9876", "23456789Y", 15000.0f));

        // Cuando se haga este metodo me devuelva los vehiculos
        Mockito.when(mockDAOVehiculo.getVehiculos()).thenReturn(vehiculos);
    }

    @Test
    void getPrecioMax() {

        float resultadoReal = servicio.getMaxPrecio();
        assertEquals(20000.0f, resultadoReal);

        verify(mockDAOVehiculo).getVehiculos();
    }
}