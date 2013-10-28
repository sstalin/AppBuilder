//
//  V2ViewController.m
//  NavView
//
//  Created by App Team on Oct 28, 2013.
//  Copyright 2013 App Inc. All rights reserved.
//

#import "V2ViewController.h"

@interface V2ViewController ()

@end

@implementation V2ViewController

@synthesize l1;




- (void)loadView 
{
	self.view = [[UIView alloc] initWithFrame:[UIScreen mainScreen].applicationFrame];
	self.view.backgroundColor = [UIColor whiteColor];
	
	l1 = [[UILabel alloc] initWithFrame: CGRectMake(10, 10, 108, 21)];
	l1.backgroundColor = [UIColor clearColor];
	l1.text = @"Something";
	[self.view addSubview: l1];
	
	self.title = @"Second";
	 	
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.

}


@end
