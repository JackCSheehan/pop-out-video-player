package main;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

//This class has all the code necessary to show the video controller pane that has video control nodes
public class VideoController 
{
	public static final Color FILL_COLOR = OptionsBar.FILL_COLOR;	//Fill color of pane
	public static final double ICON_SIZE = OptionsBar.ICON_SIZE;	//Size of icons	
	public static final double BUTTON_SPACING = 15.0;				//Spacing between buttons
	public static final double DEFAULT_VOLUME_VALUE = 100.0;		//Default setting of volume slider
	
	private static final double SPEED_SLIDER_MIN = 1;				//Minimum value for speed slider
	private static final double SPEED_SLIDER_MAX = 2;				//Maximum value fro speed slider
	
	private static final double SPEED_DIVISOR = 12.5;				//Conversion divisor for the speed slider
	
	private BorderPane pane;				//Main pane
	private HBox controlsPane;				//Pane to hold all video controls except for the video scrubber
	
	private Image playIcon;					//Icon for play button
	private Image pauseIcon;				//Icon for pause button
	private Image rewindIcon;				//Icon for rewind button
	private Image volumeIcon;				//Icon for volume slider
	private Image speedIcon;				//Icon for speed slider
	
	private ImageView playIconImageView;	//ImageView for the play icon
	private ImageView pauseIconImageView;	//ImageView for the pause icon
	private ImageView rewindIconImageView;	//ImageView for the rewind icon
	private ImageView volumeIconImageView;	//ImageView for volume icon
	private ImageView speedIconImageView;	//ImageView for speed icon
	private ImageView videoStateImageView;	//Will hold either play or pause button based on if the video is playing paused
	private StackPane videoStateIconHitbox;	//Hitbox for pause/play button
	
	private VideoScrubber videoScrubber;	//Control for scrubbing the video
	
	private boolean isVideoPlaying;			//Keeps track of whether or not the video is playing
	
	private Slider volumeSlider;			//Slider for adjusting volume
	private Slider speedSlider;				//Slider for adjusting speed
	
	private MediaPlayer mediaPlayer;		//Media player playing video
	//private PlaybackParams playbackParams;	//Playback params for adjusting pitch
	
	VideoController(MediaPlayer mp)
	{	
		
		isVideoPlaying = true;	//Since video auto plays, this flag should start as true
		mediaPlayer = mp;
		videoStateIconHitbox = new StackPane();
		
		//Assign images to icons
		playIcon = new Image("play.png");
		pauseIcon = new Image("pause.png");
		rewindIcon = new Image("rewind.png");
		volumeIcon = new Image("speaker.png");
		speedIcon = new Image("rabbit.png");
		
		videoStateImageView = new ImageView(pauseIcon);
		videoStateImageView.setFitHeight(ICON_SIZE);
		videoStateImageView.setFitWidth(ICON_SIZE);
		
		//Add video state icon to its hitbox and configure click
		videoStateIconHitbox.getChildren().add(videoStateImageView);
		videoStateIconHitbox.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent mouseEvent) 
			{
				updateVideoState();		
			}
		});
		
		//Associate ImageViews with images
		playIconImageView = new ImageView(playIcon);
		playIconImageView.setFitHeight(ICON_SIZE);
		playIconImageView.setFitWidth(ICON_SIZE);

		pauseIconImageView = new ImageView(pauseIcon);
		pauseIconImageView.setFitHeight(ICON_SIZE);
		pauseIconImageView.setFitWidth(ICON_SIZE);
		
		rewindIconImageView = new ImageView(rewindIcon);
		rewindIconImageView.setFitHeight(ICON_SIZE);
		rewindIconImageView.setFitWidth(ICON_SIZE);
		rewindIconImageView.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent mouseEvent)
			{
				mediaPlayer.seek(new Duration(0));
			}
		});
		
		volumeIconImageView = new ImageView(volumeIcon);
		volumeIconImageView.setFitHeight(ICON_SIZE);
		volumeIconImageView.setFitWidth(ICON_SIZE);
		
		speedIconImageView = new ImageView(speedIcon);
		speedIconImageView.setFitHeight(ICON_SIZE);
		speedIconImageView.setFitWidth(ICON_SIZE);
		
		volumeSlider = new Slider();
		volumeSlider.adjustValue(DEFAULT_VOLUME_VALUE);
		
		//Set dragged value to adjust volume of video
		volumeSlider.setOnMouseDragged(new EventHandler<MouseEvent>() 
		{
			public void handle(MouseEvent mouseEvent)
			{
				mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
			}
		});
		
		speedSlider = new Slider();
		speedSlider.setShowTickLabels(true);
		speedSlider.setMin(SPEED_SLIDER_MIN);
		speedSlider.setMax(SPEED_SLIDER_MAX);
		speedSlider.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent mouseEvent)
			{
				mediaPlayer.setRate(speedSlider.getValue());
			}
		});
		
		//Instantiate controls pane and add controls to it
		controlsPane = new HBox();
		controlsPane.setSpacing(BUTTON_SPACING);
		controlsPane.getChildren().addAll(videoStateIconHitbox, rewindIconImageView, volumeIconImageView, volumeSlider, speedIconImageView, speedSlider);
		
		//Create the video scrubber
		videoScrubber = new VideoScrubber(mediaPlayer);
		
		//Instantiate border pane and establish settings
		pane = new BorderPane();
		pane.setBackground(new Background(new BackgroundFill(OptionsBar.FILL_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		pane.setMaxHeight(OptionsBar.ICON_SIZE);
		
		//Add nodes to pane
		pane.setTop(videoScrubber.getNode());
		pane.setBottom(controlsPane);
	}
	
	//This function plays or pauses video based on state of current video state
	private void updateVideoState()
	{
		if (isVideoPlaying)
		{
			mediaPlayer.pause();
			videoStateImageView.setImage(playIcon);
			
			isVideoPlaying = false;
		}
		else
		{
			mediaPlayer.play();
			videoStateImageView.setImage(pauseIcon);
			
			isVideoPlaying = true;
		}
	}
	
	//Getters
	BorderPane getPane() { return pane; }
}
