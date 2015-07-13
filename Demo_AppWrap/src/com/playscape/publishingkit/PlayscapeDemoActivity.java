package com.playscape.publishingkit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.playscape.api.ads.*;
import com.playscape.api.exchange.ExchangeManager;
import com.playscape.api.report.Report;
import com.playscape.utils.L;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by VladimirM on 6/19/15.
 * <p/>
 * This class shows how to use Playscape Exchange API
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
        HashMap<String, Double> map = new HashMap<String, Double>();
        for (int i = 0; i < size; i++) {
            map.put("Game_Mode_" + i, (double) i);
        }
        Report.reportLevelStarted("level1", map);
    }

    private void reportLevelCompleted() {
        HashMap<String, Double> map = new HashMap<String, Double>();
        for (int i = 0; i < size; i++) {
            map.put("Game_Mode_" + i, (double) i);
        }
        Report.reportLevelCompleted("level1", map);
    }

    private void reportLevelFailed() {
        HashMap<String, Double> map = new HashMap<String, Double>();
        for (int i = 0; i < size; i++) {
            map.put("Game_Mode_" + i, (double) i);
        }
        Report.reportLevelFailed("level1", map);
    }

    private void registerFlow() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("SaveTheButcher", 0);
        map.put("SlayDeckardPayne", 1);
        map.put("EscapeFromHell", 2);

        Report.registerFlow(mFlowId, map);
    }

    private void startNewFlow() {
        Report.startNewFlow(mFlowId);
    }

    private void reportFlowStep() {
        HashMap<String, Double> map = new HashMap<String, Double>();

        int x = new Random().nextInt(10); // just for get new values for different reports

        map.put("xp", (double) (1000 * x));
        map.put("gold", (double) (50 * x));
        map.put("souls", (double) (15 * x));

        Report.reportFlowStep(mFlowId, "SaveTheButcher", "noob", map);
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
        IntersitialAd.getInstnace().displayInterstitialAd(IntersititialAdKind.Both, "main-menu");
    }

    private void displayBannerAd() {
        BannerAd.getInstnace().displayBannerAd(BannerAlignment.TopMiddle, "top-middle");
    }

    private void hideBanner() {
        BannerAd.getInstnace().hideBannerAd();
    }

    private void displayVideo() {
        VideoAd.getInstnace().displayVideoAd(VideoKind.Incentivised, "video");
    }

    private void enableAds() {
        AdsDisplayingManager.enableAds();
    }

    private void disableAds() {
        AdsDisplayingManager.disableAds();
    }
}
