object Versions {
    const val kotlin = "1.3.50"
    const val kotlinCore = "1.0.2"
    const val supportVersion = "1.1.0-beta01"
    const val designSupportVersion = "1.1.0-alpha03"
    const val constraintLayoutVersion = "2.0.0-alpha3"
    const val exifInterfaceVersion = "1.1.0-beta01"
    const val retrofitVersion = "2.4.0"
    const val okhttpVersion = "3.10.0"
    const val moshiVersion = "1.7.0"
    const val recyclerSelectionVersion = "1.0.0"
    const val gildeVersion = "4.8.0"
    const val picassoVersion = "2.71828"
    const val rxjavaVersion = "1.3.4"
    const val rxandroidVersion = "2.0.2"
    const val timberVersion = "4.7.1"
    const val firebaseCoreVersion = "17.0.1"
    const val firebaseAuthVersion = "18.1.0"
    const val firebaseMessagingVersion = "19.0.1"
    const val playServiceAuthVersion = "16.0.0"
    const val fbLoginVersion = "[5,6)"
    const val mockitoVersion = "2.19.0"
    const val mapVersion = "17.0.0"
    const val junitVersion = "4.12"
    const val androidxTestRunnerVersion = "1.1.2-alpha01"
    const val espressoVersion = "3.1.2-alpha01"
    const val gApisPeopleVersion = "v1-rev299-1.23.0"
    const val roomVersion = "2.1.0-rc01"
    const val lifeCycleVersion = "1.1.1"
    const val sdpVersion = "1.0.6"
    const val exoVersion = "2.9.6"
    const val pageIndicatorVersion = "0.2.8"
    const val sendBirdVersion = "3.0.92"
    const val videoTrimmerVersion = "1.0"
    const val galleryPickerVersion = "1.0.1"
    const val mp4ParserVersion = "1.1.20"
    const val edmoImageCropperVersion = "2.8.0"
    const val yanzaMediaPickerVersion = "2.1.3"
    const val crashlyticsVersion = "2.10.1"
    const val viewPager2Version = "1.0.0-beta02"
    const val imageZipperVersion = "1.3"
    const val findBugsVersion = "3.0.2"
}

object Packages {
    const val kotlinPackage = "org.jetbrains.kotlin"
    const val kotlinCore = "androidx.core"
    const val supportPackage = "androidx.appcompat"
    const val recyclerviewSelectionPackage = "androidx.recyclerview"
    const val designSupportPackage = "com.google.android.material"
    const val constraintLayoutPackage = "androidx.constraintlayout"
    const val exifInterfacePackage = "androidx.exifinterface"
    const val retrofitPackage = "com.squareup.retrofit2"
    const val okhttpPackage = "com.squareup.okhttp3"
    const val moshiPackage = "com.squareup.moshi"
    const val gildePackage = "com.github.bumptech.glide"
    const val picassoPackage = "com.squareup.picasso"
    const val rxjavaPackage = "io.reactivex"
    const val rxjava2Package = "io.reactivex.rxjava2"
    const val timberPackage = "com.jakewharton.timber"
    const val firebasePackage = "com.google.firebase"
    const val gmsPackage = "com.google.android.gms"
    const val gApiClientPackage = "com.google.api-client"
    const val gApisPackage = "com.google.apis"
    const val fbPackage = "com.facebook.android"
    const val mockitoPackage = "org.mockito"
    const val junitPackage = "junit"
    const val androidxTestPackage = "androidx.test"
    const val espressoPackage = "androidx.test.espresso"
    const val roomPackage = "androidx.room"
    const val lifeCyclePackage = "android.arch.lifecycle"
    const val sdpPackage = "com.intuit.sdp"
    const val exoPackage = "com.google.android.exoplayer"
    const val pageIndicatorPackage = "com.github.chahine"
    const val sendBirdPackage = "com.sendbird.sdk"
    const val videoTrimmerPackage = "life.knowledge4"
    const val galleryPickerPackage = "com.github.tizisdeepan"
    const val mp4ParserPackage = "com.googlecode.mp4parser"
    const val edmoImageCropperPackage = "com.theartofdev.edmodo"
    const val yanzaMediaPickerPackage = "com.yanzhenjie"
    const val crashlyticsPackage = "com.crashlytics.sdk.android"
    const val viewPager2Package = "androidx.viewpager2"
    const val imageZipperPackage = "com.github.amanjeetsingh150"
    const val findBugsPackage = "com.google.code.findbugs"
}

