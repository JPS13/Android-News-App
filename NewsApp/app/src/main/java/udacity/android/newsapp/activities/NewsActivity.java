package udacity.android.newsapp.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import udacity.android.newsapp.R;
import udacity.android.newsapp.model.NewsArticle;
import udacity.android.newsapp.utility.ArticleAdapter;
import udacity.android.newsapp.utility.NewsLoader;

/**
 * This is the NewsActivity which recieves
 *
 * @author Joseph Stewart
 * @version 2.0
 */
public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsArticle>>  {

    private static final String URL = "http://content.guardianapis.com/search?";
    private static final int ARTICLE_LOADER_ID = 1;

    // Search text entered by user, belongs to class so value persists
    private static String searchText;

    private ArticleAdapter adapter;
    private TextView emptyStateTextView;
    private ListView articleListView;

    /**
     * This method is called when the activity is created.
     *
     * @param savedInstanceState The saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        searchText = getIntent().getExtras().getString("Query Text");

        articleListView = (ListView) findViewById(R.id.list);

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            emptyStateTextView = (TextView) findViewById(R.id.empty_view);
            articleListView.setEmptyView(emptyStateTextView);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    /**
     * This method is called to create the loader.
     *
     * @param id The id.
     * @param args The Bundle.
     * @return The created loader.
     */
    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter(getString(R.string.query_key), searchText);
        uriBuilder.appendQueryParameter(getString(R.string.format_label), getString(R.string.format_value));
        uriBuilder.appendQueryParameter(getString(R.string.page_size_key), "30");
        uriBuilder.appendQueryParameter(getString(R.string.settings_order_by_key), orderBy);
        uriBuilder.appendQueryParameter(getString(R.string.api_key), getString(R.string.api_value));

        // Create a new loader for the given url
        return new NewsLoader(this, uriBuilder.toString());
    }

    /**
     * This method is called when the loading is completed. It removes the progress bar
     * and calls the displayArticles method.
     *
     * @param loader The loader.
     * @param articles The list of articles.
     */
    @Override
    public void onLoadFinished(Loader<List<NewsArticle>> loader, List<NewsArticle> articles) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);
        displayArticles(articles);
    }

    /**
     * This method is called when the loader is  reset.
     *
     * @param loader The loader.
     */
    @Override
    public void onLoaderReset(Loader<List<NewsArticle>> loader) {
        adapter.clear();
    }

    /**
     * This method displays the list of NewsArticles.
     *
     * @param articles The list of NewsArticles.
     */
    private void displayArticles(List<NewsArticle> articles) {
        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        articleListView.setEmptyView(emptyStateTextView);

        if (articles != null && !articles.isEmpty()) {
            // Set the adapter with the data list
            adapter = new ArticleAdapter(this, articles);
            articleListView.setAdapter(adapter);
        }

        // Set empty state text
        emptyStateTextView.setText(R.string.no_articles);
    }

    /**
     * This method is called when the options menu is created.
     *
     * @param menu The menu.
     * @return true if menu is inflated without throwing an exception.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * This method is called when an item is selected in the options menu.
     *
     * @param item The selected item.
     * @return true if settings intent started properly.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
