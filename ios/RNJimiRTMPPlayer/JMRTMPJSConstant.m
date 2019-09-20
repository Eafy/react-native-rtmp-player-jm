//
//  JMRTMPJSConstant.m
//  DoubleConversion
//
//  Created by lzj<lizhijian_21@163.com> on 2019/9/20.
//

#import "JMRTMPJSConstant.h"
#import <JimiVideoPlayer/JMVideoStreamPlayer.h>

NSString *const kOnStreamPlayerPlayStatus = @"kOnStreamPlayerPlayStatus";
NSString *const kOnStreamPlayerTalkStatus = @"kOnStreamPlayerTalkStatus";
NSString *const kOnStreamPlayerRecordStatus = @"kOnStreamPlayerRecordStatus";
NSString *const kOnStreamPlayerReceiveFrameInfo = @"kOnStreamPlayerReceiveFrameInfo";
NSString *const kOnStreamPlayerReceiveDeviceData = @"kOnStreamPlayerReceiveDeviceData";

@implementation JMRTMPJSConstant

+ (NSDictionary *)constantsToExport
{
    NSMutableDictionary *dic = [NSMutableDictionary dictionary];
    [dic addEntriesFromDictionary:@{kOnStreamPlayerPlayStatus: kOnStreamPlayerPlayStatus,
                                    kOnStreamPlayerTalkStatus: kOnStreamPlayerTalkStatus,
                                    kOnStreamPlayerRecordStatus: kOnStreamPlayerRecordStatus,
                                    kOnStreamPlayerReceiveFrameInfo: kOnStreamPlayerReceiveFrameInfo,
                                    kOnStreamPlayerReceiveDeviceData: kOnStreamPlayerReceiveDeviceData
                                    }];

    [dic addEntriesFromDictionary:@{@"videoStatusPrepare": @(STREAM_VIDEO_STATUS_PREPARE),
                                    @"videoStatusStart": @(STREAM_VIDEO_STATUS_START),
                                    @"videoStatusStop": @(STREAM_VIDEO_STATUS_STOP),
                                    @"videoStatusErrURLGet": @(STREAM_VIDEO_STATUS_ERR_URL_GET),
                                    @"videoStatusErrURLInvalid": @(STREAM_VIDEO_STATUS_ERR_URL_INVALID),
                                    @"videoStatusErrOpenFail": @(STREAM_VIDEO_STATUS_ERR_OPEN_FAIL),
                                    @"videoStatusErrOpenTimeout": @(STREAM_VIDEO_STATUS_ERR_OPEN_TIMEOUT),
                                    @"videoStatusErrPlayAbnormal": @(STREAM_VIDEO_STATUS_ERR_PLAY_ABNORMAL),
                                    @"videoStatusErrHttpTimeout": @(STREAM_VIDEO_STATUS_ERR_HTTP_TIMEOUT),
                                    @"videoStatusErrHttpHost": @(STREAM_VIDEO_STATUS_ERR_HTTP_HOST),
                                    @"videoStatusErrHttpParameter": @(STREAM_VIDEO_STATUS_ERR_HTTP_PARAMETER),
                                    @"videoStatusErrServerData": @(STREAM_VIDEO_STATUS_ERR_SERVER_DATA),
                                    @"videoStatusErrDeviceReplayFail": @(STREAM_VIDEO_STATUS_ERR_DEVICE_REPLY_FAIL),
                                    @"videoStatusErrNetworkAnomaly": @(STREAM_VIDEO_STATUS_ERR_NETWORK_ANOMALY)
                                    }];

    [dic addEntriesFromDictionary:@{@"talkStatusPrepare": @(STREAM_TALK_STATUS_PREPARE),
                                    @"talkStatusStart": @(STREAM_TALK_STATUS_START),
                                    @"talkStatusStop": @(STREAM_TALK_STATUS_STOP),
                                    @"talkStatusErrURLGet": @(STREAM_TALK_STATUS_ERR_URL_GET),
                                    @"talkStatusErrInit": @(STREAM_TALK_STATUS_ERR_INIT),
                                    @"talkStatusErrTalking": @(STREAM_TALK_STATUS_ERR_TALKING),
                                    @"talkStatusErrSend": @(STREAM_TALK_STATUS_ERR_SEND),
                                    @"talkStatusErrHttpTimeout": @(STREAM_TALK_STATUS_ERR_HTTP_TIMEOUT),
                                    @"talkStatusErrHttpHost": @(STREAM_TALK_STATUS_ERR_HTTP_HOST),
                                    @"talkStatusErrHttpParameter": @(STREAM_TALK_STATUS_ERR_HTTP_PARAMETER),
                                    @"talkStatusErrServerData": @(STREAM_TALK_STATUS_ERR_SERVER_DATA),
                                    @"talkStatusErrDeviceReplayFail": @(STREAM_TALK_STATUS_ERR_DEVICE_REPLY_FAIL),
                                    @"talkStatusErrNetworkAnomaly": @(STREAM_TALK_STATUS_ERR_NETWORK_ANOMALY),
                                    @"talkStatusErrAuthority": @(STREAM_TALK_STATUS_ERR_AUTHORITY)
                                    }];

    [dic addEntriesFromDictionary:@{@"recordStatusStart": @(STREAM_RECORD_STATUS_START),
                                    @"recordStatusComplete": @(STREAM_RECORD_STATUS_COMPLETE),
                                    @"recordStatusErrRecording": @(STREAM_RECORD_STATUS_ERR_RECORDING),
                                    @"recordStatusErrFail": @(STREAM_RECORD_STATUS_ERR_FAIL),
                                    @"recordStatusErrSave": @(STREAM_RECORD_STATUS_ERR_SAVE),
                                    @"recordStatusErrPath": @(STREAM_RECORD_STATUS_ERR_PATH),
                                    @"recordStatusErrAuthority": @(STREAM_RECORD_STATUS_ERR_AUTHORITY),
                                    }];

    [dic addEntriesFromDictionary:@{@"receiveCmdPlaybackOneEnd": @(STREAM_RECEIVE_CMD_PLAYBACK_FILE_END),
                                    @"receiveCmdPlaybackAllEnd": @(STREAM_RECEIVE_CMD_PLAYBACK_ALL_END),
                                    }];

    return dic;
}

@end
