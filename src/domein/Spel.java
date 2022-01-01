package domein;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import exceptions.GeenStartOfEindSteenException;
import exceptions.alSteenHierException;
import exceptions.geenCorrecteSerieRijException;
import exceptions.geenSteenHierException;
import exceptions.steenIsgeenJoker;
import exceptions.steenValtBuitenBereikException;
import javafx.scene.paint.Color;

public class Spel {
	private List<Speler> spelers;
	private Speler spelerAanBeurt;
	private List<Steen> stenenPot;
	private Steen stenenGemVeld[][]; // 2D lijst: alle series & rijen die werden gelegd
	private Steen[][] stenenGemVeldVoorBeurt;

	public Spel(List<Speler> spelers) {
		// in random volgorde zetten
		setSpelers(spelers);

		// ipv repos te maken & aan te roepen, hier alles doen
		stenenPot = new ArrayList<>();
		setStenenPot();

		// aan iedereen stenen geven
		deelStenenUit();

		// gemveld maken
		stenenGemVeld = new Steen[6][16];
	}
/**
 * hier kan je een rij splitsen, je moet de positie ingeven na welke steen je wil splitsen 
 * de rij na de positie van de steen wordt dan opgeschoven, 
 * als deze steen zogezegd uit dde rij valt word deze steen naar het gemeenschappelijk werkveld verplaatst
 * 
 */
	public void splitsTussen2Stenen(int rij, int steen) { // steen is kolom
		// exceptie als rij of steen te groot/klein is
		if (rij < 0 || rij >= stenenGemVeld.length || steen < 0 || steen >= stenenGemVeld[0].length) {
			throw new steenValtBuitenBereikException();
		}

		// meegekregen rij & steen is 1based dus
		int indexRij = rij - 1;

		if (stenenGemVeld[indexRij][15] != null) {
			verplaatsSteenNaarWerkVeld(indexRij, 15);
		}

		// lijst vd rij maken
		List<Steen> teSplitsenRij = new ArrayList<>();

		// lijst opvullen
		for (int i = 0; i < stenenGemVeld[0].length; i++) {
			teSplitsenRij.add(i, stenenGemVeld[indexRij][i]);
		}

		// bv 0 1 2 3 4 :
		// de gebruiker wilt splitsen tussen 1 en 2, dus hij voert 2 in (1based)
		// .add(2, null) (0based) --> 0 1 null 2 3 4
		teSplitsenRij.add(steen, null);

		// rij is in orde --> weer converten naar array
		Steen gesplitsteRij[] = new Steen[teSplitsenRij.size()];
		teSplitsenRij.toArray(gesplitsteRij);

		// de rij in het gemveld vervangen door deze rij
		for (int i = 0; i < stenenGemVeld[0].length; i++) {
			stenenGemVeld[indexRij][i] = gesplitsteRij[i];
		}

		// TODO mss melding geven dat nr werkveld is gegaan
	}
	/**
	 * hier verplaats je een steen van je persoonlijke stenen op het gemmeenschappelijk veld
	 */
	public void plaatsPersSteenGemVeld(int rij, int kolom, int steen) {
		// exception zien of int steen nog int bereik ligt ligt, dus asge 2 stenen hebt
		// dage ni kunt zeggen 3 & mag ook ni negatief zijn ofc
		// of indien rij / kolom buiten bereik valt
		if (steen >= spelerAanBeurt.getEigenStenen().size() || steen < 0 || rij < 0 || kolom < 0
				|| rij >= stenenGemVeld.length || kolom >= stenenGemVeld[0].length) {
			throw new steenValtBuitenBereikException();
		}

		Steen SteenDieGeplaatstMoetWorden = spelerAanBeurt.getEigenStenen().get(steen);
		correcteSerieRij1(rij, kolom, SteenDieGeplaatstMoetWorden);
	}
/**
 * hier verplaats je een steen van het werkveld op het gemmeenschappelijk veld
 */
	public void plaatsSteenWerkveldOpGemVeld(int rij, int kolom, int steen) {
		// exception zien of int steen nog int bereik ligt ligt, dus asge 2 stenen hebt
		// dage ni kunt zeggen 3 & mag ook ni negatief zijn ofc
		// of indien rij / kolom buiten bereik valt
		if (steen >= spelerAanBeurt.getStenenWerkveld().size() || steen < 0 || rij < 0 || kolom < 0
				|| rij >= stenenGemVeld.length || kolom >= stenenGemVeld[0].length) {
			throw new steenValtBuitenBereikException();
		}

		Steen SteenDieGeplaatstMoetWorden = spelerAanBeurt.getStenenWerkveld().get(steen);
		correcteSerieRij1(rij, kolom, SteenDieGeplaatstMoetWorden);
	}
/**
 * hier vervang je een joker op het gemmeenschappelijk veld met een persoonlijke steen
 */
	public void vervangJokerPersSteen(int rij, int kolom, int steen) {
		// als ge een leeg vak selecteerd
		if (stenenGemVeld[rij][kolom] == null) {
			throw new geenSteenHierException();
		}

		// als ge geen joker selecteerd
		if (!stenenGemVeld[rij][kolom].isJoker()) {
			throw new steenIsgeenJoker();
		}

		// wel een joker:

		// joker uit gemveld & naar uw werkveld
		getSpelerAanBeurt().addSteenWerkveld(stenenGemVeld[rij][kolom]);
		stenenGemVeld[rij][kolom] = null;

		// steen plaatsen
		plaatsPersSteenGemVeld(rij, kolom, steen);
	}

