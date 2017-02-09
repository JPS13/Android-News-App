package udacity.android.newsapp.model;

/**
 * This class represents a news article received from the Guardian news API.
 *
 * @author Joseph Stewart
 * @version 1.0
 */
public class NewsArticle {

    // NewsArticle attributes
    private String title;
    private String date;
    private String section;
    private String url;

    /** Constructors */

    public NewsArticle() {

    }

    public NewsArticle(String title, String section) {
        this.title = title;
        this.section = section;
    }

    public NewsArticle(String title, String date, String section, String url) {
        this.title = title;
        this.date = date;
        this.section = section;
        this.url = url;
    }

    /**
     * Returns the title of the article.
     *
     * @return The title of the article.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title for the article.
     *
     * @param title The title of the article.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the date the article was written.
     *
     * @return The date of the article.
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date the article was written.
     *
     * @param date The date of the article.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns the section for the article.
     *
     * @return The article for the section.
     */
    public String getSection() {
        return section;
    }

    /**
     * Sets the section for this article.
     *
     * @param section The section for the article.
     */
    public void setSection(String section) {
        this.section = section;
    }

    /**
     * Returns the url for this article.
     *
     * @return The url for the article.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url of this article.
     *
     * @param url The url for the article.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * A string representation of this NewsArticle object.
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return "Title: " + title + ", Date: " + date + ", Section: " + section;
    }
}
