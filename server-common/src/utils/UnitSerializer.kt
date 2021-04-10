package io.github.kasukusakuraapi.servercommon.utils

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object UnitSerializer : KSerializer<Unit> {
    override fun deserialize(decoder: Decoder) {
        decoder.decodeStructure(descriptor) {}
    }

    @OptIn(InternalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildSerialDescriptor("Unit", StructureKind.OBJECT) {
    }

    override fun serialize(encoder: Encoder, value: Unit) {
        encoder.encodeStructure(descriptor) {}
    }
}