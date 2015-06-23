package com.playscape.publishingkit;

import android.app.Activity;
import com.playscape.ads.*;
import com.playscape.utils.L;

/**
 * Responsible for setting ad manager object and set listeners to it.
 *
 * @author VladimirM
 *
 */
public class NativeAdsLifeCycle extends AdsLifeCycle {

	private final InterstitialAdEventListener mInterstitialAdEventListener =
			new InterstitialAdEventListener() {

		@Override
		public void onShown(Kind kind) {
			L.d("InterstitialAd.onShown: " + kind.ordinal());
		}

		@Override
		public void onEnded(State state, Kind kind) {
			L.d("InterstitialAd.onEnded: " + state.ordinal() + ", " + kind.ordinal());
		}
	};

	private final VideoAdEventListener mVideoAdEventListener = new VideoAdEventListener() {

		@Override
		public void onEnded(State state, Kind kind) {
			L.d("VideoAd.onEnded: " + state.ordinal() + ", " + kind.ordinal());
		}
	};

	public NativeAdsLifeCycle(Activity activity) {
		super(activity);
	}

	@Override
	protected void onAdsManagerReady(AdManager adManager) {
		// Register listeners
		adManager.setInterstitialAdEventListener(mInterstitialAdEventListener);
		adManager.setVideoAdEventListener(mVideoAdEventListener);

		ExternalAdsMethods.setAdManager(adManager);
	}
}
