package model;

import dao.Querys;
import exceptions.DBExceptions;

public class Carta {
	private String color;
	private String numero;
	private int id;
	
	public Carta(int id, String color, String numero) {
		this.id=id;
		this.color = color;
		this.numero = numero;
	}
	
	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return color+", "+numero;
	}

	public static Carta getCard(Querys querys, int id) throws DBExceptions {
		String randomNumber=Numero.getRandomNum();
		String randomColor=Color.getRandomColor(randomNumber);

		int cardid=querys.createCard(randomNumber, randomColor, id);
		return new Carta(cardid, randomColor, randomNumber);
	}

	
	public boolean cardIsPlayable(Carta ultimaCarta) {
		if(ultimaCarta==null)
			return true;
		
		if(this.color.equals(ultimaCarta.getColor()) || ultimaCarta.getColor().equals("NEGRO") || this.color.equals("NEGRO"))
			return true;
		else {
			System.out.println("Debes jugar una carta de color "+ultimaCarta.getColor()+" o robar una carta con -1.");
			return false;
		}
	}
}
