package exceptions;

import java.util.Arrays;
import java.util.List;

public class DBExceptions extends Exception{
	public static final int ERROR_BBDD = 0;
	public static final int USUARIO_YA_EXISTE=1;
	public static final int WRONG_LOGIN=2;

	private int value;

	public DBExceptions(int value) {
		this.value = value;
	}

	private List<String> message = Arrays.asList(
			"<< Error en la conexión de la base de datos. >>",
			"<< Este usuario ya esta registrado en la base de datos, inicia sesión si quieres. >>",
			"<< Mala combinación usuario/contraseña. >>");

	@Override
	public String getMessage() {
		return message.get(value);
	}
}
