package udacity.android.newsapp.utility;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import udacity.android.newsapp.R;
import udacity.android.newsapp.model.NewsArticle;

/**
 * This class provides utility methods to make network requests over the internet
 * to the Guardian API based on the passed in query url string. The query results
 * are added to a list and returned.
 *
 * @author Joseph Stewart
 * @version 2.2
 */
public final class QueryUtility {

    private static final String LOG_TAG = QueryUtility.class.getSimpleName();

    /**
     * Private constructor that throws AssertionError to prevent instantiation.
     */
    private QueryUtility() {
        throw new AssertionError("The QueryUtility cannot be instantiated.");
    }

    /**
     * This method extracts a List of NewsArticles from the passed in query URL.
     *
     * @param context The requesting context to provide access to string resources.
     * @param urlString the query URL.
     * @return The populated List.
     */
    public static List<NewsArticle> extractArticles(Context context, String urlString) {

        // Create a list to hold NewsArticles
        List<NewsArticle> articles = new ArrayList<>();

        try {
            // Create URL object
            URL urlQuery = createUrl(urlString);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = null;
            try {
                jsonResponse = makeHttpRequest(urlQuery);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error closing input stream", e);
            }

            // Create a JSONObject from the received string response
            JSONObject response = new JSONObject(jsonResponse);

            // Extract the data from the response
            JSONObject responseObject = response.getJSONObject(context.getString(R.string.response));
            JSONArray resultsArray = responseObject.getJSONArray(context.getString(R.string.results));

            // Traverse the results of the JSON array
            for(int i = 0; i < resultsArray.length(); i++) {
                // Get the properties object from the earthquake object
                JSONObject articleObject = resultsArray.getJSONObject(i);

                // The desired article attributes
                String title = null;
                String date = null;
                String section = null;
                String url = null;

                // Ensure each attribute exists to avooid errors
                if(articleObject.has(context.getString(R.string.web_title))) {
                    title = articleObject.getString(context.getString(R.string.web_title));
                }

                if(articleObject.has(context.getString(R.string.date))) {
                    date = articleObject.getString(context.getString(R.string.date));
                }

                if(articleObject.has(context.getString(R.string.section))) {
                    section = articleObject.getString(context.getString(R.string.section));
                }

                if(articleObject.has(context.getString(R.string.url))) {
                    url = articleObject.getString(context.getString(R.string.url));
                }

                // Construct and add a new NewsArticle object from the data
                NewsArticle article = new NewsArticle(title, date, section, url);
                articles.add(article);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }

        // Return the list of articles
        return articles;
    }

    /**
     * This method takes in a string and converts it into a URL object.
     *
     * @param stringUrl The string to be converted.
     * @return The constructed URL object.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL ", e);
        }
        return url;
    }

    /**
     * This method makes an http request using the passed in url.
     *
     * @param url The url to which the request is made.
     * @return The JSON response as a string.
     * @throws IOException Thrown if there is an issue with the request.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * This method reads and inputStream and converts it into a string.
     *
     * @param inputStream The inputStream to be read.
     * @return Returns the string representation of the inputStream.
     * @throws IOException Thrown if there is a problem reading from the input stream.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
