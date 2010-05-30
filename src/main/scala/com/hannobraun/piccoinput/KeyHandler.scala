/*
	Copyright (c) 2009 Hanno Braun <hanno@habraun.net>

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/



package com.hannobraun.piccoinput



import scala.collection.mutable._

import edu.umd.cs.piccolo.event._



/**
 * Handles keyboard input events from the Piccolo2D scene graph and provides methods, that allow you to
 * easily determine which keys are currently pressed. The intended usage is, to set Piccolo's keyboard focus
 * to this class and then query it every frame from the main loop.
 *
 * Example setup:
 * // Configure a KeyMap. Check KeyMap's documentation for information on how to do this.
 * val keyHandler = new KeyHandler(keyMap)
 * val canvas = PCanvas
 * canvas.getRoot.getDefaultInputManager.setKeyboardFocus(keyHandler)
 *
 * By default you'll have to click into the canvas before keyboard events will be delivered to the handler.
 * If you'd like to deliver keyboard events by default right after the application has been started, you'll
 * have to do the following:
 * // Make your window visible by calling .setVisible(true)
 * canvas.requestFocusInWindow // This will give you keyboard focus right from the start.
 *
 * Example usage:
 * while (true) {
 *     // Tell the input handler to execute an action if a specific player has pressed a specific key. Use
 *     // the player and key objects you defined when configuring the KeyMap.
 *     keyHandler.doIfPressed(player, key, () => {
 *         // do stuff here
 *     })
 *
 *     // Also works if your stuff returns something.
 *     val result = keyHandler.doIfPressed(player, key, () => {
 *         // do stuff that returns a result; your result will be wrapped in an Option
 *     })
 *
 *     // If you'd like to know if the player pressed one of several keys, do something like this.
 *     if (keyHandler.isPressed(player, someKey)
 *         // do stuff
 *     else if (keyHandler.isPressed(player, someOtherKey)
 *         // do other stuff
 *     else
 *         // do something else
 *
 *     // Do whatever else you need to do in your main loop
 * }
 */

class KeyHandler( keyMap: KeyMap ) {

	val handler = new PBasicInputEventHandler {

		val pressedKeys = new HashSet[Int]



		override def keyPressed( event: PInputEvent ) {
			pressedKeys.synchronized {
				pressedKeys.addEntry( event.getKeyCode )
			}
		}



		override def keyReleased( event: PInputEvent ) {
			pressedKeys.synchronized {
				pressedKeys.removeEntry( event.getKeyCode )
			}
		}
	}


	
	/**
	 * Returns true if a given key is pressed.
	 */

	def isPressed( player: Player, key: Key ): Boolean = {
		handler.pressedKeys.synchronized {
			val keyCode = keyMap.mappings( player )( key )
			handler.pressedKeys.contains( keyCode )
		}
	}



	/**
	 * Executes an action if a specific player has pressed a specific key.
	 * Returns Some(result) if the key is pressed, result being the result returned by your action. Returns
	 * None if the key is not pressed.
	 */

	def doIfPressed[R]( player: Player, key: Key, action: () => R ): Option[R] = {
		if ( isPressed( player, key ) )
			Some( action() )
		else
			None
	}
}
