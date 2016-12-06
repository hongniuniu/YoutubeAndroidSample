/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.faith.ctv.ytb;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewPropertyAnimator;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A sample Activity showing how to manage multiple YouTubeThumbnailViews in an adapter for display
 * in a List. When the list items are clicked, the video is played by using a YouTubePlayerFragment.
 * <p>
 * The demo supports custom fullscreen and transitioning between portrait and landscape without
 * rebuffering.
 */
@TargetApi(13)
public final class VideoListDemo2Activity extends Activity implements OnFullscreenListener {

  private static final String TAG = VideoListDemo2Activity.class.getSimpleName();
  /** The duration of the animation sliding up the video in portrait. */
  private static final int ANIMATION_DURATION_MILLIS = 300;
  /** The padding between the video list and the video in landscape orientation. */
  private static final int LANDSCAPE_VIDEO_PADDING_DP = 5;

  /** The request code when calling startActivityForResult to recover from an API service error. */
  private static final int RECOVERY_DIALOG_REQUEST = 1;

  private VideoListFragment listFragment;
//  private VideoFragment videoFragment;

//  private View videoBox;
//  private View closeButton;

  private boolean isFullscreen;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.video_list_demo2);

    listFragment = (VideoListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
//    videoFragment =
//        (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);

//    videoBox = findViewById(R.id.video_box);
//    closeButton = findViewById(R.id.close_button);
//
//    videoBox.setVisibility(View.INVISIBLE);

//    layout();

    checkYouTubeApi();
  }

  private void checkYouTubeApi() {
    YouTubeInitializationResult errorReason =
        YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
    if (errorReason.isUserRecoverableError()) {
      errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
    } else if (errorReason != YouTubeInitializationResult.SUCCESS) {
      String errorMessage =
          String.format(getString(R.string.error_player), errorReason.toString());
      Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == RECOVERY_DIALOG_REQUEST) {
      // Recreate the activity if user performed a recovery action
      recreate();
    }
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

//    layout();
  }

  @Override
  public void onFullscreen(boolean isFullscreen) {
    this.isFullscreen = isFullscreen;

//    layout();
  }

  /**
   * Sets up the layout programatically for the three different states. Portrait, landscape or
   * fullscreen+landscape. This has to be done programmatically because we handle the orientation
   * changes ourselves in order to get fluent fullscreen transitions, so the xml layout resources
   * do not get reloaded.
   */
//  private void layout() {
//    boolean isPortrait =
//        getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
//
//    listFragment.getView().setVisibility(isFullscreen ? View.GONE : View.VISIBLE);
//    listFragment.setLabelVisibility(isPortrait);
//    closeButton.setVisibility(isPortrait ? View.VISIBLE : View.GONE);
//
//    if (isFullscreen) {
//      videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
//      setLayoutSize(videoFragment.getView(), MATCH_PARENT, MATCH_PARENT);
//      setLayoutSizeAndGravity(videoBox, MATCH_PARENT, MATCH_PARENT, Gravity.TOP | Gravity.LEFT);
//    } else if (isPortrait) {
//      setLayoutSize(listFragment.getView(), MATCH_PARENT, MATCH_PARENT);
//      setLayoutSize(videoFragment.getView(), MATCH_PARENT, WRAP_CONTENT);
//      setLayoutSizeAndGravity(videoBox, MATCH_PARENT, WRAP_CONTENT, Gravity.BOTTOM);
//    } else {
//      videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
//      int screenWidth = dpToPx(getResources().getConfiguration().screenWidthDp);
//      setLayoutSize(listFragment.getView(), screenWidth / 4, MATCH_PARENT);
//      int videoWidth = screenWidth - screenWidth / 4 - dpToPx(LANDSCAPE_VIDEO_PADDING_DP);
//      setLayoutSize(videoFragment.getView(), videoWidth, WRAP_CONTENT);
//      setLayoutSizeAndGravity(videoBox, videoWidth, WRAP_CONTENT,
//          Gravity.RIGHT | Gravity.CENTER_VERTICAL);
//    }
//  }

