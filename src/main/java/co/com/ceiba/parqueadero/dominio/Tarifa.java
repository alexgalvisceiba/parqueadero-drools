package co.com.ceiba.parqueadero.dominio;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.PackageBuilder;
import org.drools.rule.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.com.ceiba.parqueadero.constantes.ConstantesParqueadero;
import co.com.ceiba.parqueadero.dominio.excepcion.NegocioException;
import co.com.ceiba.parqueadero.dominio.util.Validacion;
import co.com.ceiba.parqueadero.enums.TipoVehiculoEnum;

@Component
public class Tarifa {

	private Long codigoParqueadero;
	private TipoVehiculoEnum tipo;
	private BigDecimal valorHora;
	private BigDecimal valorDia;
	private BigDecimal cargoAdicional;
	private int topeVehiculo;

	@Autowired
	private Validacion val;

	private static final Logger LOGGER = Logger.getLogger(Tarifa.class);

	public Tarifa() {
		super();
	}

	public String obtenerTipoVehiculoParaParqueo(Tarifa t, String tipo) {
		String r = null;
		if (tipo != null) {
			if (TipoVehiculoEnum.CARRO.name().equalsIgnoreCase(tipo)) {
				t.setTipo(TipoVehiculoEnum.CARRO);
			} else if (TipoVehiculoEnum.MOTO.name().equalsIgnoreCase(tipo)) {
				t.setTipo(TipoVehiculoEnum.MOTO);
			}
		} else {
			r = ConstantesParqueadero.NO_VALIDO;
		}
		return r;
	}

	/**
	 * 
	 * @param placa
	 * @return
	 */
	public Boolean placaEmpiezaConA(String placa) {
		return placa.startsWith(ConstantesParqueadero.PLACA_A);
	}

	/**
	 * 
	 * @param placa
	 * @return
	 */
	public Boolean validarIngresoXPlaca(String placa) {
		Boolean retorno = true;
		Boolean placaA = placaEmpiezaConA(placa);
		Boolean vDia = val.diaNoLunesoDomingo(Calendar.getInstance());
		if (placaA && vDia) {
			retorno = false;
		}
		return retorno;
	}

	public Boolean topeAlcanzado(int tope) {
		return this.topeVehiculo <= tope;
	}

	/**
	 * 
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param t
	 * @return
	 */
	public BigDecimal obtenerCobro(Date fechaInicial, Date fechaFinal, Tarifa t) {
		long horas = val.obtenerHorasParqueo(fechaInicial, fechaFinal);
		long dias = horas / ConstantesParqueadero.HORAS_DIA;
		long hs = horas % ConstantesParqueadero.HORAS_DIA;
		BigDecimal cd = BigDecimal.valueOf(dias).multiply(t.getValorDia());
		BigDecimal ch;
		if (hs >= ConstantesParqueadero.TOPE_HORA) {
			ch = BigDecimal.ONE.multiply(t.getValorDia());
		} else {
			ch = BigDecimal.valueOf(hs).multiply(t.getValorHora());
		}
		return cd.add(ch);
	}

	public Tarifa ejecutarReglas(Tarifa t) {
		RuleBase ruleBase = leerReglas();
		WorkingMemory workingMemory = ruleBase.newStatefulSession();
		workingMemory.insert(t);
		workingMemory.fireAllRules();
		return t;
	}

	private RuleBase leerReglas() {
		RuleBase ruleBase = RuleBaseFactory.newRuleBase();
		// Leemos el archivo de reglas (DRL)
		try (Reader source = new InputStreamReader(
				getClass().getClassLoader().getResourceAsStream(ConstantesParqueadero.REGLAS_NEGOCIO))) {
			// Construimos un paquete de reglas
			PackageBuilder builder = new PackageBuilder();
			// Parseamos y compilamos las reglas en un unico paso
			builder.addPackageFromDrl(source);
			// Verificamos el builder para ver si hubo errores
			if (builder.hasErrors()) {
				LOGGER.error(builder.getErrors().toString());
				throw new NegocioException(ConstantesParqueadero.ERROR_CARGA_REGLAS);
			}
			// Obtenemos el package de reglas compilado
			Package pkg = builder.getPackage();
			// Agregamos el paquete a la base de reglas
			// (desplegamos el paquete de reglas).
			ruleBase.addPackage(pkg);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new NegocioException(ConstantesParqueadero.ERROR_CARGA_REGLAS);
		}
		return ruleBase;
	}

	/**
	 * @return the codigoParqueadero
	 */
	public Long getCodigoParqueadero() {
		return codigoParqueadero;
	}

	/**
	 * @param codigoParqueadero
	 *            the codigoParqueadero to set
	 */
	public void setCodigoParqueadero(Long codigoParqueadero) {
		this.codigoParqueadero = codigoParqueadero;
	}

	/**
	 * @return the tipo
	 */
	public TipoVehiculoEnum getTipo() {
		return tipo;
	}

	/**
	 * @param tipo
	 *            the tipo to set
	 */
	public void setTipo(TipoVehiculoEnum tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the valorHora
	 */
	public BigDecimal getValorHora() {
		return valorHora;
	}

	/**
	 * @param valorHora
	 *            the valorHora to set
	 */
	public void setValorHora(BigDecimal valorHora) {
		this.valorHora = valorHora;
	}

	/**
	 * @return the valorDia
	 */
	public BigDecimal getValorDia() {
		return valorDia;
	}

	/**
	 * @param valorDia
	 *            the valorDia to set
	 */
	public void setValorDia(BigDecimal valorDia) {
		this.valorDia = valorDia;
	}

	/**
	 * @return the cargoAdicional
	 */
	public BigDecimal getCargoAdicional() {
		return cargoAdicional;
	}

	/**
	 * @param cargoAdicional
	 *            the cargoAdicional to set
	 */
	public void setCargoAdicional(BigDecimal cargoAdicional) {
		this.cargoAdicional = cargoAdicional;
	}

	/**
	 * @return the topeVehiculo
	 */
	public int getTopeVehiculo() {
		return topeVehiculo;
	}

	/**
	 * @param topeVehiculo
	 *            the topeVehiculo to set
	 */
	public void setTopeVehiculo(int topeVehiculo) {
		this.topeVehiculo = topeVehiculo;
	}

}
