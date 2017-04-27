package com.anurag.healthplus.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.anurag.healthplus.R;
import com.anurag.healthplus.utils.Utilities;

import org.json.JSONArray;

//import static com.anurag.healthplus.R.id.oldButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment2 extends Fragment
{
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	TextView patientId;
	RadioGroup groupGender, groupPatientType;
	Button getApptointment, getDoctor, getSpecialization, insertPatientIntoTableButton;
	Spinner doctorSpinner, specializationSpinner, getTimeSpinner;
	Utilities utils;
	CalendarView calendar;
	int flag1, flag2;
	RequestQueue requestQueue;

	//String url = "192.168.0.105/html/healthplus/search_doctor.php";
	String urlToGetDoctor = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/search_doctor.php";
	String urltoinsertpatient = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/insert_patient.php";
	String getUrlToGetSpecialization = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/get_specialization.php";
	String urlToGetPatient = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/get_patient_detail_on_clicking_old_patient.php";
	String[][] doctorDetials;
	String[][] specializationDetials;
	String[][] patientDetails;

	View view;

	public static final String TAG = "HomeFragment";

	private OnFragmentInteractionListener mListener;

	public HomeFragment2()
	{
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
	public static HomeFragment newInstance(String param1, String param2)
	{
		HomeFragment fragment = new HomeFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
		{
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 final Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.fragment_home, container, false);
		patientId = (TextView) view.findViewById(R.id.patient_id);
		utils = new Utilities();
		requestQueue = Volley.newRequestQueue(getContext());

		//flag initialization
		flag1 = 0;    // to decide old patient or new patient
		flag2 = 0;

		// radio group to select old patient or new patients
		//to get specializtion.
		getSpecialization.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				final JsonArrayRequest jsarrayRequest = new JsonArrayRequest(Request.Method.GET,
						getUrlToGetSpecialization,            //it is correct ok
						null,
						new Response.Listener<JSONArray>()
						{
							@Override
							public void onResponse(JSONArray response)
							{
								specializationDetials = utils.getSpecializationArrayFromJsonResponse(response);
								String[] getSpecializationNameListForSpinner = utils.convertSpecializationDetailsToGetSpecializationNameListForSpinner(specializationDetials);
								ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getSpecializationNameListForSpinner);
								spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
								specializationSpinner.setPrompt("Select Specialization");
								specializationSpinner.setAdapter(spinnerAdapter);
							}
						},
						new Response.ErrorListener()
						{
							@Override
							public void onErrorResponse(VolleyError error)
							{
								Log.e(TAG, "VolleyError: " + error);
							}
						}
				);
				requestQueue.add(jsarrayRequest);
			}
		});

		specializationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
			{
				Log.d(TAG, "value : " + adapterView.getItemAtPosition(i).toString());
				Log.d(TAG, "value : " + specializationSpinner.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView)
			{

			}
		});
//process of specialization spinner finished


		doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
			{

				Log.d(TAG, "value : " + adapterView.getItemAtPosition(i).toString());
				Log.d(TAG, "value : " + doctorSpinner.getSelectedItem().toString());

			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView)
			{

			}
		});

		//to get doctor; on clicking get doctor
		getDoctor.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Log.d(TAG, "getDoctor clicked");
				final JsonArrayRequest jsarrayRequest = new JsonArrayRequest(Request.Method.GET,
						urlToGetDoctor, null,
						new Response.Listener<JSONArray>()
						{
							@Override
							public void onResponse(JSONArray response)
							{
								Log.d(TAG, "" + response);
/*								try
								{
									Log.d(TAG, "" + response.get(0));
								}
								catch (JSONException e)
								{
									e.printStackTrace();
								}*/
								doctorDetials = utils.getDoctorsArrayFromJsonResponse(response);

								Log.d(TAG, "doctorDetails => id : " + doctorDetials[0][0]);

/*								String[] getDoctorNameListForSpinner = utils.convertDoctorDetailsToGetDoctorNameListForSpinner(doctorDetials, specializationDetials, specializationSpinner.getSelectedItem().toString());
								ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, getDoctorNameListForSpinner);
								spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
								doctorSpinner.setPrompt("Select Doctor");
								doctorSpinner.setAdapter(spinnerAdapter);*/

							}
						},
						new Response.ErrorListener()
						{
							@Override
							public void onErrorResponse(VolleyError error)
							{
								Log.e(TAG, "VolleyError: " + error);
							}
						}
				);

			}
		});

		//get date for appointment
		calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
		{
			@Override
			public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2)
			{
				Log.d(TAG, "value : " + calendar.getDate());
				getTimeSpinner.setVisibility(view.VISIBLE);
				String[] timeList = new String[]{"morning", "afternon", "evening"};
				ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, timeList);
				spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
				getTimeSpinner.setPrompt("Select Time");
				getTimeSpinner.setAdapter(spinnerAdapter);


			}
		});

		//time spinner is created here
		getTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
			{
				Log.d(TAG, "value : " + getTimeSpinner.getSelectedItem().toString());

			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView)
			{

			}
		});

//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            if(flag1 == 0){
//                jsonObject.put("patient_id",patientId.getText().toString().trim());
//            }
//            jsonObject.put("doctor_id",utils.getDoctorIdFromDoctorName(doctorSpinner.getSelectedItem().toString(),doctorDetials));
//            jsonObject.put("user_id","1");
//            jsonObject.put("appointment_id","1");
//            jsonObject.put("token",getTimeSpinner.getSelectedItem().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


		// to get appointment
		getApptointment.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{


			}
		});

		// Inflate the layout for this fragment
		return view;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri)
	{
		if (mListener != null)
		{
			mListener.onFragmentInteraction(uri);
		}
	}


	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		mListener = null;
	}


	public interface OnFragmentInteractionListener
	{
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}
}
