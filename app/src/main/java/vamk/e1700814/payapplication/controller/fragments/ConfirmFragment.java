package vamk.e1700814.payapplication.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.e1700814.payapplication.R;
import com.e1700814.payapplication.databinding.FragmentConfirmBinding;

import vamk.e1700814.payapplication.api.ActivityController;
import vamk.e1700814.payapplication.api.SessionInterface;
import vamk.e1700814.payapplication.api.SessionPreferences;
import vamk.e1700814.payapplication.model.Confirm;
import vamk.e1700814.payapplication.model.HomeFragmentViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmFragment extends Fragment implements SessionInterface {

    private HomeFragmentViewModel homeFragmentViewModel;
    SessionPreferences sessionPreferences;
    Confirm confirm;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfirmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmFragment newInstance(String param1, String param2) {
        ConfirmFragment fragment = new ConfirmFragment();
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

    private FragmentConfirmBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConfirmBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confirm = new Confirm();
        sessionPreferences = new SessionPreferences(this.requireContext());
        homeFragmentViewModel = new ViewModelProvider(requireActivity()).get(HomeFragmentViewModel.class);
        setCardViewDetails();
        setOnClickListeners();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setCardViewDetails() {

        binding.textViewFullName.setText(sessionPreferences.getRecipientName());
        binding.textViewPhone.setText(sessionPreferences.getRecipientPhone());

        homeFragmentViewModel.getInput().observe(getViewLifecycleOwner(), amount -> {
            confirm.setAmount(amount);
            binding.textViewAmount.setText(String.valueOf(confirm.getAmount()));
        });

        homeFragmentViewModel.getComment().observe(getViewLifecycleOwner(), comment -> {
            confirm.setComment(comment);
            binding.textViewComment.setText(confirm.getComment());
        });

        homeFragmentViewModel.getPaymentIsSend().observe(getViewLifecycleOwner(), paymentIsSend -> {
            if (paymentIsSend) {
                confirm.setPaymentIsSend(true);
                binding.textViewRecipient.setText(R.string.confirmRecipient);
                binding.textViewOperator.setVisibility(View.VISIBLE);
                binding.textViewAmount.setTextColor(ContextCompat.getColor(requireContext(), R.color.salmon));
                binding.textViewCurrency.setTextColor(ContextCompat.getColor(requireContext(), R.color.salmon));
            }
            if (!paymentIsSend) {
                confirm.setPaymentIsSend(false);
                binding.textViewRecipient.setText(R.string.confirmSender);
                binding.textViewOperator.setVisibility(View.GONE);
                binding.textViewAmount.setTextColor(ContextCompat.getColor(requireContext(), R.color.forest_green));
                binding.textViewCurrency.setTextColor(ContextCompat.getColor(requireContext(), R.color.forest_green));
            }
        });
    }

    private void setOnClickListeners() {
        binding.confirmPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeSession();
            }
        });
        binding.cancelPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackHome();
            }
        });
    }

    @Override
    public void onAuthenticationComplete(boolean success) {
        layoutVisibility(false);
        if (success) {
            Toast.makeText(this.requireContext(), R.string.payment_created , Toast.LENGTH_LONG).show();
            navigateBackHome();
        } else {
            Toast.makeText(this.requireContext(), R.string.payment_creation_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void initializeSession() {
        layoutVisibility(true);
        ActivityController activityController = new ActivityController(ConfirmFragment.this.requireActivity().getApplication());
        activityController.setSessionInterface(this);

        if (confirm.getPaymentIsSend()) {
            activityController.newSend(sessionPreferences.getRecipientId(), confirm.getAmount(), confirm.getComment());
        }
        if (!confirm.getPaymentIsSend()) {
            activityController.newRequest(sessionPreferences.getRecipientId(), confirm.getAmount(), confirm.getComment());
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

    public void navigateBackHome() {
        homeFragmentViewModel.resetMutableValue();
        sessionPreferences.setRecipientName(null);
        sessionPreferences.setRecipientId(null);
        Navigation.findNavController(binding.getRoot()).popBackStack(R.id.homeFragment, false);
    }
}