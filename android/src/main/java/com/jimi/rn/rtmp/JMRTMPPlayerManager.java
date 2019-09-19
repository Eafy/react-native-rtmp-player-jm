package com.jimi.rn.rtmp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.jimi.jimivideoplayer.JMHttpRequestTaskListener;
import com.jimi.jimivideoplayer.JMSwitchCameraListener;
import com.jimi.jimivideoplayer.JMVideoStreamPlayer;
import com.jimi.jimivideoplayer.JMVideoStreamPlayerListener;
import com.jimi.jimivideoplayer.bean.FrameInfo;
import com.jimi.jimivideoplayer.log.JMLogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_RECEIVE_CMD_PLAYBACK_ALL_END;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_RECEIVE_CMD_PLAYBACK_FILE_END;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_RECORD_STATUS_COMPLETE;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_RECORD_STATUS_ERR_AUTHORITY;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_RECORD_STATUS_ERR_FAIL;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_RECORD_STATUS_ERR_PATH;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_RECORD_STATUS_ERR_RECORDING;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_RECORD_STATUS_ERR_SAVE;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_RECORD_STATUS_START;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_TALK_STATUS_ERR_AUTHORITY;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_TALK_STATUS_ERR_DEVICE_REPLY_FAIL;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_TALK_STATUS_ERR_HTTP_HOST;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_TALK_STATUS_ERR_HTTP_PARAMETER;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_TALK_STATUS_ERR_HTTP_TIMEOUT;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_TALK_STATUS_ERR_INIT;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_TALK_STATUS_ERR_NETWORK_ANOMALY;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_TALK_STATUS_ERR_SEND;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_TALK_STATUS_ERR_SERVER_DATA;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_TALK_STATUS_ERR_TALKING;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_TALK_STATUS_ERR_URL_GET;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_TALK_STATUS_PREPARE;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_TALK_STATUS_START;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_TALK_STATUS_STOP;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_VIDEO_STATUS_ERR_DEVICE_REPLY_FAIL;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_VIDEO_STATUS_ERR_HTTP_HOST;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_VIDEO_STATUS_ERR_HTTP_PARAMETER;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_VIDEO_STATUS_ERR_HTTP_TIMEOUT;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_VIDEO_STATUS_ERR_NETWORK_ANOMALY;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_VIDEO_STATUS_ERR_OPEN_FAIL;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_VIDEO_STATUS_ERR_OPEN_TIMEOUT;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_VIDEO_STATUS_ERR_PLAY_ABNORMAL;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_VIDEO_STATUS_ERR_SERVER_DATA;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_VIDEO_STATUS_ERR_URL_GET;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_VIDEO_STATUS_ERR_URL_INVALID;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_VIDEO_STATUS_PREPARE;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_VIDEO_STATUS_START;
import static com.jimi.jimivideoplayer.JMVideoStreamPlayerListener.STREAM_VIDEO_STATUS_STOP;

public class JMRTMPPlayerManager extends ReactContextBaseJavaModule {

    private static String kOnStreamPlayerPlayStatus = "kOnStreamPlayerPlayStatus";
    private static String kOnStreamPlayerTalkStatus = "kOnStreamPlayerTalkStatus";
    private static String kOnStreamPlayerRecordStatus = "kOnStreamPlayerRecordStatus";
    private static String kOnStreamPlayerReceiveFrameInfo = "kOnStreamPlayerReceiveFrameInfo";
    private static String kOnStreamPlayerReceiveDeviceData = "kOnStreamPlayerReceiveDeviceData";