//  public void onClickClose(@SuppressWarnings("unused") View view) {
//    listFragment.getListView().clearChoices();
//    listFragment.getListView().requestLayout();
//    videoFragment.pause();
//    ViewPropertyAnimator animator = videoBox.animate()
//        .translationYBy(videoBox.getHeight())
//        .setDuration(ANIMATION_DURATION_MILLIS);
//    runOnAnimationEnd(animator, new Runnable() {
//      @Override
//      public void run() {
//        videoBox.setVisibility(View.INVISIBLE);
//      }
//    });
//  }

  @TargetApi(16)
  private void runOnAnimationEnd(ViewPropertyAnimator animator, final Runnable runnable) {
    if (Build.VERSION.SDK_INT >= 16) {
      animator.withEndAction(runnable);
    } else {
      animator.setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          runnable.run();
        }
      });
    }
  }

  /**
   * A fragment that shows a static list of videos.
   */
  public static final class VideoListFragment extends ListFragment implements AbsListView.OnScrollListener {
    private static List<VideoEntry> mList = new ArrayList<>();
    private void addTestData(){
      mList.add(new VideoEntry("YouTube Collection", "Y_UmWdcTrrc",136 * 1000));
      mList.add(new VideoEntry("GMail Tap", "1KhZKNZO8mQ",136 * 1000));
      mList.add(new VideoEntry("Chrome Multitask", "UiLSiqyDf4Y",96 * 1000));
      mList.add(new VideoEntry("Google Fiber", "re0VRK6ouwI",124 * 1000));
      mList.add(new VideoEntry("Autocompleter", "blB_X38YSxQ",156 * 1000));
      mList.add(new VideoEntry("GMail Motion", "Bu927_ul_X0",112 * 1000));
      mList.add(new VideoEntry("Translate for Animals", "3I24bSteJpw",102 * 1000));
    }

    private PageAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addTestData();
      adapter = new PageAdapter(getActivity(), mList);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);

      getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      getListView().setOnScrollListener(this);
      setListAdapter(adapter);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState){
          case SCROLL_STATE_IDLE: // 滑动停止
            Log.d(TAG, "滑动停止-->SCROLL_STATE_IDLE");
            if (null != mPlayer && !mCurrItemIsVis) {
              long minutes = TimeUnit.MILLISECONDS.toMinutes(mPlayer.getDurationMillis());
              long second = TimeUnit.MILLISECONDS.toSeconds(mPlayer.getDurationMillis()) - minutes * 60;

              Log.d(TAG, "滑动停止-->SCROLL_STATE_IDLE-->当前播放时间 ＝ " + mPlayer.getCurrentTimeMillis() +
                      "-->总时间 ＝ " + mPlayer.getDurationMillis() +
                      "-->mPlayer.isPlaying() = " + mPlayer.isPlaying() +
                      "-->分钟 ＝ " + minutes +
                      "-->秒 ＝ " + second);
              mPlayer.release();
              mPlayer = null;
            }
            break;
          case SCROLL_STATE_TOUCH_SCROLL: // 正在滚动
            Log.d(TAG,"正在滚动-->SCROLL_STATE_TOUCH_SCROLL");
            break;
          case SCROLL_STATE_FLING: // 手指快速滑动时，在离开listview的惯性滑动
            Log.d(TAG,"快速滑动-->SCROLL_STATE_FLING");
            break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


      Log.d(TAG,"滚动状态-->firstVisibleItem = " + firstVisibleItem + "-->visibleItemCount = " + visibleItemCount + "-->totalItemCount = " + totalItemCount);

      if (firstVisibleItem <= mCurrItemPos && mCurrItemPos <= (firstVisibleItem + visibleItemCount)) {
        mCurrItemIsVis = true;
      }else{
        mCurrItemIsVis = false;
      }
      Log.d(TAG, "滚动状态-->click item是否可 ＝ " + mCurrItemIsVis);


    }

    private int mCurrItemPos; // 当前点击的行数
    private boolean mCurrItemIsVis; // 当前点击的item滑动后是否可见

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//      VideoFragment videoFragment =
//          (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);
//      videoFragment.setVideoId(videoId);
//
//      // The videoBox is INVISIBLE if no video was previously selected, so we need to show it now.
//      if (videoBox.getVisibility() != View.VISIBLE) {
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//          // Initially translate off the screen so that it can be animated in from below.
//          videoBox.setTranslationY(videoBox.getHeight());
//        }
//        videoBox.setVisibility(View.VISIBLE);
//      }
//
//      // If the fragment is off the screen, we animate it in.
//      if (videoBox.getTranslationY() > 0) {
//        videoBox.animate().translationY(0).setDuration(ANIMATION_DURATION_MILLIS);
//      }

      mCurrItemPos = position;

      for (VideoEntry entry : mList){
        entry.setPlay(false);
      }

      VideoEntry entry = (VideoEntry) l.getAdapter().getItem(position);
      entry.setPlay(true);
      adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
      super.onDestroyView();

      Log.d("初始化", "初始化VideoListFragment的onDestroyView");
      mList.clear();
      adapter.releaseLoaders();
      if (null != mPlayer) {
        mPlayer.release();
        mPlayer = null;
      }

    }
  }




  /**
   * Adapter for the video list. Manages a set of YouTubeThumbnailViews, including initializing each
   * of them only once and keeping track of the loader of each one. When the ListFragment gets
   * destroyed it releases all the loaders.
   */
  private static final class PageAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<VideoEntry> entries;
    private final List<View> entryViews;
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
    private final LayoutInflater inflater;
    private final ThumbnailListener thumbnailListener;

    public PageAdapter(Context context, List<VideoEntry> entries) {
      mContext = context;
      this.entries = entries;
      entryViews = new ArrayList<>();
      thumbnailViewToLoaderMap = new HashMap<>();
      inflater = LayoutInflater.from(context);
      thumbnailListener = new ThumbnailListener();


    }

    public void releaseLoaders() {
      for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
        loader.release();
      }
    }

    public void setLabelVisibility(boolean visible) {
      for (View view : entryViews) {
        view.findViewById(R.id.text).setVisibility(visible ? View.VISIBLE : View.GONE);
      }
    }

    @Override
    public int getCount() {
      return entries.size();
    }

    @Override
    public VideoEntry getItem(int position) {
      return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder holder;
      VideoEntry entry = entries.get(position);

      // There are three cases here
      if (convertView == null) {
        // 1) The view has not yet been created - we need to initialize the YouTubeThumbnailView.
        convertView = inflater.inflate(R.layout.video_list_item2, parent, false);
        holder = new ViewHolder();
        holder.titleTv = (TextView) convertView.findViewById(R.id.id_title);
        holder.timeTv = (TextView) convertView.findViewById(R.id.id_time);
        holder.thumbnail = (YouTubeThumbnailView) convertView.findViewById(R.id.thumbnail);
        holder.thumbnail.setTag(entry.videoId);
        holder.thumbnail.initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener);
        convertView.setTag(holder);

      } else {
        holder = (ViewHolder) convertView.getTag();

        YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(holder.thumbnail);
        if (loader == null) {
          // 2) The view is already created, and is currently being initialized. We store the
          //    current videoId in the tag.
          holder.thumbnail.setTag(entry.videoId);
        } else {
          // 3) The view is already created and already initialized. Simply set the right videoId
          //    on the loader.
//          holder.thumbnail.setImageResource(R.drawable.loading_thumbnail);
          loader.setVideo(entry.videoId);
        }
      }

      holder.titleTv.setText(entry.getText());
      long minutes = TimeUnit.MILLISECONDS.toMinutes(entry.getTime());
      long second = TimeUnit.MILLISECONDS.toSeconds(entry.getTime()) - minutes * 60;
      holder.timeTv.setText((minutes < 10 ? "0" + minutes : minutes) + ":" + (second < 10 ? "0" + second : second ));

      ViewGroup vp = (ViewGroup) convertView;
      View playView = vp.findViewById(R.id.player_container);
      if (null != playView) { // 判断itemview是否包含播放容器
        Log.d("对象", "初始化，itemview已包含播放视图");
        vp.removeView(playView);
      }

      if (entry.isPlay()) {
        entry.setPlay(false);
        Log.d("对象", "初始化，itemview开始播放");
        FrameLayout layout = new FrameLayout(mContext);
        layout.setId(R.id.player_container);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//          TextView tv = new TextView(mContext);
//          tv.setText("测试测试测试");
        vp.addView(layout);

        setPlayerView(layout.getId(),entry.videoId);
      }

      return convertView;
    }


    /**
     * 设置播放器视图
     * @param resId
     */
    public void setPlayerView(int resId,String videoId) {
      VideoFragment fragment = VideoFragment.newInstance(videoId);
      FragmentManager fm = ((Activity) mContext).getFragmentManager();
      FragmentTransaction mTransaction = fm.beginTransaction();
      mTransaction.replace(resId, fragment);
      //添加到返回按键堆栈，这样就可以执行碎片的第二条周期，也就是不会每次replace的时候都去执行onAttach
//      mTransaction.addToBackStack(null);
      mTransaction.commit();
//      fragment.setVideoId(videoId);
    }

    static class ViewHolder {
      YouTubeThumbnailView thumbnail;
      TextView titleTv;
      TextView timeTv;
    }

    private final class ThumbnailListener implements
        YouTubeThumbnailView.OnInitializedListener,
        YouTubeThumbnailLoader.OnThumbnailLoadedListener {

      @Override
      public void onInitializationSuccess(
          YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
        loader.setOnThumbnailLoadedListener(this);
        thumbnailViewToLoaderMap.put(view, loader);
        view.setImageResource(R.drawable.loading_thumbnail);
        String videoId = (String) view.getTag();
        loader.setVideo(videoId);
      }

      @Override
      public void onInitializationFailure(
          YouTubeThumbnailView view, YouTubeInitializationResult loader) {
        view.setImageResource(R.drawable.no_thumbnail);
      }

      @Override
      public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
      }

      @Override
      public void onThumbnailError(YouTubeThumbnailView view, ErrorReason errorReason) {
        view.setImageResource(R.drawable.no_thumbnail);
      }
    }

  }

  private static YouTubePlayer mPlayer;
  public static final class VideoFragment extends YouTubePlayerFragment
      implements OnInitializedListener,YouTubePlayer.PlayerStateChangeListener,YouTubePlayer.PlaybackEventListener,YouTubePlayer.PlaylistEventListener {


    private String videoId;

    public static VideoFragment newInstance(String videoId) {
      final VideoFragment fragment = new VideoFragment();
      final Bundle bundle = new Bundle();
      bundle.putString("KEY_VIDEO_ID", videoId);
      fragment.setArguments(bundle);
      return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.d("VideoListDemo2Activity", "YouTubePlayerFragment-->onCreate...");
      final Bundle arguments = getArguments();
      if (null != arguments && arguments.containsKey("KEY_VIDEO_ID")) {
        videoId = arguments.getString("KEY_VIDEO_ID");
        Log.d("VideoListDemo2Activity", "YouTubePlayerFragment-->videoId = " + videoId);
      }
      initialize(DeveloperKey.DEVELOPER_KEY, this);
    }

    @Override
    public void onPause() {
      super.onPause();
      Log.d("VideoListDemo2Activity","YouTubePlayerFragment-->onPause...");
    }

    @Override
    public void onStop() {
      super.onStop();
      Log.d("VideoListDemo2Activity","YouTubePlayerFragment-->onStop...");
    }

    @Override
    public void onDestroy() {
      Log.d("VideoListDemo2Activity","YouTubePlayerFragment-->onDestroy...");
      if (mPlayer != null) {
        mPlayer.release();
      }
      super.onDestroy();
    }

//    public void setVideoId(String videoId) {
//      if (videoId != null && !videoId.equals(this.videoId)) {
//        this.videoId = videoId;
//        if (player != null) {
//          Log.d("初始化","播放器碎片setVideoId开始播放");
//          player.cueVideo(videoId);
//        }
//      }
//    }
//
//    public void pause() {
//      if (player != null) {
//        player.pause();
//      }
//    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean restored) {
      Log.d("初始化","播放器碎片onInitializationSuccess");
      mPlayer = player;
      player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
      player.setOnFullscreenListener((VideoListDemo2Activity) getActivity());
      player.setPlayerStateChangeListener(this);
      player.setPlaybackEventListener(this);
      player.setPlaylistEventListener(this);
      player.setShowFullscreenButton(false); // 隐藏全屏按钮
//      if (!restored && videoId != null) {
//        Log.d("初始化","播放器碎片onInitializationSuccess开始播放");
//        player.cueVideo(videoId);
//      }

      if (!TextUtils.isEmpty(videoId) && null != player) {
        if (restored) {
          player.play();
        } else {
          player.loadVideo(videoId);
        }
      }
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult result) {
      Log.d("初始化","播放器onInitializationFailure");
      mPlayer = null;
    }

    @Override
    public void onLoading() {
      Log.d("播放器","初始化onLoading");
    }

    @Override
    public void onLoaded(String s) { // s为视频ID
      Log.d("播放器","初始化onLoaded-->s = " + s);
//      setVideoId(s);
      mPlayer.play();
    }

    @Override
    public void onAdStarted() {
      Log.d("播放器","初始化onAdStarted");
    }

    @Override
    public void onVideoStarted() {
      Log.d("播放器","初始化onVideoStarted");
    }

    @Override
    public void onVideoEnded() {
      Log.d("播放器","初始化onVideoEnded");
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }

    @Override
    public void onPlaying() {
      Log.d("VideoListDemo2Activity","PlaybackEventListener<onPlaying>");
    }

    @Override
    public void onPaused() {
      Log.d("VideoListDemo2Activity","PlaybackEventListener<onPaused>-->getCurrentTimeMillis-->" + mPlayer.getCurrentTimeMillis() + "-->getDurationMillis-->" + mPlayer.getDurationMillis());
    }

    @Override
    public void onStopped() {
      Log.d("VideoListDemo2Activity","PlaybackEventListener<onStopped>-->getCurrentTimeMillis-->" + mPlayer.getCurrentTimeMillis() + "-->getDurationMillis-->" + mPlayer.getDurationMillis());
    }

    @Override
    public void onBuffering(boolean b) {
      Log.d("VideoListDemo2Activity","PlaybackEventListener<onBuffering>");
    }

    @Override
    public void onSeekTo(int i) {
      Log.d("VideoListDemo2Activity","PlaybackEventListener<onSeekTo>");
    }

    @Override
    public void onPrevious() {
      Log.d("VideoListDemo2Activity","PlaylistEventListener-->onPrevious...");
    }

    @Override
    public void onNext() {
      Log.d("VideoListDemo2Activity","PlaylistEventListener-->onNext...");
    }

    @Override
    public void onPlaylistEnded() {
      Log.d("VideoListDemo2Activity","PlaylistEventListener-->onPlaylistEnded...");
    }
  }

  private static final class VideoEntry {
    private final String text;
    private final String videoId;
    private boolean isPlay;
    private long time; // 视频时长

    public VideoEntry(String text, String videoId,long time) {
      this.text = text;
      this.videoId = videoId;
      this.time = time;
      this.isPlay = false;
    }

    public String getText() {
      return text;
    }

    public String getVideoId() {
      return videoId;
    }

    public boolean isPlay() {
      return isPlay;
    }

    public void setPlay(boolean play) {
      isPlay = play;
    }

    public long getTime() {
      return time;
    }

    public void setTime(long time) {
      this.time = time;
    }
  }

  // Utility methods for layouting.

  private int dpToPx(int dp) {
    return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
  }

  private static void setLayoutSize(View view, int width, int height) {
    LayoutParams params = view.getLayoutParams();
    params.width = width;
    params.height = height;
    view.setLayoutParams(params);
  }

  private static void setLayoutSizeAndGravity(View view, int width, int height, int gravity) {
    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
    params.width = width;
    params.height = height;
    params.gravity = gravity;
    view.setLayoutParams(params);
  }

}
