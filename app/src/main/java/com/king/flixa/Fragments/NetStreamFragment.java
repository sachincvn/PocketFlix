package com.king.flixa.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.king.flixa.ExpPlayer.PlayerActivity;
import com.king.flixa.R;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NetStreamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NetStreamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private String mParam1;
    private String mParam2;


    private TextInputEditText mediaUrl;
    private TextInputEditText mediaDrm;
    private TextInputEditText mediaAgent;
    private TextInputEditText referer;
    private TextInputEditText cookie;
    private TextInputEditText origin;

    public NetStreamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NetStreamFragment newInstance(String param1, String param2) {
        NetStreamFragment fragment = new NetStreamFragment();
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
        View root = inflater.inflate(R.layout.fragment_netstream, container, false);
        CircleImageView mediaPlay = root.findViewById(R.id.mediaPlay);
        mediaUrl = root.findViewById(R.id.mediaUrl);
        mediaDrm = root.findViewById(R.id.mediaDrm);
        mediaAgent = root.findViewById(R.id.mediaAgent);
        referer = root.findViewById(R.id.referer);
        cookie = root.findViewById(R.id.cookie);
        origin = root.findViewById(R.id.origin);

        mediaPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoUrl = mediaUrl.getText().toString();
                String videoDrm = mediaDrm.getText().toString();
                String videoAgent = mediaAgent.getText().toString();
                String videoReferer = referer.getText().toString();
                String videoCookie = cookie.getText().toString();
                String videoOrigin = origin.getText().toString();

                if(!videoUrl.isEmpty()) {
                    Intent intent = new Intent(getActivity(), PlayerActivity.class);
                    intent.putExtra("stream-url", videoUrl);
                    intent.putExtra("user-agent", videoAgent);
                    intent.putExtra("origin",videoOrigin);
                    intent.putExtra("referer", videoReferer);
                    intent.putExtra("cookie",videoCookie);
                    intent.putExtra("license",videoDrm);
                    intent.putExtra("title","OwlFlix");
                    getActivity().startActivity(intent);
                }
                else {
                    Toast.makeText(getActivity(), "Please Enter Video URL", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return root;
    }}