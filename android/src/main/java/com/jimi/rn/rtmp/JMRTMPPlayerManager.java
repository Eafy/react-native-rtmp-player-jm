package com.jimi.rn.rtmp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.jimi.jimivideoplayer.JMHttpRequestTaskListener;
import com.jimi.jimivideoplayer.JMSwitchCameraListener;
import com.jimi.jimivideoplayer.JMVideoStreamPlayer;
import com.jimi.jimivideoplayer.JMVideoStreamPlayerListener;
import com.jimi.jimivideoplayer.bean.FrameInfo;
import com.jimi.jimivideoplayer.log.JMLogUtil;
import com.jimi.jimivideoplayer.opengl.GLMonitor;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static com.jimi.rn.rtmp.JMRTMPJSConstant.kOnStreamPlayerPlayStatus;
import static com.jimi.rn.rtmp.JMRTMPJSConstant.kOnStreamPlayerReceiveDeviceData;
import static com.jimi.rn.rtmp.JMRTMPJSConstant.kOnStreamPlayerReceiveFrameInfo;
import static com.jimi.rn.rtmp.JMRTMPJSConstant.kOnStreamPlayerRecordStatus;
import static com.jimi.rn.rtmp.JMRTMPJSConstant.kOnStreamPlayerTalkStatus;

public class JMRTMPPlayerManager extends ReactContextBaseJavaModule {

    private static JMVideoStreamPlayer gJMVideoStreamPlayer;
    private ReactApplicationContext mContext;
    private GLMonitor glMonitor = null;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getAction();
            if (name == "com.jimi.rn.kJMSmartAppEngineExit") {
                new Thread(new Runnable() {
                    public void run() {
                        deInitialize();
                        glMonitor = null;
                        JMRTMPMonitorManager.removeGLMonitor();
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
            if (glMonitor != null) {
                glMonitor.onResume();
            }
        }

        @Override
        public void onHostPause() {
            JMRTMPMonitorManager.isResume = false;
            if (glMonitor != null) {
                glMonitor.onPause();
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
        return JMRTMPJSConstant.constantsToExport();
    }

    @ReactMethod
    public void initialize(String key, String secret, String imei) {
        if (gJMVideoStreamPlayer != null) {
            return;
        }

        glMonitor = JMRTMPMonitorManager.getGLMonitor();
        gJMVideoStreamPlayer = new JMVideoStreamPlayer(mContext, key, secret, imei, null, mListener);
        gJMVideoStreamPlayer.attachGlMonitor(glMonitor);
    }

    @ReactMethod
    public void deInitialize() {
        if (gJMVideoStreamPlayer != null) {
            gJMVideoStreamPlayer.addVideoStreamPlayerListener(null);
            gJMVideoStreamPlayer.deattachMonitor();
            gJMVideoStreamPlayer.stop();
            gJMVideoStreamPlayer.release();
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
                            promise.resolve(filePath);
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
            WritableMap event = Arguments.createMap();
            if (status <= STREAM_VIDEO_STATUS_STOP) {
                event.putInt("status", status);
            } else {
                event.putInt("status", status);
                event.putInt("errCode", status);
                if (status == STREAM_VIDEO_STATUS_ERR_URL_GET) {
                    event.putString("errMsg", errStr);
                }
            }
            mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(kOnStreamPlayerPlayStatus, event);
        }

        public void onStreamPlayerTalkStatus(int status, String errStr) {
            WritableMap event = Arguments.createMap();
            if (status <= STREAM_TALK_STATUS_STOP) {
                event.putInt("status", status);
            } else {
                event.putInt("status", status);
                event.putInt("errCode", status);
                if (status == STREAM_TALK_STATUS_ERR_URL_GET) {
                    event.putString("errMsg", errStr);
                }
            }
            mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(kOnStreamPlayerTalkStatus, event);
        }

        public void onStreamPlayerRecordStatus(int status, String filePath) {
            WritableMap event = Arguments.createMap();
            event.putInt("status", status);
            event.putString("filePath", (filePath != null) ? filePath : "");
            mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(kOnStreamPlayerRecordStatus, event);
        }

        public void onStreamPlayerReceiveFrameInfo(FrameInfo frameInfo) {
            WritableMap event = Arguments.createMap();
            event.putInt("width", frameInfo.videoWidth);
            event.putInt("height", frameInfo.videoHeight);
            event.putInt("videoBps", frameInfo.videoBps);
            event.putInt("audioBPS", frameInfo.audioBps);
            event.putInt("timestamp", (int)frameInfo.timestamp);
            event.putInt("totalFrameCount", frameInfo.totalFrameCount);
            event.putInt("onlineCount", frameInfo.onlineCount);
            mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(kOnStreamPlayerReceiveFrameInfo, event);
        }

        public void onServerReceiveData(String data, int type) {
            JMLogUtil.d("onServerReceiveData: " + data + ",Type:" + type);
            mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(kOnStreamPlayerReceiveDeviceData, data);
        }

    };
}
