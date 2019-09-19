//
//  GLMonitor.h
//  JimiVideoPlayer
//
//  Created by lzj<lizhijian_21@163.com> on 2018/9/26.
//  Copyright © 2018年 lzj<lizhijian_21@163.com>. All rights reserved.
//

#import <GLKit/GLKit.h>

NS_ASSUME_NONNULL_BEGIN


/**
 GLMonitor视图对n模拟器不友好，最好使用真机调试
 */
@interface GLMonitor : GLKView

@property (nonatomic,strong) UIImage *image;

@end

NS_ASSUME_NONNULL_END
