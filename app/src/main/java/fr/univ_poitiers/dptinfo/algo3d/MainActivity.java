package fr.univ_poitiers.dptinfo.algo3d;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import fr.univ_poitiers.dptinfo.algo3d.gameobject.Ball;

/**
 * Class to described the only activity of the application
 *
 * @author Philippe Meseure
 * @version 1.0
 */
public class MainActivity extends Activity {
    /**
     * TAG for logging errors
     */
    private static final String LOG_TAG = "Applications 3D";
    /**
     * View where OpenGL can draw
     */
    private MyGLSurfaceView glview;
    /**
     * Reference to the Scene environment
     */
    private Scene scene;

    /**
     * Creation of the surface view and the scene
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        log("Starting " + getString(R.string.app_name) + "...");

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        this.scene = new Scene(this);
        this.glview = new MyGLSurfaceView(this, this.scene);
        setContentView(this.glview);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * Pause of the application. To do...
     */
    @Override
    protected void onPause() {
        super.onPause();
        log("Pausing " + getString(R.string.app_name) + ".");
        Ball.onPause();
        //this.glview.setPreserveEGLContextOnPause(true); //autre solution, conserver le contexte opengl quand on quitte l'appli
        this.glview.onPause();
    }

    /**
     * Resume of the application. To do...
     */
    @Override
    protected void onResume() {
        super.onResume();
        log("Resuming " + getString(R.string.app_name) + ".");
        glview.onResume();
    }

    /**
     * End of the application.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        scene.finish();
    }

    /**
     * Method used to send message to the log console
     *
     * @param message message to display in log
     */
    static public void log(String message) {
        Log.e(LOG_TAG, message);
    }
}

