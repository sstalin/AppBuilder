//
//  File:    V2.java
//  Project: Modal View 2
//
//  Created by App Team on Oct 28, 2013.
//  Copyright 2013 App Inc. All rights reserved.
//

package com.madl.app04f;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class V2 extends Activity {

    
    TextView label4;
    Button button4;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.v2);
    	label4 = (TextView) findViewById(R.id.label4);
    	button4 = (Button) findViewById(R.id.button4);
    }
    
    public void buttonAction_button4(View view) {
        finish();
    }
    
    

}
