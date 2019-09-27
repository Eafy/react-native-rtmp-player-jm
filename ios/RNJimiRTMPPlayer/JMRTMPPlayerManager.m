//
//  JMRTMPPlayerManager.m
//  RNJimiRTMPPlayer
//
//  Created by lzj<lizhijian_21@163.com> on 2019/8/27.
//  Copyright © 2019 Jimi. All rights reserved.
//

#import "JMRTMPPlayerManager.h"
#import "JMRTMPMonitorManager.h"
#import "JMRTMPJSConstant.h"
#import <JimiVideoPlayer/JMVideoStreamPlayer.h>
#import <JimiVideoPlayer/JMLogController.h>

JMVideoStreamPlayer *gJMVideoStreamPlayer = nil;

@interface JMRTMPPlayerManager() <JMVideoStreamPlayerDelegate>

@property (nonatomic, assign) BOOL hasListeners;

@end

@implementation JMRTMPPlayerManager

RCT_EXPORT_MODULE(JMRTMPPlayerManager);

- (instancetype)init
{

}

- (void)startObserving {
    self.hasListeners = YES;
}

- (void)stopObserving {
    self.hasListeners = NO;
}

- (NSArray<NSString *> *)supportedEvents
{
    return @[kOnStreamPlayerPlayStatus, kOnStreamPlayerTalkStatus, kOnStreamPlayerRecordStatus, kOnStreamPlayerReceiveFrameInfo, kOnStreamPlayerReceiveDeviceData];
}

- (NSDictionary *)constantsToExport
{
    return [JMRTMPJSConstant constantsToExport];
}

- (void)sendEventWithName:(NSString *)eventName body:(id)body
{
    if (self.hasListeners) {
        [super sendEventWithName:eventName body:body];
    }
}

- (void)didJMSmartAppEngineExit
{
    [self deInitialize];
    gJMRTMPMonitor = nil;
}

#pragma mark -

RCT_EXPORT_METHOD(initialize:(NSString *)key
                  secret:(NSString *)secret
                  imei:(NSString *_Nonnull)imei) {
    if (gJMVideoStreamPlayer != nil) return;

    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(didJMSmartAppEngineExit) name:@"kJMSmartAppEngineExit" object:nil];
    gJMVideoStreamPlayer = [[JMVideoStreamPlayer alloc] initWithKey:key secret:secret imei:imei token:nil];
    gJMVideoStreamPlayer.delegate = self;
    [gJMVideoStreamPlayer attachMonitor:gJMRTMPMonitor];
}

RCT_EXPORT_METHOD(deInitialize) {
    if (gJMVideoStreamPlayer != nil) {
        [[NSNotificationCenter defaultCenter] removeObserver:self];

        gJMVideoStreamPlayer.delegate = nil;
        [gJMVideoStreamPlayer deattachMonitor];
        [gJMVideoStreamPlayer stop];
        gJMVideoStreamPlayer = nil;
    }
}

RCT_EXPORT_METHOD(startPlayLive) {
    [gJMVideoStreamPlayer startPlayLive];
}

RCT_EXPORT_METHOD(startPlay:(NSString *)url) {
    [gJMVideoStreamPlayer startPlay:url];
}

RCT_EXPORT_METHOD(startPlayback:(NSArray *)fileNameArray) {
    [gJMVideoStreamPlayer startPlayback:fileNameArray];
}

RCT_EXPORT_METHOD(stopPlay) {
    [gJMVideoStreamPlayer stopPlay];
}

RCT_EXPORT_METHOD(startTalk) {
    [gJMVideoStreamPlayer startTalk];
}

RCT_EXPORT_METHOD(stopTalk) {
    [gJMVideoStreamPlayer stopTalk];
}

RCT_EXPORT_METHOD(stop) {
    [gJMVideoStreamPlayer stopTalk];
}

RCT_EXPORT_METHOD(switchCamera:(BOOL)isFront autoPlay:(BOOL)bAuto resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    [gJMVideoStreamPlayer switchCamera:isFront autoPlay:bAuto handler:^(BOOL success, NSString * _Nullable url, NSInteger code, NSString * _Nullable errMsg) {
        if (success && code == 0 && url) {
            resolve(url);
        } else {
            reject([NSString stringWithFormat:@"%ld", (long)code], errMsg ? errMsg : @"", nil);
        }
    }];
}

