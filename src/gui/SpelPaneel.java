package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import domein.DomeinController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SpelPaneel extends VBox {
	private final DomeinController dc;
	private final HoofdPaneel hoofdPaneel;
	private ResourceBundle resourceBundle;

	// constr
	public SpelPaneel(DomeinController dc, HoofdPaneel hoofdPaneel, ResourceBundle resourceBundle) {
		this.dc = dc;
		this.hoofdPaneel = hoofdPaneel;
		this.resourceBundle = resourceBundle;
		dc.startSpel();
		spelerSpeeltBeurt();
	}

	private Label lblSpelerAanBeurt, lblToonSpelSituatie, lblToonSpelSituatieNaBeurt, lblFoutboodschapEindbeurt;
	private int spelerAanBeurt = 0;
	private Button btnStartBeurt, btnStartActie, btnAanleggenSteen, btnSplits, btnVervangJoker, btnEindBeurt,
			btnVerplaatsSteenNaarWerkveld, btnResetBeurt, btnNext, btnNogEens;
	private HBox hbStartBeurt, hbBeurtMogelijkheden;

	private Button deleteEigenStenen;

	private void spelerSpeeltBeurt() {
		// buttons, labels maken--------------------------------------------
		maakLabelsEtc();

		// speler aan beurt instellen----------------------------------------
		dc.setSpelerAanBeurt(spelerAanBeurt % dc.getAantalSpelers());

		// label spelerAanBeurt instellen------------------------------------
		lblSpelerAanBeurt.setText(String.format("%s %s%n", dc.getSpelerAanBeurt().getGebruikersnaam(),
				resourceBundle.getString("isAanBeurt")));

		// speler wenst beurt te starten----------------------------------------
		btnStartBeurt.setText(resourceBundle.getString("klikToStart"));
		btnStartBeurt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				toonSpelsituatie(lblToonSpelSituatie);
				lblToonSpelSituatie.setVisible(true);
				btnStartActie.setVisible(true);
				btnStartActie.setDisable(false);
				btnStartBeurt.setDisable(true);
				dc.saveGemVeldVoorBeurt();
			}
		});

		// de speler wenst actie te ondernemen----------------------------------------
		btnStartActie.setText(resourceBundle.getString("onderneemActie"));
		btnStartActie.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// alle mogelijkheden tonen (buttons)
				hbBeurtMogelijkheden.setVisible(true);
				hbBeurtMogelijkheden.setDisable(false);
				// eigen stenen saven
				dc.getSpelerAanBeurt().saveEigenStenenVoorBeurt();
				btnStartActie.setDisable(true);
			}
		});

		// btnAanleggenSteen
		btnAanleggenSteen.setText(resourceBundle.getString("btnAanleggenSteen"));
		btnAanleggenSteen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				hoofdPaneel.aanleggenSteen();
			}
		});

		// btnSplits
		btnSplits.setText(resourceBundle.getString("btnSplits"));
		btnSplits.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				hoofdPaneel.splits();
			}
		});

		// btnVervangJoker
		btnVervangJoker.setText(resourceBundle.getString("btnVervangJoker"));
		btnVervangJoker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				hoofdPaneel.vervangJoker();
			}
		});

		// btnVerplaatsSteenNaarWerkveld
		btnVerplaatsSteenNaarWerkveld.setText(resourceBundle.getString("btnVerplaatsSteenNaarWerkveld"));
		btnVerplaatsSteenNaarWerkveld.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				hoofdPaneel.steenNaarWerkVeld();
			}
		});

		// btnResetBeurt
		btnResetBeurt.setText(resourceBundle.getString("btnResetBeurt"));
		btnResetBeurt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {// alles resetten naar begin beurt
				// het gemveld resetten
				dc.resetGemeenschappelijkVeld();

				// gelegde stenen resetten
				dc.getSpelerAanBeurt().resetGelegdeStenen();

				// stenen teruggeven
				dc.getSpelerAanBeurt().resetEigenStenen();

				// werkveld resetten
				dc.getSpelerAanBeurt().resetWerkveld();

				// melding geven
				lblFoutboodschapEindbeurt.setText(resourceBundle.getString("beurtWerdGereset"));
				lblFoutboodschapEindbeurt.setVisible(true);
				// spelsit tonen (staat erboven nog)
				// opties weer showen
				btnNogEens.setVisible(true);
				hbBeurtMogelijkheden.setDisable(true);

				toonSpelsituatie(getLblToonSpelSituatie());
			}
		});

		// stenen deleten om te testen
		deleteEigenStenen = new Button("TEST delete eigen stenen");
		deleteEigenStenen.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				dc.getSpelerAanBeurt().setEigenStenen(new ArrayList<>());
			}
		});
