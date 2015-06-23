package com.playscape.ads;

import android.app.Activity;
import com.playscape.ads.AdsLifeCycle;
import com.playscape.ads.AdsLifeCycleFactoryAbstract;
import com.playscape.utils.L;
import com.playscape.publishingkit.NativeAdsLifeCycle;

public class AdsLifeCycleFactoryImpl extends AdsLifeCycleFactoryAbstract {

	@Override
	public AdsLifeCycle createLifeCycle(Activity activity) {
		if (L.isEnabled()) {
			L.d("Creating NativeAdsLifeCycle");
		}

		return new NativeAdsLifeCycle(activity);
	}

}
