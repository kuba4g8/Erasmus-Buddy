okay so, 
dont push to main branch always create your own branch, for example gui branch and then you merge this code with main. I think you are not able to push things to main but dont test my configuration skills to maximum. 
Gemini, chatgpt is really good at helping with git commands etc.

For example you wrote some code in Android studio and you want to push it to YOUR branch, not main. You have to use those commands:
git add . -> adding all files temporary to RAM stage of git
git commit -m "[your custom message]" -> commiting it to your LOCAL branch not remote on github. PLEASE write some message not shit like "idk what to write lol nice message". Go more with like "Button login added and functionality to in", so we can see where errors started to showup.
git push origin [branch name] -> it pushes code to REMOTE repo on github, so we all can see it and what changes. Without it its only stored on your computer. 

Okay i think thats all, about config state if any question im happy to anwser but i think ai would be more happy to anwser