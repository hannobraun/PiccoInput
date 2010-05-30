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



import scala.collection.immutable.HashMap
import scala.collection.immutable.Map

import org.specs.Specification
import org.specs.mock.Mockito
import org.specs.runner.JUnit4



class KeyMapTest extends JUnit4( KeyMapSpec )



object KeyMapSpec extends Specification {

	object TestPlayer extends Player
	
	object Key1 extends Key
	object Key2 extends Key

	val keyCode1 = 5
	val keyCode2 = 10



	"KeyMap" should {
		"have no mappings initially." in {
			val keyMap = new KeyMap

			keyMap.mappings.isEmpty must beTrue
		}

		"add a new mapping to the provided map." in {
			val keyMap = ( new KeyMap ).addMapping( TestPlayer, Key1, keyCode1 )

			val mapping = keyMap.mappings( TestPlayer )
			mapping( Key1 ) must beEqualTo( keyCode1 )
		}

		"add a new mapping to the provided map, even if there already is a mapping." in {
			val keyMap = ( new KeyMap )
					.addMapping( TestPlayer, Key1, keyCode1 )
					.addMapping( TestPlayer, Key2, keyCode2 )

			val mapping = keyMap.mappings( TestPlayer )
			mapping( Key2 ) must beEqualTo( keyCode2 )
		}

		"not overwrite the first mapping if a second one is added." in {
			val keyMap = ( new KeyMap )
					.addMapping( TestPlayer, Key1, keyCode1 )
					.addMapping( TestPlayer, Key2, keyCode2 )

			val mapping = keyMap.mappings( TestPlayer )
			mapping( Key1 ) must beEqualTo( keyCode1 )
		}
	}
}
