//
//  TopViewController.m
//  NavView
//
//  Created by App Team on Oct 28, 2013.
//  Copyright 2013 App Inc. All rights reserved.
//

#import "TopViewController.h"
#import "V1ViewController.h"

@interface TopViewController ()

@end

@implementation TopViewController

@synthesize v1;




- (void)loadView 
{
	[super loadView];
	
	
	v1 = [[V1ViewController alloc] init];
	[self pushViewController:v1 animated:NO];
	 	
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.

}


@end
