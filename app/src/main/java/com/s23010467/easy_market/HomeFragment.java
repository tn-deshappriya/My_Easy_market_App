package com.s23010467.easy_market;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView announcementBanner = view.findViewById(R.id.announsment);
        announcementBanner.setSelected(true);

        String[] announcements = {
                "Hello",
                "Hi",
                "I'm thinira Nilushan Deshappriya.\n I'm In Sooriyawewa.\n I'm Open University Student.\n I'm Software Engineering Student."
        };
        final int[] index = {0};

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (announcements.length==0){
                    announcementBanner.setText("This Is Our Announcement Section\nAnd\nThis Time Not Have\nAnnouncements.");
                }else {
                    announcementBanner.setText(announcements[index[0]]);
                    index[0] = (index[0] + 1) % announcements.length;
                    handler.postDelayed(this, 4000); // Change every 4 seconds
                }
            }
        };
        handler.post(runnable);

        ImageView home_profile = view.findViewById(R.id.home_prof_icon);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseRef = FirebaseDatabase
                .getInstance("https://my-easy-market-c4753-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users");

        if (currentUser != null) {
            databaseRef.child(currentUser.getUid()).child("profileImageUrl")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            String imageUrl = snapshot.getValue(String.class);
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                // Use Picasso
                                Picasso.get().load(imageUrl).into(home_profile);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(requireContext(), "Database Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

//        Set nevigation to profile Activity...

        CardView db_profile = view.findViewById(R.id.db_profile);

        db_profile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), profile.class);
            startActivity(intent);
        });
        return view;

    }
}