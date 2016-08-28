<?php
// 2016-Aug-28 Brockman IWP Version5 Revamp!
// 2008-Jan-25 Brockman NCSSM IWP
// Simple PHP replacement for legacy .cgi script., Lists all the problems for easy copy + paste.

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

        <!-- Page Features -->
        <div class="row text-center">

<?php

$searchPath = rtrim($animationPath . $pathInfo, "/");

$searchName = str_replace("/", " / ", ucfirst($searchPath));
echo "<h1 style='margin-bottom: 1em;'>Browsing $searchName</h1>";

// RENDER Row per Animation Dir
// RENDER Tile per Animation

$animationDirs = recurseDirs($searchPath, 0 );
array_unshift($animationDirs, $searchPath );
// print_r($animationDirs);
foreach ( $animationDirs as $dir ) { 
	$hrefDir = str_replace_first($animationPath, '', $dir);
	$relDir = str_replace_first($searchPath.'/', '', $dir);
       if ( $dir != $searchPath ) {
?>
	<!-- Sub Directory -->
        <div class="row">
            <div class="col-lg-12">
	    	 <h3><a href="<?= $browseUrl ?><?= $hrefDir ?>"><?= $relDir ?></a></h3>
	    </div>
        </div>

<?php
       } else { 
	$animationFiles = recurseFind($dir, '/.iwp$/', 0 );
?>
        <!-- Current Directory --> 
<!--
        <div class="row">
            <div class="col-lg-12">
                <h3><?= $dir ?></h3>
            </div>
        </div>
-->
	<!-- /.row -->

	<!-- Animations -->
        <div class="row">

<?php
foreach ( $animationFiles as $file ) { 
	$name = str_replace_first($dir.'/', '', $file);
	$uri = str_replace_first($animationPath,'',$file);
?>
            <div class="col-md-4 col-sm-6 hero-feature">
              <div class="thumbnail">
                   <h3><?= $name ?></i></h3> <!-- color: #FFD700; -->
            <img src="<?= $screenshotsUrl ?><?= $uri ?>.png" alt="">
                <div class="caption">

                          <a target="_anim" href="<?= $baseAnimateUrl ?><?= $uri ?>" class="btn btn-default">Animate</a>
                        <p>
			<i class="fa fa-star-o" style="color: #333;"></i>	
			<i class="fa fa-bug" style="color: #333;"></i>	
			<a target="_xtoj" href="<?= $baseXtojUrl ?><?= $uri ?>"><i class="fa fa-file-code-o fa-1x" style="color: #333;"></i></a>
			<a target="_applet" href="https://www.iwphys.org/pps/webInterface.php/packagedProblems/<?= str_replace_first('/iwp-packaged','',$uri) ?>"><i class="fa fa-coffee fa-1x" style="color: #333;"></i></a>
			</p>
			<p>
			<?= readIwpFileDescription($file) ?>
                        </p>
                </div>
             </div>
           </div>
<?php } ?>
	</div>
<?php }

}
 ?>

     </div>
</body>
</html>
