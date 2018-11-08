# IWP5 New Server Concept 

## 2018Nov08 Brockman

"IWP5 New Server Concept" describes the motivations, considerations for, information modeling, and scope of tasks that would be involved with the Interactive Web Physics team replacing the Version 3 PHP hosting infrastructure with a modern alternative model view controller implementation.


### Motivations and Drivers

- Our PHP hosting rig that runs iwphys.org is becoming rapidly outdated.  While it does provide great instruction for how to provision, update, test, and maintain a linux web host running php, nginx, and fpm, this process is now less attractive in the era of multiplatform.  Within the past 24 months, we have succesfully installed on Windows, Mac, Linux, but required signficant trial, error, and setup time.  Most recently, a MacOS Mojave update broke a mac homebrew nginx installation, and we had to fall back to a ubuntu 16 virtual machine.  Note, is similar to the motivator to build IWP5 : the lack of support in modern browsers for legacy application frameworks (Java Applets).  All time is precious, especially software development time and I want to spend it on feature work, not terraforming environments.

- For the next Big Feature, we are considering rebuilding the Designer interface to completely separate from the iwp4 codebase.  This will enable Physics authors and educators to create and share their dynamic IWP animations using only a web browser.  No desktop java runtime or any special tools required.  The new Designer may demand a fresh animation storage system;  I think it would be more efficient to use a great NoSQL community edition database instead of bolting it onto a php / raw linux filesystem.  The new daemons are fast, efficient, and reliable- although I did have to restart one this morning due to what I think was high volume filehandle exhaustion!

- Professional teams around me have had success with Play Framework, a modern model view controller package that runs in JVM, great for destkop development, plus easy server side / cloud deployment.  I personally have a lot of experience with play scala and java, and my development speed would be great.  Knowledge transfer of play framework to new developers is straightfoard, lots of public + personal resources available.



## Information Sharing on the Modern Web

- Sites for easily editing, saving, and distributing hosted web content have proliferated. JS Fiddle, Geojson.io, Github Gists, D3 Blocks, and Scala.js are favorite examplels.

Examples:
	https://beta.observablehq.com/@mbostock/d3-sankey-diagram
	
	https://scalafiddle.io/sf/1xV3xo8/0
	
Key features include:

	- Cloning or forking existing content from other users

	- Permalinks (aka canonical urls) become semi-permanent on the web and easily dirstirbuted or plugged into other applciations (WebAssign)

	- In browser compile checking

	- Instant animation or content preview. Make a change, hit run, test it, repeat.


- We have to make a decision on the centralization -vs- distribution of the Animation content.  Should we host an animation repository on behalf of our users, letting anyone or registered users, submit content at will?  Or do we require authors to provide their own web hosting, dropox, github, etc and we just refer to their 3rd party content?  Could we make this a dynamic choice?

- IWP5 could provide our packaged animations as base EXAMPLES so that any user could clone any one of our animations for their own used, likely not published in our public directory, or could be requested to be added to the public site.  By keeping new user animation clones private, we could help insulate our .org www presence from improper user submitted content.

- So far in 2018, we have defined (in trello) an effective process for testing content. Albert and Niall have been moving quickly through the packaged content, originally authored by Loren Winters. We are quantifying which animations test good, which need content / clarification updates, and which have display/rendering artifacts in the new iwp5.js animator and will require code fixes.  Example of recent fixes were tightening up the graph controls to better match IWP4 visibility and user multi selection options.

- Arbitrary javascript execution should never be allowed, only well formed Json content 


## Data Modeling, Serde, and Storage Design

- The new .iwp storage format is Json, no longer XML.  .json files are easily hosted, stored, and distributed via URLs.  Interview Question: What does the acronym REST stand mean to you?  Hint: The best answer possible is "Representational State Transfer" and then to be able to describe why that is important for creating efficient distributed, collaborative software applications.

- Create a diagram or model for the structure of an IWP.  Then write a json schema for it.

- We have a variety of json schema validation standards + tools available for us to help keep content formatting and serialization quality high, versioned, and migratable, given unforseeable futures.

- Implement a json validation routine, iwp-validate.js? That's a standalone parser / detection / tester / linter that's run over any and all content.

