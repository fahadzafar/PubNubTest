// Copyrights Fahad Zafar 2013
// Email: zoalord12@gmail.com

package com.example.pubnubtest;

import org.achartengine.ChartFactory;

import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

// The main activity that displays when the app is launched. The purpose of this
// demo is to measure and plot the collective time taken to publish a certain 
// number of bytes on to PubNub and for the subscriber to be updated with the 
// message. The benchmark sends 300, 600, 900 and 1200 bytes randomly generated
// to the PubNub cloud server and waits for the message to come back to it since
// the program is the subscriber to the channel it is publishing. It then 
// calculates the time difference to track how much time it took for the data
// to come through the PubNub pipeline. It uses the AChart api to plot the data
// after the experiment.

// DO NOT FORGET TO CHANGE THE TWO PUBNUB KEYS IN PubManager.java line#15, 
// line#16

public class MainActivity extends Activity {
	// The pubnub manager takes care of most of the heavy lifting for this demo.
	PubManager pubObject_;

	// Color set used for the output latency pie chart.
	private static int[] COLORS = new int[]{Color.GREEN, Color.BLUE,
			Color.MAGENTA, Color.CYAN};

	// The values input to the output latency pie chart
	public static double[] VALUES = new double[]{10, 11, 12, 13};

	// Names of the bins displayed on the pie chart used in the experiment.
	// These are the number of bytes sent to PubNub.
	private static String[] NAME_LIST = new String[]{"300", "600", "900",
			"1200"};

	// AChart API objects used to render the chart correctly.
	private static CategorySeries mSeries = new CategorySeries("");
	private static DefaultRenderer mRenderer = new DefaultRenderer();
	private static GraphicalView mChartView;
	static int call_counter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pubObject_ = new PubManager();

		// Attach callback to the button which launches the benchmark. It
		// randomly generates the data for the pre-defined bins and calculates
		// the latency.
		Button btnSendData = (Button) findViewById(R.id.btn_sendData);
		btnSendData.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				v.setEnabled(false);

				TextView userMessage = (TextView) findViewById(R.id.txtMessage);
				userMessage.setText("Click the chart to enable button.");

				for (int i = 1; i < 5; i++) {
					Benchmark.FillData(300 * i);
					Benchmark.SetStartTime(i - 1);
					pubObject_.publish(Benchmark.data_);
				}

			}
		});

		// Set up the chart parameters
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.WHITE);
		mRenderer.setChartTitleTextSize(20);
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setLabelsTextSize(15);
		mRenderer.setLegendTextSize(15);
		mRenderer.setShowLegend(false);
		mRenderer.setMargins(new int[]{20, 30, 15, 0});
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setStartAngle(90);
		mRenderer.setChartTitle("Byte Transfer Timings");
		
		// Just render a dummy chart for the first time. The benchmark will 
		// take responsibility for calling the UpdateChart function to update
		// the chart.
		RenderChart();
	}

	// Update the particular series whose results have been returneda and its
	// latency calculated.
	static void UpdateChart(int i) {
		mSeries.set(i, NAME_LIST[i] + "B-" + VALUES[i] + "ms", VALUES[i]);
		mChartView.repaint();
	}

	static void RenderChart() {
		// Render chart responsibility is handled here. First the previous
		// value are cleared.
		// mRenderer.removeAllRenderers();

		int total = mSeries.getItemCount();
		for (int i = total - 1; i > -1; i--) {
			mSeries.set(0, NAME_LIST[i] + "B-" + VALUES[i] + "ms", VALUES[i]);
		}

		// The new values are added to the chart engine.
		for (int i = 0; i < VALUES.length; i++) {
			mSeries.add(NAME_LIST[i] + "B-" + VALUES[i] + "ms", VALUES[i]);
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mSeries.getItemCount() - 1)
					% COLORS.length]);
			mRenderer.addSeriesRenderer(renderer);
		}

		// The chart view is updated.
		if (mChartView != null) {
			mChartView.repaint();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart_cnvs_chart);
			mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
			mRenderer.setClickEnabled(true);
			mRenderer.setSelectableBuffer(10);

			mChartView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Button btnSendData = (Button) findViewById(R.id.btn_sendData);
					btnSendData.setEnabled(true);
					TextView userMessage = (TextView) findViewById(R.id.txtMessage);
					userMessage
							.setText("Program assumes internet connectivity exists.");
				}
			});

			layout.addView(mChartView, new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		} else {
			mChartView.repaint();
		}

	}
}
