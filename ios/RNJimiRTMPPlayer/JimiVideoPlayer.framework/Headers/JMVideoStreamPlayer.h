//
//  JMVideoStreamPlayer.h
//  JimiVideoPlayer
//
//  Created by lzj<lizhijian_21@163.com> on 2018/9/6.
//  Copyright © 2018年 lzj<lizhijian_21@163.com>. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Monitor.h"
#import "GLMonitor.h"

/**
 自定义消息回调block

 @param success 是否请求成功(不表示功能正常，只表示http请求成功，也可以用statusCode==200来判断)
 @param statusCode 表示Http请求的状态。 http访问的状态码:http://tool.oschina.net/commons?type=5，正常则为200，其他自定义状态码：访问超时HTTP_ERR_TIMEOUT = 32，Host错误HTTP_ERR_HOST = 33,，服务器数据解析异常HTTP_ERR_SERVER_DATA = 34,
 @param dataJsonStr 具体业务需求的回复内容，解析成功则为data数据内容，否则返回整个接收到的数据
 */
typedef void(^JMCustomMsgHandler)(BOOL success, long statusCode, const char * _Nullable dataJsonStr);

typedef void JMHttpRequestTask;     //http访问句柄

/**
 摄像机切换回调Block

 @param success 是否成功
 @param url 切换成功之后的推流地址
 */

/**
 摄像机切换回调Block

 @param success 是否成功
 @param url 切换成功之后的推流地址
 @param code 服务器错误代码，0：成功，-1:回复数据为空，-2:回复数据非Json字符串，-3:异常数据，-4: 设备拒绝切换，其他：参考服务器代码，比如228：为设备不在线，其他状态遵循JMCustomMsgHandler中的statusCode
 @param errMsg 服务器错误提示字符串
 */
typedef void (^JMSwitchCameraHandler)(BOOL success, NSString * _Nullable url, NSInteger code, NSString * _Nullable errMsg);

//播放器视频播放状态
typedef enum : NSUInteger {
    STREAM_VIDEO_STATUS_NONE = 0,
    STREAM_VIDEO_STATUS_PREPARE,                //正在准备播放
    STREAM_VIDEO_STATUS_START,                  //正在播放
    STREAM_VIDEO_STATUS_STOP,                   //播放结束
    
    STREAM_VIDEO_STATUS_ERR_URL_GET = 4,        //获取URL失败
    STREAM_VIDEO_STATUS_ERR_URL_INVALID,        //URL无效
    STREAM_VIDEO_STATUS_ERR_OPEN_FAIL,          //打开URL失败
    STREAM_VIDEO_STATUS_ERR_OPEN_TIMEOUT,       //打开URL超时
    STREAM_VIDEO_STATUS_ERR_PLAY_ABNORMAL,      //播放异常或设备停止推流(会自动停止播放器)
    STREAM_VIDEO_STATUS_ERR_HTTP_TIMEOUT,       //http请求超时
    STREAM_VIDEO_STATUS_ERR_HTTP_HOST,          //域名或IP错误
    STREAM_VIDEO_STATUS_ERR_HTTP_PARAMETER,     //http参数错误
    STREAM_VIDEO_STATUS_ERR_SERVER_DATA,        //服务器数据解析异常
    STREAM_VIDEO_STATUS_ERR_DEVICE_REPLY_FAIL,  //设备回复“失败”
    STREAM_VIDEO_STATUS_ERR_NETWORK_ANOMALY,    //网络异常
} STREAM_PLAY_STATUS;

//对讲状态
typedef enum : NSUInteger {
    STREAM_TALK_STATUS_NONE = 0,
    STREAM_TALK_STATUS_PREPARE,                 //进行对讲准备工作
    STREAM_TALK_STATUS_START,                   //对讲已开始
    STREAM_TALK_STATUS_STOP,                    //对讲已停止
    
    STREAM_TALK_STATUS_ERR_URL_GET = 4,         //获取URL失败
    STREAM_TALK_STATUS_ERR_INIT,                //初始化对讲URL失败
    STREAM_TALK_STATUS_ERR_TALKING,             //正在对讲不能再次开始
    STREAM_TALK_STATUS_ERR_SEND,                //发送音频数据失败
    STREAM_TALK_STATUS_ERR_HTTP_TIMEOUT,        //http请求超时
    STREAM_TALK_STATUS_ERR_HTTP_HOST,           //域名或IP错误
    STREAM_TALK_STATUS_ERR_HTTP_PARAMETER,      //http参数错误
    STREAM_TALK_STATUS_ERR_SERVER_DATA,         //服务器数据解析异常
    STREAM_TALK_STATUS_ERR_DEVICE_REPLY_FAIL,   //设备回复"失败"，即表示设备端拒绝对讲或对讲初始化异常
    STREAM_TALK_STATUS_ERR_NETWORK_ANOMALY,     //网络异常
    STREAM_TALK_STATUS_ERR_AUTHORITY,           //无麦克风权限
} STREAM_TALK_STATUS;

