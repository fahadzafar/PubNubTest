// Copyrights Fahad Zafar 2013
// Email: zoalord12@gmail.com

package com.example.pubnubtest;

import java.util.Random;

// This class provides helper functions. 
public class PubNubHelper {
	// Generate a random string using the alphabet provided. The lenght of the
	// return string is input to the function.
	public static String RandomString(int length) {
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();

		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		}
		return sb.toString();

	}
}
