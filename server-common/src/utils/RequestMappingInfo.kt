package io.github.kasukusakuraapi.servercommon.utils

import io.github.kasukusakuraapi.servercommon.annotations.RequestMapping
import java.lang.reflect.Method


fun Method.parseMappingInfo(): RequestMappingInfo? {
    val top = declaringClass.getDeclaredAnnotation(RequestMapping::class.java)
    val met = getDeclaredAnnotation(RequestMapping::class.java)
    if (top == null && met == null) return null
    return RequestMappingInfo(
        "${top?.value ?: ""}${met?.value ?: ""}",
        met?.methods?.toList() ?: emptyList()
    )
}

data class RequestMappingInfo(
    val path: String,
    val methods: List<String>
)