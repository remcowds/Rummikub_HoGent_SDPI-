package domein;

public class Steen {
	private String kleur;
	private int getal;
	private boolean isJoker;

	// constr
	public Steen(String kleur, int getal, boolean isJoker) {
		this.kleur = kleur;
		this.getal = getal;
		this.isJoker = isJoker;
	}

	public int getGetal() {
		return getal;
	}

	public String getKleur() {
		return kleur;
	}

	public boolean isJoker() {
		return isJoker;
	}

	@Override
	public String toString() {
		if (isJoker) {
			return String.format("%s","Joker");
		}
		else {
			
			return String.format("[%d,%s]", getal, kleur);
		}
	}

}
