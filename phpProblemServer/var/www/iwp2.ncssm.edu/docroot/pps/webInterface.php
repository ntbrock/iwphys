<?
// 2008-Jan-25 Brockman NCSSM IWP
// New-age php script tha trenders the right java code based on the PATH_INFO

include_once('common.inc');

// in url bar. Need to be respectful of the file paths.

$iwpPath = $_SERVER['PATH_INFO'];

// The fully formed URL to the problem file.
$iwpUrl = 'http://' . $_SERVER['HTTP_HOST'] . $prefixUri . '/' . $problemPath . $iwpPath;

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

<center>(<i>The Java Applet will create resizable popup windows on your desktop</i>)</center><br>
<br>


<? common_footer(); ?>
