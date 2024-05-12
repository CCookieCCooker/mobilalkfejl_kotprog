package com.example.mobilalkfejl_kotprog.ui.teams;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilalkfejl_kotprog.CreateTeamActivity;
import com.example.mobilalkfejl_kotprog.EditTeamActivity;
import com.example.mobilalkfejl_kotprog.R;
import com.example.mobilalkfejl_kotprog.databinding.FragmentTeamsListBinding;
import com.example.mobilalkfejl_kotprog.helpers.StaticHelpers;
import com.example.mobilalkfejl_kotprog.models.Team;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.stream.StreamSupport;

public class TeamsListFragment extends Fragment {

    private FragmentTeamsListBinding binding;

    private LinearLayout listLayout;

    private FirebaseFirestore firestore;

    private FirebaseUser user;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentTeamsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton fab = root.findViewById(R.id.addTeam);
        fab.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), CreateTeamActivity.class));
        });

        listLayout = root.findViewById(R.id.teamsLayout);

        firestore = FirebaseFirestore.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        listLayout.removeAllViews();
        firestore
                .collection("users")
                .document(user.getUid())
                .collection("teams")
                .orderBy("name")
                .get()
                .addOnSuccessListener(snapshot -> {
                    StreamSupport.stream(snapshot.spliterator(), false)
                            .map(doc -> doc.toObject(Team.class))
                            .forEach(team -> {
                                View teamView = getLayoutInflater().inflate(R.layout.team_list_item, null);

                                TextView teamName = teamView.findViewById(R.id.teamListItemTeamName);
                                TextView teamPrincipal = teamView.findViewById(R.id.teamListItemTeamPrincipal);

                                teamName.setText(String.format("%s %s", team.getCity(), team.getName()));
                                teamPrincipal.setText(team.getPrincipal());

                                listLayout.addView(teamView);

                                teamView.setOnClickListener(v -> {
                                    Intent intent = new Intent(getActivity(), EditTeamActivity.class);
                                    intent.putExtra("TEAM_ID", team.getId());
                                    startActivity(intent);
                                });
                            });
                })
                .addOnFailureListener(ex -> {
                    StaticHelpers.showTopSnackbar(getView(), ex);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}