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
import kr.heartpattern.spikot.component.bean.BeanInstance
import kr.heartpattern.spikot.component.interceptor.Interceptor
import kr.heartpattern.spikot.component.scopes.server.ServerBeanInstance
import kr.heartpattern.spikot.component.scopes.server.ServerBeanInterceptor

@Interceptor
object TestInterceptor : ServerBeanInterceptor {
    override fun preLoad(bean: ServerBeanInstance) {
        println("pre load: ${bean.instance}")
    }

    override fun postLoad(bean: ServerBeanInstance) {
        println("post load: ${bean.instance}")
    }

    override fun preEnable(bean: BeanInstance<Component>) {
        println("pre enable: ${bean.instance}")
    }

    override fun postEnable(bean: BeanInstance<Component>) {
        println("post enable: ${bean.instance}")
    }

    override fun preDisable(bean: BeanInstance<Component>) {
        println("pre disable: ${bean.instance}")
    }

    override fun postDisable(bean: BeanInstance<Component>) {
        println("post disable: ${bean.instance}")
    }
}