	/**
	 * hier vervang je een joker op het werk veld met een persoonlijke steen
	 */
	public void vervangJokerWerkveld(int rij, int kolom, int steen) {
		// als ge een leeg vak selecteerd
		if (stenenGemVeld[rij][kolom] == null) {
			throw new geenSteenHierException();
		}

		// als ge geen joker selecteerd
		if (!stenenGemVeld[rij][kolom].isJoker()) {
			throw new steenIsgeenJoker();
		}

		// wel een joker:

		// joker uit gemveld & naar uw werkveld
		getSpelerAanBeurt().addSteenWerkveld(stenenGemVeld[rij][kolom]);
		stenenGemVeld[rij][kolom] = null;

		// steen plaatsen
		plaatsSteenWerkveldOpGemVeld(rij, kolom, steen);
	}

	/**
	 * hier wordt er gekeken of de serie correct is als de kolom 0 of 15 is -> anders foutboodschap
	 */
	public void correcteSerieRij1(int rij, int kolom, Steen SteenDieGeplaatstMoetWorden) {
		Steen steenDieGeplaatstMoetWorden = SteenDieGeplaatstMoetWorden;

		// exception er ligt al een steen op die plaats
		// indien een joker vervangen: skippen

		if (stenenGemVeld[rij][kolom] != null) {
//			if (!stenenGemVeld[rij][kolom].isJoker()) { //anders kunde steen leggen op joker
			throw new alSteenHierException();
//			}

		}

		// als er nog geen steen op ligt
		if (stenenGemVeld[rij][kolom] == null) {
			// een joker mag geplaatst worden zonder controle te doen
			if (steenDieGeplaatstMoetWorden.isJoker()) {
				stenenGemVeld[rij][kolom] = steenDieGeplaatstMoetWorden;
				return;
			}

			// controleren of serie/rij gerespecteerd wordt
			// -->ofwel zelfde kleur & oplopend getal
			// -->ofwel andere kleur & zelfde getal

			// als kolom 0 is dan enkel check op kolom + 1
			if (kolom == 0) {
				// serierij beginnen
				if (stenenGemVeld[rij][kolom + 1] == null) {
					stenenGemVeld[rij][kolom] = steenDieGeplaatstMoetWorden;
					return;
				}
				// serierij verderbouwen
				if ((steenDieGeplaatstMoetWorden.getKleur() == stenenGemVeld[rij][kolom + 1].getKleur()
						&& steenDieGeplaatstMoetWorden.getGetal() + 1 == stenenGemVeld[rij][kolom + 1].getGetal())
						|| (steenDieGeplaatstMoetWorden.getKleur() != stenenGemVeld[rij][kolom + 1].getKleur()
								&& steenDieGeplaatstMoetWorden.getGetal() == stenenGemVeld[rij][kolom + 1]
										.getGetal())) {
					// GOED
					stenenGemVeld[rij][kolom] = steenDieGeplaatstMoetWorden;
				}
				else {
					throw new geenCorrecteSerieRijException();
				}
				return;
			}

			// als kolom 15 is (laatste kolom = stenengemveld[0]-1) dan enkel kolom - 1
			if (kolom == stenenGemVeld[0].length - 1) {
				// serierij beginnen
				if (stenenGemVeld[rij][kolom - 1] == null) {
					stenenGemVeld[rij][kolom] = steenDieGeplaatstMoetWorden;
					return;
				}
				// serierij verderbouwen
				if ((stenenGemVeld[rij][kolom - 1].getKleur() == steenDieGeplaatstMoetWorden.getKleur()
						&& stenenGemVeld[rij][kolom - 1].getGetal() + 1 == steenDieGeplaatstMoetWorden.getGetal())
						|| (stenenGemVeld[rij][kolom - 1].getKleur() != steenDieGeplaatstMoetWorden.getKleur()
								&& stenenGemVeld[rij][kolom - 1].getGetal() == steenDieGeplaatstMoetWorden
										.getGetal())) {
					// GOED
					stenenGemVeld[rij][kolom] = steenDieGeplaatstMoetWorden;
				}
				else {
					throw new geenCorrecteSerieRijException();
				}
				return;
			}

			// kolom is tussen 0 en 15
			if (correcteSerieRij2(rij, kolom, steenDieGeplaatstMoetWorden)) {
				stenenGemVeld[rij][kolom] = steenDieGeplaatstMoetWorden;
			}
		}
	}
	/**
	 * hier wordt er gekeken of de serie correct is tussen kolom 0-15-> anders foutboodschap
	 * 
	 */
	public boolean correcteSerieRij2(int rij, int kolom, Steen steenDieGeplaatstMoetWorden) {
		// alst om te starten is is kolom +1 en kolom -1 null dus dan gwn doen
		if (stenenGemVeld[rij][kolom - 1] == null && stenenGemVeld[rij][kolom + 1] == null) {
			return true;
		}

		// als de kolom ervoor null is moet die niet gecheckt worden
		if (stenenGemVeld[rij][kolom - 1] == null) {
			// zelfde kleur & stijgend getal tegenover kolom erna?
			if (steenDieGeplaatstMoetWorden.getKleur() == stenenGemVeld[rij][kolom + 1].getKleur()
					&& steenDieGeplaatstMoetWorden.getGetal() + 1 == stenenGemVeld[rij][kolom + 1].getGetal()) {
				return true;
			}

			// zelfde getal & andere kleur tegenover kolom erna?
			if (steenDieGeplaatstMoetWorden.getKleur() != stenenGemVeld[rij][kolom + 1].getKleur()
					&& steenDieGeplaatstMoetWorden.getGetal() == stenenGemVeld[rij][kolom + 1].getGetal()) {
				return true;
			}
		}

		// als de kolom erna null is moet die niet gecheckt worden
		if (stenenGemVeld[rij][kolom + 1] == null) {
			// zelfde kleur & stijgend getal tegenover kolom ervoor?
			if (stenenGemVeld[rij][kolom - 1].getKleur() == steenDieGeplaatstMoetWorden.getKleur()
					&& stenenGemVeld[rij][kolom - 1].getGetal() + 1 == steenDieGeplaatstMoetWorden.getGetal()) {
				return true;
			}

			// zelfde getal & andere kleur tegenover kolom ervoor?
			if (stenenGemVeld[rij][kolom - 1].getKleur() != steenDieGeplaatstMoetWorden.getKleur()
					&& stenenGemVeld[rij][kolom - 1].getGetal() == steenDieGeplaatstMoetWorden.getGetal()) {
				return true;
			}
		}

		// ne keer hier, betekent dat de steen er niet thuis hoort
		throw new geenCorrecteSerieRijException();
	}

