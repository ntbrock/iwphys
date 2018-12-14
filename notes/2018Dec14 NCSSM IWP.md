2018Dec14 NCSSM IWP

http://68.183.111.167:8470/DProblem/animations%2Fwinters-ncssm-2009%2Fcoulombslaw03.iwp
 
http://68.183.111.167:8470/valiation/unit-test-2017/TEST_euler.iwp

http://iwp6.iwphys.org:8470/valiation/unit-test-2017/TEST_euler.iwp


Albert
L> Iwp6 UI - Continued on creating the index page. Got most of the skeleton HTML, everything except for the animations.
N> Focus Port the CSS, looking into less.  Copy bootstrap over into Public Folder.  In our pair time next week, we will connect the filesytem browser to the views.


Niall
L> Matching up the v4 -> v5 data arrays. Confused where we want the data interpretation to go.  Write into the hidden value?  Figure out the best way to manipulate data in javascript.
N> Get oriented into app/controllers/ValidationController.scala, follow these code paths:

	val v4 = iwpVersion4CalculatorService.animateToJsonFrames(path)
	val v6 = iwpVersion6CalculatorService.animateToJsonFrames(s"${path}.json")
	val diffs = iwpDifferenceCalculatorService.diff(v4, v6)

In our pair time, help TB fill out the master Excel sheet.

Taylor
L> Lots of Development! Cloud deployment to iwp6. Bulk converted all animations to new json format. Filesystem controller browser. Validation difference comparator, Milestone 0 progress:  Valdiation tests, circlular refernecs, missing - bulk fill out the testing spreadsheet.

N> Automated fill out of testing sheet.  Enable Albert to continue IWP Animatjor port and Enable Niall to add more validation / help fill out the overall bulk sheet.  "Complete" on Milestone 0 tasks.

Dr. Bennett
L> Lots of work!  Waiting for results today.


## After

After - Bulk testing spreadhseet.

	Next step  Taylor + Niall -- Fill out the 1054 item spreadsheet completely and circulate before winter break next Friday.

= Albert - Demo of User Interface.


After - Less - Albert.

After - Taylor's Implementation of comaprison - Niall.



## Milestone 0 Status



Milestone 0 - Solidify The Data Model

= 100% Define a Json schema for IWP5.js with visualizations, examples - 6h

= 100%  Bulk convert packaged Xml Animations to their JSON counterparts, leverage php - 6h

= 100%  Choose and tool out a json schema validator application run over packaged content, refine schema - 8h

= 100%  Refactor the iwp5.js code for Animation parsing out to a new iwp5-validation.js library - 4h -- Iwp6!

- 50%   Implement a circular reference detector in iwp5-validation - 8h

Output: All Animations, know which ones have frame by frame / variable differences in bulk.








## Milestone 0 worklog

Goal is to have a fuyll spreadsheet of all anumations and test results for each.

= 1. Update iwp.js to be pure JS so it executes in scala runtime to load animations and calculate frames.

Old:
  $.each( solids, function( index, solid ) {
    resetSolidCalculators(solid);
  });

New:
  solids.forEach(function ( solid, index) {
    resetSolidCalculators(solid);
  });


= 2. How am I going to parse + pass the animation js to the new iwp6-calc?  Made a new iwp6-read interface.

1352> Reqady to do this now for the Euler Unit Test

= 3. Build an animation specific comparator for V4 -vs- V5.


4. Convert all .iwp and create their JSOn counterpaerts.

5. Browser that is driven off filesystem availbaility to run validation.

6. Deploy to Droplet, DNS fix



ruby convertAllIwp.rb iwp-packaged/Charged\ Particle\ Motion/|bash

ruby convertAllIwp.rb iwp-packaged/Electrostatics/|bash

ruby convertAllIwp.rb iwp-packaged/Forces/|bash

ruby convertAllIwp.rb iwp-packaged/Function\ Reference/|bash

ruby convertAllIwp.rb iwp-packaged/Gravitation/|bash

ruby convertAllIwp.rb iwp-packaged/Kinematics\ 1D/|bash

ruby convertAllIwp.rb iwp-packaged/Kinematics\ 2D/|bash

ruby convertAllIwp.rb iwp-packaged/Magnetism/|bash

ruby convertAllIwp.rb iwp-packaged/Momentum,\ Collisions,\ KE/|bash

ruby convertAllIwp.rb iwp-packaged/Optics,\ Geometric/|bash

ruby convertAllIwp.rb iwp-packaged/Optics,\ Physical/|bash

ruby convertAllIwp.rb iwp-packaged/Oscillations/|bash

ruby convertAllIwp.rb iwp-packaged/TEST\ Developer\ Test/|bash

ruby convertAllIwp.rb iwp-packaged/Waves\ and\ Sound/|bash

# top levels

ruby convertAllIwp.rb iwp-2015/|bash

ruby convertAllIwp.rb summer-physics/|bash

ruby convertAllIwp.rb ftemo/|bash

ruby convertAllIwp.rb unit-test-2017/|bash

ruby convertAllIwp.rb winters-ncssm-2009/|bash





http://68.183.111.167:8470/DProblem/animations%2Fwinters-ncssm-2009%2Fcoulombslaw03.iwp
 
 