object KotlinDependencies {

    val kotlinStd =
        buildDependency(Packages.kotlinPackage, "kotlin-stdlib-jdk7", Versions.kotlin)

    val kotlinReflex =
        buildDependency(Packages.kotlinPackage, "kotlin-reflect", Versions.kotlin)
    val kotlinCore =
        buildDependency(Packages.kotlinCore, "core-ktx", Versions.kotlinCore)

    val gradle =
        buildDependency(Packages.kotlinPackage, "kotlin-gradle-plugin", Versions.kotlin)

}

object SupportDependencies {

    val design =
        buildDependency(Packages.designSupportPackage, "material", Versions.designSupportVersion)

    val appCompat =
        buildDependency(Packages.supportPackage, "appcompat", Versions.supportVersion)

    val constraintlayout = buildDependency(
        Packages.constraintLayoutPackage,
        "constraintlayout",
        Versions.constraintLayoutVersion
    )

    val findBugs =
        buildDependency(Packages.findBugsPackage, "jsr305", Versions.findBugsVersion)

    val recyclerviewSelection = buildDependency(
        Packages.recyclerviewSelectionPackage,
        "recyclerview-selection",
        Versions.recyclerSelectionVersion
    )

    val exifInterface = buildDependency(
        Packages.exifInterfacePackage,
        "exifinterface",
        Versions.exifInterfaceVersion
    )

    val viewpager2 =
        buildDependency(Packages.viewPager2Package, "viewpager2", Versions.viewPager2Version)

}

object LifeCycle {

    val extensions =
        buildDependency(Packages.lifeCyclePackage, "extensions", Versions.lifeCycleVersion)

    val viewmodel =
        buildDependency(Packages.lifeCyclePackage, "viewmodel", Versions.lifeCycleVersion)

}

object RetrofitDependencies {

    val retrofit =
        buildDependency(Packages.retrofitPackage, "retrofit", Versions.retrofitVersion)

    val gson =
        buildDependency(Packages.retrofitPackage, "converter-gson", Versions.retrofitVersion)

    val moshiConverter =
        buildDependency(Packages.retrofitPackage, "converter-moshi", Versions.retrofitVersion)

    val rxAdapterJava =
        buildDependency(Packages.retrofitPackage, "adapter-rxjava2", Versions.retrofitVersion)

    val okhttp =
        buildDependency(Packages.okhttpPackage, "logging-interceptor", Versions.okhttpVersion)

}

object RoomDependencies {

    val roomRunTime =
        buildDependency(Packages.roomPackage, "room-runtime", Versions.roomVersion)

    val roomKtx =
        buildDependency(Packages.roomPackage, "room-ktx", Versions.roomVersion)

    val roomCompiler =
        buildDependency(Packages.roomPackage, "room-compiler", Versions.roomVersion)

    val roomTesting =
        buildDependency(Packages.roomPackage, "room-testing", Versions.roomVersion)

    val roomRxJava =
        buildDependency(Packages.roomPackage, "room-rxjava2", Versions.roomVersion)

}

object GildeDependencies {

    val gilde =
        buildDependency(Packages.gildePackage, "glide", Versions.gildeVersion)

    val compiler =
        buildDependency(Packages.gildePackage, "compiler", Versions.gildeVersion)

}

object Rxjava2Dependencies {

    val rxjava2 =
        buildDependency(Packages.rxjava2Package, "rxjava", Versions.rxandroidVersion)

    val rxandroid =
        buildDependency(Packages.rxjava2Package, "rxandroid", Versions.rxandroidVersion)

}

