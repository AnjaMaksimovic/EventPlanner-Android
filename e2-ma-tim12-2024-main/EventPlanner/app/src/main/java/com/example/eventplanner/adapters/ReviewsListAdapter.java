package com.example.eventplanner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.R;
import com.example.eventplanner.model.reviews.Review;
import com.example.eventplanner.model.reviews.ReviewReport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReviewsListAdapter extends ArrayAdapter<Review> {

    private ArrayList<Review> aReviews;
    private OnReviewClickListener mListener;
    public interface  OnReviewClickListener{
        void OnReportClick(Review review);
    }

    public ReviewsListAdapter(@NonNull Context context, @NonNull ArrayList<Review> reviews, OnReviewClickListener listener) {
        super(context, R.layout.review_card, reviews);
        aReviews = reviews;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return aReviews.size();
    }
    @Nullable
    @Override
    public Review getItem(int position) {
        return aReviews.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Review review = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_card,
                    parent, false);
        }
        TextView date = convertView.findViewById(R.id.date_card);
        TextView comment = convertView.findViewById(R.id.comment_card);
        TextView grade = convertView.findViewById(R.id.grade_card);
        Button reportBtn = convertView.findViewById(R.id.review_card_report_btn);

        if(review != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            date.setText(dateFormat.format(review.getDate()));
            comment.setText(review.getComment());
            grade.setText(String.valueOf(review.getGrade()));

           reportBtn.setOnClickListener(v -> {
               Log.i("ReviewListAdapter", "Review clicked");
               if(mListener != null){
                   mListener.OnReportClick(review);
               }
           });
        }
        return convertView;
    }
}