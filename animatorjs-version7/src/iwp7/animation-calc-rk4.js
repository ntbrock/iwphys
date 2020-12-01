/**
 * Brand new Rk4 Calculator implementation!
 * @param resultVariable
 * @param calculator
 * @param calculateStep
 * @param changeStep
 * @param vars
 * @param verbose
 * @param objectName
 * @returns {{acceleration: null, step: *, displacement: number, velocity: number, value: number}}
 */

const calcMathJs = require('./animation-calc-mathjs')
const deepExtend = require('./deepExtend')

const CONFIG_throw_acceleration_calculation_exceptions = true;


function calculateAccel(mathJsCompiled, x, v, vars )  {

	// WARNING: These will overwrite each frame's data. I could jsut turn these off and
	// use Object.xvel, which would be much better.
	// vars.setAtCurrentTick( ACCEL_EQN_VAR_D, x);
	// vars.setAtCurrentTick( ACCEL_EQN_VAR_X, x);
	//vars.setAtCurrentTick( ACCEL_EQN_VAR_V, v);

	const varsOverride = deepExtend( { d: x, x: x, v: v}, vars);
	return mathJsCompiled.evaluate(varsOverride);

}

function evaluateRK4Calculator( resultVariable, calculator, calculateStep, changeStep, vars, verbose, objectName ) {

	const dt = vars["delta_t"];
	const t = vars["t"];

	try {
		if ( t === undefined ) {
			throw Error("No variable 't' at step: " + calculateStep + " in vars: " + JSON.stringify(vars));
		}
		if ( dt === undefined ) {
			throw Error("No variable 'delta_t' at step: " + calculateStep + " in vars: " + JSON.stringify(vars));
		}

		let acceleration = null;

		//if( curTick == 0 ) {
		if ( calculateStep === 0 ) {

			// double x0 = initDispEqn.calculate(vars);
			const x0 = calculator.initialDisplacementCompiled.evaluate(vars);

			//double v0 = initVelEqn.calculate(vars);
			const v0 = calculator.initialVelocityCompiled.evaluate(vars);

			const a0 = calculator.accelerationCompiled.evaluate(vars);

			const breaker41=true
				// initial acceleration calculation. The same for all Diff calculators.
			//	storePoint( curTick, vars.getCurrentTime(),
			//		x0, v0, calculateAccel(vars.getCurrentTime(), x0, v0, vars) );

			const pointsToStore = { x : x0, v : v0, a: a0, t : vars.t }
			calculator.historicalPoints[calculateStep] = pointsToStore;


		} else {
			// void calculatePointAfterZero

			// double dt = vars.getDeltaTime();

			// Get previous values
			const previous = calculator.historicalPoints[calculateStep-1];
			if ( previous === undefined ) {
				throw Error("No previous values defined at calculateStep: " + calculateStep );
			}

			// double t = ((Double)vT.elementAt(nTick - 1)).doubleValue();
			const tPrev = previous.t

			// double x = ((Double)vX.elementAt(nTick - 1)).doubleValue();
			const xPrev = previous.x

			// double v = ((Double)vV.elementAt(nTick - 1)).doubleValue();
			const vPrev = previous.v


			const rk4Vars = deepExtend( { v: vPrev, x: xPrev }, vars );

			// double kx0, kx1, kx2, kx3;
			// double kv0, kv1, kv2, kv3;

			const kx0 = dt * ( vPrev );
			const kv0 = dt * calculateAccel(calculator.accelerationCompiled, xPrev, vPrev, vars)
			const kx1 = dt * (vPrev + kv0 / 2);
			const kv1 = dt * calculateAccel(calculator.accelerationCompiled, xPrev + kx0 / 2, vPrev + kv0 / 2, vars);
			const kx2 = dt * (vPrev + kv1 / 2);
			const kv2 = dt * calculateAccel(calculator.accelerationCompiled, xPrev + kx1 / 2, vPrev + kv1 / 2, vars);
			const kx3 = dt * (vPrev + kv2);
			const kv3 = dt * calculateAccel(calculator.accelerationCompiled, xPrev + kx2, vPrev + kv2,vars);

			const breaker80=true


			// x += ((kx0) + (2 * kx1) + (2 * kx2) + (kx3)) / 6;
			const xNew = xPrev + ((kx0) + (2 * kx1) + (2 * kx2) + (kx3)) / 6;

			// double a = ((kv0) + (2 * kv1) + (2 * kv2) + (kv3)) / 6;
			const aNew = ((kv0) + (2 * kv1) + (2 * kv2) + (kv3)) / 6;

			const vNew = vPrev + aNew;

			// super.storePoint(nTick,t,x,v,a);
			const pointsToStore = { x : xNew, v : vNew, a: aNew, t : vars.t }
			calculator.historicalPoints[calculateStep] = pointsToStore;
		}

		// Get the latest stored.
		const latestPoints = calculator.historicalPoints[calculateStep];



		return { step: calculateStep,
			value: latestPoints.x,
			displacement: latestPoints.x,
			velocity: latestPoints.v,
			acceleration: latestPoints.a };


		//------------------------------------------------


		/*
		// 2019Jan08 Taylor Addressing the Euler self-references, as exemplified in iwp-packaged / Oscillations / damped-1.iwp
		// console.log("animation-calc:614> acceleration resultVariable: ", resultVariable );
		// console.log("animation-calc:615> acceleration calculator: ", calculator );
		// console.log("animation-calc:616> acceleration BEFORE vars: ", vars );
		// Enable the acceleration equations to self-reference our own ypos/yvel

		if ( resultVariable.endsWith(".y") ) {

			if ( typeof vars[ objectName ] !== "object" ) { vars[objectName] = {} }
			vars[ objectName ][ "ypos" ] = calculator.currentDisplacement
			vars[ objectName ][ "ydisp" ] = calculator.currentDisplacement
			vars[ objectName ][ "yvel" ] = calculator.currentVelocity

		} else if ( resultVariable.endsWith(".x") ) {

			if ( typeof vars[ objectName ] !== "object" ) { vars[objectName] = {} }
			vars[ objectName ][ "xpos" ] = calculator.currentDisplacement
			vars[ objectName ][ "xdisp" ] = calculator.currentDisplacement
			vars[ objectName ][ "xvel" ] = calculator.currentVelocity

		} else {
			console.log(__filename + ":60> Warning, Unknown resultVariable endsWith: " , resultVariable );
		}
		// console.log("animation-calc:636> acceleration AFTER vars: ", vars );




		try {
			// then calculate the acceleration
			// 2018Mar22 Fix to only apply the acceleration time component to the change in velocity.

			// console.log("iwp5:1088> Calculating acceleration via calculator: ", calculator, " at calcStep: " + calculateStep + " for vars: ", vars )
			acceleration = calculator.accelerationCompiled.evaluate(vars);

			if ( !isFinite(acceleration) ) {
				throw Error("Calculator.accelerationCompiled result is not finite, is: " + acceleration);
			}
			if ( isNaN(acceleration) ) {
				throw Error("Calculator.accelerationCompiled result is NaN");
			}

		} catch ( err ) {
			console.log("evaluateCalculator:1095> ERROR " + resultVariable + "> Unable to evaluate acceleration, setting to 0.  Calculator: ", err, calculator.equation, "Vars: ", JSON.stringify(vars) );
			if ( CONFIG_throw_acceleration_calculation_exceptions ) {
				throw err;
			}
		}



		if ( calculateStep === 0 ) {
			// For good measure, recalculate initials just in case other dependencies have changed.
			calculator.initialDisplacement = calcMathJs.evaluate(calculator.initialDisplacementCompiled, vars)
			calculator.initialVelocity = calcMathJs.evaluate(calculator.initialVelocityCompiled, vars)

			calculator.currentVelocity = calculator.initialVelocity;
			calculator.currentDisplacement = calculator.initialDisplacement; // no adjustment

			//console.log("animation-calc:672> Recalculating Euler Calc Step 0, calculator: " , calculator);
			//var breaker=1;

		} else if ( changeStep > 0 ) {

				// Expect the RK4 implemetnation needs to happen here!

			if ( acceleration != null ) {
				// Positive direction calcuation
				if ( calculateStep === 1 ) {
					// Special first frame consideration
					calculator.currentVelocity += acceleration * dt * 0.5;
				} else {
					calculator.currentVelocity += acceleration * dt;
				}
				calculator.currentDisplacement += calculator.currentVelocity * dt;
			}

		} else if ( changeStep < 0 ) {
			// Negative direction calculation
			if ( acceleration != null ) {
				calculator.currentVelocity -= acceleration * dt;
				calculator.currentDisplacement -= calculator.currentVelocity * dt;
			}
		} else {
			// No step direction
		}


		const breaker125=true
		// console.log("iwp5:1152> evaluateCalculator calculateStep: "+ calculateStep + "  changeStep: " + changeStep + " calculator.currentVelocity: " + calculator.currentVelocity + " calculator.currentDisplacement: " + calculator.currentDisplacement )
		//Return only value if just an output?
*/

		// return displacement.value;
	} catch ( err ) {
		if ( verbose ) {
			console.log("evaluateCalculator:375> Unable to evaluate calculator: ", err, calculator.equation, dt);
		}
		throw err;
	}
}


module.exports = {
	evaluateRK4Calculator: evaluateRK4Calculator
}
