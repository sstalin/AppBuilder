//
//  V1ViewController.m
//  Modal View 2
//
//  Created by App Team on Oct 28, 2013.
//  Copyright 2013 App Inc. All rights reserved.
//

#import "V1ViewController.h"
#import "V2ViewController.h"

@interface V1ViewController ()

@end

@implementation V1ViewController

@synthesize button1;
@synthesize label1;
@synthesize v2;




- (void)loadView 
{
	self.view = [[UIView alloc] initWithFrame:[UIScreen mainScreen].applicationFrame];
	self.view.backgroundColor = [UIColor whiteColor];
	
	label1 = [[UILabel alloc] initWithFrame: CGRectMake(10, 10, 243, 96)];
	label1.backgroundColor = [UIColor clearColor];
	label1.text = @"View 1 \nLine 1-1 \nLine 1-2\n ...\n";
	label1.numberOfLines = 5;
	[self.view addSubview: label1];
	
	button1 = [UIButton buttonWithType:UIButtonTypeRoundedRect];
	button1.frame = CGRectMake(10, 114, 64, 37);
	[button1 setTitle:@"Press" forState:UIControlStateNormal];
	[self.view addSubview: button1];
	
	[button1 addTarget:self action:@selector(buttonAction_button1:) forControlEvents:UIControlEventTouchUpInside];
	 	
}

- (IBAction) buttonAction_button1:(id) sender 
{
	NSLog(@"buttonAction_button1");
	if (v2 == nil) v2 = [[V2ViewController alloc] init];
	v2.modalTransitionStyle = UIModalTransitionStylePartialCurl;
	[self presentViewController:v2 animated:YES completion: NULL];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.

}


@end
