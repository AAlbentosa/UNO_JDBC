package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import exceptions.DBExceptions;
import model.Carta;
import model.Jugador;

public class Querys {
	
		private Connection conn;
		
		private static final String LOGIN = "SELECT id, usuario FROM jugador WHERE usuario = ? AND password = ? LIMIT 1";
		private static final String CREATE_PLAYER="INSERT INTO jugador (usuario, password, nombre, partidas, ganadas) VALUES (?, ?, ?, ?, ?)";
		private static final String CHECK_IF_USERNAME_EXIST="SELECT id FROM jugador WHERE usuario=? LIMIT 1";
		private static final String GET_LAST_CARD = "SELECT id_carta FROM partida ORDER BY id DESC LIMIT 1";
		private static final String GET_CARD_BY_ID = "SELECT color, numero FROM carta WHERE id=? LIMIT 1";
		private static final String CREATE_CARD = "INSERT INTO carta (id_jugador, numero, color) VALUES (?, ?, ?)";
		private static final String ADD_CARD_TO_GAME="INSERT INTO partida (id_carta) VALUES (?)";
		private static final String GET_HAND="SELECT carta.id, carta.color, carta.numero  FROM carta WHERE carta.id NOT IN (SELECT id_carta FROM partida) AND carta.id_jugador=?";
		private static final String ADD_VICTORY="UPDATE jugador SET ganadas=ganadas+1 WHERE id=? LIMIT 1";
		private static final String ADD_GAME_TO_ALL_PLAYERS="UPDATE jugador SET partidas=partidas+1";
		private static final String RESET_PARTIDA="DELETE FROM partida";
		private static final String RESET_CARTA="DELETE FROM carta";
		private static final String REMOVE_CARD_FROM_CARTA="DELETE FROM carta WHERE id=?";
		private static final String REMOVE_CARD_FROM_PARTIDA="DELETE FROM partida WHERE id_carta=?";
		
		
		public Querys(Connection connection) {
				conn=connection;
		}
		
		public Jugador signIn(String username, String password) throws DBExceptions {
			Jugador jugador;
			
			try {
				PreparedStatement sentence= conn.prepareStatement(LOGIN);
				sentence.setString(1, username);
				sentence.setString(2, password);
				ResultSet result = sentence.executeQuery();
				if(result.next()) {
					jugador=new Jugador(result.getInt(1), result.getString(2));
					return jugador;
				}else {
					throw new DBExceptions(DBExceptions.WRONG_LOGIN);
				}
			} catch (SQLException e) {
				throw new DBExceptions(DBExceptions.ERROR_BBDD);
			}
		}
		
		public Carta getLastCardUsed() throws DBExceptions {
			
			try {
				PreparedStatement sentence= conn.prepareStatement(GET_LAST_CARD);
				ResultSet result = sentence.executeQuery();
				if(result.next()) {
					int carta_id=result.getInt(1);
					sentence= conn.prepareStatement(GET_CARD_BY_ID);
					sentence.setInt(1, carta_id);
					result = sentence.executeQuery();
					if(result.next())
						return new Carta(carta_id, result.getString(1), result.getString(2));
				}
			} catch (SQLException e) {
				throw new DBExceptions(DBExceptions.ERROR_BBDD);
			}
			return null;
			
		}

		public int createCard(String numero, String color, int id) throws DBExceptions {
			try {
				PreparedStatement sentence= conn.prepareStatement(CREATE_CARD, PreparedStatement.RETURN_GENERATED_KEYS);
				sentence.setInt(1, id);
				sentence.setString(2, numero);
				sentence.setString(3, color);
				sentence.executeUpdate();
		
				ResultSet card_id = sentence.getGeneratedKeys();  
				int key = card_id.next() ? card_id.getInt(1) : 0;
				return key;
				
			} catch (SQLException e) {
				throw new DBExceptions(DBExceptions.ERROR_BBDD);
			}
			
		}

		public ArrayList<Carta> getMano(int id) throws DBExceptions {
			ArrayList<Carta> mano=new ArrayList<>();
			try {
				PreparedStatement sentence= conn.prepareStatement(GET_HAND);
				sentence.setInt(1, id);
				ResultSet result = sentence.executeQuery();
				while(result.next()){
					mano.add(new Carta(result.getInt(1), result.getString(2), result.getString(3)));
				}
			} catch (SQLException e) {
				throw new DBExceptions(DBExceptions.ERROR_BBDD);
			}
			
			if(mano.size()>0)
				return mano;
			else
				return null;
		}

		public void playCard(Carta carta) throws DBExceptions {
			try {
				PreparedStatement sentence= conn.prepareStatement(ADD_CARD_TO_GAME);
				sentence.setInt(1, carta.getId());
				sentence.executeUpdate();
				
			} catch (SQLException e) {
				throw new DBExceptions(DBExceptions.ERROR_BBDD);
			}
		}

		public void addVictory(int id) throws DBExceptions {
			try {
				PreparedStatement sentence= conn.prepareStatement(ADD_VICTORY);
				sentence.setInt(1, id);
				sentence.executeUpdate();
				
			} catch (SQLException e) {
				throw new DBExceptions(DBExceptions.ERROR_BBDD);
			}
		}

		public void addGameToAllPlayers() throws DBExceptions {
			try {
				PreparedStatement sentence= conn.prepareStatement(ADD_GAME_TO_ALL_PLAYERS);
				sentence.executeUpdate();
				
			} catch (SQLException e) {
				throw new DBExceptions(DBExceptions.ERROR_BBDD);
			}
		}

		public void resetDb() throws DBExceptions {
			try {
				PreparedStatement sentence= conn.prepareStatement(RESET_PARTIDA);
				sentence.executeUpdate();
				
				sentence= conn.prepareStatement(RESET_CARTA);
				sentence.executeUpdate();
				
			} catch (SQLException e) {
				throw new DBExceptions(DBExceptions.ERROR_BBDD);
			}
			
		}

		public void removeCardFromGame(int card_id) throws DBExceptions {
			try {
				PreparedStatement sentence= conn.prepareStatement(REMOVE_CARD_FROM_CARTA);
				sentence.setInt(1, card_id);
				sentence.executeUpdate();
				
				sentence= conn.prepareStatement(REMOVE_CARD_FROM_PARTIDA);
				sentence.setInt(1, card_id);
				sentence.executeUpdate();
				
			} catch (SQLException e) {
				throw new DBExceptions(DBExceptions.ERROR_BBDD);
			}
		}

		public Jugador signUp(String username, String password, String nombre) throws DBExceptions {
			try {
				PreparedStatement sentence= conn.prepareStatement(CHECK_IF_USERNAME_EXIST);
				sentence.setString(1, username);
				ResultSet result = sentence.executeQuery();
				if(result.next()) {
					throw new DBExceptions(DBExceptions.USUARIO_YA_EXISTE);
				}else {
					sentence= conn.prepareStatement(CREATE_PLAYER, PreparedStatement.RETURN_GENERATED_KEYS);
					sentence.setString(1, username);
					sentence.setString(2, password);
					sentence.setString(3, nombre);
					sentence.setInt(4, 0);
					sentence.setInt(5, 0);
					sentence.executeUpdate();
					
					ResultSet key = sentence.getGeneratedKeys();  
					int id = key.next() ? key.getInt(1) : 0;
					Jugador jugador=new Jugador(id, username);
					return jugador;
				}
			} catch (SQLException e) {
				throw new DBExceptions(DBExceptions.ERROR_BBDD);
			}
		}
}

