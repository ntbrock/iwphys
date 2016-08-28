<?
// 2008-Jan-25 Brockman NCSSM IWP
// Simple PHP replacement for legacy .cgi script.
// Lists all the problems for easy copy + paste.

include_once('common.inc');


common_header("Problem List");
?>

<b>IWP Problem List</b><br>
<br>

<?
$problemFiles = recurseFind($problemPath, '/.iwp$/');
foreach ( $problemFiles as $file ) { 
	$problemUrl = preg_replace("!$problemPath!", "", $file);
	$problemUrl = preg_replace("!^/!", "", $problemUrl ); // take of front slash.
	echo "<a href=\"$animateUri/" . $problemUrl . "\">$problemUrl</a><br>\n";
}

common_footer();

?>






<?
// This method is a recursive directory find
// Rather than call find . |grep iwp, I use the readdir to make it system agnos
function recurseFind($dir, $pattern)
{
	global $filesep;
	$subFiles = array();
	$subDirs = array();

	$dh = opendir($dir);
	while ( false != ($file = readdir($dh)) ) { 
		if ( $file != '.' && $file != '..' ) {

			$fullFile = $dir . $filesep . $file;

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

	foreach ( $subDirs as $subDir ) { 
		foreach ( recurseFind($subDir, $pattern) as $subsubFile ) {
			array_push($subFiles, $subsubFile );
		}
	}
	return $subFiles;
}

?>

