# Spicy Spicy Spicy Spicy Spicy

I had this idea in the middle of last night, what if, as soon as you joined the server, you are set to half a heart and half a heart only?

So the premise is: you are set to half a heart, maximum. You gain half a heart every time you eat a unique piece of food. This does not cap at 20.

The idea comes from the Spice of Life: Carrot edition mod, but like, more extreme. 

Ok, it's only *vaguely* inspired. Have fun playing with half a heart until you can eat.

## Very Spicy Mode

Turning this mode on means your food list is *reset upon death*. Yep, back to half a heart.

## Commands and Perms

* `foodlist` to output your foodlist. The results are just formatted according to `Set.toString` so don't expect them to be too pretty
* `veryspicy <setting>` takes parameter true or false. Turns on the *very spicy hard mode*. Requires `spicyhearts.veryspicy` permission to run.
* `foodlist_all` to output lists for all players. Requires op.

## Changelog

### 1.1.0

* Fix plugin for 1.18 (there was a deprecated command that needed replacing)
* Added `foodlist` and `foodlist_all`