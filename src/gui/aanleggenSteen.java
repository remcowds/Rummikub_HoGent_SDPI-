package gui;

import java.util.ResourceBundle;

import domein.DomeinController;
import exceptions.alSteenHierException;
import exceptions.geenCorrecteSerieRijException;
import exceptions.steenValtBuitenBereikException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class aanleggenSteen extends VBox {
	private final DomeinController dc;
	private final HoofdPaneel hoofdPaneel;
	private SpelPaneel spelpaneel;
	private ResourceBundle resourceBundle;

	private TextField txfRij, txfKolom, txfSteen;
	private HBox hBPositie, hBSteen, hBEigenOfWerk;
	private Button btnPlaats, btnTerug, btnNogEens, btnEigen, btnWerkv;
	private Label lblEigenOfWerk, lblWelkeSteen, lblUwStenen, lblWelkePlaats, lblGemVeld, lblFout;

	private String eigenStenen, stenenWerkveld;

	private boolean boolEigenSteen = true;

	public aanleggenSteen(DomeinController dc, HoofdPaneel hoofdPaneel, SpelPaneel spelpaneel,
			ResourceBundle resourceBundle) {
		this.dc = dc;
		this.hoofdPaneel = hoofdPaneel;
		this.spelpaneel = spelpaneel;
		this.resourceBundle = resourceBundle;

		plaatsSteen();
	}

	private void plaatsSteen() {
		maakLabelsEtc();

		// -----------------

		// eigen stenen laten zien om uit te kiezen
		eigenStenen = dc.getSpelerAanBeurt().getEigenStenenAlsString();
		eigenStenen = spelpaneel.vertaalStenen(eigenStenen);
		eigenStenen = String.format("\t%s %s\n", resourceBundle.getString("persStenen"), eigenStenen);

		// -----------------

		// stenen uit werkveld laten zien om uit te kiezen
		stenenWerkveld = dc.getStenenWerkveldAlsString();
		stenenWerkveld = spelpaneel.vertaalStenen(stenenWerkveld);
		stenenWerkveld = String.format("\t%s %s\n", resourceBundle.getString("stenenUitWerkveld"), stenenWerkveld);

		// -----------------

		btnPlaats.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				int rij = 0, kolom = 0, steen = 0;

				try {
					rij = Integer.parseInt(txfRij.getText());
					kolom = Integer.parseInt(txfKolom.getText());
					steen = Integer.parseInt(txfSteen.getText());

					if (boolEigenSteen) {
						// de steen (niet werkveld) op het gemeenschappelijk veld plaatsen
						dc.plaatsPerssSteenGemVeld(rij - 1, kolom - 1, steen - 1);

						// toevoegen om alle gelegde stenen v/d beurt bij te houden (enkel persoonlijk
						// bezig ig)
						dc.getSpelerAanBeurt().addGelegdeSteen(dc.getSpelerAanBeurt().getEigenStenen().get(steen - 1));

						// de steen uit de inventory v/d speler doen
						dc.getSpelerAanBeurt().getEigenStenen().remove(steen - 1);

						vernieuwPersoonlijkeStenen();
					}
					else { // steen uit werkveld
							// steen uit werkveld op gemveld plaatsen
						dc.plaatsSteenWerkveldOpGemVeld(rij - 1, kolom - 1, steen - 1);

						// steen uit werkveld doen
						dc.getSpelerAanBeurt().getStenenWerkveld().remove(steen - 1);

						vernieuwWerkveld();
					}

					vernieuwGemVeld();
				}
				catch (NumberFormatException e) {
					erGingIetsFout();
				}
				catch (alSteenHierException e) {
					erGingIetsFout();
				}
				catch (steenValtBuitenBereikException e) {
					erGingIetsFout();
				}
				catch (geenCorrecteSerieRijException e) {
					erGingIetsFout();
				}

				txfKolom.setText("");
				txfRij.setText("");
				txfSteen.setText("");
			}
		});
		
		// -----------------

		btnNogEens.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				btnNogEens.setVisible(false);
				btnPlaats.setDisable(false);
				lblFout.setVisible(false);
			}
		});

		// -----------------

		btnTerug.setOnAction(e -> hoofdPaneel.speelVerder());

		// -----------------

		btnEigen.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				boolEigenSteen = true;

				btnEigen.setDisable(true);
				btnWerkv.setDisable(true);

				lblUwStenen.setText(eigenStenen);
				lblUwStenen.setVisible(true);

				hBSteen.setVisible(true);
				hBPositie.setVisible(true);

				lblGemVeld.setVisible(true);
				btnTerug.setVisible(true);
			}
		});

		// -----------------

		btnWerkv.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				boolEigenSteen = false;

				btnEigen.setDisable(true);
				btnWerkv.setDisable(true);

				lblUwStenen.setText(stenenWerkveld);
				lblUwStenen.setVisible(true);

				hBSteen.setVisible(true);
				hBPositie.setVisible(true);

				lblGemVeld.setVisible(true);
				btnTerug.setVisible(true);
			}
		});

		// -----------------

		// gui maken
		getChildren().addAll(hBEigenOfWerk, hBSteen, lblUwStenen, hBPositie, lblGemVeld, btnTerug);
	}
	
	public void erGingIetsFout() {
		lblFout.setText(resourceBundle.getString("erGingIetsFout"));
		lblFout.setVisible(true);
		btnPlaats.setDisable(true);
		btnNogEens.setText(resourceBundle.getString("probeerOpnieuw"));
		btnNogEens.setVisible(true);
	}

	public void vernieuwGemVeld() {
		// in dit paneel
		String gemveld = spelpaneel.vertaalStenen(dc.getStenenGemVeldAlsString());
		lblGemVeld.setText(gemveld);

		// op spelpaneel
		spelpaneel.toonSpelsituatie(spelpaneel.getLblToonSpelSituatie());
	}

	public void vernieuwPersoonlijkeStenen() {
		String eigenStenen = spelpaneel.vertaalStenen(dc.getSpelerAanBeurt().getEigenStenenAlsString());
		lblUwStenen.setText(String.format("%s %s%n", resourceBundle.getString("persStenen"), eigenStenen));
	}

	public void vernieuwWerkveld() {
		String stenenWerkveld = spelpaneel.vertaalStenen(dc.getSpelerAanBeurt().getStenenWerkveldAlsString());
		lblUwStenen.setText(String.format("%s %s\n", resourceBundle.getString("stenenUitWerkveld"), stenenWerkveld));
	}

	private void maakLabelsEtc() {
		btnEigen = new Button(resourceBundle.getString("uitEigenStenenKiezen"));
		btnWerkv = new Button(resourceBundle.getString("uitWerkveldKiezen"));
		btnTerug = new Button();
		btnTerug.setPadding(new Insets(1));
		btnTerug.setText(resourceBundle.getString("gaTerug"));
		btnNogEens = new Button();
		btnNogEens.setVisible(false);
		btnPlaats = new Button();
		btnPlaats.setPadding(new Insets(1));
		btnPlaats.setMaxWidth(150);
		btnPlaats.setText(resourceBundle.getString("plaatsSteenOpBord"));

		// -----------------------------------

		lblEigenOfWerk = new Label(resourceBundle.getString("steenUitEigenStenenOFWerkveld"));
		lblWelkeSteen = new Label();
		lblWelkeSteen.setText(resourceBundle.getString("vraagWelkeSteenAanleggen"));
		lblFout = new Label();
		// zijn stenen tonen om te kiezen
		lblUwStenen = new Label();
		lblUwStenen.setText(resourceBundle.getString("uwStenen"));
		lblUwStenen.setVisible(false);
		lblWelkePlaats = new Label();
		lblWelkePlaats.setText(resourceBundle.getString("welkePlaats"));
		// het gemeenschappelijk veld laten zien om de plaats te kiezen
		lblGemVeld = new Label();
		String gemveld = dc.getStenenGemVeldAlsString();
		gemveld = spelpaneel.vertaalStenen(gemveld);
		lblGemVeld.setText(gemveld);
		lblGemVeld.setVisible(false);

		// -----------------------------------

		txfSteen = new TextField();
		txfRij = new TextField();
		txfKolom = new TextField();

		txfRij.setPadding(new Insets(1));
		txfRij.setMaxWidth(50);
		txfRij.setPromptText(resourceBundle.getString("rij"));

		txfKolom.setPadding(new Insets(1));
		txfKolom.setMaxWidth(50);
		txfKolom.setPromptText(resourceBundle.getString("kolom"));

		txfSteen.setPadding(new Insets(1));
		txfSteen.setMaxWidth(50);
		txfSteen.setPromptText(resourceBundle.getString("steen"));

		// -----------------------------------

		hBEigenOfWerk = new HBox(lblEigenOfWerk, btnEigen, btnWerkv);
		hBSteen = new HBox(lblWelkeSteen, txfSteen);
		hBSteen.setVisible(false);
		hBPositie = new HBox(lblWelkePlaats, txfRij, txfKolom, btnPlaats, lblFout, btnNogEens);
		hBPositie.setVisible(false);
	}
}
