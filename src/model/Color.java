package model;

import java.util.Random;

public enum Color {
	ROJO, AMARILLO, VERDE, AZUL, NEGRO;
	
	public static String getRandomColor(String numero) {
		String color;
		
		switch(numero) {
			case "MASCUATRO":
			case "CAMBIOCOLOR":
				color="NEGRO";
				break;
			default:
				int randIndex=new Random().nextInt(Color.values().length-1);
				color=Color.values()[randIndex].toString();
				break;
		}
        return color;
    }
}
