package vamk.e1700814.payapplication.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.e1700814.payapplication.R;
import com.e1700814.payapplication.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import vamk.e1700814.payapplication.api.UserController;
import vamk.e1700814.payapplication.api.SessionInterface;
import vamk.e1700814.payapplication.model.HomeFragmentViewModel;

public class HomeFragment extends Fragment implements View.OnClickListener, SessionInterface {

    private HomeFragmentViewModel homeFragmentViewModel;

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
    public void onStart() {
        super.onStart();
        initializeSession();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeFragmentViewModel = new ViewModelProvider(requireActivity()).get(HomeFragmentViewModel.class);
        layoutVisibility(false);
        setOnClickListener();
        setViewModelValues();
        blinkingAnimation();
    }

    @Override
    public void onResume() {
        super.onResume();
        setViewModelValues();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void initializeSession() {
        UserController userController = new UserController(HomeFragment.this.requireActivity().getApplication());
        userController.setSessionInterface(this);
        userController.setSession();
    }

    @Override
    public void onAuthenticationComplete(boolean success){
        if (success) {
            layoutVisibility(true);
        } else {
            layoutVisibility(false);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.landingFragment);
        }
    }

    @Override
    public void layoutVisibility(boolean setVisible) {
        final ProgressBar loadingProgressBar = binding.loading;
        final ConstraintLayout constraintLayout = binding.sendFragmentLayout;
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);

        if (setVisible) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.VISIBLE);
            loadingProgressBar.setVisibility(View.INVISIBLE);
        } else {
            constraintLayout.setVisibility(View.INVISIBLE);
            loadingProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void setOnClickListener() {
        Button[] keys = {binding.numpadOne, binding.numpadTwo, binding.numpadThree,
                binding.numpadFour, binding.numpadFive, binding.numpadSix,
                binding.numpadSeven, binding.numpadEight, binding.numpadNine,
                binding.numpadZero, binding.numpadPoint, binding.numpadConfirm};
        for (Button key : keys) {
            key.setOnClickListener(this);
        }
        binding.imageViewBackSpace.setOnClickListener(this);
    }

    public void setViewModelValues() {
        if (homeFragmentViewModel.hasDecimalPoint()) {
            binding.displayedPoint.setText(getResources().getString(R.string.localDecimal));
        }
        binding.displayedWholeNumber.setText(homeFragmentViewModel.getWholeNumber());
        binding.displayedDecimal.setText(homeFragmentViewModel.getDecimal());
        animateBackspace();
    }

    public void blinkingAnimation() {
        Animation blinkingLine = new AlphaAnimation(0.0f, 1.0f);
        blinkingLine.setDuration(50);
        blinkingLine.setStartOffset(450);
        blinkingLine.setRepeatMode(Animation.REVERSE);
        blinkingLine.setRepeatCount(Animation.INFINITE);
        binding.blinkingLine.startAnimation(blinkingLine);
    }

    public void backSpace() {
        if (homeFragmentViewModel.hasDecimal()) {
            eraseDecimal();
        } else if (homeFragmentViewModel.hasWholeNumber()) {
            eraseWholeNumber();
        }
    }

    public void eraseWholeNumber() {
        if (homeFragmentViewModel.hasDecimalPoint()) {
            setDecimalPoint(false);
        } else {
            binding.displayedWholeNumber.setText(homeFragmentViewModel.decrementWholeNumber());
        }
    }

    public void eraseDecimal() {
        binding.displayedDecimal.setText(homeFragmentViewModel.decrementDecimal());
    }

    public void setDecimalPoint(Boolean b) {
        if (!homeFragmentViewModel.hasWholeNumber()) {
            return;
        }
        homeFragmentViewModel.toggleDecimalPoint(b);
        binding.displayedPoint.setText(homeFragmentViewModel.setDecimalPoint(b));
    }

    public void addNumber() {
        if (homeFragmentViewModel.isDecimalAndMaxNotReached()) {
            binding.displayedDecimal.setText(homeFragmentViewModel.incrementDecimal());
        } else if (homeFragmentViewModel.isWholeNumberAndMaxNotReached()) {
            binding.displayedWholeNumber.setText(homeFragmentViewModel.incrementWholeNumber());
        }
    }

    public void noValueFound() {
        Toast.makeText(getActivity(),getResources().getString(R.string.noValueFound),Toast.LENGTH_SHORT).show();
    }

    public void moveToNextFragment() {
        setRadioButtonValue();
        homeFragmentViewModel.setMutableValue();
        if (homeFragmentViewModel.isValidInput()) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_contactsFragment);
        } else {
            noValueFound();
        }
    }

    public void setRadioButtonValue() {
        int checkedRadioButtonId = binding.radioButtonGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.radioButtonSend) {
            homeFragmentViewModel.setRadioButtonToSend(true);
        } else if (checkedRadioButtonId == R.id.radioButtonReceive) {
            homeFragmentViewModel.setRadioButtonToSend(false);
        }
    }

    public void checkForKeyType() {
        if (homeFragmentViewModel.getKey().equals(getResources().getString(R.string.numpadBackSpace))) {
            backSpace();
        } else if (homeFragmentViewModel.getKey().contentEquals(binding.numpadPoint.getText())) {
            setDecimalPoint(true);
        } else if (homeFragmentViewModel.getKey().contentEquals(binding.numpadConfirm.getText())) {
            moveToNextFragment();
        } else {
            addNumber();
        }
    }

    public void numPadKeySelector(int selectedKey) {
        Button[] keys = {binding.numpadOne, binding.numpadTwo, binding.numpadThree,
                binding.numpadFour, binding.numpadFive, binding.numpadSix,
                binding.numpadSeven, binding.numpadEight, binding.numpadNine,
                binding.numpadZero, binding.numpadPoint, binding.numpadConfirm};
        for (Button key : keys) {
            if (selectedKey == key.getId()) {
                homeFragmentViewModel.setKey((String) key.getText());
                break;
            }
        }
        if (selectedKey == R.id.imageViewBackSpace) {
            homeFragmentViewModel.setKey(getResources().getString(R.string.numpadBackSpace));
        }
    }

    @Override
    public void onClick(View view) {
        numPadKeySelector(view.getId());
        checkForKeyType();
        animateBackspace();
    }

    public void animateBackspace() {
        if (homeFragmentViewModel.getWholeNumber().length() > 0) {
            binding.imageViewBackSpace.setImageResource(R.drawable.ic_baseline_backspace_selected);
        } else {
            binding.imageViewBackSpace.setImageResource(R.drawable.ic_baseline_backspace);
        }
    }
}