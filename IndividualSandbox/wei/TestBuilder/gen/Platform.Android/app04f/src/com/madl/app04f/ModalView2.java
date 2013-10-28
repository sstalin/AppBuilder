//
//  File:    ModalView2.java
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

public class ModalView2 extends Activity {

    
    TextView label3;
    Button button3;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	label3 = (TextView) findViewById(R.id.label3);
    	button3 = (Button) findViewById(R.id.button3);
    }
    
    public void buttonAction_button3(View view) {
        startActivity(new Intent(ModalView2.this, V2.class));
    }
    
    

}
