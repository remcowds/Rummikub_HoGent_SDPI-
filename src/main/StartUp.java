package main;

import cui.RummikubApplicatie_UC1;
import cui.RummikubApplicatie_UC2;
import domein.DomeinController;
import gui.HoofdPaneel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class StartUp extends Application {

	public static void main(String[] args) {

//		// UC1 (CLI):
//		RummikubApplicatie_UC1 uc1 = new RummikubApplicatie_UC1();
//
//		// UC2 (CLI):
//		RummikubApplicatie_UC2 uc2 = new RummikubApplicatie_UC2(uc1);
//		// aan uc2 uc1 meegeven om dezelfde domeincontroller en resourcebundel te hebben  

		// UC3 (GUI):
		Application.launch(StartUp.class, args);

		// UC3: normaal verloop DONE, alt verl niet:
		// TODO UC3: enkel nog joker leggen & vervangen uitwerken
	}

	@Override
	public void start(Stage stage) {
		DomeinController controller = new DomeinController();
		Scene scene = new Scene(new HoofdPaneel(controller));
		stage.setMaximized(true);
//		stage.setFullScreen(true);
		stage.setTitle("Rummikub - G95");
		stage.setScene(scene);
		stage.show();
	}

}
