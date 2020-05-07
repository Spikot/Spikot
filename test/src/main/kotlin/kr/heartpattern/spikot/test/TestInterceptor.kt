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
import kr.heartpattern.spikot.component.interceptor.Interceptor
import kr.heartpattern.spikot.component.scopes.ServerBeanInterceptor
import kr.heartpattern.spikot.component.scopes.ServerComponent

@Interceptor
object TestInterceptor : ServerBeanInterceptor {
    override fun preLoad(bean: ServerComponent) {
        println("pre load: $bean")
    }

    override fun postLoad(bean: ServerComponent) {
        println("post load: $bean")
    }

    override fun preEnable(bean: Component) {
        println("pre enable: $bean")
    }

    override fun postEnable(bean: Component) {
        println("post enable: $bean")
    }

    override fun preDisable(bean: Component) {
        println("pre disable: $bean")
    }

    override fun postDisable(bean: Component) {
        println("post disable: $bean")
    }
}