
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.common.logging.FLog;

import java.util.ArrayList;

import com.facebook.react.bridge.Callback;

import android.media.AudioTrack;
import android.media.AudioManager;
import android.media.AudioFormat;
import android.util.Log;

public class RNAudioTrackModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private static AudioTrack audioTrack;
  private static final String TAG = "RNAudioTrack";

  public RNAudioTrackModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    if(audioTrack != null){
      audioTrack.stop();
      audioTrack.release();
      audioTrack = null;
    }
  }

  @Override
  public String getName() {
    return "RNAudioTrack";
  }

  @ReactMethod
  public void init(ReadableMap options) {
    int streamType =  AudioManager.STREAM_MUSIC;
    int sampleRateInHz = 7680;
    int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
    int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    int bufferSize = 7680;
    int mode = AudioTrack.MODE_STREAM;

    if (options.hasKey("bitsPerChannel")) {
      int bitsPerChannel = options.getInt("bitsPerChannel");

      if (bitsPerChannel == 8) {
        audioFormat = AudioFormat.ENCODING_PCM_8BIT;
      }
    }
    if (options.hasKey("channelsPerFrame")) {
      int channelsPerFrame = options.getInt("channelsPerFrame");

      // every other case --> CHANNEL_IN_MONO
      if (channelsPerFrame == 2) {
        channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
      }
    }
    if (options.hasKey("sampleRate")) {
      sampleRateInHz = options.getInt("sampleRate");
    }
    audioTrack = new AudioTrack(streamType,sampleRateInHz,channelConfig,audioFormat,bufferSize,mode);
  }

  @ReactMethod
  public void Play() {
    if(audioTrack != null){
      audioTrack.play();
    }
  }

  @ReactMethod
  public void Stop() {
    if(audioTrack != null){
      audioTrack.stop();
      audioTrack.release();
      audioTrack = null;
    }
  }
  @ReactMethod
  public void Pause() {
    if(audioTrack != null){
      audioTrack.pause();
    }
  }
  @ReactMethod
  public void SetVolume(float gain) {
    if(audioTrack != null){
      audioTrack.setVolume(gain);
    }
  }

    @ReactMethod
    public void Test(ReadableMap bytesArray, int offsetInBytes){

    }

    @ReactMethod
    public void Write(ReadableArray bytesArray,int offsetInBytes,int sizeInBytes){

        if(audioTrack != null && bytesArray != null){

          short[] buffer = new short[sizeInBytes];
          //FLog.e(TAG, "abc1.");
          for(int i = 0; i < sizeInBytes;i++){
            buffer[i] = (short)bytesArray.getInt(i);
          }

          audioTrack.write(buffer,offsetInBytes,sizeInBytes);
        }
    }

}
