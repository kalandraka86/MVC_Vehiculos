package Recursos;

import java.util.Objects;
import java.util.Random;

public class Vehiculo {

	private String marca;

	private String modelo;

	private String matricula;

	private String dniCliente;
	private Random r = new Random();
	private float precio;

	public Vehiculo() {
	}

	public Vehiculo(String marca, String modelo, String matricula, String dniCliente,float precio) {
		this.marca = marca;
		this.modelo = modelo;
		this.matricula = matricula;
		this.dniCliente = dniCliente;
		this.precio = precio;
	}

	public Vehiculo(String marca, String modelo, String matricula, String dniCliente) {
		this.marca = marca;
		this.modelo = modelo;
		this.matricula = matricula;
		this.dniCliente = dniCliente;
	}

	public Vehiculo(String marca, String modelo, String matricula) {
		this.marca = marca;
		this.modelo = modelo;
		this.matricula = matricula;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getMatricula() {
		return matricula;
	}

	public float getPrecio() {
		return precio;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Vehiculo vehiculo = (Vehiculo) o;
		return Objects.equals(marca, vehiculo.marca) && Objects.equals(modelo, vehiculo.modelo) && Objects.equals(matricula, vehiculo.matricula);
	}

	public String getDniCliente() {
		return dniCliente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(marca, modelo, matricula);
	}

	@Override
	public String toString() {
		return "Vehiculo [marca=" + marca + ", modelo=" + modelo + ", matricula=" + matricula + "]";
	}


}