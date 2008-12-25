<?
/// Common.inc configuration file for IWP2 php replacement scripts.
//

// Update this with the location of the script
$prefixUri = "/pps";

// fwd slash for unix. back slash for dos.
$filesep = '/';

// where the problems on disk are located.
$problemPath = "etc" . $filesep . "problems";

// Helpful urls.
$animateUri = $prefixUri . "/webInterface.php";
$problemListUri = $prefixUri . "/problemList.php";
$jarUri = $prefixUri . "/content/iwp_applet.jar";
$gfxUri = $prefixUri . "/gfx";
$helpUri = $prefixUri . "/help.php";
$indexUri = $prefixUri . "/";

$jarUri = $prefixUri . "/content/iwp_applet.jar";

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


<?
} 

function common_footer() {

?>


</td>
</tr>
</table>

	<center>Copyright &copy; 2008 Loren Winters, Taylor Brockman, IWP</center>
</body>
</html>


<? 
}
?>
