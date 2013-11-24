
Notes about Madl Editor workspace setup, development, build, and deploy.

====================================
  Installation
====================================



====================================
  Get the Source
====================================

https://github.com/sstalin/AppBuilder.git

====================================
Creating new Project
====================================

1. Right-click in Package Explorer
2. Select New
3. Select Project
4. Under the AppBuilder category, select AppBuilder Project
4.1. IMPORTANT! Select Next, not Finish. Finish is not implemented yet and will cause the wizard to fail. It fails because no project name is entered and there is no default project name, which leads to the wizard throwing a NullPointerException when working on the project name. Finish should probably be disabled at this stage.
5. Enter a unique project name
6. Now click Finish
7. Open the {ProjectName}/src/app.madl file to view a sample MADL app

====================================
Building and running MADL project
====================================

 // Wei your comments
