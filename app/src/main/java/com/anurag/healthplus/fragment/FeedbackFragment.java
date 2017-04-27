package com.anurag.healthplus.fragment;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anurag.healthplus.R;
import com.anurag.healthplus.activity.LoginActivity;
import com.anurag.healthplus.activity.MainActivity;
import com.anurag.healthplus.app.AppConfig;
import com.anurag.healthplus.app.AppController;

import java.util.HashMap;
import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedbackFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button button;
    RequestQueue requestQueue;
    String patientId;
    String urlToCancelAppointment = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/get_appointment_table.php";
    EditText editText;
    private ProgressDialog pDialog;
    View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FeedbackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedbackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feedback, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        MainActivity activity = (MainActivity) getActivity();
        patientId = activity.getIntent().getExtras().getString("patient_id");
        Log.d(ContentValues.TAG,"value of patient_id : " + patientId);
        button = (Button) view.findViewById(R.id.cancelbutton);
        editText = (EditText) view.findViewById(R.id.writeppointment);
       final String tag_string_req = "req_login";

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String apptId = editText.getText().toString().trim();
                Log.d(TAG,"value of apptId : " + apptId);
                if(!apptId.equals("")) {
                    StringRequest strReq = new StringRequest(Request.Method.POST,
                            AppConfig.URL_CANCEL, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "value Response: " + response);
                            Toast.makeText(getActivity(), "response:" + response, Toast.LENGTH_LONG).show();
                            hideDialog();

                            // Launch main activity
                            if (response.equals("0")) {
                                Toast.makeText(getActivity(), "Invalid credentials!", Toast.LENGTH_LONG).show();

                            }
                            else {
                                Toast.makeText(getActivity(), "SuccessFull!", Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Login Error: " + error.getMessage());
                            Toast.makeText(getActivity(),
                                    error.getMessage(), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            // Posting parameters to login url
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id", apptId);
                            params.put("pat_id",patientId);
                            return params;
                        }

                    };
                    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

                }
            }
        });

        return view;
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
