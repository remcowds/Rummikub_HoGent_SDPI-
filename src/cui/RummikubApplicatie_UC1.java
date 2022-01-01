package cui;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import domein.DomeinController;
import exceptions.AlAangemeldException;
import exceptions.VerkeerdeGegevensException;

public class RummikubApplicatie_UC1 {
	// scanner
	Scanner sc = new Scanner(System.in);

	// domeincontroller
	DomeinController dc = new DomeinController();

	// taal
	Locale locale_en = new Locale("en");
	Locale locale_fr = new Locale("fr");
	ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.bundle"); // taal standaard nederlands
	// --------------

	/**
	 * De taal wordt gevraagd en neemt de juiste resource bundle 
	 * Het aantal spelers word gevraagd 
	 * voor het aantal spelers moet je een gebruiker aanmelden 
	 */
	public RummikubApplicatie_UC1() {

		// taal kiezen & instellen --------------------------------------------
//		dc.startAanmelden();
		int invoer = 0;
		boolean herhalen = true;

		do { // zolang invoer niet [1,3]
			do { // zolang er inputmismatches zijn
				try {
					System.out.printf(
							"Kies uw taal: %nGeef \"1\" in voor Nederlands%nGeef \"2\" in voor Engels%nGeef \"3\" in voor Frans%nUw keuze: ");
					invoer = sc.nextInt();
					herhalen = false;
				} catch (InputMismatchException ex) {
					System.out.println(resourceBundle.getString("foutGetalIngeven"));
					sc.nextLine();
				}
			} while (herhalen);

			if (invoer < 1 || invoer > 3)
				System.out.println(resourceBundle.getString("interval13"));

		} while (invoer < 1 || invoer > 3);

		// taal effectief instellen

		switch (invoer) {
			case 2:
				resourceBundle = ResourceBundle.getBundle("resources.bundle", locale_en); // taal instellen op engels
				break;
			case 3:
				resourceBundle = ResourceBundle.getBundle("resources.bundle", locale_fr); // frans
				break;
		}

		//switch is mss beter
//		if (invoer == 2) {
//			resourceBundle = ResourceBundle.getBundle("resources.bundle", locale_en); // taal instellen op engels
//		}
//		if (invoer == 3) {
//			resourceBundle = ResourceBundle.getBundle("resources.bundle", locale_fr); // frans
//		}
		// -------------------------------------------------------------------------------------------

//		dc.geefAantalSpelersIn(); //-----------------------------------------------------
		do { // zolang aantal spelers niet [2,4]
			boolean herhalen2 = true; // herhalen zolang er inputmismatches zijn
			do { // zolang er inputmismatches zijn
				try { // er kan een inputmismatch gebeuren
					System.out.print(resourceBundle.getString("hoeveelAanmelden"));
					dc.setAantalSpelers(sc.nextInt());
					herhalen2 = false;
				} catch (InputMismatchException ime) {
					System.out.println(resourceBundle.getString("foutGetalIngeven"));
					sc.nextLine();
				}
			} while (herhalen2);

			// exceptie goeien als het getal niet in het interval ligt
			if (dc.getAantalSpelers() > 4 || dc.getAantalSpelers() < 2)
				System.out.println(resourceBundle.getString("tussen2en4"));

		} while (dc.getAantalSpelers() > 4 || dc.getAantalSpelers() < 2);
		// --------------------------------------------------------------------

//		dc.geefGegevensIn(); //--------------------------------------------------
		// voor elke speler
		for (int i = 1; i <= dc.getAantalSpelers(); i++) {
			System.out.printf("%s %d: %n", resourceBundle.getString("speler"), i);
			System.out.printf(resourceBundle.getString("vraagGebrnaam"));
			String gebruikersnaam = sc.next();

			System.out.printf(resourceBundle.getString("vraagWW"));
			String wachtwoord = sc.next();

			try { // proberen aan te melden
				dc.meldAan(gebruikersnaam, wachtwoord);
			} catch (VerkeerdeGegevensException ex) {
				System.out.println(resourceBundle.getString("verkeerdeGeg"));
				i--;
			} catch (AlAangemeldException ex) {
				System.out.println(resourceBundle.getString("alAangemeld"));
				i--;
			}
			// als gebruiker probeert aan te melden en faalt mag de teller (i) niet
			// hoger, cuz opnieuw proberen --> i--

		}
		// ----------------------------------------------------------------------------------------

		System.out.println(resourceBundle.getString("overzichtgeb"));
		System.out.println(dc.geefOverzicht());

		System.out.println(resourceBundle.getString(dc.toonKeuzemogelijkheden()));

	}

}
