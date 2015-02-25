<?
// 2008-Jan-25 Brockman NCSSM IWP
// New-age php script tha trenders the right java code based on the PATH_INFO

include_once('common.inc');

// in url bar. Need to be respectful of the file paths.

$iwpPath = $_SERVER['PATH_INFO'];

// The fully formed URL to the problem file.
// Special Cloudflare logic to detect forwarded SSL  2015Feb25 Brockman
$httpSecure = false;
if ( ! empty($_SERVER['HTTPS']) ) { $httpSecure = true; }
if ( ! empty($_SERVER['HTTP_X_FORWARDED_PROTO']) && $_SERVER['HTTP_X_FORWARDED_PROTO'] == 'https' ) { $httpSecure = true; }

$iwpUrl = ( $httpSecure ? 'https://' : 'http://' ) . $_SERVER['HTTP_HOST'] . $prefixUri . '/' . $problemPath . $iwpPath;

$iwpUrl = preg_replace("/[ ]/", "%20", $iwpUrl, -1 );

?>

<? common_header('Animate Problem'); ?>
<br>

<center>Filename: <b><?= $iwpPath ?></b></center><br>

<center><applet code="edu.ncssm.iwp.bin.IWP_Applet" archive="<?= $jarUri ?>" width="400" height="200">
<param name="problem" value="<?= $iwpUrl ?>">
<param name="student" value="true">
</applet></center>

<br><br>


  <center><p><i><font size="+2">Java Applet not running?</font></i></p><p>Look for a 'Plug In Blocked' <img src="/pps/gfx/plugin-blocked.png"> in the URL location bar of your web browser. You will need to give your permission to run the applet!  Upgrading to Java Version 8 is also a great idea.</p></center>

<br>



<? common_footer(); ?>
