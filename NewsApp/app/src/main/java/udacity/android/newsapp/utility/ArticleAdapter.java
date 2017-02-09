package udacity.android.newsapp.utility;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import udacity.android.newsapp.R;
import udacity.android.newsapp.model.NewsArticle;

/**
 * This class extends the ArrayAdapter class and allows customization
 * to accommodate a NewsArticle object being represented in a list view.
 *
 * @author Joseph Stewart
 * @version 1.4
 */
public class ArticleAdapter extends ArrayAdapter<NewsArticle> {

    /**
     * This class allows for the ViewHolder pattern
     * to be implemented for efficiency. It holds
     * references to views for reuse.
     */
    private static class ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView sectionTextView;
    }

    /** Constructor */
    public ArticleAdapter(Activity context, List<NewsArticle> articles) {
        super(context, 0, articles);
    }

    /**
     * This method sets up the view with NewsArticle data.
     *
     * @param position The current position in the list of NewsArticles.
     * @param convertView The view currently being set up.
     * @param parent The parent view group.
     * @return The set up view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Create new NewsArticle object
        final NewsArticle currentArticle = getItem(position);

        ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);

            holder = new ViewHolder();
            holder.titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.date_text_view);
            holder.sectionTextView = (TextView) convertView.findViewById(R.id.section_text_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // TextView for the title of the article
        holder.titleTextView.setText(currentArticle.getTitle());

        // TextView for the section of the article
        holder.sectionTextView.setText(currentArticle.getSection());

        // TextView for the modified date of the article
        String date = currentArticle.getDate().substring(0, 10); // Removes time and zone data
        holder.dateTextView.setText(date);

        // Navigate to the website of the article when clicked
        if(currentArticle.getUrl() != null) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(currentArticle.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    getContext().startActivity(intent);
                }
            });
        }

        return convertView;
    }
}
