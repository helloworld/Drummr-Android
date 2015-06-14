package thupukari.com.drummr;
import android.app.Application;
import com.facebook.stetho.Stetho;

/**
 * Created by Sashank on 6/1/15.
 */
public class Drummr extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
