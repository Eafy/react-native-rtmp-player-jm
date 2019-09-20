//
//  JMRTMPJSConstant.h
//  DoubleConversion
//
//  Created by lzj<lizhijian_21@163.com> on 2019/9/20.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

extern NSString *const kOnStreamPlayerPlayStatus;
extern NSString *const kOnStreamPlayerTalkStatus;
extern NSString *const kOnStreamPlayerRecordStatus;
extern NSString *const kOnStreamPlayerReceiveFrameInfo;
extern NSString *const kOnStreamPlayerReceiveDeviceData;

@interface JMRTMPJSConstant : NSObject

+ (NSDictionary *)constantsToExport;

@end

NS_ASSUME_NONNULL_END
