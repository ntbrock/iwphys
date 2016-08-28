<?php
// Intended to be included by iwp-config, once.
//----------------------------------------------------------------------------------------
// Relative URL Routes

$baseUri     = $prefixUri;
$animateUri  = $prefixUri . "animate.php";
$xtojUri     = $prefixUri . "xtoj.php";
$browseUri   = $prefixUri . "browse.php";


//----------------------------------------------------------------------------------------
// Web Script path setup

$pathInfo = $_SERVER['PATH_INFO'];

// The fully formed URL to the problem file.
// Special Cloudflare logic to detect forwarded SSL  2015Feb25 Brockman
$httpSecure = false;
if ( ! empty($_SERVER['HTTPS']) ) { $httpSecure = true; }
if ( ! empty($_SERVER['HTTP_X_FORWARDED_PROTO']) && $_SERVER['HTTP_X_FORWARDED_PROTO'] == 'https' ) { $httpSecure = true; }


$animateUrl = ( $httpSecure ? 'https://' : 'http://' ) . $_SERVER['HTTP_HOST'] . $animateUri . $pathInfo;
$baseAnimateUrl = ( $httpSecure ? 'https://' : 'http://' ) . $_SERVER['HTTP_HOST'] . $animateUri;
$xtojUrl = ( $httpSecure ? 'https://' : 'http://' ) . $_SERVER['HTTP_HOST'] . $xtojUri . $pathInfo;
$baseUrl = ( $httpSecure ? 'https://' : 'http://' ) . $_SERVER['HTTP_HOST'] . $baseUri;
$browseUrl = ( $httpSecure ? 'https://' : 'http://' ) . $_SERVER['HTTP_HOST'] . $browseUri;


//----------------------------------------------------------------------------------------
// Filesystem Scanning

// Look for the fiel on disk
$iwpFile =  $animationPath . $pathInfo;
if ( ! file_exists ( $iwpFile ) ) { die("No Such File: " . $iwpFile); }


function readIwpFileJson($fullPath) { 
	 $xml_string = file_get_contents($fullPath);
	 $xml = simplexml_load_string($xml_string);
	 return json_encode($xml);
}

function readIwpFileDescription($fullPath) { 
	 $json = readIwpFileJson($fullPath);
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
function str_replace_first($from, $to, $subject)
{
    $from = '/'.preg_quote($from, '/').'/';
    return preg_replace($from, $to, $subject, 1);
}


// This method is a recursive directory find
// Rather than call find . |grep iwp, I use the readdir to make it system agnos
function recurseFind($dir, $pattern, $depthRemaining )
{
	global $filesep;
	$subFiles = array();
	$subDirs = array();

	$dh = opendir($dir);
	while ( false != ($file = readdir($dh)) ) { 
		if ( $file != '.' && $file != '..' ) {

			$fullFile = $dir . DIRECTORY_SEPARATOR . $file;

			if ( is_dir($fullFile) ) { 
				array_push($subDirs, $fullFile);
			} else {
				if ( preg_match($pattern, $fullFile) ) { 
					array_push($subFiles, $fullFile);
				}
			}
		}
	}
	closedir($dh);

	// 2016-Aug-28 -- Limiting the recursion
	if ( $depthRemaining > 0 ) { 

	foreach ( $subDirs as $subDir ) { 
		foreach ( recurseFind($subDir, $pattern, $depthRemaining-- ) as $subsubFile ) {
			array_push($subFiles, $subsubFile );
		}
	}

	}

	return $subFiles;
}

function recurseDirs($dir, $depthRemaining )
{
	global $filesep;
	$subFiles = array();
	$subDirs = array();

	$dh = opendir($dir);
	while ( false != ($file = readdir($dh)) ) { 
		if ( $file != '.' && $file != '..' ) {

			$fullFile = $dir . DIRECTORY_SEPARATOR . $file;
			if ( is_dir($fullFile) ) { 
				array_push($subDirs, $fullFile);
			} else {
			// SKIP ALL FILES
			}
		}
	}
	closedir($dh);

	// 2016-Aug-28 -- Limiting the recursion
	if ( $depthRemaining > 0 ) { 

	foreach ( $subDirs as $subDir ) { 
		foreach ( recurseDirs($subDir, $depthRemaining-- ) as $subsubFile ) {
			array_push($subDirs, $subsubFile );
		}
	}

	}

	return $subDirs;
}

return '';
?>