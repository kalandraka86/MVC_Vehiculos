package Recursos;

import DAOs.MockDAOVehiculos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IVATest {

    private ServicioVehiculosTest servicio;


    @BeforeEach
    void setUp(){
        servicio = new ServicioVehiculosTest();
        MockDAOVehiculos.getInstance().insertarVehiculo(new Vehiculo("Toyota", "Corolla", "ABC1234", "12345678A"));
    }

    @Test
    void testGetPrecioIVA1() {
        Vehiculo v = MockDAOVehiculos.getInstance().getVehiculos().get(0);
        float precioIVA = servicio.getPrecioIVA(v.getMatricula());

        float precioEsperado = v.getPrecio()+(v.getPrecio()*0.21f);
        assertEquals(precioEsperado,precioIVA);
    }

    @Test
    void testGetPrecioIVA2() {
        Vehiculo v = MockDAOVehiculos.getInstance().getVehiculos().get(0);
        float precioIVA = servicio.getPrecioIVA(v.getMatricula());

        float precioEsperado = v.getPrecio()*1.21f;
        assertEquals(precioEsperado,precioIVA);
    }

    @Test
    void testGetPrecioIVA3() {
        Vehiculo v = MockDAOVehiculos.getInstance().getVehiculos().get(0);
        float precioIVA = servicio.getPrecioIVA(v.getMatricula());

        float precioEsperado = v.getPrecio();
        assertEquals(precioEsperado,precioIVA);
    }
}