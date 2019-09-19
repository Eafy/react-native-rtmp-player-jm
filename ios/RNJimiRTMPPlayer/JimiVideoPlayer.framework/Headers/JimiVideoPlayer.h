//
//  JimiVideoPlayer.h
//  JimiVideoPlayer
//
//  Created by lzj<lizhijian_21@163.com> on 2018/9/6.
//  Copyright © 2018年 lzj<lizhijian_21@163.com>. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "JMVideoStreamPlayer.h"
#import "JMLogController.h"

@interface JimiVideoPlayer : NSObject

/**
 初始化SDK

 @return YES:成功
 */
+ (BOOL)Initialize;

/**
 释放SDK

 @return YES:成功
 */
+ (BOOL)DeInitialize;

/**
 获取SDK版本号
 
 @return 版本字符串(比如：1.0.0)
 */
+ (NSString *)getVersion;

/**
 获取SDK编译版本号(日期)
 
 @return 编译版本(比如:2018071101)
 */
+ (NSString *)getBuildVersion;

/**
 开始PING服务器(最多同时支持3个)
 
 @param host 服务器地址(支持域名)
 @return 0:success
 */
+ (NSInteger)startPing:(NSString *)host;

/**
 停止PING服务器
 
 @param host 服务器地址(支持域名)
 */
+ (void)stopPing:(NSString *)host;

/**
 视频转码成H264+AAC数据流文件

 @param inFilePath 视频输入路径
 @param outFilePath 视频输出路径
 @return YES：转码完成
 */
+ (BOOL)startVideoConverter:(NSString *)inFilePath outFilePath:(NSString *)outFilePath;

@end
