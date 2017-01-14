# YoutubeAndroidSample
### 简阅：<a id="orgheadline1"></a>

```
本Sample直接在官方给的demo上做修改，涉及到的功能点完全可以和一个线上的app相媲美，在此先说下我在实战中踩的一些坑吧和
相应的解决办法，相信这样能更快的击中大家的兴趣点；该项目也会持续维护，也欢迎大家多提意见，欢迎star，欢迎提交issues；
谢谢;
```

> **情景一：手机上安装并启用了YouTube：**

* **坑1：在Fragment中的如何使用YouTubePlayerView？**
* **坑2：如何编写视频列表，点击列表的item在当前行上直接播放？**
* **坑3：为什么有时候视频播着几秒后自动停止了？**
* **坑4：为什么有时候视频播着就闪退了？**
* **坑5：当播放的视频暂停后如何续播当前视频？**
* **坑6：当播放的视频资源释放后如何续播上一个视频？**
* **坑7：当未启动YouTube客户端的时候，在某些手机下视频播放几秒后自动闪退了，比如OnePlus ONE A2001机型或其它大陆版的机型**
* **坑8：如何让播放视频前先播放广告？**

> **情景二：手机上未安装或者未启用YouTube：**

* **坑9：如何播放YouTube视频？**
* **坑10：如何适配播放器在各种分辨率下的宽高？**
* **坑11：如何监听播放状态？**
* **坑12：播放器上为啥没有全屏按钮显示？**
* **坑13：如何添加全屏播放功能？**
* **坑14：为何有些视频在手机上播放不了，但能在网页上播放？**
* **坑15：如何检测播放出错的监听事件?**

<font color="red" size="4"> 注：以上两种情景都需要在连接VPN的情况下测试 </font>

### Screenshots

<img src="screenshots/guide1.png" width="24.2%" />
<img src="screenshots/guide2.png" width="24.2%" />
<img src="screenshots/guide3.png" width="24.2%" />
<img src="screenshots/guide4.png" width="24.2%" />


### 针对以上的坑做的填坑措施：

##### 坑1：在Fragment中的如何使用YouTubePlayerView？
* SDK是不支持在fragment中直接使用YouTubePlayerView的，可以使用sdk中提供的YouTubePlayerFragment，如果有在项目中要求在Fragment中使用播放器，
可以直接继承YouTubePlayerFragment或者动态添加单独的YouTubePlayerFragment，通过：
```
final FrameLayout layout = new FrameLayout(mAct);
layout.setId(R.id.view_player_container);
layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
mAct.getFragmentManager().beginTransaction().replace(layout.getId(), fragment).commitAllowingStateLoss();
```
这种方式添加也行，比如视频列表就是采取这个方式，保证播放器永远只存在一个，节省内存的开销；不用的时候需要释放播放器资源

