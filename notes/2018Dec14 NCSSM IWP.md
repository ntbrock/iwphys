2018Dec14 NCSSM IWP


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



# ruby convertAllIwp.rb iwp-packaged/Charged\ Particle\ Motion/|bash

# ruby convertAllIwp.rb iwp-packaged/Electrostatics/|bash

# ruby convertAllIwp.rb iwp-packaged/Forces/|bash

# ruby convertAllIwp.rb iwp-packaged/Function\ Reference/|bash

# ruby convertAllIwp.rb iwp-packaged/Gravitation/|bash

# ruby convertAllIwp.rb iwp-packaged/Kinematics\ 1D/|bash

# ruby convertAllIwp.rb iwp-packaged/Kinematics\ 2D/|bash

# ruby convertAllIwp.rb iwp-packaged/Magnetism/|bash

# ruby convertAllIwp.rb iwp-packaged/Momentum,\ Collisions,\ KE/|bash

# ruby convertAllIwp.rb iwp-packaged/Optics,\ Geometric/|bash

# ruby convertAllIwp.rb iwp-packaged/Optics,\ Physical/|bash

# ruby convertAllIwp.rb iwp-packaged/Oscillations/|bash

# ruby convertAllIwp.rb iwp-packaged/TEST\ Developer\ Test/|bash

# ruby convertAllIwp.rb iwp-packaged/Waves\ and\ Sound/|bash




