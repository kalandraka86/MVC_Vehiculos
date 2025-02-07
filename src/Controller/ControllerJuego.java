package Controller;


import javax.swing.JOptionPane;
import Recursos.GestionVehiculos;
import Vista.PanelJuego;

import java.sql.SQLException;

public class ControllerJuego{
	
	private GestionVehiculos gestionVehiculos;
	
	private PanelJuego panelJuego;
	
	public ControllerJuego(PanelJuego panelJuego) throws SQLException {
		
		gestionVehiculos = new GestionVehiculos();
		this.panelJuego= panelJuego;
	}

	public  void nuevoCoche()
	{ panelJuego.etiSecreta.setText(gestionVehiculos.getVehiculo());
		
	}
	
	public  void comprobarCoche()
	{ String palabra =  panelJuego.txtRespuesta.getText().toString();
		if (gestionVehiculos.compara(palabra)) 
			JOptionPane.showMessageDialog(panelJuego, "Correcto");
		else JOptionPane.showMessageDialog(panelJuego, "Incorrecto");
		
	}

}
