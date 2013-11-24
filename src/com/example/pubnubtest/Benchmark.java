// Copyrights Fahad Zafar 2013
// Email: zoalord12@gmail.com
package com.example.pubnubtest;

// This class tracks the timings of certain executions. The caller manages the 
// calling sequence of the SetStartTime and the GetTimeDifference function.
public class Benchmark {

	// The data that will be sent to PubHub and returned. Size is determined at
	// runtime.
	public static String data_;
	
	// The time variable, initialized to -1. If the calling sequence is 
	// incorrect or there is a reset requited, it is set to -1.
	public static long startTime_[] = {-1, -1, -1, -1};

	// Populate the data using random characters based on the input size in the
	// buffer.
	public static void FillData(int size) {
		data_ = PubNubHelper.RandomString(size);
	}

	// Save the start time.
	public static void SetStartTime(int index) {
		startTime_[index] = System.currentTimeMillis();
	}
	
	// Get the time difference.
	public static long GetTimeDifference(int index) {
		if (startTime_[index] == -1)
			return -1;
		else {
			return System.currentTimeMillis() - startTime_[index];
		}
	}
	
	// Reset the time.
	public static void ResetAll() {
		startTime_[3] = startTime_[2] = startTime_[1] = startTime_[0] = -1;
	}
}