	// mag deze steen weg uit het gemveld? DR_STEEN_NAAR_WERKVELD
	/**
	 * hier kan je een steen verplaatsen naar het werkveld
	 */
	public void verplaatsSteenNaarWerkVeld(int rij, int kolom) {
		if (stenenGemVeld[rij][kolom] == null) { // lege plaats aangeduid
			throw new geenSteenHierException();
		}

		// als hij een net geplaatste steen naar het werkveld wil doen, dan geeft het
		// error (zie isDeelVanSerieGemVeld) --> fix 3 ifs

		// ----------------losseSteen----------------------
		// dus als het geen deel is van serie / rij (losse steen)--> gewoon doen

		// als kolom 0 is dan enkel check op kolom + 1
		if (kolom == 0 && stenenGemVeld[rij][kolom + 1] == null) {
			// op werkveld zetten
			spelerAanBeurt.getStenenWerkveld().add(stenenGemVeld[rij][kolom]);

			// uit gemveld doen
			stenenGemVeld[rij][kolom] = null;

			return;
		}

		// als kolom 15 is (laatste kolom = stenengemveld[0]-1) dan enkel kolom - 1
		if (kolom == stenenGemVeld[0].length - 1 && stenenGemVeld[rij][kolom - 1] == null) {

			// op werkveld zetten
			spelerAanBeurt.getStenenWerkveld().add(stenenGemVeld[rij][kolom]);

			// uit gemveld doen
			stenenGemVeld[rij][kolom] = null;

			return;
		}

		if (stenenGemVeld[rij][kolom + 1] == null && stenenGemVeld[rij][kolom - 1] == null) {
			// op werkveld zetten
			spelerAanBeurt.getStenenWerkveld().add(stenenGemVeld[rij][kolom]);

			// uit gemveld doen
			stenenGemVeld[rij][kolom] = null;
			return;
		}

		// ----------------/losseSteen----------------------

		// rij: elke steen mag naar werkveld------------
		if (!isDeelVanSerieGemVeld(rij, kolom)) {
			if (stenenGemVeld[rij][kolom] != null) {
				// op werkveld zetten
				spelerAanBeurt.getStenenWerkveld().add(stenenGemVeld[rij][kolom]);

				// uit gemveld doen
				stenenGemVeld[rij][kolom] = null;

				// als de plaats ervoor of erna leeg is --> gewoon laten

				// als de plaats ervoor en erna gevuld is --> stenen rechts ervan opschuiven
				// maar enkel erna alst kolom 0 is
				if (kolom == 0) {
					if (stenenGemVeld[rij][kolom + 1] != null) {
						// stenen rechts ervan opschuiven
						int i = 0;
						do {
							stenenGemVeld[rij][kolom + i] = stenenGemVeld[rij][kolom + i + 1];
							stenenGemVeld[rij][kolom + i + 1] = null;
							i++;
						} while (kolom + i + 1 < stenenGemVeld[rij].length);
					}
					return;
				}

				// alst kolom 15 is moet er niks opgeschoven worden
				if (kolom == stenenGemVeld[0].length - 1) {
					return;
				}

				if (stenenGemVeld[rij][kolom - 1] != null && stenenGemVeld[rij][kolom + 1] != null) {
					// stenen rechts ervan opschuiven
					int i = 0;
					do {
						stenenGemVeld[rij][kolom + i] = stenenGemVeld[rij][kolom + i + 1];
						stenenGemVeld[rij][kolom + i + 1] = null;
						i++;
					} while (kolom + i + 1 < stenenGemVeld[rij].length);
				}
			}

			// tis een rij dus dan moetge nimr zien oft een serie is
			return;
		}
		// ----------------/rij----------------------

		// serie: enkel start- of eindsteen mag naar werkveld
		if (isDeelVanSerieGemVeld(rij, kolom)) {
			// als het begint op positie 0, komt de kolom op -1 (same opt einde) -> exceptie
			// als de kolom 0 of 15 is ist sws begin of eind dus gwn doen
			if (kolom == 0 || kolom == stenenGemVeld[0].length - 1) {
				// op werkveld zetten
				spelerAanBeurt.getStenenWerkveld().add(stenenGemVeld[rij][kolom]);

				// uit gemveld doen
				stenenGemVeld[rij][kolom] = null;
			}

			// als het dan ergens anders ligt:
			// enkel als de steen ervoorn null is (begin)
			// of enkel als de steen erna null is (einde)
			else if (stenenGemVeld[rij][kolom - 1] == null || stenenGemVeld[rij][kolom + 1] == null) {
				// op werkveld zetten
				spelerAanBeurt.getStenenWerkveld().add(stenenGemVeld[rij][kolom]);

				// uit gemveld doen
				stenenGemVeld[rij][kolom] = null;
			}
			// anders is het een steen int midden v/e serierij --> mag niet: exception
			else {
				throw new GeenStartOfEindSteenException();
			}
		}
		return;
		// ----------------/serie----------------------
	}
/**
 * hier wordt er gekeken of het deel uit maakt van de serie(kijkt naar de kleur ervoor of erna)
 */
	public boolean isDeelVanSerieGemVeld(int rij, int kolom) {
		// als de kleur hetzelfde is als die v/d steen ervoor / erna dan is het
		// een serie

		// werkt niet voor jokers: dus als een vd 2 joker is return true;
		if (stenenGemVeld[rij][kolom].isJoker() || stenenGemVeld[rij][kolom + 1].isJoker()) {
			return true;
		}

		// als kolom 0 of 15 is dan +1 of -1 is out of bounds
		// -> dan enkel zien naar t volgende/vorige
		if (kolom == 0) {
			if (stenenGemVeld[rij][kolom].getKleur() == stenenGemVeld[rij][kolom + 1].getKleur()) {
				return true;
			}
			else {
				return false;
			}
		}

		if (kolom == stenenGemVeld[rij].length - 1) {
			if (stenenGemVeld[rij][kolom].getKleur() == stenenGemVeld[rij][kolom - 1].getKleur()) {
				return true;
			}
		}

		// als het eind v/e serierij is moet ge naar de vorige zien
		if (stenenGemVeld[rij][kolom + 1] == null) {
			if (stenenGemVeld[rij][kolom].getKleur() == stenenGemVeld[rij][kolom - 1].getKleur()) {
				return true;
			}
		}

		// als het begin v/e serierij is of int midden moet ge naar de volgende zien
		if (stenenGemVeld[rij][kolom - 1] == null
				|| (stenenGemVeld[rij][kolom - 1] != null && stenenGemVeld[rij][kolom + 1] != null)) {
			if (stenenGemVeld[rij][kolom].getKleur() == stenenGemVeld[rij][kolom + 1].getKleur()) {
				return true;
			}
		}

		// anders is het een rij
		return false;
	}

