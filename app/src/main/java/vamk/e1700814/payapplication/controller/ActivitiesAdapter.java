package vamk.e1700814.payapplication.controller;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.e1700814.payapplication.R;
import com.e1700814.payapplication.databinding.FragmentActivitiesRowBinding;

import java.util.ArrayList;

import vamk.e1700814.payapplication.api.ActivityController;
import vamk.e1700814.payapplication.api.SessionInterface;
import vamk.e1700814.payapplication.model.Activities;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.MyViewHolder> implements SessionInterface {

    Context context;
    ArrayList<Activities> activitiesList;
    private int id_request;
    private String status;
    private static final String STATUS_COMPLETED = "completed";
    private static final String STATUS_DECLINED = "declined";

    public ActivitiesAdapter(Context context, ArrayList<Activities> activitiesList) {
        this.context = context;
        this.activitiesList = activitiesList;
    }

    private FragmentActivitiesRowBinding binding;

    @NonNull
    @Override
    public ActivitiesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = FragmentActivitiesRowBinding.inflate(inflater, parent, false);
        return new ActivitiesAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivitiesAdapter.MyViewHolder holder, int position) {
        Activities activity = activitiesList.get(position);

        holder.user.setText(activity.getUser());
        holder.date.setText(activity.getTimestamp());
        holder.phone.setText(activity.getPhone());
        holder.comment.setText(activity.getComment());
        holder.amount.setText(Double.toString(activity.getAmount()));
        holder.icon.setImageResource(activity.getImageView());

        if (activity.isColorGreen()) {
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.forest_green));
            holder.currencySign.setTextColor(ContextCompat.getColor(context, R.color.forest_green));
            holder.operator.setVisibility(View.GONE);
        }

        if (!activity.isColorGreen()) {
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.currencySign.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.operator.setVisibility(View.VISIBLE);
        }

        if (activity.isPendingRequest()) {
            holder.declineButton.setVisibility(View.VISIBLE);
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.divider.setVisibility(View.VISIBLE);

            holder.declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id_request = activity.getId_request();
                    status = STATUS_DECLINED;
                    initializeSession();
                }
            });
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id_request = activity.getId_request();
                    status = STATUS_COMPLETED;
                    initializeSession();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return activitiesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView user;
        TextView date;
        TextView amount;
        TextView operator;
        TextView currencySign;
        TextView phone;
        TextView comment;
        Button declineButton;
        Button acceptButton;
        View divider;

        public MyViewHolder(@NonNull FragmentActivitiesRowBinding itemView) {
            super(itemView.getRoot());
            icon = binding.imageViewAvatar;
            user = binding.textViewFullName;
            date = binding.textViewMenuDate;
            amount = binding.textViewMenuCurrency;
            operator = binding.textViewMenuCurrencyOperator;
            currencySign = binding.textViewMenuCurrencySign;
            phone = binding.textViewPhone;
            comment = binding.textViewComment;
            declineButton = binding.buttonDecline;
            acceptButton = binding.buttonAccept;
            divider = binding.divider;
        }
    }

    @Override
    public void onAuthenticationComplete(boolean success) {
        if (success) {
            Toast.makeText(context, R.string.payment_updated, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.payment_failed, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void initializeSession() {
        ActivityController activityController = new ActivityController((Application) context.getApplicationContext());
        activityController.setSessionInterface(this);
        activityController.updateStatus(id_request, status);
    }

    @Override
    public void layoutVisibility(boolean setVisible) {

    }

}
