package persistentie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domein.Speler;

public class SpelerMapper {

	// spelers aanmaken & returnen
	public List<Speler> geefSpelers() {
		List<Speler> spelers = new ArrayList<>();

		try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
				PreparedStatement query = conn.prepareStatement("SELECT * FROM ID344358_sdpig95.Gebruiker");
				ResultSet rs = query.executeQuery()) {

			while (rs.next()) {
				String gebruikersnaam = rs.getString("gebruikersnaam");
				String wachtwoord = rs.getString("wachtwoord");

				spelers.add(new Speler(gebruikersnaam, wachtwoord));
			}
		}
		catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return spelers;
	}

	public Speler controleerWachtwoordEnGeefSpeler(String gebruikersnaam, String wachtwoord) {
		// roept geefSpeler op, vergelijkt ww uit database met ingevoerd wachtwoord
		Speler s = geefSpeler(gebruikersnaam);
		// indien geen speler gevonden
		if (s == null) {
			return null;
		}
		if (s.getWachtwoord().equals(wachtwoord)) // validatie
			return s;

		return null;
	}

	public Speler geefSpeler(String gebruikersnaam) { // speler ophalen adhv gebruikersnaam
		Speler speler = null;

		try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
				PreparedStatement query = conn
						.prepareStatement("SELECT * FROM ID344358_sdpig95.Gebruiker WHERE gebruikersnaam = ?")) {
			query.setString(1, gebruikersnaam);
			try (ResultSet rs = query.executeQuery()) { // ook het ww wordt dus opgehaald
				if (rs.next()) {
					String wachtwoord = rs.getString("wachtwoord");
					speler = new Speler(gebruikersnaam, wachtwoord);
				}
			}
		}
		catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return speler; // gebruikersnaam & ww dus
	}

	// scores uit database halen en overzicht opmaken
	public String geefScoresOverzicht(List<Speler> spelers) {
		String uitvoer = "";

		for (int i = 0; i < spelers.size(); i++) {
			// lijst van 1 speler zijn scores
			List<Integer> scoresSpeler = new ArrayList<>();

			try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
					PreparedStatement query = conn.prepareStatement("SELECT * FROM ID344358_sdpig95.Score "
							+ "WHERE gebruikersnaam = \"" + spelers.get(i).getGebruikersnaam() + "\"");
					ResultSet rs = query.executeQuery()) {
				// lijst opvullen
				while (rs.next()) {
					scoresSpeler.add(rs.getInt("score"));
				}

				// bv "RemcoDeSmedt: "
				uitvoer += spelers.get(i).getGebruikersnaam() + ": ";

				// alle scores aan uitvoer toevoegen
				for (int j = 0; j < scoresSpeler.size(); j++) {
					uitvoer += String.format(" %d%s ", scoresSpeler.get(j), j == scoresSpeler.size() - 1 ? "" : ",");
				}

//				for (Integer integer : scoresSpeler) {
//					uitvoer += String.format(" %d ", integer);
//				}

				// = totaalscore
				uitvoer += String.format(" --> %d ", scoresSpeler.stream().reduce(0, (a, b) -> a + b));
				uitvoer += "\n";
			}
			catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		}

		return uitvoer;
	}

	// scores in databank steken
	public void voegScoresToe(List<Speler> spelers) {
		// voor elke speler de score toevoegen
		for (int i = 0; i < spelers.size(); i++) {
			try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
					PreparedStatement query = conn.prepareStatement(
							"INSERT INTO ID344358_sdpig95.Score (gebruikersnaam, score)" + "VALUES (?, ?)")) {
				query.setString(1, spelers.get(i).getGebruikersnaam());
				query.setInt(2, spelers.get(i).getScore());
				query.executeUpdate();
			}
			catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		}

	}

}
