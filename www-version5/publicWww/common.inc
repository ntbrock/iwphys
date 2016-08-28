<?php
/// Common.inc configuration file for IWP2 php replacement scripts.
//

require_once('iwp-config.php');


function common_header($sectionName) {
	global $gfxUri, $indexUri, $helpUri, $problemListUri;
?>

<html>
<head>
  <title>Interactive Web Physics : <?= $sectionName ?></title>
</head>

<body bgcolor="#ffffff" text="#000000" link="#333333" vlink="#333333" style="margin: 0px;">

<table border="0" width="100%" cellpadding="4" cellspacing="0">
<tr bgcolor="#cccccc">

<td width="33%" valign="left" align="left"><a href="<?= $indexUri ?>"><img src="<?= $gfxUri ?>/IWP_Banner.png" alt="Interactive Web Physics: A physics simulation tool for teachers and students" border="0"></a></td>
<td width="33%" align="center"><nobr><b><?= $sectionName ?></b></nobr></td>
<td width="33%" align="right">
	 <a href="<?= $problemListUri ?>">Problem List</a> &nbsp; &nbsp;
	<a href="<?= $helpUri ?>">HELP!</a>
</td>
</tr>


<tr>
<td colspan="3">


<?php
} 

function common_footer() {

?>


</td>
</tr>
</table>
	<center>Copyright &copy; 2008-2016 Loren Winters, Taylor Brockman, IWP</center>
</body>
</html>


<?php
}

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
