//
//  File:    NavView.java
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

public class NavView extends Activity {

    
    Button b1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	b1 = (Button) findViewById(R.id.b1);
    }
    
    public void buttonAction_b1(View view) {
        startActivity(new Intent(NavView.this, V2.class));
    }
    
    

}
