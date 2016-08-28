<?php
// 2016-Aug-28 Brockman - Revamp for IWP5
// 'New-age' php script tha trenders the right java code based on the PATH_INFO

include_once('iwp-config.php');
header('Content-Type: text/html');

?><!DOCTYPE html>
<meta charset="utf-8">
<html lang="en">

<?php include_once("iwp-head.php") ?>

<body>

<?php include_once("iwp-nav.php") ?>

  <div id="problem" problem-uri="<?= $xtojUrl ?>"></div>

  <div id="canvasDiv">
    <svg id="canvas" viewbox="0 0 1000 1000" preserveAspectRatio="xMinYMin meet"></svg>
  </div>

  <div>
    <table id="playBar">
      <tr>  
        <td class="bottomBorder">
          <span>Time: </span>
          <span id="itime">--</span>
        </td> 
        <td id="buttonControls">
          <span><input id="resetButton" type="button" onclick="handleResetClick()" value="Reset"></input></span>
          <span><input id="backButton" type="button" onclick="handleBackClick()" value="<<"></input></span>
          <span><input id="startStopButton" type="button" onclick="handleStartClick()" value="Start"></input></span>
          <span><input id="forwardButton" type="button" onclick="handleForwardClick()" value=">>"></input></span>
        </td>
        <td class="bottomBorder">
          <span>Time Step: </span>
          <span><input id="itime_change" type="text" value="--" size="4em"/></span>
        </td>
      </tr>
    </table>
  </div>

 <table id="variableTable"></table>

 <table id="tabs">
  <tr>
    <td id="vtb" class="bottomBorder"><input id="variableTableButton" type="button" onclick="variableTableOn()" value="Variables"/></td>
    <td  id="ws" class=""><input id="windowSettings" type="button" onclick="windowSettingsOn()" value="Window Settings"/></td>
    <td  id="oib" class=""><input id="otherInfoButton" type="button" onclick="otherInfoOn()" value="Additional Information"/></td>
  </tr>
 </table>

 <table id="iwindow" align='center'>
    <tr><th>Window Settings</th></tr>
    <tr class="bottomBorder">
      <td>X min:</td>
      <td><input id="iwindow_xmin" type="text"></td></tr>
    <tr class="bottomBorder">
      <td>X max:</td>
      <td><input id="iwindow_xmax" type="text"></td></tr>
    <tr class="bottomBorder">
      <td>X grid:</td>
      <td><input id="iwindow_xgrid" type="text"></td></tr>
    <tr class="bottomBorder">
      <td>X unit:</td>
      <td><input id="iwindow_xunit" type="text"></td></tr>
    <tr class="bottomBorder">
      <td>Y min:</td>
      <td><input id="iwindow_ymin" type="text"></td></tr>
    <tr class="bottomBorder">
      <td>Y max:</td>
      <td><input id="iwindow_ymax" type="text"></td></tr>
    <tr class="bottomBorder">
      <td>Y grid:</td>
      <td><input id="iwindow_ygrid" type="text"></td></tr>
    <tr class="bottomBorder">
      <td>Y unit:</td>
      <td><input id="iwindow_yunit" type="text"></td></tr>
 </table>

  <table id="otherInfo">
    <tr>
      <th colspan="2">Additional Information</th>
    </tr>
    <tr class="bottomBorder">
      <td class="italic"><p>GraphWindow</p></td>
      <td><div id="graphWindow">- Coming Soon -</div></td>
    </tr>
    <tr class="bottomBorder">
      <td class="italic"><p>Description</p></td>
      <td><div id="description"></div></td>
    </tr>
    <tr class="bottomBorder">
      <td class="italic"><p>Author: </p></td>
      <td><div id="authorUsername">Loading...</div></td>
    </tr>
  </table>


  <script type="text/javascript">
    // On Pageload, pull our hardcoded unit test.
    $.getJSON( $('#problem').attr('problem-uri'), function(problem) {
      parseProblemToMemory(problem);
      renderProblemFromMemory();
      calculateVarsAtStep(0);
    });
  </script>
</body>
<html>
