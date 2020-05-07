# How to contribute

First of all, thanks for taking time to contribute and show interest to spikot. Following is guidelines to contribute to spikot.

## Did you find a bug?

- Open new github issue with detail description of bug.
- Describe how to reproduce a bug.
- Attach stack trace or thread dump if presented.

## Did you fix a bug?

- Open new github pull request for bug.
- Describe detail description of bug. Include relevant issue number if applicable.
- Before make pull request, please read Contributing to code section of this document.

## Did you have feature suggestion?

- Open new github issue with detail description of feature.
- Add some example code about the feature.

## Did you add a new feature?

- Before writing code for a new feature, open github issue or send mail to jun2620354@kaist.ac.kr. After positive feedback, start writing code for feature.

- Open new github pull request for a new feature.
- Before make pull request, please read Contributing to code section of this document.

# Contributing to code

Spikot follow the coding convention of kotlin language. Following link describe detail about this style.

https://kotlinlang.org/docs/reference/coding-conventions.html

Spikot use 4 space for indentation. Do not use or reformat code to tab.

Spikot is focus on extendability and customizablity. Spikot user should always have maximum ability to configure and personalize their plugin. To achieve this, spikot does not hard code anything which directly interact to player. For example, rather than sending an error message directly to player, provide error handling callback. Rather than hard coding the sealed class or enum, provide companion object and extension property for companion object. Please design structure as flexible as possible to give more option to spikot user.