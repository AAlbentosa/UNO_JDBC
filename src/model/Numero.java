package model;

import java.util.Random;

public enum Numero {
	CERO, UNO, DOS, TRES, CUATRO, CINCO, SEIS, SIETE, OCHO, NUEVE, CAMBIO, MASDOS, SALTO, CAMBIOCOLOR, MASCUATRO;
	
	public static String getRandomNum() {
        int randIndex = new Random().nextInt(Numero.values().length);
        return Numero.values()[randIndex].toString();
    }
}
