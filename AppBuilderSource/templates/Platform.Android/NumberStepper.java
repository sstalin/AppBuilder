//
//  File:    NumberStepper.java
//  Project: ___PROJECTNAME___
//
//  Created by ___FULLUSERNAME___ on ___DATE___.
//  Copyright ___YEAR___ ___ORGANIZATIONNAME___. All rights reserved.
//

package ___PACKAGE___;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class NumberStepper extends LinearLayout {

  final long REPEAT_DELAY = 50;
  final int BUTTON_HEIGHT = 35;

  protected int fontSize = 15;
	
  protected float minValue = 0;
  protected float maxValue = 999;
  protected float value = 0;
  protected float stepValue = 1;
  protected boolean autoRepeat = true;
  protected boolean intValue = false; 

  protected Button decrement;
  protected Button increment;
	
  protected Handler repeatUpdateHandler = new Handler();
	
  protected boolean autoIncrement = false;
  protected boolean autoDecrement = false;
	
  interface ValueChangedListener {
	void valueChanged(View view);
  }
	
  protected ValueChangedListener valueChangedListener;

  class RepetetiveUpdater implements Runnable {
	public void run() {
	  if (autoRepeat) {
		if (autoIncrement){
		  increment();
		  repeatUpdateHandler.postDelayed(new RepetetiveUpdater(), REPEAT_DELAY);
		} else if (autoDecrement){
		  decrement();
		  repeatUpdateHandler.postDelayed(new RepetetiveUpdater(), REPEAT_DELAY);
		}
	  }
	}
  }
	
  public NumberStepper(Context context, AttributeSet attributeSet) {
	super(context, attributeSet);
		
	this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	LayoutParams buttonParams = new LinearLayout.LayoutParams(BUTTON_HEIGHT, BUTTON_HEIGHT);
		
	TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.NumberStepper);
	final int n = a.getIndexCount();
	for (int i = 0; i < n; ++i) {
	  int attr = a.getIndex(i);
	  switch (attr) {
	  case R.styleable.NumberStepper_value:
		setValue(a.getFloat(attr, 0));
		break;
	  case R.styleable.NumberStepper_minValue:
		setMinValue(a.getFloat(attr, 0));
		break;
	  case R.styleable.NumberStepper_maxValue:
		setMaxValue(a.getFloat(attr, 999));
		break;
	  case R.styleable.NumberStepper_step:
		setStepValue(a.getFloat(attr, 1));
		break;
	  case R.styleable.NumberStepper_autoRepeat: 
		setAutoRepeat(a.getBoolean(attr, true));
		break;
	  case R.styleable.NumberStepper_intValue: 
		intValue = a.getBoolean(attr, true);
		break;
	  case R.styleable.NumberStepper_onAction:
		final String onAction = a.getString(attr);
		if (onAction != null) {
		  setValueChangedListener(new ValueChangedListener() {
			  private Method mHandler;
	         
			  public void valueChanged(View v) {
				if (mHandler == null) {
				  try {
					mHandler = getContext().getClass().getMethod(onAction, View.class);
				  } catch (NoSuchMethodException e) {
					throw new IllegalStateException();
				  }
				}
	         
				try {
				  mHandler.invoke(getContext(), v);
				} catch (IllegalAccessException e) {
				  throw new IllegalStateException();
				} catch (InvocationTargetException e) {
				  throw new IllegalStateException();
				}
			  }
			});
		}
		break;

	  }
	}
	a.recycle();
		
	increment = initButton(context, true);
	decrement = initButton(context, false);		
	addView(decrement, buttonParams);
	addView(increment, buttonParams);
  }
	
  protected Button initButton(Context context, boolean inc) {
	Button button = new Button(context);
	button.setTextSize(fontSize);
	button.setText(inc ? "+" : "-");

	if (inc) {
	  button.setOnClickListener(new View.OnClickListener() {
		  public void onClick(View v) {
			increment();
		  }
		});
			
	  button.setOnLongClickListener(new View.OnLongClickListener() {
		  public boolean onLongClick(View arg0) {
			autoIncrement = true;
			repeatUpdateHandler.post(new RepetetiveUpdater());
			return false;
		  }
		});
			
	  button.setOnTouchListener(new View.OnTouchListener() {
		  public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP && autoIncrement) {
			  autoIncrement = false;
			}
			return false;
		  }
		});
	} else {
	  button.setOnClickListener(new View.OnClickListener() {
		  public void onClick(View v) {
			decrement();
		  }
		});
			
	  button.setOnLongClickListener(new View.OnLongClickListener() {
		  public boolean onLongClick(View arg0) {
			autoDecrement = true;
			repeatUpdateHandler.post(new RepetetiveUpdater());
			return false;
		  }
		});
			
	  button.setOnTouchListener(new View.OnTouchListener() {
		  public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP && autoDecrement) {
			  autoDecrement = false;
			}
			return false;
		  }
		});
	}
		
	return button;
  }
	
  public void increment(){
	value += stepValue;
	if (value > maxValue) value = maxValue;
	valueChanged();
  }

  public void decrement(){
	value -= stepValue;
	if (value < minValue) value = minValue;
	valueChanged();
  }
	
  public float getValue(){
	return value;
  }
	
  public void setValue(float value){
	if (value > maxValue) value = maxValue;
	if (value < minValue) value = minValue;
	this.value = value;	
  }
	
  public float getMinValue() {
	return minValue;
  }

  public void setMinValue(float minValue) {
	this.minValue = minValue;
  }

  public float getMaxValue() {
	return maxValue;
  }

  public void setMaxValue(float maxValue) {
	this.maxValue = maxValue;
  }
	
  public float getStepValue() {
	return stepValue;
  }

  public void setStepValue(float stepValue) {
	if (stepValue < 1) stepValue = 1;
	this.stepValue = stepValue;
  }

  public boolean isAutoRepeat() {
	return autoRepeat;
  }

  public void setAutoRepeat(boolean autoRepeat) {
	this.autoRepeat = autoRepeat;
  }
	
  public void setValueChangedListener(ValueChangedListener valueChangedListener) {
	this.valueChangedListener = valueChangedListener;
  }
	
  public void valueChanged() {
	if (valueChangedListener != null) {
	  valueChangedListener.valueChanged(this);
	}
  }
	
}