//视频录制状态
typedef enum : NSUInteger {
    STREAM_RECORD_STATUS_NONE = 0,
    STREAM_RECORD_STATUS_START,             //开始录制
    STREAM_RECORD_STATUS_COMPLETE,          //录制完成
    STREAM_RECORD_STATUS_ERR_RECORDING,     //正在录制
    STREAM_RECORD_STATUS_ERR_FAIL,          //录制失败
    STREAM_RECORD_STATUS_ERR_SAVE,          //保存失败
    STREAM_RECORD_STATUS_ERR_PATH,          //无效路径
    STREAM_RECORD_STATUS_ERR_AUTHORITY,     //无权限
} STREAM_RECORD_STATUS;

//设备端透传的命令字(即回调didServerReceiveData中可能包含的字段：code的值)
typedef enum : NSUInteger {
    STREAM_RECEIVE_CMD_PLAYBACK_FILE_END = 0x108,  // 回放文件结尾
    STREAM_RECEIVE_CMD_PLAYBACK_ALL_END = 0x109,   // 回放所有文件结束
} STREAM_RECEIVE_CMD;

@protocol JMVideoStreamPlayerDelegate;

@interface JMVideoStreamPlayer : NSObject

@property (nonatomic,weak) id<JMVideoStreamPlayerDelegate> _Nullable delegate;
@property (readonly) NSString * _Nullable appID;                    //手机自身的标识
@property (readonly) NSInteger videoWidth;              //视频宽度
@property (readonly) NSInteger videoHeight;             //视频高度

@property (nonatomic,assign) BOOL HWDecodeState;        //视频硬件解码状态(YES:开，NO:关)
@property (nonatomic,assign) BOOL mute;                 //静音设置
@property (nonatomic,assign) double delayMaxTime;       //允许播放的缓冲(延迟)时间(默认10.0毫秒)
@property (nonatomic,assign) double timeoutInterval;        //http请求超时时间(默认45.0秒)，包括startPlayLive、startPlayback、stopPlay、startTalk、stopTalk、sendRequestWithParameterDic等接口中附带的请求

#pragma mark - SDK初始化及信息配置

/**
 初始化SDK
 #对应释放接口：DeInitialize

 @See 同JimiVideoPlayer:+Initialize
 */
+ (BOOL)Initialize;

/**
 释放SDK
 #释放之前需要先释放所有的JMVideoStreamPlayer对象
 #对应初始化接口：Initialize

 @See 同JimiVideoPlayer:+DeInitialize
 */
+ (BOOL)DeInitialize;

/**
 配置开发者信息
 #调用此接口，在不再使用SDK时需要调用DeInitialize释放SDK

 @param key 开发者Key
 @param secret 开发者Secret
 @param token 业务Token,若不知晓请填nil
 @return 是否配置成功，YES：成功
 */
+ (BOOL)configWithKey:(NSString *_Nonnull)key secret:(NSString *_Nonnull)secret token:(NSString *_Nullable)token;

/**
 配置Web服务器地址、端口及Http超时时间
 #需要初始化SDK之后再调用此接口

 @param hosts Web服务器域名或IP(默认live.jimivideo.com)
 @param port Web服务器端口(默认80)，<=0则使用默认
 @param timeoutInterval http请求超时时间，默认45秒，<=0则使用默认
 @return  是否配置成功，YES：成功
 */
+ (BOOL)configWebServer:(NSString *_Nonnull)hosts port:(int)port timeoutInterval:(double)timeoutInterval;

/**
 配置网关服务器地址、端口及连接心跳时间
 #需要初始化SDK之后再调用此接口

 @param hosts 网关服务器域名或IP(默认live.jimivideo.com)
 @param port 网关服务器端口(默认22100)，<=0则使用默认
 @param heartbeatTime 与服务器保持连接的心跳时间(内部默认10秒)，<=0则使用默认
 @return  是否配置成功，YES：成功
 */
