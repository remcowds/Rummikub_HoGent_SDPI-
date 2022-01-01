package domein;

import java.util.List;

import persistentie.SpelerMapper;

public class SpelerRepository {
	private SpelerMapper mapper;

	// constr: lijst aanmaken
	public SpelerRepository() {
//		spelers = new ArrayList<>();
		mapper = new SpelerMapper();
	}
	
	public Speler controleerWachtwoordEnGeefSpeler(String gebruikersnaam, String wachtwoord) {
		return mapper.controleerWachtwoordEnGeefSpeler(gebruikersnaam, wachtwoord);
	}

	public void voegScoresToe(List<Speler> spelers) {
		mapper.voegScoresToe(spelers);
	}	
	
	public String geefScoresOverzicht(List<Speler> spelers) {
		return mapper.geefScoresOverzicht(spelers);
	}
	
	

}
