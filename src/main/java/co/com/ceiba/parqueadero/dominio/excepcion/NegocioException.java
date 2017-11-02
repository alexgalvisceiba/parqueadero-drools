package co.com.ceiba.parqueadero.dominio.excepcion;

public class NegocioException  extends RuntimeException {

	/**
	 * Constante de serializacion.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor de la clase.
	 * 
	 * @param message
	 *            el mensaje de error de acuerdo a reglas de negocio.
	 */
	public NegocioException(String message) {
		super(message);
	}

}
