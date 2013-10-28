//
//  V2ViewController.m
//  Modal View 2
//
//  Created by App Team on Oct 28, 2013.
//  Copyright 2013 App Inc. All rights reserved.
//

#import "V2ViewController.h"

@interface V2ViewController ()

@end

@implementation V2ViewController

@synthesize button2;
@synthesize label2;




- (void)loadView 
{
	self.view = [[UIView alloc] initWithFrame:[UIScreen mainScreen].applicationFrame];
	self.view.backgroundColor = [UIColor whiteColor];
	
	label2 = [[UILabel alloc] initWithFrame: CGRectMake(10, 10, 243, 96)];
	label2.backgroundColor = [UIColor clearColor];
	label2.text = @"View 2 \nLine 2-1 \nLine 2-2\n ...\n";
	label2.numberOfLines = 5;
	[self.view addSubview: label2];
	
	button2 = [UIButton buttonWithType:UIButtonTypeRoundedRect];
	button2.frame = CGRectMake(10, 114, 82, 37);
	[button2 setTitle:@"Dismiss" forState:UIControlStateNormal];
	[self.view addSubview: button2];
	
	[button2 addTarget:self action:@selector(buttonAction_button2:) forControlEvents:UIControlEventTouchUpInside];
	 	
}

- (IBAction) buttonAction_button2:(id) sender 
{
	NSLog(@"buttonAction_button2");
	[self dismissViewControllerAnimated:YES completion:NULL];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.

}


@end
