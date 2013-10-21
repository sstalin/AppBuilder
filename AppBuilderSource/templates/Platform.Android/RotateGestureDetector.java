//
//  File:    RotateGestureDetector.java
//  Project: ___PROJECTNAME___
//
//  Created by ___FULLUSERNAME___ on ___DATE___.
//  Copyright ___YEAR___ ___ORGANIZATIONNAME___. All rights reserved.
//

package ___PACKAGE___;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Detects Rotating transformation gestures using the supplied {@link MotionEvent}s.
 * The {@link OnRotateGestureListener} callback will notify users when a particular
 * gesture event has occurred.
 *
 * This class should only be used with {@link MotionEvent}s reported via touch.
 *
 * To use this class:
 * <ul>
 *  <li>Create an instance of the {@code RotateGestureDetector} for your
 *      {@link View}
 *  <li>In the {@link View#onTouchEvent(MotionEvent)} method ensure you call
 *          {@link #onTouchEvent(MotionEvent)}. The methods defined in your
 *          callback will be executed when the events occur.
 * </ul>
 */
public class RotateGestureDetector {
    private static final String TAG = "RotateGestureDetector";

    /**
     * The listener for receiving notifications when gestures occur.
     * If you want to listen for all the different gestures then implement
     * this interface. If you only want to listen for a subset it might
     * be easier to extend {@link SimpleOnRotateGestureListener}.
     *
     * An application will receive events in the following order:
     * <ul>
     *  <li>One {@link OnRotateGestureListener#onRotateBegin(RotateGestureDetector)}
     *  <li>Zero or more {@link OnRotateGestureListener#onRotate(RotateGestureDetector)}
     *  <li>One {@link OnRotateGestureListener#onRotateEnd(RotateGestureDetector)}
     * </ul>
     */
    public interface OnRotateGestureListener {
        /**
         * Responds to Rotating events for a gesture in progress.
         * Reported by pointer motion.
         *
         * @param detector The detector reporting the event - use this to
         *          retrieve extended info about event state.
         * @return Whether or not the detector should consider this event
         *          as handled. If an event was not handled, the detector
         *          will continue to accumulate movement until an event is
         *          handled. This can be useful if an application, for example,
         *          only wants to update Rotation if the change is
         *          greater than 0.01.
         */
        public boolean onRotate(RotateGestureDetector detector);

        /**
         * Responds to the beginning of a Rotating gesture. Reported by
         * new pointers going down.
         *
         * @param detector The detector reporting the event - use this to
         *          retrieve extended info about event state.
         * @return Whether or not the detector should continue recognizing
         *          this gesture. For example, if a gesture is beginning
         *          with a focal point outside of a region where it makes
         *          sense, onRotateBegin() may return false to ignore the
         *          rest of the gesture.
         */
        public boolean onRotateBegin(RotateGestureDetector detector);

        /**
         * Responds to the end of a Rotate gesture. Reported by existing
         * pointers going up.
         *
         * Once a Rotate has ended, {@link RotateGestureDetector#getFocusX()}
         * and {@link RotateGestureDetector#getFocusY()} will return focal point
         * of the pointers remaining on the screen.
         *
         * @param detector The detector reporting the event - use this to
         *          retrieve extended info about event state.
         */
        public void onRotateEnd(RotateGestureDetector detector);
    }

    /**
     * A convenience class to extend when you only want to listen for a subset
     * of Rotating-related events. This implements all methods in
     * {@link OnRotateGestureListener} but does nothing.
     * {@link OnRotateGestureListener#onRotate(RotateGestureDetector)} returns
     * {@code false} so that a subclass can retrieve the accumulated Rotate
     * factor in an overridden onRotateEnd.
     * {@link OnRotateGestureListener#onRotateBegin(RotateGestureDetector)} returns
     * {@code true}.
     */
    public static class SimpleOnRotateGestureListener implements OnRotateGestureListener {

        public boolean onRotate(RotateGestureDetector detector) {
            return false;
        }

        public boolean onRotateBegin(RotateGestureDetector detector) {
            return true;
        }

        public void onRotateEnd(RotateGestureDetector detector) {
            // Intentionally empty
        }
    }

    private final Context mContext;
    private final OnRotateGestureListener mListener;

    private float mFocusX;
    private float mFocusY;

    private float mCurrAngle;
    private float mPrevAngle;
    
    private boolean mInProgress;
    
    public RotateGestureDetector(Context context, OnRotateGestureListener listener) {
        mContext = context;
        mListener = listener;
    }

    /**
     * Accepts MotionEvents and dispatches events to a {@link OnRotateGestureListener}
     * when appropriate.
     *
     * <p>Applications should pass a complete and consistent event stream to this method.
     * A complete and consistent event stream involves all MotionEvents from the initial
     * ACTION_DOWN to the final ACTION_UP or ACTION_CANCEL.</p>
     *
     * @param event The event to process
     * @return true if the event was processed and the detector wants to receive the
     *         rest of the MotionEvents in this event stream.
     */
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();

        final boolean streamComplete = action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_CANCEL;
        if (action == MotionEvent.ACTION_DOWN || streamComplete) {
            // Reset any Rotate in progress with the listener.
            // If it's an ACTION_DOWN we're beginning a new event stream.
            if (mInProgress) {
                mListener.onRotateEnd(this);
                mInProgress = false;
            }

            if (streamComplete) {
                return true;
            }
        }
        
        final boolean configChanged = action == MotionEvent.ACTION_DOWN ||
                action == MotionEvent.ACTION_POINTER_UP ||
                action == MotionEvent.ACTION_POINTER_DOWN;

        final int count = event.getPointerCount();
        if (count != 2) return true; 

        final float x1 = event.getX(0);
        final float y1 = event.getY(0);
        final float x2 = event.getX(1);
        final float y2 = event.getY(1);
        final float angle = (float) Math.atan2(y1 - y2, x1 - x2); 
        mFocusX = (x1 + x2) / 2;
        mFocusY = (y1 + y2) / 2;

        if (mInProgress && configChanged) {
            mListener.onRotateEnd(this);
            mInProgress = false;
        }
        
        if (configChanged) {
        	mPrevAngle = mCurrAngle = angle;
        }
        
        if (!mInProgress) {
        	mPrevAngle = mCurrAngle = angle;
            mInProgress = mListener.onRotateBegin(this);
        }
        
        if (action == MotionEvent.ACTION_MOVE) {
        	mCurrAngle = angle;

            boolean updatePrev = true;
            if (mInProgress) {
                updatePrev = mListener.onRotate(this);
            }

            if (updatePrev) {
            	mPrevAngle = mCurrAngle;
            }
        }

        return true;
    }

    /**
     * Returns {@code true} if a Rotate gesture is in progress.
     */
    public boolean isInProgress() {
        return mInProgress;
    }

    /**
     * Get the X coordinate of the current gesture's focal point.
     * If a gesture is in progress, the focal point is between
     * each of the pointers forming the gesture.
     *
     * @return X coordinate of the focal point in pixels.
     */
    public float getFocusX() {
        return mFocusX;
    }

    /**
     * Get the Y coordinate of the current gesture's focal point.
     * If a gesture is in progress, the focal point is between
     * each of the pointers forming the gesture.
     *
     * @return Y coordinate of the focal point in pixels.
     */
    public float getFocusY() {
        return mFocusY;
    }

    /**
     * Return the Rotating factor from the previous Rotate event to the current
     * event. This value is defined as
     *
     * @return The current Rotation.
     */
    public float getRotation() {
    	float r = mCurrAngle - mPrevAngle;
    	if (r > Math.PI) r -= Math.PI;
    	if (r < -Math.PI) r += Math.PI;
     	return r;
    }

    public float getCurrentAngle() {
        return mCurrAngle;
    }

    public float getPreviousAngle() {
        return mPrevAngle;
    }

}
