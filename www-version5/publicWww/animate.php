<?php
// 2016-Aug-28 Brockman - Revamp for IWP5
// 2008-Jan-25 Brockman NCSSM IWP
// 'New-age' php script tha trenders the right java code based on the PATH_INFO

include_once('iwp-config.php');



?>
<p>animateUrl: <?= $animateUrl ?></p>
<p>xtojUrl: <?= $xtojUrl ?></p>
<p>iwpFile: <?= $iwpFile ?></p>

<p>Description: <?= readIwpFileDescription($iwpFile) ?>
