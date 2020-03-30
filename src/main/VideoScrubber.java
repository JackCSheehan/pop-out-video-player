package main;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

//This custom node allows users to scrub through their video from file
public class VideoScrubber 
{
	public static final Color BACKGROUND_COLOR = OptionsBar.FILL_COLOR;	//Background color of scrubber
	public static final String THUMB_COLOR = "white";					//Thumb color for CSS style  setting
	
	private boolean shouldUpdateScrubber;	//Keeps track of whether or not scrubber should update	
	private Slider scrubber;				//Controller for video scrubbing
	
	VideoScrubber(MediaPlayer mediaPlayer)
	{
		//By default, the scrubber should update until the user tries to scrub
		shouldUpdateScrubber = true;

		scrubber = new Slider();
		scrubber.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
				
		//Adds handler to disable scrubber updating on mouse down; this allows the user to drag the scrubber
		scrubber.setOnMousePressed(new EventHandler<MouseEvent>() 
		{
			public void handle(MouseEvent mouseEvent)
			{
				shouldUpdateScrubber = false;
			}
		});
				
		//Adds handler to enable scrubber to actually scrub through video as user is moving the thumb of the slider
		scrubber.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
			double millisToSeekTo;	//The place in the video to seek to while the scrubber is being moved
					
			public void handle(MouseEvent mouseEvent)
			{
				//Calculate the fraction of the video by dividing the total duration by the value of the scrubber / 100
				millisToSeekTo = mediaPlayer.getTotalDuration().toMillis() * (scrubber.getValue() / 100.0);
				mediaPlayer.seek(new Duration(millisToSeekTo));
			}
		});
				
		//Adds handler to enable scrubber updating once mouse is released; allows scrubber to continue moving along as video plays
		scrubber.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent mouseEvent)
			{
				shouldUpdateScrubber = true;
			}
		});
				
		//Create a timer that will update the progress of the video timer
		AnimationTimer scrubberTimer = new AnimationTimer()
		{
			@Override
			public void handle(long now)
			{
				//If the scrubber should be updated, update it
				if (shouldUpdateScrubber)
				{
					//Set the duration of the scrubber to the percentage of the video that has already played
					scrubber.adjustValue(mediaPlayer.getCurrentTime().toMillis() / mediaPlayer.getTotalDuration().toMillis() * 100);
				}
			}
		};
		scrubberTimer.start();
	}
	
	//Getters
	boolean getShouldUpdateScrubber() { return shouldUpdateScrubber; }
	Slider getNode() { return scrubber; }
	
	//Setters
	void setShouldUpdatesScrubber(boolean s) { shouldUpdateScrubber = s; }
}
