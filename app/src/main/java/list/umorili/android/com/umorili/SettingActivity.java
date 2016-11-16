package list.umorili.android.com.umorili;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import list.umorili.android.com.umorili.fragments.SettingFragment;

@EActivity(R.layout.setting_fragment)
public class SettingActivity extends AppCompatActivity {

    private void replaceFragment(Fragment fragment) {
        String backStackName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStackName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStackName) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.setting_liner, fragment, backStackName);
            ft.addToBackStack(backStackName);
            ft.commit();
        }
    }


    @AfterViews
    public void main(){
        SettingFragment settingFragment = new SettingFragment();
        replaceFragment(settingFragment);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.setting_liner);
                if (f == null) {
                    onBackPressed();
                }
            }
        });

    }


}
