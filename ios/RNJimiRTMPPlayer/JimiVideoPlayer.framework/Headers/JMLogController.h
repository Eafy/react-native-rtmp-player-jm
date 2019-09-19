//
//  JMLogController.h
//  JimiVideoPlayer
//
//  Created by lzj<lizhijian_21@163.com> on 2018/5/10.
//  Copyright © 2018年 concox. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum : NSUInteger {
    JMLOG_LEVEL_VERBOSE = 0,       //任意信息都打印(默认级别)
    JMLOG_LEVEL_DEBUG = 1,         //DEBUG模式下打印
    JMLOG_LEVEL_INFO = 2,          //提示性消息打印
    JMLOG_LEVEL_WARN = 3,          //警告信息打印
    JMLOG_LEVEL_ERROR = 4,         //错误信息打印
} JMLOG_LEVEL;

@protocol JMLogControllerDelegate <NSObject>

- (void)didReceiveLogString:(NSString *)logStr;

@end

@interface JMLogController : NSObject

/**
 设置日志代理
 #一旦代理则日志走代理方法

 @param delegate 代理对象
 */
+ (void)setDelegate:(id)delegate;

/**
 设置日志显示级别

 @param level JMLOG_LEVEL,默认级别JMLOG_LEVEL_VERBOSE
 */
+ (void)setLevel:(JMLOG_LEVEL)level;

/**
 关闭日志，DEBUG模式下默认开启，输出到控制台
 */
+ (void)setLogOFF;

/**
 是否将日志保存到沙盒并上传至服务器
 #上传功能暂未实现

 @param isSave YES:保存，默认NO
 */
+ (void)saveLog:(BOOL)isSave;

@end
