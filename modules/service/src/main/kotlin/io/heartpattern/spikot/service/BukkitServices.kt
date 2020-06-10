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

package io.heartpattern.spikot.service

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.ServicePriority
import kotlin.reflect.KClass

inline fun <reified T : Any> Plugin.registerService(implementation: T, priority: ServicePriority = ServicePriority.Normal) {
    registerService(T::class, implementation, priority)
}

fun <T : Any> Plugin.registerService(service: KClass<T>, implementation: T, priority: ServicePriority = ServicePriority.Normal) {
    Bukkit.getServicesManager().register(service.java, implementation, this, priority)
}

inline fun <reified T : Any> getRegisteredServiceProvider(): RegisteredServiceProvider<T>? {
    return getRegisteredServiceProvider(T::class)
}

fun <T : Any> getRegisteredServiceProvider(service: KClass<T>): RegisteredServiceProvider<T>? {
    return Bukkit.getServicesManager().getRegistration(service.java)
}

inline fun <reified T : Any> getRegisteredServiceProviders(): Collection<RegisteredServiceProvider<T>> {
    return getRegisteredServiceProviders(T::class)
}

fun <T : Any> getRegisteredServiceProviders(service: KClass<T>): Collection<RegisteredServiceProvider<T>> {
    return Bukkit.getServicesManager().getRegistrations(service.java)
}

inline fun <reified T : Any> getRegisteredService(): T? {
    return getRegisteredService(T::class)
}

fun <T : Any> getRegisteredService(service: KClass<T>): T? {
    return getRegisteredServiceProvider(service)?.provider
}