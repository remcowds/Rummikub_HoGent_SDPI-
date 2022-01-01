package cui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import domein.DomeinController;

public class RummikubApplicatie_UC2 {

	/**
	 * Hier kan je een overzicht van scores tonen op het scherm 
	 * of het spel spelen dan is er een random speler aan beurt
	 * 
	 */
	public RummikubApplicatie_UC2(RummikubApplicatie_UC1 uc1) {
		// scanner
		Scanner sc = new Scanner(System.in);

		// domeincontroller
		DomeinController dc = uc1.dc;

		// taal
		ResourceBundle resourceBundle = uc1.resourceBundle;

		// eind UC1: Kies uit speel spel (1) en toon overzicht (2):
		int keuze = 0;
		boolean herhalen = true;
		do {
			try {
				System.out.print(resourceBundle.getString("keuze"));
				keuze = sc.nextInt();

				if (keuze < 1 || keuze > 2) {
					throw new IllegalArgumentException();
				}

				herhalen = false;
			}
			catch (InputMismatchException ime) {
				System.out.println(resourceBundle.getString("keuzeOverzichtOfSpel"));
				sc.nextLine();
			}
			catch (IllegalArgumentException iae) {
				System.out.println(resourceBundle.getString("keuzeOverzichtOfSpel"));
				sc.nextLine();
			}
		} while (herhalen);

		if (keuze == 2) {
			System.out.println(dc.geefScoresOverzicht());
			return;
			// appl mag hier stoppen indien toon overzicht wordt gekozen
		}

		// en als de keuze niet 2 is dan wordt dit uitgevoerd, ipv een "else"
		// (overzichtelijker)

		// het spel starten voor de gebruikers (nieuw spel wordt gemaakt, de volgorde
		// van spelers wordt random bepaalt, steenRepos wordt gemaakt en gevuld met 106
		// stenen,

		dc.startSpel();

		// TODO spelerAanBeurt in spel bijhouden HOE?
		int spelerAanBeurt = 0;
		do {
			// registreren wie aan de beurt is + tonen
			System.out.printf("%s %s%n", dc.getSpelerNamen().get(spelerAanBeurt % dc.getAantalSpelers()),
					resourceBundle.getString("isAanBeurt"));

			// SPEELT BEURT (UC3)
			// eind v/d beurt: checken of hij gewonnen is --> ja? uit loop
			// eind v/d beurt: checken of er nog stenen liggen int midden --> nee? uit loop

			spelerAanBeurt++;
		} while (spelerAanBeurt < 10);// !dc.stenenZijnOp() && !dc.iemandIsGewonnen());
		// => zolang er stenen liggen int midden
		// => zolang er geen speler wint

		System.out.println(resourceBundle.getString("scores"));

		// bijhouden in list
		List<Integer> scores = dc.geefScores();

		// voor elke score in scores: scores per speler tonen
		for (int i = 0; i < scores.size(); i++) {
			// loser: score is negatief, printen
			if (scores.get(i) < 0) {
				System.out.printf("%s %s: %d %s.%n", dc.getSpelerNamen().get(i), resourceBundle.getString("verloren"),
						scores.get(i), resourceBundle.getString("punten"));
			}

			// winnaar: score is 0 => instellen op de som van de scores * -1
			if (scores.get(i) == 0) {
				System.out.printf("%s %s: %d %s.%n", dc.getSpelerNamen().get(i), resourceBundle.getString("gewonnen"),
						-1 * scores.stream().reduce(0, (a, b) -> a + b), resourceBundle.getString("punten"));
			}

//			controle lambda: 
//			List<Integer> getallen = Arrays.asList(1, 2, 3, 4, 5);
//			System.out.println(getallen);
//			System.out.println(getallen.stream().reduce(0, (a,b) -> a+b));
		}

	}

}
