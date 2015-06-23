package com.playscape.publishingkit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.playscape.exchange.ExchangeManager;
import com.playscape.utils.MoDi;
import com.playscape.api.ads.*;
import com.playscape.api.report.*;

import java.util.concurrent.ScheduledExecutorService;

public class PlayscapeDemoActivity extends Activity {

    private ListView listView;

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
                reportLevelSrarted();
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

    private void reportWalletOperation() {

    }

    private void reportLevelSrarted() {

    }

    private void reportLevelCompleted() {

    }

    private void reportLevelFailed() {

    }

    private void registerFlow() {

    }

    private void startNewFlow() {

    }

    private void reportFlowStep() {

    }

    private void setCustomVariable() {

    }

    private void getCustomVariable() {

    }

    private void reportEvent() {

    }

    private void showPlayscapecatalog() {
        ExchangeManager.getInstance().showCatalog();
    }

    private void displayIntersitial() {
       IntersitialAd.displayInterstitialAd(2, "main-menu");
    }

    private void displayBannerAd() {
        BannerAd.displayBannerAd(0, "top-middle");
    }

    private void hideBanner() {
        BannerAd.hideBannerAd();
    }

    private void displayVideo() {
        VideoAd.displayVideoAd(0, "video");
    }

    private void enableAds() {
        AdsDisplayingManager.enableAds();
    }

    private void disableAds() {
        AdsDisplayingManager.disableAds();
    }
}