    private static JMVideoStreamPlayer gJMVideoStreamPlayer;
    private ReactApplicationContext mContext;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getAction();
            if (name == "com.jimi.rn.kJMSmartAppEngineExit") {
                new Thread(new Runnable() {
                    public void run() {
                        deInitialize();
                        if (JMRTMPMonitorManager.glMonitor != null) {
                            JMRTMPMonitorManager.glMonitor = null;
                        }
                    }
                });
            }
        }
    };

    @Override
    public String getName() {
        return "JMRTMPPlayerManager";
    }

    public JMRTMPPlayerManager(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.jimi.rn.kJMSmartAppEngineExit");
        reactContext.registerReceiver(mBroadcastReceiver, intentFilter);

        reactContext.addLifecycleEventListener(mLifecycleEventListener);
    }

    private final LifecycleEventListener mLifecycleEventListener = new LifecycleEventListener() {
        @Override
        public void onHostResume() {
            JMRTMPMonitorManager.isResume = true;
            if (JMRTMPMonitorManager.glMonitor != null) {
                JMRTMPMonitorManager.glMonitor.onResume();
            }
        }

        @Override
        public void onHostPause() {
            JMRTMPMonitorManager.isResume = false;
            if (JMRTMPMonitorManager.glMonitor != null) {
                JMRTMPMonitorManager.glMonitor.onPause();
            }
        }

        @Override
        public void onHostDestroy() {
            JMRTMPMonitorManager.isResume = false;
            if (mContext != null && mBroadcastReceiver != null) {
                mContext.unregisterReceiver(mBroadcastReceiver);
                mBroadcastReceiver = null;
            }
        }
    };

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> constants = new HashMap<>();
        constants.put(kOnStreamPlayerPlayStatus, kOnStreamPlayerPlayStatus);
        constants.put(kOnStreamPlayerTalkStatus, kOnStreamPlayerTalkStatus);
        constants.put(kOnStreamPlayerRecordStatus, kOnStreamPlayerRecordStatus);
        constants.put(kOnStreamPlayerReceiveFrameInfo, kOnStreamPlayerReceiveFrameInfo);
        constants.put(kOnStreamPlayerReceiveDeviceData, kOnStreamPlayerReceiveDeviceData);

        constants.put("videoStatusPrepare", STREAM_VIDEO_STATUS_PREPARE);
        constants.put("videoStatusStart", STREAM_VIDEO_STATUS_START);
        constants.put("videoStatusStop", STREAM_VIDEO_STATUS_STOP);
        constants.put("videoStatusErrURLGet", STREAM_VIDEO_STATUS_ERR_URL_GET);
        constants.put("videoStatusErrURLInvalid", STREAM_VIDEO_STATUS_ERR_URL_INVALID);
        constants.put("videoStatusErrOpenFail", STREAM_VIDEO_STATUS_ERR_OPEN_FAIL);
        constants.put("videoStatusErrOpenTimeout", STREAM_VIDEO_STATUS_ERR_OPEN_TIMEOUT);
        constants.put("videoStatusErrPlayAbnormal", STREAM_VIDEO_STATUS_ERR_PLAY_ABNORMAL);
        constants.put("videoStatusErrHttpTimeout", STREAM_VIDEO_STATUS_ERR_HTTP_TIMEOUT);
        constants.put("videoStatusErrHttpHost", STREAM_VIDEO_STATUS_ERR_HTTP_HOST);
        constants.put("videoStatusErrHttpParameter", STREAM_VIDEO_STATUS_ERR_HTTP_PARAMETER);
        constants.put("videoStatusErrServerData", STREAM_VIDEO_STATUS_ERR_SERVER_DATA);
        constants.put("videoStatusErrDeviceReplayFail", STREAM_VIDEO_STATUS_ERR_DEVICE_REPLY_FAIL);
        constants.put("videoStatusErrNetworkAnomaly", STREAM_VIDEO_STATUS_ERR_NETWORK_ANOMALY);

        constants.put("talkStatusPrepare", STREAM_TALK_STATUS_PREPARE);
        constants.put("talkStatusStart", STREAM_TALK_STATUS_START);
        constants.put("talkStatusStop", STREAM_TALK_STATUS_STOP);
        constants.put("talkStatusErrURLGet", STREAM_TALK_STATUS_ERR_URL_GET);
        constants.put("talkStatusErrInit", STREAM_TALK_STATUS_ERR_INIT);
        constants.put("talkStatusErrTalking", STREAM_TALK_STATUS_ERR_TALKING);
        constants.put("talkStatusErrSend", STREAM_TALK_STATUS_ERR_SEND);
        constants.put("talkStatusErrHttpTimeout", STREAM_TALK_STATUS_ERR_HTTP_TIMEOUT);
        constants.put("talkStatusErrHttpHost", STREAM_TALK_STATUS_ERR_HTTP_HOST);
        constants.put("talkStatusErrHttpParameter", STREAM_TALK_STATUS_ERR_HTTP_PARAMETER);
        constants.put("talkStatusErrServerData", STREAM_TALK_STATUS_ERR_SERVER_DATA);
        constants.put("talkStatusErrDeviceReplayFail", STREAM_TALK_STATUS_ERR_DEVICE_REPLY_FAIL);
        constants.put("talkStatusErrNetworkAnomaly", STREAM_TALK_STATUS_ERR_NETWORK_ANOMALY);
        constants.put("talkStatusErrAuthority", STREAM_TALK_STATUS_ERR_AUTHORITY);

        constants.put("recordStatusStart", STREAM_RECORD_STATUS_START);
        constants.put("recordStatusComplete", STREAM_RECORD_STATUS_COMPLETE);
        constants.put("recordStatusErrRecording", STREAM_RECORD_STATUS_ERR_RECORDING);
        constants.put("recordStatusErrFail", STREAM_RECORD_STATUS_ERR_FAIL);
        constants.put("recordStatusErrSave", STREAM_RECORD_STATUS_ERR_SAVE);
        constants.put("recordStatusErrPath", STREAM_RECORD_STATUS_ERR_PATH);
        constants.put("recordStatusErrAuthority", STREAM_RECORD_STATUS_ERR_AUTHORITY);

        constants.put("receiveCmdPlaybackOneEnd", STREAM_RECEIVE_CMD_PLAYBACK_FILE_END);
        constants.put("receiveCmdPlaybackAllEnd", STREAM_RECEIVE_CMD_PLAYBACK_ALL_END);

        return constants;
    }

    @ReactMethod
    public void initialize(String key, String secret, String imei) {
        this.deInitialize();

        gJMVideoStreamPlayer = new JMVideoStreamPlayer(mContext, key, secret, imei, null, mListener);
        gJMVideoStreamPlayer.attachGlMonitor(JMRTMPMonitorManager.glMonitor);
        JMRTMPMonitorManager.setIsResume(true);
    }

    @ReactMethod
    public void deInitialize() {
        if (gJMVideoStreamPlayer != null) {
            gJMVideoStreamPlayer.addVideoStreamPlayerListener(null);
            gJMVideoStreamPlayer.deattachMonitor();
            gJMVideoStreamPlayer.stop();
            gJMVideoStreamPlayer = null;
        }
    }

    @ReactMethod
    public void startPlayLive() {
        if (gJMVideoStreamPlayer != null) {
            gJMVideoStreamPlayer.startPlayLive();
        }
    }

    @ReactMethod
    public void startPlay(String url) {
        if (gJMVideoStreamPlayer != null) {
            gJMVideoStreamPlayer.startPlay(url);
        }
    }

    @ReactMethod
    public void startPlayback(ReadableArray fileNameArray) {
        if (gJMVideoStreamPlayer != null) {
            ArrayList<Object> listObj = fileNameArray.toArrayList();

            List list = new ArrayList();
            for(int i=0; i< listObj.size(); i++) {
                list.add(listObj.get(i));
            }

            gJMVideoStreamPlayer.startPlayback(list);
        }
    }

    @ReactMethod
    public void stopPlay() {
        if (gJMVideoStreamPlayer != null) {
            gJMVideoStreamPlayer.stopPlay();
        }
    }

    @ReactMethod
    public void startTalk() {
        if (gJMVideoStreamPlayer != null) {
            gJMVideoStreamPlayer.startTalk();
        }
    }

    @ReactMethod
    public void stopTalk() {
        if (gJMVideoStreamPlayer != null) {
            gJMVideoStreamPlayer.stopTalk();
        }
    }

    @ReactMethod
    public void stop() {
        if (gJMVideoStreamPlayer != null) {
            gJMVideoStreamPlayer.stop();
        }
    }

    @ReactMethod
    public void switchCamera(Boolean isFront, boolean autoPlay, Promise promise) {
        if (gJMVideoStreamPlayer != null) {
            gJMVideoStreamPlayer.switchCamera(isFront, autoPlay, new JMSwitchCameraListener() {
                @Override
                public void onSwitchCameraHandler(boolean b, String s, long l, String s1) {
                    if (b && l == 0 && !TextUtils.isEmpty(s)) {
                        promise.resolve(s);
                    } else {
                        promise.reject(String.valueOf(l), s1 != null ? s1 : "");
                    }
                }
            });
        }
    }

    @ReactMethod
    public void snapshot(String filePath, Promise promise) {
        if (gJMVideoStreamPlayer != null) {
            Bitmap bitmap = gJMVideoStreamPlayer.snapshot();
            if (bitmap != null) {
                if (TextUtils.isEmpty(filePath)) {
                    promise.reject("-2", "Err: Invalid file path");
                    return;
                } else {
                    if (filePath.length() <= 3 || (!filePath.endsWith(".png") && !filePath.endsWith(".PNG"))) {
                        promise.reject("-3", "Err: Invalid file suffix");
                        return;
                    } else {
                        String filePath1 = filePath.substring(0, filePath.lastIndexOf("/"));
                        File file = new File(filePath1);
                        if (!file.exists()) {
                            if (file.mkdirs()) {
                                promise.reject("-2", "Err: Invalid file path");
                                return;
                            }
                        }

                        File imageFile = new File(filePath);
                        try {
                            FileOutputStream fos = new FileOutputStream(imageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            fos.flush();
                            fos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            promise.reject("-3", "Err: Save image");
                        }
                    }
                }
            } else {
                promise.reject("-1", "Failed to do snapshot");
            }
        }
    }

    @ReactMethod
    public void startRecording(String filePath) {
        if (gJMVideoStreamPlayer != null) {
            if (TextUtils.isEmpty(filePath)) {
                filePath = "";
            }
            gJMVideoStreamPlayer.startRecording(filePath);
        }
    }

    @ReactMethod
    public void stopRecording() {
        if (gJMVideoStreamPlayer != null) {
            gJMVideoStreamPlayer.stopRecording();
        }
    }

    @ReactMethod
    public void isRecording(Promise promise) {
        if (gJMVideoStreamPlayer != null) {
            promise.resolve(gJMVideoStreamPlayer.isRecording());
        } else {
            promise.resolve(false);
        }
    }

    @ReactMethod
    public void getRecordingDuration(Promise promise) {
        if (gJMVideoStreamPlayer != null) {
            promise.resolve(gJMVideoStreamPlayer.getRecordingRuration());
        } else {
            promise.resolve(0);
        }
    }

    @ReactMethod
    public void sendCustomRequest(ReadableMap paraDic, Promise promise) {
        if (gJMVideoStreamPlayer != null) {
            HashMap<String, Object> mapT = paraDic.toHashMap();
            HashMap<String, String> map = new HashMap();

            for (Iterator var5 = mapT.keySet().iterator(); var5.hasNext();) {
                String key = (String)var5.next();
                String value = String.valueOf(mapT.get(key));
                map.put(key, value);
            }

            gJMVideoStreamPlayer.sendAsyncRequest(map, new JMHttpRequestTaskListener() {
                @Override
                public void onCustomMsgHandler(boolean b, long l, String s) {
                    String msg = "";
                    if (s != null) {
                        msg = s;
                    }

                    if (b && l == 200) {
                        promise.resolve(msg);
                    } else {
                        promise.reject(String.valueOf(l), msg);
                    }
                }
            });
        }
    }

    @ReactMethod
    public void setMute(boolean mute) {
        if (gJMVideoStreamPlayer != null) {
            gJMVideoStreamPlayer.setMute(mute);
        }
    }

    @ReactMethod
    public void getMute(boolean mute, Promise promise) {
        if (gJMVideoStreamPlayer != null) {
            promise.resolve(gJMVideoStreamPlayer.getMute());
        } else {
            promise.resolve(false);
        }
    }

    @ReactMethod
    public void getVideoSize(Promise promise) {
        HashMap<String, Object> map = new HashMap();
        map.put("width", 0);
        map.put("height", 0);

        if (gJMVideoStreamPlayer != null) {
            map.put("width", JMVideoStreamPlayer.mVideoWidth);
            map.put("height", JMVideoStreamPlayer.mVideoHeight);
        }
        promise.resolve(map);
    }

    private JMVideoStreamPlayerListener mListener = new JMVideoStreamPlayerListener() {
        public void onStreamPlayerPlayStatus(int status, String errStr) {
        }

        public void onStreamPlayerTalkStatus(int status, String errStr) {
        }

        public void onStreamPlayerRecordStatus(int status, String filePath) {
        }

        public void onStreamPlayerReceiveFrameInfo(FrameInfo frameInfo) {
        }

        public void onServerReceiveData(String data, int type) {
            JMLogUtil.d("onServerReceiveData: " + data + ",Type:" + type);

            try {
                JSONObject jsonObject = new JSONObject(data);
                if (jsonObject.has("code")) {
                    int code = jsonObject.getInt("code");
                    switch (code) {
                        case JMVideoStreamPlayerListener.STREAM_RECEIVE_CMD_PLAYBACK_FILE_END://单个文件回放结束
                            String filePath = jsonObject.getString("filePath");
                            JMLogUtil.d(filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()) + "文件回放结束");
                            break;
                        case JMVideoStreamPlayerListener.STREAM_RECEIVE_CMD_PLAYBACK_ALL_END://请求的全部回放文件播放结束
                            JMLogUtil.d("回放文件已全部播完");
                            break;
                        default:
                            JMLogUtil.d("自定义消息数据");
                            break;
                    }
                } else {
                    JMLogUtil.d("自定义消息数据");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    };
}
