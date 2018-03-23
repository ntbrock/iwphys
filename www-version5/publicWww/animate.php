<?php
// 2016-Aug-28 Brockman - Revamp for IWP5
// 'New-age' php script tha trenders the right java code based on the PATH_INFO

include_once('iwp-config.php');
header('Content-Type: text/html');

?><!DOCTYPE html>
<meta charset="utf-8">
<html lang="en">

<head> 
  <?php 
    error_reporting(E_ERROR | E_WARNING | E_PARSE); //Dev
    $config_suppressHeadOgTags = true;
    include_once("iwp-head.php") 
  ?>

  <!-- IWP5 Animation includes -->
  <link href="<?= $baseUrl ?>css/iwp5.css" rel="stylesheet">
  <link href="<?= $baseUrl ?>css/iwp5-graph.css" rel="stylesheet">
  <meta property="og:title" content="<?= str_replace(".iwp","",end(explode('/', $animateFile))); ?> - Interactive Web Physics"/>
  <meta name="og:description" content="Click to play animation in your web browser."/>
  <script charset="utf-8" type="text/javascript" src="<?= $baseUrl ?>js/iwp5.js"></script>
  <script charset="utf-8" type="text/javascript" src="<?= $baseUrl ?>js/iwp5-ui.js"></script>
  <script charset="utf-8" type="text/javascript" src="<?= $baseUrl ?>js/iwp5-graph.js"></script>

</head>

<body>

<?php include_once("iwp-nav-minimal.php") ?>

<!-- Json IWP Content controls -->
<div id="problem" problem-uri="<?= $xtojUrl ?>"></div>
     
<div class="iwp-animation-container">
  <div class="iwp-animation">

  <!-- Left Hand Side -->
  <div class="iwp-left-container">

    <!-- Time controls -->
    <div class="iwp-control-container iwp-time-control-container">
      <table class="iwp-time-controls">
        <tr>  
          <td id="time">
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
    </div>

    <!-- Canvas -->
    <div id="canvasDiv" class="iwp-animation-canvas-container">
      <svg id="canvas" class="iwp-animation-canvas" viewbox="0 0 1000 1000" preserveAspectRatio="xMinYMin meet" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg">
        <g id="gridlines"></g>
      </svg>
    </div>

  </div>

  <!-- Right Hand Side -->
  <div class="iwp-right-container">

    <!-- Tab Controls -->
    <div class="iwp-control-container iwp-tab-control-container">
    <table class="iwp-tab-controls" class="tab">
      <tr>
        <td id="it" class="bottomBorder" onclick="animationTabOn()" iwp-tab="animationTab">
          <i id="inputTableButton" class="fa fa-list fa-2x"></i> <br/>Animate</td>
        <td id="gt" class="" onclick="graphTabOn()" iwp-tab="graphTab">
          <i id="graphTableButton" class="fa fa-area-chart fa-2x"> </i> <br/>Graph</td>
        <td id="oib" class="" onclick="timeTabOn()" iwp-tab="timeTab">
          <i id="timeTabButton" class="fa fa-clock-o fa-2x"> </i> <br/>Time</td>
        <td id="ws" class=""  onclick="iwindowTabOn()" iwp-tab="iwindowTab">
          <i id="windowSettings" class="fa fa-clone fa-2x"> </i> <br/>Axes </td>
      </tr>
    </table>
    </div>

    <div class="iwp-tab-container">

      <!-- 2018 Animation Table now contains description and some re-imagined inputs -->
      <div class="iwp-tab" id="animationTab"  style="display:inline;"> 

        <div id="animationTitle">
            <h3><?= str_replace(".iwp","",end(explode('/', $animateFile))); ?></h3>
            <br/><div id="description"></div>
        </div>

        <table id="inputTable"></table>

        <table id="outputTable"></table>

      </div>


      <!-- 2018 Graph Table now contains an SVG canvas for the graph -->
      <div id="graphTab" class="iwp-tab"> <!-- For easier debugging, starting w/ graph tab on -->
        <svg id="graph" class="iwp-graph" viewBox="-100 -100 200 200">    

        <div id="graphControls" class="iwp-graph-controls"></div> <!-- Written by iwp5-graph.js -->

      </div>


      <!-- Window Settings -->
      <div id="iwindowTab" class="iwp-tab3column">
        <table class="trim">
          <tr><th colspan="3">Animation Axes Scale</th></tr>
          <tr>
            <td style="width:33%"></td>
            <td style="width:33%">X</td>
            <td style="width:33%">Y</td>
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
            <td style="width: 100%;"><div id="iwindow_xunit"></td>
            <td style="width: 100%;"><div id="iwindow_yunit"></td>
          </tr>
        </table>
      </div>



     <!-- Time Controls -->
      <div id="timeTab" class="iwp-tab">
        <table class="trim">
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

    </div> <!-- end iwp-tab-container -->
  </div> <!-- end iwp-right-container -->

</div> <!-- end iwp-animation -->
</div> <!-- end iwp-animation-container -->

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
