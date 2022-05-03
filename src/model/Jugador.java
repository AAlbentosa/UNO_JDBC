package model;

public class Jugador {
	private int id;
	private String username;
	
	public Jugador(int id, String username) {
		this.id=id;
		this.username=username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "Jugador [id=" + id + ", username=" + username + "]";
	}
	
	
}