	public void saveGemVeldVoorBeurt() {
		Steen steenSpel[][] = getStenenGemVeld();
		stenenGemVeldVoorBeurt = new Steen[steenSpel.length][steenSpel[0].length];

		for (int i = 0; i < steenSpel.length; i++) {
			for (int j = 0; j < steenSpel[i].length; j++) {
				stenenGemVeldVoorBeurt[i][j] = steenSpel[i][j];
			}
		}

//		stenenGemVeldVoorBeurt = spel.getStenenGemVeld();
	}

	public boolean vergelijkGemVeldVoorEnNaBeurt() {
		// true: niks veranderd
		return Arrays.deepEquals(getStenenGemVeldVoorBeurt(), getStenenGemVeld());

		// eerst dit, maar dit gaat niet, Arrays.equals gaat ook niet voor 2D arrays -->
		// Arrays.deepEquals
//		return (getStenenGemVeldVoorBeurt().equals(getStenenGemVeld()));
	}

	public void resetGemeenschappelijkVeld() {
		for (int i = 0; i < stenenGemVeld.length; i++) {
			for (int j = 0; j < stenenGemVeld[i].length; j++) {
				stenenGemVeld[i][j] = stenenGemVeldVoorBeurt[i][j];
			}
		}
	}

	// false indien spelsit niet juist is
	/**
	 * hier wordt er gekeken of de spelsituatie klopt
	 */
	public boolean controleerRijSerieGemeenschappelijkVeld() {
		// voor elke lijn van het gemveld
		for (int i = 0; i < stenenGemVeld.length; i++) {
			int start = 0; // vanaf hier kijken om de volgende serie/rij te vinden op deze lijn
			boolean erIsEenBegin;
			boolean erIsEenEind;
			boolean juisteSerieRij = false;
			int beginSerieRij;
			int eindSerieRij;

			// voor elke serie/rij op de lijn
			do {
				erIsEenBegin = false;
				erIsEenEind = false;
				beginSerieRij = 0;
				eindSerieRij = 0;

				// ------------beginbepalen-------------
				for (int j = start; j < stenenGemVeld[i].length; j++) {
					if (stenenGemVeld[i][j] != null) {
						// hier staat een steen --> dit is het begin van een serie/rij
						beginSerieRij = j;
						erIsEenBegin = true;
					}
					if (erIsEenBegin) {
						break; // eruit gaan als er een gevonden is
					}
				}
				// ------------/beginbepalen-------------

				// al dit enkel als er een begin is
				if (erIsEenBegin) {
					// ------------eindbepalen-------------
					for (int j = beginSerieRij; j < stenenGemVeld[0].length; j++) {
						if (stenenGemVeld[i][j] == null) {
							// i j is geen steen meer dus die daarvoor (j-1) is de laatste
							eindSerieRij = j - 1;
							erIsEenEind = true;
						}

						// alst opt einde is (dus j==15)
						// kunde de volgende ni checken oft null is
						// das dus sws het eind, dus als j == 15 dan is dat het eind v/d rij
						if (j == 15) {
							eindSerieRij = j;
							erIsEenEind = true;
						}

						if (erIsEenEind) {
							break;
						}
					}

					// ------------/eindbepalen-------------

					// de serie/rij opslaan in een array
					Steen serierij[] = new Steen[eindSerieRij - beginSerieRij + 1];
					for (int j = 0; j < serierij.length; j++) {
						serierij[j] = stenenGemVeld[i][beginSerieRij + j];
					}

					// een serie of rij moet 3 lang zijn, dus indien het niet lang genoeg is klopt
					// de spelsituatie al niet
					if (serierij.length < 3) {
						return false;
					}

					// alst 2 jokers en ne steen zijn kunde ni weten oft serie/rij is
					// --> 2jokers + steen is sws goed
					if (serierij.length == 3) {
						if (serierij[0].isJoker() && serierij[1].isJoker()
								|| serierij[0].isJoker() && serierij[2].isJoker()
								|| serierij[1].isJoker() && serierij[2].isJoker()) {
							break; // naar volgende serie/rij gaan
						}
					}

					// dus nu is de lengte 4 met mogelijks 2 jokers
					// of de lengte is 3 met max 1 joker

					// 2 elementen uit de serie / rij halen om te vergelijken:
					// maar: mag enkel alst geen joker is

					// ------------indexes bepalen-------------
					// dus 0 joker, 1 niet --> gwn index 1 en 2 pakken
					// dus 0 niet, 1 joker --> gwn index 0 en 2 pakken
					// dus 0 joker, 1 joker --> lengte is sws 4 of meer dus index 2 en 3 pakken
					// dus 0 niet, 1 niet --> gwn index 0 en 1 pakken

					int index1 = 0, index2 = 1;

					if (serierij[index1].isJoker() && !serierij[index2].isJoker()) {
						index1 = 2;
					}

					if (!serierij[index1].isJoker() && serierij[index2].isJoker()) {
						index2 = 2;
					}

					if (serierij[index1].isJoker() && serierij[index2].isJoker()) {
						index1 = 2;
						index2 = 3;
					}

					if (!serierij[index1].isJoker() && !serierij[index2].isJoker()) {
						// goed
					}
					// ------------/indexesbepalen-------------

					// ------------serie-------------
					// (kleuren v/d eerste 2 zijn gelijk --> serie)
					// (dit mogen dus geen jokers zijn om te bepalen of het een serie/ rij is

					if (serierij[index1].getKleur() == serierij[index2].getKleur()) {
						// grootte controleren (serie: min 3 max 13 --> gaat van 1 tot 13)
						if (serierij.length < 3 || serierij.length > 13)
							return false;

						// alles moet dezelfde kleur zijn
						String kleurVanEenSteen = serierij[index1].getKleur();

						for (Steen steen : serierij) {
							if (!steen.isJoker()) {// niet controleren als het een joker is
								if (kleurVanEenSteen != steen.getKleur()) {
									return false; // ergens niet de juiste kleur
								}
							}
						}

						// cijfers moeten opvolgend zijn
						for (int k = 0; k < serierij.length - 1; k++) {
							// alst begint me ne joker, continue: deze iteratie skippen en volgend dingen
							// bezien
							if (serierij[0].isJoker()) {
								continue;
							}

							// laatste iteratie: alst eindigt me ne joker, break: moet nimr naar gezien
							// worden
							if (k == serierij.length - 2) {
								if (serierij[serierij.length - 1].isJoker()) {
									break;
								}
							}

							// als joker int midden ergens zit:
							// als de volgende ne joker is, zien naar diene nog een plaats verder of da
							// getal 2 meer is & nog nen l++ doen dat em de joker skipt

							if (serierij[k + 1].isJoker()) {
								if (serierij[k].getGetal() + 2 != serierij[k + 2].getGetal()) {
									return false;
								}
								k++;
							}

							// tvolgende is gene joker
							else {
								// getal van de ene steen +1 moet gelijk zijn aan die van de volgende steen
								// --> opeenvolgende stenen
								if (serierij[k].getGetal() + 1 != serierij[k + 1].getGetal()) {
									return false; // ni opvolgend
								}
							}

						}
						// deze serie is juist
						juisteSerieRij = true;
					}
					// ------------/serie-------------

					// ------------rij-------------
					// (getallen v/d eerste 2 zijn gelijk --> rij)
					if (serierij[index1].getGetal() == serierij[index2].getGetal()) {
						// 3 of 4 stenen lang
						if (serierij.length < 3 || serierij.length > 4)
							return false;

						// allemaal zelfde cijfers
						for (int l = 0; l < serierij.length - 1; l++) {
							// alst begint me ne joker, continue: deze iteratie skippen en volgend dingen
							// bezien
							if (serierij[0].isJoker()) {
								continue;
							}

							// laatste iteratie: alst eindigt me ne joker, break: moet nimr naar gezien
							// worden
							if (l == serierij.length - 2) {
								if (serierij[serierij.length - 1].isJoker()) {
									break;
								}
							}

							// als joker int midden ergens zit:
							// als de volgende ne joker is, zien naar diene nog een plaats verder of da
							// getal tzelfde is & nog nen l++ doen dat em de joker skipt

							if (serierij[l + 1].isJoker()) {
								if (serierij[l].getGetal() != serierij[l + 2].getGetal()) {
									return false;
								}
								l++;
							}

							// tvolgende is gene joker
							else {
								if (serierij[l].getGetal() != serierij[l + 1].getGetal()) {
									return false; // ni opvolgend
								}
							}

						}

						// allemaal verschillende kleur
						// een nieuwe lijst maken om unieke kleuren in te zetten
						List<String> uniekekleurenlijst = new ArrayList<>();

						// indien joker: kleur v/d joker is "" dus die wordt ook toegevoegd
						// hier moet dus niets aangepast worden om te laten werken met jokers
						// tenzij het 2 jokers zijn

						for (Steen steen : serierij) {
							if (!uniekekleurenlijst.contains(steen.getKleur())) { // skipt de kleuren dat er al inzitten
								uniekekleurenlijst.add(steen.getKleur());
							}
						}

						// moesten er 2 jokers inzitten dan moet de size van uniekekleurenlijst verhoogt
						// worden met 1, aangezien er enkel unieke waarden inzitten
						int aantalJokersInSerierij = 0;
						for (int j = 0; j < serierij.length; j++) {
							if (serierij[j].isJoker()) {
								aantalJokersInSerierij++;
							}
						}

						if (aantalJokersInSerierij == 2) {
							if (uniekekleurenlijst.size() + 1 < serierij.length) {
								return false;
							}
						}
						// 1 of geen jokers
						else {
							// als de nieuwe (unieke kleuren) lijst kleiner is zit er een kleur 2x in
							// serieOfRij
							if (uniekekleurenlijst.size() < serierij.length) {
								return false;
							}
						}

						// deze rij is juist
						juisteSerieRij = true;
					}
					// ------------/rij-------------

					// vanaf et volgende beginnen zien voor next serie/rij op deze lijn
					start = eindSerieRij + 1;

					// geen serie / rij: ook ni goed
					if (!juisteSerieRij) {
						return false;
					}

				}
				// indien geen begin: eriseenbegin blijft false

				/*
				 * een serie/rij is sws 3 lang, en er moet een plaats tussen t eind vd vorige en
				 * t begin v/d volgende zijn --> er moeten nog 4plaatsen zijn DUS breedte bord
				 * (16) - eindserierij >= 4
				 */

				// eriseenbegin --> indien er op een lijn geen series/rijen zijn gaat hij er uit
				// en ist next lijn

			} while (erIsEenBegin && (stenenGemVeld[0].length - eindSerieRij >= 4)); // zolang er nog series/rijen zijn
																						// op deze lijn
		}

		// indien er niks fout liep is de spelsituatie juist
		return true;
	}

