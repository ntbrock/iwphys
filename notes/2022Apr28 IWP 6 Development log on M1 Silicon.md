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


0723> Reloaded animation with the issue, reproduced.

http://localhost:8470/animation/winters-ncssm-2009/mirror-concave-ray-tracing-02.iwp


or: <polyline> attribute points: Expected number, "…33333333333,500 NaN,NaN 833.3333…".


iwp6-calc:883> ray_1c is a line, solid.shape:  {shapeType: 'line', points: Array(0), file: undefined, drawTrails: false, drawVectors: false, …}


TODO L:earn: https://github.com/koekeishiya/yabai



http://localhost:8470/animation/winters-ncssm-2009/mirror-concave-ray-tracing-02.iwp


0739> solid_ray_1b has points that are very negative.

<polyline id="solid_ray_1b" points="375,375 -2625,545.4545454545454 -2598.2101731915423,558.9565889877605 -2625,545.4545454545454 -2599.9119738930035,529.0048966420431 " stroke="rgb(204,0,0)" stroke-width="2" fill="none"></polyline>


xPath : p 

Y Path: ho

width: p*3f*step(t-delta_t)*check

height: (ho*pp/(pp-c))*3*step(t-delta_t)*check

Theta: 0


Very negative midpoints.


iwp6-animator:319> Vector draw for : ray_1b x1: 375 x2: -2625 y1: 375 y2: 545.4545454545454
iwp6-animator.js:335 iwp6-animator:335> Vector draw for : ray_1b setting points to:  375,375 -2625,545.4545454545454 -2598.2101731915423,558.9565889877605 -2625,545.4545454545454 -2599.9119738930035,529.0048966420431 
iwp6-animator.js:300 




Point 2 is very far off:
iwp6-animator:335> Vector draw for : ray_1b ; point2:  -2625,545.4545454545454 

How is it different than ray1a?


Ray_1a: 

x Path: p

y Path: ho 

Width: -p*step(t)*check

Height: -(ho*pp/(pp-c))*step(t)*check

Theta: 0


Looks like the use of 3f in the width, sending it too far left.


f = focal legnth = 0.8




iwp6-animator:335> Vector draw for : ray_1b pathAndShape.width:  -120

iwp6-animator:335> Vector draw for : ray_1b pathAndShape.x:  -5
iwp6-animator.js:321 iwp6-animator:335> Vector draw for : ray_1b pathAndShape.y:  5
iwp6-animator.js:322 iwp6-animator:335> Vector draw for : ray_1b pathAndShape.width:  -120
iwp6-animator.js:323 iwp6-animator:335> Vector draw for : ray_1b pathAndShape.height:  -6.818181818181818



Fozen frame, ahve teh vars


p = -5

f = 8,

t = 0.2, 

delta_t = 0.1

check = 1

-5 * 3(8) * 1 * 1 = 

-120


p*3f*step(t-delta_t)*check



ray1a.w = 5

ray1b.w = -120



What is the value in iwp4?

How does my debug tool work again?

url = "http://localhost:8470/validation/calculation/#{uri}?format=csv"


http://localhost:8470/validation/calculation/winters-ncssm-2009/mirror-concave-ray-tracing-02.iwp?format=csv


"result","viewUrl","validateUrl","path","legacyObjectNames","objectCount","objectNames","totalFrames","framesWithDifferences","totalDifferences","framesWithLeftMissing","framesWithRightMissing","totalLeftMissing","totalRightMissing"
"Complete","https://iwp6.iwphys.org/animation/winters-ncssm-2009/mirror-concave-ray-tracing-02.iwp","https://iwp6.iwphys.org/validation/calculation/winters-ncssm-2009/mirror-concave-ray-tracing-02.iwp","../animations/winters-ncssm-2009/mirror-concave-ray-tracing-02.iwp","false","37","G axis bg c c_label center check deltaTime delta_t f f_label f_left have_bg hi ho image mirror object p pi pp q ray_1a ray_1b ray_1c ray_2a ray_2b ray_2c ray_3a ray_3b ray_3c ray_3d sf t tDel tDelta title","1001","998","998","1","1","47000","3000"


0825> Passes the ordering check, just the delta_t is different

http://localhost:8470/validation/ordering/winters-ncssm-2009/mirror-concave-ray-tracing-02.iwp?format=csv



The full compare:

http://localhost:8470/validation/calculation/winters-ncssm-2009/mirror-concave-ray-tracing-02.iwp


There it is!

var	left	right
ray_1b.width	-15.0	-120


How does version 4 calcualte ray_1b.width as -15?


p*3f*step(t-delta_t)*check


p = -5

f = 8

f would be 1??

