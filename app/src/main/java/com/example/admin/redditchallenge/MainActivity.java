package com.example.admin.redditchallenge;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;

import com.example.admin.redditchallenge.model.Child;
import com.example.admin.redditchallenge.model.RedditResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RedditRecyclerViewAdapter.OnListInteractionListener {
    private static final String TAG = "MainTag";
    private RedditRecyclerViewAdapter.OnListInteractionListener mListener;
    private List<Child> redditResponses = new ArrayList<>();
    private SearchView svSearch;

    private Observer<Response<RedditResponse>> redditObserver = new Observer<Response<RedditResponse>>() {
        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "onSubscribe: ");
        }

        @Override
        public void onNext(Response<RedditResponse> redditResponseResponse) {
            Log.d(TAG, "onNext: " + redditResponseResponse.message());
            if(redditResponseResponse.body() != null) {
                List<Child> children = redditResponseResponse.body().getData().getChildren();
                redditRecyclerViewAdapter.updateList(children);
            }

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };
    private RecyclerView recyclerView;
    private RedditRecyclerViewAdapter redditRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: ");

        recyclerView = findViewById(R.id.rvReddit);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        redditRecyclerViewAdapter = new RedditRecyclerViewAdapter(redditResponses, this);
        recyclerView.setAdapter(redditRecyclerViewAdapter);

        svSearch = findViewById(R.id.svSearch);
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Observable<Response<RedditResponse>> responseObservable = RetrofitHelper.createSearch(query);
                responseObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(redditObserver);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Observable<Response<RedditResponse>> responseObservable = RetrofitHelper.createSearch("funny");
        responseObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(redditObserver);


    }

    @Override
    public void onListInteraction(Child item) {
        Log.d(TAG, "onListInteraction: ");
        Uri uri = Uri.parse("smsto:4802681516");
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", item.getData().getAuthor() + "\n" + item.getData().getTitle());
        startActivity(it);
    }





}
