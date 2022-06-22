package fr.univ_poitiers.dptinfo.algo3d;


import static android.view.MotionEvent.INVALID_POINTER_ID;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;


/**
 * Class to described the surface view. Mainly based on well-known code.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    /**
     * Renderer.
     */
    private final MyGLRenderer renderer;

    /**
     * Scene.
     */
    private final Scene scene;

    /**
     * Constructor.
     * @param context
     * @param scene
     */
    public MyGLSurfaceView(Context context, Scene scene) {
        super(context);
        this.scene = scene;

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        this.renderer = new MyGLRenderer(this, scene);
        setRenderer(this.renderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        //setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    /**
     * X coordinate of the origin of the left virtual joystick
     */
    private float leftJoystickOriginX;

    /**
     * y coordinate of the origin of the left virtual joystick
     */
    private float leftJoystickOriginY;

    /**
     * X coordinate of the origin of the right virtual joystick
     */
    private float rightJoystickOriginX;

    /**
     * y coordinate of the origin of the right virtual joystick
     */
    private float rightJoystickOriginY;

    /**
     * Left pointer id.
     * Represent the left virtual joystick id.
     */
    private int leftJoystickId = -1;

    /**
     * Right pointer id.
     * Represent the right virtual joystick id.
     */
    private int rightJoystickId = -1;

    /**
     * Active pointer id.
     */
    private int activePointerId = INVALID_POINTER_ID;

    /**
     * Method to emulate virtual joysticks when touching the screen.
     * Reset the position and rotation of the scene when more than two fingers touch the screen.
     * @param e
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        if (e.getPointerCount() > 2) {
            if (leftJoystickId != -1) {
                leftJoystickOriginX = e.getX(leftJoystickId);
                leftJoystickOriginY = e.getY(leftJoystickId);
            }
            if (rightJoystickId != -1) {
                rightJoystickOriginX = e.getX(rightJoystickId);
                rightJoystickOriginY = e.getY(rightJoystickId);
            }
            scene.ldx = 0;
            scene.ldy = 0;
            scene.rdx = 0;
            scene.rdy = 0;
            scene.anglex = 0;
            scene.angley = 0;
            scene.posx = 0;
            scene.posz = 0;
        } else {

            int screenWidth = getWidth();

            float x;
            float y;

            int action = e.getActionMasked();
            // Get the index of the pointer associated with the action.
            int pointerIndex;
            switch (action) {
                //First finger to touch the screen, will be saved as the activePointerId
                //If it is in the left half of the screen it will be considered as the left joystick
                //Otherwise, it is the right one.
                case MotionEvent.ACTION_DOWN:
                    pointerIndex = e.getActionIndex();
                    x = e.getX(pointerIndex);
                    y = e.getY(pointerIndex);

                    if (x < screenWidth / 2) {
                        leftJoystickOriginX = x;
                        leftJoystickOriginY = y;
                        leftJoystickId = e.getPointerId(0);
                    } else {
                        rightJoystickOriginX = x;
                        rightJoystickOriginY = y;
                        rightJoystickId = e.getPointerId(0);
                    }
                    // Save the ID of this pointer
                    activePointerId = e.getPointerId(0);
                    break;

                //Second finger to touch the screen
                //If it is in the left half of the screen it will be considered as the left joystick
                //Otherwise, it is the right one.
                //It will override the previous finger if it is on the same side of the screen.
                case MotionEvent.ACTION_POINTER_DOWN: {
                    pointerIndex = e.getActionIndex();
                    x = e.getX(pointerIndex);
                    y = e.getY(pointerIndex);
                    if (x < screenWidth / 2 && leftJoystickId == -1) {
                        leftJoystickOriginX = x;
                        leftJoystickOriginY = y;
                        leftJoystickId = pointerIndex;
                    } else if (x >= screenWidth / 2 && rightJoystickId == -1) {
                        rightJoystickOriginX = x;
                        rightJoystickOriginY = y;
                        rightJoystickId = pointerIndex;
                    }
                    break;
                }
                //if the left joystick move, the scene ldx and ldy will be modified, else if it is the right one, rdx and rdy will be modified.
                case MotionEvent.ACTION_MOVE: {
                    int pointerCount = e.getPointerCount();
                    for (int i = 0; i < pointerCount; ++i) {
                        pointerIndex = i;
                        x = e.getX(pointerIndex);
                        y = e.getY(pointerIndex);
                        int pointerId = e.getPointerId(pointerIndex);
                        if (pointerId == leftJoystickId) {
                            scene.ldx = x - leftJoystickOriginX;
                            scene.ldy = y - leftJoystickOriginY;
                        } else if (pointerId == rightJoystickId) {
                            scene.rdx = x - rightJoystickOriginX ;
                            scene.rdy = y - rightJoystickOriginY;

                        }
                    }
                    break;
                }
                //The last touching finger is raised. All joysticks are reset.
                case MotionEvent.ACTION_UP: {
                    activePointerId = INVALID_POINTER_ID;
                    leftJoystickId = -1;
                    rightJoystickId = -1;
                    scene.ldx = 0;
                    scene.ldy = 0;
                    scene.rdx = 0;
                    scene.rdy = 0;
                    break;
                }

                //A finger is raised but there is still one touching the screen.
                //If that finger, was the first one to touch the screen, then the last finger to have touch the screen will be stored as the activepointerid
                case MotionEvent.ACTION_POINTER_UP: {

                    pointerIndex = e.getActionIndex();
                    final int pointerId = e.getPointerId(pointerIndex);
                    if (pointerId == leftJoystickId) {
                        leftJoystickId = -1;
                        scene.ldx = 0;
                        scene.ldy = 0;
                    } else if (pointerId == rightJoystickId) {
                        rightJoystickId = -1;
                        scene.rdx = 0;
                        scene.rdy = 0;
                    }
                    if (pointerId == activePointerId) {
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        if (pointerId == leftJoystickId)
                            leftJoystickId = newPointerIndex;
                        else rightJoystickId = newPointerIndex;
                        activePointerId = e.getPointerId(newPointerIndex);
                    }
                    break;
                }
            }
        }
        this.requestRender();
        return true;
    }
}
