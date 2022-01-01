package domein;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Speler {
	private String gebruikersnaam;
	private String wachtwoord;
	private int score;
	private boolean legtEersteKeer = true;
	private List<Steen> eigenStenen;
	private List<Steen> eigenStenenVoorBeurt;
	private List<Steen> stenenWerkveld;
	private List<Steen> gelegdeStenen;

	// constructor
	public Speler(String gebruikersnaam, String wachtwoord) {
		setGebruikersnaam(gebruikersnaam);
		setWachtwoord(wachtwoord);

		gelegdeStenen = new ArrayList<>();
		stenenWerkveld = new ArrayList<>();
	}

	public void addSteenWerkveld(Steen steen) {
		this.stenenWerkveld.add(steen);
	}

	public void resetWerkveld() {
		this.stenenWerkveld = new ArrayList<>();
	}

	public void sorteerEigenStenen() {
		this.eigenStenen.sort(Comparator.comparing(Steen::getKleur).thenComparing(Steen::getGetal));
	}

	// meth
	// als zijn stenen op zijn, is hij gewonnen en is het spel gedaan
	public boolean isGewonnen() {
		if (eigenStenen.isEmpty()) {
			return true;
		}
		return false;
	}

	// score bepalen adhv stenen
	public void bepaalScore() {
		int score = 0;

		// voor verliezer, winnaar heeft score 0 (zijn stenen zijn op)
		if (!isGewonnen()) {
			for (Steen steen : eigenStenen) {
				score -= steen.getGetal();
			}
		}

		setScore(score);
	}

	public String getEigenStenenAlsString() {
		String uitvoer = "\n\t";

		for (int i = 0; i < eigenStenen.size(); i++) {
			if (i > 0) {
				// als de kleur veranderd: new line
				if (eigenStenen.get(i - 1).getKleur() != eigenStenen.get(i).getKleur()) {
					uitvoer += "\n\t";
				}
			}
			uitvoer += String.format("%3d: %-10s", i+1, eigenStenen.get(i).toString());
		}

		return uitvoer;

//		String uitvoer = "";
//		int i = 1;
//		for (Steen steen : eigenStenen) {
//			uitvoer += String.format("%3d: %-10s", i, steen.toString());
//			i++;
//		}
//		return uitvoer;
	}
	
	public List<Steen> geefStenen() {
		
		return eigenStenen;
	}

	public String getStenenWerkveldAlsString() {
		String uitvoer = "";

		int i = 1;
		for (Steen steen : stenenWerkveld) {
			if (steen != null) {
				uitvoer += String.format("%3d:%-10s", i, steen.toString());
				i++;
			}
		}

		return uitvoer;
	}

	public void addEigenSteen(Steen steen) {
		eigenStenen.add(steen);
		sorteerEigenStenen();
	}

	public void saveEigenStenenVoorBeurt() {
		// eerst is leegmaken
		eigenStenenVoorBeurt = new ArrayList<>();
		// dan opvullen
		for (Steen steen : eigenStenen) {
			eigenStenenVoorBeurt.add(steen);
		}
	}

	public void resetEigenStenen() {
		// leegmaken
		eigenStenen = new ArrayList<Steen>();
		// opvullen
		for (Steen steen : eigenStenenVoorBeurt) {
			eigenStenen.add(steen);
		}
	}

	public void addGelegdeSteen(Steen steen) {
		this.gelegdeStenen.add(steen);
	}

	public void resetGelegdeStenen() {
		this.gelegdeStenen = new ArrayList<>();
	}

	// getters & setters
	public String getGebruikersnaam() {
		return gebruikersnaam;
	}

	private void setGebruikersnaam(String gebruikersnaam) {
		this.gebruikersnaam = gebruikersnaam;
	}

	public String getWachtwoord() {
		return wachtwoord;
	}

	private void setWachtwoord(String wachtwoord) {
		this.wachtwoord = wachtwoord;
	}

	public List<Steen> getEigenStenen() {
		return eigenStenen;
	}

	public void setEigenStenen(List<Steen> stenen) {
		this.eigenStenen = stenen;
		sorteerEigenStenen();
	}

	public boolean LegtEersteKeer() {
		return legtEersteKeer;
	}

	public void setLegtEersteKeer(boolean legtEersteKeer) {
		this.legtEersteKeer = legtEersteKeer;
	}

	public List<Steen> getGelegdeStenen() {
		return gelegdeStenen;
	}

	public List<Steen> getStenenWerkveld() {
		return stenenWerkveld;
	}

	public void setStenenWerkveld(List<Steen> stenenWerkveld) {
		this.stenenWerkveld = stenenWerkveld;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
