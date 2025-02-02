import Foundation

@objc public class SimpleNativeSocketio: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
