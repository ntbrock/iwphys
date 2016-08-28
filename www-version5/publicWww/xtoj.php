<?php
require_once('iwp-config.php');


// http://stackoverflow.com/questions/8830599/php-convert-xml-to-json
// 2016-Jun-14 Brockman concept for converting xml to json for iwp files

$basePath = "./animations";
$pathInfo = $_SERVER['PATH_INFO'];
$fullPath = $basePath . $pathInfo;

// echo $fullPath;

$xml_string = file_get_contents($fullPath);

$xml = simplexml_load_string($xml_string);
$json = json_encode($xml);

header('Content-Type: application/json');
echo $json;

$array = json_decode($json,TRUE);

?>