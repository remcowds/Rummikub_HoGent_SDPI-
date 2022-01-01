package gui;

import java.util.Locale;
import java.util.ResourceBundle;

import domein.DomeinController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StartPaneel extends VBox {
	private final DomeinController dc;
	private final HoofdPaneel hoofdPaneel;
	private Locale locale_en = new Locale("en");
	private Locale locale_fr = new Locale("fr");

	public StartPaneel(DomeinController dc, HoofdPaneel hoofdPaneel) {
		this.dc = dc;
		this.hoofdPaneel = hoofdPaneel;

		kiesTaal();
	}

	// -----------------------------------
	private ImageView rummikubFoto;
	private Label lblKiesTaal;
	private Button btnNed, btnEng, btnFr;
	private HBox hboxKiesTaal;

	private void kiesTaal() {
		// foto
		rummikubFoto = new ImageView(getClass().getResource("/images/rummikub.png").toExternalForm());

		// TAAL--------------------------------------------------
		// label voor het kiezen van de taal
		lblKiesTaal = new Label();
		lblKiesTaal.setText("Welkom bij Rummikub, kies uw taal om verder te gaan.");
		lblKiesTaal.setPadding(new Insets(10));

		btnNed = new Button("Nederlands");
		btnEng = new Button("Engels");
		btnFr = new Button("Frans");

		btnNed.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// taal staat al op nederlands
				kiesAantalSpelers();
				btnNed.setDisable(true);
				btnEng.setDisable(true);
				btnFr.setDisable(true);
			}
		});

		btnEng.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				hoofdPaneel.setResourceBundle(ResourceBundle.getBundle("resources.bundle", locale_en)); // taal
																										// instellen op
																										// engels
				kiesAantalSpelers();
				btnNed.setDisable(true);
				btnEng.setDisable(true);
				btnFr.setDisable(true);
			}
		});

		btnFr.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				hoofdPaneel.setResourceBundle(ResourceBundle.getBundle("resources.bundle", locale_fr)); // frans
				kiesAantalSpelers();
				btnNed.setDisable(true);
				btnEng.setDisable(true);
				btnFr.setDisable(true);
			}
		});

		// buttons naast elkaar zetten + witruimte
		hboxKiesTaal = new HBox(btnNed, btnEng, btnFr);
		hboxKiesTaal.setSpacing(10);
		// --------------------------------------------------

		getChildren().addAll(rummikubFoto, lblKiesTaal, hboxKiesTaal);

		// ipv aanmelden, ingeven van aantal spelers, dan button naar aanmeldpaneel (om
		// het aantal spelers aan te melden), na aanmelden: nieuw paneel voor het spel
//		setCenter(aanmeldPaneel); 
	}
	// -----------------------------------

	private Label lblAantalSpelers, aantalSpelersBoodschap;
	private TextField txfAantalSpelers;
	private Button btnBevestigAantal, btnVolgende;
	private HBox hBaantalSpelers;

	private void kiesAantalSpelers() {
		// nieuw label voor het invoeren van het aantal spelers
		lblAantalSpelers = new Label();
		lblAantalSpelers.setText(hoofdPaneel.getResourceBundle().getString("hoeveelAanmelden"));
		lblAantalSpelers.setPadding(new Insets(10));

		// textfield voor het invoeren aantal spelers
		txfAantalSpelers = new TextField();

		// foutboodschap
		aantalSpelersBoodschap = new Label();

		// Button om aantal te bevestigen
		btnBevestigAantal = new Button(hoofdPaneel.getResourceBundle().getString("bevestig"));

		btnVolgende = new Button(hoofdPaneel.getResourceBundle().getString("gaNaarAanmelden"));
		btnVolgende.setVisible(false);
		btnVolgende.setPadding(new Insets(10));


		btnBevestigAantal.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// indien txf leeg is / tekst bevat --> exceptie vangen
				int aantalSpelers;
				try {
					aantalSpelers = Integer.parseInt(txfAantalSpelers.getText());
				}
				catch (NumberFormatException nfe) {
					aantalSpelersBoodschap.setText(hoofdPaneel.getResourceBundle().getString("foutGetalIngeven"));
					return;
				}

				// indien het niet in het interval ligt
				if (aantalSpelers < 2 || aantalSpelers > 4) {
					aantalSpelersBoodschap.setText(hoofdPaneel.getResourceBundle().getString("tussen2en4"));
					return;
				}

				// invoer juist -> aantal spelers instellen
				dc.setAantalSpelers(aantalSpelers);
				aantalSpelersBoodschap.setText(hoofdPaneel.getResourceBundle().getString("aantalSpelersIngesteld") + aantalSpelers);
				aantalSpelersBoodschap.setPadding(new Insets(10));
				btnVolgende.setVisible(true);

			}
		});

		btnVolgende.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				hoofdPaneel.startAanmelden();
			}
		});

		hBaantalSpelers = new HBox(lblAantalSpelers, txfAantalSpelers, btnBevestigAantal);

		getChildren().addAll(hBaantalSpelers, aantalSpelersBoodschap, btnVolgende);
	}
	// -----------------------------------

}
