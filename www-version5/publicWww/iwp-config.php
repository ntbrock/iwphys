<?php
// 2016-Aug-28 Brockman - IWP5 
//
//----------------------------------------------------------------------------------------
// File Path Setup

// where the problems on disk are located.
$animationPath = "animations";


//----------------------------------------------------------------------------------------
// Relative URL Routes

$prefixUri       = '/';
$indexUri        = $prefixUri;
$animateUri      = $prefixUri . "animate.php";
$xtojUri         = $prefixUri . "xtoj.php";
$problemListUri  = $prefixUri . "problemList.php";


//----------------------------------------------------------------------------------------
// Web Script path setup

$pathInfo = $_SERVER['PATH_INFO'];

// The fully formed URL to the problem file.
// Special Cloudflare logic to detect forwarded SSL  2015Feb25 Brockman
$httpSecure = false;
if ( ! empty($_SERVER['HTTPS']) ) { $httpSecure = true; }
if ( ! empty($_SERVER['HTTP_X_FORWARDED_PROTO']) && $_SERVER['HTTP_X_FORWARDED_PROTO'] == 'https' ) { $httpSecure = true; }


$animateUrl = ( $httpSecure ? 'https://' : 'http://' ) . $_SERVER['HTTP_HOST'] . $animateUri . $pathInfo;
$xtojUrl = ( $httpSecure ? 'https://' : 'http://' ) . $_SERVER['HTTP_HOST'] . $animateUri . $pathInfo;


//----------------------------------------------------------------------------------------
// Filesystem Scanning

// Look for the fiel on disk
$iwpFile =  $animationPath . $pathInfo;
if ( ! file_exists ( $iwpFile ) ) { die("No Such File: " . $iwpFile); }

$description = 'todo';

function readIwpFileDescription($fullPath) { 
	 $xml_string = file_get_contents($fullPath);
	 $xml = simplexml_load_string($xml_string);
	 $json = json_encode($xml);
	 $jobject = json_decode($json,true);
	 return $jobject['objects']['description']['text'];
}

//----------------------------------------------------------------------------------------
// Functions

// http://stackoverflow.com/questions/834303/startswith-and-endswith-functions-in-php
function endsWith($haystack, $needle) {
  // search forward starting from end minus needle length characters
  return $needle === "" || (($temp = strlen($haystack) - strlen($needle)) >= 0 && strpos($haystack, $needle, $temp) !== FALSE);
}
function startsWith($haystack, $needle) {
  // search backwards starting from haystack length characters from the end
  return $needle === "" || strrpos($haystack, $needle, -strlen($haystack)) !== FALSE;
}

?>