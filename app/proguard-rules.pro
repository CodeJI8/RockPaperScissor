# Proguard rules for RPS Duel

# Keep Jetpack Compose models & view models if needed
-keep class com.gamebd.rpsduel.model.** { *; }
-keep class com.gamebd.rpsduel.viewmodel.** { *; }

# Keep animated/serializable content
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
