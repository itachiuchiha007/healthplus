package com.anurag.healthplus.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anurag.healthplus.R;
import com.anurag.healthplus.activity.MainActivity;
import com.anurag.healthplus.utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class   HomeFragment extends Fragment
{
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	Button chooseDateButton,bookAppointment;
	Calendar myCalendar;
	EditText editTextDate;
	android.widget.Spinner spinnerSpecialization, spinnerHospital, spinnerDoctor, spinnerTimeSlot;
	Utilities utils;
	String urlGetSpecializationSpinner = "http://athena.nitc.ac"
			+ ".in/balmukund_b130168cs/healthplus/get_specialization_spinner.php";
	String urlGetHospitalSpinner = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/get_hospital_spinner.php";
	String urlGetDoctorSpinner = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/get_doctor_spinner.php";
	String urlGetBookAppointment = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/book.php";
	String urlGetTimeSpinner = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/book.php";
	String urlGetAppointmentId = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/get_appoint_id.php";
	String[][] specializationDetails, hospitalDetails, doctorDetails;
	String[] timelist,getHospitalIdList;
	RequestQueue requestQueue;
	String hospitalId,hospitalname;
	String specialization;
	String appointId,doctorId = "",patientId;
	CalendarView calendar;

	int year,month,day;

	boolean flag[] = new boolean[4];
	String date;
	static final int DATE_DIALOG_ID = 999;
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	public HomeFragment()
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

		if (context instanceof OnFragmentInteractionListener)
		{
			mListener = (OnFragmentInteractionListener) context;
		}
		else
		{
//			throw new RuntimeException(context.toString()
//					+ " must implement OnFragmentInteractionListener");
		}
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
							 Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		chooseDateButton = (Button) view.findViewById(R.id.chooseDateButton);
		bookAppointment = (Button) view.findViewById(R.id.buttonBookAppointment);
		editTextDate = (EditText) view.findViewById(R.id.editTextDate);
		spinnerSpecialization = (android.widget.Spinner) view.findViewById(R.id.spinnerSpecialization);
		spinnerHospital = (android.widget.Spinner) view.findViewById(R.id.spinnerHospital);
		spinnerDoctor = (android.widget.Spinner) view.findViewById(R.id.spinnerDoctor);
		requestQueue = Volley.newRequestQueue(getContext());
		spinnerTimeSlot = (Spinner) view.findViewById(R.id.spinnerTimeSlot);
		calendar = (CalendarView) view.findViewById(R.id.calendar);


		MainActivity activity = (MainActivity) getActivity();
		patientId = activity.getIntent().getExtras().getString("patient_id");
		Log.d(TAG,"value of patient_id : " + patientId);

 //// THIS IS TO GET APPOINTMENTID WHICH IS NOT WORKING PLZ CHECK
 	    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlGetAppointmentId, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				//		JSONObject object = response;
				//		appointId = response.getString("response");
				try {
					Log.d(TAG,"value of response to get appoint : " + response.getString("response"));
					appointId = response.getString("response");
					Log.d(TAG,"value of appointmentId : " + appointId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//		Log.d(TAG,"value appoint-id : " + appointId);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "VolleyError: " + error);
			}
		});
		requestQueue.add(jsonObjectRequest);


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
				spinnerSpecialization.setPrompt("Select Specialization");
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
		setBoolean(3,false);
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
				spinnerHospital.setPrompt("Select Specialization");
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
				Log.d(TAG,"value hospital name : " + hospitalname);

				if(hospitalname!= "Select") {
					utils = new Utilities();
					hospitalId = utils.getHospitalIdFromHospitalName(hospitalDetails, hospitalname);

					Log.d(TAG, "value HospitalId : " + hospitalId);
					setBoolean(1, true);
					doctorSpinner();
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

//		JSONObject jsonObject = new JSONObject();
//		try {
//			jsonObject.put("patient_id","P01");
//			jsonObject.put("doctor_id","D01");
//			jsonObject.put("dates","2017-03-04");
//			jsonObject.put("timeslot","00:10:09");
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}