object FirebaseDependencies {

    val core =
        buildDependency(Packages.firebasePackage, "firebase-core", Versions.firebaseCoreVersion)

    val auth =
        buildDependency(Packages.firebasePackage, "firebase-auth", Versions.firebaseAuthVersion)

    val messaging = buildDependency(
        Packages.firebasePackage,
        "firebase-messaging",
        Versions.firebaseMessagingVersion
    )

    val analytics = buildDependency(
        Packages.firebasePackage,
        "firebase-analytics",
        Versions.firebaseCoreVersion
    )

}

object GApiClientDependencies {

    val android =
        buildDependency(Packages.gApiClientPackage, "google-api-client-android", "1.22.0")

}

object GApisDependencies {

    val people = buildDependency(
        Packages.gApisPackage,
        "google-api-services-people",
        Versions.gApisPeopleVersion
    )

}

object GeneralDependencies {

    val moshi =
        buildDependency(Packages.moshiPackage, "moshi-kotlin", Versions.moshiVersion)

    val picasso =
        buildDependency(Packages.picassoPackage, "picasso", Versions.picassoVersion)

    val rxjava =
        buildDependency(Packages.rxjavaPackage, "rxjava", Versions.rxjavaVersion)

    val sdp =
        buildDependency(Packages.sdpPackage, "sdp-android", Versions.sdpVersion)

    val exoPlayer =
        buildDependency(Packages.exoPackage, "exoplayer", Versions.exoVersion)

    val pageIndicator = buildDependency(
        Packages.pageIndicatorPackage,
        "pageindicator",
        Versions.pageIndicatorVersion
    )

    val googleMap =
        buildDependency(Packages.gmsPackage, "play-services-maps", Versions.mapVersion)

    val googleLocation =
        buildDependency(Packages.gmsPackage, "play-services-location", Versions.mapVersion)

    val sendBird =
        buildDependency(Packages.sendBirdPackage, "sendbird-android-sdk", Versions.sendBirdVersion)

    val timber =
        buildDependency(Packages.timberPackage, "timber", Versions.timberVersion)

    val k4lVideoTrimmer = buildDependency(
        Packages.videoTrimmerPackage,
        "k4l-video-trimmer",
        Versions.videoTrimmerVersion
    )

    val galleryPicker = buildDependency(
        Packages.galleryPickerPackage,
        "gallerypicker",
        Versions.galleryPickerVersion
    )

    val imageZipper =
        buildDependency(Packages.imageZipperPackage, "ImageZipper", Versions.imageZipperVersion)

    val mp4Parser =
        buildDependency(Packages.mp4ParserPackage, "isoparser", Versions.mp4ParserVersion)

    val yanzhenjieMediaPicker =
        buildDependency(Packages.yanzaMediaPickerPackage, "album", Versions.yanzaMediaPickerVersion)

    val edmoImageCropper = buildDependency(
        Packages.edmoImageCropperPackage,
        "android-image-cropper",
        Versions.edmoImageCropperVersion
    )

    val crashLytics =
        buildDependency(Packages.crashlyticsPackage, "crashlytics", Versions.crashlyticsVersion)

    val gmsPlayServiceAuth =
        buildDependency(Packages.gmsPackage, "play-services-auth", Versions.playServiceAuthVersion)

    val fbLogin =
        buildDependency(Packages.fbPackage, "facebook-login", Versions.fbLoginVersion)

    val mockito =
        buildDependency(Packages.mockitoPackage, "mockito-core", Versions.mockitoVersion)

    val junit =
        buildDependency(Packages.junitPackage, "junit", Versions.junitVersion)

    val testRunner =
        buildDependency(Packages.androidxTestPackage, "runner", Versions.androidxTestRunnerVersion)

    val espresso =
        buildDependency(Packages.espressoPackage, "espresso-core", Versions.espressoVersion)

}

fun buildDependency(packageName: String, name: String, version: String): String {

    return "${packageName}:${name}:${version}"

}