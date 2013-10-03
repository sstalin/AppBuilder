package xj.mobile.codegen.templates

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

class IOSPageViewTemplates { 

  static templates = [
	pageview : [
	  [
		delegate: 'UIScrollViewDelegate'
	  ],
	  [
		declaration:  '''UIScrollView *scrollView;
UIPageControl *pageControl;
NSMutableArray *viewControllers;
BOOL pageControlUsed;

static NSUInteger numberOfPages = ${numberOfPages};
'''
	  ],
	  [
		loadView: '''self.view = [[UIView alloc] initWithFrame:[UIScreen mainScreen].applicationFrame];
scrollView = [[UIScrollView alloc] initWithFrame: CGRectMake(0, 0, 320, 440)];
pageControl = [[UIPageControl alloc] initWithFrame:CGRectMake(0, 440, 320, 20) ]; 
[self.view addSubview: scrollView];
[self.view addSubview: pageControl];

[pageControl addTarget:self action:@selector(changePage:) forControlEvents:UIControlEventValueChanged];

viewControllers = [[NSMutableArray alloc] init];
for (unsigned i = 0; i < numberOfPages; i++)
{
\t[viewControllers addObject:[NSNull null]];
}

scrollView.pagingEnabled = YES;
scrollView.contentSize = CGSizeMake(scrollView.frame.size.width * numberOfPages, scrollView.frame.size.height);
scrollView.showsHorizontalScrollIndicator = NO;
scrollView.showsVerticalScrollIndicator = NO;
scrollView.scrollsToTop = NO;
scrollView.delegate = self;
    
pageControl.numberOfPages = numberOfPages;
pageControl.currentPage = 0;

[self loadScrollViewWithPage:0];
[self loadScrollViewWithPage:1];
'''
	  ],
	  [
		methodDeclaration: '- (void)loadScrollViewWithPage:(int)page;'
	  ],
	  [
		method: '''- (void)loadScrollViewWithPage:(int)page
{
    if (page < 0)
        return;
    if (page >= numberOfPages)
        return;

    UIViewController *controller = [viewControllers objectAtIndex:page];
    if ((NSNull *)controller == [NSNull null])
    {
${indent(pageAllocCode, 2)}
        [viewControllers replaceObjectAtIndex:page withObject:controller];
    }
    
    if (controller.view.superview == nil)
    {
        CGRect frame = scrollView.frame;
        frame.origin.x = frame.size.width * page;
        frame.origin.y = 0;
        controller.view.frame = frame;
        [scrollView addSubview:controller.view];        
    }    
}

- (void)scrollViewDidScroll:(UIScrollView *)sender
{
    if (pageControlUsed)
    {
        // do nothing - the scroll was initiated from the page control, not the user dragging
        return;
    }
	
    // Switch the indicator when more than 50% of the previous/next page is visible
    CGFloat pageWidth = scrollView.frame.size.width;
    int page = floor((scrollView.contentOffset.x - pageWidth / 2) / pageWidth) + 1;
    pageControl.currentPage = page;
    
    [self loadScrollViewWithPage:page - 1];
    [self loadScrollViewWithPage:page];
    [self loadScrollViewWithPage:page + 1];
}

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView
{
    pageControlUsed = NO;
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView
{
    pageControlUsed = NO;
}

- (void)changePage:(id)sender
{
    int page = pageControl.currentPage;
    NSLog(@\"changePage: %d\", page);
	
    [self loadScrollViewWithPage:page - 1];
    [self loadScrollViewWithPage:page];
    [self loadScrollViewWithPage:page + 1];
    
    CGRect frame = scrollView.frame;
    frame.origin.x = frame.size.width * page;
    frame.origin.y = 0;
    [scrollView scrollRectToVisible:frame animated:YES];
    
    pageControlUsed = YES;
}'''
	  ],
	],
  ];

}