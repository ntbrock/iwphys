<?
include_once("common.inc");

common_header("Help");
?>




<b><font size="+2">IWP ProblemServer Walkthrough and Tutorial</font></b><br>
<b>Taylor Brockman - September 25, 2004</b>
<br>

<!-- ========================================================================= -->
<hr>


<p>
<b>0. Definitions</b><br>
<br>

<u>Animator</u>: The half of the IWP software program that will playback saved .iwp files
from the ProblemServer, the web, or the local Hard Drive.<br><br>
<u>Designer</u>: The half of the IWP software program that allows teachers to design problems and create and edit .iwp files.<br><br>
<u>ProblemServer</u>: A web interface and a web service that is a centrally located repository for storing, loading, editing and sharing .iwp animations.<br><br>
</p>

<!-- ========================================================================= -->
<hr>

<p>
<b>1. Download the latest JRE w/ the Java Plug-in</b><br>
<br>

<a href="http://java.sun.com/j2se/1.4.2/download.html">http://java.sun.com/j2se/1.4.2/download.html</a><br>
Recommended: J2SE v1.4.2_05 JRE<br><Br>

The applet has been tested to run under both Windows and RedHat Linux. 
<br>

<br>
<u>A warning about security:</u><br>
To enable the network problemServer communication, we had to use a 3rd Party XML-RPC 
library that needs access to some protected environment variables. This means that the
applet is signed by us, an untrusted signer. The first time that you load the applet,
you will see the following screen:

<center>
<img src="<?= $gfxUri ?>/walkthrough/appletSecurity.png" border="1"><br>
<b>Figure: Example Applet Security Warning</b><br>
</center>
<br>

You will need to click "YES" on this screen to grant the applet the correct access rights
to be able to use the problemServer communication.

</p>
<br>

<!-- ========================================================================= -->
<hr>

<p>
<b>2. Use the Animator: Browse User Problems (Students)</b><br>
<br>

Load the <a href="/pps/webInterface.cgi/Example%20Problems/">Example User Problems directory</a>. Here you will see sample files that you can animate.<br>

<center>
<img src="<?= $gfxUri ?>/walkthrough/browseProblems.png" border="1"><br>
<b>Figure: Browsing Example Problems</b><br>
</center><br>

Clicking on the filename (Example Target Problem.iwp) of a file will bring up an Animator Applet webpage. There is a 
java applet tag in this page that should cause the Animator applet to appear as a 
new desktop window and load the desired problem from the problemServer.<br>
<br>

<center>
<img src="<?= $gfxUri ?>/walkthrough/viewProblemHTMLPage.png" border="1"><br>
<b>Figure: Animating a problem: HTML Page - Applet will popup</b><br>
</center><br>

<center>
<img src="<?= $gfxUri ?>/walkthrough/viewProblemAnimator.png" border="1"><br>
<b>Figure: Animating a problem: Animator Applet View - Popup Window</b><br>
</center><br>

At this point, you can interact with the Animator to play the problem back and 
forth, edit input values, and interact with the physics problem.<br>

<i>TODO: Explain more about the animator controls.</i><br>

<br><br>
<u>Webmaster Tip</u>: You can deep-link to the animator page from other websites to 
integrate IWP content within your own website. To link to the example problem above
create an A HREF link to: <a href="/pps/webInterface.php/Example%20Problems/Example%20Target%20Problem.iwp">/pps/webInterface.php/Example%20Problems/Example%20Target%20Problem.iwp</a> (use %20 for spaces). This will work for files out of any public iwp problemServer directory, even your own user directory.

</p>


<!-- ========================================================================= -->
<hr>

<br>
<u>3.4 Local Files</u> are usable because the applet is signed. You can use the 
'Local File' option under the 'Open', 'Save', and 'Save As' to work locally on 
.iwp files.<br>

<br>

<u>Tip:</u> To make a copy of a file, Load it from the problemServer with the designer
and then 'Save As' to save it under a new filename in your /Users/ directory.<br>
<br>

<u>Tip:</u> Every IWP Problem file must have a .iwp extension on it to be properly
recognized by the client and the server<br><br>

<!-- ========================================================================= -->
<hr>


<b>4. Download the iwp.jar locally and Create a User Account and begin designing problems</b><br>
<br>
<i>TODO: Write Documentation on this</i><br>

<pre># build release;</pre>  creates a developer.jar that is manifested.


<?
common_footer();
?>

