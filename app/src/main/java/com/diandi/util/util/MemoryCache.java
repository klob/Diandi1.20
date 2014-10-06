package com.diandi.util.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * User : SuLinger(462679107@qq.com) .
 * Date : 2014-08-10 .
 * Time:  13:10 .
 * Project name : com.planboxone.
 * Copyright @ 2014, SuLinger, All Rights Reserved
 */


public class MemoryCache {

    private final LruCache<Integer, Bitmap> mMemoryCache;

    public MemoryCache() {
        final int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024);
        mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(final Integer key, final Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    public  void addBitmapToMemoryCache(final int key, final Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public  Bitmap getBitmapFromMemCache(final int key) {
        return mMemoryCache.get(key);
    }
}
