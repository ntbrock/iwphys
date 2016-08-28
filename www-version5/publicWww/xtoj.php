<?php
// 2016-Jun-14 Brockman concept for converting xml to json for iwp files
// http://stackoverflow.com/questions/8830599/php-convert-xml-to-json

require_once('iwp-config.php');

$json = readIwpFileJson($iwpFile);

header('Content-Type: application/json');
echo $json;

?>