2022Apr28 IWP 6 Development log on M1 Silicon

0630> Getting envrionment back online - Git clone, idea, plugins, 

[error] java.lang.UnsatisfiedLinkError: /private/var/folders/lk/m9y_dld159vcxf0l6_1fxplh0000gn/T/jna-365485363/jna9587897555444502727.tmp: dlopen(/private/var/folders/lk/m9y_dld159vcxf0l6_1fxplh0000gn/T/jna-365485363/jna9587897555444502727.tmp, 0x0001): tried: '/private/var/folders/lk/m9y_dld159vcxf0l6_1fxplh0000gn/T/jna-365485363/jna9587897555444502727.tmp' (fat file, but missing compatible architecture (have 'i386,x86_64', need 'arm64e'))


https://stackoverflow.com/questions/70368863/unsatisfiedlinkerror-for-m1-macs-while-running-play-server-locally

https://github.com/java-native-access/jna/releases/tag/5.7.0

0641> Memory lane, installing ant. 

0654> scala 2.12.15,  sbt 1.6.2

Total spaces 3 
https://discuss.binaryage.com/t/can-we-help-test-total-spaces-3-if-we-have-apple-silicon/8199/44

Up and running on port http://localhost:8470/

java.lang.NoSuchMethodError: 'scala.tools.nsc.settings.AbsSettings$AbsSetting scala.tools.nsc.Settings.bootclasspath()'


0700> updating to Play 2.6.25

Ok, how about play 2.8.13?

0711> Logging api update, 23 complie errors remain

0720> Homepage works