##### 坑2：如何编写视频列表，点击列表的item在当前行上直接播放？
* 请参考项目中的[VideoListDemo2Activity](https://github.com/hongniuniu/YoutubeAndroidSample/blob/master/app/src/main/java/com/faith/ctv/ytb/VideoListDemo2Activity.java)文件；

##### 坑3：为什么有时候视频播着几秒后自动停止了？
* 这是因为在播放器的上面还有其他控件盖在上面，比如一条分割线等，具体原因就是播放器未完全呈现出来；错误信息如下：
```
YouTube video playback stopped due to unauthorized overlay on top of player
```
解决办法自然就是移除对应的覆盖物就行了;

##### 坑4：为什么有时候视频播着就闪退了？
* 针对这个问题有两种情况：
情况1：sdk内部报错：这种错误可以通过try catch拦截掉，不会导致客户端奔溃
情况2：跟坑7的错误原因一致，具体解决方案请看坑7的介绍；

##### 坑5：当播放的视频暂停后如何续播当前视频？
这个在已有YouTube客户端（已开启服务）的环境下基本不算坑，直接走sdk逻辑就行，sdk内部会控制播放暂停功能；

##### 坑6：当播放的视频资源释放后如何续播上一个视频？
针对该问题，需要如下两个步骤：
步骤1：通过YouTubePlayer对象注册两个事件，如下：
```
player.setPlaybackEventListener(this);
player.setPlayerStateChangeListener(this);
```

步骤2：在onPlaying函數中添加seekToMillis方法进行续播（前提使你知道具体续播的时间，单位为秒）
```
@Override
public void onPlaying() {
    mPlayer.seekToMillis(TDNewsApplication.mPrefer.getPlayerTime());
}
```

##### 坑7：当未启动YouTube客户端的时候，在某些手机下视频播放几秒后自动闪退了，比如OnePlus ONE A2001机型或其它大陆版的机型
这个坑算的上是一个巨坑，对于喜欢追求完美的我来说，为了能更好的给用户带来方便，这个坑必须填的啦；
经过多次测试得出经验：需要通过命令的方式去激活com.google.android.youtube.player该服务，具体通过什么方式去激活呢？需要通过隐式开启服务中的activity：代码如下：
```
Intent intent = new Intent();
intent.setPackage("com.google.android.youtube");
intent.setAction("com.google.android.youtube.api.StandalonePlayerActivity.START");
intent.putExtra("video_id", "cdgQpa1pUUE"); // 随便定义一个视频ID
intent.putExtra("lightbox_mode", true); // 让activity呈现一个dialog的方式，避免遮挡过大
startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
```
像这种逻辑一般都是写在启动页里的，这里就又有个问题了，启动页是程序每次启动的时候需要执行的，难不成我的那个激活服务的代码每次都要调用一遍吗？已经激活了还需要调用？那这样
就不是很合理了，或者说根本不合理吧，那就只能加条件检测当前服务是否已经开启了，这里又用到了新的这是了，通过去执行dos命令的方式去检测代码如下：
```
 ShellUtil.CommandResult result = ShellUtil.execCommand("ps | grep 'com.google.android.youtube'", false);
 LogUtils.d("测试：result = " + result.responseMsg);
 if (null == result || (null != result && StringUtil.isEmpty(result.responseMsg))) {
    LogUtils.d("测试：result = " + result.responseMsg + "-->again"); // 未开启，开启就在else里了
 }
```
具体的ShellUtil文件请查看项目中的：[ShellUtil](https://github.com/hongniuniu/YoutubeAndroidSample/blob/master/app/src/main/java/com/faith/ctv/ytb/utils/ShellUtil.java)

##### 坑8：如何让播放视频前先播放广告？
这是一个技巧性的问题，一般人我不告诉他的，我也是摸索了好久才突然发现的；这么做的目的是因为，我的项目中需要测试广告播放结束后继续视频播放的UI呈现问题；好了不啰嗦了，直接上技巧：
* 步骤：先断开vpn进入视频列表，点击播放，提示：请检查你的网络连线，再连接vpn，点击播放，此时播放应该有广告显示，经多次测试得出的结果；

##### 坑9：如何播放YouTube视频？
扯了这么多，终于轮到h5视频闪亮登场了；

针对该问题：简单点说就是需要导入h5脚本文件，动态设置视频ID去播放，详细介绍：等我demo功能放上去再告诉大家；

##### 坑10：如何适配播放器在各种分辨率下的宽高？
脚本文件中设置以下代码：
```
player.setSize(window.innerWidth, window.innerHeight);
```

##### 坑11：如何监听播放状态？
该问题就牵扯到前端和客户端的交互了，具体实现思路就是在h5的播放状态函数中分发事件给客户端，代码如下：
```
//当播放器的状态改变时，这个函数调用这个函数
var done = false;
function onPlayerStateChange(event) {
    console.log('js调用：播放状态...data = ' + event.data + '-->errorCode = ' + errorCode);
    window.location.href = 'wabridge://onPlayerStateChange?data=' + event.data + "&errorCode=" + errorCode;
}
```

### 针对YouTube的sdk和h5播放器的功能可以参照如下链接，进行知识点梳理和查阅，也欢迎大家提issue：
* Android的API介绍：
[https://developers.google.com/youtube/android/player/reference/com/google/android/youtube/player/package-summary](https://developers.google.com/youtube/android/player/reference/com/google/android/youtube/player/package-summary)
* YouTube的Android问题板块：
[http://stackoverflow.com/questions/tagged/android-youtube-api](http://stackoverflow.com/questions/tagged/android-youtube-api)
* H5的API介绍：
[https://developers.google.cn/youtube/player_parameters](https://developers.google.cn/youtube/player_parameters)


### 关于我

* **QQ:** 907167515
* **微信:** faith-hb
* **Weibo:** [http://weibo.com/234351856/home?wvr=5](http://weibo.com/234351856/home?wvr=5)
* **Email:** [hongbinghp@163.com](mailto:hongbinghp@163.com) | [hongbinghp@gmail.com](mailto:hongbinghp@gmail.com)
* **Github:** [https://github.com/faith-hb](https://github.com/faith-hb)
* **Blog:** [http://blog.csdn.net/hongbingfans](http://blog.csdn.net/hongbingfans)

###License

```
Copyright (c) 2016 [hongbinghp@163.com | hongbinghp@gmail.com]

Licensed under the Apache License, Version 2.0 (the "License”);
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
   
   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
