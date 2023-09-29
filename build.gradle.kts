

// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false
    alias(libs.plugins.com.google.dagger.hilt) apply false
    alias(libs.plugins.spotless).apply(false)
}

true // Needed to make the Suppress annotation work for the plugins block
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

val ktlintVersion = "0.46.1"


subprojects {
    plugins.matching { anyPlugin -> anyPlugin is com.android.build.gradle.AppPlugin || anyPlugin is com.android.build.gradle.LibraryPlugin }.whenPluginAdded {
        apply(plugin = libs.plugins.spotless.get().pluginId)
        extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
            kotlin {
                target("**/*.kt")
                targetExclude("$buildDir/**/*.kt")
                ktlint(ktlintVersion)
                    .setEditorConfigPath("${project.rootDir}/spotless/.editorconfig").editorConfigOverride(
                        mapOf(
                            "ktlint_code_style" to "android",
                            "ij_kotlin_allow_trailing_comma" to true,
                            "trailing-comma-on-call-site" to false,
                            // These rules were introduced in ktlint 0.46.0 and should not be
                            // enabled without further discussion. They are disabled for now.
                            // See: https://github.com/pinterest/ktlint/releases/tag/0.46.0
                            "disabled_rules" to
                                    "filename," +
                                    "annotation,annotation-spacing," +
                                    "argument-list-wrapping," +
                                    "double-colon-spacing," +
                                    "enum-entry-name-case," +
                                    "trailing-comma-on-call-site," +
                                    "multiline-if-else," +
                                    "no-empty-first-line-in-method-block," +
                                    "package-name," +
                                    "trailing-comma," +
                                    "spacing-around-angle-brackets," +
                                    "spacing-between-declarations-with-annotations," +
                                    "spacing-between-declarations-with-comments," +
                                    "unary-op-spacing"
                        )
                    )
            }
            format("kts") {
                target("**/*.kts")
                targetExclude("**/build/**/*.kts")
            }
        }
    }
}
