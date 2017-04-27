package com.anurag.healthplus.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by baji on 11/3/17.
 */

public class Utilities
{
	public Utilities()
	{

	}

//	public int getIdByDoctorName(String doctorName, String[] nameArray, int[] idArray)
//	{
//		for (int i = 0; i < nameArray.length; i++)
//		{
//			if (doctorName.equals(nameArray[i]))
//			{
//				return idArray[i];
//			}
//		}
//		return -1;
//	}

	public String[][] getDoctorsArrayFromJsonResponse(JSONArray response)
	{
		int count = 0;
		String[][] doctorsNameArray = new String[response.length()][8];

		int length = response.length();
		while (count < length)
		{
			try
			{

				JSONObject object = response.getJSONObject(count);
				doctorsNameArray[count][0] = object.getString("doctor_id");
				doctorsNameArray[count][1] = object.getString("doctor_fname");
				doctorsNameArray[count][2] = object.getString("doctor_lname");
				doctorsNameArray[count][3] = object.getString("doctor_address");
				doctorsNameArray[count][4] = object.getString("hospital_id");
				doctorsNameArray[count][5] = object.getString("specialization");
				doctorsNameArray[count][6] = object.getString("qualification");
				doctorsNameArray[count][7] = object.getString("fees");

				count++;

			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		return doctorsNameArray;
	}

	public String[][] getSpecializationArrayFromJsonResponse(JSONArray response)
	{
		int count = 0;
		String[][] specializationIdArray = new String[response.length()][2];
		int len = response.length();

		//Log.d("Util", "response : " + response);

		while (count < len)
		{
			try
			{
				JSONObject object = response.getJSONObject(count);

				specializationIdArray[count][0] = object.getString("specialization_id");
				specializationIdArray[count][1] = object.getString("specialization_name");
				count++;
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		return specializationIdArray;
	}

	public String[][] getHospitalArrayFromJsonResponse(JSONArray response)
	{
		int count = 0;
		String[][] hospitalIdArray = new String[response.length()][2];
		int len = response.length();

		//Log.d("Util", "response : " + response);

		while (count < len)
		{
			try
			{
				JSONObject object = response.getJSONObject(count);

				hospitalIdArray[count][0] = object.getString("hospital_id");
				hospitalIdArray[count][1] = object.getString("hospital_name");
				count++;
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		return hospitalIdArray;
	}

	public  String[] getTimeSlotFromJsonArray(JSONArray response)
	{
		int count = 0;
		String[] timeSlotArray = new String[response.length()];
		int len = response.length();

		//Log.d("Util", "response : " + response);

		while (count < len)
		{
			try
			{
		        JSONObject object = response.getJSONObject(count);

				timeSlotArray[count] = object.getString("");
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		return timeSlotArray;

	}

	public String[] convertDoctorDetailsToGetDoctorNameListForSpinner(String[][] doctorList,String hospitalId, String specialization)
	{
		String[] doctorNameList = new String[doctorList.length];
		int count = 0;
		//doctorNameList[0] = "Select";
        int j=0;


		while (count < doctorList.length)
		{
		//	Log.d(TAG,"value 1 not related in utilites : " + doctorList[count][4]);
		//	Log.d(TAG,"value 2 not related in utilites : " + doctorList[count][5]);
		//	Log.d(TAG,"value hospital in utilies : " + hospitalId);
		//	Log.d(TAG,"value spec in utilies : " + specialization);
                 if(doctorList[count][4].equals(hospitalId) && doctorList[count][5].equals(specialization))
				 {
					 doctorNameList[count] = doctorList[count][1] + " " + doctorList[count][2];
		//			 Log.d(TAG,"inside checking value : " + doctorList[count][4]);
		//			 Log.d(TAG,"inside checking value : " + doctorList[count][5]);
				 }
			else
				 {
					 doctorNameList[count] = "Select";
					 j++;
				 }

			count++;
		}
		count = 0 ;
		int i = 0;
		String[] doctorNameList1 = new String[doctorList.length - j];
		//Log.d(TAG,"value of j in utilites : " + j);
		while(count < doctorList.length)
		{
			if(doctorNameList[count].equals("Select"));

			else
			{
				doctorNameList1[i] = doctorNameList[count];
				i++;
			}
			count++;

		}


		return doctorNameList1;
	}

	public String[] convertSpecializationDetailsToGetSpecializationNameListForSpinner(String[][] specializationList)
	{
		String[] specializationNameList = new String[specializationList.length];
		int count = 0;

		while (count < specializationList.length)
		{

			specializationNameList[count] = specializationList[count][1];
			count++;
		}

		return specializationNameList;
	}

	public String[] convertHospitalDetailsToGetHospitalNameListForSpinner(String[][] hospitalsList)
	{
		String[] hospitalsNameList = new String[hospitalsList.length];
		int count = 0;

		while (count < hospitalsList.length)
		{

			hospitalsNameList[count] = hospitalsList[count][1];
			count++;
		}

		return hospitalsNameList;
	}

	public String getDoctorIdFromDoctorName(String doctorName, String[][] doctorList)
	{
		String doctorId =new String();
		String name;
		int count = 0;
		while (count < doctorList.length)
		{
			name = doctorList[count][1]+" "+doctorList[count][2];
			if (doctorName.equals(name))
			{
				doctorId = doctorList[count][0];
				break;
			}
			count++;
		}
		return doctorId;
	}

	public String getHospitalIdFromHospitalName(String[][] hospitalList, String hospitalName)
	{
		int count = 0;

		while(count < hospitalList.length)
		{
			if(hospitalList[count][1].equals(hospitalName))
			{
				break;
			}
			count++;
		}
			return hospitalList[count][0];
	}

}
