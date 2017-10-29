package re.parkhe.parkhere.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import re.parkhe.parkhere.R;
import re.parkhe.parkhere.model.ParkingLotsList;


public class TicketFragment extends Fragment {

    TextView spotTextView;
    TextView expirationTextView;
    BootstrapButton addTimeButton;
    BootstrapButton subTimeButton;
    BootstrapButton payTimeButton;
    BootstrapButton carInSpotsButton;
    BootstrapButton reportButton;
    TextView costTextView;
    TextView costLabelTextView;
    int time_count = 0;
    double cost = 5.00;
    double total_cost = 0;
    String expire_time = "04:02 PM";
    int parking_spot_num = 0;

    public TicketFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_ticket, container, false);
        spotTextView = rootview.findViewById(R.id.current_spot);
        expirationTextView = rootview.findViewById(R.id.expire_time);
        addTimeButton = rootview.findViewById(R.id.button_add_time);
        subTimeButton = rootview.findViewById(R.id.button_subtract_time);
        payTimeButton = rootview.findViewById(R.id.button_pay_now);
        carInSpotsButton = rootview.findViewById(R.id.button_report);
        reportButton = rootview.findViewById(R.id.button_report_error);
        costTextView = rootview.findViewById(R.id.cost_text_view);
        costLabelTextView = rootview.findViewById(R.id.cost_text);

        payTimeButton.setEnabled(false);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        if (ParkingLotsList.hour_add == 0 || ParkingLotsList.hour_add == 1) {
            c.add(Calendar.HOUR, 1);
            total_cost += cost;
        } else {
            c.add(Calendar.HOUR, 2);
            total_cost += cost * 2;
        }
        if (ParkingLotsList.hour_add % 2 == 1) {
            c.add(Calendar.MINUTE, 30);
            total_cost += cost * .5;
        }

        costLabelTextView.setText("Total Cost: $" + total_cost);
        expire_time = df.format(c.getTime());
        expirationTextView.setText("Expires: " + expire_time);
        Random random = new Random();
        parking_spot_num = random.nextInt(100);
        spotTextView.setText("Spot: " + parking_spot_num);
        costTextView.setText("Initial Spot Cost: $" + total_cost + " (Expires: " + expire_time + ")");


        addTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time_count += 15;
                payTimeButton.setText(time_count + " Minutes ($" + cost * (time_count / 60.0) + ")");
                payTimeButton.setEnabled(true);
            }
        });
        subTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (time_count > 15) {
                    time_count -= 15;
                    payTimeButton.setText(time_count + " Minutes ($" + cost * (time_count / 60.0) + ")");
                } else {
                    time_count = 0;
                    payTimeButton.setEnabled(false);
                }
            }
        });


        payTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title("Extra Time Confirmation")
                        .content("Adding " + time_count + " Minutes\nCost: $" + (time_count / 60.0) * cost)
                        .positiveText("Confirm")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
                                Date d = null;
                                try {
                                    d = df.parse(expire_time);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(d);
                                calendar.add(Calendar.MINUTE, time_count);
                                expire_time = df.format(calendar.getTime());
                                expirationTextView.setText("Expires: " + expire_time);
                                String stuff = (String) costTextView.getText();
                                costTextView.setText(stuff + "\nExtra " + time_count + " Minutes Cost: $" + (time_count / 60.0) * cost + " ( Expires: " + expire_time + ")");
                                payTimeButton.setText("0 Minutes ($0.00)");
                                total_cost += (time_count / 60.0) * cost;
                                costLabelTextView.setText("Total Cost: $" + total_cost);
                                payTimeButton.setEnabled(false);
                                time_count = 0;
                            }
                        })
                        .show();
            }
        });

        carInSpotsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title("Report Car in Spot")
                        .content("You will be assigned a new Parking Spot")
                        .positiveText("Confirm")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                parking_spot_num++;
                                spotTextView.setText("Spot: " + parking_spot_num);
                            }
                        })
                        .show();
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title("Contact Support")
                        .content("Livechat with support Here\nContact support at 555-555-5555")
                        .negativeText("Cancel")
                        .show();
            }
        });


        return rootview;
    }

}
