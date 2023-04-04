package vamk.e1700814.payapplication.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.e1700814.payapplication.R;
import com.e1700814.payapplication.databinding.FragmentContactsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vamk.e1700814.payapplication.api.SessionInterface;
import vamk.e1700814.payapplication.api.SessionPreferences;
import vamk.e1700814.payapplication.api.UserController;
import vamk.e1700814.payapplication.model.Contacts;
import vamk.e1700814.payapplication.model.HomeFragmentViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment implements SessionInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Boolean isAboutToNavigate = false;
    SessionPreferences sessionPreferences;
    Contacts contacts;
    private HomeFragmentViewModel homeFragmentViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
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

    private FragmentContactsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentContactsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sessionPreferences = new SessionPreferences(ContactsFragment.this.requireActivity().getApplication());
        homeFragmentViewModel = new ViewModelProvider(requireActivity()).get(HomeFragmentViewModel.class);
        binding.autoCompleteUsername.setText(sessionPreferences.getRecipientName());
        textListener();
        binding.buttonGoToConfirmFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = binding.editTextComment.getText().toString();
                if (!comment.isEmpty()) {
                    isAboutToNavigate = true;
                    homeFragmentViewModel.setCommentMutableValue(comment);
                    initializeSession();
                } else {
                    Toast.makeText(getActivity(), R.string.error_comment_required, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void textListener() {
        binding.autoCompleteUsername.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                initializeSession();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // Required empty override method
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // Required empty override method
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAuthenticationComplete(boolean success) {
        if (!success) {
            isAboutToNavigate = false;
            return;
        }
        if (isAboutToNavigate) {
            isAboutToNavigate = false;
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_contactsFragment_to_confirmFragment);
        }
        if (!isAboutToNavigate) {
            layoutVisibility(true);
        }
    }

    @Override
    public void initializeSession() {
        UserController userController = new UserController(ContactsFragment.this.requireActivity().getApplication());
        userController.setSessionInterface(this);
        if (isAboutToNavigate) {
            userController.getRecipientDetails(String.valueOf(binding.autoCompleteUsername.getText()));
        }
        if (!isAboutToNavigate) {
            userController.getUsers(String.valueOf(binding.autoCompleteUsername.getText()));
        }
    }

    @Override
    public void layoutVisibility(boolean setVisible) {
        contacts = new Contacts();
        if (setVisible) {
            String jsonString = sessionPreferences.getUsersList();

            ArrayList<String> listOfUsers = contacts.getParsedArrayList(jsonString);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listOfUsers);
            binding.autoCompleteUsername.setAdapter(adapter);
        }
    }
}