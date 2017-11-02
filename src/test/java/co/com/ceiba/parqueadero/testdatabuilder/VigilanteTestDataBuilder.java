package co.com.ceiba.parqueadero.testdatabuilder;

import org.mockito.Mockito;

import co.com.ceiba.parqueadero.dominio.Tarifa;
import co.com.ceiba.parqueadero.dominio.Vigilante;
import co.com.ceiba.parqueadero.persistencia.repositorio.IParqueaderoJPA;
import co.com.ceiba.parqueadero.persistencia.repositorio.IRegistroJPA;
import co.com.ceiba.parqueadero.persistencia.repositorio.ITipoVehiculoJPA;
import co.com.ceiba.parqueadero.persistencia.repositorio.IVehiculoJPA;

public class VigilanteTestDataBuilder {

	private IParqueaderoJPA pBO;
	private IRegistroJPA rBO;
	private IVehiculoJPA vBO;
	private ITipoVehiculoJPA tvBO;
	private Tarifa t;

	public static final IParqueaderoJPA PBO = Mockito.mock(IParqueaderoJPA.class);
	public static final IRegistroJPA RBO = Mockito.mock(IRegistroJPA.class);
	public static final IVehiculoJPA VBO = Mockito.mock(IVehiculoJPA.class);
	public static final ITipoVehiculoJPA TVBO = Mockito.mock(ITipoVehiculoJPA.class);
	public static final Tarifa T = Mockito.mock(Tarifa.class);

	public VigilanteTestDataBuilder() {
		this.pBO = PBO;
		this.rBO = RBO;
		this.vBO = VBO;
		this.tvBO = TVBO;
		this.t = T;
	}
	
	public VigilanteTestDataBuilder conTarifa(Tarifa t) {
		this.t = t;
		return this;
	}
	
	public VigilanteTestDataBuilder conPBO(IParqueaderoJPA pBO) {
		this.pBO = pBO;
		return this;
	}
	
	public VigilanteTestDataBuilder conRBO(IRegistroJPA rBO) {
		this.rBO = rBO;
		return this;
	}
	
	public VigilanteTestDataBuilder conVBO(IVehiculoJPA vBO) {
		this.vBO = vBO;
		return this;
	}

	public VigilanteTestDataBuilder conTVBO(ITipoVehiculoJPA tvBO) {
		this.tvBO = tvBO;
		return this;
	}
	
	public Vigilante build() {
		return new Vigilante(pBO, rBO, vBO, tvBO, t);
	}
}
