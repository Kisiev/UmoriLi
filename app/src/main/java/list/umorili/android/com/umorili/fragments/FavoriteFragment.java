package list.umorili.android.com.umorili.fragments;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import list.umorili.android.com.umorili.ConstantManager;
import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.adapters.ClickListener;
import list.umorili.android.com.umorili.adapters.FavoriteFragmentAdapter;
import list.umorili.android.com.umorili.adapters.MainFragtentAdapter;
import list.umorili.android.com.umorili.entity.FavoriteEntity;
import list.umorili.android.com.umorili.entity.MainEntity;

@EFragment(R.layout.favorite_fragment)
public class FavoriteFragment extends Fragment {

    public static FavoriteFragmentAdapter adapter;
    public static RecyclerView recyclerView;
    public static Context context;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public static AppCompatActivity activity;
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    public static ActionMode actionMode;
    @ViewById(R.id.name_item_favorite)
    TextView name_item;

    FlowContentObserver observer = new FlowContentObserver();

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_layout);
        Button okButton = (Button) dialog.findViewById(R.id.button_OK);
        final Button cancelButton = (Button) dialog.findViewById(R.id.button_Cancel);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteEntity.deleteAllFavorite();
                MainEntity.updateFavoriteAll(false);
                MainFragment.recyclerView.setAdapter(new MainFragtentAdapter(MainEntity.listUmor()));
                loadEntity();
                dialog.dismiss();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.favorite_fragment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container_favorite);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadEntity();
            }
        });
        observer.registerForContentChanges(getActivity(), FavoriteEntity.class);
        observer.addOnTableChangedListener(new FlowContentObserver.OnTableChangedListener() {
            @Override
            public void onTableChanged(@Nullable Class<? extends Model> tableChanged, BaseModel.Action action) {
                loadEntity();
            }
        });
        context = getActivity();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_bt:
                showDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_activity, menu);
    }
    public void loadEntity() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getLoaderManager().restartLoader(ConstantManager.ID_FRAGMENT, null, new LoaderManager.LoaderCallbacks<List<FavoriteEntity>>() {
                    @Override
                    public Loader<List<FavoriteEntity>> onCreateLoader(int id, Bundle args) {
                        final AsyncTaskLoader<List<FavoriteEntity>> loader = new AsyncTaskLoader<List<FavoriteEntity>>(getActivity()) {
                            @Override
                            public List<FavoriteEntity> loadInBackground() {
                                return FavoriteEntity.selectedALL();
                            }
                        };
                        loader.forceLoad();
                        return loader;
                    }

                    @Override
                    public void onLoadFinished(Loader<List<FavoriteEntity>> loader, List<FavoriteEntity> data) {
                        adapter = new FavoriteFragmentAdapter(data, new ClickListener() {
                            @Override
                            public void onItemSelected(int position) {
                                if (actionMode != null) {
                                    toggleSelection(position);
                                }
                            }

                            @Override
                            public boolean onItemLongSelected(int position) {
                                if (actionMode == null) {
                                    activity = (AppCompatActivity) getActivity();
                                    actionMode = activity.startSupportActionMode(actionModeCallback);
                                }
                                toggleSelection(position);
                                return true;
                            }
                        }, getActivity());

                        recyclerView.setAdapter(adapter);
                        mSwipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onLoaderReset(Loader<List<FavoriteEntity>> loader) {
                    }
                });
            }
        });


    }

    private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.contextual_action_bar, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.menu_remove:
                    adapter.removeItems(adapter.getSelectedItems());
                    mode.finish();

                    return true;
                case R.id.menu_selected_all:
                    adapter.clearSelection();
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        adapter.toggleSelection(i);
                    }
                    return true;
                case R.id.menu_item_share:
                    if (adapter.getSelectedItems().size() == 1) {
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText(getString(R.string.copy), adapter.selectItem(adapter.getSelectedItems()));
                        clipboard.setPrimaryClip(clip);
                    } else Toast.makeText(getActivity(), R.string.error_copy, Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelection();
            actionMode = null;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        loadEntity();
        Log.d("ONSTART FAVORITE", "OnStart");
    }
}