+ (BOOL)configGatewayServer:(NSString *_Nonnull)hosts port:(int)port heartbeatTime:(double)heartbeatTime;

#pragma mark - 基本接口

/**
 初始化视频播放器，并配置设备IMEI
 #一旦使用此接口前必须调用Initialize初始化SDK，并调用configWithKey:secret:token:配置开发者信息，不在使用此调用DeInitialize释放SDK；
 #适用于单一Camera场景

 @param imei 设备IMEI
 @return 播放器对象，若未初始化SDK，则会返还nil
 */
- (instancetype _Nullable)initWithIMEI:(NSString *_Nonnull)imei;

/**
 初始化视频播放器，并配置设备IMEI、开发者信息
 #使用此接口会自动调用Initialize、DeInitialize初始化及释放SDK;
 #适用于多个Camera场景

 @param key 开发者Key
 @param secret 开发者Secret
 @param imei 设备IMEI
 @param token 业务Token,若不知晓请填nil
 @return 播放器对象，若未初始化SDK，则会返还nil
 */
- (instancetype _Nullable)initWithKey:(NSString *_Nonnull)key secret:(NSString *_Nonnull)secret imei:(NSString *_Nonnull)imei token:(NSString *_Nullable)token;

/**
 关联显示视图(UIImage)
 
 @param monitor Monitor视图
 */
- (void)attachMonitor:(Monitor *_Nullable)monitor;

/**
 关联显示视图(GLKView)

 @param monitor GLMonitor视图
 */
- (void)attachGLMonitor:(GLMonitor *_Nullable)monitor;

/**
 移除显示视图关联
 */
- (void)deattachMonitor;

/**
 开始直播
 
 @See 代理:-didStreamPlayerPlayWithStatus:errStr:;
 @Ass(相关接口): -stopPlay；
 */
- (void)startPlayLive;

/**
 开始回放

 @param fileNameArray 需要回放的文件列表，比如：@[@"2018-08-29-09-21-27_30268.mp4", @"2018-08-29-09-25-59_35532.mp4"]
 
 @See 代理:-didStreamPlayerPlayWithStatus:errStr:;
 @Ass: -stopPlay；
 */
- (void)startPlayback:(NSArray *_Nonnull)fileNameArray;

/**
 使用URL进行播放

 @param url RTMP视频流URL
 
 @See 代理:-didStreamPlayerPlayWithStatus:errStr:;
 @Ass: -stopPlay；
 */
- (void)startPlay:(NSString *_Nonnull)url;

/**
 停止直播、回放或URL视频播放
 
 @See 代理:-didStreamPlayerPlayWithStatus:errStr:；
 */
- (void)stopPlay;

/**
 开始对讲
 
 @See 代理:-didStreamPlayerTalkWithStatus:errStr:；
 @Ass: -stopTalk；
 */
- (void)startTalk;

/**
 停止对讲
 
 @See 代理:-didStreamPlayerTalkWithStatus:errStr:；
 */
- (void)stopTalk;

/**
 停止SDK内部所有已启动的功能，包括视频播放、对讲、视频录制、Http请求和网关服务
 */
- (void)stop;

/**
 切换摄像头

 @param isFront 是否切换至前置摄像头(设备端不支持，此参数暂时无效)
 @param bAuto 是否由内部自动进行播放视频
 @param handler 切换之后的回调block
 */
- (void)switchCamera:(BOOL)isFront autoPlay:(BOOL)bAuto handler:(JMSwitchCameraHandler _Nullable)handler;

/**
 切换分辨率(暂时无效)

 @param quality 视频质量(0：标清，1:高清)
 @param bAuto 是否自动播放
 @param handler 切换之后的回调
 */
- (void)switchResolution:(NSInteger)quality autoPlay:(BOOL)bAuto handler:(JMSwitchCameraHandler _Nullable)handler;

#pragma mark - 截图及视频录制

/**
 实时视频截图

 @return 截图成功返回UIImage，否则nil
 */
- (UIImage *_Nullable)snapshot;

/**
 开始录制视频
 #若正在录制视频，视频被主动或被动停止均会自动停止视频录制

 @param filePath 保存视频的沙盒路径，必须以.mp4为文件后缀
 
 @See 代理:-didStreamPlayerRecordWithStatus:path:；
 @Ass: -stopRecording，-isRecording，-getRecordingRuration；
 */
