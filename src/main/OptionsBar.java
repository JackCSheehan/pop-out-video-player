package main;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//This class will be used to display the options bar at the top of the window
public class OptionsBar {
	public static final Color FILL_COLOR = Color.web("0x4F4F4F", 0.77);		//Fill color of options bar
	public static final double ICON_SIZE = 25.0;							//Length and width of icons
	
	private Image searchIcon;							//Search button icon
	private StackPane searchIconHitbox;					//Hitbox for the search icon
	
	private BorderPane pane;							//Main pane where icons will be inserted
	private ImageView searchIconImageView;				//ImageView to display search icon
	
	private VideoSelectionDialog videoSelectionDialog;	//The dialog that comes up asking users if they want to select media from the internet or a file
	
	OptionsBar(Stage primaryStage, StackPane root, UI ui)
	{	
		//Assign images to icons
		searchIcon = new Image("search.png");
		
		//Create pane and modify it's settings
		pane = new BorderPane();
		pane.setBackground(new Background(new BackgroundFill(FILL_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		pane.setMaxHeight(ICON_SIZE);
		
		//Set search image size and add to pane
		searchIconImageView = new ImageView(searchIcon);
		searchIconImageView.setFitHeight(ICON_SIZE);
		searchIconImageView.setFitWidth(ICON_SIZE);
		
		//Creates the video selection dialog
		videoSelectionDialog = new VideoSelectionDialog(primaryStage, root, ui);
		StackPane.setAlignment(videoSelectionDialog.getPane(), Pos.BOTTOM_CENTER);
		
		//Set hover functionality to video selection dialog goes away when mouse moves off of the app
		root.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> 
		{
			//Remove video selection dialog when mouse stops hovering on app
			if (!newValue)
			{
				//Check to make sure the selection dialog is added before trying to remove it
				if (root.getChildren().contains(videoSelectionDialog.getPane()))
				{
					root.getChildren().remove(videoSelectionDialog.getPane());
				}
			}
		});
		
		searchIconHitbox = new StackPane();
		searchIconHitbox.getChildren().add(searchIconImageView);
		
		//Set on click function for the search button
		searchIconHitbox.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent mouseEvent)
			{
				root.getChildren().add(videoSelectionDialog.getPane());
			}
		});
		
		pane.setLeft(searchIconHitbox);
	}
	
	//Getters
	BorderPane getPane() { return pane; }
	VideoSelectionDialog getVideoSelectionDialog() { return videoSelectionDialog; }
	
}
