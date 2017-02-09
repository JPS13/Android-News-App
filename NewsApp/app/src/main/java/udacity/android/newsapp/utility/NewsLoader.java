package udacity.android.newsapp.utility;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;
import udacity.android.newsapp.model.NewsArticle;

/**
 * This class provides for the network request over the internet
 * being performed on a background thread to avoid performance
 * issues. It receives a string query url which is passed to
 * the QueryUtility class for action.
 *
 * @author Joseph Stewart
 * @version 1.2
 *
 */
public class NewsLoader extends AsyncTaskLoader<List<NewsArticle>> {

    // The query url
    private String url;

    /**
     * Constructor
     *
     * @param context The context of the Activity calling constructor.
     * @param url The query url.
     */
    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This method calls the QueryUtility.extractArticles method
     * on a background thread.
     *
     * @return The populated list of NewsArticles.
     */
    @Override
    public List<NewsArticle> loadInBackground() {
        if(url == null) {
            return null;
        }
        return QueryUtility.extractArticles(getContext(), url);
    }

}
