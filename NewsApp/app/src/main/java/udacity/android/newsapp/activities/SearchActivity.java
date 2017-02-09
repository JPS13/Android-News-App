package udacity.android.newsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import udacity.android.newsapp.R;

/**
 * This is the activity for the search function. It displays an EditText
 * that allows the user to enter a search topic and a button that gets
 * the entered text and uses it to build a query url for the Google Books
 * API.
 *
 * @author Joseph Stewart
 * @version 1.1
 */
public class SearchActivity extends AppCompatActivity {

    private EditText searchEditText;

    /**
     * Called when the Activity is created. This method gets the
     * entered text and passed the built url to the BookActivity.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = (EditText) findViewById(R.id.search_text);
        Button searchButton = (Button) findViewById(R.id.search_button);

        // Set the action listener for pressing enter
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    startQuery();
                    return true;
                } else {
                    return false;
                }
            }
        });

        // Set the action listener for clicking the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuery();
            }
        });
    }

    /**
     * This method gets the text entered by the user and sends it
     * to the NewsActivity so it can be used in the url query.
     */
    private void startQuery() {
        // Get text entered by user
        String queryText = searchEditText.getText().toString().trim();

        // Go to the BookActivity to display the results
        Intent intent = new Intent(SearchActivity.this, NewsActivity.class);
        intent.putExtra("Query Text", queryText);
        startActivity(intent);
    }

}
