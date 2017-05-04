package com.anurag.healthplus.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.anurag.healthplus.R;
import com.anurag.healthplus.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApptHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ApptHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApptHistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    View view;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TableLayout tableLayout;
    JSONArray object,array;
    RequestQueue requestQueue;
    String patient_id,input;
    EditText editText;
    int flag;
    Button button1,button2,button3,button4;
    String urlToGetAppointmentHistory = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/get_appointment_table.php";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ApptHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ApptHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ApptHistoryFragment newInstance(String param1, String param2) {
        ApptHistoryFragment fragment = new ApptHistoryFragment();
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

        view = inflater.inflate(R.layout.fragment_appt_history, container, false);
        tableLayout = (TableLayout) view.findViewById(R.id.table);
        requestQueue = Volley.newRequestQueue(getContext());
        button1 = (Button) view.findViewById(R.id.button);
        button2 = (Button) view.findViewById(R.id.button2);
        button3 = (Button) view.findViewById(R.id.button3);
        button4 = (Button) view.findViewById(R.id.button4);
        editText = (EditText) view.findViewById(R.id.enterdoctor);

        MainActivity activity = (MainActivity) getActivity();
        patient_id = activity.getIntent().getExtras().getString("patient_id");
        Log.d(TAG,"value of patient_id : " + patient_id);

        TableRow tableRow = new TableRow(getContext());
        TextView textView = new TextView(getContext());
        TextView textView1 = new TextView(getContext());
        TextView textView2 = new TextView(getContext());
        TextView textView3 = new TextView(getContext());

        textView.setText("App_ID   ");
        textView.setTextColor(Color.BLUE);
        textView.setTextSize(20);
        tableRow.addView(textView);

        textView1.setText("Doctor_Name     ");
        textView1.setTextColor(Color.BLUE);
        textView1.setTextSize(20);
        tableRow.addView(textView1);
        textView2.setText("Appointment_Time    ");
        textView2.setTextColor(Color.BLUE);
        textView2.setTextSize(20);
        tableRow.addView(textView2);
        textView3.setText("Status");
        textView3.setTextColor(Color.BLUE);
        textView3.setTextSize(20);
        tableRow.addView(textView3);
        tableLayout.addView(tableRow);

        JSONObject object1 = new JSONObject();
        try {
            object1.put("id",patient_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array = new JSONArray();

        array.put(object1);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, urlToGetAppointmentHistory,array, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                object = new JSONArray();
                Log.d(TAG,"value of response in appthistory : " + response);
                object = response;
                init();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"value of error : " + error);
            }
        });

        requestQueue.add(jsonArrayRequest);


       button1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               editText.setVisibility(view.VISIBLE);
               flag = 1;
           }
       });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setVisibility(view.VISIBLE);
                flag=2;
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setVisibility(view.VISIBLE);
                flag=3;
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                input = editText.getText().toString().trim();
                init1();
            }
        });


     return view;
    }
    private void init1()
    {
        if(!input.isEmpty()){
            cleanTable(tableLayout);

            for(int i = 0;i<object.length();i++ )
            {


                try {
                    JSONObject objectJson = object.getJSONObject(i);

                    if(input.equals(objectJson.getString("firstname")+" "+objectJson.getString("lastname")) && flag ==1) {
                        TableRow tableRow1 = new TableRow(getContext());
                        TextView textView4 = new TextView(getContext());
                        TextView textView5 = new TextView(getContext());
                        TextView textView6 = new TextView(getContext());
                        TextView textView7 = new TextView(getContext());

                        textView5.setText("AP_ID " + objectJson.getString("sn"));
                        textView5.setTextColor(Color.BLACK);
                        tableRow1.addView(textView5);

                        textView4.setText(objectJson.getString("firstname") + " " + objectJson.getString("lastname"));
                        textView4.setTextColor(Color.BLACK);
                        tableRow1.addView(textView4);

                        textView6.setText(objectJson.getString("dates"));
                        textView6.setTextColor(Color.BLACK);
                        tableRow1.addView(textView6);

                        textView7.setText(objectJson.getString("appointment_status"));
                        textView7.setTextColor(Color.BLACK);
                        tableRow1.addView(textView7);
                        tableLayout.addView(tableRow1);
                    }
                    if(input.equals(objectJson.getString("dates")) && flag ==2) {
                        TableRow tableRow1 = new TableRow(getContext());
                        TextView textView4 = new TextView(getContext());
                        TextView textView5 = new TextView(getContext());
                        TextView textView6 = new TextView(getContext());
                        TextView textView7 = new TextView(getContext());

                        textView5.setText("AP_ID " + objectJson.getString("sn"));
                        textView5.setTextColor(Color.BLACK);
                        tableRow1.addView(textView5);

                        textView4.setText(objectJson.getString("firstname") + " " + objectJson.getString("lastname"));
                        textView4.setTextColor(Color.BLACK);
                        tableRow1.addView(textView4);

                        textView6.setText(objectJson.getString("dates"));
                        textView6.setTextColor(Color.BLACK);
                        tableRow1.addView(textView6);

                        textView7.setText(objectJson.getString("appointment_status"));
                        textView7.setTextColor(Color.BLACK);
                        tableRow1.addView(textView7);
                        tableLayout.addView(tableRow1);
                    }
                    if(input.equals(objectJson.getString("appointment_status")) && flag ==3) {
                        TableRow tableRow1 = new TableRow(getContext());
                        TextView textView4 = new TextView(getContext());
                        TextView textView5 = new TextView(getContext());
                        TextView textView6 = new TextView(getContext());
                        TextView textView7 = new TextView(getContext());

                        textView5.setText("AP_ID " + objectJson.getString("sn"));
                        textView5.setTextColor(Color.BLACK);
                        tableRow1.addView(textView5);

                        textView4.setText(objectJson.getString("firstname") + " " + objectJson.getString("lastname"));
                        textView4.setTextColor(Color.BLACK);
                        tableRow1.addView(textView4);

                        textView6.setText(objectJson.getString("dates"));
                        textView6.setTextColor(Color.BLACK);
                        tableRow1.addView(textView6);

                        textView7.setText(objectJson.getString("appointment_status"));
                        textView7.setTextColor(Color.BLACK);
                        tableRow1.addView(textView7);
                        tableLayout.addView(tableRow1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
        else {
            Toast.makeText(getContext(),"PLZ PROVIDE INPUT",Toast.LENGTH_LONG).show();
        }
    }

    private void init()
    {

        for(int i = 0;i<object.length();i++ )
        {

            try {
                JSONObject objectJson = object.getJSONObject(i);

                    TableRow tableRow1 = new TableRow(getContext());
                    TextView textView4 = new TextView(getContext());
                    TextView textView5 = new TextView(getContext());
                    TextView textView6 = new TextView(getContext());
                    TextView textView7 = new TextView(getContext());

                    textView5.setText("AP_ID "+objectJson.getString("sn"));
                    textView5.setTextColor(Color.BLACK);
                    tableRow1.addView(textView5);

                    textView4.setText(objectJson.getString("firstname")+" "+objectJson.getString("lastname"));
                    textView4.setTextColor(Color.BLACK);
                    tableRow1.addView(textView4);

                    textView6.setText(objectJson.getString("dates"));
                    textView6.setTextColor(Color.BLACK);
                    tableRow1.addView(textView6);

                    textView7.setText(objectJson.getString("appointment_status"));
                    textView7.setTextColor(Color.BLACK);
                    tableRow1.addView(textView7);
                    tableLayout.addView(tableRow1);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
