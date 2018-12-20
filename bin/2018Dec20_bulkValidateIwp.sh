#!/bin/sh


ruby ../bin/validateIwp6.rb iwp-packaged/Charged\ Particle\ Motion/|bash

ruby ../bin/validateIwp6.rb iwp-packaged/Electrostatics/|bash

ruby ../bin/validateIwp6.rb iwp-packaged/Forces/|bash

ruby ../bin/validateIwp6.rb iwp-packaged/Function\ Reference/|bash

ruby ../bin/validateIwp6.rb iwp-packaged/Gravitation/|bash

ruby ../bin/validateIwp6.rb iwp-packaged/Kinematics\ 1D/|bash

ruby ../bin/validateIwp6.rb iwp-packaged/Kinematics\ 2D/|bash

ruby ../bin/validateIwp6.rb iwp-packaged/Magnetism/|bash

ruby ../bin/validateIwp6.rb iwp-packaged/Momentum,\ Collisions,\ KE/|bash

ruby ../bin/validateIwp6.rb iwp-packaged/Optics,\ Geometric/|bash

ruby ../bin/validateIwp6.rb iwp-packaged/Optics,\ Physical/|bash

ruby ../bin/validateIwp6.rb iwp-packaged/Oscillations/|bash

ruby ../bin/validateIwp6.rb iwp-packaged/TEST\ Developer\ Test/|bash

ruby ../bin/validateIwp6.rb iwp-packaged/Waves\ and\ Sound/|bash

# top levels

ruby ../bin/validateIwp6.rb iwp-2015/|bash

ruby ../bin/validateIwp6.rb summer-physics/|bash

ruby ../bin/validateIwp6.rb ftemo/|bash

ruby ../bin/validateIwp6.rb unit-test-2017/|bash

ruby ../bin/validateIwp6.rb winters-ncssm-2009/|bash



