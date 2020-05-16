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

package kr.heartpattern.spikot.event

import kr.heartpattern.spikot.component.Component
import kr.heartpattern.spikot.component.bean.BeanInstance
import kr.heartpattern.spikot.component.interceptor.BeanInterceptorComponent
import kr.heartpattern.spikot.component.interceptor.Interceptor
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList

@Interceptor
private object EventInterceptor : BeanInterceptorComponent() {
    override fun postEnable(bean: BeanInstance<Component>) {
        Bukkit.getPluginManager().registerEvents(bean.instance, bean.plugin)
    }

    override fun postDisable(bean: BeanInstance<Component>) {
        HandlerList.unregisterAll(bean.instance)
    }
}