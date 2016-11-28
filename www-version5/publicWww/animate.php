<?php
// 2016-Aug-28 Brockman - Revamp for IWP5
// 'New-age' php script tha trenders the right java code based on the PATH_INFO

include_once('iwp-config.php');
header('Content-Type: text/html');

?><!DOCTYPE html>
<meta charset="utf-8">
<html lang="en">

<head>
  <?php include_once("iwp-head.php") ?>

  <!-- IWP5 Animation includes -->
  <link href="<?= $baseUrl ?>css/iwp5.css" rel="stylesheet">
  <script charset="utf-8" type="text/javascript" src="<?= $baseUrl ?>js/iwp5.js"></script>

</head>

<body>

<?php include_once("iwp-nav.php") ?>
     
  <div class="iwp-animate">

  <div id="problem" problem-uri="<?= $xtojUrl ?>"></div>

  <div id="canvasDiv">
    <svg id="canvas" viewbox="0 0 1000 1000" preserveAspectRatio="xMinYMin meet"></svg>
  </div>
  <table id="tabs">
    <tr>
      <td id="it" class="bottomBorder" onclick="inputTableOn()"><i id="inputTableButton" class="fa fa-list fa-2x"></i></td>
      <!-- 9 Nov 2016 Ryan Steed
        Removed to create single variable tab:
        <td id="ot" class=""><input id="outputTableButton" type="button" onclick="outputTableOn()" value="Outputs"/></td>
      -->
      <td id="oib" class="" onclick="timeTabOn()"><i id="timeTabButton" class="fa fa-clock-o fa-2x"></td>
      <td id="ws" class=""  onclick="windowSettingsOn()"><i id="windowSettings" class="fa fa-clone fa-2x"></i></td>
    </tr>
  </table>
  
 <div id="tabTables">
 <table id="inputTable" class="trim"></table>
 <table id="iwindow" align='center' class="trim">
    <tr><th colspan="3">Window Settings</th></tr>
    <tr>
      <td></td>
      <td>X</td>
      <td>Y</td>
    <tr class="bottomBorder">
      <td>Min:</td>
      <td><input id="iwindow_xmin" type="text"></td>
      <td><input id="iwindow_ymin" type="text"></td>
    </tr>
    <tr class="bottomBorder">
      <td>Max:</td>
      <td><input id="iwindow_xmax" type="text"></td>
      <td><input id="iwindow_ymax" type="text"></td>
    </tr>
    <tr class="bottomBorder">
      <td>Grid:</td>
      <td><input id="iwindow_xgrid" type="text"></td>
      <td><input id="iwindow_ygrid" type="text"></td>
    </tr>
    <tr class="bottomBorder">
      <td>Unit:</td>
      <td><input id="iwindow_xunit" type="text"></td>
      <td><input id="iwindow_yunit" type="text"></td>
    </tr>
 </table>
 <table id="timeTab">
    <tr>
      <th colspan="2">Time Controls</th>
    </tr>
    <tr class="bottomBorder">
      <td>
          <span>Time Step</span>
      </td>
      <td>
          <span><input id="itime_change" type="text" value="--"/> seconds</span>
      </td>
    </tr>
    <tr class="bottomBorder">
      <td>Start Time</td>
      <td><span><input id="itime_start" type="text" value="--"/> seconds</span></td>
    </tr>
    <tr class="bottomBorder">
      <td>Stop Time</td>
      <td><span><input id="itime_stop" type="text" value="--"/> seconds</span></td>
    </tr>
  </table>
  </div>

    <table id="playBar">
      <tr>  
        <td class="bottomBorder" id="time">
          <span><i class="fa fa-clock-o"></i></span>
          <span id="itime">--</span>
        </td> 
        <td id="buttonControls">
          <div onclick="handleBackClick()" id="backButton"><i class="fa fa-step-backward fa-lg"></i></div>
          <div onclick="handleStartClick()" id="startStopButton"><i id="startStopIcon" class="fa fa-play fa-lg"></i></div>
          <div onclick="handleForwardClick()" id="forwardButton"><i class="fa fa-step-forward fa-lg"></i></div>
          <div onclick="handleResetClick()" id="resetButton"><i class="fa fa-repeat fa-lg"></i></div>
        </td>
      </tr>
    </table>

  <table id="otherInfo">
    <tr>
      <th colspan="2">Additional Information</th>
    </tr>
    <tr class="bottomBorder">
      <td class="italic"><p>GraphWindow</p></td>
      <td><div id="graphWindow">Coming Soon</div></td>
    </tr>
    <tr class="bottomBorder">
      <td class="italic"><p>Description</p></td>
      <td><div id="description"></div></td>
    </tr>
    <tr class="bottomBorder">
      <td class="italic"><p>Author</p></td>
      <td><div id="authorUsername">Loading...</div></td>
    </tr>
  </table>
 

  </div> <!-- end iwp-animate -->

  <script type="text/javascript">
    // On Pageload, pull our hardcoded unit test.
    $.getJSON( $('#problem').attr('problem-uri'), function(problem) {
      parseProblemToMemory(problem);
      renderProblemFromMemory();
      // calculateVarsAtStep(0);
      masterResetSteps();
    });
  </script>

  <?php include_once('iwp-foot.php'); ?>

</body>
</html>
