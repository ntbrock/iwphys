# Iwp6-order Module Design
2020Sep10 brockman

## Module.Exports

+ Function> reorderAnimationObjectsBySymbolicDependency(loop)

    Maybe change loop to be 'animation'?

## Unit Test Ideas

- Empty Animation
- Simple Animation
- Complex Animation
- Animation with a circular dependency


## Function List with Entry Points

Found that only one function had an entry point in the rest of this folder

- function reorderAnimationObjectsBySymbolicDependencyJsonStringify( unused ) 

- function reorderAnimationObjectsBySymbolicDependency(loop) 

```js
./iwp6-calc.js:1401:  animation.loop = reorderAnimationObjectsBySymbolicDependency(animation.loop);
```

- function equationRequires(eqn) 

- function calculatorRequires(calc) 

- function animationObjectRequires(object) 

- function animationObjectProvides(object) 

- function arrayToObject(arr) 

- function arrayUnique(arr) 

- function animationObjectReorder(loop) 

## Techniques
```shell script
cat iwp6-order.js|grep 'function '
egrep -rni animationObjectReorder .
```
