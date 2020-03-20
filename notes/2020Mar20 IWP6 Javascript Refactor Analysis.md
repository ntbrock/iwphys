# Interactive Web Physics

##  Javascript Animator Refactor Analysis

2020Mar20 Brockman


Andy Wang, Ben Wu, and Taylor Brockman are considering adding RK4 calculator support to IWP6.  This document is an analysis of the existing javascript code base for animation, (note that the new react.js is a separate project ) and a recommendation on next steps.

We would implement RK4 as cleanly as possible in the 'new' style' with unit test coverage, then begin to refactor neighboring components out to their new proper location.


## Refactor Concepts

As each component is refactored, it should have a set of corresponding tests written as a companion. We will start with RK4 and build a great example that future contributors may choose to emulate.

Our refactor may take the form of a totally new standalone javascript top-level module, as imagined in master in animatorjs-version7, a javascript/yarn based build that's using Jest for unit testing.

### Unit Test Reading

https://blog.bitsrc.io/top-javascript-testing-frameworks-in-demand-for-2019-90c76e7777e9

https://github.com/facebook/jest

### Compiled Javascript Sub-module

Concepting this in animatorjs-version7/

If fully implemented, this would 'compile' the javascript to a static web location that would then be referenced by the play web pages.  The Web UI would become seperate from the javascript evaluator. 


### OO Class design

Object Types: solid, input, output, time, etc.  Each of these could be placed into an object specific source file.  Each object source file would have an implemetnation for the standard lifecycle of the object: compile, evaluate, illustrate, test, etc.

Would our new library be includable from the designer?

What steps would be necessary for develoeprs to implement new object types?

User Interface should to remain as isolated as possible from the backend calculation loop, so that we can evaluate and test animations server-side with node.js or nashorn.

Isolated features, such as Graphing, or Save As Png should be isolated into their own files.


## Entry Point

An important entry point between play and the javascript layer is iwp6-calc.js function parseAnimationToMemory : 1325.  A first step of a refactor would be to bubble this entry point out to something like iwp6-main.js.

When a user views an animation, the handoff from the Play layer to Javascript is in play-version6/app/views/animation/animation.scala

```
var animationJson = @Html(animation.toJsonString);

parseAnimationToMemory(animationJson);   // iwp6-calc.js:1325 

renderProblemFromMemory();  // iwp6-animator.js:61

masterResetSteps(); // iwp6-calc.js:95
```


## Euler Calculator Call Stack

It is revealing to analyze the full stack trace for how the Euler's Calculators get evaluated today.

### Compile

```
iwp6-calc.js:1012 compileCalculator
	migrateLegacyEquation
	mathjs
```

### Initialization
```

calc:512 calculateVarsAtStep(step)

calc:609 initializeEulerCalculator(solid, step, vars, axis, calculator)


```

### Evaluation (Each step of the Animation)

```
iwp6-animator:455 handleStartClick()

iwp6-animator:463 handleGoClick()

iwp6-calc:178 handleStep()

iwp6-calc:512 calculateVarsAtStep(step)

iwp6-calc:329 calculateSolidAtStep(solid, step, vars, verbose)

Calls for X and Y and Height and Width, Angles, Floating Texts

iwp6-calc:1100 evaluateCalculator ( resultVariable, calculator, calculateStep, vars, verbose, objectName )

iwp6-calc:1190 evaluateEulerCalculator( resultVariable, calculator, calculateStep, vars, verbose, objectName )

```

## RK4 Proposal

New JS File called:   iwp6-calculator-rk4.js  or  version7/app/calculator/rk4.js

Refactor Euler's out to iwp6-calculator-euler.js  or  version7/app/calculator/euler.js

What are the common traits/functions that each calculator must have?  Do we relocate the designer hints / schema requirements here?

Refactor the compileCalculator and main calculator loop to iwp6-calculator.js or version7/app/calculator/calculator.js

Revisit if method signature is ok, can this be independently tested?  A good next step would be to build a clean room re-impementation of 

( resultVariable, calculator, calculateStep, vars, verbose, objectName )

Write standalone, NO user interface unit tests test-calculator-rk4.js

Invoke the rk4 tests from play / nashhorn? (Heavy) Better Js testing framework? Yes = Jest


## General Refactor Proposal

Strongly consider going to animatorsj-version7/, new .js project with pre-compiling, minification, and unit testing!

### Objectification

Series of new JS files called:  iwp6-object-solid.js, iwp6-object-input.js, iwp6-object-output.js, iwp6-object-floatingText, etc.

Each object type would have functions:
	
	compile - No Side effects
	
	illustrateSvg

	schema
	
	designerProperties
	
	unitTests


### Parsing 


New JS file called iwp6-parse.js,  place parseAnimationToMemory there

New JS File called iwp6-animator-svg.js , move most of the canvas + animation function helpers out.


Evaluate a js compiler (node.js) or at least minification and sure that localhost edit/rebuild cycles are as fast as possible.



## Globals

Version 6 of IWP uses Javascript globals.  We imagine a refactored codebase that minimizes this and instead passes context objects.


varsAtStep : Array

parsedAnimation : Object




## iwp6-animator : 514 lines

### function repaintStep(step) : 13

Loops over the parsedAnimation time, outputs, solids, and floatingTexts and updates the displays calling: updateTimeDisplay, updateUserFormOutputDouble, updateSolidSvgPathAndShape, and updateFloatTextSvgPathAndShape


### function setAuthorName(username) : 56

Called by parseAnimationToMemory

Jq Dom update #authorUsername




function renderProblemFromMemory() : 61

function fitText(input)

function addSolidsToCanvas(solids)

function renderCanvas()

function queryTimeStepInputDouble()

function queryTimeStartInputDouble()

