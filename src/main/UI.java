package main;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//Master UI class that has boilerplate JavaFX code and shows different UIs based on video type
public class UI 
{
	public static Color FILL_COLOR = Color.BLACK;	//Background color of window
	
	private StackPane root;						//Root pane where all nodes will be added to
	private Scene scene;						//Main scene of the application
	private Stage primaryStage;					//Primary stage of UI
	private String windowTitle;					//Title of window
	private OptionsBar optionsBar;				//Bar with search button
	
	private FilePlayerUI filePlayerUI;			//The UI for the file plater
	private YouTubePlayerUI youTubePlayerUI;	//The UI for playing YouTube videos
	
	UI(Stage ps, String wt)
	{
		//Assign members
		primaryStage = ps;
		windowTitle = wt;
		
		root = new StackPane();
		root.setBackground(new Background(new BackgroundFill(FILL_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		
		optionsBar = new OptionsBar(primaryStage, root, this);
		StackPane.setAlignment(optionsBar.getPane(), Pos.TOP_CENTER);
		
		//Add options bar so that it is displayed when application is launched
		root.getChildren().add(optionsBar.getPane());
		
		//Add hover functionality to root so that options bar is removed
		root.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> 
		{
			//Show top and bottom bars on hover
			if (newValue)
			{
				//Check to make sure options bar is not already being displayed before trying to add it again
				if (!root.getChildren().contains(optionsBar.getPane()))
				{
					root.getChildren().addAll(optionsBar.getPane());
				}
			}
			else
			{
				//Hide options bar when the mouse moves off of the window
				root.getChildren().remove(optionsBar.getPane());
			}
		});
		
		//Create scene
		scene = new Scene(root, 256, 124);
		scene.setFill(FILL_COLOR);
	}
	
	//Determines the video type based on the media source
	private VideoType determineVideoType(String mediaSource)
	{
		VideoType videoType;	//The video type of the media source
		
		//Determine video type
		if (mediaSource.toLowerCase().contains("youtube"))
		{
			videoType = VideoType.YOUTUBE;
		}
		else
		{
			videoType = VideoType.FILE;
		}
		
		return videoType;
	}
	
	//Sets all of the primary stage's properties
	public void setStage()
	{
		primaryStage.setTitle(windowTitle);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.sizeToScene();
		primaryStage.setScene(scene);
	}
	
	//Display the window on the screen
	public void showWindow()
	{
		primaryStage.show();
	}
	
	//Reloads the UI and determines which UI type to use
	public void reloadUI(String mediaSource)
	{
		//Remove all nodes from UI
		root.getChildren().removeAll();
		
		//Get video type
		VideoType videoType = determineVideoType(mediaSource);
		
		//Check to see the type of media being played before adding stuff to the root pane.
		if (videoType == VideoType.FILE)
		{
			//If there is already a file player, call its close method so it stops all other media
			if (filePlayerUI != null)
			{
				filePlayerUI.close();
			}
			
			filePlayerUI = new FilePlayerUI(primaryStage, mediaSource);
			root.getChildren().add(filePlayerUI.getPane());
		}
		else
		{
			YouTubePlayerUI youTubePlayerUI = new YouTubePlayerUI(mediaSource);
			root.getChildren().add(youTubePlayerUI.getPane());
			
		}
	}
}
