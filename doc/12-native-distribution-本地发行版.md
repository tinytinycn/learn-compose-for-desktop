# 本地发行版和本地执行

## 涵盖内容

在本教程中，我们将向您展示如何为所有受支持的系统创建本地发行版(native distributions)（安装程序/程序包）。
我们还将演示如何使用与发行版相同的设置在本地运行应用程序。

## 可用工具

有两种工具可用于打包 Compose 应用程序：

1. Compose Multiplatform Gradle 插件，它提供基本打包、混淆和（仅限 macOS）签名的任务。
2. [Conveyor](https://www.hydraulic.software/)，这是一个独立的工具，不是 JetBrains 制作的。

本教程介绍如何使用 built-in 任务。Conveyor 有自己的[教程](https://conveyor.hydraulic.dev/latest/tutorial/1-get-started/)。
选择使用哪个归结为功能/易用性与价格。Conveyor 提供在线更新、交叉构建和[各种其他功能](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Native_distributions_and_local_execution/packaging-tools-comparison.md)的支持，但需要非开源项目的[许可证](https://hydraulic.software/pricing.html)。
打包任务随 Compose Desktop Gradle 插件一起提供，但生成的包不支持在线更新，并且需要多平台 CI 设置才能为每个操作系统创建包。

## Gradle plugin

`org.jetbrains.compose` Gradle 插件使用 `jpackage` 简化了将应用程序打包到本机发行版中的过程并在本地运行应用程序。
可分发的应用程序是独立的、可安装的二进制文件，其中包括它们需要的所有 Java 运行时组件，而不需要在目标系统上安装 JDK。

[Jlink](https://openjdk.java.net/jeps/282) 将负责只在可分发包中捆绑必要的 Java 模块以最小化包大小，
但您仍然必须配置 Gradle 插件以告诉它您需要哪些模块（请参阅 `Configuring included JDK modules` 部分）。

## 基本用法

插件中的基本配置单元是一个 `application`。`application` 为一组最终二进制文件定义共享配置。
换句话说，`application` DSL 中的允许您将一堆文件连同 JDK 分发包打包成一组各种格式（`.dmg`、`.deb`、`.msi`、`.exe` 等）的压缩二进制安装程序。

<details><summary>代码☕️</summary>

```kotlin
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(compose.desktop.currentOS)
}

compose.desktop {
    application {
        mainClass = "example.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
        }
    }
}
```

</details>

该插件创建以下任务：

- `package<FormatName>`（例如 `packageDmg` 或 `packageMsi`）用于将应用程序打包成相应的格式。请注意，目前没有可用的交叉编译支持，因此只能使用特定操作系统构建格式（例如，要构建 `.dmg`，您必须使用 macOS）。默认情况下会跳过与当前操作系统不兼容的任务。
- `packageDistributionForCurrentOS` 是一个生命周期任务，聚合了应用程序的所有包任务。
- `packageUberJarForCurrentOS` 用于创建单个 jar 文件，其中包含当前操作系统的所有依赖项。该任务从 M2 版本开始可用。该任务期望将 `compose.desktop.currentOS` 用作编译/实现/运行时依赖项。
- `run` 用于在本地运行应用程序。您需要定义一个 `mainClass` — class 的 fq-name，包含 `main` 函数。请注意，该运行会启动具有完整运行时的非打包 JVM 应用程序。这比创建具有最小运行时间的紧凑二进制映像更快、更容易调试。要运行最终的二进制图像，请改用 `runDistributable`。
- `createDistributable` 用于在不创建安装程序的情况下创建预打包的应用程序映像和最终应用程序映像。
- `runDistributable` 用于运行预打包的应用程序映像。

请注意，只有在脚本中使用 `application` block/property 时才会创建任务。

构建后，可以在 `${project.buildDir}/compose/binaries` 中找到输出二进制文件。

## 配置包含的 JDK 模块

Gradle 插件使用 [jlink](https://openjdk.java.net/jeps/282) 通过仅包含必要的 JDK 模块来最小化可分发的大小。

此时，Gradle 插件不会自动确定必要的 JDK 模块。未能提供必要的模块不会导致编译问题，但会在运行时导致 `ClassNotFoundException`。

如果您在运行打包应用程序或 `runDistributable` 任务时遇到 `ClassNotFoundException`，您可以使用 `modules` DSL 方法包含其他 JDK 模块（参见下面的示例）。

您可以手动或通过运行 `suggestModules` 任务来确定哪些模块是必需的。 `suggestModules` 使用 [jdeps](https://docs.oracle.com/javase/9/tools/jdeps.htm) 静态分析工具来确定可能缺少的模块。请注意，该工具的输出可能不完整或列出了不必要的模块。

如果可分发的大小不重要，您可以使用 `includeAllModules` DSL 属性简单地包含所有运行时模块作为替代方案。

```kotlin
compose.desktop {
    application {
        nativeDistributions {
            modules("java.sql")
            // alternatively: includeAllModules = true
        }
    }
}
```

## 可用格式

以下格式可用于支持的操作系统：

* macOS — `.dmg` (`TargetFormat.Dmg`), `.pkg` (`TargetFormat.Pkg`)
* Windows — `.exe` (`TargetFormat.Exe`), `.msi` (`TargetFormat.Msi`)
* Linux — `.deb` (`TargetFormat.Deb`), `.rpm` (`TargetFormat.Rpm`)

## macOS 上的签名和公证

默认情况下，Apple 不允许用户执行从 Internet 下载的未签名应用程序。尝试运行此类应用程序的用户将面临如下错误：

<details><summary>图片🖼️</summary>

![attrs-error](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Native_distributions_and_local_execution/attrs-error.png)

</details>

请参阅我们的[教程](https://github.com/JetBrains/compose-jb/blob/master/tutorials/Signing_and_notarization_on_macOS/README.md)，了解如何签署和公证您的申请。

## 指定包版本

您必须为本机分发包指定包版本。

您可以使用以下 DSL 属性（按优先级降序排列）：

- `nativeDistributions.<os>.<packageFormat>PackageVersion` 指定单个包格式的版本；
- `nativeDistributions.<os>.packageVersion` 指定单个目标操作系统的版本；
- `nativeDistributions.packageVersion` 指定所有包的版本；

对于 macOS，您还可以使用以下 DSL 属性指定构建版本（按优先级降序排列）：

- `nativeDistributions.macOS.<packageFormat>PackageBuildVersion` 指定单一包格式的构建版本；
- `nativeDistributions.macOS.packageBuildVersion` 为所有 macOS 包指定构建版本；

如果未指定构建版本，则使用包版本。有关 macOS 版本的更多信息，
请参阅 [CFBundleShortVersionString](https://developer.apple.com/documentation/bundleresources/information_property_list/cfbundleshortversionstring)（包版本）
和 [CFBundleVersion](https://developer.apple.com/documentation/bundleresources/information_property_list/cfbundleversion)（构建版本）。

<details><summary>代码☕️</summary>

```kotlin
compose.desktop {
    application {
        nativeDistributions {
            // a version for all distributables
            packageVersion = "..." 
            
            linux {
              // a version for all Linux distributables
              packageVersion = "..." 
              // a version only for the deb package
              debPackageVersion = "..." 
              // a version only for the rpm package
              rpmPackageVersion = "..." 
            }
            macOS {
              // a version for all macOS distributables
              packageVersion = "..."
              // a version only for the dmg package
              dmgPackageVersion = "..." 
              // a version only for the pkg package
              pkgPackageVersion = "..." 
              
              // a build version for all macOS distributables
              packageBuildVersion = "..."
              // a build version only for the dmg package
              dmgPackageBuildVersion = "..." 
              // a build version only for the pkg package
              pkgPackageBuildVersion = "..." 
            }
            windows {
              // a version for all Windows distributables
              packageVersion = "..."  
              // a version only for the msi package
              msiPackageVersion = "..."
              // a version only for the exe package
              exePackageVersion = "..." 
            }
        }
    }
}
```

</details>

版本必须遵循以下规则：
* For `dmg` and `pkg`:
    * The format is `MAJOR[.MINOR][.PATCH]`, where:
        * `MAJOR` is an integer > 0;
        * `MINOR` is an optional non-negative integer;
        * `PATCH` is an optional non-negative integer;
* For `msi` and `exe`:
    * The format is `MAJOR.MINOR.BUILD`, where:
        * `MAJOR` is a non-negative integer with a maximum value of 255;
        * `MINOR` is a non-negative integer with a maximum value of 255;
        * `BUILD` is a non-negative integer with a maximum value of 65535;
* For `deb`:
    * The format is `[EPOCH:]UPSTREAM_VERSION[-DEBIAN_REVISION]`, where:
        * `EPOCH` is an optional non-negative integer;
        * `UPSTREAM_VERSION`
            * may contain only alphanumerics and the characters `.`, `+`, `-`, `~`;
            * must start with a digit;
        * `DEBIAN_REVISION`
            * is optional;
            * may contain only alphanumerics and the characters `.`, `+`, `~`;
    * See [Debian documentation](https://www.debian.org/doc/debian-policy/ch-controlfields.html#version) for more details;
* For `rpm`:
    * A version must not contain the `-` (dash) character.

## 自定义 JDK 版本

该插件使用 `jpackage`，为此您应该至少使用 [JDK 15](https://openjdk.java.net/projects/jdk/15/)。确保您至少满足以下要求之一：

- `JAVA_HOME` 环境变量指向兼容的 JDK 版本。
- `javaHome` 通过 DSL 设置：

```kotlin
compose.desktop {
    application {
        javaHome = System.getenv("JDK_15")
    }
}
```

## 自定义输出目录

```kotlin
compose.desktop {
    application {
        nativeDistributions {
            outputBaseDir.set(project.buildDir.resolve("customOutputDir"))
        }
    }
}
```

## 自定义启动器 launcher

以下属性可用于自定义应用程序启动：

- `mainClass` — 类的完全限定名称，包含主要方法；
- `args` — 应用程序主要方法的参数；
- `jvmArgs` — 应用程序 JVM 的参数。

```kotlin
compose.desktop {
    application {
        mainClass = "MainKt"
        jvmArgs += listOf("-Xmx2G") 
        args += listOf("-customArgument") 
    }
}
```

## 自定义元数据 metadata

以下属性在 `nativeDistributions` DSL 块中可用：

- `packageName` — 应用程序的名称（默认值：Gradle 项目的名称）；
- `version` — 应用程序的版本（默认值：Gradle 项目的版本）；
- `description` — 应用程序的描述（默认值：无）；
- `copyright` — 应用程序的版权（默认值：无）；
- `vendor` — 应用程序的供应商（默认值：无）；
- `licenseFile` — 应用程序的许可证（默认值：无）。

```kotlin
compose.desktop {
    application {
        nativeDistributions {
            packageName = "ExampleApp"
            version = "0.1-SNAPSHOT"
            description = "Compose Example App"
            copyright = "© 2020 My Name. All rights reserved."
            vendor = "Example vendor"
            licenseFile.set(project.file("LICENSE.txt"))
        }
    }
}
```

## 打包 resources 

使用 Compose for Desktop 可以通过多种方式打包和加载资源。

### JVM resource loading

由于 Compose for Desktop 使用 JVM 平台，您可以使用 `java.lang.Class` API 从 jar 文件加载资源。
将文件放在 `src/main/resources` 下，然后使用 `Class::getResource` 或 `Class::getResourceAsStream` 访问它。

### 将文件添加到打包的应用程序

在某些情况下，从 jar 文件中放入和读取资源可能不方便。或者你可能想要包含一个目标特定的资产
（例如，一个文件，它只包含在 macOS 包中，但不包含在 Windows 包中）。

可以将 Compose Gradle 插件配置为将其他资源文件放在安装目录下。

为此，通过 DSL 指定根资源目录：

```kotlin
compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageVersion = "1.0.0"

            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
        }
    }
}
```

在上面的示例中，根资源目录设置为 <PROJECT_DIR>/resources。

Compose Gradle 插件将包含以下子目录下的所有文件：

1. `<RESOURCES_ROOT_DIR>/common` 中的文件将包含在所有包中。
2. `<RESOURCES_ROOT_DIR>/<OS_NAME>` 中的文件将仅包含在特定操作系统的包中。 `<OS_NAME>` 的可能值为：`windows`、`macos`、`linux`。
3. 来自 `<RESOURCES_ROOT_DIR>/<OS_NAME>-<ARCH_NAME>` 的文件将仅包含在特定操作系统和 CPU 架构组合的包中。 `<ARCH_NAME>` 的可能值为：`x64` 和 `arm64`。
例如，来自 `<RESOURCES_ROOT_DIR>/macos-arm64` 的文件将仅包含在为 `Apple Silicon Mac` 构建的包中。

可以通过 `compose.application.resources.dir` 系统属性访问包含的资源：

```kotlin
import java.io.File

val resourcesDir = File(System.getProperty("compose.application.resources.dir"))

fun main() {
    println(resourcesDir.resolve("resource.txt").readText())
}
```

##  自定义内容 content

当使用 `org.jetbrains.kotlin.jvm` 或 `org.jetbrains.kotlin.multiplatform` 插件时，插件可以自行配置。

- 对于 `org.jetbrains.kotlin.jvm`，该插件包含 `main` [source set](https://docs.gradle.org/current/userguide/java_plugin.html#source_sets) 中的内容。
- 使用 `org.jetbrains.kotlin.multiplatform`，插件包含单个 [jvm](https://kotlinlang.org/docs/reference/mpp-dsl-reference.html#targets) 目标的内容。
如果定义了多个 JVM 目标，默认配置将被禁用。在这种情况下，应该手动配置插件，或者指定一个目标target（见下文）。

如果默认配置不明确或不充分，可以配置插件：

- Using a Gradle [source set](https://docs.gradle.org/current/userguide/java_plugin.html#source_sets):

```kotlin
plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
} 

val customSourceSet = sourceSets.create("customSourceSet")
compose.desktop {
    application {
        from(customSourceSet)
    }
}
```

- Using a Kotlin JVM target:

```kotlin
plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
} 

kotlin {
    jvm("customJvmTarget") {}
}

compose.desktop {
    application {
        from(kotlin.targets["customJvmTarget"])
    }
}
```

- manually(手动)
  * `disableDefaultConfiguration` 可用于禁用默认配置；
  * `dependsOn` 可用于为所有插件的任务添加任务依赖；
  * `fromFiles` 可用于指定要包含的文件；
  * `mainJar` 文件属性可以指定为指向包含主类的 jar。

```kotlin
compose.desktop {
    application {
        disableDefaultConfiguration()
        fromFiles(project.fileTree("libs/") { include("**/*.jar") })
        mainJar.set(project.file("main.jar"))
        dependsOn("mainJarTask")
    }
}
```

## 特定于平台的选项

应使用相应的 DSL 块设置特定于平台的选项：

```kotlin
compose.desktop {
    application {
        nativeDistributions {
            macOS {
                // macOS specific options
            }
            windows {
                // Windows specific options
            }
            linux {
                // Linux specific options
            }
        }
    }
}
```

以下特定于平台的选项可用（**不建议**使用未记录的属性）：
* All platforms:
    * `iconFile.set(File("PATH_TO_ICON"))` — a path to a platform-specific icon for the application.
      (see the section `App icon` for details);
    * `packageVersion = "1.0.0"` — a platform-specific package version
      (see the section `Specifying package version` for details);
    * `installationPath = "PATH_TO_INSTALL_DIR"` — an absolute or relative path to the default installation directory;
        * On Windows `dirChooser = true` may be used to enable customizing the path during installation.
* Linux:
    * `packageName = "custom-package-name"` overrides the default application name;
    * `debMaintainer = "maintainer@example.com"` — an email of the deb package's maintainer;
    * `menuGroup = "my-example-menu-group"` — a menu group for the application;
    * `appRelease = "1"` — a release value for the rpm package, or a revision value for the deb package;
    * `appCategory = "CATEGORY"` — a group value for the rpm package, or a section value for the deb package;
    * `rpmLicenseType = "TYPE_OF_LICENSE"` — a type of license for the rpm package;
    * `debPackageVersion = "DEB_VERSION"` — a deb-specific package version
      (see the section `Specifying package version` for details);
    * `rpmPackageVersion = "RPM_VERSION"` — a rpm-specific package version
      (see the section `Specifying package version` for details);
* macOS:
    * `bundleID` — a unique application identifier;
        * May only contain alphanumeric characters (`A-Z`,`a-z`,`0-9`), hyphen (`-`) and period (`.`) characters;
        * Use of a reverse DNS notation (e.g. `com.mycompany.myapp`) is recommended;
    * `packageName` — a name of the application;
    * `dockName` — a name of the application displayed in the menu bar, the "About <App>" menu item, in the dock, etc.
      Equals to `packageName` by default.
    * `signing`, `notarization`, `provisioningProfile`, and `runtimeProvisioningProfile` — see
      [the corresponding tutorial](/tutorials/Signing_and_notarization_on_macOS/README.md)
      for details;
    * `appStore = true` — build and sign for the Apple App Store. Requires at least JDK 17;
    * `appCategory` — category of the app for the Apple App Store.
      Default value is `public.app-category.utilities` when building for the App Store, `Unknown` otherwise.
      See [LSApplicationCategoryType](https://developer.apple.com/documentation/bundleresources/information_property_list/lsapplicationcategorytype) for a list of valid categories;
    * `entitlementsFile.set(File("PATH_TO_ENTITLEMENTS"))` — a path to file containing entitlements to use when signing.
      When a custom file is provided, make sure to add the entitlements that are required for Java apps.
      See [sandbox.plist](https://github.com/openjdk/jdk/blob/master/src/jdk.jpackage/macosx/classes/jdk/jpackage/internal/resources/sandbox.plist) for the default file that is used when building for the App Store. It can be different depending on your JDK version.
      If no file is provided the default entitlements provided by jpackage are used.
      See [the corresponding tutorial](/tutorials/Signing_and_notarization_on_macOS/README.md#configuring-entitlements)
    * `runtimeEntitlementsFile.set(File("PATH_TO_RUNTIME_ENTITLEMENTS"))` — a path to file containing entitlements to use when signing the JVM runtime.
      When a custom file is provided, make sure to add the entitlements that are required for Java apps.
      See [sandbox.plist](https://github.com/openjdk/jdk/blob/master/src/jdk.jpackage/macosx/classes/jdk/jpackage/internal/resources/sandbox.plist) for the default file that is used when building for the App Store. It can be different depending on your JDK version.
      If no file is provided then `entitlementsFile` is used. If that was also not provided, the default entitlements provided by jpackage are used.
      See [the corresponding tutorial](/tutorials/Signing_and_notarization_on_macOS/README.md#configuring-entitlements)
    * `dmgPackageVersion = "DMG_VERSION"` — a dmg-specific package version
      (see the section `Specifying package version` for details);
    * `pkgPackageVersion = "PKG_VERSION"` — a pkg-specific package version
      (see the section `Specifying package version` for details);
    * `packageBuildVersion = "DMG_VERSION"` — a package build version
      (see the section `Specifying package version` for details);
    * `dmgPackageBuildVersion = "DMG_VERSION"` — a dmg-specific package build version
      (see the section `Specifying package version` for details);
    * `pkgPackageBuildVersion = "PKG_VERSION"` — a pkg-specific package build version
      (see the section `Specifying package version` for details);
    * `infoPlist` — see the section `Customizing Info.plist on macOS` for details;
* Windows:
    * `console = true` adds a console launcher for the application;
    * `dirChooser = true` enables customizing the installation path during installation;
    * `perUserInstall = true` enables installing the application on a per-user basis
    * `menuGroup = "start-menu-group"` adds the application to the specified Start menu group;
    * `upgradeUuid = "UUID"` — a unique ID, which enables users to update an app via installer,
      when an updated version is newer, than an installed version. The value must remain constant for a single application.
      See [the link](https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html)
      for details on generating a UUID.
    * `msiPackageVersion = "MSI_VERSION"` — a msi-specific package version
      (see the section `Specifying package version` for details);
    * `exePackageVersion = "EXE_VERSION"` — a pkg-specific package version
      (see the section `Specifying package version` for details);

## App icon

应用程序图标需要以特定于操作系统的格式提供：

- `.icns` for macOS;
- `.ico` for Windows;
- `.png` for Linux.

```kotlin
compose.desktop {
    application {
        nativeDistributions {
            macOS {
                iconFile.set(project.file("icon.icns"))
            }
            windows {
                iconFile.set(project.file("icon.ico"))
            }
            linux {
                iconFile.set(project.file("icon.png"))
            }
        }
    }
}
```

## 在 macOS 上自定义 Info.plist

我们的目标是通过声明式 DSL 支持重要的特定于平台的定制用例。然而，提供的 DSL 有时是不够的。
如果您需要指定未在 DSL 中建模的 `Info.plist` 值，您可以通过指定一段原始 XML 来解决，它将附加到应用程序的 `Info.plist`。

### 示例：深度链接(deep linking)到 macOS 应用程序

1. 指定自定义 URL 方案：

<details><summary>代码☕️</summary>

```kotlin
// build.gradle.kts
compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg)
            packageName = "Deep Linking Example App"
            macOS {
                bundleID = "org.jetbrains.compose.examples.deeplinking"
                infoPlist {
                    extraKeysRawXml = macExtraPlistKeys
                }
            }
        }
    }
}

val macExtraPlistKeys: String
    get() = """
      <key>CFBundleURLTypes</key>
      <array>
        <dict>
          <key>CFBundleURLName</key>
          <string>Example deep link</string>
          <key>CFBundleURLSchemes</key>
          <array>
            <string>compose</string>
          </array>
        </dict>
      </array>
    """
```

</details>

2. 使用 `java.awt.Desktop` 设置 URI 处理程序：

<details><summary>代码☕️</summary>

```kotlin
// src/main/main.kt

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.singleWindowApplication
import java.awt.Desktop

fun main() {
    var text by mutableStateOf("Hello, World!")

    try {
        Desktop.getDesktop().setOpenURIHandler { event ->
            text = "Open URI: " + event.uri
        }
    } catch (e: UnsupportedOperationException) {
        println("setOpenURIHandler is unsupported")
    }

    singleWindowApplication {
        MaterialTheme {
            Text(text)
        }
    }
}
```

</details>

3. run `./gradlew runDistributable`。
4. 像 `compose://foo/bar` 这样的 links 现在可以从浏览器重定向到您的应用程序。

## 缩小 & 混淆

从 1.2 开始，Compose Gradle 插件支持开箱即用的 [ProGuard](https://www.guardsquare.com/proguard)。
ProGuard 是由 [Guardsquare](https://www.guardsquare.com/) 开发的，
用于缩小和混淆的众所周知的[开源](https://github.com/Guardsquare/proguard)工具。

Gradle插件为每个对应的默认打包任务提供了一个 _release_ 任务：

| Default task (w/o ProGuard) | Release task (w. ProGuard)       | Description                                                               |
|-----------------------------|----------------------------------|---------------------------------------------------------------------------|
 | `createDistributable`       | `createReleaseDistributable`     | Creates an application image with bundled JDK & resources                 |
 | `runDistributable`          | `runReleaseDistributable`        | Runs an application image with bundled JDK & resources                    |
 | `run`                       | `runRelease`                     | Runs a non-packaged application `jar` using Gradle JDK                    |
 | `package<FORMAT_NAME>`      | `packageRelease<FORMAT_NAME>`    | Packages an application image into a `<FORMAT_NAME>` file                 |
 | `packageForCurrentOS`       | `packageReleaseForCurrentOS`     | Packages an application image into a format compatible with current OS    |
 | `notarize<FORMAT_NAME>`     | `notarizeRelease<FORMAT_NAME>`   | Uploads a `<FORMAT_NAME>` application image for notarization (macOS only) |
 | `checkNotarizationStatus`   | `checkReleaseNotarizationStatus` | Checks if notarization succeeded (macOS only)                             |

默认配置添加了一些 ProGuard 规则：

- 缩小应用程序图像，即删除未使用的类；
- `compose.desktop.application.mainClass` 用作入口点；
- 一些 `保留` 规则以避免破坏 Compose 运行时。

在许多情况下，获得缩小的 Compose 应用程序不需要任何额外的配置。
但是，有时 ProGuard 可能无法跟踪字节码中的某些用法（例如，如果通过反射使用类，则可能会发生这种情况）。
如果您遇到仅在 ProGuard 处理后才会发生的问题，您可能需要添加自定义规则。为此，通过 DSL 指定配置文件：

```kotlin
compose.desktop {
    application {
        buildTypes.release.proguard {
            configurationFiles.from(project.file("compose-desktop.pro"))
        }
    }
}
```

请参阅 Guardsquare 关于 ProGuard 规则和配置选项的[综合手册](https://www.guardsquare.com/manual/configuration/usage)。

默认情况下禁用混淆。要启用它，请通过 Gradle DSL 设置以下属性：

```kotlin
compose.desktop {
    application {
        buildTypes.release.proguard {
            obfuscate.set(true)
        }
    }
}
```