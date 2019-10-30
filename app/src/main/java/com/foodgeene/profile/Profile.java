package com.foodgeene.profile;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodgeene.R;
import updateprofile.UpdateProfile;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

    TextView editProfile;


    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        editProfile = rootView.findViewById(R.id.editProfile);
        editProfile.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), UpdateProfile.class));
        });
        return rootView;


    }

}
