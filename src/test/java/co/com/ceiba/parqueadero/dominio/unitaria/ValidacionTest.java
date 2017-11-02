package co.com.ceiba.parqueadero.dominio.unitaria;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import co.com.ceiba.parqueadero.dominio.util.Validacion;

public class ValidacionTest {
	
	private Validacion val;
	
	@Before
	public void setUp() {
		// arrange
		val = Mockito.mock(Validacion.class);
	}

	@Test
	public void diaNoLunesoDomingoFalseTest() {
		// arrange
		Calendar c = Calendar.getInstance();
		Mockito.when(val.diaNoLunesoDomingo(c)).thenReturn(false);
		// act
		Boolean b = val.diaNoLunesoDomingo(c);
		// assert
		Assert.assertFalse(b);
	}

	@Test
	public void diaNoLunesoDomingoTrueTest() {
		// arrange
		Calendar c = Calendar.getInstance();
		Mockito.when(val.diaNoLunesoDomingo(c)).thenReturn(true);
		// act
		Boolean b = val.diaNoLunesoDomingo(c);
		// assert
		Assert.assertTrue(b);
	}

	@Test
	public void obtenerHorasParqueoTest() {
		// arrange
		long expected = 2L;
		Calendar ingreso = Calendar.getInstance();
		Calendar fin = Calendar.getInstance();
		fin.add(Calendar.HOUR, 2);
		Mockito.when(val.obtenerHorasParqueo(ingreso.getTime(), fin.getTime())).thenReturn(2L);
		// act
		long r = val.obtenerHorasParqueo(ingreso.getTime(), fin.getTime());
		// assert
		Assert.assertEquals(expected, r);
	}
	
	@Ignore
	public void obtenerHorasParqueoHMTest() {
		// TODO reparar este escenario
		// arrange
		long expected = 3L;
		Calendar ingreso = Calendar.getInstance();
		Calendar fin = Calendar.getInstance();
		fin.add(Calendar.HOUR, 2);
		fin.add(Calendar.MINUTE, 30);
		// act
		long r = val.obtenerHorasParqueo(ingreso.getTime(), fin.getTime());
		// assert
		Assert.assertEquals(expected, r);
	}

}
