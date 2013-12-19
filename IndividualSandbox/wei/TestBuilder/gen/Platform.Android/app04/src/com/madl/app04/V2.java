//
//  File:    V2.java
//  Project: NavView
//
//  Created by App Team on Oct 28, 2013.
//  Copyright 2013 App Inc. All rights reserved.
//

package com.madl.app04;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class V2 extends Activity {

    
    TextView l1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.v2);
    	l1 = (TextView) findViewById(R.id.l1);
    }
    
    

}
