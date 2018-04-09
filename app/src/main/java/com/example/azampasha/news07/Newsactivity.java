package com.example.azampasha.news07;

;
import android.app.LoaderManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class Newsactivity extends AppCompatActivity  {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mtoggle;

    private NewsAdapter mAdapter;
    TextView mEmptyStateTextView;
    // Start the AsyncTask to fetch the earthquake data
    NewsAsyncTask task = new NewsAsyncTask();

    /**
     * URL for news data from the newsapi.org dataset
     */
    private static final String NEWS_REQUEST_URL =
            "https://newsapi.org/v2/top-headlines?sources=google-news&apiKey=00cb12f45995443da4d3e34c5703f210";
    private static final String CNN_REQUEST_URL =
            "https://newsapi.org/v2/top-headlines?sources=cnn&apiKey=00cb12f45995443da4d3e34c5703f210";
    private static final String ESPN_REQUEST_URL =
            "https://newsapi.org/v2/top-headlines?sources=espn&apiKey=00cb12f45995443da4d3e34c5703f210";
    private static final String MTV_REQUEST_URL =
            "https://newsapi.org/v2/top-headlines?sources= mtv-news&apiKey=00cb12f45995443da4d3e34c5703f210";
    private static final String NYTIMES_REQUEST_URL=
            "https://newsapi.org/v2/top-headlines?sources=new-york-magazine&apiKey=00cb12f45995443da4d3e34c5703f210";



    private static final int NEWS_LOADER_ID = 1;

    private static final String LOG_TAG = Newsactivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsactivity);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mtoggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);
        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

       /* TextView mEmptyStateTextView = (TextView) findViewById(R.id.empty_view); */
        newsListView.setEmptyView(mEmptyStateTextView);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                News currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.geturl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });


        task.execute(NEWS_REQUEST_URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.activity_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (mtoggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (id){

            case R.id.news_cnn:
                NewsAsyncTask task1 = new NewsAsyncTask();
                task1.execute(CNN_REQUEST_URL);
                return  true;


            case R.id.news_espn:
                NewsAsyncTask task2 = new NewsAsyncTask();
                task2.execute(ESPN_REQUEST_URL);
                break;
            case R.id.news_google:
                NewsAsyncTask task3 = new NewsAsyncTask();
                task3.execute(NEWS_REQUEST_URL);
                break;
            case R.id.news_mtv:
                NewsAsyncTask task4 = new NewsAsyncTask();
                task4.execute(MTV_REQUEST_URL);
                break;
            case R.id.news_newYorkTimes:
                NewsAsyncTask task5 = new NewsAsyncTask();
                task5.execute(NYTIMES_REQUEST_URL);
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the list of earthquakes in the response.
     * <p>
     * AsyncTask has three generic parameters: the input type, a type used for progress updates, and
     * an output type. Our task will take a String URL, and return an Earthquake. We won't do
     * progress updates, so the second generic is just Void.
     * <p>
     * We'll only override two of the methods of AsyncTask: doInBackground() and onPostExecute().
     * The doInBackground() method runs on a background thread, so it can run long-running code
     * (like network activity), without interfering with the responsiveness of the app.
     * Then onPostExecute() is passed the result of doInBackground() method, but runs on the
     * UI thread, so it can use the produced data to update the UI.
     */
    private class NewsAsyncTask extends AsyncTask<String, Void, List<News>> {

        /**
         * This method runs on a background thread and performs the network request.
         * We should not update the UI from a background thread, so we return a list of
         * {@link News}s as the result.
         */

        @Override
        protected List<News> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<News> result = QueryUtils.fetchNewsData(urls[0]);

            return result;
        }

        /**
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of earthquake data from a previous
         * query to USGS. Then we update the adapter with the new list of earthquakes,
         * which will trigger the ListView to re-populate its list items.
         */
        @Override
        protected void onPostExecute(List<News> data) {
            // Clear the adapter of previous earthquake data
            mAdapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
            final ProgressBar progressBar = (ProgressBar)findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.GONE);
        }


    }
}
