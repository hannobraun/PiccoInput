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



import edu.umd.cs.piccolo.event.PInputEvent
import org.specs.Specification
import org.specs.mock.Mockito
import org.specs.runner.JUnit4



class KeyHandlerTest extends JUnit4( KeyHandlerSpec )



object KeyHandlerSpec extends Specification with Mockito {
	
	object TestPlayer extends Player
	object TestKey extends Key

	val keyCode = 5



	"KeyHandler.isPressed" should {
		"return false if no events have been received." in {
			val keyMap = mock[ KeyMap ]
			val keyHandler = new KeyHandler( keyMap )

			val mappings = mock[ Map[ Player, Map[ Key, Int ] ] ]
			val mapping = mock[ Map[ Key, Int] ]
			keyMap.mappings returns mappings
			mappings( TestPlayer ) returns mapping
			mapping( TestKey ) returns keyCode

			val event = mock[ PInputEvent ]
			event.getKeyCode returns keyCode

			keyHandler.isPressed( TestPlayer, TestKey ) must beFalse
		}

		"return true if a pressed event has been received." in {
			val keyMap = mock[ KeyMap ]
			val keyHandler = new KeyHandler( keyMap )

			val mappings = mock[ Map[ Player, Map[ Key, Int ] ] ]
			val mapping = mock[ Map[ Key, Int] ]
			keyMap.mappings returns mappings
			mappings( TestPlayer ) returns mapping
			mapping( TestKey ) returns keyCode

			val event = mock[ PInputEvent ]
			event.getKeyCode returns keyCode

			keyHandler.handler.keyPressed( event )

			keyHandler.isPressed( TestPlayer, TestKey) must beTrue
		}

		"return false if a pressed event and a released event have been received." in {
			val keyMap = mock[ KeyMap ]
			val keyHandler = new KeyHandler( keyMap )

			val mappings = mock[ Map[ Player, Map[ Key, Int ] ] ]
			val mapping = mock[ Map[ Key, Int] ]
			keyMap.mappings returns mappings
			mappings( TestPlayer ) returns mapping
			mapping( TestKey ) returns keyCode

			val event = mock[ PInputEvent ]
			event.getKeyCode returns keyCode

			keyHandler.handler.keyPressed( event )
			keyHandler.handler.keyReleased( event )

			keyHandler.isPressed( TestPlayer, TestKey ) must beFalse
		}
	}



	"KeyHandler.doIfPressed" should {
		"not perform the action and return None if no events have been received." in {
			val keyMap = mock[ KeyMap ]
			val keyHandler = new KeyHandler( keyMap )

			val mappings = mock[ Map[ Player, Map[ Key, Int ] ] ]
			val mapping = mock[ Map[ Key, Int] ]
			keyMap.mappings returns mappings
			mappings( TestPlayer ) returns mapping
			mapping( TestKey ) returns keyCode

			val event = mock[ PInputEvent ]
			event.getKeyCode returns keyCode

			val r = 8
			val action = mock[ Function0[ Int ] ]
			action() returns r

			val result = keyHandler.doIfPressed( TestPlayer, TestKey, action )

			action() wasnt called
			result must beEqualTo( None )
		}

		"perform the action and return its result if a pressed event has been received." in {
			val keyMap = mock[ KeyMap ]
			val keyHandler = new KeyHandler( keyMap )

			val mappings = mock[ Map[ Player, Map[ Key, Int ] ] ]
			val mapping = mock[ Map[ Key, Int] ]
			keyMap.mappings returns mappings
			mappings( TestPlayer ) returns mapping
			mapping( TestKey ) returns keyCode

			val event = mock[ PInputEvent ]
			event.getKeyCode returns keyCode

			val r = 8
			val action = mock[ Function0[ Int ] ]
			action() returns r

			keyHandler.handler.keyPressed( event )
			val result = keyHandler.doIfPressed( TestPlayer, TestKey, action )

			action() was called
			result must beEqualTo( Some( r ) )
		}
	}
}
