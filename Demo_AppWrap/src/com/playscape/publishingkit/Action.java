package com.playscape.publishingkit;

/**
 * Created by mac on 6/17/15.
 */
public enum Action {
    // ************** Report section **************
    // **** Wallet
    ReportWalletOperation("Report wallet operation"),
    // **** Level progression
    ReportLevelStarted("Report level started"),
    ReportLevelCompleted("Report level completed"),
    ReportLevelFailed("Report level failed"),
    // **** Custom flows
    RegisterFlow("Register flow"),
    StartNewFlow("Start new flow"),
    ReportFlowStep("Report flow step"),
    // **** GMAUX
    SetCustomVariable("Set custom variable"),
    GetCustomVariable("Get custom variable"),
    // **** Custom analytics
    ReportEvent("Report custom event"),
    // ************** Playscape Exchange Catalog **************
    ShowPlayscapeCatalog("Show Playscape catalog"),
    // ************** Ad mediation **************
    DisplayIntersitial("Display Intersitial"),
    DisplayBanner("Display Banner"),
    HideBanner("Hide Banner"),
    DisplayVideo("Display Video"),
    EnableAds("Enable Ads"),
    DisableAds("Disable Ads"),

    ;
    Action(String name) {
        this.mName = name;
    }

    private String mName;

    public String getName() {
        return mName;
    }
}
