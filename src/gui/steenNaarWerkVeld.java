package gui;

import java.util.ResourceBundle;

import domein.DomeinController;
import exceptions.GeenStartOfEindSteenException;
import exceptions.alSteenHierException;
import exceptions.geenSteenHierException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class steenNaarWerkVeld extends VBox {
	private final DomeinController dc;
	private final HoofdPaneel hoofdPaneel;
	private SpelPaneel spelpaneel;
	private ResourceBundle resourceBundle;

	private TextField txfRij, txfKolom;
	private HBox hBPositie;
	private Button btnVerplaats, btnTerug;

	public steenNaarWerkVeld(DomeinController dc, HoofdPaneel hoofdPaneel, ResourceBundle resourceBundle,
			SpelPaneel spelPaneel) {
		this.dc = dc;
		this.hoofdPaneel = hoofdPaneel;
		this.resourceBundle = resourceBundle;
		this.spelpaneel = spelPaneel;

		plaatsSteen();
	}

	private Label lblWelkePlaats, lblGemVeld, lblWerkVeld, lblFoutboodschap;
	private Button btnNogEens;

	private void plaatsSteen() {
		maakLabelsEtc();

		btnVerplaats.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					int rij = Integer.parseInt(txfRij.getText());
					int kolom = Integer.parseInt(txfKolom.getText());

					// de steen naar het werkveld verplaatsen (dus ier verwijd)
					dc.verplaatsSteenWerkVeld(rij - 1, kolom - 1);

					vernieuwWerkEnGemVeld();
				}
				catch (NumberFormatException e) {
					lblFoutboodschap.setText(resourceBundle.getString("steenVESerie"));
					lblFoutboodschap.setVisible(true);
					btnVerplaats.setDisable(true);
					btnNogEens.setText(resourceBundle.getString("probeerOpnieuw"));
					btnNogEens.setVisible(true);
				}
				catch (geenSteenHierException e) {
					lblFoutboodschap.setText(resourceBundle.getString("hierLigtGeenSteen"));
					lblFoutboodschap.setVisible(true);
					btnVerplaats.setDisable(true);
					btnNogEens.setText(resourceBundle.getString("probeerOpnieuw"));
					btnNogEens.setVisible(true);
				}
				catch (GeenStartOfEindSteenException e) {
					lblFoutboodschap.setText(resourceBundle.getString("steenVESerie"));
					lblFoutboodschap.setVisible(true);
					btnVerplaats.setDisable(true);
					btnNogEens.setText(resourceBundle.getString("probeerOpnieuw"));
					btnNogEens.setVisible(true);
				}

				txfKolom.setText("");
				txfRij.setText("");

			}
		});

		btnNogEens.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				btnNogEens.setVisible(false);
				btnVerplaats.setDisable(false);
				lblFoutboodschap.setVisible(false);
			}
		});

		btnTerug.setOnAction(e -> hoofdPaneel.speelVerder());

		getChildren().addAll(hBPositie, lblGemVeld, lblWerkVeld, btnTerug);
	}

	public void vernieuwWerkEnGemVeld() {
		// in dit paneel
		String gemveld = spelpaneel.vertaalStenen(dc.getStenenGemVeldAlsString());
		String werkveld = spelpaneel.vertaalStenen(dc.getStenenWerkveldAlsString());
		lblWerkVeld.setText(String.format("%s %s%n", resourceBundle.getString("uwWerkveld"), werkveld));
		lblGemVeld.setText(gemveld);

		// op spelpaneel
		spelpaneel.toonSpelsituatie(spelpaneel.getLblToonSpelSituatie());
		spelpaneel.toonSpelsituatie(spelpaneel.getLblToonSpelSituatie());
	}

	public void maakLabelsEtc() {
		lblFoutboodschap = new Label();

		lblWelkePlaats = new Label();
		lblWelkePlaats.setText(resourceBundle.getString("welkePlaats"));

		lblGemVeld = new Label();
		String gemveld = spelpaneel.vertaalStenen(dc.getStenenGemVeldAlsString());
		lblGemVeld.setText(String.format("%s %s%n", resourceBundle.getString("gemVeld"), gemveld));

		lblWerkVeld = new Label();
		lblWerkVeld.setText(String.format("%s %s%n", resourceBundle.getString("uwWerkveld"),
				dc.getSpelerAanBeurt().getStenenWerkveldAlsString()));

		txfRij = new TextField();
		txfRij.setPadding(new Insets(1));
		txfRij.setMaxWidth(50);
		txfRij.setPromptText(resourceBundle.getString("rij"));

		txfKolom = new TextField();
		txfKolom.setPadding(new Insets(1));
		txfKolom.setMaxWidth(50);
		txfKolom.setPromptText(resourceBundle.getString("kolom"));

		btnNogEens = new Button();
		btnNogEens.setVisible(false);

		btnVerplaats = new Button();
		btnVerplaats.setPadding(new Insets(1));
		btnVerplaats.setMaxWidth(150);
		btnVerplaats.setText(resourceBundle.getString("verplaatsSteenNaarWerkVeld"));

		btnTerug = new Button();
		btnTerug.setPadding(new Insets(1));
		btnTerug.setMaxWidth(50);
		btnTerug.setText(resourceBundle.getString("gaTerug"));

		hBPositie = new HBox(lblWelkePlaats, txfRij, txfKolom, btnVerplaats, lblFoutboodschap, btnNogEens);
	}
}
