<?php
// 2016-Aug-28 Brockman - Revamp for IWP5
// 'New-age' php script tha trenders the right java code based on the PATH_INFO

include_once('iwp-config.php');

?><!DOCTYPE html>
<meta charset="utf-8">
<html lang="en">

<head>
  <?php include_once("iwp-head.php") ?>
</head>

<body>
    <?php include_once("iwp-nav.php") ?>

    <!-- Page Content -->
    <div class="container">

        <!-- Jumbotron Header -->
        <div class="row">
            <div class="col-lg-12">
        <header class="jumbotron hero-spacer">
                <p>Welcome the <b>Web</b> back to <b>Interactive Web Physics</b>!</p>
		<p> In Version 5, Animations play natively using all major web browsers and tablets using HTML <i class="fa fa-html5"></i></p>
		<p>When you find something that can be improved, please <a href="https://github.com/ntbrock/iwphys/issues">open a new github issue.</a></p>
</header>
            </div>
        </div>

        <!-- /.row -->

        <!-- Title -->
        <div class="row">
            <div class="col-lg-12" style="margin-bottom: 0.5em;">
                <h3>Physics Animations for the Web Browser - Featured Animations</h3>
		
            </div>
        </div>
	<!-- /.row -->

        <!-- Page Features -->
        <div class="row text-center">

            <div class="col-md-4 col-sm-6 hero-feature">
                <div class="thumbnail">
                    <img src="./images/iwp-screenshots/em-ratio-1c.png" alt="">
                    <div class="caption">
                        <h3>em-ratio-1c <i class="fa fa-star fa-1x" style="color: #FFD700;"></i></h3>
                        <p>Electro Magnetic Fields, Dr. Loren Winters</p>
                        <p>
                          <a href="animate.php/iwp-packaged/Charged%20Particle%20Motion/em-ratio-1.iwp" class="btn btn-default">Animate</a>
                          <!-- <a href="#" class="btn btn-default"><i class="fa fa-icon-star fa-2x" style="color: black"></i></a> -->
                        </p>
                    </div>
                </div>
            </div>

            <div class="col-md-4 col-sm-6 hero-feature">
                <div class="thumbnail">
                    <img src="./images/iwp-screenshots/em-ratio-1c.png" alt="">
                    <div class="caption">
                        <h3>em-ratio-1d <i class="fa fa-star-o fa-1x" style="color: #ccc;"></i></h3>
                        <p>Electro Magnetic Fields, Dr. Loren Winters</p>
                        <p>
                          <a href="./animations/Charged Particle Motion/em-ratio-1.iwp" class="btn btn-default">Animate</a>
                        </p>
                    </div>
                </div>
            </div>

            <div class="col-md-4 col-sm-6 hero-feature">
                <div class="thumbnail">
                    <img src="http://placehold.it/800x500" alt="">
                    <div class="caption">
                        <h3>Feature Label</h3>
                        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.</p>
                        <p>
                            <a href="#" class="btn btn-primary">Buy Now!</a> <a href="#" class="btn btn-default">More Info</a>
                        </p>
                    </div>
                </div>
            </div>

            <div class="col-md-4 col-sm-6 hero-feature">
                <div class="thumbnail">
                    <img src="http://placehold.it/800x500" alt="">
                    <div class="caption">
                        <h3>Feature Label</h3>
                        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.</p>
                        <p>
                            <a href="#" class="btn btn-primary">Buy Now!</a> <a href="#" class="btn btn-default">More Info</a>
                        </p>
                    </div>
                </div>
            </div>

        </div>
        <!-- /.row -->

        <hr>

        <!-- Footer -->
        <footer>
            <div class="row">
                <div class="col-lg-12">
		    <p>
		    IWP is made possible in part by these wonderful projects: 
		    <a href="https://jquery.com/download/">Jquery 2</a> / 
		    <a href="https://d3js.org/">D3 Data Driven Documents 4.2</a> / 
		    <a href="http://fontawesome.io/">Font Awesome 4.6.3</a> / 
		    <a href="https://startbootstrap.com/template-overviews/heroic-features/">HTML 5 Template is StartBootstrap.com, Heroic Features</a>
		    </p>
		    <p>Kudos to Dr. Jonathan Bennett, Dr. Loren Winters, and all past code contributors for inspiration and success of Interactive Web Physics.</p>
                    <p>Copyright &copy; Taylor Brockman 1999 - 2016</p>
                </div>
            </div>
        </footer>

    </div>
    <!-- /.container -->

    <?php include_once('iwp-foot.php'); ?>

</body>

</html>
