package com.tevinjeffrey.rutgersct.ui.trackedsections;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.data.search.SearchManager;
import com.tevinjeffrey.rutgersct.data.search.UCTSubscription;
import com.tevinjeffrey.rutgersct.ui.IntroActivity;
import com.tevinjeffrey.rutgersct.ui.base.MVPFragment;
import com.tevinjeffrey.rutgersct.ui.chooser.ChooserFragment;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoFragment;
import com.tevinjeffrey.rutgersct.ui.utils.CircleSharedElementCallback;
import com.tevinjeffrey.rutgersct.ui.utils.CircleView;
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener;
import com.tevinjeffrey.rutgersct.ui.utils.RecyclerSimpleScrollListener;
import com.tevinjeffrey.rutgersct.utils.Utils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jonathanfinerty.once.Once;
import timber.log.Timber;

import static com.tevinjeffrey.rutgersct.ui.base.View.LayoutType.EMPTY;
import static com.tevinjeffrey.rutgersct.ui.base.View.LayoutType.LIST;

@SuppressWarnings({"ClassWithTooManyMethods"})
public class TrackedSectionsFragment extends MVPFragment
    implements TrackedSectionsView, SwipeRefreshLayout.OnRefreshListener,
    ItemClickListener<UCTSubscription, View> {

  public static final String TAG = TrackedSectionsFragment.class.getSimpleName();
  public static final String CORRUPT_SECTIONS = "corrupt_sections";

  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout mSwipeRefreshLayout;
  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @BindView(R.id.add_courses_fab)
  FloatingActionButton mFab;
  @BindView(R.id.tsf_list)
  RecyclerView mRecyclerView;
  @BindView(R.id.add_courses_to_track)
  ViewGroup mEmptyView;
  @BindView(R.id.error_view)
  ViewGroup mErrorView;

  TrackedSectionsViewState mViewState = new TrackedSectionsViewState();

  @Inject
  SearchManager searchManager;
  @Inject
  TrackedSectionsSubcomponent subcomponent;

  private ArrayList<UCTSubscription> mListDataset;
  private Unbinder unBinder;
  private Snackbar snackbar;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    LayoutInflater themedInflator =
        inflater.cloneInContext(Utils.wrapContextTheme(getActivity(), R.style.RutgersCT));
    ViewGroup rootView =
        (ViewGroup) themedInflator.inflate(R.layout.fragment_tracked_sections, container, false);
    unBinder = ButterKnife.bind(this, rootView);

    if (!Once.beenDone(CORRUPT_SECTIONS) && !Once.beenDone(IntroActivity.TOUR_STARTED)) {
      // Show alert
      new MaterialDialog.Builder(getParentActivity())
          .title("Oops!")
          .titleColor(ContextCompat.getColor(getParentActivity(), R.color.primary))
          .positiveText("Ok")
          .content(
              "We were unable to restore your tracked sections after the latest update. We are sorry for the inconvenience.")
          .positiveColor(ContextCompat.getColor(getParentActivity(), R.color.primary))
          .show();
      Once.markDone(CORRUPT_SECTIONS);
    }
    return rootView;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //Recreate presenter if necessary.
    if (mBasePresenter == null) {
      mBasePresenter = new TrackedSectionsPresenterImpl();
      subcomponent.inject((TrackedSectionsPresenterImpl) mBasePresenter);
    }
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewState.apply(this, savedInstanceState != null);
    //Attach view to presenter
    mBasePresenter.attachView(this);

    //Load data depending on if the view is currently refreshing
    if (mIsInitialLoad) {
      getPresenter().loadTrackedSections(true);
    } else {
      //Silently refresh tracked sections
      if (!getPresenter().isLoading()) {
        getPresenter().loadTrackedSections(false);
      }
    }
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_tracked_sections, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_refresh:
        onRefresh();
        return true;
      case R.id.action_webreg:
        launchWebReg();
        return true;
      case R.id.action_rate:
        launchMarket();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    dismissSnackbar();
    unBinder.unbind();
  }

  public void initRecyclerView() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getParentActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    layoutManager.setSmoothScrollbarEnabled(true);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setHasFixedSize(true);

    RecyclerSimpleScrollListener recyclerSimpleScrollListener = new RecyclerSimpleScrollListener();
    mRecyclerView.addOnScrollListener(recyclerSimpleScrollListener);
    if (mListDataset == null) {
      mListDataset = new ArrayList<>(10);
    }

    if (mRecyclerView.getAdapter() == null) {
      mRecyclerView.setAdapter(new TrackedSectionsFragmentAdapter(mListDataset, this));
    }
  }

  public void initSwipeLayout() {
    mSwipeRefreshLayout.setOnRefreshListener(this);
    mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
    mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green);
  }

  public void initToolbar() {
    setToolbar(mToolbar);
  }

  @Override
  public void injectTargets() {
  }

  @Override
  public void onItemClicked(UCTSubscription subscription, View view) {
    Timber.i("Selected subscription: %s", subscription);
    searchManager.setSearchFlow(subscription.getSearchFlow());
    startSectionInfoFragment(SectionInfoFragment.newInstance(), view);
  }

  @Override
  public void onRefresh() {
    // Retrieve section and while showing the loading animation.
    getPresenter().loadTrackedSections(true);
  }

  @Override
  public void setData(List<UCTSubscription> data) {
    mViewState.data = data;
    mListDataset.clear();
    mListDataset.addAll(data);
    mRecyclerView.getAdapter().notifyDataSetChanged();

    if (data.size() == 0) {
      showLayout(EMPTY);
    } else if (data.size() > 0) {
      showLayout(LIST);
    }
  }

  @Override
  public void showError(Throwable t) {
    String message;
    Resources resources = getContext().getResources();
    if (t instanceof UnknownHostException) {
      message = resources.getString(R.string.no_internet);
    } else if (t instanceof SocketTimeoutException) {
      message = resources.getString(R.string.timed_out);
    } else {
      message = t.getMessage();
    }
    // Save current error message ahead of config change.
    mViewState.errorMessage = message;
    // Show the error layout if there's nothing in the adpater to show.
    // Redirects the message that would usually be in the snackbar, to error layout.
    // https://www.google.com/design/spec/patterns/errors.html#errors-app-errors
    if (!adapterHasItems()) {
      showLayout(LayoutType.ERROR);
      TextView textViewMessage = ButterKnife.findById(mErrorView, R.id.text);
      textViewMessage.setText(message);
    } else {
      showSnackBar(message);
    }
  }

  public void showLayout(LayoutType type) {
    mViewState.layoutType = type;
    switch (type) {
      case ERROR:
        showEmptyLayout(View.GONE);
        showRecyclerView(View.GONE);
        showErrorLayout(View.VISIBLE);
        break;
      case EMPTY:
        showRecyclerView(View.GONE);
        showErrorLayout(View.GONE);
        showEmptyLayout(View.VISIBLE);
        break;
      case LIST:
        showErrorLayout(View.GONE);
        showEmptyLayout(View.GONE);
        showRecyclerView(View.VISIBLE);
        break;
      default:
        throw new RuntimeException("Unknown type: " + type);
    }
  }

  //Click events get through to the SwipeRefreshLayout even though another view is covering it.
  // Instead of interecepting clicks I can disable it the layout entirely. Unfortunately disabling the SRL disables
  // the gesture and the loading animation when setEnabled(false) is called. This is an issue as
  // the refresh animation is the only way to notify the user of the work being done. So e.g if the
  // we're in an empty state an the user issues a refresh there will be no refresh animation.
    /*private void enableSwipeRefreshLayout(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }*/

  @Override
  public void showLoading(final boolean pullToRefresh) {
    mViewState.isRefreshing = pullToRefresh;
    mSwipeRefreshLayout.post(new Runnable() {
      @Override
      public void run() {
        if (mSwipeRefreshLayout != null) {
          mSwipeRefreshLayout.setRefreshing(pullToRefresh);
        }
      }
    });
  }

  @Override
  public String toString() {
    return TAG;
  }

  @OnClick(R.id.add_courses_fab)
  public void onFabClick(View view) {
    startChooserFragment();
  }

  @OnClick(R.id.try_again)
  public void onTryAgainClick(View view) {
    onRefresh();
  }

  private boolean adapterHasItems() {
    return mRecyclerView.getAdapter().getItemCount() > 0;
  }

  private void dismissSnackbar() {
    //It's only being dismissed to not leak the fragment
    if (snackbar != null) {
      snackbar.dismiss();
    }
  }

  private TrackedSectionsPresenter getPresenter() {
    return (TrackedSectionsPresenter) mBasePresenter;
  }

  private void launchMarket() {
    final Uri uri = Uri.parse(
        "market://details?id=" + getParentActivity().getApplicationContext().getPackageName());
    final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);
    if (getParentActivity().getPackageManager().queryIntentActivities(rateAppIntent, 0).size()
        > 0) {
      startActivity(rateAppIntent);
    }
  }

  private void launchWebReg() {
    String url = "http://webreg.rutgers.edu";
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(url));
    startActivity(i);
  }

  private void showEmptyLayout(int visibility) {
    if (mEmptyView.getVisibility() != visibility) {
      mEmptyView.setVisibility(visibility);
    }
  }

  private void showErrorLayout(int visibility) {
    if (mErrorView.getVisibility() != visibility) {
      mErrorView.setVisibility(visibility);
    }
  }

  private void showRecyclerView(int visibility) {
    if (mRecyclerView.getVisibility() != visibility) {
      mRecyclerView.setVisibility(visibility);
    }
  }

  private void showSnackBar(CharSequence message) {
    snackbar = makeSnackBar(message);
    snackbar.setAction(R.string.retry, view -> {
      onRefresh();
      mViewState.snackBarShowing = false;
    });
    snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
      @Override
      public void onDismissed(final Snackbar transientBottomBar, final int event) {
        mViewState.snackBarShowing = false;
        snackbar.removeCallback(this);
      }

      @Override
      public void onShown(final Snackbar transientBottomBar) {
        mViewState.snackBarShowing = true;
      }
    });
    snackbar.show();
  }

  private void startChooserFragment() {
    ChooserFragment chooserFragment = ChooserFragment.newInstance();
    FragmentTransaction ft = getFragmentManager().beginTransaction();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      mToolbar.setTransitionName(getString(R.string.transition_name_tool_background));
      ft.addSharedElement(mToolbar, getString(R.string.transition_name_tool_background));
      setExitTransition(new Fade(Fade.OUT).setDuration(getResources().getInteger(R.integer.exit_anim)));
      chooserFragment.setAllowEnterTransitionOverlap(false);
      chooserFragment.setAllowReturnTransitionOverlap(false);
    } else {
      ft.setCustomAnimations(
          R.animator.enter,
          R.animator.exit,
          R.animator.pop_enter,
          R.animator.pop_exit
      );
    }
    startFragment(TrackedSectionsFragment.this, chooserFragment, ft);
  }

  private void startSectionInfoFragment(SectionInfoFragment sectionInfoFragment, View view) {
    FragmentTransaction ft =
        this.getFragmentManager().beginTransaction();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      CircleView circleView = ButterKnife.findById(view, R.id.section_number_background);
      mFab.setTransitionName(getString(R.string.transition_name_fab));
      ft.addSharedElement(mFab, getString(R.string.transition_name_fab));

      circleView.setTransitionName(getString(R.string.transition_name_circle_view));
      ft.addSharedElement(circleView, getString(R.string.transition_name_circle_view));

      Transition tsfSectionEnter = TransitionInflater
          .from(getParentActivity())
          .inflateTransition(R.transition.tsf_section_enter);

      Transition tsfSectionReturn = TransitionInflater
          .from(getParentActivity())
          .inflateTransition(R.transition.tsf_section_return);

      sectionInfoFragment.setEnterTransition(tsfSectionEnter);
      sectionInfoFragment.setReturnTransition(tsfSectionReturn);

      setReenterTransition(new Fade(Fade.IN).addTarget(RecyclerView.class));
      setExitTransition(new Fade(Fade.OUT).addTarget(RecyclerView.class));

      sectionInfoFragment.setAllowReturnTransitionOverlap(false);
      sectionInfoFragment.setAllowEnterTransitionOverlap(false);

      Transition sharedElementsEnter = TransitionInflater
          .from(getParentActivity())
          .inflateTransition(R.transition.tsf_shared_element_enter);
      Transition sharedElementsReturn = TransitionInflater
          .from(getParentActivity())
          .inflateTransition(R.transition.tsf_shared_element_return);

      sectionInfoFragment.setSharedElementEnterTransition(sharedElementsEnter);
      sectionInfoFragment.setSharedElementReturnTransition(sharedElementsReturn);

      CircleSharedElementCallback sharedelementCallback = new CircleSharedElementCallback();
      sectionInfoFragment.setEnterSharedElementCallback(sharedelementCallback);
      sharedElementsEnter.addListener(sharedelementCallback.getTransitionCallback());
    } else {
      ft.setCustomAnimations(
          R.animator.enter,
          R.animator.exit,
          R.animator.pop_enter,
          R.animator.pop_exit
      );
    }
    startFragment(this, sectionInfoFragment, ft);
  }
}