function queryTimeStopInputDouble()

function queryUserFormInputDouble(input)

function queryUserFormWindowDouble(index)

function updateUserFormOutputDouble(output, newValue)

function updateTimeDisplay(t)

function updateSolidSvgPathAndShape(solid, pathAndShape)

function updateFloatingTextSvgPathAndShape(text, pathAndShape)

function handleStartClick()

function handleGoClick()

function handleStopClick()

function handleResetClick()

function handleBackClick()

function handleForwardClick()

function handleInputChange()



## iwp6-calc : 1621 lines

var deepExtend = function(out)

function varsAtStepJson(step)

function masterResetSteps()

function printDecimal( incomingNumber, incomingPlaces )

function setStepDirection(newDirection)

function stepForwardAndPause()

function stepBackwardAndPause()

function handleStep()

function archiveVarsAtStep( step, vars )

function calculateInputAtStep(input, step, vars, verbose )

function calculateOutputAtStep(output, step, vars, verbose)

function calculateFloatingTextAtStep(floatingText, step, vars, verbose)

function calculateSolidAtStep(solid, step, vars, verbose)

function calculateVarsAtStep(step)

function initializeEulerCalculator(solid, step, vars, axis, calculator)

function setTime(inTime)

function setDescription(inDescription)

function setWindow(inWindow)

function initializeGraphVars(s, inGraphWindow)

function setGraphWindow(inGraphWindow)

function compileInput(input)

function illustrateInput(input)

function compileOutput(output)

function illustrateOutput(output)

function resetSolidCalculators(solid)

function compileSolid(solid)

function illustrateSolid(solid)

function compileFloatingText(object)

function illustrateFloatingText(object)

function addObject(object)

function compileCalculator(iwpCalculator)

### function migrateLegacyEquation(toMigrate)

Simple regex that removes the .value completely from the equations.


function evaluateCompiledMath( compiled, vars )

function evaluateCalculator( resultVariable, calculator, calculateStep, vars, verbose, objectName )
	
function evaluateParametricCalculator( resultVariable, calculator, calculateStep, vars, verbose, objectName )
	
function evaluateEulerCalculator( resultVariable, calculator, calculateStep, vars, verbose, objectName )
	
### function parseAnimationToMemory( rawAnimation )

Entry Point

Accepts rawAnimation as a Json Structure, loops through all of its objects and constructs a new animation = { loop: [] }. Contains defensive checks to make sure the object types are valid (loop, time, description, etc.)

Calls:  setTime,  setDescription,  setWindow,  initializeGraphVars,  setGraphWindow,  reorderAnimationObjectsBySymbolicDependency, 


function decorateAnimationFunctions()
	
function yCanvas(y)
	
function xCanvas(x)
	
function xCanvasGridlines(x)
	
function yCanvasGridlines(y)
	
function xWidth(size)
	
function yHeight(size)
	
function ygraph(y)
	
function xgraph(x)
	
function xgraphGridlines(x)
	
function ygraphGridlines(y)
	
function gxWidth(size)
	
function gyHeight(size)

## iwp6-designer : 7 lines

	NOT USED


## iwp6-graph : 596 lines

function scaleAxes(graphParameters)

function graphInit(s)

function queryUserFormGraphDouble(s, index)

function graphSetWindowFromAnimation(s, graphWindow)

function updateGraph(s, graphWindow)

function graphResetZero(step, vars, solids, s, graphWindow)

function rgbColor(o)

function graphMeasureClick(button)

function graphStepForward(step, vars, s, graphWindow)

function graphStepBackward(step, vars, s)



## iwp6-help : 584 lines

function get_page_main_names(d_mains)

function activate_pages()

function activate_links()

function listen_to_dropdown()

function toggle_drpdwn()

function toggle_prdb()

function toggle_prdb_showall(event)

function on_prdb_showall(h_wrap)

function off_prdb_showall(h_wrap)

function toggle_display(elt)

function toggle_caret(i)

function on_caret(i)

function off_caret(i)

function get_info__push_r__page_map(d, depth, rList, big_name)

function make_b_dropdown(depth, info)

function make_ba(name, href, cls="")

function make_b(innerHTML, classList="")

function make_el(tag, attr_map)

function make_a_from_index(depth, info)

function make_a_from_href(depth, info)

function make_li(depth, info)

function make_d__push_dhd(depth, dList, dhList, i)

function make_u__push_ud(depth, uList, dhList, i)

function format_hab(h, info, more_bool)

function format_ha(h, info)

function make_h__page_map(depth, info, dh, more_bool)

function pop_these(list)

function l(list, distance=1)

function wrap(el, wrapper)

function get_toc_in_text(big_name)

function rc_toc_in_text()

function load_toc_in_text()

function get_toc_sidenav(big_name)

function rc_toc_sidenav()

function load_toc_sidenav()

function make_pretoc()

function make_d_paired()

function make_paired_btn(d_container, b1, b2)

function make_links_extent()



## iwp6-order : 622 lines

function reorderAnimationObjectsBySymbolicDependencyJsonStringify( unused )

function reorderAnimationObjectsBySymbolicDependency(loop)

function equationRequires(eqn)

function calculatorRequires(calc)

function animationObjectRequires(object)

function animationObjectProvides(object)

function arrayToObject(arr)

function arrayUnique(arr)

function animationObjectReorder(loop)



## iwp6-read : 62 Lines

function readAnimationObject( animation )

function readAnimationString( animationString )

function playAnimationToEnd( animationString )


## iwp6-ui : 62 lines

function animationTabOn()

function animationTabOff()

function graphTabOn()

function graphTabOff()

function iwindowTabOn()

function iwindowTabOff()

function timeTabOn()

function timeTabOff()

