package Recaman;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
//		recaman.toImage(1920, 30);
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		Recaman recaman = new Recaman();
		
		Label stepsLabel = new Label("Calculated\nSteps");
		stepsLabel.setCenterShape(true);
		Label stepsCountLabel = new Label("0");
		stepsCountLabel.setFont(new Font(30));
		HBox stepsLabelBox = new HBox(30, stepsLabel, stepsCountLabel);

		Label calculateCompleteLabel = new Label("Length of\ncomplete list");
		Label calculateCompleteNumberLabel = new Label("0");
		calculateCompleteNumberLabel.setFont(new Font(30));
		HBox calculateCompleteBox = new HBox(30, calculateCompleteLabel, calculateCompleteNumberLabel);
		
		Slider completeSlider = new Slider(200, 1200, 1000);
		completeSlider.setMajorTickUnit(200);
		completeSlider.setMinorTickCount(0);
		completeSlider.setSnapToTicks(true);
		completeSlider.setShowTickMarks(true);
		completeSlider.setShowTickLabels(true);
		completeSlider.setPrefWidth(200);
		Label completeLabel = new Label("Complete\nSequence");
		Button calculateCompleteButton = new Button("Calculate");
		calculateCompleteButton.setOnAction(e -> {
			recaman.calcComplete((int)completeSlider.getValue());
			calculateCompleteNumberLabel.setText((int)completeSlider.getValue() + "  (actual: " + recaman.getComplete() + ")");
			stepsCountLabel.setText(recaman.getSteps() + "");
		});
		HBox completeBox = new HBox(30, calculateCompleteButton, completeSlider, completeLabel);
		
		TextField calculateStepsInput = new TextField("10000");
		// force the field to be numeric only
		calculateStepsInput.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) calculateStepsInput.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
		Button calculateStepsButton = new Button("Calculate Steps");
		calculateStepsButton.setOnAction(e -> {
			recaman.calcSteps(Integer.parseInt(calculateStepsInput.getText()));
			calculateCompleteNumberLabel.setText("" + recaman.getComplete());
			stepsCountLabel.setText(recaman.getSteps() + "");
		});
		HBox calculateStepsBox = new HBox(30, calculateStepsButton, calculateStepsInput);
		
		VBox mainBox = new VBox(30);
		mainBox.setPadding(new Insets(20));
		mainBox.setAlignment(Pos.CENTER);
		mainBox.getChildren().addAll(
				stepsLabelBox,
				calculateCompleteBox,
				new Rectangle(300, 5, Color.GREEN),
				calculateStepsBox,
				completeBox,
				new Rectangle(300, 5, Color.GREEN)
		);
		
		stage.setScene(new Scene(mainBox));
		stage.setTitle("Recamán Maker");
		stage.centerOnScreen();
		stage.setResizable(false);
		stage.show();
	}
}
