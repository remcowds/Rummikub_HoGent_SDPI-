package gui;

import java.util.ResourceBundle;

import domein.DomeinController;
import javafx.scene.layout.BorderPane;

public class HoofdPaneel extends BorderPane {
	// taal
	private ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.bundle"); // taal standaard nederlands
	// --------------

	private final DomeinController dc;
	private final StartPaneel startPaneel;
	private SpelPaneel spelPaneel;

	// constructor
	public HoofdPaneel(DomeinController dc) {
		this.dc = dc;
		this.startPaneel = new StartPaneel(dc, this);

		setCenter(startPaneel);
	}

	public void startAanmelden() {
		setCenter(new AanmeldPaneel(dc, this, resourceBundle));
	}

	public void startSpel() {
		spelPaneel = new SpelPaneel(dc, this, resourceBundle);
		setCenter(spelPaneel);
	}

	public void speelVerder() {
		setCenter(spelPaneel);
	}

	// -------------------------------------------------

	public void aanleggenSteen() {
		setCenter(new aanleggenSteen(dc, this, spelPaneel, resourceBundle));
	}

	public void splits() {
		setCenter(new splits(dc, this, spelPaneel,resourceBundle));
	}

	public void vervangJoker() {
		setCenter(new vervangJoker(dc, this, spelPaneel, resourceBundle));
	}
	
	public void steenNaarWerkVeld() {
		setCenter(new steenNaarWerkVeld(dc, this, resourceBundle, spelPaneel));
	}

	// -------------------------------------------------

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

}