	public String getStenenGemVeldAlsString() {
		String uitvoer = String.format("\n%-18s", "0");
		for (int i = 1; i <= stenenGemVeld[0].length; i++) {
			uitvoer += String.format("%-20s", i);
		}
		uitvoer += "\n";
		// voor elke serieOfRij
		for (int i = 0; i < stenenGemVeld.length; i++) {
			uitvoer += String.format("%-18s", i + 1);
//			uitvoer = uitvoer.concat(String.format("%-25d", i + 1));
			for (int j = 0; j < stenenGemVeld[i].length; j++) {
				if (stenenGemVeld[i][j] != null) {
					uitvoer += String.format("%-18s", stenenGemVeld[i][j].toString());
//					uitvoer = uitvoer.concat(String.format("%-25s", stenenGemVeld[i][j].toString()));
				}

				if (stenenGemVeld[i][j] == null) {
					uitvoer += String.format("%-18s", "leeg");
//					uitvoer = uitvoer.concat(String.format("%-25s", "leeg"));
				}
			}
			uitvoer += "\n";
		}
		return uitvoer;
	}

	// gebruikersnaam weergeven
	public String toonGebruikersnaamSpelerAanBeurt() {
		return spelerAanBeurt.getGebruikersnaam();
	}

	// als stenen int midden op zijn en niemand kan nog leggen is het gedaan
	// (niemand wint)
	public boolean stenenZijnOp() {
		// als stenen op zijn
		return stenenPot.isEmpty() ? true : false;
	}

