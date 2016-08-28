<?php
// 2016-Aug-28 Brockman IWP Version5 Revamp!
// 2008-Jan-25 Brockman NCSSM IWP
// Simple PHP replacement for legacy .cgi script., Lists all the problems for easy copy + paste.

include_once('iwp-config.php');

?><!DOCTYPE html>
<meta charset="utf-8">
<html lang="en">

<?php include_once("iwp-head.php") ?>

<body>
    <?php include_once("iwp-nav.php") ?>

    <!-- Page Content -->
    <div class="container">

        <!-- Page Features -->
        <div class="row text-center">

<?php

$searchPath = $animationPath . $pathInfo;
$searchName = str_replace("/", " / ", ucfirst($searchPath));
echo "<h1>Browsing $searchName</h1>";

// RENDER Row per Animation Dir
// RENDER Tile per Animation

$animationDirs = recurseDirs($searchPath, 0 );
array_unshift($animationDirs, $searchPath );
// print_r($animationDirs);
foreach ( $animationDirs as $dir ) { 

	$animationFiles = recurseFind($dir, '/.iwp$/', 0 );
//   $animationUrl = preg_replace("!$animationPath!", "", $file);
//   $animationUrl = preg_replace("!^/!", "", $animationUrl ); // take of front slash.
//      echo "animationUrl: $animationUrl\n";
?>
        <!-- Directory -->
        <div class="row">
            <div class="col-lg-12">
                <h3><?php if ( $dir != $searchPath ) { echo $dir; } ?></h3>
            </div>
        </div>
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
            <img src="./screenshots/<?= $file ?>.png" alt="">
                <div class="caption">
                   <h3><?= $name ?></i></h3> <!-- color: #FFD700; -->
                          <a href="<?= $animateUrl ?>/<?= $uri ?>" class="btn btn-default">Animate</a>
                        <p>
			<i class="fa fa-star-o fa-1x" style="color: #ccc;"></i>	
			<?= readIwpFileDescription($file) ?>
                        </p>
                </div>
             </div>
           </div>
<?php } ?>
	</div>
<?php } ?>

     </div>
</body>
</html>
