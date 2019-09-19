package com.jimi.rn.rtmp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.jimi.jimivideoplayer.opengl.GLMonitor;

public class JMRTMPMonitorManager extends SimpleViewManager<GLMonitor> {
    public static GLMonitor glMonitor;
    public static boolean isResume = false;
    private DataSource<CloseableReference<CloseableImage>> dataSource;
    private DraweeHolder<?> imageHolder;

    @Override
    public String getName() {
        return "JMRTMPMonitor";
    }

    @Override
    protected GLMonitor createViewInstance(ThemedReactContext reactContext) {
        if (glMonitor == null) {
            glMonitor = new GLMonitor(reactContext);

            GenericDraweeHierarchy genericDraweeHierarchy = new GenericDraweeHierarchyBuilder(glMonitor.getResources())
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                    .setFadeDuration(0)
                    .build();
            imageHolder = DraweeHolder.create(genericDraweeHierarchy, reactContext);
            imageHolder.onAttach();
        }
        return glMonitor;
    }

    public static void setIsResume(boolean isResume) {
        JMRTMPMonitorManager.isResume = isResume;
        if (glMonitor != null) {
            if (isResume) {
                glMonitor.onResume();
            } else {
                glMonitor.onPause();
            }
        }
    }

    private final ControllerListener<ImageInfo> imageControllerListener =
            new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, @Nullable final ImageInfo imageInfo, @Nullable Animatable animatable) {
                    CloseableReference<CloseableImage> imageReference = null;
                    try {
                        imageReference = dataSource.getResult();
                        if (imageReference != null) {
                            CloseableImage image = imageReference.get();
                            if (image != null && image instanceof CloseableStaticBitmap) {
                                CloseableStaticBitmap closeableStaticBitmap = (CloseableStaticBitmap) image;
                                Bitmap bitmap = closeableStaticBitmap.getUnderlyingBitmap();
                                if (bitmap != null && glMonitor != null) {
                                    glMonitor.displayBitmap(bitmap);
                                }
                            }
                        }
                    } finally {
                        dataSource.close();
                        if (imageReference != null) {
                            CloseableReference.closeSafely(imageReference);
                        }
                    }
                }
            };

    @ReactProp(name="image")
    public void setImage(GLMonitor monitor, ReadableMap imgMap) {
        String url = imgMap.getString("uri");
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("http://") || url.startsWith("https://") ||
                    url.startsWith("file://") || url.startsWith("asset://")) {
                ImageRequest imageRequest = ImageRequestBuilder
                        .newBuilderWithSource(Uri.parse(url))
                        .build();
                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(imageRequest)
                        .setControllerListener(imageControllerListener)
                        .setOldController(imageHolder.getController())
                        .build();
                imageHolder.setController(controller);
            } else {
                int resId = glMonitor.getResources().getIdentifier(
                        url,
                        "drawable",
                        glMonitor.getContext().getPackageName());
                Bitmap bitmap = BitmapFactory.decodeResource(glMonitor.getResources(), resId);
                if (bitmap != null) {
                    glMonitor.displayBitmap(bitmap);
                }
            }
        }
    }
}
