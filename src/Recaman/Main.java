package Recaman;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
		stepsLabel.setPrefWidth(100);
		Label stepsCountLabel = new Label("0");
		stepsCountLabel.setFont(new Font(30));
		HBox stepsLabelBox = new HBox(20, stepsLabel, stepsCountLabel);

		Label completeLabel = new Label("Length of\ncomplete list");
		completeLabel.setPrefWidth(100);
		Label completeCountLabel = new Label("0");
		completeCountLabel.setFont(new Font(30));
		HBox completeBox = new HBox(20, completeLabel, completeCountLabel);
		
		TextField calculateStepsInput = new TextField("10000");
		Button calculateStepsButton = new Button("Calculate Steps");
		HBox calculateStepsBox = new HBox(20, calculateStepsButton, calculateStepsInput);
		
		Slider calculateCompleteSlider = new Slider(200, 1200, 1000);
		calculateCompleteSlider.setMajorTickUnit(200);
		calculateCompleteSlider.setMinorTickCount(0);
		calculateCompleteSlider.setSnapToTicks(true);
		calculateCompleteSlider.setShowTickMarks(true);
		calculateCompleteSlider.setShowTickLabels(true);
		calculateCompleteSlider.setPrefWidth(200);
		Label calculateCompleteLabel = new Label("Complete\nSequence");
		Button calculateCompleteButton = new Button("Calculate");
		HBox calculateCompleteBox = new HBox(20, calculateCompleteButton, calculateCompleteSlider, calculateCompleteLabel);
		
		TextField xField = new TextField(recaman.DEFAULT_X + "");
		xField.setPrefWidth(100);
		Label xLabel = new Label("Width of Image (0 for default)");
		xLabel.setFont(new Font(16));
		HBox xBox = new HBox(20, xField, xLabel);
		
		TextField ratioXField = new TextField(recaman.DEFAULT_RATIO_X + "");
		TextField ratioYField = new TextField(recaman.DEFAULT_RATIO_Y + "");
		ratioXField.setPrefWidth(40);
		ratioYField.setPrefWidth(40);
		Label ratioLabel = new Label("Image Ratio");
		ratioLabel.setFont(new Font(16));
		TextField scaleField = new TextField(recaman.DEFAULT_SCALE + "");
		scaleField.setPrefWidth(60);
		Label scaleLabel = new Label("Scale");
		scaleLabel.setFont(new Font(16));
		HBox ratioBox = new HBox(20, ratioXField, ratioYField, ratioLabel, scaleField, scaleLabel);
		
		CheckBox coloredCheck = new CheckBox("Colored");
		coloredCheck.setSelected(recaman.DEFAULT_COLORED);
		TextField lineWidthField = new TextField(recaman.DEFAULT_LINE_WIDTH + "");
		lineWidthField.setPrefWidth(60);
		Label lineWidthLabel = new Label("Width of the Lines");
		lineWidthLabel.setFont(new Font(16));
		HBox coloredBox = new HBox(20, coloredCheck, lineWidthField, lineWidthLabel);
		
		Button renderButton = new Button("Render");
		renderButton.setPrefWidth(100);
		Label renderLabel1 = new Label("    Filename:    ");
		renderLabel1.setFont(new Font(16));
		TextField renderField = new TextField("img");
		renderField.setPrefWidth(130);
		Label renderLabel2 = new Label(".png");
		renderLabel2.setFont(new Font(16));
		HBox renderBox = new HBox(renderButton, renderLabel1, renderField, renderLabel2);
		
		
		// Main Box
		VBox mainBox = new VBox(30);
		mainBox.setPadding(new Insets(20));
		mainBox.setAlignment(Pos.CENTER);
		mainBox.getChildren().addAll(
				stepsLabelBox,
				completeBox,
				new Rectangle(350, 5, Color.GREEN),
				calculateStepsBox,
				calculateCompleteBox,
				new Rectangle(350, 5, Color.GREEN),
				xBox,
				ratioBox,
				coloredBox,
				renderBox
		);

		// force the field to be numeric only
		calculateStepsInput.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) calculateStepsInput.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
		xField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) xField.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
		ratioXField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) ratioXField.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
		ratioYField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) ratioYField.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
		scaleField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) scaleField.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
		lineWidthField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) lineWidthField.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
		
		// Buttons on Action
		calculateCompleteButton.setOnAction(e -> {
			stage.setTitle("calculating...");
			recaman.calcComplete((int)calculateCompleteSlider.getValue());
			completeCountLabel.setText((int)calculateCompleteSlider.getValue() + "  (actual: " + recaman.getComplete() + ")");
			stepsCountLabel.setText(recaman.getSteps() + "");
			stage.setTitle("Recamán Maker");
		});
		calculateStepsButton.setOnAction(e -> {
			stage.setTitle("calculating...");
			recaman.calcSteps(Integer.parseInt(calculateStepsInput.getText()));
			completeCountLabel.setText("" + recaman.getComplete());
			stepsCountLabel.setText(recaman.getSteps() + "");
			stage.setTitle("Recamán Maker");
		});
		renderButton.setOnAction(e -> {
			stage.setTitle("rendering...");
			recaman.toImage(Integer.parseInt(xField.getText()), Integer.parseInt(ratioXField.getText()),
					Integer.parseInt(ratioYField.getText()), Integer.parseInt(scaleField.getText()),
					coloredCheck.isSelected(), Integer.parseInt(lineWidthField.getText()));
			stage.setTitle("Recamán Maker");
		});
		
		Scene scene = new Scene(mainBox);
		stage.setScene(scene);
		stage.setTitle("Recamán Maker");
		stage.centerOnScreen();
		stage.setResizable(false);
		stage.show();
	}
}
