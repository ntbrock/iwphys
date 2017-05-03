    <!-- Template - StartBootStrap.com Heroic Features - https://startbootstrap.com/template-overviews/heroic-features/ -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Interactive Web Physics Animations">
    <meta name="author" content="Taylor Brockman">

    <!-- Link unfurling -->
    <?php
        if ( isset($config_suppressHeadOgTags) && $config_suppressHeadOgTags ) {
            
        }
        else { ?>
            <meta property="og:title" content="IWP5"/>
            <meta property="og:description" content="Interactive Web Physics is a Java and JavaScript, Web-based animation and problem designer tool. Anyone can quickly design mathematics or physics animations and simulations that run in a web browser."/>
            <meta property="og:image" content="screenshots/unfurlSample.png"/>
            <meta property="twitter:card" content="summary_large_image">
        <?php }
    ?>

    <title>Interactive Web Physics - iwphys.org</title>

    <!-- Bootstrap Core CSS -->
    <link href="<?= $baseUrl ?>css/bootstrap.css" rel="stylesheet">
    <link href="<?= $baseUrl ?>css/font-awesome.css" rel="stylesheet"> <!-- Version 4.6.3 -->

    <!-- Custom CSS -->
    <link href="<?= $baseUrl ?>css/heroic-features.css" rel="stylesheet">

    <!-- IWP Common Application CSS -->
    <link href="<?= $baseUrl ?>css/iwp-common.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script charset="utf-8" type="text/javascript" src="<?= $baseUri ?>js/jquery-3.0.0.js"></script>


    <!-- Bootstrap Core JavaScript -->
    <script src="<?= $baseUrl ?>js/bootstrap.min.js"></script>

    <script charset="utf-8" type="text/javascript" src="<?= $baseUrl ?>js/d3.min.js"></script>
    <script charset="utf-8" type="text/javascript" src="<?= $baseUrl ?>js/math.js"></script>

<?php return ''; ?>