	// als er iemand wint ist gedaan
	public boolean iemandIsGewonnen() {
		// voor elke speler nazien
		for (Speler speler : this.spelers) {
			if (speler.isGewonnen()) {
				return true;
			}
		}
		return false;
	}

	// geeft iedereen 14 random stenen
	public void deelStenenUit() {
		// voor elke speler
		for (int i = 0; i < spelers.size(); i++) {
			// lijst maken
			List<Steen> stenen = new ArrayList<>();

			// lijst opvullen met 14 stenen en diezelfde stenen uit lijst stenen halen
			for (int j = 0; j < 14; j++) {
				stenen.add(this.stenenPot.get(j));
				this.stenenPot.remove(j);
			}
			// de speler zijn stenen effectief instellen
			spelers.get(i).setEigenStenen(stenen);
		}

	}

	public List<Integer> geefScores() {
		List<Integer> scores = new ArrayList<>();

		for (Speler speler : this.spelers) {
			// score bepalen
			speler.bepaalScore();
			// score toevoegen aan lijst
			scores.add(speler.getScore());
		}

		return scores;
	}

	public void geefSteenUitPot() {
		// steen uit pot meegeven
		spelerAanBeurt.addEigenSteen(stenenPot.get(0));
		// steen uit pot doen
		stenenPot.remove(0);
	}

