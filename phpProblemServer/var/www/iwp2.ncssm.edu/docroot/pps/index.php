<?
include_once("common.inc");

common_header("Welcome");
?>

<center><font size="+2">Welcome to <b>Interactive Web Physics</b>, Problem Server.</font></center>

<p align="justify">The IWP Animator is a tool that displays animations and simulations using a Java Swing desktop or applet client. The Animator
	interface allows a user to pause, rewind, and step through animations as well as adjust the inputs to the problem to change the
	outcome. The Animator reads 'Problem Files' (XML files) containing parameter data that describes a situation that could be modeled
with parametric equations or Euler + RK2 simulations.</p>


<p align="justify">The IWP Designer is a companion tool that makes Problem Files easy to create. Many unique types of problems can
	designed using a combination of primitive objects: mathematical inputs, outputs, 'solid' objects, window ranges, and time parameters.
	With relative ease, anyone can create an equation system that makes simulation move. The designer has visual menus that
are responsive and easy to navigate.</p>


<p align="justify">The ProblemServer is a tool that ties the Animator and the Designer together to make them readily available to the
	general public. The ProblemServer is written in server-side Perl. It has functionality that helps web users view animations, design
	problems, and share files. The ProblemServer serves both the Animator and the Designer down to the web browsers as Java Applets.
You will need a browser with a Java Plugin installed.</p>

<p align="justify">IWP's code has been under developement at the <a href="http://www.ncssm.edu/">North Carolina School of Science
and Mathematics</a> since 1999. Responsibilites for maintenance and features have been passed down student to student for 5 years
under the supervision of Dr. Loren Winters. The project itself doubles an educational programming experience for students that teaches
Java Development, Source Control, Real World Debugging, and Team Software Development.</p>

<li><a href="http://iwp.sourceforge.net/">The IWP project development page is hosted at SourceForge.net</a>
<li>The code is distributed under the <a href="http://www.gnu.org/copyleft/gpl.html">GNU GPL Copyleft.</a>

<br><br>

<?
common_footer();
?>


