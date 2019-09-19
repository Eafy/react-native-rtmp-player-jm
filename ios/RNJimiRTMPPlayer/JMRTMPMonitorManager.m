//
//  JMRTMPMonitorManager.m
//  example
//
//  Created by lzj<lizhijian_21@163.com> on 2019/8/27.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "JMRTMPMonitorManager.h"
#import <JimiVideoPlayer/Monitor.h>
#import <React/RCTConvert.h>

Monitor *gJMRTMPMonitor = nil;

@implementation JMRTMPMonitorManager

RCT_EXPORT_MODULE(JMRTMPMonitor)

RCT_CUSTOM_VIEW_PROPERTY(image, NSDictionary, Monitor) {
    UIImage *img = [RCTConvert UIImage:json];
    if (img) {
        view.image = img;
    }
}

- (UIView *)view
{
    if (gJMRTMPMonitor == nil) {
        gJMRTMPMonitor = [[Monitor alloc] init];
    }

    return gJMRTMPMonitor;
}

@end
