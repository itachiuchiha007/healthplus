package com.anurag.healthplus.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.anurag.healthplus.R;
import com.anurag.healthplus.utils.Utilities;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchDoctorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchDoctorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchDoctorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button searchDoctor;
    Utilities utils;
    Spinner spinnerSpecialization,spinnerHospital;
    RadioGroup radioGroup;
    RadioButton r1,r2,r3,radioButton;
    View view;

    String urlGetSpecializationSpinner = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/get_specialization_spinner.php";
    String urlGetHospitalSpinner = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/get_hospital_spinner.php";
    String urlToGetDoctor = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/get_doctor_spinner.php";
    String urlToGetDoctor1 = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/get_doctor1.php";
    String urlToGetDoctor2 = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/get_doctor2.php";
    String [][] specializationDetails, hospitalDetails, doctorDetails;
    String hospitalId,specialization;
    RequestQueue requestQueue;
    RelativeLayout relativeLayout1,relativeLayout2;
    String hospitalname;
    TableLayout tableLayout;
    JSONArray object;
    int flag=0;
    boolean check[] = new boolean[2];


    private OnFragmentInteractionListener mListener;

    public SearchDoctorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchDoctorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchDoctorFragment newInstance(String param1, String param2) {
        SearchDoctorFragment fragment = new SearchDoctorFragment();
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

        view = inflater.inflate(R.layout.fragment_search_doctor, container, false);

        radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        r1 = (RadioButton) view.findViewById(R.id.radio1);
        r2 = (RadioButton) view.findViewById(R.id.radio2);
        r3 = (RadioButton) view.findViewById(R.id.radio3);
        spinnerSpecialization = (Spinner) view.findViewById(R.id.spinnerSpecialization);
        spinnerHospital = (Spinner) view.findViewById(R.id.spinnerHospital);
        searchDoctor = (Button) view.findViewById(R.id.searchDoctor);
        relativeLayout1 = (RelativeLayout) view.findViewById(R.id.relativeLayoutSpecialization);
        relativeLayout2 = (RelativeLayout) view.findViewById(R.id.relativeLayoutHospital);
        requestQueue = Volley.newRequestQueue(getContext());
        tableLayout = (TableLayout) view.findViewById(R.id.tablelayout);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = (RadioButton) view.findViewById(i);
                FindRadioButton(radioButton);
            }
        });

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, urlGetSpecializationSpinner,
                null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
//				Log.d(TAG, "" + (response == null));

                utils = new Utilities();
                specializationDetails = utils.getSpecializationArrayFromJsonResponse(response);

                String[] getSpecializationNameListForSpinner = utils.convertSpecializationDetailsToGetSpecializationNameListForSpinner(specializationDetails);
                int len = getSpecializationNameListForSpinner.length;
                String[] temp = new String[len + 1];

                temp[0] = "Select";
                System.arraycopy(getSpecializationNameListForSpinner, 0, temp, 1, len);

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout
                        .simple_spinner_item, temp);

                spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinnerSpecialization.setAdapter(spinnerAdapter);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "VolleyError: " + error);
            }
        });

        requestQueue.add(jsonArrayRequest);
        //setBoolean(3,false);
        spinnerSpecialization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            //index = 0
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
//				Log.d(TAG, "value : " + adapterView.getItemAtPosition(i).toString());
//				Log.d(TAG, "value : " + spinnerSpecialization.getSelectedItem().toString());

                specialization = spinnerSpecialization.getSelectedItem().toString();
                if(specialization.equals("Select")){
                    setBoolean(0, false);
                }
                else{
                    setBoolean(0, true);
                }
                Log.d(TAG, "value specialization1 : " + specialization);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

         jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, urlGetHospitalSpinner,
                null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                utils = new Utilities();
                hospitalDetails = utils.getHospitalArrayFromJsonResponse(response);

                String[] getHospitalNameListForSpinner = utils
                        .convertHospitalDetailsToGetHospitalNameListForSpinner(hospitalDetails);

                int len = getHospitalNameListForSpinner.length;
                String[] temp = new String[len + 1];

                temp[0] = "Select";
                System.arraycopy(getHospitalNameListForSpinner, 0, temp, 1, len);

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout
                        .simple_spinner_item, temp);

                spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
              //  spinnerHospital.setPrompt("Select Specialization");
                spinnerHospital.setAdapter(spinnerAdapter);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "VolleyError: " + error);
            }
        });

        requestQueue.add(jsonArrayRequest);
        spinnerHospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
