package vamk.e1700814.payapplication.controller.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e1700814.payapplication.databinding.FragmentActivitiesBinding;

import java.util.ArrayList;

import vamk.e1700814.payapplication.api.SessionInterface;
import vamk.e1700814.payapplication.api.ActivityController;
import vamk.e1700814.payapplication.api.SessionPreferences;
import vamk.e1700814.payapplication.model.Activities;
import vamk.e1700814.payapplication.controller.ActivitiesAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivitiesFragment extends Fragment implements SessionInterface {

    ArrayList<Activities> activitiesList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ActivitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeSession();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivitiesFragment newInstance(String param1, String param2) {
        ActivitiesFragment fragment = new ActivitiesFragment();
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

    private FragmentActivitiesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentActivitiesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setUpActivitiesList() {
        SessionPreferences sessionPreferences = new SessionPreferences(ActivitiesFragment.this.requireActivity().getApplication());
        String jsonInString = sessionPreferences.getActivitiesList();

        Activities activities = new Activities();
        activitiesList = activities.getParsedArrayList(jsonInString);

        if (activitiesList.size() > 0) {
            layoutVisibility(true);
        } else {
            layoutVisibility(false);
        }
    }

    @Override
    public void initializeSession() {
        ActivityController activityController = new ActivityController(ActivitiesFragment.this.requireActivity().getApplication());
        activityController.setSessionInterface(this);
        activityController.getActivities();
    }

    @Override
    public void onAuthenticationComplete(boolean success) {
        if (success) {
            setUpActivitiesList();
        }
        if (!success) {
            layoutVisibility(false);
        }
    }

    @Override
    public void layoutVisibility(boolean setVisible) {
        binding.loading.setVisibility(View.GONE);
        if (setVisible) {
            RecyclerView recyclerView = binding.activitiesRecyclerView;
            ActivitiesAdapter adapter = new ActivitiesAdapter(this.getContext(), activitiesList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            recyclerView.setAdapter(adapter);

            binding.activitiesContainer.setVisibility(View.VISIBLE);
            binding.activitiesEmptyTextView.setVisibility(View.GONE);
        }

        if (!setVisible) {
            binding.activitiesContainer.setVisibility(View.GONE);
            binding.activitiesEmptyTextView.setVisibility(View.VISIBLE);
        }
    }

}
