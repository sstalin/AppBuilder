//
//  V1ViewController.m
//  NavView
//
//  Created by App Team on Oct 28, 2013.
//  Copyright 2013 App Inc. All rights reserved.
//

#import "V1ViewController.h"
#import "V2ViewController.h"

@interface V1ViewController ()

@end

@implementation V1ViewController

@synthesize b1;
@synthesize v2;




- (void)loadView 
{
	self.view = [[UIView alloc] initWithFrame:[UIScreen mainScreen].applicationFrame];
	self.view.backgroundColor = [UIColor whiteColor];
	
	b1 = [UIButton buttonWithType:UIButtonTypeRoundedRect];
	b1.frame = CGRectMake(10, 10, 60, 37);
	[b1 setTitle:@"Push" forState:UIControlStateNormal];
	[self.view addSubview: b1];
	
	[b1 addTarget:self action:@selector(buttonAction_b1:) forControlEvents:UIControlEventTouchUpInside];
	
	self.title = @"First";
	 	
}

- (IBAction) buttonAction_b1:(id) sender 
{
	NSLog(@"buttonAction_b1");
	if (v2 == nil) v2 = [[V2ViewController alloc] init];
	[self.navigationController pushViewController:v2 animated:YES];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.

}


@end
