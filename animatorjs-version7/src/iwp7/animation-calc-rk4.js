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

const CONFIG_throw_acceleration_calculation_exceptions = true;



function evaluateRK4Calculator( resultVariable, calculator, calculateStep, changeStep, vars, verbose, objectName ) {

	const dt = vars["delta_t"];

	try {

		// 2019-Jan-08 Euler Self-referential, fixes for damped-1.iwp to prevent exceptions with new iwp6 javascript.

		// 2016-Sep-23 For Euler V5, store the cache of current displacement and velocity IN calculator.
		// An enhancement would be to expose these values out as complex return ttypes of this function,
		// so that they could he historically archived in the variable step array.
		//  IIRC, IWPv4, these were available as xdisp, xvel, xaccell on solids, for example.
		// Today, in IWP5, our return structure out of thi sufnction is a single double value.

		if ( ! dt ) {
			throw Error("No variable 'delta_t' at step: " + calculateStep + " in vars: " + JSON.stringify(vars));
		}

		let acceleration = null;

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

		return { step: calculateStep,
			value: calculator.currentDisplacement,
			displacement: calculator.currentDisplacement,
			velocity: calculator.currentVelocity,
			acceleration: acceleration };

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
