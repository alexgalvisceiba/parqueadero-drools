package co.com.ceiba.parqueadero.dominio;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.com.ceiba.parqueadero.constantes.ConstantesParqueadero;
import co.com.ceiba.parqueadero.dominio.excepcion.IngresoException;
import co.com.ceiba.parqueadero.enums.TipoVehiculoEnum;
import co.com.ceiba.parqueadero.persistencia.entidad.ParqueaderoEntidad;
import co.com.ceiba.parqueadero.persistencia.entidad.RegistroEntidad;
import co.com.ceiba.parqueadero.persistencia.entidad.TipoVehiculoEntidad;
import co.com.ceiba.parqueadero.persistencia.entidad.VehiculoEntidad;
import co.com.ceiba.parqueadero.persistencia.repositorio.IParqueaderoJPA;
import co.com.ceiba.parqueadero.persistencia.repositorio.IRegistroJPA;
import co.com.ceiba.parqueadero.persistencia.repositorio.ITipoVehiculoJPA;
import co.com.ceiba.parqueadero.persistencia.repositorio.IVehiculoJPA;

@Component
public class Vigilante {

	@Autowired
	private IParqueaderoJPA pBO;
	@Autowired
	private IRegistroJPA rBO;
	@Autowired
	private IVehiculoJPA vBO;
	@Autowired
	private ITipoVehiculoJPA tvBO;
	@Autowired
	private Tarifa t;

	/**
	 * Constructor explicito.
	 */
	public Vigilante() {
		super();
	}

	/**
	 * 
	 * @param pBO
	 * @param rBO
	 * @param vBO
	 * @param tvBO
	 * @param t
	 */
	public Vigilante(IParqueaderoJPA pBO, IRegistroJPA rBO, IVehiculoJPA vBO, ITipoVehiculoJPA tvBO, Tarifa t) {
		super();
		this.pBO = pBO;
		this.rBO = rBO;
		this.vBO = vBO;
		this.tvBO = tvBO;
		this.t = t;
	}

	/**
	 * Permite registrar el ingreso de un vehiculo en el parqueadero.
	 * 
	 * @param ve
	 *            el vehiculo
	 * @param pe
	 *            el parqueadero
	 * @return
	 */
	public Boolean registrarIngreso(String nombreParqueadero, String placa, String tipo, String cilindraje) {
		Boolean retorno = false;
		String tp = t.obtenerTipoVehiculoParaParqueo(t, tipo);
		if (tp != null) {
			throw new IngresoException(tp);
		}
		TipoVehiculoEnum tve = TipoVehiculoEnum.valueOf(tipo);
		if (t.validarIngresoXPlaca(placa)) {
			VehiculoEntidad v = validarVehiculo(placa, cilindraje, tve);
			ParqueaderoEntidad p = validarParqueadero(nombreParqueadero);
			List<RegistroEntidad> lr = rBO.obtenerParqueados(v.getTipo());
			t = t.ejecutarReglas(t);
			if (t.topeAlcanzado(lr == null ? 0 : lr.size())) {
				throw new IngresoException(ConstantesParqueadero.NO_CUPO);
			} else {
				RegistroEntidad re = new RegistroEntidad().conFechaIngreso(Calendar.getInstance().getTime())
						.conVehiculo(v).conParqueadero(p);
				rBO.save(re);
				retorno = true;
			}
		} else {
			throw new IngresoException(ConstantesParqueadero.NO_PUEDE_INGRESAR);
		}
		return retorno;
	}

	/**
	 * Permite registrar la salida de un vehiculo en el parqueadero.
	 * 
	 * @param nombreParqueadero
	 * @param placa
	 */
	public void registrarSalida(String placa) {
		VehiculoEntidad v = vBO.findOne(placa);
		List<RegistroEntidad> lr = rBO.obtenerXVehiculo(v);
		if (lr != null && !lr.isEmpty()) {
			t.ejecutarReglas(t);
			RegistroEntidad re = lr.get(0);
			Date fechaSalida = new Date();
			BigDecimal cobro = t.obtenerCobro(re.getFechaIngreso(), fechaSalida, t);
			re.conCobro(cobro).conFechaSalida(fechaSalida);
			rBO.save(re);
		}
	}

	private VehiculoEntidad validarVehiculo(String placa, String cilindraje, TipoVehiculoEnum tipo) {
		VehiculoEntidad v = vBO.findOne(placa);
		if (v == null) {
			v = vBO.save(new VehiculoEntidad().conPlaca(placa).conCilindraje(Long.parseLong(cilindraje))
					.conTipo(validarTipoVehiculo(tipo)));
		}
		return v;
	}

	private TipoVehiculoEntidad validarTipoVehiculo(TipoVehiculoEnum tipo) {
		TipoVehiculoEntidad tv = tvBO.obtenerEntidadXNombre(tipo);
		if (tv == null) {
			tv = tvBO.save(new TipoVehiculoEntidad().conNombre(tipo));
		}
		return tv;
	}

	private ParqueaderoEntidad validarParqueadero(String nombreParqueadero) {
		ParqueaderoEntidad p = pBO.findByNombre(nombreParqueadero);
		if (p == null) {
			p = pBO.save(new ParqueaderoEntidad().conNombre(nombreParqueadero));
		}
		return p;
	}

}
