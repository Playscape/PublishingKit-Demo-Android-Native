package com.playscape.publishingkit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.playscape.api.ads.AdsDisplayingManager;
import com.playscape.api.ads.BannerAd;
import com.playscape.api.ads.IntersitialAd;
import com.playscape.api.ads.VideoAd;
import com.playscape.api.report.Report;
import com.playscape.exchange.ExchangeManager;
import com.playscape.utils.L;
import com.playscape.utils.MoDi;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by VladimirM on 6/19/15.
 *
 * This class shows how to use Playscape Exchange API
 *
 */
public class PlayscapeDemoActivity extends Activity {

    private static final String TAG = PlayscapeDemoActivity.class.getSimpleName();

    // listview with items for calling API methods
    private ListView listView;

    // we'll use it just for report events with different values
    private static int amount = 0;

    // its needed for arrays with parameters for reporting
    private int size = 3;

    // flow id for Flow reporting
    private String mFlowId = "Quests";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ExchangeManager.getInstance().init(getApplication(),
                getApplicationContext(),
                MoDi.getInjector(this).getInstance(ScheduledExecutorService.class));

        listView = (ListView) findViewById(R.id.list);

        ListViewAdapter adapter = new ListViewAdapter(this, Action.values());
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                executeAPIMethod((Action) view.getTag());
            }
        });
    }

    private void executeAPIMethod(Action action) {
        switch (action) {
// ************** Report section **************
// **** Wallet
            case ReportWalletOperation:
                reportWalletOperation();
                break;
            case ReportLevelStarted:
                reportLevelStarted();
                break;
            case ReportLevelCompleted:
                reportLevelCompleted();
                break;
            case ReportLevelFailed:
                reportLevelFailed();
                break;
// **** Custom flows
            case RegisterFlow:
                registerFlow();
                break;
            case StartNewFlow:
                startNewFlow();
                break;
            case ReportFlowStep:
                reportFlowStep();
                break;
// **** GMAUX
            case SetCustomVariable:
                setCustomVariable();
                break;
            case GetCustomVariable:
                getCustomVariable();
                break;
// **** Custom analytics
            case ReportEvent:
                reportEvent();
                break;
// ************** Playscape Exchange Catalog **************
            case ShowPlayscapeCatalog:
                showPlayscapecatalog();
                break;
// ************** Ad mediation **************
            case DisplayIntersitial:
                displayIntersitial();
                break;
            case DisplayBanner:
                displayBannerAd();
                break;
            case HideBanner:
                hideBanner();
                break;
            case DisplayVideo:
                displayVideo();
                break;
            case EnableAds:
                enableAds();
                break;
            case DisableAds:
                disableAds();
                break;
        }
    }

    /* ************** Wrappers for Report, BannerAd, IntersitialAd, VideoAd,
       ************** and AdsDisplayingManager classes */

    private void reportWalletOperation() {
        int walletOperation = 0; // Deposit = 0; Withdraw = 1
        int walletResult = 0; // Success = 0, Failed = 1, Cancel = 2
        String dealType = "SingleItem";
        Report.reportWalletOperation(walletOperation, dealType, "transactionID", 50.0d, "currency",
                    "source", "flow", "step", "item", walletResult, "reason");
    }

    private void reportLevelStarted() {
        Report.reportLevelStarted("level1", size, getKeysArray(size), getDoubleValueArray(size));
    }

    private void reportLevelCompleted() {
        Report.reportLevelCompleted("level1", size, getKeysArray(size), getDoubleValueArray(size));
    }

    private void reportLevelFailed() {
        Report.reportLevelFailed("level1", size, getKeysArray(size), getDoubleValueArray(size));
    }

    private void registerFlow() {
        FlowStep[] steps = FlowStep.values();
        String[] stepsStr = new String[steps.length];
        int[] values = new int[steps.length];

        for (FlowStep step : steps) {
            stepsStr[step.ordinal()] = step.getName();
            values[step.ordinal()] = step.ordinal();
        }

        Report.registerFlow(mFlowId, size, stepsStr, values);
    }

    private void startNewFlow() {
        Report.startNewFlow(mFlowId);
    }

    private void reportFlowStep() {
        FlowStep step = FlowStep.SaveTheButcher;

        int size = step.getDetailsValues().size();
        String[] stepsStr = new String[size];
        double[] values = new double[size];

        for (int i = 0; i < size; i++) {
            stepsStr[i] = step.getDetailsNames().get(i);
            values[i] = step.getDetailsValues().get(i);
        }
        Report.reportFlowStep(mFlowId, step.getName(), step.getStatus(), stepsStr.length, stepsStr, values);
    }

    private void setCustomVariable() {
        amount++;
        Report.setCustomVariable("NewCustomVariableKey_" + amount, "NewCustomVariableValue_" + amount);
    }

    private void getCustomVariable() {
        String variable = Report.getCustomVariable("NewCustomVariableKey_" + amount);
        L.d("custom variable: " + variable);
    }

    private void reportEvent() {
        amount++;
        Report.reportEvent("custom_event_" + amount);
    }

    private void showPlayscapecatalog() {
        ExchangeManager.getInstance().showCatalog();
    }

    private void displayIntersitial() {
        IntersitialAd.getInstnace().displayInterstitialAd(2, "main-menu");
    }

    private void displayBannerAd() {
        BannerAd.getInstnace().displayBannerAd(0, "top-middle");
    }

    private void hideBanner() {
        BannerAd.getInstnace().hideBannerAd();
    }

    private void displayVideo() {
        VideoAd.getInstnace().displayVideoAd(0, "video");
    }

    private void enableAds() {
        AdsDisplayingManager.enableAds();
    }

    private void disableAds() {
        AdsDisplayingManager.disableAds();
    }

    private double[] getDoubleValueArray(int size) {
        double[] values = new double[size];

        for (int i = 0; i < size; i++) {
            values[i] = i;
        }

        return values;
    }

    private String[] getKeysArray(int size) {
        String[] keys = new String[size];

        for (int i = 0; i < size; i++) {
            keys[i] = "Game_Mode_" + i;
        }

        return keys;
    }

    // Helper for testing Reporting Custom Flows
    private enum FlowStep {
        SaveTheButcher("SaveTheButcher", "noob", 5),
        SlayDeckardPayne("SlayDeckardPayne", "master", 2),
        EscapeFromHell("EscapeFromHell", "noob", 0.25f),
        ;
        private String mName;
        private String mStatus;

        private ArrayList<String> mDetailsNames;
        private ArrayList<Integer> mDetailsValues;

        FlowStep(String name, String status, float x) {
            mName = name;
            mStatus = status;

            mDetailsNames = new ArrayList<String>();
            mDetailsValues = new ArrayList<Integer>();

            mDetailsNames.add("xp");
            mDetailsValues.add((int) (1000 * x));

            mDetailsNames.add("gold");
            mDetailsValues.add((int) (50 * x));

            mDetailsNames.add("souls");
            mDetailsValues.add((int) (15 * x));

        }

        public String getName() {
            return mName;
        }

        public String getStatus() {
            return mStatus;
        }

        public ArrayList<String> getDetailsNames() {
            return mDetailsNames;
        }

        public ArrayList<Integer> getDetailsValues() {
            return mDetailsValues;
        }
    }
}
