package io.codeall9.tictactoe

import com.android.build.api.dsl.ManagedDevices
import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.api.dsl.TestOptions
import org.gradle.kotlin.dsl.invoke
import java.util.Locale

internal fun TestOptions.setupGradleManagedDevices() {
    managedDevices {
        createDevices(
            DeviceConfig("Pixel 4", 30, "aosp-atd"),
            DeviceConfig("Pixel 6", 31, "aosp"),
            DeviceConfig("Pixel C", 30, "aosp-atd"),
        )
    }
}

private fun ManagedDevices.createDevices(vararg configs: DeviceConfig) {
    devices {
        configs.forEach { config ->
            maybeCreate(config.taskName, ManagedVirtualDevice::class.java).apply {
                device = config.device
                apiLevel = config.apiLevel
                systemImageSource = config.systemImageSource
            }
        }
    }
}

private data class DeviceConfig(
    val device: String,
    val apiLevel: Int,
    val systemImageSource: String,
) {
    val taskName = buildString {
        append(device.lowercase(Locale.ROOT).replace(" ", ""))
        append("api")
        append(apiLevel.toString())
        append(systemImageSource.replace("-", ""))
    }
}