//		jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, urlGetDoctorSpinner,
//				null, new Response.Listener<JSONArray>()
//		{
//			@Override
//			public void onResponse(JSONArray response)
//			{
//				utils = new Utilities();
//				Log.d(TAG, "valueinside doctorspinner : " + hospitalId);
//				doctorDetails = utils.getDoctorsArrayFromJsonResponse(response);
//
//
//				String[] getDoctorNameListForSpinner = utils
//						.convertDoctorDetailsToGetDoctorNameListForSpinner(doctorDetails);
//				int len = getDoctorNameListForSpinner.length;
//				String[] temp = new String[len + 1];
//
//				temp[0] = "Select";
//				System.arraycopy(getDoctorNameListForSpinner, 0, temp, 1, len);
//
//				ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout
//						.simple_spinner_item, temp);
//
//				spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//				spinnerDoctor.setPrompt("Select Specialization");
//				spinnerDoctor.setAdapter(spinnerAdapter);
//			}
//		}, new Response.ErrorListener()
//		{
//			@Override
//			public void onErrorResponse(VolleyError error)
//			{
//				Log.e(TAG, "VolleyError: " + error);
//			}
//		});
//
//		requestQueue.add(jsonArrayRequest);
//		spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//		{
//			@Override
//			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
//			{
////				Log.d(TAG, "value : " + adapterView.getItemAtPosition(i).toString());
////				Log.d(TAG, "value : " + spinnerSpecialization.getSelectedItem().toString());
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> adapterView)
//			{
//
//			}
//		});

// CALENDAR IS NOT WORKING PLZ CHECK
//		chooseDateButton.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(final View view)
//			{
//				myCalendar = Calendar.getInstance();
//
//				final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
//				{
//					@Override
//					public void onDateSet(DatePicker view, int year, int monthOfYear,
//										  int dayOfMonth)
//					{
//						// TODO Auto-generated method stub
//						myCalendar.set(Calendar.YEAR, year);
//						myCalendar.set(Calendar.MONTH, monthOfYear);
//						myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//						updateLabel();
//					}
//				};
//
//				Context appContext = view.getContext();
//				new DatePickerDialog(appContext, date, myCalendar.get(Calendar.YEAR),
//						myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//
//				editTextDate.setOnClickListener(new View.OnClickListener()
//				{
//					@Override
//					public void onClick(View v)
//					{
//						// TODO Auto-generated method stub
//						Context appContext = view.getContext();
//						new DatePickerDialog(appContext, date, myCalendar.get(Calendar.YEAR),
//								myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//					}
//				});
//
//				spinnerTimeSlot.setVisibility(View.VISIBLE);
//			}
//		});

//		calendar = Calendar.getInstance();
//		year = calendar.get(Calendar.YEAR);
//		month = calendar.get(Calendar.MONTH);
//		day = calendar.get(Calendar.DAY_OF_MONTH);
//		showdate(year,month+1,day);



//		jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, urlGetTimeSpinner, null, new Response.Listener<JSONArray>() {
//			@Override
//			public void onResponse(JSONArray response) {
//				utils = new Utilities();
//				timelist = new String[] {"9 - 10","10 - 11","11 - 12","4-5","5-6","6-7"};
//				ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout
//						.simple_spinner_item, timelist);
//
//				spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//				spinnerTimeSlot.setAdapter(spinnerAdapter);
//
//
//			}}
//		, new Response.ErrorListener()
//		{
//			@Override
//			public void onErrorResponse(VolleyError error)
//			{
//				Log.e(TAG, "VolleyError: " + error);
//			}
//		});
//		requestQueue.add(jsonArrayRequest);


//		spinnerTimeSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//		{
//			@Override
//			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
//			{
////				Log.d(TAG, "value : " + adapterView.getItemAtPosition(i).toString());
////				Log.d(TAG, "value : " + spinnerSpecialization.getSelectedItem().toString());
////				Log.d(TAG, "value : " + myCalendar.getTime());
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> adapterView)
//			{
//
//			}
//		});


//		chooseDateButton .setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				showDialogue(DATE_DIALOG_ID);
//			}
//		});
//
//		final Calendar c = Calendar.getInstance();
//		month = c.get(Calendar.MONTH);
//		year = c.get(Calendar.YEAR);
//		day = c.get(Calendar.DAY_OF_MONTH);

