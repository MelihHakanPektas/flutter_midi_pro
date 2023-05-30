#include <jni.h>
#include <string>
#include <fluidsynth.h>
#include <unistd.h>

fluid_settings_t *settings = new_fluid_settings();
fluid_synth_t *synth = new_fluid_synth(settings);
fluid_audio_driver_t *adriver = new_fluid_audio_driver(settings, synth);
extern "C" JNIEXPORT void JNICALL Java_com_example_fluidsynthandroidhelloworld_MainActivity_loadSoundfont(JNIEnv* env, jobject, jstring jSoundfontPath) {
    fluid_settings_setstr (settings, "audio.oboe.performance-mode", "LowLatency");
    const char* soundfontPath = env->GetStringUTFChars(jSoundfontPath, nullptr);
    fluid_synth_sfload(synth, soundfontPath, 1);
}

extern "C" JNIEXPORT void JNICALL Java_com_example_fluidsynthandroidhelloworld_MainActivity_playNote(JNIEnv* env, jobject, jint channel, jint key, jint velocity) {
    fluid_synth_noteon(synth,channel,key,velocity);
}

extern "C" JNIEXPORT void JNICALL Java_com_example_fluidsynthandroidhelloworld_MainActivity_stopNote(JNIEnv* env, jobject, jint channel, jint key) {
    fluid_synth_noteoff(synth,channel,key);
}
