package com.drone.imavis.mvp.ui.tabs.flyplans;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.ui.base.BaseFragment;
import com.drone.imavis.mvp.ui.flyplanner.FlyplannerActivity;
import com.drone.imavis.mvp.ui.tabs.ProjectsFlyplansActivity;
import com.drone.imavis.mvp.ui.tabs.flyplanAddOrEdit.FlyplanAction;
import com.drone.imavis.mvp.ui.tabs.flyplanAddOrEdit.FlyplanAddOrEditActivity;
import com.drone.imavis.mvp.util.DialogUtil;
import com.drone.imavis.mvp.util.swipelistview.SwipeActionButtons;
import com.drone.imavis.mvp.util.swipelistview.SwipeItemOnClickListener;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by adigu on 08.05.2017.
 */

public class FlyplansFragment extends BaseFragment implements IFlyplansMvpView, SwipeItemOnClickListener<FlyPlan> {

    public static final String TAG = "FlyplansFragment";
    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.flyplans.FlyplansActivity.EXTRA_TRIGGER_SYNC_FLAG";
    @Inject
    DialogUtil dialogUtil;
    @Inject
    FlyplansPresenter flyplansPresenter;
    @BindView(R.id.fabAddFlyplan)
    FloatingActionButton fabAddFlyplan;
    private int selectedItem = -1;
    private FlyplanSwipeListViewAdaper flyplanListViewAdapter;
    private ListView flyplanListView;
    private Context context;
    private Activity activity;
    private Project project;
    private int BACK_CODE = 2000;

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, FlyplannerActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_flyplans, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = this.getContext();
        activity = this.getActivity();

        fabAddFlyplan.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_plus)
                .colorRes(R.color.icons)
                .actionBarSize());

        flyplanListView = view.findViewById(R.id.flyplanSwipeListView);
        flyplanListViewAdapter = new FlyplanSwipeListViewAdaper(getContext(), this);
        flyplanListView.setAdapter(flyplanListViewAdapter);
        flyplanListViewAdapter.setMode(Attributes.Mode.Single);
        loadListViewEvents();

        flyplansPresenter.attachView(this);
        loadFlyplans(null);
    }

    public void loadFlyplans(Project project) {
        if (project == null) {
            showFlyplansEmpty();
            return;
        }
        ((ProjectsFlyplansActivity) getActivity()).getSupportActionBar().setTitle(StringUtils.abbreviate(project.getName(), 10) + " » Flyplans");
        this.project = project;
        flyplansPresenter.loadFlyplans(project);
    }

    private void loadListViewEvents() {
        flyplanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //goToActivity(context, FlyplannerActivity.class, new Bundle());

                FlyPlan flyplan = (FlyPlan) flyplanListView.getItemAtPosition(position);
                // todo: find flyplan offline node data

                Intent intent = new Intent(context, FlyplannerActivity.class);
                intent.putExtra("Flyplan", flyplan);
                startActivityForResult(intent, 2000);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flyplansPresenter.detachView();
    }

    public ViewParent findParentRecursively(View view, int targetId) {
        if (view.getId() == targetId)
            return (ViewParent) view;
        View parent = (View) view.getParent();
        if (parent == null)
            return null;
        return findParentRecursively(parent, targetId);
    }

    @OnClick(R.id.fabAddFlyplan)
    public void onFabClicked() {
        Intent intent = new Intent(context, FlyplanAddOrEditActivity.class);
        intent.putExtra("FlyplanAction", FlyplanAction.Add);
        intent.putExtra("Project", project);
        startFlyplanActivity(fabAddFlyplan, intent, FlyplanAction.Add, fabAddFlyplan.getTransitionName());
    }

    // TODO FlyplanAction rename to swipeAction
    private void startFlyplanActivity(View view, Intent intent, FlyplanAction flyplanAction, String transitionName) {
        int requestCode = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, view, transitionName);
            startActivityForResult(intent, requestCode, options.toBundle());
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                FlyPlan flyplan = data.getParcelableExtra("Flyplan");
                FlyplanAction flyplanAction = (FlyplanAction) data.getSerializableExtra("FlyplanAction");
                if (flyplanAction == FlyplanAction.Add)
                    flyplanListViewAdapter.addItem(flyplan);
                else if (flyplanAction == FlyplanAction.Edit)
                    flyplanListViewAdapter.updateItem(selectedItem, flyplan);
                selectedItem = -1;
            }
        } else if (resultCode == BACK_CODE) {
            //FlyPlan flyplan = data.getParcelableExtra("Flyplan");
        }
    }

    @Override
    public void showFlyplans(List<FlyPlan> flyplanList) {
        // TODO
        flyplanListViewAdapter.setItems(flyplanList);
        flyplanListViewAdapter.notifyDataSetChanged();
        Toast.makeText(this.context, "flyplans sync successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showFlyplansEmpty() {
        // TODO
        List<FlyPlan> flyplanList = new ArrayList<>();
        flyplanListViewAdapter.setItems(flyplanList);
        flyplanListViewAdapter.notifyDataSetChanged();
        Toast.makeText(this.context, "flyplans empty", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError() {
        // TODO
        showFlyplansEmpty();
    }

    @Override
    public void onDeleteSuccess(FlyPlan flyplan) {
        flyplanListViewAdapter.deleteItem(flyplan);
    }

    @Override
    public void onDeleteFailed() {

    }

    @Override
    public void onEditSuccess(int position, FlyPlan flyplan) {

    }

    @Override
    public void onEditFailed() {

    }

    @Override
    public void onCallback(View view, SwipeActionButtons action, int position, FlyPlan item) {
        selectedItem = position;
        SwipeLayout swipeView = (SwipeLayout) findParentRecursively(view, R.id.flyplanItemSwipe);

        switch (action) {
            case Delete:
                String title = "Hinweis";
                String message = "Wollen Sie den Flugplan wirklich löschen?";
                Map<Integer, String> buttons = new HashMap<Integer, String>();
                buttons.put(DialogInterface.BUTTON_POSITIVE, "Ja");
                buttons.put(DialogInterface.BUTTON_NEGATIVE, "Nein");
                dialogUtil.showSimpleDialogMessage(title, message, buttons, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        swipeView.close();
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                flyplansPresenter.deleteFlyplan(item);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                });
                break;
            case Edit:
                swipeView.close();
                Intent intent = new Intent(context, FlyplanAddOrEditActivity.class);
                intent.putExtra("FlyplanAction", FlyplanAction.Edit);
                intent.putExtra("Project", project);
                intent.putExtra("Flyplan", item); // TODO PARCEABLE!
                startFlyplanActivity(view, intent, FlyplanAction.Edit, fabAddFlyplan.getTransitionName());
                break;
        }
    }
}
