
# react-native-rtmp-player-jm [![npm version](https://img.shields.io/npm/v/react-native-rtmp-player-jm.svg?style=flat)](https://www.npmjs.com/package/react-native-rtmp-player-jm)

Jimi RTMP Player SDK modules and view for React Native(Android & IOS), support react native 0.57+

![示例图](https://raw.githubusercontent.com/Eafy/react-native-baidu-map-jm/master/images/screenShot.png)

### Environments 环境要求
1.JS
- node: 8.0+

2.Android
- Android SDK: api 28+
- gradle: 4.5
- Android Studio: 3.1.3+

3.IOS
- XCode: 9.0+


### Install 安装
#### 使用 npm 源
npm install react-native-rtmp-player-jm --save

### 原生模块导入

#### Android Studio
`react-native link react-native-rtmp-player-jm`

#### IOS/Xcode
1、方式一：`react-native link react-native-rtmp-player-jm`
2、方式二：
Podfile 增加
```
  pod 'React', :path => '../node_modules/react-native', :subspecs => [
    'Core',
    'CxxBridge',
    'DevSupport', 
    'RCTText',
    'RCTNetwork',
    'RCTWebSocket', 
    'RCTAnimation'
  ]
  pod 'yoga', :path => '../node_modules/react-native/ReactCommon/yoga'
  pod 'DoubleConversion', :podspec => '../node_modules/react-native/third-party-podspecs/DoubleConversion.podspec'
  pod 'glog', :podspec => '../node_modules/react-native/third-party-podspecs/glog.podspec'
  pod 'Folly', :podspec => '../node_modules/react-native/third-party-podspecs/Folly.podspec'

  pod 'react-native-rtmp-player-jm', :podspec => '../node_modules/react-native-rtmp-player-jm/ios/react-native-rtmp-player-jm.podspec'
```

### Usage 使用方法

    import { Platform, NativeModules, NativeEventEmitter } from 'react-native';
    import { JMRTMPMonitorView} from 'react-native-rtmp-player-jm';
    const { JMRTMPPlayerManager } = NativeModules;
    

#### JMRTMPMonitorView Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| image                   | require  | null     | Monitor视图默认展示图

#### JMRTMPPlayerManager Methods
| Method                    | Listener | Result | Description
| ------------------------- | -------  | ------ | -------
| initialize(String key, String secret, String imei) | null | 初始化成功 | RTMP SDK内部初始化
| deInitialize() | null | null | RTMP SDK 释放(解初始化)
| startPlayLive() | kOnStreamPlayerPlayStatus | 见回调说明 | 开始播放
| startPlay(String url) | kOnStreamPlayerPlayStatus | 见回调说明 | 使用RTMP URL进行播放
| startPlayback(Array fileNameArray) | kOnStreamPlayerReceiveDeviceData | 见回调说明 | 回放文件，参数为文件名称字符串列表
| stopPlay() | null | null | 停止播放
| startTalk() | kOnStreamPlayerTalkStatus | 见回调说明 | 开始对讲，iOS会自动申请对讲权限，Android需要自己先申请权限
| stopTalk() | kOnStreamPlayerTalkStatus | 见回调说明 | 停止对讲
| stop() | null | null | 停止内部所有功能（播放、对讲，网络请求）
| Promise switchCamera(bool isFront, bool bAuto, resolver, rejecter) | Promise | bAuto，默认请填true，成功返回url，否则错误码及错误信息，比如：{ [Error: 设备不在线] code: '228'} | 切换摄像头
| Promise snapshot(String filePath) | Promise | 成功返回保存后的图片路径，否则错误码及错误信息，比如：{ [Error: Failed to do snapshot] code: '-1'} | 视频截图，filePath：需要保存图片的绝对路径，必须以".png"结尾，code:-1，截图失败，-2:保存图片失败，-3:格式无效
| startRecording(String filePath) | kOnStreamPlayerRecordStatus | 见回调说明 | mp4视频录制，filePath：需要保存视频的绝对路径(iOS默认只录制在沙盒，必须以".mp4"结尾)
| stopRecording() | kOnStreamPlayerRecordStatus | 见回调说明 | 停止视频录制
| Promise isRecording(resolve, rejecter) | Promise | true：正在录制；false：未录制 | 视频是否在录制
| Promise getRecordingDuration(resolve, rejecter) | Promise | 0 | 视频录制的时长(毫秒)
| Promise sendCustomRequest(Map paraDic, resolve, rejecter) | Promise | 0 | 发送自定义指令(请求)
| setMute(bool mute) | null | null | 设置静音
| Promise getMute() | Promise | true：静音；false：未静音 | 获取静音状态
| Promise videoSize() | Promise | {"width": 0, "hieght": 0} | 获取视频宽高，只有视频开始且显示画面之后才有效

#### JMRTMPPlayerManager Listener:- kOnStreamPlayerPlayStatus
| Field                    | Must Exist | Type | Value | Description
| ----------------------- | ------- | ------- | ------ | -------
| status | YES | int | 1~14, "videoStatusPrepare": 正在准备播放; "videoStatusStart": 正在播放; "videoStatusStop": 停止播放；"videoStatusErrURLGet": 获取URL失败；"videoStatusErrURLInvalid": URL无效；"videoStatusErrOpenFail": 打开URL失败；"videoStatusErrOpenTimeout": 打开URL超时；"videoStatusErrPlayAbnormal": 播放异常或设备停止推流(会自动停止播放器)；"videoStatusErrHttpTimeout": http请求超时；"videoStatusErrHttpHost": 域名或IP错误；"videoStatusErrHttpParameter": http参数错误；"videoStatusErrServerData": 服务器数据解析异常；"videoStatusErrDeviceReplayFail": 设备回复“失败”；"videoStatusErrNetworkAnomaly": 网络异常；| 播放状态
| errCode | NO | int | > videoStatusStop，或其他服务器返回错误码| 播放错误码
| errMsg | NO | int | 错误提示语 | 错误提示语，仅当 status===videoStatusErrURLGet 时才有

#### JMRTMPPlayerManager Listener:- kOnStreamPlayerTalkStatus
| Field                    | Must Exist | Type | Value | Description
| ----------------------- | ------- | ------- | ------ | -------
| status | YES | int | 1~14, "talkStatusPrepare": 进行对讲准备工作; "talkStatusStart": 对讲已开始; "talkStatusStop": 对讲已停止；"talkStatusErrURLGet": 获取URL失败；"talkStatusErrInit": 初始化对讲URL失败；"talkStatusErrTalking": 正在对讲不能再次开始；"talkStatusErrSend": 发送音频数据失败；"talkStatusErrHttpTimeout": http请求超时；"talkStatusErrHttpHost": 域名或IP错误；"talkStatusErrHttpParameter": http参数错误；"talkStatusErrServerData": 服务器数据解析异常；"talkStatusErrDeviceReplayFail": 设备回复“失败”；"talkStatusErrNetworkAnomaly": 网络异常；"talkStatusErrAuthority": 无麦克风权限 | 播放状态
| errCode | NO | int | > talkStatusStop，或其他服务器返回错误码| 播放错误码
| errMsg | NO | int | 错误提示语 | 错误提示语，仅当 status===talkStatusErrURLGet 时才有

#### JMRTMPPlayerManager Listener:- kOnStreamPlayerRecordStatus
| Field                    | Must Exist | Type | Value | Description
| ----------------------- | ------- | ------- | ------ | -------
| status | YES | int | 1~7, "recordStatusStart": 开始录制; "recordStatusComplete": 录制完成; "recordStatusErrRecording": 正在录制；"recordStatusErrFail": 录制失败；"recordStatusErrSave": 保存失败；"recordStatusErrPath": 无效路径；"recordStatusErrAuthority": 无权限； | 录制状态
| filePath | YES | string | 录制视频的保存路径


#### JMRTMPPlayerManager Listener:- kOnStreamPlayerReceiveFrameInfo
| Field                    | Must Exist | Type | Value | Description
| ----------------------- | ------- | ------- | ------ | -------
| width | YES | int | 0 | 视频宽度
| height | YES | int | 0 | 视频高度
| videoBps | YES | int | 0 | 视频每秒码率
| audioBPS | YES | int | 0 | 音频每秒高度
| timestamp | YES | int | 0 | 视频时间戳
| totalFrameCount | YES | int | 0 | 视频每秒帧总数

#### JMRTMPPlayerManager Listener:- kOnStreamPlayerReceiveDeviceData
| Field                    | Must Exist | Type | Value | Description
| ----------------------- | ------- | ------- | ------ | -------
| undefine | YES | undefine | null | 回复内容有设备端决定，一般是json字符串或字符串

#### Demo示例
[https://github.com//Eafy/react-native-rtmp-player-jm/tree/master/example/App.js](example/App.js)
