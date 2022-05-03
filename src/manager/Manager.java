package manager;

import java.util.ArrayList;
import java.util.Scanner;
import dao.MysqlCon;
import dao.Querys;
import exceptions.DBExceptions;
import exceptions.LogicExceptions;
import gui.CartaFrame;
import model.Carta;
import model.Jugador;
import utils.CheckInput;

public class Manager {
	private MysqlCon conn;
	private Querys querys;
	private Jugador jugador;
	private ArrayList<Carta> mano;
	private Carta ultimaCarta;
	private Scanner sc;
	
	public Manager() {
		mano=new ArrayList<>();
		sc=new Scanner(System.in);
	}
	
	public void init() {
		try {
			conn=new MysqlCon();
			querys=new Querys(conn.getConnection());
			jugador=Autentication();
			
			if(jugador!=null) {
				start();
				playCard();
				checkJugador();
			}
		} catch (DBExceptions | LogicExceptions e) {
			System.out.println(e.getMessage());
		}
	}
	
	private Jugador Autentication() throws DBExceptions {
		System.out.println("Elige que quieres hacer\n1.-Login\n2.-Registro ");
		int option=CheckInput.checkInput(1, 2);
		if(option==1)
			return signIn();
		else 
			return signUp();
	}
	
	private Jugador signUp() throws DBExceptions {
		System.out.println("Introduce tu nombre de usuario:");
		String username=sc.nextLine();
		System.out.println("Introduce tu contraseña");
		String password=sc.nextLine();
		System.out.println("Introduce tu nombre");
		String name=sc.nextLine();
		return querys.signUp(username, password, name);
	}

	private Jugador signIn() throws DBExceptions {
		System.out.println("Introduce tu nombre de usuario:");
		String username=sc.nextLine();
		System.out.println("Introduce tu contraseña");
		String password=sc.nextLine();
		return querys.signIn(username, password);
	}

	private void start() throws DBExceptions, LogicExceptions {
		ultimaCarta=querys.getLastCardUsed();
		mano=querys.getMano(jugador.getId());
		
		if(mano==null) {
			mano=new ArrayList<>();
			for(int x=0; x<7; x++)
				mano.add(Carta.getCard(querys, jugador.getId()));
		}

		if(ultimaCarta!=null) {
			showCard();
			checkCard(ultimaCarta);
		}else {
			System.out.println("No hay cartas en juego, inicia el juego con cualquier carta.");
		}
	}
	
	private void showCard() {
		CartaFrame frame=new CartaFrame();
		frame.showImage(ultimaCarta.getColor().toLowerCase(), ultimaCarta.getNumero().toLowerCase());
		
	}

	private void checkJugador() throws DBExceptions {
		if(mano.size()==0) {
			querys.addVictory(jugador.getId());
			querys.addGameToAllPlayers();
			querys.resetDb();
		}
	}

	private void playCard() throws DBExceptions, LogicExceptions {
		int option;
	
		do {
			showCards();
			option=CheckInput.checkInput(-1, mano.size()-1);
			if(option==-1 || ultimaCarta==null)
				break;
		}while(!mano.get(option).cardIsPlayable(ultimaCarta));
		
		if(option==-1) {
			mano.add(Carta.getCard(querys, jugador.getId()));
			playCard();
		}else {
			querys.playCard(mano.get(option));
			mano.remove(option);
		}
		throw new LogicExceptions(LogicExceptions.FINAL_TURNO);
	}

	private void showCards() {
		System.out.println("Selecciona una carta para jugar:");
		for(int x=0; x<mano.size(); x++)
			System.out.println(x+".-"+mano.get(x).toString());
		System.out.println("-1.-Robar carta");
	}

	private void checkCard(Carta carta) throws DBExceptions, LogicExceptions {
		switch(carta.getNumero()) {
			case "MASDOS":
				if(playMasCard())
					throw new LogicExceptions(LogicExceptions.FINAL_TURNO);
				for(int x=0; x<2; x++)
					mano.add(Carta.getCard(querys, jugador.getId()));
				break;
			case "MASCUATRO":
				if(playMasCard())
					throw new LogicExceptions(LogicExceptions.FINAL_TURNO);
				for(int x=0; x<4; x++)
					mano.add(Carta.getCard(querys, jugador.getId()));
				break;
			case "CAMBIOCOLOR":
			case "SALTO":
			case "CAMBIO":
				querys.removeCardFromGame(ultimaCarta.getId());
				throw new LogicExceptions(LogicExceptions.SALTO_TURNO);
		}
	}

	private boolean playMasCard() throws DBExceptions {
		int count=0;
		ArrayList<Carta> aux=new ArrayList<>();
		
		for(int x=0; x<mano.size(); x++)
			if((mano.get(x).getNumero().equals(ultimaCarta.getNumero()) &&  mano.get(x).getColor().equals(ultimaCarta.getColor())) || 
					(ultimaCarta.getNumero().equals("MASDOS") && mano.get(x).getNumero().equals("MASCUATRO")))
				count++;

		if(count>0) {
			int index=0;
			System.out.println("Tienes una carta del mismo tipo, si la tiras te ahorras el robar, que quieres hacer?");
			
			for(Carta c:mano)
				if((c.getNumero().equals(ultimaCarta.getNumero()) &&  c.getColor().equals(ultimaCarta.getColor()))|| 
						(ultimaCarta.getNumero().equals("MASDOS") && c.getNumero().equals("MASCUATRO"))) {
					System.out.println(index+".-"+c.toString());
					aux.add(c);
				}
			System.out.println("-1 .- Tirar otra carta.");
			
			int option=CheckInput.checkInput(-1, count-1);
			
			if(option==-1)
				return false;
			else {
				querys.playCard(aux.get(option));
				mano.remove(aux.get(option));
				return true;
			}	
		}else 
			return false;
	}
}
