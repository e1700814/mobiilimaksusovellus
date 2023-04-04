package vamk.e1700814.payapplication.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.e1700814.payapplication.R;
import com.e1700814.payapplication.databinding.FragmentLandingBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import vamk.e1700814.payapplication.api.UserController;
import vamk.e1700814.payapplication.api.SessionInterface;
import vamk.e1700814.payapplication.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LandingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandingFragment extends Fragment implements View.OnClickListener, SessionInterface {

    Animation slideInFromTopAnim;
    Animation slideInFromBottomAnim;
    private User user;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LandingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LandingFragment newInstance(String param1, String param2) {
        LandingFragment fragment = new LandingFragment();
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

    private FragmentLandingBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLandingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListener();
        hideBottomNavBar();
        landingAnimation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.linkToRegister) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_landingFragment_to_registerFragment);
        }
        if (view.getId() == R.id.button_login) {
            loginUser();
        }
    }

    public void loginUser() {
        user = new User();
        user.setPhone(binding.editTextPhoneNumber.getText().toString());
        user.setPassword(binding.editTextPassword.getText().toString());

        if (user.isValidUserInputForLogin()) {
            initializeSession();
        } else {
            showInvalidInputMessage();
        }
    }

    @Override
    public void initializeSession() {
        layoutVisibility(true);
        UserController userController = new UserController(LandingFragment.this.requireActivity().getApplication());
        userController.setSessionInterface(this);
        userController.login(user.getPhone(), user.getPassword());
    }

    @Override
    public void onAuthenticationComplete(boolean success){
        layoutVisibility(false);
        if (success) {
            Navigation.findNavController(binding.getRoot()).popBackStack(R.id.homeFragment, false);
        }
    }

    @Override
    public void layoutVisibility(boolean setVisible) {
        final ProgressBar loadingProgressBar = binding.loading;
        if (setVisible) {
            loadingProgressBar.setVisibility(View.VISIBLE);
        } else {
            loadingProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void showInvalidInputMessage() {
        Toast.makeText(getActivity(),"Ole hyvä ja täytä kaikki kentät.",Toast.LENGTH_SHORT).show();
    }

    private void hideBottomNavBar() {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void landingAnimation() {
        slideInFromTopAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_from_top);
        slideInFromBottomAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_from_bottom);
        binding.headerConstraintLayout.setAnimation(slideInFromTopAnim);
        binding.constraintLayoutLoginInput.setAnimation(slideInFromBottomAnim);
    }

    private void setOnClickListener() {
        binding.linkToRegister.setOnClickListener(this);
        binding.buttonLogin.setOnClickListener(this);
    }
}