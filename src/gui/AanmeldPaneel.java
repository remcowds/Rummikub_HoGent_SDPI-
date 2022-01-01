package gui;

import java.util.ResourceBundle;

import domein.DomeinController;
import exceptions.AlAangemeldException;
import exceptions.VerkeerdeGegevensException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AanmeldPaneel extends VBox {
	private final DomeinController dc;
	private final HoofdPaneel hoofdPaneel;
	private ResourceBundle resourceBundle;

	// constructor
	public AanmeldPaneel(DomeinController dc, HoofdPaneel hoofdPaneel, ResourceBundle resourceBundle) {
		this.dc = dc;
		this.hoofdPaneel = hoofdPaneel;
		this.resourceBundle = resourceBundle;
		aanmeldenEnOverzicht();
	}

	private HBox hBGebruikersnaam, hBWachtwoord;
	private Label lblGebruikersnaam, lblWachtwoord, lblBoodschapAanmelden, lblOverzichtGebruikers;
	private TextField txfGebruikersnaam;
	private PasswordField pwdWachtwoord;
	private Button btnMeldAan, btnSpeel;

	private void aanmeldenEnOverzicht() {
		hBGebruikersnaam = new HBox();
		lblGebruikersnaam = new Label(resourceBundle.getString("gebruikersnaam"));
		lblGebruikersnaam.setPadding(new Insets(10));
		txfGebruikersnaam = new TextField();
		hBGebruikersnaam = new HBox(lblGebruikersnaam, txfGebruikersnaam);

		hBWachtwoord = new HBox();
		lblWachtwoord = new Label(resourceBundle.getString("wachtwoord"));
		lblWachtwoord.setPadding(new Insets(10));
		pwdWachtwoord = new PasswordField();
		hBWachtwoord = new HBox(lblWachtwoord, pwdWachtwoord);

		lblBoodschapAanmelden = new Label();
		lblOverzichtGebruikers = new Label();

		btnMeldAan = new Button(resourceBundle.getString("meldAan"));
		btnMeldAan.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try { // proberen aan te melden
					dc.meldAan(txfGebruikersnaam.getText(), pwdWachtwoord.getText());
					lblBoodschapAanmelden.setText(
							resourceBundle.getString("welkomAangemeld") + txfGebruikersnaam.getText());
					txfGebruikersnaam.setText("");
					pwdWachtwoord.setText("");
				}
				catch (VerkeerdeGegevensException ex) {
					lblBoodschapAanmelden.setText(resourceBundle.getString("verkeerdeGeg"));
					txfGebruikersnaam.setText("");
					pwdWachtwoord.setText("");
					return;
				}
				catch (AlAangemeldException ex) {
					lblBoodschapAanmelden.setText(resourceBundle.getString("alAangemeld"));
					txfGebruikersnaam.setText("");
					pwdWachtwoord.setText("");
					return;
				}

				// als alle spelers aangemeld zijn het overzicht geven, de button disablen &
				// meth oproepen
				if (dc.getSpelers().size() >= dc.getAantalSpelers()) {
					btnMeldAan.setDisable(true);
					lblOverzichtGebruikers.setText(String.format("%n%s%n%s",
							resourceBundle.getString("overzichtgeb"), dc.geefOverzicht()));
					kiezenSpeelOverzicht();
				}

			}
		});

		getChildren().addAll(hBGebruikersnaam, hBWachtwoord, btnMeldAan, lblBoodschapAanmelden, lblOverzichtGebruikers);
	}

	private Label lblkiesUit, lblOverzicht;
	private Button btngeefOverzicht, btnspeel;
	private HBox hBkiezen;

	/**
	 * geeft een overzicht met elke mogelijke actie
	 */
	private void kiezenSpeelOverzicht() {
		lblkiesUit = new Label();
		lblkiesUit.setText(resourceBundle.getString("kiesUitSpeelEnOverzichtGUI"));

		btngeefOverzicht = new Button(resourceBundle.getString("geefOverzicht"));
		lblOverzicht = new Label();
		btngeefOverzicht.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				lblOverzicht.setText(dc.geefScoresOverzicht());
			}
		});

		btnspeel = new Button(resourceBundle.getString("speelSpel"));
		btnspeel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//spelpaneel weergeven
				hoofdPaneel.startSpel();
			}
		});

		hBkiezen = new HBox(btngeefOverzicht, btnspeel);

		getChildren().addAll(lblkiesUit, hBkiezen, lblOverzicht);
	}

}
