package gui;

import java.util.ResourceBundle;

import domein.DomeinController;
import exceptions.splitsException;
import exceptions.steenValtBuitenBereikException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class splits extends VBox {
	private final DomeinController dc;
	private final HoofdPaneel hoofdPaneel;
	private final SpelPaneel spelpaneel;
	private ResourceBundle resourceBundle;

	private TextField txfRij, txfSteen;
	private Label lblWelkeRij, lblGemVeld, lblnaWelkeSteen, lblFout;
	private Button btnTerug, btnSplits, btnOpnieuw;
	private HBox hBsplits, hBwelkeStenen;

	public splits(DomeinController dc, HoofdPaneel hoofdPaneel, SpelPaneel spelpaneel, ResourceBundle resourceBundle) {
		this.dc = dc;
		this.hoofdPaneel = hoofdPaneel;
		this.spelpaneel = spelpaneel;
		this.resourceBundle = resourceBundle;
		splits();
	}

	private void splits() {
		maakLabelsEtc();

		btnSplits.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				int rij = 0, steen1 = 0;

				try {
					rij = Integer.parseInt(txfRij.getText());
					steen1 = Integer.parseInt(txfSteen.getText());
					
					dc.splitsTussen2Stenen(rij, steen1);
				}
				catch (NumberFormatException e) {
					erGingIetsFout();
				}
				catch(steenValtBuitenBereikException e) {
					erGingIetsFout();					
				}

				vernieuwGemVeld();
			}
		});

		btnTerug.setOnAction(e -> hoofdPaneel.speelVerder());
		
		btnOpnieuw.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				btnSplits.setDisable(false);
				lblFout.setVisible(false);
				btnOpnieuw.setVisible(false);
				lblFout.setVisible(false);
				txfRij.setText("");
				txfSteen.setText("");
			}
		});
			
		getChildren().addAll(hBsplits, hBwelkeStenen, lblGemVeld, btnTerug);
	}
	
	public void erGingIetsFout() {
		lblFout.setText(resourceBundle.getString("erGingIetsFout"));
		lblFout.setVisible(true);
		btnSplits.setDisable(true);
		btnOpnieuw.setText(resourceBundle.getString("probeerOpnieuw"));
		btnOpnieuw.setVisible(true);
	}

	public void vernieuwGemVeld() {
		// in dit paneel
		String gemveld = spelpaneel.vertaalStenen(dc.getStenenGemVeldAlsString());
		lblGemVeld.setText(gemveld);

		// op spelpaneel
		spelpaneel.toonSpelsituatie(spelpaneel.getLblToonSpelSituatie());
	}

	private void maakLabelsEtc() {
		lblWelkeRij = new Label();
		lblWelkeRij.setText(resourceBundle.getString("welkeRijSplitsen"));

		lblnaWelkeSteen = new Label();
		lblnaWelkeSteen.setText(resourceBundle.getString("naWelkeSteenSplitsen"));

		lblGemVeld = new Label();
		String gemveld = dc.getStenenGemVeldAlsString().isBlank() ? resourceBundle.getString("hetVeldIsNogLeeg")
				: dc.getStenenGemVeldAlsString();
		lblGemVeld.setText(spelpaneel.vertaalStenen(gemveld));
		
		lblFout = new Label(resourceBundle.getString("erGingIetsFout"));
		lblFout.setVisible(false);

		// --------------------------------------

		txfRij = new TextField();
		txfRij.setPadding(new Insets(1));
		txfRij.setMaxWidth(50);
		txfRij.setPromptText(resourceBundle.getString("rij"));

		txfSteen = new TextField();
		txfSteen.setPadding(new Insets(1));
		txfSteen.setMaxWidth(50);
		txfSteen.setPromptText(resourceBundle.getString("steen"));

		// --------------------------------------

		btnSplits = new Button();
		btnSplits.setPadding(new Insets(1));
		btnSplits.setMaxWidth(50);
		btnSplits.setText(resourceBundle.getString("splits"));

		btnTerug = new Button();
		btnTerug.setPadding(new Insets(1));
		btnTerug.setMaxWidth(50);
		btnTerug.setText(resourceBundle.getString("gaTerug"));
		
		btnOpnieuw = new Button(resourceBundle.getString("probeerOpnieuw"));
		btnOpnieuw.setVisible(false);

		// --------------------------------------

		hBsplits = new HBox(lblWelkeRij, txfRij);
		hBwelkeStenen = new HBox(lblnaWelkeSteen, txfSteen, btnSplits, lblFout, btnOpnieuw);

	}
}