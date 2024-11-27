package Recursos;

import DAOs.MockDAOVehiculos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DescuentoTest {

    private ServicioVehiculosTest servicio;

    @BeforeEach
    void setUp(){
        servicio = new ServicioVehiculosTest();
        MockDAOVehiculos.getInstance().insertarVehiculo(new Vehiculo("Toyota", "Corolla", "ABC1234", "12345678A", 12000f));
        //Precio: 12000f
    }

    @Test
    void setDescuentoPrecio1() {
        Vehiculo v = MockDAOVehiculos.getInstance().getVehiculos().get(0);
        double precioDescuento = servicio.setDescuentoPrecio("ABC1234",50);

        double precioEsperado = v.getPrecio()-(v.getPrecio()*0.50);
        System.out.println(precioEsperado+" "+precioDescuento);
        assertEquals(precioEsperado,precioDescuento);
    }

    @Test
    void setDescuentoPrecio2() {
        Vehiculo v = MockDAOVehiculos.getInstance().getVehiculos().get(0);
        double precioDescuento = servicio.setDescuentoPrecio("ABC1234",5);

        double precioEsperado = v.getPrecio()-(v.getPrecio()*0.05);
        System.out.println(precioEsperado+" "+precioDescuento);
        assertEquals(precioEsperado,precioDescuento);
    }

    @Test
    void setDescuentoPrecio3() {
        Vehiculo v = MockDAOVehiculos.getInstance().getVehiculos().get(0);
        double precioDescuento = servicio.setDescuentoPrecio("ABC1234",25);

        double precioEsperado = v.getPrecio()-(v.getPrecio()*0.15);
        System.out.println(precioEsperado+" "+precioDescuento);
        assertEquals(precioEsperado,precioDescuento);
    }
}