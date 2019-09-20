package com.jimi.rn.rtmp;

import java.util.HashMap;
import java.util.Map;

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

public class JMRTMPJSConstant {
    public static String kOnStreamPlayerPlayStatus = "kOnStreamPlayerPlayStatus";
    public static String kOnStreamPlayerTalkStatus = "kOnStreamPlayerTalkStatus";
    public static String kOnStreamPlayerRecordStatus = "kOnStreamPlayerRecordStatus";
    public static String kOnStreamPlayerReceiveFrameInfo = "kOnStreamPlayerReceiveFrameInfo";
    public static String kOnStreamPlayerReceiveDeviceData = "kOnStreamPlayerReceiveDeviceData";

    public static Map<String, Object> constantsToExport() {
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

}
