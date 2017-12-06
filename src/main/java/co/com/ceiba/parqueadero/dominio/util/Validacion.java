package co.com.ceiba.parqueadero.dominio.util;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import co.com.ceiba.parqueadero.constantes.ConstantesParqueadero;

@Component
public class Validacion {

	/**
	 * 
	 * @return TRUE si ya ha pasado el Lunes.
	 * @param dia
	 */
	public  Boolean diaNoLunesoDomingo(Calendar dia) {
		int diaSemana = dia.get(Calendar.DAY_OF_WEEK);
		return diaSemana > ConstantesParqueadero.LUNES;
	}

	/**
	 * Permite obtener la diferencia entre las fechas en horas.
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public  long obtenerHorasParqueo(Date d1, Date d2) {
		long duration = d2.getTime() - d1.getTime();
		return TimeUnit.MILLISECONDS.toHours(duration);
	}

}
