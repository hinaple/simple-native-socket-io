// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "SimpleNativeSocketIo",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "SimpleNativeSocketIo",
            targets: ["SimpleNativeSocketioPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "SimpleNativeSocketioPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/SimpleNativeSocketioPlugin"),
        .testTarget(
            name: "SimpleNativeSocketioPluginTests",
            dependencies: ["SimpleNativeSocketioPlugin"],
            path: "ios/Tests/SimpleNativeSocketioPluginTests")
    ]
)