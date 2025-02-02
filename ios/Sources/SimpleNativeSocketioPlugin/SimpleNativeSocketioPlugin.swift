import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(SimpleNativeSocketioPlugin)
public class SimpleNativeSocketioPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "SimpleNativeSocketioPlugin"
    public let jsName = "SimpleNativeSocketio"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "echo", returnType: CAPPluginReturnPromise)
    ]
    private let implementation = SimpleNativeSocketio()

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.resolve([
            "value": implementation.echo(value)
        ])
    }
}
