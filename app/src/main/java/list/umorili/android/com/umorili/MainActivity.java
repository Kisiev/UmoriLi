package list.umorili.android.com.umorili;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TabHost;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import list.umorili.android.com.umorili.fragments.FavoriteFragment;
import list.umorili.android.com.umorili.fragments.MainFragment;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private final static String TAG1 = "tag1";
    private final static String TAG2 = "tag2";

    @ViewById
    TabHost tabHost;
    @AfterViews
    void main (){
        tabHost.setup();
        // Вкладка главная
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(TAG1);
        tabSpec.setIndicator(getString(R.string.main_tab));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);
        // Вкладка избранное
        tabSpec = tabHost.newTabSpec(TAG2);
        tabSpec.setIndicator(getString(R.string.favorite_tab));
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);
        // по умолчанию показывать главную вкладку
        tabHost.setCurrentTabByTag(TAG1);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                switch (s){
                    case TAG1:
                        MainFragment mainFragment = new MainFragment();
                        replaceFragment(mainFragment, R.id.tab1);
                        Log.d("BIL", "BIVAL");
                        break;
                    case TAG2:
                        FavoriteFragment favoriteFragment = new FavoriteFragment();
                        replaceFragment(favoriteFragment, R.id.tab2);
                        Log.d("BIL", "BIVAL");
                        break;
                }
            }
        });

    }
    private void replaceFragment(Fragment fragment, int id) {
        String backStackName = fragment.getClass().getName();

        Log.d("BIL", "BIVAL");
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStackName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStackName) == null) {

            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(id, fragment, backStackName);
            ft.addToBackStack(backStackName);
            ft.commit();Log.d("BIL", "BIVAL");
        }
    }
}
