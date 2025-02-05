// package com.fainthit.simplenativesocketio;

// import com.getcapacitor.Plugin;
// import com.getcapacitor.annotation.CapacitorPlugin;
// import com.getcapacitor.PluginCall;
// import com.getcapacitor.JSObject;
// import com.getcapacitor.PluginMethod;

// import org.json.JSONObject;

// import io.socket.client.IO;
// import io.socket.client.Socket;
// import io.socket.emitter.Emitter;

// // Imports for audio and vibration functionality
// import android.content.Context;
// import android.media.AudioManager;
// import android.media.MediaPlayer;
// import android.net.Uri;
// import android.os.Build;
// import android.os.VibrationEffect;
// import android.os.Vibrator;
// import android.util.Log;

// @CapacitorPlugin(name = "SimpleNativeSocketio")
// public class SimpleNativeSocketio extends Plugin {

//     private static final String TAG = "SimpleNativeSocketio";

//     private Socket mSocket;

//     /**
//      * startSocketConnection - Initializes the socket connection.
//      * @param call Options: { url: string }
//      */
//     @PluginMethod
//     public void startSocketConnection(PluginCall call) {
//         String url = call.getString("url");
//         if (url == null) {
//             call.reject("URL is required");
//             return;
//         }
//         try {
//             mSocket = IO.socket(url);
//         } catch (Exception e) {
//             call.reject("Error creating socket", e);
//             return;
//         }
//         registerDefaultEvents();
//         mSocket.connect();
//         call.resolve();
//         JSObject ret = new JSObject();
//         notifyListeners("test", ret);
//     }

//     /**
//      * registerDefaultEvents - Registers default socket events such as connect, disconnect, and connect_error.
//      */
//     private void registerDefaultEvents() {
//         mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//             @Override
//             public void call(Object... args) {
//                 JSObject ret = new JSObject();
//                 ret.put("event", "connect");
//                 ret.put("message", "Socket connected");
//                 Log.d(TAG, "SOCKET CONNECTED");
//                 notifyListeners("connect", ret);
//             }
//         });
//         mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
//             @Override
//             public void call(Object... args) {
//                 JSObject ret = new JSObject();
//                 ret.put("event", "disconnect");
//                 // ret.put("message", "Socket disconnected");
//                 if (args != null && args.length > 0) {
//                     ret.put("message", args[0].toString());
//                 }
//                 notifyListeners("disconnect", ret);
//             }
//         });
//         mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
//             @Override
//             public void call(Object... args) {
//                 JSObject ret = new JSObject();
//                 ret.put("event", "connect_error");
//                 if (args != null && args.length > 0) {
//                     ret.put("message", args[0].toString());
//                 }
//                 notifyListeners("connect_error", ret);
//             }
//         });
//     }

//     /**
//      * addSocketListener - Dynamically registers a listener for a specified event.
//      * @param call Options: { event: string }
//      */
//     @PluginMethod
//     public void addSocketListener(PluginCall call) {
//         String eventName = call.getString("event");
//         if (eventName == null) {
//             call.reject("Event name is required");
//             return;
//         }
//         if (mSocket != null) {
//             mSocket.on(eventName, new Emitter.Listener() {
//                 @Override
//                 public void call(Object... args) {
//                     JSObject ret = new JSObject();
//                     ret.put("event", eventName);
//                     if (args != null && args.length > 0) {
//                         try {
//                             JSONObject jsonData;
//                             if (args[0] instanceof JSONObject) {
//                                 jsonData = (JSONObject) args[0];
//                             } else {
//                                 jsonData = new JSONObject(args[0].toString());
//                             }
//                             ret.put("data", new JSObject(jsonData.toString()));
//                         } catch (Exception e) {
//                             ret.put("data", args[0].toString());
//                         }
//                     }
//                     // Process special actions if vibrationDuration or audioURI is present
//                     processSpecialActions(ret);
//                     notifyListeners(eventName, ret);
//                 }
//             });
//             call.resolve();
//         } else {
//             call.reject("Socket not initialized. Call startSocketConnection first.");
//         }
//     }

//     /**
//      * processSpecialActions - Executes vibration and audio playback if "vibrationDuration" or "audioURI" exists in the data.
//      */
//     private void processSpecialActions(JSObject ret) {
//         if (ret.has("data")) {
//             JSObject data = ret.getJSObject("data");
//             // Handle vibration if "vibrationDuration" exists
//             if (data.has("vibrationDuration")) {
//                 try {
//                     long duration = data.getLong("vibrationDuration");
//                     Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
//                     if (vibrator != null) {
//                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                             vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
//                         } else {
//                             vibrator.vibrate(duration);
//                         }
//                     }
//                 } catch (Exception e) {
//                     e.printStackTrace();
//                 }
//             }
//             // Handle audio playback if "audioURI" exists
//             if (data.has("audioURI")) {
//                 try {
//                     String audioURI = data.getString("audioURI");
//                     MediaPlayer mediaPlayer = new MediaPlayer();
//                     mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
//                     mediaPlayer.setDataSource(getContext(), Uri.parse(audioURI));
//                     mediaPlayer.prepare();
//                     mediaPlayer.start();
//                     mediaPlayer.setOnCompletionListener(mp -> mp.release());
//                 } catch (Exception e) {
//                     e.printStackTrace();
//                 }
//             }
//         }
//     }

//     /**
//      * emit - Emits an event with the provided JSON object data to the server.
//      * @param call Options: { event: string, data: object }
//      */
//     @PluginMethod
//     public void emit(PluginCall call) {
//         String event = call.getString("event");
//         JSObject data = call.getObject("data");
//         Log.d(TAG, "EMIT REQUEST: " + event);
//         if (mSocket != null && event != null && data != null) {
//             // Emit data as a JSON object (native socket.io-client-java handles serialization)
//             mSocket.emit(event, data);
//             call.resolve();
//         } else {
//             call.reject("Socket not connected, or event/data is null");
//         }
//     }

//     /**
//      * onSocketEvent - Initializes event reception on the JS side (keepAlive).
//      */
//     @PluginMethod
//     public void onSocketEvent(PluginCall call) {
//         call.setKeepAlive(true);
//         call.resolve();
//     }
// }