RCT_EXPORT_METHOD(snapshot:(NSString *)filePath resolve:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {

    __block NSString *filePathT = filePath;
    dispatch_async(dispatch_get_main_queue(), ^{
        UIImage *img = [gJMVideoStreamPlayer snapshot];
        if (img) {
            if (!filePathT) {
                filePathT = NSTemporaryDirectory();
            } else if ([filePathT hasPrefix:@"./"]) {
                filePathT = [NSTemporaryDirectory() stringByAppendingPathComponent:[filePathT substringFromIndex:1]];
            }

            NSString *extensionName = filePathT.pathExtension;
            if (!extensionName || !extensionName.length) {
                if (![[NSFileManager defaultManager] fileExistsAtPath:filePathT]) {
                    [[NSFileManager defaultManager] createDirectoryAtPath:filePathT withIntermediateDirectories:YES attributes:nil error:nil];
                }
                filePathT = [filePathT stringByAppendingPathComponent:@"tempSnapshot.png"];
            } else {
                NSArray *arr = [filePathT pathComponents];
                NSString *fileName = [arr lastObject];
                NSString *path = [filePathT substringToIndex:filePathT.length - fileName.length];
                if (![[NSFileManager defaultManager] fileExistsAtPath:path]) {
                    [[NSFileManager defaultManager] createDirectoryAtPath:path withIntermediateDirectories:YES attributes:nil error:nil];
                }
            }

            if ([UIImagePNGRepresentation(img) writeToFile:filePathT atomically:YES]) {
                resolve(filePathT);
            } else {
                reject(@"-2", @"Failed to save image", nil);
            }
        } else {
            reject(@"-1", @"Failed to do snapshot", nil);
        }
    });
}

RCT_EXPORT_METHOD(startRecording:(NSString *)filePath) {
    if (!filePath) {
        filePath = [NSTemporaryDirectory() stringByAppendingPathComponent:@"tempRecording.mp4"];
    } else if ([filePath hasPrefix:@"./"]) {
        filePath = [NSTemporaryDirectory() stringByAppendingPathComponent:[filePath substringFromIndex:1]];
    }
    [gJMVideoStreamPlayer startRecording:filePath];
}

RCT_EXPORT_METHOD(stopRecording) {
    [gJMVideoStreamPlayer stopRecording];
}

RCT_EXPORT_METHOD(isRecording:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    BOOL isRecording = [gJMVideoStreamPlayer isRecording];
    resolve(@(isRecording));
}

RCT_EXPORT_METHOD(getRecordingDuration:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    long time = [gJMVideoStreamPlayer getRecordingRuration];
    resolve(@(time));
}

RCT_EXPORT_METHOD(sendCustomRequest:(NSDictionary *)paraDic resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    [gJMVideoStreamPlayer sendRequestWithParameterDic:paraDic completionHandler:^(BOOL success, long statusCode, const char * _Nullable dataJsonStr) {
        NSString *msg = @"";
        if (dataJsonStr) {
            msg = [NSString stringWithUTF8String:dataJsonStr];
        }
        if (success && statusCode == 200) {
            resolve(msg);
        } else {
            reject([NSString stringWithFormat:@"%ld", (long)statusCode], msg, nil);
        }
    }];
}

RCT_EXPORT_METHOD(setMute:(BOOL)mute) {
   gJMVideoStreamPlayer.mute = mute;
}

RCT_EXPORT_METHOD(getMute:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    resolve(@(gJMVideoStreamPlayer ? gJMVideoStreamPlayer.mute : NO));
}

RCT_EXPORT_METHOD(videoSize:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *dic = @{@"width": @(gJMVideoStreamPlayer.videoWidth),
                          @"hieght": @(gJMVideoStreamPlayer.videoHeight)};
    resolve(dic);
}

#pragma mark -

- (NSMutableDictionary *)getEmptyBody {
    NSMutableDictionary *body = @{}.mutableCopy;
    return body;
}

#pragma mark - JMVideoStreamPlayerDelegate

- (void)didStreamPlayerPlayWithStatus:(STREAM_PLAY_STATUS)status errStr:(NSString *_Nullable)errStr
{
    NSMutableDictionary *body = [self getEmptyBody];
    if (status <= STREAM_VIDEO_STATUS_STOP) {
        body[@"status"] = @(status);
    } else {
        body[@"status"] = @(status);
        body[@"errCode"] = @(status);
        if (status == STREAM_VIDEO_STATUS_ERR_URL_GET) {
            body[@"errMsg"] = errStr;
        }
    }

    [self sendEventWithName:kOnStreamPlayerPlayStatus body:body];
}

- (void)didStreamPlayerTalkWithStatus:(STREAM_TALK_STATUS)status errStr:(NSString *_Nullable)errStr
{
    NSMutableDictionary *body = [self getEmptyBody];
    if (status <= STREAM_TALK_STATUS_STOP) {
        body[@"status"] = @(status);
    } else {
        body[@"errCode"] = @(status);
        if (status == STREAM_TALK_STATUS_ERR_URL_GET) {
            body[@"errMsg"] = errStr;
        }
    }

    [self sendEventWithName:kOnStreamPlayerTalkStatus body:body];
}

- (void)didStreamPlayerRecordWithStatus:(STREAM_RECORD_STATUS)status path:(NSString *_Nullable)filePath
{
    NSMutableDictionary *body = [self getEmptyBody];
    body[@"status"] = @(status);
    body[@"filePath"] = filePath ? filePath : @"";

    [self sendEventWithName:kOnStreamPlayerRecordStatus body:body];
}

- (void)didStreamPlayerReceiveFrameInfoWithVideoWidth:(NSInteger)videoWidth videoHeight:(NSInteger)videoHeight videoBPS:(NSInteger)videoBps audioBPS:(NSInteger)audioBps timestamp:(NSUInteger)timestamp totalFrameCount:(NSInteger)totalFrameCount
{
    NSMutableDictionary *body = [self getEmptyBody];
    body[@"width"] = @(videoWidth);
    body[@"height"] = @(videoHeight);
    body[@"videoBps"] = @(videoBps);
    body[@"audioBPS"] = @(audioBps);
    body[@"timestamp"] = @(timestamp);
    body[@"totalFrameCount"] = @(totalFrameCount);

    [self sendEventWithName:kOnStreamPlayerReceiveFrameInfo body:body];
}


- (void)didServerReceiveData:(NSData *_Nullable)data codeType:(NSInteger)type
{
    if (!data) return;

    NSString *result = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
    if (result) {
        [self sendEventWithName:kOnStreamPlayerReceiveDeviceData body:result];
    }
}

@end
