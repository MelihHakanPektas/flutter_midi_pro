package com.melihhakanpektas.flutter_midi_pro;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** FlutterMidiProPlugin */
public class FlutterMidiProPlugin implements FlutterPlugin, MethodCallHandler {
  // Used to load the 'native-lib' library on application startup.
  static {
    System.loadLibrary("native-lib");
  }
  /// The MethodChannel that will the communication between Flutter and native
  /// Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine
  /// and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_midi_pro");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("load_soundfont")) {
      String path = null;
      try {
        path = copyAssetToTmpFile("sndfnt.sf2");
        loadSoundfont(path);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      result.success("Soundfont loaded successfully");

    } else if (call.method.equals("play_midi_note")) {
      int note = call.argument("note");
      int velocity = call.argument("velocity");
      System.out.print("PLAYING NOTE");
      playNote(0, note, velocity);

    } else if (call.method.equals("stop_midi_note")) {
      int note = call.argument("note");
      int velocity = call.argument("velocity");
      System.out.print("STOPPED NOTE");
      stopNote(0, note);

    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  private String copyAssetToTmpFile(String fileName) throws IOException {
    Context context = FlutterMidiProPlugin.this.context; // Pluginin bulunduğu context'i alır
    try (InputStream is = context.getAssets().open(fileName)) {
      String tempFileName = "tmp_" + fileName;
      try (FileOutputStream fos = context.openFileOutput(tempFileName, Context.MODE_PRIVATE)) {
        int bytes_read;
        byte[] buffer = new byte[4096];
        while ((bytes_read = is.read(buffer)) != -1) {
          fos.write(buffer, 0, bytes_read);
        }
      }
      return context.getFilesDir() + "/" + tempFileName;
    } catch (IOException e) {
      throw e;
    }
  }

  /**
   * A native method that is implemented by the 'native-lib' native library,
   * which is packaged with this application.
   */
  public native void loadSoundfont(String soundfontPath);

  public native void playNote(int channel, int note, int velocity);

  public native void stopNote(int channel, int note);
}
