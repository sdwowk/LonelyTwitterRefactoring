package ca.ualberta.cs.lonelytwitter;

import java.util.List;

import lonelyTweet.ImportantLonelyTweet;
import lonelyTweet.LonelyTweet;
import lonelyTweet.NormalLonelyTweet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class LonelyTwitterActivity extends Activity {

	private static final String STAR = "*";
	private EditText bodyText;
	private ListView oldTweetsList;

	private List<LonelyTweet> tweets;
	private ArrayAdapter<LonelyTweet> adapter;
	private TweetsFileManager tweetsProvider;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		bodyText = (EditText) findViewById(R.id.body);
		oldTweetsList = (ListView) findViewById(R.id.oldTweetsList);
	}

	@Override
	protected void onStart() {
		super.onStart();

		tweetsProvider = new TweetsFileManager(this);
		setTweets(tweetsProvider.loadTweets());
		adapter = new ArrayAdapter<LonelyTweet>(this, R.layout.list_item,
				tweets);
		oldTweetsList.setAdapter(adapter);
	}

	public void save(View v) {
		LonelyTweet tweet = determineTweet();
		
		if (tweet.isValid()) {
			getTweets().add(tweet);
			adapter.notifyDataSetChanged();

			bodyText.setText("");
			tweetsProvider.saveTweets(tweets);
		} else {
			Toast.makeText(this, "Invalid tweet", Toast.LENGTH_SHORT).show();
		}
	}

	private LonelyTweet determineTweet() {
		String text = bodyText.getText().toString();

		LonelyTweet tweet;

		if (text.contains(STAR)) {
			tweet = new ImportantLonelyTweet(text);
		} else {
			tweet = new NormalLonelyTweet(text);
		}
		return tweet;
	}

	public void clear(View v) {
		tweets.clear();
		adapter.notifyDataSetChanged();
		tweetsProvider.saveTweets(tweets);
	}

	public List<LonelyTweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<LonelyTweet> tweets) {
		this.tweets = tweets;
	}

}