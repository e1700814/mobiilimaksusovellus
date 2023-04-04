package vamk.e1700814.payapplication.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.e1700814.payapplication.R;
import com.e1700814.payapplication.databinding.FragmentRegisterBinding;

import vamk.e1700814.payapplication.api.SessionInterface;
import vamk.e1700814.payapplication.api.UserController;
import vamk.e1700814.payapplication.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener, SessionInterface {

    private User user;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
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
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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

    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonRegister.setOnClickListener(this);
        binding.linkBackToLanding.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonRegister) {
            registerUser();
        }
        if (view.getId() == R.id.linkBackToLanding) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_registerFragment_to_landingFragment);
        }
    }

    public void registerUser() {
        user = new User();
        user.setWholename(binding.editTextFullName.getText().toString());
        user.setPassword(binding.editTextPassword.getText().toString());
        user.setPhone(binding.editTextPhoneNumber.getText().toString());
        user.setCity(binding.editTextCity.getText().toString());
        user.setPostal(Integer.parseInt(binding.editTextPostalCode.getText().toString()));
        if (user.isValidUserInputForRegister()) {
            initializeSession();
        } else {
            Toast.makeText(getActivity(), R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void initializeSession() {
        layoutVisibility(true);
        UserController userController = new UserController(RegisterFragment.this.requireActivity().getApplication());
        userController.setSessionInterface(this);
        userController.register(user.getWholename(),
                user.getPassword(),
                user.getPhone(),
                user.getCity(),
                user.getPostal());
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
}