	// getters en setters

	public void setStenenPotNull() {
		this.stenenPot = new ArrayList<>();
	}

	public void setStenenPot() {
		// voor elk getal van 1 tem 13
		for (int i = 1; i <= 13; i++) {
			// voor elke kleur 2x van 1tem13
			for (int k = 0; k < 2; k++) {
				// zwart
				stenenPot.add(new Steen("zwart",i, false));

				// rood
				stenenPot.add(new Steen("rood",i, false));

				// blauw
				stenenPot.add(new Steen( "blauw",i, false));

				// geel
				stenenPot.add(new Steen("geel",i, false));
			}
		}

		// 2 jokers toevoegen
		for (int i = 0; i < 2; i++) {
			stenenPot.add(new Steen("", 25, true));
		}

		// in een random volgorde plaatsen
		Collections.shuffle(stenenPot);
	}

	public Steen[][] getStenenGemVeld() {
		return stenenGemVeld;
	}

	public List<Speler> getSpelers() {
		return spelers;
	}

	public void setSpelers(List<Speler> spelers) {
		// in een random volgorde zetten & dan instellen als attribuut
		Collections.shuffle(spelers);
		this.spelers = spelers;
	}

	public Speler getSpelerAanBeurt() {
		return this.spelerAanBeurt;
	}

	public void setSpelerAanBeurt(int i) {
		this.spelerAanBeurt = spelers.get(i);
	}

	public Steen[][] getStenenGemVeldVoorBeurt() {
		return stenenGemVeldVoorBeurt;
	}
}
