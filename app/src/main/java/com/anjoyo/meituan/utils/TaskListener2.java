package com.anjoyo.meituan.utils;

/**
 * Extra callbacks for various Task life cycle. This interface is created to
 * protected {@link TaskListener}.
 */
public interface TaskListener2<Result> {

    /**
     * This callback will be fired when {@link AbstractTask#postDataChanged(int, Object)}
     * is called.
     *
     * @param dataIndentifier
     * @param object
     */
    public void onTaskDataChanged(int dataIndentifier, Object changeData);
}
