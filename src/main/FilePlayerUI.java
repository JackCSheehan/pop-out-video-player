package main;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//This class has all the code needed for the file player UI
public class FilePlayerUI 
{
	public static final String ERROR_TEXT = "Could not load file: ";	//The text show when the video cannot be loaded
	public static final String LOADING_TEXT = "Loading video: ";		//Text to show while video is being loaded
	public static final Color FILL_COLOR = Color.BLACK;					//Color of background
	public static final Color TEXT_COLOR = Color.WHITE;					//Color of text color
	
	private VideoController videoController;	//Video controller with nodes for controlling video
	private String mediaSource;					//The file path of the video to play
	private Media media;						//Media object for video
	private MediaPlayer mediaPlayer;			//Media player to play the video
	private MediaView mediaView;				//Media view to view the video
	private StackPane pane;						//Main pane where all nodes will be added
	private Text errorText;						//Text to show if video can't be loaded
	private Text loadingText;					//Text to show while loading
	
	FilePlayerUI(Stage primaryStage, String ms)
	{	
		//Assign members
		mediaSource = ms;
		
		//Create pane and set settings
		pane = new StackPane();
		pane.setBackground(new Background(new BackgroundFill(FILL_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		
		loadingText = new Text(LOADING_TEXT + mediaSource);
		loadingText.setFill(TEXT_COLOR);
		pane.getChildren().add(loadingText);
			
		//Try to load the media source and set hover functionality for pane
		try
		{
			loadAndPlayMedia();
			
			//Add hover functionality so that the video controller goes away when the mouse leaves the window
			pane.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> 
			{
				handleMouseHoverAction(newValue);
			});
		}
		catch (Exception e)
		{
			errorText = new Text(ERROR_TEXT + mediaSource);
			errorText.setFill(TEXT_COLOR);
			
			StackPane.setAlignment(errorText, Pos.CENTER);
			
			//Remove all and add error text to pane
			pane.getChildren().add(errorText);
		}
		
		pane.getChildren().remove(loadingText);
	}
	
	//Load media from media source and set up pane to display that media
	private void loadAndPlayMedia()
	{	
		media = new Media(mediaSource);
		
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setAutoPlay(true);
		
		//Video controller that lets user control video
		videoController = new VideoController(mediaPlayer);
		StackPane.setAlignment(videoController.getPane(), Pos.BOTTOM_CENTER);
		
		mediaView = new MediaView(mediaPlayer);
		
		//Bind the media view's size to the window's size so they resize together
		mediaView.fitWidthProperty().bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
		mediaView.fitHeightProperty().bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
		
		//Add media view to pane
		pane.getChildren().removeAll();
		pane.getChildren().add(mediaView);
	}
	
	//This function handles the logic of hiding and showing UI when the mouse moves on and off the application
	private void handleMouseHoverAction(boolean newValue)
	{
		//Show video controller on hover
		if (newValue)
		{
			//Check to make sure the video controller is not already being displayed before trying to add it again
			if (!pane.getChildren().contains(videoController.getPane()))
			{
				pane.getChildren().addAll(videoController.getPane());
			}
		}
		//Hide controller when mouse leaves window
		else
		{
			pane.getChildren().remove(videoController.getPane());
		}
	}
	
	//This method stops the currently playing media
	public void close()
	{
		mediaPlayer.stop();
	}
	
	//Getters
	public StackPane getPane() { return pane; }
}
