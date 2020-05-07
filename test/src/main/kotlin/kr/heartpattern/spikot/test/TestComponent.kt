/*
 * Copyright 2020 Spikot project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package kr.heartpattern.spikot.test

import kr.heartpattern.spikot.component.Component
import kr.heartpattern.spikot.component.Priority
import kr.heartpattern.spikot.component.conditionals.Disable
import kr.heartpattern.spikot.component.scopes.ServerComponent
import kr.heartpattern.spikot.component.scopes.ServerScope

@ServerScope
@Priority(5)
object TestComponent : ServerComponent() {
    override fun onLoad() {
        println("load test: $plugin")
    }

    override fun onEnable() {
        println("enable test")
    }

    override fun onDisable() {
        println("disable test")
    }

    override fun toString(): String {
        return "Test"
    }
}

@ServerScope
class Test2Component : Component() {
    override fun onEnable() {
        println("enable test2")
    }

    override fun onDisable() {
        println("disable test2")
    }

    override fun toString(): String {
        return "Test2"
    }
}

@ServerScope
@Disable
class Test3Component : Component() {
    override fun onEnable() {
        println("enable test3")
    }

    override fun onDisable() {
        println("disable test3")
    }

    override fun toString(): String {
        return "Test3"
    }
}