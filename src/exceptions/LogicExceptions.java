package exceptions;

import java.util.Arrays;
import java.util.List;

public class LogicExceptions extends Exception {
	public static final int SALTO_TURNO = 0;
	public static final int FINAL_TURNO = 1;
	
	private int value;

	public LogicExceptions(int value) {
		this.value = value;
	}

	private List<String> message = Arrays.asList(
			"<< Salto / Cambio de turno. >>",
			"<< Final de turno >>");

	@Override
	public String getMessage() {
		return message.get(value);
	}
}
