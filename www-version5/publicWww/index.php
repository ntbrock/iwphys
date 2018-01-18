<?php
// 2016-Aug-28 Brockman - Revamp for IWP5
// 'New-age' php script that renders the right java code based on the PATH_INFO

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

        <div class="row">
            <div class="col-lg-12" style="margin-bottom: 0.5em;">
	    	 <h1>Welcome the <b>Web</b> back to <b>Interactive Web Physics</b>!</h1>
		
            </div>
        </div>

        <!-- Jumbotron Header -->
        <div class="row">
            <div class="col-lg-12">
        <header class="jumbotron hero-spacer">

		<p> In Version 5.0, Animations play natively using all major web browsers and tablets using HTML <i class="fa fa-html5"></i></p>
		<p>When you find something that can be improved, please <a href="https://github.com/ntbrock/iwphys/issues">open a new github issue.</a></p>
</header>
            </div>
        </div>

        <!-- /.row -->

        <!-- Title -->
        <div class="row">
            <div class="col-lg-12" style="margin-bottom: 0.5em;">
                <h1>Featured Physics Animations for the Web Browser</h1>
		
            </div>
        </div>
	<!-- /.row -->

        <!-- Page Features -->
        <div class="row text-center">

	<?php include_once('index-popular-animations.php') ?>

        </div>
        <!-- /.row -->

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