//				Log.d(TAG, "value : " + adapterView.getItemAtPosition(i).toString());
//				Log.d(TAG, "value : " + spinnerHospital.getSelectedItem().toString());

                hospitalname = spinnerHospital.getSelectedItem().toString();
                if(hospitalname!= "Select") {
                    utils = new Utilities();
                    hospitalId = utils.getHospitalIdFromHospitalName(hospitalDetails, hospitalname);

                    Log.d(TAG, "value HospitalId : " + hospitalId);
                    setBoolean(1, true);

                }
                else
                {
                    setBoolean(1,false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        TableRow tableRow = new TableRow(getContext());
        TextView textView = new TextView(getContext());
        TextView textView1 = new TextView(getContext());
        TextView textView2 = new TextView(getContext());
        TextView textView3 = new TextView(getContext());

        textView.setText("Name                            ");
        textView.setTextColor(Color.BLUE);
        textView.setTextSize(20);
        tableRow.addView(textView);

        textView1.setText("Specialization     ");
        textView1.setTextColor(Color.BLUE);
        textView1.setTextSize(20);
        tableRow.addView(textView1);
        textView2.setText("Qualification      ");
        textView2.setTextColor(Color.BLUE);
        textView2.setTextSize(20);
        tableRow.addView(textView2);
        textView3.setText("Fees");
        textView3.setTextColor(Color.BLUE);
        textView3.setTextSize(20);
        tableRow.addView(textView3);
        tableLayout.addView(tableRow);

        searchDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, urlToGetDoctor, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        object = new JSONArray();
                        object = response;
                        Log.d(TAG,"value object : " + response);

                        if(flag==1 && check[0])
                        {
                            init(1);
                        }
                        if(flag==2 && check[1])
                        {
                            init(2);
                        }
                        if(flag==3 && check[0] && check[1])
                        {
                            init(3);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(jsonArrayRequest);



            }
        });



        return view;
    }
    private void FindRadioButton(RadioButton r)
    {
        String a = "Specialization";
        String b = "Hospital";
        String c = "Both";
        String d = r.getText().toString();
        if(d.equals(a))
        {
            Log.d(TAG,"value a : " + a);
            relativeLayout1.setVisibility(view.VISIBLE);
            relativeLayout2.setVisibility(view.GONE);
            flag=1;
        }
        if(d.equals(b))
        {
            Log.d(TAG,"value b : " + b);
            relativeLayout1.setVisibility(view.GONE);
            relativeLayout2.setVisibility(view.VISIBLE);
            flag=2;
        }
        if(d.equals(c))
        {
            Log.d(TAG,"value c : " + c);
            relativeLayout1.setVisibility(view.VISIBLE);
            relativeLayout2.setVisibility(view.VISIBLE);
            flag=3;
        }


    }
    private void init(int toCheck)
    {
        cleanTable(tableLayout);



        for(int i = 0;i<object.length();i++ )
        {

            try {
                JSONObject objectJson = object.getJSONObject(i);
                if(toCheck == 1 && specialization.equals(objectJson.getString("specialization"))){
                    TableRow tableRow1 = new TableRow(getContext());
                    TextView textView4 = new TextView(getContext());
                    TextView textView5 = new TextView(getContext());
                    TextView textView6 = new TextView(getContext());
                    TextView textView7 = new TextView(getContext());

                textView4.setText(objectJson.getString("doctor_fname")+" "+objectJson.getString("doctor_lname"));
                textView4.setTextColor(Color.BLACK);
                tableRow1.addView(textView4);
                textView5.setText(objectJson.getString("specialization"));
                textView5.setTextColor(Color.BLACK);
                tableRow1.addView(textView5);
                textView6.setText(objectJson.getString("qualification"));
                textView6.setTextColor(Color.BLACK);
                tableRow1.addView(textView6);
                textView7.setText(objectJson.getString("fees"));
                textView7.setTextColor(Color.BLACK);
                tableRow1.addView(textView7);
                tableLayout.addView(tableRow1);}
                if(toCheck == 2 && hospitalId.equals(objectJson.getString("hospital_id"))){
                    TableRow tableRow1 = new TableRow(getContext());
                    TextView textView4 = new TextView(getContext());
                    TextView textView5 = new TextView(getContext());
                    TextView textView6 = new TextView(getContext());
                    TextView textView7 = new TextView(getContext());

                    textView4.setText(objectJson.getString("doctor_fname")+" "+objectJson.getString("doctor_lname"));
                    textView4.setTextColor(Color.BLACK);

                    tableRow1.addView(textView4);
                    textView5.setText(objectJson.getString("specialization"));
                    textView5.setTextColor(Color.BLACK);

                    tableRow1.addView(textView5);
                    textView6.setText(objectJson.getString("qualification"));
                    textView6.setTextColor(Color.BLACK);

                    tableRow1.addView(textView6);

                    textView7.setText(objectJson.getString("fees"));
                    textView7.setTextColor(Color.BLACK);
                    textView7.setGravity(Gravity.CENTER);
                    tableRow1.addView(textView7);
                    tableLayout.addView(tableRow1);}
                if(toCheck == 3 && specialization.equals(objectJson.getString("specialization")) && hospitalId.equals(objectJson.getString("hospital_id"))){
                    TableRow tableRow1 = new TableRow(getContext());
                    TextView textView4 = new TextView(getContext());
                    TextView textView5 = new TextView(getContext());
                    TextView textView6 = new TextView(getContext());
                    TextView textView7 = new TextView(getContext());

                    textView4.setText(objectJson.getString("doctor_fname")+" "+objectJson.getString("doctor_lname"));
                    textView4.setTextColor(Color.BLACK);
                    tableRow1.addView(textView4);
                    textView5.setText(objectJson.getString("specialization"));
                    textView5.setTextColor(Color.BLACK);
                    tableRow1.addView(textView5);
                    textView6.setText(objectJson.getString("qualification"));
                    textView6.setTextColor(Color.BLACK);
                    tableRow1.addView(textView6);
                    textView7.setText(objectJson.getString("fees"));
                    textView7.setTextColor(Color.BLACK);
                    tableRow1.addView(textView7);
                    tableLayout.addView(tableRow1);}

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

    private void setBoolean(int index, Boolean bool){ check[index] = bool;}

//    private void doctorSpinner()
//    {
//       JsonArrayRequest  jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, urlGetDoctorSpinner,
//                null, new Response.Listener<JSONArray>()
//        {
//            @Override
//            public void onResponse(JSONArray response)
//            {
//                utils = new Utilities();
//                doctorDetails = utils.getDoctorsArrayFromJsonResponse(response);
//
//                String[] getDoctorNameListForSpinner = utils
//                        .convertDoctorDetailsToGetDoctorNameListForSpinner(doctorDetails,hospitalId,specialization);
//                int len = getDoctorNameListForSpinner.length;
//                String[] temp = new String[len + 1];
//
//                temp[0] = "Select";
//                System.arraycopy(getDoctorNameListForSpinner, 0, temp, 1, len);
//
//                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout
//                        .simple_spinner_item, temp);
//
//                spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//                spinnerDoctor.setPrompt("Select Specialization");
//                spinnerDoctor.setAdapter(spinnerAdapter);
//            }
//        }, new Response.ErrorListener()
//        {
//            @Override
//            public void onErrorResponse(VolleyError error)
//            {
//                Log.e(TAG, "VolleyError: " + error);
//            }
//        });
//
//        requestQueue.add(jsonArrayRequest);
//        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
//            {
////				Log.d(TAG, "value : " + adapterView.getItemAtPosition(i).toString());
////				Log.d(TAG, "value : " + spinnerSpecialization.getSelectedItem().toString());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView)
//            {
//
//            }
//        });

 //   }

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
