<?php
// 2016-Aug-28 Brockman - Revamp for IWP5
// 'New-age' php script tha trenders the right java code based on the PATH_INFO

include_once('iwp-config.php');

?>

<!DOCTYPE html>
<meta charset="utf-8">
<html lang="en">

<?php include_once("iwp-head.php") ?>

<body>

    <!-- Navigation -->
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">Interactive Web Physics</a>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li>
                        <a href="http://www.iwphys.org/">Web Browser Animations for Physics Students and Educators</a>
                    </li>
                    <li>
                        <a href="http://github.com/ntbrock/iwphys">Open Source on Github</a>
                    </li>
		    <li>
		      <a href="https://github.com/ntbrock/iwphys/issues">Github Issues</a>
		    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>

    <!-- Page Content -->
    <div class="container">

        <!-- Jumbotron Header -->
<!--
        <header class="jumbotron hero-spacer">
            <h1>A Warm Welcome!</h1>
            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ipsa, ipsam, eligendi, in quo sunt possimus non incidunt odit vero aliquid similique quaerat nam nobis illo aspernatur vitae fugiat numquam repellat.</p>
            <p><a class="btn btn-primary btn-large">Call to action!</a>
            </p>
        </header>

        <hr>
-->

        <div class="row">
            <div class="col-lg-12">
                <p>Please welcome the <b>Web</b> back to <b>Interactive Web Physics</b>! In Version 5, Animations play natively using all major web browsers and tablets.</p>
		<p>When you find something that can be improved, please <a href="https://github.com/ntbrock/iwphys/issues">open a new github issue.</a></p>
            </div>
        </div>

        <!-- /.row -->

        <!-- Title -->
        <div class="row">
            <div class="col-lg-12">
                <h3>Charged Particle Motion</h3>
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

    <!-- jQuery -->
    <script src="js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>

</body>

</html>
