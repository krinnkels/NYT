package com.example.nyt.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nyt.ArticleAdapter;
import com.example.nyt.BookAdapter;
import com.example.nyt.FakeAPI;
import com.example.nyt.FakeDatabase;
import com.example.nyt.R;
import com.example.nyt.model.BestsellerList;
import com.example.nyt.model.BestsellerListResponse;
import com.example.nyt.model.Book;
import com.example.nyt.model.TopStoriesResponse;
import com.google.gson.Gson;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookRecyclerFragment extends Fragment {
    private RecyclerView recyclerView;


    public BookRecyclerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_book_recycler, container, false);
        recyclerView = view.findViewById(R.id.rv_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

       final BookAdapter bookAdapter = new BookAdapter();


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url ="https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json?api-key=3kSMxH6fAKnCD623wl9RY6UrtkjCjIFS";
        // Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Display the first 500 characters of the response string.
                        //WHen you get a successful response, data will be stored here. JSON is here.
                        //Pass the response you have received
                        //Set the adapter
                        Gson gson = new Gson();

                        //String jsonString = FakeAPI.getCurrentHardcoverFictionBestsellers();
                        BestsellerListResponse bestsellerListResponse = gson.fromJson(response, BestsellerListResponse.class);
                        BestsellerList listFromResponse = bestsellerListResponse.getResults();
                        ArrayList<Book> booksFromJson = listFromResponse.getBooks();
                        bookAdapter.setData(booksFromJson);
                        // We have reworked FakeDatabase to act as a place to store these Books, such that we
                        // can access them via their isbn. This will allow our intents to the DetailView to keep
                        // functioning.
                        FakeDatabase.saveBooksToFakeDatabase(booksFromJson);
                        recyclerView.setAdapter(bookAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error not receiving");

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return view;
    }

}
