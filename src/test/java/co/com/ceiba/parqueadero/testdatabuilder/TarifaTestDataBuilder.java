package co.com.ceiba.parqueadero.testdatabuilder;

import co.com.ceiba.parqueadero.constantes.ConstantesParqueadero;
import co.com.ceiba.parqueadero.dominio.Tarifa;

public class TarifaTestDataBuilder {
	
	private int topeVehiculo;
	
	public static final int TOPEV0 = 0;
	public static final int TOPEV1 = ConstantesParqueadero.TOPE_CARRO;
	public static final int TOPEV2 = ConstantesParqueadero.TOPE_MOTO;
	
	public TarifaTestDataBuilder() {
		this.topeVehiculo = TOPEV0;
	}
	
	public TarifaTestDataBuilder conTope(int tope) {
		this.topeVehiculo = tope;
		return this;
	}
	
	public Tarifa build() {
		Tarifa t = new Tarifa();
		t.setTopeVehiculo(topeVehiculo);
		return t;
	}
}