- Detect circular references in the modern iwp5.js validation layer. A validation / linting routine for the animations (like the badges on the github pages that show sbt/ivy distirbutions, versions, and are autochecked).

- Enable user editing / submission of the json directly or restrict access to posting content to only what's created by the designer UI? "View Source".

- Should we pick a tool like couchbase that woudl do it's own authentication / silo'ing directly communicating XHR to the browser?  This would save us from having to write a plumbing layer at all.  Or go 'classic auth' with a single application account?

- Would be great to be able to auto-generate the screenshots, even initial SVG animation images, to show automated previews of any user animation in directory views for quickly finding the time=0 image of the animation you want to play.


- Streamline developer environment installation 
	- Install JDK
	- Install SBT
	- Github SSH Connectivity, Git Clone
	- sbt run
	- Browse to localhost:8xxx
	- Begin playing, editing animations.
	- Shared database?  Could we avoid checking this password into open source?
	- IP Whitelisting for security?
	- The Javascript modules would be distributed in the play application.
	- The play application itself only needs a few functions:
		Serving the static .js content 
		proxy REST posting of anitmation content -- 1 to 1 with the URL?
		User nicknames or profiles?
		Directory browsing of published content 
		HTTP environment for localhost development -- be sure to not ask user to recompile scala with javascript/css mods only.   Pretty good at super-fast JS dev now.
		
		

## Tasking


### Milestone 0 - Solidify The Data Model

1. Define a Json schema for IWP5.js with visualizations, examples - 6h

1. Bulk convert packaged Xml Animations to their JSON counterparts, leverage php - 6h

1. Choose and tool out a json schema validator application, run over packaged content, refine schema - 8h

1. Refactor the iwp5.js code for Animation parsing out to a new iwp5-validation.js library - 4h

1. Implement a circular reference detector in iwp5-validation - 8h

### Milestone 1 - New Web Host + Development Environment

1. Create Play project skeleton, CAST port - 2h

1. Provision developer laptops with new play skeleton - 2h

1. Define Routes, Setup Layout, Welcome page - 4h
	Forklift as much as possible of the css / html / js from the PHP app, dont redevelop.

1. Relocate the iwp5.js content from the php folders and place over into new play location - 2h

1. Choose, Provision, and Connect to Database Tech - 6h

1. Store our first IWP animation into database from Play - 4h

1. Enable Listing + Browsing + Retrieval of Play Animations in web UI - 8h

1. Implement the Cover art capture routine, svg grabber - 8h

1. Bulk import of all animations and directory information to new storage - 6h

1. Production Website Cutover and Deep link preservation testing - 8h


### Milestone 2 - New Designer

1. Enable users to anonymously replicate existing animation content to go to their own editor - 4h
	+F> When you clone, a creation cookie is created or used on your PC. 
	+F> Without the creation cookie, animations are READ ONLY and can only be cloned?
	+F> How to enable multi computer editing for one users who changes around?
	+F> Credientialing: Verification emails?  Password protect the animations?

1. Very simple raw JSON Editor to prove that animations can be modified, posted back - 4h

1. Designer Layout, Paneling, CSS architecture - 6h

1. Designer Object Browser - 6h

1. Designer handover to Animator to 'play' - 2h

1. Designer Equation Editor - 4h

1. Designer Color Picker and Geometry sizing - 2h

1. Designer Solid Editor - 4h

1. Designer Input / Output editors - 6h

1. Designer Developer Testing - 8h

1. Designer Author Testing  + Fixes - 8h

1. Designer End User Testing + Fixes - 8h

### Milestone 3 - Market Rollout

1. Record Demonstrations and HOWTOs - 6h

1. Prepare a slide deck presentation - 8h

1. Deliver the first friendly demo to a small audience - 4h

1. Deliver a medium size audience talk - 8h

1. Write a research paper describing why IWP5 is important and our techniques - 16h

1. Initial support for new Authors and Teachers with questions - 16h

1. Community engagement to promote the tool on forums, directories - 16h

1. Collect student testimonials, perform usability of Animator - 16h

1. Describe the roadmap of IWP5 in a community published document - 8h


## Future Ideas

	- Begin to enable new animation content types - Arbitrary images?  Arbitrary js animation object framework?
