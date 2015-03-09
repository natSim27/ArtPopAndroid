package com.supercourse.artpop.artpopandroid.tools;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.supercourse.artpop.artpopandroid.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class CommentFragment extends Fragment {


    public CommentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }


}
