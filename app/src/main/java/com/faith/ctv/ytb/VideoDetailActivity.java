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

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Sample activity showing how to properly enable custom fullscreen behavior.
 * <p>
 * This is the preferred way of handling fullscreen because the default fullscreen implementation
 * will cause re-buffering of the video.
 */
public class VideoDetailActivity extends YouTubeFailureRecoveryActivity implements
    View.OnClickListener,
    YouTubePlayer.OnFullscreenListener {

  private static final String TAG = VideoDetailActivity.class.getSimpleName();

  private LinearLayout baseLayout;
  private YouTubePlayerView playerView;
  private YouTubePlayer player;
  private View otherViews;
  private String videoId = "avP5d16wEp0";

  private boolean fullscreen;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.video_detail_demo);
    baseLayout = (LinearLayout) findViewById(R.id.layout);
    playerView = (YouTubePlayerView) findViewById(R.id.player);
    otherViews = findViewById(R.id.other_views);

    playerView.initialize(DeveloperKey.DEVELOPER_KEY, this);

    doLayout();
  }

  @Override
  public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
      boolean wasRestored) {
    this.player = player;
    // Specify that we want to handle fullscreen behavior ourselves.
    player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
    player.setOnFullscreenListener(this);
//    if (!wasRestored) {
//      player.cueVideo("avP5d16wEp0");
//    }

    if (!TextUtils.isEmpty(videoId) && null != player) {
      if (wasRestored) {
        player.play();
      } else {
        player.loadVideo(videoId);
      }
    }

  }

  @Override
  protected YouTubePlayer.Provider getYouTubePlayerProvider() {
    return playerView;
  }

  @Override
  public void onClick(View v) {
    player.setFullscreen(!fullscreen);
  }

  private void doLayout() {
    LayoutParams playerParams = (LayoutParams) playerView.getLayoutParams();
    if (fullscreen) {
      // When in fullscreen, the visibility of all other views than the player should be set to
      // GONE and the player should be laid out across the whole screen.
      playerParams.width = LayoutParams.MATCH_PARENT;
      playerParams.height = LayoutParams.MATCH_PARENT;

      otherViews.setVisibility(View.GONE);
    } else {
      otherViews.setVisibility(View.VISIBLE);
      ViewGroup.LayoutParams otherViewsParams = otherViews.getLayoutParams();
      playerParams.width = otherViewsParams.width = MATCH_PARENT;
      playerParams.height = WRAP_CONTENT;
      playerParams.weight = 0;
      otherViewsParams.height = 0;
      baseLayout.setOrientation(LinearLayout.VERTICAL);
    }
  }

  @Override
  public void onFullscreen(boolean isFullscreen) {
    fullscreen = isFullscreen;
    doLayout();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    doLayout();
  }

  @Override
  public void onBackPressed() {
    if (fullscreen) { // 当视频处于最大化的时候，拦截返回键，强制设置成竖屏
      Log.d(TAG, "点击手机返回键...");
      fullscreen = false;
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      return;
    } else {
      super.onBackPressed();
    }
  }
}