//		deleteEigenStenen.setVisible(false);
		
		// btnEindBeurt--------------------------------------------------------
		btnEindBeurt.setText(resourceBundle.getString("btnEindBeurt"));
		btnEindBeurt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				btnStartActie.setDisable(true);
				hbBeurtMogelijkheden.setDisable(true);

				// gemeenschappelijkveld voor en na beurt vergelijken om te zien of de speler
				// heeft gelegd, gesplitst, ...
				// --> speler heeft niets gedaan, dus melding & hij krijgt een steen uit de pot
				if (dc.vergelijkGemVeldVoorEnNaBeurt() && dc.werkVeldIsLeeg()) {
					lblFoutboodschapEindbeurt.setText(resourceBundle.getString("niksGedaan"));
					dc.geefSteenUitPot();
				}

				// speler heeft dus wel iets gedaan
				// --> systeem valideert de spelsituatie
				else {
					// indien hij voor de eerste keer legt
					if (dc.getSpelerAanBeurt().LegtEersteKeer()) {
						// waarde gelegde stenen moet >= 30 zijn (dus de serie/rij die gelegd werd)
						if (dc.bepaalWaardeGelegdeStenen() < 30) {
							// ongeldige spelsit: melding geven
							lblFoutboodschapEindbeurt.setText(resourceBundle.getString("eersteKeer30"));
							lblFoutboodschapEindbeurt.setVisible(true);

							// terug naar de mogelijke acties
							btnNogEens.setVisible(true);

							// hij moet dan de rest ni controleren want er is toch al een fout dus "return"
							return;
						}

						// --> joker mag niet gebruikt worden
						if (dc.jokerInGelegdeStenen()) {
							// ongeldige spelsit: melding geven
							lblFoutboodschapEindbeurt.setText(resourceBundle.getString("geenJokerEersteKeer"));
							lblFoutboodschapEindbeurt.setVisible(true);

							// terug naar de mogelijke acties
							btnNogEens.setVisible(true);

							// hij moet dan de rest ni controleren dus "return"
							return;
						}

					}

					// controleren, eerste beurt of niet
					// false: gemeenschappelijk veld niet in orde
					if (!dc.controleerRijSerieGemeenschappelijkVeld()) {
						lblFoutboodschapEindbeurt.setText(resourceBundle.getString("foutenGemVeld"));
						lblFoutboodschapEindbeurt.setVisible(true);

						btnNogEens.setVisible(true);

						// hij moet dan de rest ni controleren dus "return"
						return;
					}

					// systeem valideert de beurt

					// werkveld moet leeg zijn
					if (!dc.werkVeldIsLeeg()) {
						lblFoutboodschapEindbeurt.setText(resourceBundle.getString("hetWerkVeldMoetLeegZijn"));
						lblFoutboodschapEindbeurt.setVisible(true);

						btnNogEens.setVisible(true);

						// hij moet dan de rest ni controleren dus "return"
						return;
					}
					dc.getSpelerAanBeurt().setLegtEersteKeer(false);
				}

				// de andere buttons weer disabled zetten
				btnStartActie.setDisable(true);
				hbBeurtMogelijkheden.setDisable(true);

				// systeem toont spelsituatie na de beurt (geupdated)
				toonSpelsituatie(lblToonSpelSituatieNaBeurt);
				lblToonSpelSituatieNaBeurt.setVisible(true);

				// "volgende"
				btnNext.setVisible(true);

			}
		});

		// btnNogEens
		btnNogEens.setText(resourceBundle.getString("probeerNogEens"));
		btnNogEens.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				btnNogEens.setVisible(false);
				btnStartActie.setDisable(false);
				lblFoutboodschapEindbeurt.setVisible(false);

				// resetten
				dc.resetGemeenschappelijkVeld();
				dc.getSpelerAanBeurt().resetGelegdeStenen();
				dc.getSpelerAanBeurt().resetEigenStenen();
				dc.getSpelerAanBeurt().resetWerkveld();

				// updaten
				toonSpelsituatie(lblToonSpelSituatie);
			}
		});

		// btnNext
		btnNext.setText(resourceBundle.getString("btnNext"));
		btnNext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// al de rest weer invisible zetten
				lblToonSpelSituatie.setVisible(false);
				lblToonSpelSituatieNaBeurt.setVisible(false);
				btnStartActie.setVisible(false);
				hbBeurtMogelijkheden.setVisible(false);
				btnNext.setVisible(false);
				lblFoutboodschapEindbeurt.setVisible(false);

				// en de startbeurt clickable
				btnStartBeurt.setDisable(false);

				// gelegde stenen weer leegmaken voor volgende beurt
				dc.getSpelerAanBeurt().resetGelegdeStenen();

				// UC2: check: gewonnen? of stenen op int midden? -> einde spel
				// stop spel -> meth scores printen
				if (dc.stenenZijnOp() || dc.iemandIsGewonnen()) {
					hbStartBeurt.setVisible(false);
					toonScores();
				}
				else {
					spelerAanBeurt++;
					dc.setSpelerAanBeurt(spelerAanBeurt % dc.getAantalSpelers());
					lblSpelerAanBeurt.setText(String.format("%s %s%n", dc.getSpelerAanBeurt().getGebruikersnaam(),
							resourceBundle.getString("isAanBeurt")));
				}

			}
		});

		// gui maken----------------------------------------------------------
		getChildren().addAll(hbStartBeurt, lblToonSpelSituatie, btnStartActie, hbBeurtMogelijkheden,
				lblFoutboodschapEindbeurt, deleteEigenStenen, btnNogEens, lblToonSpelSituatieNaBeurt, btnNext);
	}

	public void maakLabelsEtc() {
		lblSpelerAanBeurt = new Label();
		lblToonSpelSituatie = new Label();
		lblToonSpelSituatieNaBeurt = new Label();
		btnStartBeurt = new Button();
		btnStartActie = new Button();
		btnEindBeurt = new Button();
		btnAanleggenSteen = new Button();
		btnSplits = new Button();
		btnVervangJoker = new Button();
		btnVerplaatsSteenNaarWerkveld = new Button();
		btnResetBeurt = new Button();
		btnNext = new Button();
		lblFoutboodschapEindbeurt = new Label();
		lblFoutboodschapEindbeurt.setVisible(false);
		btnNogEens = new Button();

		hbStartBeurt = new HBox(lblSpelerAanBeurt, btnStartBeurt);
		hbBeurtMogelijkheden = new HBox(btnAanleggenSteen, btnSplits, btnVervangJoker, btnEindBeurt,
				btnVerplaatsSteenNaarWerkveld, btnResetBeurt);

		// onnodige buttons enz eerst invisible zetten
		lblToonSpelSituatie.setVisible(false);
		lblToonSpelSituatieNaBeurt.setVisible(false);
		btnStartActie.setVisible(false);
		hbBeurtMogelijkheden.setVisible(false);
		btnNext.setVisible(false);
		btnNogEens.setVisible(false);
	}

	/**
	 * toont de spelsituatie
	 * Gem veld, eigenstenen, stenen werkveld -> String
	 */
	public void toonSpelsituatie(Label spelSit) {
		// vertalen
		String gemeenschappelijkVeld = dc.getStenenGemVeldAlsString().isBlank() ? resourceBundle.getString("leeg")
				: dc.getStenenGemVeldAlsString();

		gemeenschappelijkVeld = gemeenschappelijkVeld.replaceAll("zwart", resourceBundle.getString("zwart"));
		gemeenschappelijkVeld = gemeenschappelijkVeld.replaceAll("rood", resourceBundle.getString("rood"));
		gemeenschappelijkVeld = gemeenschappelijkVeld.replaceAll("blauw", resourceBundle.getString("blauw"));
		gemeenschappelijkVeld = gemeenschappelijkVeld.replaceAll("geel", resourceBundle.getString("geel"));
		gemeenschappelijkVeld = gemeenschappelijkVeld.replaceAll("leeg", resourceBundle.getString("leeg"));

		String eigenStenen = dc.getEigenStenenAlsString();
		eigenStenen = eigenStenen.replaceAll("zwart", resourceBundle.getString("zwart"));
		eigenStenen = eigenStenen.replaceAll("rood", resourceBundle.getString("rood"));
		eigenStenen = eigenStenen.replaceAll("blauw", resourceBundle.getString("blauw"));
		eigenStenen = eigenStenen.replaceAll("geel", resourceBundle.getString("geel"));

		String stenenWerkveld = dc.getStenenWerkveldAlsString();
		stenenWerkveld = stenenWerkveld.replaceAll("zwart", resourceBundle.getString("zwart"));
		stenenWerkveld = stenenWerkveld.replaceAll("rood", resourceBundle.getString("rood"));
		stenenWerkveld = stenenWerkveld.replaceAll("blauw", resourceBundle.getString("blauw"));
		stenenWerkveld = stenenWerkveld.replaceAll("geel", resourceBundle.getString("geel"));

		spelSit.setText(String.format("%s\n%s%s\n%s%n%s\n%s%s\n\n", resourceBundle.getString("spelsituatie"),
				resourceBundle.getString("gemVeld"), gemeenschappelijkVeld, resourceBundle.getString("persStenen"),
				eigenStenen, resourceBundle.getString("werkvStenen"), stenenWerkveld));
	}

	private Label lblScores;

	private void toonScores() {
		lblScores = new Label();

		String uitvoerScores = "";

		// scores bijhouden in list
		List<Integer> scoresList = dc.geefScores();
		// voor elke score in scores: score tonen
		for (int i = 0; i < scoresList.size(); i++) {
			// loser: score is negatief, printen
			if (scoresList.get(i) < 0) {
				uitvoerScores += String.format("%s %s: %d %s.%n", dc.getSpelerNamen().get(i),
						resourceBundle.getString("verloren"), scoresList.get(i), resourceBundle.getString("punten"));
			}

			// winnaar: score is 0 => instellen op de som van de scores * -1
			if (scoresList.get(i) == 0) {
				int scoreWinnaar = -1 * scoresList.stream().reduce(0, (a, b) -> a + b);
				dc.getSpelers().get(i).setScore(scoreWinnaar);
				uitvoerScores += String.format("%s %s: %d %s.%n", dc.getSpelerNamen().get(i),
						resourceBundle.getString("gewonnen"), scoreWinnaar, resourceBundle.getString("punten"));
			}
		}

		// scores in database toevoegen
		dc.voegScoresToe();

		lblScores.setText(uitvoerScores + "\n\n\n" + dc.geefScoresOverzicht());
		getChildren().removeAll(lblToonSpelSituatie, lblToonSpelSituatieNaBeurt);
		getChildren().addAll(lblScores);

	}

	public Label getLblToonSpelSituatie() {
		return lblToonSpelSituatie;
	}

	public String vertaalStenen(String stenen) {
		stenen = stenen.replaceAll("zwart", resourceBundle.getString("zwart"));
		stenen = stenen.replaceAll("rood", resourceBundle.getString("rood"));
		stenen = stenen.replaceAll("blauw", resourceBundle.getString("blauw"));
		stenen = stenen.replaceAll("geel", resourceBundle.getString("geel"));
		stenen = stenen.replaceAll("leeg", resourceBundle.getString("leeg"));
		return stenen;
	}
}
