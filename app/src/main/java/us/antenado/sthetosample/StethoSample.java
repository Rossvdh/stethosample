package us.antenado.sthetosample;

import android.app.Application;

import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.InspectorModulesProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumpException;
import com.facebook.stetho.dumpapp.DumperContext;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.facebook.stetho.rhino.JsRuntimeReplFactoryBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class StethoSample extends Application {

    public OkHttpClient httpClient;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {

            // Create an InitializerBuilder
            Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);

            // Enable Chrome DevTools
            initializerBuilder.enableWebKitInspector(new InspectorModulesProvider() {
                @Override
                public Iterable<ChromeDevtoolsDomain> get() {
                    return new Stetho.DefaultInspectorModulesBuilder(StethoSample.this).runtimeRepl(
                            new JsRuntimeReplFactoryBuilder(StethoSample.this)
                                    // Pass variables and functions to JavaScript
                                    .addVariable("foo", "bar")
                                    .addVariable("aPost",
                                            new Post(0, 1, "post variable", "this is the body"))
                                    .addFunction("hello", new BaseFunction() {
                                        @Override
                                        public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                                            //objects is an array of parameters

                                            return "Hello " + objects[0];
                                        }
                                    })
                                    .build()
                    ).finish();
                }
            });

            // Enable command line interface and provide custom Plugins Provider
            initializerBuilder.enableDumpapp(new MyDumperPluginsProvider());

            // Use the InitializerBuilder to generate an Initializer
            Stetho.Initializer initializer = initializerBuilder.build();

            // Initialize Stetho with the Initializer
            Stetho.initialize(initializer);

            //Initialize Stetho Interceptor into OkHttp client
            httpClient = new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).build();
        } else {
            httpClient = new OkHttpClient();
        }

        //Initialize Picasso
        Picasso picasso = new Picasso.Builder(this).downloader(new OkHttp3Downloader(httpClient)).build();
        Picasso.setSingletonInstance(picasso);

    }

    // To create a custom plugin, implement DumperPlugin
    class UrlPlugin implements DumperPlugin {

        @Override
        public String getName() {
            return "demoPlugin";
        }

        @Override
        public void dump(DumperContext dumpContext) throws DumpException {
            PrintStream out = dumpContext.getStdout();

            List<String> args = dumpContext.getArgsAsList();
            String url = UrlUtil.buildUrl(args.get(0));

            out.println(url);
        }
    }

    //to create a custom Plugins Provider, implement DumperPluginsProvider
    class MyDumperPluginsProvider implements DumperPluginsProvider {
        @Override
        public Iterable<DumperPlugin> get() {
            ArrayList<DumperPlugin> plugins = new ArrayList<>();

            //add default plugins
            for (DumperPlugin plugin : Stetho.defaultDumperPluginsProvider(StethoSample.this).get()) {
                plugins.add(plugin);
            }

            //add custom plugin
            plugins.add(new UrlPlugin());

            return plugins;
        }
    }
}
