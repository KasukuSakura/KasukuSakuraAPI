package io.github.kasukusakuraapi.servercommon

import io.github.kasukusakuraapi.servercommon.auth.AuthService
import io.github.kasukusakuraapi.servercommon.service.ControllerResponseToContentService
import io.github.kasukusakuraapi.servercommon.service.ExceptionToResp
import io.github.kasukusakuraapi.servercommon.service.ServiceSearcher
import io.github.kasukusakuraapi.servercommon.service.service
import io.github.kasukusakuraapi.servercommon.utils.KtorPipelineContext
import io.github.kasukusakuraapi.servercommon.utils.parseMappingInfo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.util.pipeline.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.kotlinFunction

class KasukuSakuraCommonServer(
    private val services: ServiceSearcher
) {

    val server = embeddedServer(CIO, environment = applicationEngineEnvironment {
        this.module { setup() }
        connector { config() }
    })

    private fun EngineConnectorBuilder.config() {
        this.host = "0.0.0.0"
        this.port = 18742
    }

    private fun Application.setup() {
        val auth = services.service<AuthService>()
        val etr = services.service<ExceptionToResp>()
        intercept(ApplicationCallPipeline.Call) {
            try {
                auth.run { doAuth() }
            } catch (e: Throwable) {
                call.respond(etr.toResp(e))
                finish()
            }
        }

        controllers.forEach { registerController(it, this) }
    }

    private val controllers = mutableListOf<Class<*>>()
    fun registerController(type: Class<*>) {
        controllers.add(type)
    }

    private fun registerController(type: Class<*>, application: Application) {
        val c = services.loadService(type)
        val etr = services.service<ExceptionToResp>()

        type.declaredMethods.forEach { method ->
            method.parseMappingInfo()?.let { info ->
                method.isAccessible = true
                val func = method.kotlinFunction!!
                val translator = services.service<ControllerResponseToContentService>()
                    .getTranslator(func, c)

                val x: List<((KtorPipelineContext) -> Any?)> = func.parameters.map { type ->
                    return@map when (type.kind) {
                        KParameter.Kind.INSTANCE -> {
                            ({ c })
                        }
                        else -> when (val jtype = (type.type.classifier as KClass<*>).java) {
                            ApplicationCall::class.java -> {
                                ({ it.context })
                            }
                            PipelineContext::class.java -> {
                                ({ it })
                            }
                            else -> {
                                ({ services.loadService(jtype) })
                            }
                        }
                    }
                }
                application.routing {
                    route(info.path) {
                        info.methods.map { HttpMethod.parse(it) }.forEach { m ->
                            method(m) {
                                handle {
                                    try {
                                        call.respond(translator(func.callSuspend(*Array(x.size) {
                                            x[it](this@handle)
                                        })))
                                    } catch (e: Throwable) {
                                        call.respond(etr.toResp(e))
                                    }
                                    finish()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
