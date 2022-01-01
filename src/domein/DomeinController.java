package domein;

import java.util.ArrayList;
import java.util.List;

import exceptions.AlAangemeldException;
import exceptions.VerkeerdeGegevensException;

public class DomeinController {

	private SpelerRepository spelerRepository;
	private int aantalSpelers;
	private Spel spel;
	private List<Speler> spelers; // lijst spelers om spel te starten

	// constructor
	public DomeinController() {
		spelerRepository = new SpelerRepository();
		spelers = new ArrayList<>();
	}

	public String geefScoresOverzicht() {
		return spelerRepository.geefScoresOverzicht(spelers);
	}

	public void voegScoresToe() {
		spelerRepository.voegScoresToe(spelers);
	}

	public void setStenenPotNull() {
		spel.setStenenPotNull();
	}

	public void vervangJokerPersSteen(int rij, int kolom, int steen) {
		spel.vervangJokerPersSteen(rij, kolom, steen);
	}

	public void vervangJokerWerkveld(int rij, int kolom, int steen) {
		spel.vervangJokerWerkveld(rij, kolom, steen);
	}

	public void splitsTussen2Stenen(int rij, int steen) {
		spel.splitsTussen2Stenen(rij, steen);
	}

	// voegtoe: bij het aanmelden v/d speler deze speler toevoegen aan de lijst
	// spelers
	public void voegToe(Speler speler) {
		spelers.add(speler);
	}

	// geefOverzicht: geeft gebruikersnamen weer van de aangemelde spelers
	public String geefOverzicht() {
		// welke taal?
		String overzicht = "";

		for (int i = 0; i < spelers.size(); i++) { // lijst overlopen
			overzicht += String.format("%s%n", spelers.get(i).getGebruikersnaam());
		}

		return overzicht;
	}

	// validatie gebruiker
	public void meldAan(String gebruikersnaam, String wachtwoord) {
		// zoeken of er een speler is met die gegevens en die instellen
		Speler speler = spelerRepository.controleerWachtwoordEnGeefSpeler(gebruikersnaam, wachtwoord);

		// als er geen speler gevonden is met deze gegevens -> foute gebrnaam / ww
		// exceptie goeien voor verkeerde gebruikersnaam / wachtwoord
		if (speler == null) {
			throw new VerkeerdeGegevensException();
		}
		// wel een gevonden --> toevoegen in repos
		// maar enkel als die er nog niet in zit
		else if (geefOverzicht().indexOf(gebruikersnaam) != -1) { // zit er al in
			throw new AlAangemeldException();
		}
		else {
			voegToe(speler);
		}
	}

	// keuzemogelijkheden tonen
	public String toonKeuzemogelijkheden() {
		return String.format("kiesUitSpeelEnOverzicht");
	}

	public Speler controleerWachtwoordEnGeefSpeler(String gebruikersnaam, String wachtwoord) {
		return spelerRepository.controleerWachtwoordEnGeefSpeler(gebruikersnaam, wachtwoord);
	}

	// een nieuw spel starten
	public void startSpel() {
		spel = new Spel(spelers);
	}

//	public String toonGebruikersnaamSpelerAanBeurt() {
//		return spel.toonGebruikersnaamSpelerAanBeurt();
//	}

	public List<String> getSpelerNamen() {
		List<String> spelerNamen = new ArrayList<>();

		List<Speler> spelers = new ArrayList<>();
		spelers = spel.getSpelers();

		for (Speler speler : spelers) {
			spelerNamen.add(speler.getGebruikersnaam());
		}

		return spelerNamen;
	}

	public List<Integer> geefScores() {
		return spel.geefScores();
	}

	public boolean stenenZijnOp() {
		// als de stenen op zijn
		return spel.stenenZijnOp();
	}

	public boolean iemandIsGewonnen() {
		return spel.iemandIsGewonnen();
	}

	public Speler getSpelerAanBeurt() {
		return spel.getSpelerAanBeurt();
	}

	public void setSpelerAanBeurt(int i) {
		spel.setSpelerAanBeurt(i);
	}

	public String getStenenGemVeldAlsString() {
		return spel.getStenenGemVeldAlsString();
	}

	public String getEigenStenenAlsString() {
		return getSpelerAanBeurt().getEigenStenenAlsString();
	}

	public String getStenenWerkveldAlsString() {
		return getSpelerAanBeurt().getStenenWerkveldAlsString();
	}

	public Steen[][] getStenenGemVeldVoorBeurt() {
		return spel.getStenenGemVeldVoorBeurt();
	}

	public void saveGemVeldVoorBeurt() {
		spel.saveGemVeldVoorBeurt();
	}

	public boolean vergelijkGemVeldVoorEnNaBeurt() {
		return spel.vergelijkGemVeldVoorEnNaBeurt();
	}

	public void resetGemeenschappelijkVeld() {
		spel.resetGemeenschappelijkVeld();
	}

	public int bepaalWaardeGelegdeStenen() {
		int waarde = 0;
		// voor elke steen in gelegde stenen
		for (Steen steen : spel.getSpelerAanBeurt().getGelegdeStenen()) {
			waarde += steen.getGetal();
		}

		return waarde;
	}

	// gelegde stenen opvragen & zien of er joker in zit
	public boolean jokerInGelegdeStenen() {
		for (Steen steen : getSpelerAanBeurt().getGelegdeStenen()) {
			if (steen.isJoker()) {
				return true;
			}
		}
		return false;
	}

	public boolean controleerRijSerieGemeenschappelijkVeld() {
		return spel.controleerRijSerieGemeenschappelijkVeld();
	}

	public boolean werkVeldIsLeeg() {
		// indien werkveld leeg is--> length is 0
		if (getSpelerAanBeurt().getStenenWerkveld().isEmpty()) {
			return true;
		}
		return false;
	}

	public void geefSteenUitPot() {
		spel.geefSteenUitPot();
	}

	public void plaatsPerssSteenGemVeld(int rij, int kolom, int steen) {
		spel.plaatsPersSteenGemVeld(rij, kolom, steen);
	}

	public void plaatsSteenWerkveldOpGemVeld(int rij, int kolom, int steen) {
		spel.plaatsSteenWerkveldOpGemVeld(rij, kolom, steen);
	}

	public void verplaatsSteenWerkVeld(int rij, int kolom) {
		spel.verplaatsSteenNaarWerkVeld(rij, kolom);
	}

	// getters en setters
	public List<Speler> getSpelers() {
		return spelers;
	}

	public int getAantalSpelers() {
		return aantalSpelers;
	}

	public void setAantalSpelers(int aantalSpelers) {
		this.aantalSpelers = aantalSpelers;
	}


}
