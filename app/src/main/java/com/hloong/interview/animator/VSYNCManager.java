package com.hloong.interview.animator;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟信号发送， 16ms 一次循环
 */
public class VSYNCManager {
    private static final VSYNCManager ourInstance = new VSYNCManager();

    public static VSYNCManager getInstance() {
        return ourInstance;
    }

    private VSYNCManager() {
        new Thread(runnable).start();
    }
    public void add(AnimationFrameCallback animationFrameCallback) {
        list.add(animationFrameCallback);
    }
    private List<AnimationFrameCallback> list = new ArrayList<>();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (AnimationFrameCallback animationFrameCallback : list) {
                    animationFrameCallback.doAnimationFrame(System.currentTimeMillis());
                }
            }
        }
    };
    interface AnimationFrameCallback {
        boolean doAnimationFrame(long currentTime);
    }


}
