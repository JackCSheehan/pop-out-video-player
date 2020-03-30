package main;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

//This class has all the code for the YouTube video player UI
public class YouTubePlayerUI 
{
	public static final String ERROR_TEXT = "Could not load YouTube video.\n"
												  + "Please try another link";		//The text show when the video cannot be loaded
	public static final String LOADING_TEXT = "Loading YouTube video...";			//Text to show while video is being loaded
	public static final String EMBED_LINK_TEMPLATE = "https://youtube.com/embed/";	//Template for embedding a YouTube video
	public static final String VIDEO_QUERY = "v=";									//String in URL that indicates video search ID
	public static final Color FILL_COLOR = UI.FILL_COLOR;							//Fill color of background around web view
	public static final Color TEXT_COLOR = Color.WHITE;								//Fill color of text
	
	private StackPane pane;			//The pane where all of the YouTube player UI nodes will be added to
	private WebView webView;		//WebView where video will be displayed
	private String youTubeLink;		//The YouTube link to the video to be played
	private Text loadingText;		//Text to show while video is loading
	private Text errorText;			//Text to show if video can't be loaded
	
	YouTubePlayerUI(String ytl)
	{
		pane = new StackPane();
		pane.setBackground(new Background(new BackgroundFill(FILL_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		
		//Assign members
		youTubeLink = ytl;
		
		loadingText = new Text(LOADING_TEXT);
		loadingText.setFill(TEXT_COLOR);
		//While video is being fetched, show loading text
		pane.getChildren().add(loadingText);
		
		errorText = new Text(ERROR_TEXT);
		errorText.setFill(TEXT_COLOR);
		
		try
		{	
			//Get the embed link from the passed YouTube link
			String embedLink = getYouTubeEmbedLink(youTubeLink);
			
			//Create the web view to load and embed the YouTube video
			webView = new WebView();
			webView.getEngine().load(embedLink);
			
			//Remove loading text and add web view to pane
			pane.getChildren().add(webView);
		}
		catch (Exception e)
		{
			//If video can't be loaded, show error text
			Text errorText = new Text(ERROR_TEXT);
			errorText.setFill(TEXT_COLOR);
			pane.getChildren().add(errorText);
		}	
		
		//Remove loading text once either error text has been shown or video was loaded
		pane.getChildren().remove(loadingText);
	}
	
	private String getYouTubeEmbedLink(String youTubeLink) throws Exception
	{
		String embedLink = "";	//The embed link that will be extrapolated from the passed YouTube video link
		String videoID;			//The ID of the passed YouTube video
		URI uri;				//URL for the passed YouTube video link

		//Get the ID of the passed YouTube video
		youTubeLink = youTubeLink.trim();
		uri = new URI(youTubeLink);
		videoID = uri.toURL().getQuery().replace(VIDEO_QUERY, "");
			
		//Use the embed template to create the YouTube embed link
		embedLink = EMBED_LINK_TEMPLATE + videoID;

		return embedLink;
	}
	
	//Getters
	public StackPane getPane() { return pane; }
}
