# RoboSpice-samples


All samples of the RoboSpice library.


## Convert samples to Ant projects


```bash
#go to the sample of your choice.
cd samples/robospice-sample-core
#1. create ant files for current project
android update project -p .
#2. use maven to copy dependencies to the libs-for-ant folder
mvn clean install -Pant
#3. move all maven dependencies to the libs folder for ant
mv libs-for-ant libs
#build the project with ant
ant clean debug install
```

your project is now ant-ready, the libs folder contains all needed dependencies to add robospice and extensions modules to your project.

This method works for every sample project. Once you got all the needed jar files, you can move them to your own projects.

**Note** : you will need to define the `ANDROID_HOME` environment variable to point to you android SDK home folder for maven to work.
