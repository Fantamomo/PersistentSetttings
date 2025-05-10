# Persistent Settings Library

A simple and efficient Kotlin library for Settings Handling with PersistentData

## Features

Many applications require a reliable way to store and retrieve settings. This library simplifies that process with
easy-to-use tools.

At its core, the library provides `Settings`, an abstract class for handling settings.

## 🚀 Installation

Clone the repository and install it to your local Maven repository:

```bash
# Clone the repository
git clone https://github.com/Fantamomo/PersistentSettings.git

# Enter the project directory
cd Persistent

# Install the library
mvn clean install
```

### ⚡ Add to your project

#### Maven

```xml

<dependency>
    <groupId>com.fantamomo.persistent</groupId>
    <artifactId>PersistentSettings</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
```

#### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenLocal()
}

dependencies {
    implementation("com.fantamomo.persistent:PersistentSettings:LATEST_VERSION")
}
```

Replace `LATEST_VERSION` with the latest version.

## Usage

### Creating a Settings Class

Create a simple **object** (recommended) that extends `at.leisner.persistent.settings.Settings`

```kotlin
object MySettings : Settings()
```

> Note: It is not bound to `object`, but they are required to have a name, so anonymous and locale are not allowed, this will lead to an exception. 

If we want to add an entry for saving a username, we write:

```kotlin
object MySettings : Settings() {
    val username = create("username", PersistentDataType.STRING) { "" }
}
```

This creates a new Entry with the name "username" the type is STRING, and `{ "" }` defines the default type.

`create` is defined as:

```kotlin
fun <P : Any, C : Any> create(name: String, type: PersistentDataType<P, C>, lazyDefault: () -> C): SettingsEntry<P, C>
```

To set and get values, use `get` and `set`:

```kotlin
val username: String = MySettings.username.get()
// and
MySettings.username.set(username)
```

Because this is a frequent task, you can use the `by` syntax:

```kotlin
object MySettings : Settings() {
    var username by create("username", PersistentDataType.STRING) { "" }
}
```

so you can write:

```kotlin
val username: String = MySettings.username
// and
MySettings.username = username
```

### Structuring Settings

When you have many settings, it can quickly become confusing:

```kotlin
object ConfusionSettings : Settings() {
    var username by create("username", PersistentDataType.STRING) { "" }
    var theme by create("theme", PersistentDataType.ENUM()) { Theme.LIGHT }
    var fontSize by create("font-size", PersistentDataType.INT) { 29 }
    // and so on
}
```

To improve structure, you can use nested objects:

```kotlin
object StructSettings : Settings() {
    object UserData : SettingsNode() {
        var username by create(PersistentDataType.STRING) { "" }
        var password by create(PersistentDataType.STRING) { "" }
    }
    object Appearance : SettingsNode() {
        var theme by create(PersistentDataType.ENUM()) { Theme.LIGHT }
        var fontSize by create("font-size", PersistentDataType.INT) { 29 }
    }
}
```

It is necessary that the inner objects extend `SettingsNode`. `SettingsNode` is declared as `inner`, so it can only be
used inside `Settings`.

In this environment, you can declare settings entries with the same name in different scopes. This is possible because
the actual name of an entry is its path plus its name. For example, `StructSettings.UserData.username` is saved as
`UserData/username`.

Notices that specifying the name of an entry is optional if it matches the property name. This is only supported with the
`by` syntax. If you try to call `get` or `set` on an entry without a name, an exception will be thrown.

**Info:** Nested objects in objects like `Appearance` are also supported.

**Note:** Data is only initialized and saved when it is first loaded or set. If `Appearance.theme` is never used, it
will not be saved. However, if it is accessed once, it will be loaded and saved persistently.

#### Saving and Loading Settings

To save settings, use the `saveTo` function. It is declared as `protected`:

```kotlin
object MySettings : Settings() {
    // ...

    fun save() {
        val stream = Path("settings.dat").outputStream()
        saveTo(stream)
    }
}
```

To load settings, use the `loadFrom` method. If you want to call when you initialize the object, you can pass it to the constructor of `Settings`
used:

```kotlin
object MySettings : Settings() {
    init {
        val stream = Path("settings.dat").inputStream()
        loadFrom(stream)
    }
}
```

This is the same as:

```kotlin
object MySettings : Settings(input = Path("settings.dat").inputStream()) {
    // ...
}
```

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE.txt) file for details.