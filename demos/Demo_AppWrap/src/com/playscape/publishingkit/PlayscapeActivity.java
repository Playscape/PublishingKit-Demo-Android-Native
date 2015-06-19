package com.playscape.publishingkit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.playscape.exchange.ExchangeManager;
import com.playscape.utils.MoDi;

import java.util.concurrent.ScheduledExecutorService;

public class PlayscapeActivity extends Activity {

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
            case ShowPlayscapeCatalog:
                showPlayscapecatalog();
                break;
        }
    }

    private void showPlayscapecatalog() {
        // Show Alert
        Toast.makeText(getApplicationContext(), "show catalog", Toast.LENGTH_SHORT).show();
        ExchangeManager.getInstance().showCatalog();
    }

}
