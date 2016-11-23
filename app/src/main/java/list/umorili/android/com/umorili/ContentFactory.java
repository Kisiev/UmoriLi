package list.umorili.android.com.umorili;

import android.content.Context;
import android.view.View;
import android.widget.TabHost;

public class ContentFactory implements TabHost.TabContentFactory{

    Context context;
    public ContentFactory(Context context){
        this.context = context;
    }

    @Override
    public View createTabContent(String s) {
        View view = new View(context);
        view.setMinimumWidth(0);
        view.setMinimumHeight(0);
        return view;
    }
}

