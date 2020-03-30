package main;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

//Custom video selection dialog that comes up on screen to let users pick if they want to get video from internet or from PC
public class VideoSelectionDialog 
{
	public static final Color FILL_COLOR = OptionsBar.FILL_COLOR;				//Fill color of pane
	public static final Color TEXT_COLOR = Color.WHITE;							//Color of text options
	public static final double TEXT_SIZE = 20;									//Size of text options
	
	public static final String FROM_LOCAL_FILE_TEXT = "From Local File";		//Text of from local file button
	public static final String FROM_YOUTUBE_TEXT = "From YouTube";				//Text of from YouTube button
	
	private VBox pane;						//Pane where all options will be put into
	
	private Text fromLocalFileOption;		//Option to let user choose a video from file
	private Text fromYouTubeOption;			//Option that lets user enter a YouTube video URL
	
	private FileChooser fileChooser;		//The file selection dialog used to select a video file to play

	VideoSelectionDialog(Stage primaryStage, StackPane root, UI ui)
	{	
		//Create pane and set settings
		pane = new VBox();
		pane.setBackground(new Background(new BackgroundFill(FILL_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		pane.setAlignment(Pos.CENTER);

		//Create the file chooser dialog and set its settings
		fileChooser = new FileChooser();
		fileChooser.setTitle("Open Video File");
		
		//Add extension filter to file chooser
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Video Viles", "*.mp4", "*.avc"));
		
		//Create from file option text node
		fromLocalFileOption = new Text(FROM_LOCAL_FILE_TEXT);
		fromLocalFileOption.setFill(TEXT_COLOR);
		fromLocalFileOption.setFont(Font.font(TEXT_SIZE));
		
		//Set the on-clicked functionality of the from file option button
		fromLocalFileOption.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent mouseEvent)
			{
				//When the button is pressed, remove the video selection dialog from the root pane
				root.getChildren().remove(pane);
				
				try
				{
					//Launch the file selection dialog
					File selectedVideo = fileChooser.showOpenDialog(primaryStage);
					
					//Reload the UI with a new media source
					ui.reloadUI(selectedVideo.toURI().toString());
				}
				catch (Exception e)
				{
					//...error is handled in Video Player UI class
				}
			}
		});
		
		fromYouTubeOption = new Text(FROM_YOUTUBE_TEXT);
		fromYouTubeOption.setFill(TEXT_COLOR);
		fromYouTubeOption.setFont(Font.font(TEXT_SIZE));
		fromYouTubeOption.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent mouseEvent)
			{
				TextInputDialog textInputDialog = new TextInputDialog();
				textInputDialog.setHeaderText("Enter a YouTube video URL");
				String youTubeURL;	//URL gotten from user
				
				try
				{
					youTubeURL = textInputDialog.showAndWait().get();
					ui.reloadUI(youTubeURL);
				}
				catch (Exception e)
				{
					//...error text is handled in YouTube Player class
				}
			}
		});
		
		pane.getChildren().addAll(fromLocalFileOption, fromYouTubeOption);
	}
	
	//Getters
	VBox getPane() { return pane; }
}