//		updateDisplay();
		calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {

				editTextDate.setText(new StringBuilder()
				.append(i).append("-").append(0)
				.append(i1+1).append("-")
				.append(i2));
				date = editTextDate.getText().toString();
				Log.d(TAG,"edittextdate : " + date);
			    setBoolean(3,true);
			}
		});


		bookAppointment.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View view)
			{
				final JSONObject jsonObject = new JSONObject();
				try {


					Log.d(TAG,"value of doctor_id : " + doctorId);
				    if(flag[3]&&flag[2]&&flag[1]&&flag[0]){
						jsonObject.put("patient_id",patientId);
						jsonObject.put("doctor_id", doctorId);
						jsonObject.put("dates", date);
						Log.d(TAG, "value of date : " + date);
						jsonObject.put("timeslot", "00:11:09");
						jsonObject.put("sn",appointId);
						Log.d(TAG, "jsonobject : " + jsonObject);

					}
					else
					{

						Toast.makeText(getContext(),"PLZ PROVIDE ALL INPUT",Toast.LENGTH_LONG).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			if(jsonObject.length()!=0) {
				JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlGetBookAppointment, jsonObject, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG,"value of response : " + response);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});
				requestQueue.add(jsonObjectRequest);
			}

			}
		});


		return view;
	}



//	private void updateDisplay()
//	{
//		this.editTextDate.setText(new StringBuilder()
//				// Month is 0 based so add 1
//				.append(month + 1).append("-")
//				.append(day).append("-")
//				.append(year).append(" ")););
//	}
//
//private DatePickerDialog.OnDateSetListener mDateSetListener =
//			new DatePickerDialog.OnDateSetListener() {
//				public void onDateSet(DatePicker view, int years,
//									  int monthOfYear, int dayOfMonth) {
//					year = years;
//					month = monthOfYear;
//					day = dayOfMonth;
//					updateDisplay();
//				}
//			};
//	@Override
//	protected DatePickerDialog onCreateDialog(int id) {
//		switch (id) {
//			case DATE_DIALOG_ID:
//				return new DatePickerDialog(getView().getContext(), mDateSetListener,year, month, day);
//		}
//		return null;
//	}

//	private void updateLabel()
//	{
//		String myFormat = "dd/MM/yy"; //In which you need put here
//		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//		editTextDate.setText(sdf.format(myCalendar.getTime()));
//	}


//	private void showdate(int year,int month,int day)
//	{
//		editTextDate.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
//	}

    private void doctorSpinner()
	{
		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, urlGetDoctorSpinner,
				null, new Response.Listener<JSONArray>()
		{
			@Override
			public void onResponse(JSONArray response)
			{
				utils = new Utilities();
				Log.d(TAG, "valueinside doctorspinner1 : " + hospitalId);
				doctorDetails = utils.getDoctorsArrayFromJsonResponse(response);


				String[] getDoctorNameListForSpinner = utils
						.convertDoctorDetailsToGetDoctorNameListForSpinner(doctorDetails,hospitalId,specialization);
				int len = getDoctorNameListForSpinner.length;
				String[] temp = new String[len + 1];

				temp[0] = "Select";
				System.arraycopy(getDoctorNameListForSpinner, 0, temp, 1, len);

				ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout
						.simple_spinner_item, temp);

				spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
				spinnerDoctor.setPrompt("Select Specialization");
				spinnerDoctor.setAdapter(spinnerAdapter);
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
		spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
			{
//				Log.d(TAG, "value : " + adapterView.getItemAtPosition(i).toString());
//				Log.d(TAG, "value : " + spinnerSpecialization.getSelectedItem().toString());
			    utils = new Utilities();
				Log.d(TAG,"value of spinner doctor : " + spinnerDoctor.getSelectedItem().toString());
				if(!spinnerDoctor.getSelectedItem().toString().equals("Select")){
					doctorId = utils.getDoctorIdFromDoctorName(spinnerDoctor.getSelectedItem().toString(), doctorDetails);
					Log.d(TAG, "value of doctorid : " + doctorId);
					setBoolean(2,true);
				}
				else
				{
					setBoolean(2,false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView)
			{

			}
		});

	}

	private void setBoolean(int index,Boolean bool)
	{
		flag[index] = bool;
	}


	@Override
	public void onDetach()
	{
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
	public interface OnFragmentInteractionListener
	{
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}

}