- (void)startRecording:(NSString *_Nonnull)filePath;

/**
 停止视频录制
 #若正在录制视频，视频被主动或被动停止均会自动停止视频录制
 
 @See 代理:-didStreamPlayerRecordWithStatus:path:；
 @Ass：-stopPlay
 */
- (void)stopRecording;

/**
 是否正在录制
 
 @return YES：是
 */
- (BOOL)isRecording;

/**
 获取正在录制视频的时长(毫秒)
 
 @return 时长(毫秒)，0：未在进行视频录制或时长是0，其他为已录制的视频时长
 */
- (long)getRecordingRuration;

/**
 是否开启音频降噪功能及级别

 @param level 级别：0~3，0表示关闭，默认3
 */
- (void)setDenoiseLevel:(int)level;

#pragma mark - 高级接口

/**
 启动网关服务器服务(初始化时默认开启)
 
 @See 代理:-didServerReceiveData:codeType:；
 @Ass: -stopGatewayServer；
 */
- (void)startGatewayServer;

/**
 关闭网关服务器服务
 */
- (void)stopGatewayServer;

/**
 发送自定义消息请求(异步)，即透传数据给设备端
 
 @param paraDic 参数字典
 @param handler 消息回调
 @return 网络访问句柄JMHttpRequestTask
 
 @Ass: -cancelRequest:；
 */
- (JMHttpRequestTask *_Nullable)sendRequestWithParameterDic:(NSDictionary *_Nonnull)paraDic completionHandler:(JMCustomMsgHandler _Nullable )handler;

/**
 取消自定义消息请求
 #若发送自定义消息请求已经回调，取消则无效
 
 @param task 网络访问句柄JMHttpRequestTask
 */
- (void)cancelRequest:(JMHttpRequestTask *_Nullable)task;

/**
 发送自定义消息请求(同步)，即透传数据给设备端

 @param paraDic 参数字典
 @param handler 消息回调
 */
- (void)sendSyncRequestWithParameterDic:(NSDictionary *_Nonnull)paraDic completionHandler:(JMCustomMsgHandler _Nullable)handler;

@end

@protocol JMVideoStreamPlayerDelegate <NSObject>
@optional

/**
 实时视频(直播或回放)的播放状态

 @param status 播放状态
 @param errStr 只有当status为STREAM_VIDEO_STATUS_ERR_URL_GET时返回的是服务器的错误提示，其他情况均为空
 */
- (void)didStreamPlayerPlayWithStatus:(STREAM_PLAY_STATUS)status errStr:(NSString *_Nullable)errStr;

/**
 实时视频对讲状态

 @param status 对讲状态
 @param errStr 只有当status为STREAM_TALK_STATUS_ERR_URL_GET时返回的是服务器的错误提示，其他情况均为空
 */
- (void)didStreamPlayerTalkWithStatus:(STREAM_TALK_STATUS)status errStr:(NSString *_Nullable)errStr;

/**
 实时视频录制状态

 @param status 录制状态
 @param filePath 视频保存的沙盒路径(内部可能会更改路径，保存的路径以回调为主)
 */
- (void)didStreamPlayerRecordWithStatus:(STREAM_RECORD_STATUS)status path:(NSString *_Nullable)filePath;

/**
 音视频流信息(每秒更新一次，若视频宽高变化则会立即更新)
 
 @param videoWidth 视频宽度
 @param videoHeight 视频高度
 @param videoBps 视频码率(B/s)
 @param audioBps 音频码率(B/s)
 @param timestamp 视频时间戳，实时视频已经播放的时长
 @param totalFrameCount 总接收到的视频帧数
 */
- (void)didStreamPlayerReceiveFrameInfoWithVideoWidth:(NSInteger)videoWidth videoHeight:(NSInteger)videoHeight videoBPS:(NSInteger)videoBps audioBPS:(NSInteger)audioBps timestamp:(NSUInteger)timestamp totalFrameCount:(NSInteger)totalFrameCount;

/**
 网关透传设备发送的消息(即设备端自定义发送的信息)

 @param data 具体数据内容(一般为字典)
 @param type 编码类型(暂时可忽略，内部默认已经编码为UTF-8)
 
 @See 部分code命令字可查看枚举：STREAM_RECEIVE_CMD
 */
- (void)didServerReceiveData:(NSData *_Nullable)data codeType:(NSInteger)type;

@end
