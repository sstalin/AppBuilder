package xj.mobile.tool

class GeneratePropertyClass {

  static main(args) { 
	ProcessType.generatePropertyClass('InterfaceOrientations', 'UIInterfaceOrientation', 
									  [ 'Portrait', 'PortraitUpsideDown', 'LandscapeLeft', 'LandscapeRight' ],
									  [ 'Unknown', 'Portrait', 'PortraitUpsideDown', 'LandscapeLeft',
										'LandscapeRight', 'FaceUp', 'FaceDown' ])

	ProcessType.generatePropertyClass('DeviceOrientation', 'UIDeviceOrientation',
									  [ 'Unknown', 'Portrait', 'PortraitUpsideDown', 'LandscapeLeft',
										'LandscapeRight', 'FaceUp', 'FaceDown' ])
  }
} 

